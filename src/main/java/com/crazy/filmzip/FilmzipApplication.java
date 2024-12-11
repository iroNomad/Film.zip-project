package com.crazy.filmzip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FilmzipApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmzipApplication.class, args);
	}

}
