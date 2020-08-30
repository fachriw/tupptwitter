package tupp_twitter;

import twitter4j.User;

public class UserEntry {
	private String username;
	private String profilePictureURL;
	private String link;

	public UserEntry(User user) {
		this.setUsername(user.getScreenName());
		this.setProfilePictureURL(user.get400x400ProfileImageURL());
		this.setLink("https://twitter.com/" + user.getScreenName());
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProfilePictureURL() {
		return profilePictureURL;
	}

	public void setProfilePictureURL(String profilePictureURL) {
		this.profilePictureURL = profilePictureURL;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
