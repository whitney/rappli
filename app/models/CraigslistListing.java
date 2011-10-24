package models;

import play.*;
import play.db.jpa.*;
import play.data.validation.Required;

import javax.persistence.*;
import java.util.*;

@Entity
public class CraigslistListing extends Model {

	private static final String source = "craigslist";

	@Required
	public String postUrl;
	
	@Required
	public String postTitle;
	
	@Required
	public String html;
	
	@Required
	public int postPrice;
	
	@Required 
	public String contactEmail;
	
	public ArrayList<String> imgUrls;
	/*
	public CraigslistListing(String postUrl, String title, String html, int price) {
		super(postUrl, title, html, price);
		// TODO Auto-generated constructor stub
	}
	*/

}
