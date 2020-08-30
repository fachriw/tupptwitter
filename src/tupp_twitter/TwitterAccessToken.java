package tupp_twitter;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class TwitterAccessToken implements Serializable {
	private String encryptedAccessToken;
	private String encryptedAccessTokenSecret;

	public TwitterAccessToken(String accessToken, String accessTokenSecret)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvocationTargetException {
		this.setAccessToken(accessToken);
		this.setAccessTokenSecret(accessTokenSecret);

	}

	public String getAccessToken() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvocationTargetException {
		Key key = new SecretKeySpec(new String("Bar12345Bar12345").getBytes(), "AES");
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = Base64.getDecoder().decode(this.encryptedAccessToken);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	public String getAccessTokenSecret() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvocationTargetException {

		Key key = new SecretKeySpec(new String("Bar12345Bar12345").getBytes(), "AES");
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = Base64.getDecoder().decode(this.encryptedAccessTokenSecret);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	public void setAccessToken(String accessToken) throws IllegalBlockSizeException, BadPaddingException,
			InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvocationTargetException {
		Key key = new SecretKeySpec(new String("Bar12345Bar12345").getBytes(), "AES");
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encValue = c.doFinal(accessToken.getBytes());
		String encryptedValue = new String(Base64.getEncoder().encode(encValue));
		this.encryptedAccessToken = encryptedValue;

	}

	public void setAccessTokenSecret(String accessTokenSecret) throws IllegalBlockSizeException, BadPaddingException,
			InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvocationTargetException {
		Key key = new SecretKeySpec(new String("Bar12345Bar12345").getBytes(), "AES");
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encValue = c.doFinal(accessTokenSecret.getBytes());
		String encryptedValue = new String(Base64.getEncoder().encode(encValue));
		this.encryptedAccessTokenSecret = encryptedValue;
	}

}
