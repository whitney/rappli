package notifiers;
 
import play.Play;
import play.mvc.*;
import java.util.*;

import models.User;
 
public class MailManager extends Mailer {
 
	private static final String SENDER = "Rappli <admin@rappli.com>";
	private static final String BASE_URL = Play.configuration.getProperty("application.baseUrl");
	
	public static void signup(User user) {
		setSubject("Welcome to Rappli!");
		addRecipient(user.email);
		setFrom(SENDER);
		String baseUrl = BASE_URL;
		send(user, baseUrl);
	}
 
	public static void resetPassword(User user) {
		String newpassword = user.password;
		setFrom("Robot <robot@thecompany.com>");
		setSubject("Your password has been reset");
		addRecipient(user.email);
		send(user, newpassword);
	}
 
		
	/*
	public static void emailWithAttachment(User user) {
		setSubject("Welcome to Rappli!");
		addRecipient(user.email);
		setFrom(SENDER);
		EmailAttachment attachment = new EmailAttachment();
		attachment.setDescription("A pdf document");
		attachment.setPath(Play.getFile("rules.pdf").getPath());
		addAttachment(attachment);
		send(user);
	}
	*/
 
}
