package com.newsfeed.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.rometools.rome.feed.synd.SyndEntry;

@XmlRootElement
public class Item {

	private String link;
	private String title;
	private String description;
	private String pubDate;
	private String category;
	private String comments;
	private String guid;
	
	public Item() {
		// TODO Auto-generated constructor stub
	}
	
	public Item(SyndEntry entry) {
		setLink(entry.getLink());
		setTitle(entry.getTitle());
		setDescription(entry.getDescription().getValue());
		setPubDate(entry.getPublishedDate().toString());
		setCategory(entry.getCategories().get(0).getName());
		setComments(entry.getComments());
		setGuid(entry.getUri());
	}

	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	
	
}
