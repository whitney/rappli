package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
	
	private static final String REGEX = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	
	public static boolean valid(String email) {
		CharSequence inputStr = email;
		Pattern pattern = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher.matches();
	}

}
