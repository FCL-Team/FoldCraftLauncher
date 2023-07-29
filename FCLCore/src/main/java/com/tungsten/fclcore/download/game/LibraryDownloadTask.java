package com.tungsten.fclcore.download.game;

import static com.tungsten.fclcore.util.Logging.LOG;

import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.download.AbstractDependencyManager;
import com.tungsten.fclcore.download.ArtifactMalformedException;
import com.tungsten.fclcore.download.DefaultCacheRepository;
import com.tungsten.fclcore.game.Library;
import com.tungsten.fclcore.task.DownloadException;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.DigestUtils;
import com.tungsten.fclcore.util.Pack200Utils;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.io.IOUtils;
import com.tungsten.fclcore.util.io.NetworkUtils;

import org.tukaani.xz.XZInputStream;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.logging.Level;

public class LibraryDownloadTask extends Task<Void> {
    private FileDownloadTask task;
    protected final File jar;
    protected final DefaultCacheRepository cacheRepository;
    protected final AbstractDependencyManager dependencyManager;
    private final File xzFile;
    protected final Library library;
    protected final String url;
    protected boolean xz;
    private final Library originalLibrary;
    private boolean cached = false;

    public LibraryDownloadTask(AbstractDependencyManager dependencyManager, File file, Library library) {
        this.dependencyManager = dependencyManager;
        this.originalLibrary = library;

        setSignificance(TaskSignificance.MODERATE);

        if (library.is("net.minecraftforge", "forge"))
            library = library.setClassifier("universal");

        this.library = library;
        this.cacheRepository = dependencyManager.getCacheRepository();

        url = library.getDownload().getUrl();
        jar = file;

        xzFile = new File(file.getAbsoluteFile().getParentFile(), file.getName() + ".pack.xz");
    }

    @Override
    public Collection<Task<?>> getDependents() {
        if (cached) return Collections.emptyList();
        else return Collections.singleton(task);
    }

    @Override
    public boolean isRelyingOnDependents() {
        return false;
    }

    @Override
    public void execute() throws Exception {
        if (cached) return;

        if (!isDependentsSucceeded()) {
            // Since FileDownloadTask wraps the actual exception with DownloadException.
            // We should extract it letting the error message clearer.
            Exception t = task.getException();
            if (t instanceof DownloadException)
                throw new LibraryDownloadException(library, t.getCause());
            else if (t instanceof CancellationException)
                throw new CancellationException();
            else
                throw new LibraryDownloadException(library, t);
        } else {
            if (xz) unpackLibrary(jar, Files.readAllBytes(xzFile.toPath()));
        }
    }

    @Override
    public boolean doPreExecute() {
        return true;
    }

    @Override
    public void preExecute() {
        Optional<Path> libPath = cacheRepository.getLibrary(originalLibrary);
        if (libPath.isPresent()) {
            try {
                FileUtils.copyFile(libPath.get().toFile(), jar);
                cached = true;
                return;
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Failed to copy file from cache", e);
                // We cannot copy cached file to current location
                // so we try to download a new one.
            }
        }

        if (testURLExistence(url)) {
            List<URL> urls = dependencyManager.getDownloadProvider().injectURLWithCandidates(url + ".pack.xz");
            task = new FileDownloadTask(urls, xzFile, null);
            task.setCacheRepository(cacheRepository);
            task.setCaching(true);
            xz = true;
        } else {
            List<URL> urls = dependencyManager.getDownloadProvider().injectURLWithCandidates(url);
            task = new FileDownloadTask(urls, jar,
                    library.getDownload().getSha1() != null ? new FileDownloadTask.IntegrityCheck("SHA-1", library.getDownload().getSha1()) : null);
            task.setCacheRepository(cacheRepository);
            task.setCaching(true);
            task.addIntegrityCheckHandler(FileDownloadTask.ZIP_INTEGRITY_CHECK_HANDLER);
            xz = false;
        }
    }

    private boolean testURLExistence(String rawUrl) {
        List<URL> urls = dependencyManager.getDownloadProvider().injectURLWithCandidates(rawUrl);
        for (URL url : urls) {
            URL rawURL = NetworkUtils.toURL(url.toString());
            URL xzURL = NetworkUtils.toURL(url + ".pack.xz");
            for (int retry = 0; retry < 3; retry++) {
                try {
                    if (NetworkUtils.urlExists(rawURL))
                        return false;
                    return NetworkUtils.urlExists(xzURL);
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Failed to test for url existence: " + url + ".pack.xz", e);
                }
            }
        }
        return false; // maybe some ugly implementation will give timeout for not existent url.
    }

    @Override
    public boolean doPostExecute() {
        return true;
    }

    @Override
    public void postExecute() throws Exception {
        if (!cached) {
            try {
                cacheRepository.cacheLibrary(library, jar.toPath(), xz);
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Failed to cache downloaded library " + library, e);
            }
        }
    }

    public static boolean checksumValid(File libPath, List<String> checksums) {
        try {
            if (checksums == null || checksums.isEmpty()) {
                return true;
            }
            byte[] fileData = Files.readAllBytes(libPath.toPath());
            boolean valid = checksums.contains(DigestUtils.digestToString("SHA-1", fileData));
            if (!valid && libPath.getName().endsWith(".jar")) {
                valid = validateJar(fileData, checksums);
            }
            return valid;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean validateJar(byte[] data, List<String> checksums) throws IOException {
        HashMap<String, String> files = new HashMap<>();
        String[] hashes = null;
        JarInputStream jar = new JarInputStream(new ByteArrayInputStream(data));
        JarEntry entry = jar.getNextJarEntry();
        while (entry != null) {
            byte[] eData = IOUtils.readFullyWithoutClosing(jar);
            if (entry.getName().equals("checksums.sha1")) {
                hashes = new String(eData, StandardCharsets.UTF_8).split("\n");
            }
            if (!entry.isDirectory()) {
                files.put(entry.getName(), DigestUtils.digestToString("SHA-1", eData));
            }
            entry = jar.getNextJarEntry();
        }
        jar.close();
        if (hashes != null) {
            boolean failed = !checksums.contains(files.get("checksums.sha1"));
            if (!failed) {
                for (String hash : hashes) {
                    if ((!hash.trim().equals("")) && (hash.contains(" "))) {
                        String[] e = hash.split(" ");
                        String validChecksum = e[0];
                        String target = hash.substring(validChecksum.length() + 1);
                        String checksum = files.get(target);
                        if ((!files.containsKey(target)) || (checksum == null)) {
                            LOG.warning("    " + target + " : missing");
                            failed = true;
                            break;
                        } else if (!checksum.equals(validChecksum)) {
                            LOG.warning("    " + target + " : failed (" + checksum + ", " + validChecksum + ")");
                            failed = true;
                            break;
                        }
                    }
                }
            }
            return !failed;
        }
        return false;
    }

    private static void unpackLibrary(File dest, byte[] src) throws IOException {
        if (dest.exists())
            if (!dest.delete())
                throw new IOException("Unable to delete file " + dest);

        byte[] decompressed;
        try {
            decompressed = IOUtils.readFullyAsByteArray(new XZInputStream(new ByteArrayInputStream(src)));
        } catch (IOException e) {
            throw new ArtifactMalformedException("Library " + dest + " is malformed");
        }

        String end = new String(decompressed, decompressed.length - 4, 4);
        if (!end.equals("SIGN"))
            throw new IOException("Unpacking failed, signature missing " + end);

        int x = decompressed.length;
        int len = decompressed[(x - 8)] & 0xFF | (decompressed[(x - 7)] & 0xFF) << 8 | (decompressed[(x - 6)] & 0xFF) << 16 | (decompressed[(x - 5)] & 0xFF) << 24;

        Path temp = Files.createTempFile("minecraft", ".pack");

        byte[] checksums = Arrays.copyOfRange(decompressed, decompressed.length - len - 8, decompressed.length - 8);

        try (OutputStream out = Files.newOutputStream(temp)) {
            out.write(decompressed, 0, decompressed.length - len - 8);
        }

        try (FileOutputStream jarBytes = new FileOutputStream(dest); JarOutputStream jos = new JarOutputStream(jarBytes)) {
            Pack200Utils.unpack(FCLPath.NATIVE_LIB_DIR, temp.toAbsolutePath().toString(), dest.getAbsolutePath());

            JarEntry checksumsFile = new JarEntry("checksums.sha1");
            checksumsFile.setTime(0L);
            jos.putNextEntry(checksumsFile);
            jos.write(checksums);
            jos.closeEntry();
        }
    }
}
