package com.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class EcommerceApplication {

    public static void main(String[] args) {
        decodeBase64EnvVarsToFiles();
        SpringApplication.run(EcommerceApplication.class, args);
    }

    private static void decodeBase64EnvVarsToFiles() {
        try {
            String keystoreBase64 = System.getenv("KAFKA_KEYSTORE_BASE64");
            if (keystoreBase64 != null && !keystoreBase64.trim().isEmpty()) {
                File keystoreFile = File.createTempFile("client.keystore", ".p12");
                keystoreFile.deleteOnExit();
                try (FileOutputStream fos = new FileOutputStream(keystoreFile)) {
                    // Remove whitespace/newlines from Base64 before decoding
                    fos.write(Base64.getDecoder().decode(keystoreBase64.replaceAll("\\s", "")));
                }
                System.setProperty("spring.kafka.ssl.key-store-location", "file:" + keystoreFile.getAbsolutePath());
            }

            String truststoreBase64 = System.getenv("KAFKA_TRUSTSTORE_BASE64");
            if (truststoreBase64 != null && !truststoreBase64.trim().isEmpty()) {
                File truststoreFile = File.createTempFile("client.truststore", ".jks");
                truststoreFile.deleteOnExit();
                try (FileOutputStream fos = new FileOutputStream(truststoreFile)) {
                    fos.write(Base64.getDecoder().decode(truststoreBase64.replaceAll("\\s", "")));
                }
                System.setProperty("spring.kafka.ssl.trust-store-location", "file:" + truststoreFile.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Failed to decode Kafka certificates from environment variables: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
