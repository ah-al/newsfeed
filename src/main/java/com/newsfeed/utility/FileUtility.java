package com.newsfeed.utility;

import java.io.File;
import java.text.SimpleDateFormat;

import com.rometools.rome.feed.synd.SyndEntry;

public class FileUtility {

	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");//EEE, dd MMM yyyy HH:mm:ss z
	public static synchronized String getDirectoryPath(String paseDirectory, SyndEntry entry) {
		File fileDirectory = new File(paseDirectory + dateFormatter.format(entry.getPublishedDate()));
		if (!fileDirectory.exists()) {
			fileDirectory.mkdir();
		}
		fileDirectory = new File(paseDirectory + dateFormatter.format(entry.getPublishedDate()) + "\\" +entry.getCategories().get(0).getName());
		if (!fileDirectory.exists()) {
			fileDirectory.mkdir();
		}
		return fileDirectory.getAbsolutePath();
	}
}
