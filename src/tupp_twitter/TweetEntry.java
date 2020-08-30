package tupp_twitter;

import twitter4j.Status;

public class TweetEntry {

	private String username;
	private String text;
	private String favorited;
	private String retweeted;
	private String link;

	public TweetEntry(Status s) {

		username = s.getUser().getScreenName();
		text = s.getText();
		favorited = Integer.toString(s.getFavoriteCount());
		retweeted = Integer.toString(s.getRetweetCount());
		link = "https://twitter.com/" + s.getUser().getScreenName() + "/status/" + s.getId();

	}

	public String getText() {
		return text;
	}

	public String getFavorited() {
		return favorited;
	}

	public String getRetweeted() {
		return retweeted;
	}

	public String getLink() {
		return link;
	}

	public String getUsername() {
		return username;
	}

}
