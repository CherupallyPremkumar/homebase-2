package com.homebase.ecom.build;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

@SpringBootApplication
@ComponentScan(basePackages = { "com.homebase.ecom",
		"org.chenile","org.chenile.service.registry.configuration","com.homebase.ecom.cconfig.configuration" })
@EntityScan(basePackages = { "com.homebase.ecom", "org.chenile.service.registry.model","com.homebase.ecom.cconfig.model" })
@EnableJpaRepositories(basePackages = { "com.homebase.ecom", "org.chenile.service.registry.configuration.dao","com.homebase.ecom.cconfig.configuration.dao" })
@EnableTransactionManagement
public class BuildApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		decodeBase64EnvVarsToFiles();
		SpringApplication.run(BuildApplication.class, args);
	}

	private static void decodeBase64EnvVarsToFiles() {
		try {
			String keystoreBase64 = System.getenv("KAFKA_KEYSTORE_BASE64");
			if (keystoreBase64 != null && !keystoreBase64.trim().isEmpty()) {
				File keystoreFile = File.createTempFile("client.keystore", ".p12");
				keystoreFile.deleteOnExit();
				try (FileOutputStream fos = new FileOutputStream(keystoreFile)) {
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
			System.err.println("Failed to decode Kafka certificates: " + e.getMessage());
		}
	}
}
