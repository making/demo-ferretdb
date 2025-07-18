package com.example;

import org.springframework.boot.SpringApplication;

public class TestDemoFerretApplication {

	public static void main(String[] args) {
		SpringApplication.from(DemoFerretApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
