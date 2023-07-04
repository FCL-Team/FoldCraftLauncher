package com.tungsten.fclcore.util.png;

import com.tungsten.fclcore.util.png.image.ArgbImage;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public final class PNGWriter implements Closeable {
    public static final int DEFAULT_COMPRESS_LEVEL = Deflater.DEFAULT_COMPRESSION;

    private static final int COMPRESS_THRESHOLD = 20;
    private static final byte[] PNG_FILE_HEADER = {
            (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
    };

    private final OutputStream out;
    private final PNGType type;
    private final int compressLevel;

    private final Deflater deflater = new Deflater();
    private final CRC32 crc32 = new CRC32();
    private final byte[] writeBuffer = new byte[8];

    public PNGWriter(OutputStream out) {
        this(out, PNGType.RGBA, DEFAULT_COMPRESS_LEVEL);
    }

    public PNGWriter(OutputStream out, PNGType type) {
        this(out, type, DEFAULT_COMPRESS_LEVEL);
    }

    public PNGWriter(OutputStream out, int compressLevel) {
        this(out, PNGType.RGBA, compressLevel);
    }

    public PNGWriter(OutputStream out, PNGType type, int compressLevel) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(out);

        if (compressLevel != Deflater.DEFAULT_COMPRESSION && (compressLevel < 0 || compressLevel > 9)) {
            throw new IllegalArgumentException();
        }

        if (type != PNGType.RGB && type != PNGType.RGBA) {
            throw new UnsupportedOperationException("SimplePNG currently only supports RGB or RGBA images");
        }

        this.type = type;
        this.compressLevel = compressLevel;
        this.out = out;

        this.deflater.setLevel(compressLevel);
    }

    public PNGType getType() {
        return type;
    }

    public int getCompressLevel() {
        return compressLevel;
    }

    private void writeByte(int b) throws IOException {
        out.write(b);
        crc32.update(b);
    }

    private void writeShort(int s) throws IOException {
        writeBuffer[0] = (byte) (s >>> 8);
        writeBuffer[1] = (byte) (s >>> 0);
        writeBytes(writeBuffer, 0, 2);
    }

    private void writeInt(int value) throws IOException {
        writeBuffer[0] = (byte) (value >>> 24);
        writeBuffer[1] = (byte) (value >>> 16);
        writeBuffer[2] = (byte) (value >>> 8);
        writeBuffer[3] = (byte) (value >>> 0);
        writeBytes(writeBuffer, 0, 4);
    }

    private void writeLong(long value) throws IOException {
        writeBuffer[0] = (byte) (value >>> 56);
        writeBuffer[1] = (byte) (value >>> 48);
        writeBuffer[2] = (byte) (value >>> 40);
        writeBuffer[3] = (byte) (value >>> 32);
        writeBuffer[4] = (byte) (value >>> 24);
        writeBuffer[5] = (byte) (value >>> 16);
        writeBuffer[6] = (byte) (value >>> 8);
        writeBuffer[7] = (byte) (value >>> 0);
        writeBytes(writeBuffer, 0, 8);
    }

    private void writeBytes(byte[] bytes) throws IOException {
        writeBytes(bytes, 0, bytes.length);
    }

    private void writeBytes(byte[] bytes, int off, int len) throws IOException {
        out.write(bytes, off, len);
        crc32.update(bytes, off, len);
    }

    private void beginChunk(String header, int length) throws IOException {
        writeBuffer[0] = (byte) (length >>> 24);
        writeBuffer[1] = (byte) (length >>> 16);
        writeBuffer[2] = (byte) (length >>> 8);
        writeBuffer[3] = (byte) (length >>> 0);
        writeBuffer[4] = (byte) header.charAt(0);
        writeBuffer[5] = (byte) header.charAt(1);
        writeBuffer[6] = (byte) header.charAt(2);
        writeBuffer[7] = (byte) header.charAt(3);
        out.write(writeBuffer, 0, 8);

        crc32.reset();
        crc32.update(writeBuffer, 4, 4);
    }

    private void endChunk() throws IOException {
        int crc = (int) crc32.getValue();
        writeBuffer[0] = (byte) (crc >>> 24);
        writeBuffer[1] = (byte) (crc >>> 16);
        writeBuffer[2] = (byte) (crc >>> 8);
        writeBuffer[3] = (byte) (crc >>> 0);
        out.write(writeBuffer, 0, 4);
    }

    private static final byte[] textSeparator = {0};

    private void textChunk(String keyword, String text) throws IOException {
        byte[] keywordBytes = keyword.getBytes(StandardCharsets.US_ASCII);
        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        int textBytesLength = textBytes.length;

        boolean isAscii = text.length() == textBytesLength;

        boolean compress = compressLevel != 0 && textBytesLength >= COMPRESS_THRESHOLD;

        if (compress) {
            byte[] compressed = new byte[textBytesLength];

            deflater.reset();
            deflater.setInput(textBytes);
            deflater.finish();

            int len = deflater.deflate(compressed, 0, textBytesLength, Deflater.SYNC_FLUSH);
            if (len < textBytesLength) {
                textBytes = compressed;
                textBytesLength = len;
            } else {
                compress = false;
                deflater.reset();
            }
        }


        String chunkType;

        byte[] separator;

        if (isAscii) {
            if (compress) {
                chunkType = "zTXt";
                separator = new byte[]{
                        0, // null separator
                        0  // compression method
                };
            } else {
                chunkType = "tEXt";
                separator = new byte[]{
                        0, // null separator
                };
            }
        } else {
            chunkType = "iTXt";
            separator = new byte[] {
                    0, // null separator
                    (byte) (compress ? 1 : 0), // compression flag
                    0, // compression method
                    0, // null separator
                    0  // null separator
            };
        }

        beginChunk(chunkType, keywordBytes.length + separator.length + textBytesLength);
        writeBytes(keywordBytes);
        writeBytes(separator);
        writeBytes(textBytes, 0, textBytesLength);
        endChunk();
    }

    public void write(ArgbImage image) throws IOException {
        PNGMetadata metadata = image.getMetadata();
        final int width = image.getWidth();
        final int height = image.getHeight();

        out.write(PNG_FILE_HEADER);

        // IHDR Chunk
        beginChunk("IHDR", 13);
        writeInt(width);
        writeInt(height);
        writeByte(8); // bit depth
        writeByte(type.id);
        writeByte(0); // compression
        writeByte(0); // filter
        writeByte(0); // interlace method
        endChunk();

        if (metadata != null) {
            for (Map.Entry<String, String> entry : metadata.texts.entrySet()) {
                textChunk(entry.getKey(), entry.getValue());
            }
        }

        // IDAT Chunk
        int colorPerPixel = type.cpp;
        int bytesPerLine = 1 + colorPerPixel * width;
        int rawOutputSize = bytesPerLine * height;
        byte[] lineBuffer = new byte[bytesPerLine];

        deflater.reset();
        OutputBuffer buffer = new OutputBuffer(compressLevel == 0 ? rawOutputSize + 12 : rawOutputSize / 2);
        try (DeflaterOutputStream dos = new DeflaterOutputStream(buffer, deflater)) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int color = image.getArgb(x, y);
                    int off = 1 + colorPerPixel * x;

                    lineBuffer[off + 0] = (byte) (color >>> 16);
                    lineBuffer[off + 1] = (byte) (color >>> 8);
                    lineBuffer[off + 2] = (byte) (color >>> 0);
                    if (colorPerPixel == 4)
                        lineBuffer[off + 3] = (byte) (color >>> 24);
                }

                dos.write(lineBuffer);
            }
        }

        int len = buffer.size();
        beginChunk("IDAT", len);
        writeBytes(buffer.getBuffer(), 0, len);
        endChunk();

        // IEND Chunk
        beginChunk("IEND", 0);
        endChunk();
    }

    @Override
    public void close() throws IOException {
        deflater.end();
        out.close();
    }

    private static final class OutputBuffer extends ByteArrayOutputStream {
        public OutputBuffer() {
        }

        public OutputBuffer(int size) {
            super(size);
        }

        byte[] getBuffer() {
            return super.buf;
        }
    }
}