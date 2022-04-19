package com.example.spring_graphql;

import com.example.spring_graphql.fetcher.EventDataFetcher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;

@SpringBootTest
class SpringGraphqlApplicationTests {

	@Test
	void contextLoads() {
		EventDataFetcher e = new EventDataFetcher();
		e.events();
	}

}
