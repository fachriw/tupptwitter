package tupp_twitter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAuthentication {

	private static final String oAuthConsumerKey = "29sYaMwTacaYv9GCiqsijEJIi";
	private static final String oAuthConsumerKeySecret = "Wf3UzAgrUWk9jvswMEgt0Yepr7OPaaX3iS7ejJwncnBe3lgxCd";

	private static RequestToken lastRequestToken;

	public static boolean isTwitterInstanceAuthenticated(Twitter twitter) {
		Map<String, RateLimitStatus> rateLimitStatus;
		try {
			rateLimitStatus = twitter.getRateLimitStatus("search");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
		if (searchTweetsRateLimit == null) {
			return false;
		} else {
			return true;
		}
	}

	public static Twitter getTwitterInstance() {

		try (FileInputStream fis = new FileInputStream(".config"); ObjectInputStream ois = new ObjectInputStream(fis)) {

			try {
				TwitterAccessToken tat = (TwitterAccessToken) ois.readObject();
				String accessToken = tat.getAccessToken();
				String accessTokenSecret = tat.getAccessTokenSecret();
				ConfigurationBuilder cb = new ConfigurationBuilder();
				cb.setDebugEnabled(true).setOAuthConsumerKey(oAuthConsumerKey)
						.setOAuthConsumerSecret(oAuthConsumerKeySecret).setOAuthAccessToken(accessToken)
						.setOAuthAccessTokenSecret(accessTokenSecret);

				TwitterFactory tf = new TwitterFactory(cb.build());

				Twitter twitter = tf.getInstance();

				if (isTwitterInstanceAuthenticated(twitter)) {

					return twitter;
				} else {
					return null;
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (InvalidKeyException e) {
				e.printStackTrace();
				return null;
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
				return null;
			} catch (BadPaddingException e) {
				e.printStackTrace();
				return null;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return null;
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
				return null;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String getTwitterAuthenticationURL() throws TwitterException {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(oAuthConsumerKey).setOAuthConsumerSecret(oAuthConsumerKeySecret);

		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();

		RequestToken requestToken = twitter.getOAuthRequestToken();
		lastRequestToken = requestToken;

		return requestToken.getAuthorizationURL();
	}

	public static Twitter getTwitterInstance(String PIN) throws TwitterException {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(oAuthConsumerKey).setOAuthConsumerSecret(oAuthConsumerKeySecret);

		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();

		AccessToken accessToken = twitter.getOAuthAccessToken(lastRequestToken, PIN);
		twitter.setOAuthAccessToken(accessToken);
		if (isTwitterInstanceAuthenticated(twitter)) {
			try (FileOutputStream fos = new FileOutputStream(".config");
					ObjectOutputStream oos = new ObjectOutputStream(fos)) {
				TwitterAccessToken tat = new TwitterAccessToken(accessToken.getToken(), accessToken.getTokenSecret());
				oos.writeObject(tat);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return twitter;
		} else {
			return null;
		}

	}

	public void requestAuthentication() {

	}

}
