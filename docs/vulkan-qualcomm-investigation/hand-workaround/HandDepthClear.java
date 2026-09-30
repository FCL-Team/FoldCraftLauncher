// SPDX-License-Identifier: GPL-3.0-or-later
package fcl.compat;

import java.lang.classfile.*;
import java.lang.classfile.instruction.InvokeInstruction;
import java.lang.constant.*;
import java.lang.instrument.*;
import java.security.MessageDigest;
import java.security.ProtectionDomain;
import java.util.HexFormat;
import java.util.jar.JarFile;
import java.net.URLClassLoader;
import java.nio.file.Path;

/** Snapshot-specific, reversible hand-pass depth-clear workaround. */
public final class HandDepthClear {
    private static final String TARGET = "net/minecraft/client/renderer/GameRenderer";
    private static final String ORIGINAL_SHA256 =
        "e31c63dfe463b191845c5403e62cd5462a875d9f7337eb1a5d07c27bfe26f376";
    private static final String METHOD = "renderItemInHand";
    private static final String DESCRIPTOR =
        "(Lnet/minecraft/client/renderer/state/level/CameraRenderState;" +
        "Lnet/minecraft/client/renderer/state/level/PlayerRenderState;" +
        "Lcom/mojang/renderpearl/api/textures/GpuTextureView;)V";
    private static final ClassDesc OPTIONAL_DOUBLE = ClassDesc.of("java.util.OptionalDouble");

    public static void premain(String args, Instrumentation instrumentation) {
        if (Runtime.version().feature() != 25)
            throw new IllegalStateException("HandDepthClear requires the tested Java 25 runtime");
        System.err.println("[HandDepthClear] active: native driver unchanged; snapshot-specific hand attachment clear");
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override public byte[] transform(ClassLoader loader, String name, Class<?> type,
                                               ProtectionDomain domain, byte[] bytes) {
                if (!TARGET.equals(name)) return null;
                try {
                    byte[] patched = patch(bytes, loader);
                    System.err.println("[HandDepthClear] patched GameRenderer.renderItemInHand: depth LOAD -> CLEAR(0.0)");
                    return patched;
                } catch (Exception failure) {
                    System.err.println("[HandDepthClear] NOT APPLIED; unsupported class or patch failure: " + failure);
                    return null;
                }
            }
        });
    }

    static byte[] patch(byte[] original, ClassLoader loader) throws Exception {
        String hash = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(original));
        if (!ORIGINAL_SHA256.equals(hash))
            throw new IllegalArgumentException("GameRenderer SHA256 mismatch: " + hash);
        ClassFile cf = ClassFile.of(ClassFile.ClassHierarchyResolverOption.of(
            ClassHierarchyResolver.ofResourceParsing(loader).cached()));
        ClassModel model = cf.parse(original);
        if (!model.thisClass().asInternalName().equals(TARGET))
            throw new IllegalArgumentException("Unexpected class name");
        int[] replacements = {0};
        byte[] patched = cf.transformClass(model, (builder, element) -> {
            if (element instanceof MethodModel method &&
                method.methodName().equalsString(METHOD) && method.methodType().equalsString(DESCRIPTOR)) {
                builder.transformMethod(method, MethodTransform.transformingCode((code, instruction) -> {
                    if (instruction instanceof InvokeInstruction call &&
                        call.owner().asInternalName().equals("java/util/OptionalDouble") &&
                        call.name().equalsString("empty") &&
                        call.typeSymbol().equals(MethodTypeDesc.of(OPTIONAL_DOUBLE))) {
                        // Retain all existing clears, barriers, world rendering,
                        // hand drawing and resource cleanup. Only the hand pass
                        // uses an attachment clear to avoid the failing reuse path.
                        code.dconst_0().invokestatic(OPTIONAL_DOUBLE, "of",
                            MethodTypeDesc.of(OPTIONAL_DOUBLE, ConstantDescs.CD_double));
                        replacements[0]++;
                    } else code.with(instruction);
                }));
            } else builder.with(element);
        });
        if (replacements[0] != 1)
            throw new IllegalStateException("Expected exactly one hand depth attachment, found " + replacements[0]);
        var errors = cf.verify(patched);
        if (!errors.isEmpty()) throw new IllegalStateException(errors.toString());
        return patched;
    }

    private static long countCalls(ClassModel model, String method, String owner, String name) {
        return model.methods().stream().filter(m -> m.methodName().equalsString(method))
            .flatMap(m -> m.code().stream()).flatMap(c -> c.elementList().stream())
            .filter(e -> e instanceof InvokeInstruction i && i.owner().asInternalName().equals(owner)
                && i.name().equalsString(name)).count();
    }

    public static void main(String[] args) throws Exception {
        var urls = new java.net.URL[args.length];
        for (int i = 0; i < args.length; i++) urls[i] = Path.of(args[i]).toUri().toURL();
        try (URLClassLoader loader = new URLClassLoader(urls, HandDepthClear.class.getClassLoader());
             JarFile jar = new JarFile(args[0])) {
            byte[] original = jar.getInputStream(jar.getJarEntry(TARGET + ".class")).readAllBytes();
            byte[] patched = patch(original, loader);
            ClassModel before = ClassFile.of().parse(original), after = ClassFile.of().parse(patched);
            if (countCalls(before, METHOD, "java/util/OptionalDouble", "empty") != 1 ||
                countCalls(after, METHOD, "java/util/OptionalDouble", "empty") != 0 ||
                countCalls(after, METHOD, "java/util/OptionalDouble", "of") != 1)
                throw new AssertionError("Hand clear replacement check failed");
            for (String method : new String[] {"render3dHud", "render"})
                if (countCalls(before, method, "com/mojang/renderpearl/api/commands/CommandEncoder", "clearDepthTexture") !=
                    countCalls(after, method, "com/mojang/renderpearl/api/commands/CommandEncoder", "clearDepthTexture"))
                    throw new AssertionError("Existing clear changed in " + method);
            byte[] unsupported = original.clone(); unsupported[unsupported.length - 1] ^= 1;
            try { patch(unsupported, loader); throw new AssertionError("Unsupported class accepted"); }
            catch (IllegalArgumentException expected) { /* Hash guard rejects changed snapshots/mods. */ }
            System.out.println("PASS: verified bytecode; exactly one hand pass CLEAR(0.0); existing clears preserved; hash guard rejects changes");
        }
    }
}
