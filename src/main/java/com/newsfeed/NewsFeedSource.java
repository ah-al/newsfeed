package com.newsfeed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;

@Component
public class NewsFeedSource{

	private static Logger logger = LoggerFactory
		      .getLogger(NewsFeedSource.class);

	private List<SyndEntry> feed;  
	
	public NewsFeedSource() {
		feed = retrieveFeed();
	}
	
	public List<SyndEntry> getFeed() {
		return feed;
	}
	
	private List<SyndEntry> retrieveFeed() {
		List<SyndEntry> feedItems = new ArrayList<>();
		
		try {
			SyndFeed feed = readFeed("https://www.aljazeera.net/aljazeerarss/a7c186be-1baa-4bd4-9d80-a84db769f779/73d0e1b4-532f-45ef-b135-bfdff8b8cab9");
			for (SyndEntry entry : feed.getEntries()) {
				if (entry != null) {
					feedItems.add(entry);
				}
			}
		} catch (Exception e) {
			logger.error("Problem while retrieving feed: url = {}\n exception = {}", "www.aljazeera.net", e);
		}
		
		return feedItems;
	}


	private SyndFeed readFeed(String url) throws IOException  {

		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = null;
		HttpGet httpGet = new HttpGet(url);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
					feed = input.build(reader);
				} catch (Exception e) {
					logger.error("Problem while Parsing feed: url = {}\n exception = {}", url, e);
				}
			}
		}

		return feed;

	}
	

}
