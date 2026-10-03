/*
 * Fold Craft Launcher
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.tungsten.fcl.vulkan;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.instrument.ClassFileTransformer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.ProtectionDomain;

/** Same tested attachment change, with an exact-class guard and no JAR/signature edits. */
public final class HandDepthTransformer implements ClassFileTransformer {
    static final String TARGET = "net/minecraft/client/renderer/GameRenderer";
    static final String METHOD = "renderItemInHand";
    static final String DESCRIPTOR = "(Lnet/minecraft/client/renderer/state/level/CameraRenderState;"
            + "Lnet/minecraft/client/renderer/state/level/PlayerRenderState;"
            + "Lcom/mojang/renderpearl/api/textures/GpuTextureView;)V";
    static final String ORIGINAL_SHA256 =
            "e31c63dfe463b191845c5403e62cd5462a875d9f7337eb1a5d07c27bfe26f376";

    @Override
    public byte[] transform(ClassLoader loader, String name, Class<?> type,
                            ProtectionDomain domain, byte[] bytes) {
        if (!TARGET.equals(name) || type != null) return null;
        if (!ORIGINAL_SHA256.equals(sha256(bytes))) {
            System.err.println("[VulkanHandDepthFix] Skipped: unsupported or modified GameRenderer");
            return null;
        }
        try {
            byte[] patched = patchHandPass(bytes);
            System.err.println("[VulkanHandDepthFix] Applied: hand depth LOAD -> CLEAR(0.0)");
            return patched;
        } catch (RuntimeException failure) {
            System.err.println("[VulkanHandDepthFix] Not applied; original class retained: " + failure);
            return null;
        }
    }

    /** Package-visible for synthetic tests; production transform checks the original hash first. */
    static byte[] patchHandPass(byte[] original) {
        ClassReader reader = new ClassReader(original);
        if (!TARGET.equals(reader.getClassName())) throw new IllegalArgumentException("Unexpected class");
        // Preserve existing frames and untouched methods. ASM adjusts label/frame offsets;
        // OptionalDouble.of consumes the injected double, leaving the same stack shape.
        ClassWriter writer = new ClassWriter(reader, 0);
        int[] replacements = {0};
        reader.accept(new ClassVisitor(Opcodes.ASM9, writer) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor,
                                             String signature, String[] exceptions) {
                MethodVisitor delegate = super.visitMethod(access, name, descriptor, signature, exceptions);
                if (!METHOD.equals(name) || !DESCRIPTOR.equals(descriptor)) return delegate;
                return new MethodVisitor(Opcodes.ASM9, delegate) {
                    @Override
                    public void visitMethodInsn(int opcode, String owner, String method, String desc, boolean isInterface) {
                        if (opcode == Opcodes.INVOKESTATIC && "java/util/OptionalDouble".equals(owner)
                                && "empty".equals(method) && "()Ljava/util/OptionalDouble;".equals(desc)) {
                            super.visitInsn(Opcodes.DCONST_0);
                            super.visitMethodInsn(Opcodes.INVOKESTATIC, owner, "of",
                                    "(D)Ljava/util/OptionalDouble;", false);
                            replacements[0]++;
                        } else {
                            super.visitMethodInsn(opcode, owner, method, desc, isInterface);
                        }
                    }

                    @Override
                    public void visitMaxs(int maxStack, int maxLocals) {
                        super.visitMaxs(maxStack + 2, maxLocals);
                    }
                };
            }
        }, 0);
        if (replacements[0] != 1) throw new IllegalArgumentException("Expected exactly one hand depth attachment");
        return writer.toByteArray();
    }

    private static String sha256(byte[] bytes) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(bytes);
            char[] hex = "0123456789abcdef".toCharArray();
            StringBuilder result = new StringBuilder(digest.length * 2);
            for (byte value : digest) result.append(hex[(value & 0xff) >>> 4]).append(hex[value & 0x0f]);
            return result.toString();
        } catch (NoSuchAlgorithmException impossible) {
            throw new IllegalStateException("SHA-256 is unavailable", impossible);
        }
    }
}
