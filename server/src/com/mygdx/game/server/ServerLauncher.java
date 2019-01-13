package com.mygdx.game.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServerLauncher {

	public static void main(String[] args) {
		SpringApplication.run(ServerLauncher.class, args);
	}

}
