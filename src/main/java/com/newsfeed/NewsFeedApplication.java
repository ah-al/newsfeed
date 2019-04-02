package com.newsfeed;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rometools.rome.feed.synd.SyndEntry;

@SpringBootApplication
public class NewsFeedApplication implements CommandLineRunner {

	private static Logger logger = LoggerFactory
		      .getLogger(NewsFeedApplication.class);
	
	@Autowired
	private NewsFeedSource newsFeedSource;

	public static void main(String[] args) {
		SpringApplication.run(NewsFeedApplication.class, args);
		
	}

	@Override
	public void run(String... args) throws Exception {
		
        List<SyndEntry> feed = newsFeedSource.getFeed();
        for (SyndEntry entry : feed) {
        	logger.info(entry.getCategories().get(0).getName());
        }

	}
}
