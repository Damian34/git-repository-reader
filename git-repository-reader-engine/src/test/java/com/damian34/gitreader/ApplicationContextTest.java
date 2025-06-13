package com.damian34.gitreader;

import com.damian34.gitreader.config.TestContainerKafkaInitializer;
import com.damian34.gitreader.config.TestContainerMongoInitializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = {
		TestContainerMongoInitializer.class,
		TestContainerKafkaInitializer.class
})
class ApplicationContextTest {

	@Test
	void springContextTest() {
		// test context only
		Assertions.assertTrue(true);
	}
}
