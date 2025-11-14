package com.mobile.utils;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;

/**
 * Utility class for generating TOTP codes for testing
 * Uses the TOTP library to generate 6-digit codes based on secret key
 */
public class TotpGenerator {

    private static final HashingAlgorithm ALGORITHM = HashingAlgorithm.SHA1;
    private static final int DIGITS = 6;
    private static final int PERIOD = 30; // 30 seconds

    /**
     * Generate TOTP code from secret key
     * @param secretKey The base32 encoded secret key
     * @return 6-digit TOTP code
     */
    public static String generateTotpCode(String secretKey) {
        try {
            TimeProvider timeProvider = new SystemTimeProvider();
            CodeGenerator codeGenerator = new DefaultCodeGenerator(ALGORITHM, DIGITS);
            
            // Generate code
            long currentBucket = Math.floorDiv(timeProvider.getTime(), PERIOD);
            String code = codeGenerator.generate(secretKey, currentBucket);
            
            return code;
        } catch (dev.samstevens.totp.exceptions.CodeGenerationException e) {
            throw new RuntimeException("Failed to generate TOTP code: " + e.getMessage(), e);
        }
    }

    /**
     * Verify if a TOTP code is valid
     * @param secretKey The base32 encoded secret key
     * @param code The code to verify
     * @return true if valid, false otherwise
     */
    public static boolean verifyCode(String secretKey, String code) {
        try {
            TimeProvider timeProvider = new SystemTimeProvider();
            CodeGenerator codeGenerator = new DefaultCodeGenerator(ALGORITHM, DIGITS);
            CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
            
            return verifier.isValidCode(secretKey, code);
        } catch (Exception e) {
            // Catch any exception during verification
            return false;
        }
    }

    /**
     * Extract secret key from TOTP URI
     * Example URI: otpauth://totp/StaffAuth:user@example.com?secret=JBSWY3DPEHPK3PXP&issuer=StaffAuth
     * @param totpUri The TOTP URI
     * @return The secret key
     */
    public static String extractSecretFromUri(String totpUri) {
        if (totpUri == null || totpUri.isEmpty()) {
            throw new IllegalArgumentException("TOTP URI cannot be null or empty");
        }
        
        try {
            // Find secret parameter
            String[] parts = totpUri.split("secret=");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Invalid TOTP URI - no secret found");
            }
            
            String secretPart = parts[1];
            // Secret ends at & or end of string
            int endIndex = secretPart.indexOf('&');
            if (endIndex > 0) {
                return secretPart.substring(0, endIndex);
            } else {
                return secretPart;
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract secret from URI: " + e.getMessage(), e);
        }
    }
}
