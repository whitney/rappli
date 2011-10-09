package util;

import java.security.SecureRandom;
import java.math.BigInteger;

public final class TokenGenerator {

	private SecureRandom random = new SecureRandom();

	public String nextToken() {
		return new BigInteger(130, random).toString(32);
	}
}
