package com.tungsten.fcl.vulkan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Proxy;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.OptionalDouble;
import java.util.concurrent.atomic.AtomicReference;

public class HandDepthTransformerTest {
    @Test
    public void patchedClassExecutesAndOnlyHandDepthGetsAClear() throws Exception {
        byte[] patched = HandDepthTransformer.patchHandPass(fixture(1));
        FixtureLoader loader = new FixtureLoader();
        String[] argumentNames = {
                "net.minecraft.client.renderer.state.level.CameraRenderState",
                "net.minecraft.client.renderer.state.level.PlayerRenderState",
                "com.mojang.renderpearl.api.textures.GpuTextureView"
        };
        Class<?>[] arguments = new Class<?>[argumentNames.length];
        for (int i = 0; i < arguments.length; i++) arguments[i] = loader.defineEmpty(argumentNames[i]);
        Class<?> renderer = loader.define(HandDepthTransformer.TARGET.replace('/', '.'), patched);
        Object instance = renderer.getConstructor().newInstance();
        renderer.getMethod(HandDepthTransformer.METHOD, arguments).invoke(instance, null, null, null);
        OptionalDouble hand = (OptionalDouble) renderer.getField("hand").get(null);
        assertTrue(hand.isPresent());
        assertEquals(0.0, hand.getAsDouble(), 0.0);
        renderer.getMethod("render3dHud").invoke(instance);
        assertFalse(((OptionalDouble) renderer.getField("other").get(null)).isPresent());
    }

    @Test
    public void missingOrDuplicateAttachmentAnchorsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> HandDepthTransformer.patchHandPass(fixture(0)));
        assertThrows(IllegalArgumentException.class, () -> HandDepthTransformer.patchHandPass(fixture(2)));
    }

    @Test
    public void unsupportedHashesOtherClassesAndRetransformsAreNotChanged() {
        HandDepthTransformer transformer = new HandDepthTransformer();
        byte[] original = fixture(1);
        assertNull(transformer.transform(null, HandDepthTransformer.TARGET, null, null, original));
        assertNull(transformer.transform(null, "unrelated/Class", null, null, original));
        assertNull(transformer.transform(null, HandDepthTransformer.TARGET, String.class, null, original));
    }

    @Test
    public void bundledAsmAndTransformerUsePrivateLoader() throws Exception {
        Path agentJar = Path.of(System.getProperty("fcl.compat.test.agentJar"));
        AtomicReference<ClassFileTransformer> registered = new AtomicReference<>();
        Instrumentation instrumentation = (Instrumentation) Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class<?>[]{Instrumentation.class}, (proxy, method, args) -> {
                    if ("addTransformer".equals(method.getName())) registered.set((ClassFileTransformer) args[0]);
                    return null;
                });
        try (URLClassLoader loader = AgentBootstrap.implementationLoader(agentJar.toUri().toURL())) {
            Class<?> asm = loader.loadClass("org.objectweb.asm.ClassReader");
            assertEquals(loader, asm.getClassLoader());
            assertNotSame(classReaderLoader(), asm.getClassLoader());
            loader.loadClass("com.tungsten.fcl.vulkan.HandDepthAgent")
                    .getMethod("premain", Instrumentation.class).invoke(null, instrumentation);
            assertEquals(loader, registered.get().getClass().getClassLoader());
        }
    }

    private static ClassLoader classReaderLoader() {
        return org.objectweb.asm.ClassReader.class.getClassLoader();
    }

    // Synthetic class only: no Minecraft bytecode is redistributed in the tests.
    private static byte[] fixture(int anchors) {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        writer.visit(Opcodes.V17, Opcodes.ACC_PUBLIC, HandDepthTransformer.TARGET, null, "java/lang/Object", null);
        writer.visitField(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "hand", "Ljava/util/OptionalDouble;", null, null).visitEnd();
        writer.visitField(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "other", "Ljava/util/OptionalDouble;", null, null).visitEnd();
        MethodVisitor constructor = writer.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        constructor.visitCode();
        constructor.visitVarInsn(Opcodes.ALOAD, 0);
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        constructor.visitInsn(Opcodes.RETURN);
        constructor.visitMaxs(0, 0);
        constructor.visitEnd();
        MethodVisitor hand = writer.visitMethod(Opcodes.ACC_PUBLIC, HandDepthTransformer.METHOD,
                HandDepthTransformer.DESCRIPTOR, null, null);
        hand.visitCode();
        Label ready = new Label();
        hand.visitVarInsn(Opcodes.ALOAD, 1);
        hand.visitJumpInsn(Opcodes.IFNULL, ready);
        hand.visitInsn(Opcodes.NOP);
        hand.visitLabel(ready);
        for (int i = 0; i < anchors; i++) emptyToField(hand, "hand");
        hand.visitInsn(Opcodes.RETURN);
        hand.visitMaxs(0, 0);
        hand.visitEnd();
        MethodVisitor other = writer.visitMethod(Opcodes.ACC_PUBLIC, "render3dHud", "()V", null, null);
        other.visitCode();
        emptyToField(other, "other");
        other.visitInsn(Opcodes.RETURN);
        other.visitMaxs(0, 0);
        other.visitEnd();
        writer.visitEnd();
        return writer.toByteArray();
    }

    private static void emptyToField(MethodVisitor method, String field) {
        method.visitMethodInsn(Opcodes.INVOKESTATIC, "java/util/OptionalDouble", "empty", "()Ljava/util/OptionalDouble;", false);
        method.visitFieldInsn(Opcodes.PUTSTATIC, HandDepthTransformer.TARGET, field, "Ljava/util/OptionalDouble;");
    }

    private static final class FixtureLoader extends ClassLoader {
        Class<?> define(String name, byte[] bytes) {
            return defineClass(name, bytes, 0, bytes.length);
        }

        Class<?> defineEmpty(String name) {
            ClassWriter writer = new ClassWriter(0);
            writer.visit(Opcodes.V17, Opcodes.ACC_PUBLIC, name.replace('.', '/'), null, "java/lang/Object", null);
            writer.visitEnd();
            return define(name, writer.toByteArray());
        }
    }
}
