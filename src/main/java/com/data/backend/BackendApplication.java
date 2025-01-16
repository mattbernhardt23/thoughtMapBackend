package com.data.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		String mongoUri = null;

		// 1. Check for Docker secret
		// String secretPath = "/run/secrets/mongo-uri";
		String mySecretValue = System.getenv("MONGO_URI");
		try {
			if (mySecretValue != null) {
				// mongoUri = new String(Files.readAllBytes(Paths.get(secretPath)),
				// StandardCharsets.UTF_8).trim();
				mongoUri = mySecretValue;
				System.out.println("Loaded MONGO_URI from Docker secret.");
			}
		} catch (Exception e) {
			System.err.println("Failed to read Docker secret: " + e.getMessage());
		}

		// 2. Fallback to environment variable if secret not found
		if (mongoUri == null) {
			Dotenv dotenv = Dotenv.configure().directory(".").load();
			mongoUri = dotenv.get("MONGO_URI");
			if (mongoUri != null) {
				System.out.println("Loaded MONGO_URI from environment variable.");
			} else {
				System.err.println("MONGO_URI not found in Docker secret or environment variable.");
			}
		}

		// 3. Set the system property
		System.setProperty("MONGO_URI", mongoUri);
		System.out.println("MONGO_URI: " + System.getProperty("MONGO_URI"));
		SpringApplication.run(BackendApplication.class, args);
	}
}
