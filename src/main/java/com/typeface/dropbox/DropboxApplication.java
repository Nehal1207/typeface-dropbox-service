package com.typeface.dropbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class DropboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(DropboxApplication.class, args);
	}

}
