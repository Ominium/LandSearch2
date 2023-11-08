package com.example.landsearch

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@EnableJpaRepositories(basePackages = ["com.example.landsearch.repository.maria"])
@EnableMongoRepositories(basePackages = ["com.example.landsearch.repository.mongo"])
@SpringBootTest
class LandSearchApplicationTests {

	@Test
	fun contextLoads() {
	}

}
