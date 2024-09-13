package com.company.forumservice;

import org.springframework.boot.SpringApplication;

public class TestForumServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(ForumServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
