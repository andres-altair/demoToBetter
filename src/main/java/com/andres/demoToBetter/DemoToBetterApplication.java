package com.andres.demotobetter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DemoToBetterApplication {
// tree src\main\java /F
	public static void main(String[] args) {
		SpringApplication.run(DemoToBetterApplication.class, args);

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); 
		System.out.println(encoder.encode("passs"));
	}
}