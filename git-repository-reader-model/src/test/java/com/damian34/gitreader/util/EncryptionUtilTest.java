package com.damian34.gitreader.util;

import com.damian34.gitreader.model.encryption.util.EncryptionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class EncryptionUtilTest {
    
    private EncryptionUtil encryptionUtil;
    
    @BeforeEach
    void setUp() {
        encryptionUtil = new EncryptionUtil("S3cur3AndL0ngEncrypTionKey2025!!");
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "password123", "complex_password!@#$%^&*()"})
    void shouldEncryptAndDecryptCorrectly(String input) {
        // when
        String encrypted = encryptionUtil.encrypt(input);
        String decrypted = encryptionUtil.decrypt(encrypted);
        
        // then
        Assertions.assertNotEquals(input, encrypted, "Encrypted value should be different from input");
        Assertions.assertEquals(input, decrypted, "Decrypted value should match original input");
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    void shouldHandleNullAndEmptyValues(String input) {
        // when
        String encrypted = encryptionUtil.encrypt(input);
        String decrypted = encryptionUtil.decrypt(encrypted);
        
        // then
        Assertions.assertEquals(input, decrypted, "Should handle null/empty values properly");
    }
    
    @Test
    void shouldReturnDifferentValuesForDifferentInputs() {
        // given
        String input1 = "test1";
        String input2 = "test2";
        
        // when
        String encrypted1 = encryptionUtil.encrypt(input1);
        String encrypted2 = encryptionUtil.encrypt(input2);
        
        // then
        Assertions.assertNotEquals(encrypted1, encrypted2, "Different inputs should yield different encrypted values");
    }
} 