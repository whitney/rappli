package controllers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.User;
import util.TokenGenerator;
import util.EmailValidator;
import notifiers.MailManager;

import play.mvc.Http.StatusCode;
import play.data.validation.Required;
import play.Logger;

public class Users extends Application {

	/**
	 * Renders the sign-up form.
	 */
	public static void signup() {
		render();
	}
	
	/**
	 * Handles the POST request from the sign-up form.
	 * 
	 * @param email
	 */
	public static void signupHandler(@Required String email) {
		if(validation.hasErrors()) {
			params.flash(); // add http parameters to the flash scope
			validation.keep(); // keep the errors for the next request
		}
		
		boolean validEmail = EmailValidator.valid(email);
    	User user = User.find("byEmail", email).first();
    	Logger.debug("validEmail: " + validEmail);
    	Logger.debug("user: " + user);
    	
    	if (validEmail && user == null) {
    		TokenGenerator tokenGen = new TokenGenerator();
    		String emailToken = tokenGen.nextToken();
    		Logger.debug("New user being created with email {}, token {}", email, emailToken);
    		user = new User(email, emailToken);
    		user.save();
    		
    		// send email to user containing welcome copy and tokened activation link.
    		MailManager.signup(user);
    		//response.status = StatusCode.BAD_REQUEST;
    		render("Users/accountCreated.html", user);
    	} else {
    		// TODO: nicer treatment here
    		validation.email("email", email);
    		Logger.info("Account cannot be created for email: {}", email);
    		response.status = StatusCode.BAD_REQUEST;
    		render("Users/signup.html");
    	}
	}
	
	/**
	 * Renders the activation form.
	 * 
	 * @param email
	 * @param emailToken
	 */
	public static void activate(@Required String email, @Required String emailToken) {
		User user = User.find("byEmailAndEmailToken", email, emailToken).first();
		if (user == null) {
    		// TODO: put this information in the "flash" to inform the user what to do 
    		Logger.info("Account cannot be created for email: {}, token: {}", email, emailToken);
    		render("Users/signup.html");
		} else {
			render(user);
		}
	}
	
	/**
	 * Handles the POST request from the activate form.
	 * 
	 * @param email
	 * @param emailToken
	 * @param password
	 * @param passwordConfirmation
	 * @param firstName
	 * @param lastName
	 */
	public static void activateHandler(@Required String email, @Required String emailToken, 
										@Required String password, @Required String passwordConfirmation, 
										@Required String firstName, @Required String lastName) {
		if(validation.hasErrors()) {
			params.flash(); // add http parameters to the flash scope
			validation.keep(); // keep the errors for the next request
		}
		
		User user = User.find("byEmailAndEmailToken", email, emailToken).first();
		boolean authenticated = false;
		if (user == null) {
    		// TODO: put this information in the "flash" to inform the user what to do 
    		Logger.info("Account cannot be created for email: {}, token: {}", email, emailToken);
    		signup();
		} else if (!password.equals(passwordConfirmation)) {
    		// TODO: put this information in the "flash" to inform the user what to do 
			activate(email, emailToken);
		} else if (!EmailValidator.valid(email)) {
    		// TODO: put this information in the "flash" to inform the user what to do 
			activate(email, emailToken);
		} else if (firstName.isEmpty() || lastName.isEmpty()) {
    		// TODO: put this information in the "flash" to inform the user what to do 
			activate(email, emailToken);
		} else {
			user.setPassword(password);
			user.firstName = firstName;
			user.lastName = lastName;
			user.activated = true;
			user.save();
			authenticated = Security.authenticate(email, password);
		}
		
		if (authenticated) {
			Logger.info("Account successfully activated!", user);
			Home.index(user);
		} else {
    		// TODO: put this information in the "flash" to inform the user what to do 
			activate(email, emailToken);
		}
	}
}
