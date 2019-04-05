package com.newsfeed;

import static com.newsfeed.utility.FileUtility.convertObjectToXML;
import static com.newsfeed.utility.FileUtility.getDateFolderName;
import static com.newsfeed.utility.FileUtility.getElementFromMessage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.feed.inbound.FeedEntryMessageSource;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.scheduling.PollerMetadata;

import com.rometools.rome.feed.synd.SyndEntry;

@SpringBootApplication()
public class NewsFeedApplication implements CommandLineRunner {

	private static Logger LOGGER = LoggerFactory.getLogger(NewsFeedApplication.class);
	
	@Value("${file.path}")
	private String filePath;
	
	@Value("${source.uri}")
	private String uri;
	
	@Value("${poll.messegares.max}")
	private int maxMessagesPerPoll;
	
	@Value("${poll.checkRate}")
	private int pollCheckRate;

	public static void main(String[] args) {
		SpringApplication.run(NewsFeedApplication.class, args);
		
	}

	@Override
	public void run(String... args) throws Exception {
		File baseDirectory  = new File(filePath);
		if (!baseDirectory.exists()) {
			baseDirectory.mkdirs();
		}
	}
	
	@Bean(name = PollerMetadata.DEFAULT_POLLER)
    public  PollerMetadata poller() {
        return Pollers.fixedRate(pollCheckRate).maxMessagesPerPoll(maxMessagesPerPoll).get();
    }
	
	@Bean
	public IntegrationFlow flow() throws MalformedURLException {
		return IntegrationFlows.from(new FeedEntryMessageSource(new URL(uri),"source"))
				.split()
				.publishSubscribeChannel(c -> c
						.subscribe(s -> s
								.<SyndEntry, String>transform(e -> convertObjectToXML(e))
								.handle(Files.outboundAdapter(new File(filePath))
								.fileExistsMode(FileExistsMode.REPLACE)
								.autoCreateDirectory(true)
		                	    .fileNameGenerator(message -> getDateFolderName(message.getPayload().toString()) 
		                	    		+ "\\" + getElementFromMessage(message.getPayload().toString(), "category") 
		                	    		+ "\\" + getElementFromMessage(message.getPayload().toString(), "guid") + ".xml")
		                	    .get()))
						)
				.log(LoggingHandler.Level.FATAL, "newsFeed.category", m -> ((SyndEntry)m.getPayload()).getUri())
                .get();
	}
	
}
