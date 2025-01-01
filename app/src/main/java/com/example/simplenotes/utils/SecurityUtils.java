package com.example.simplenotes.utils;

import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import java.security.KeyStore;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import android.content.SharedPreferences;
import android.util.Base64;

public class SecurityUtils {
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final String KEY_ALIAS = "NotesEncryptionKey";
    private static final String PREFS_NAME = "NotesSecurityPrefs";
    private static final String KEY_ENCRYPTION = "encryption_key";
    private static final int KEY_SIZE = 256;

    public static void init(Context context) {
        // Pre-initialize encryption key to avoid delay during first database access
        getDeviceSpecificKey(context);
    }

    @SuppressWarnings("NewApi")
    public static byte[] getDeviceSpecificKey(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
                keyStore.load(null);

                // Check if key exists
                if (!keyStore.containsAlias(KEY_ALIAS)) {
                    // Generate a new key if it doesn't exist
                    KeyGenerator keyGenerator = KeyGenerator.getInstance(
                            KeyProperties.KEY_ALGORITHM_AES,
                            ANDROID_KEYSTORE
                    );

                    KeyGenParameterSpec keySpec = new KeyGenParameterSpec.Builder(
                            KEY_ALIAS,
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setKeySize(KEY_SIZE)
                            .setUserAuthenticationRequired(false)
                            .build();

                    keyGenerator.init(keySpec);
                    keyGenerator.generateKey();
                }

                // Retrieve the key
                KeyStore.SecretKeyEntry keyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(
                        KEY_ALIAS,
                        null
                );
                SecretKey secretKey = keyEntry.getSecretKey();
                return secretKey.getEncoded();
            } catch (Exception e) {
                // Fallback to legacy implementation if anything goes wrong
                return getLegacyKey(context);
            }
        } else {
            // For older Android versions, use SharedPreferences-based storage
            return getLegacyKey(context);
        }
    }

    private static byte[] getLegacyKey(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String savedKey = prefs.getString(KEY_ENCRYPTION, null);
            
            if (savedKey != null) {
                return Base64.decode(savedKey, Base64.DEFAULT);
            }

            // Generate a new key
            byte[] key = new byte[32]; // 256 bits
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(key);
            
            // Save the key
            String keyBase64 = Base64.encodeToString(key, Base64.DEFAULT);
            prefs.edit()
                .putString(KEY_ENCRYPTION, keyBase64)
                .apply();
            
            return key;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate or retrieve encryption key", e);
        }
    }
} 