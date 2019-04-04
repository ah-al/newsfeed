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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;

@Service
public class NewsFeedSourceService{

	@Value("${source.uri}")
	private String uri;
	
	private static Logger logger = LoggerFactory
		      .getLogger(NewsFeedSourceService.class);

	public List<SyndEntry> getFeed() {
		List<SyndEntry> feedItems = new ArrayList<>();
		
		try {
			SyndFeed feed = readFeed(uri);
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
				CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {//rest template

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
