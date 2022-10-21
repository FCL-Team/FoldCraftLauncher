package com.tungsten.fclcore.util;

import java.security.*;
import java.util.Base64;

public final class KeyUtils {
    private KeyUtils() {
    }

    public static KeyPair generateKey() {
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(4096, new SecureRandom());
            return gen.genKeyPair();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toPEMPublicKey(PublicKey key) {
        byte[] encoded = key.getEncoded();
        return "-----BEGIN PUBLIC KEY-----\n" +
                Base64.getMimeEncoder(76, new byte[]{'\n'}).encodeToString(encoded) +
                "\n-----END PUBLIC KEY-----\n";
    }
}
