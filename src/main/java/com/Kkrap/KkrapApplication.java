package com.Kkrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KkrapApplication {

	public static void main(String[] args) {
		SpringApplication.run(KkrapApplication.class, args);
	}
}
