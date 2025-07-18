package com.example;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean
	GenericContainer<?> ferretDbContainer() {
		return new GenericContainer<>(DockerImageName.parse("ghcr.io/ferretdb/ferretdb-eval:2"))
			.withExposedPorts(27017, 5432)
			.withEnv("POSTGRES_USER", "user")
			.withEnv("POSTGRES_PASSWORD", "password")
			.withEnv("FERRETDB_TELEMETRY", "false")
			.waitingFor(new HostPortWaitStrategy().forPorts(27017, 5432));
	}

	@Bean
	DynamicPropertyRegistrar dynamicPropertyRegistrar(GenericContainer<?> ferretDbContainer) {
		return registry -> registry.add("spring.data.mongodb.uri", () -> "mongodb://user:password@%s:%d/test"
			.formatted(ferretDbContainer.getHost(), ferretDbContainer.getMappedPort(27017)));
	}

}
