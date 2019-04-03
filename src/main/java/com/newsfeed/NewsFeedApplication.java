package com.newsfeed;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.newsfeed.thread.SaveEntryToFileThread;
import com.rometools.rome.feed.synd.SyndEntry;

@SpringBootApplication
public class NewsFeedApplication implements CommandLineRunner {

	private static Logger logger = LoggerFactory.getLogger(NewsFeedApplication.class);
	
	@Autowired
	private NewsFeedSource newsFeedSource;

	@PostConstruct
	private void initialize() {
		System.out.println("@PostConstruct");
	}
	
	@Autowired
    private TaskExecutor taskExecutor;
	
    @Autowired
    private ApplicationContext applicationContext;
	
    public void executeAsynchronously(SyndEntry entry) {
    	SaveEntryToFileThread myThread = applicationContext.getBean(SaveEntryToFileThread.class);
    	myThread.setEntry(entry);
        taskExecutor.execute(myThread);
    }
    
	public static void main(String[] args) {
		SpringApplication.run(NewsFeedApplication.class, args);
		
	}

	@Override
	public void run(String... args) throws Exception {
		
        List<SyndEntry> feed = newsFeedSource.getFeed();
        for (SyndEntry entry : feed) {
        	executeAsynchronously(entry);
        }
        ((ThreadPoolTaskExecutor)taskExecutor).shutdown();
	}
	
}
