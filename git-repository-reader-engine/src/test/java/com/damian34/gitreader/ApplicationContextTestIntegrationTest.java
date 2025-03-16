package com.damian34.gitreader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationContextTestIntegrationTest extends AbstractBaseIntegrationTest {

	@Test
	void springContextTest() {
		// test context only
		Assertions.assertTrue(true);
	}
}
