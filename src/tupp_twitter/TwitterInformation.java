package tupp_twitter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter4j.Status;
import twitter4j.User;

public class TwitterInformation implements Serializable {
	private String bio;

	private int amountUserTweets;
	private int amountMentionsTweets;

	private int amountFollowers;

	private int amountFollowed;

	private int amountLiked;
	private int amountRetweeted;

	private ArrayList<Status> userTweets;
	private ArrayList<Status> mentionsTweets;

	private ArrayList<User> followers;

	public static enum timeFrame {
		YESTERDAY, WEEK, MONTH, THREE_MONTHS, SIX_MONTHS, TWELVE_MONTHS, EVER
	}

	public static String[] timeFrameStrings = { "Yesterday", "Last week", "Last month", "Last 3 months",
			"Last 6 months", "Last year", "Ever" };

	private Map<String, timeFrame> timeFramesByString;

	public static enum orderType {
		FAVORITED, RETWEETED
	}

	public static String[] orderTypeStrings = { "Amount likes", "Amount retweets" };

	private Map<String, orderType> orderTypesByString;

	public static enum sourceType {
		USERTIMELINE, MENTIONSTIMELINE
	}

	private Map<timeFrame, ArrayList<Integer>> userIndicesByFavorited;
	private Map<timeFrame, ArrayList<Integer>> userIndicesByRetweeted;
	private Map<timeFrame, ArrayList<Integer>> mentionsIndicesByFavorited;
	private Map<timeFrame, ArrayList<Integer>> mentionsIndicesByRetweeted;

	TwitterInformation() {
		timeFramesByString = new HashMap<String, timeFrame>();
		timeFramesByString.put("Yesterday", timeFrame.YESTERDAY);
		timeFramesByString.put("Last week", timeFrame.WEEK);
		timeFramesByString.put("Last month", timeFrame.MONTH);
		timeFramesByString.put("Last 3 months", timeFrame.THREE_MONTHS);
		timeFramesByString.put("Last 6 months", timeFrame.SIX_MONTHS);
		timeFramesByString.put("Last year", timeFrame.TWELVE_MONTHS);
		timeFramesByString.put("Ever", timeFrame.EVER);

		orderTypesByString = new HashMap<String, orderType>();
		orderTypesByString.put("Amount likes", orderType.FAVORITED);
		orderTypesByString.put("Amounted retweets", orderType.RETWEETED);

		userIndicesByFavorited = new HashMap<timeFrame, ArrayList<Integer>>();
		userIndicesByRetweeted = new HashMap<timeFrame, ArrayList<Integer>>();
		mentionsIndicesByFavorited = new HashMap<timeFrame, ArrayList<Integer>>();
		mentionsIndicesByRetweeted = new HashMap<timeFrame, ArrayList<Integer>>();

		for (timeFrame t : timeFrame.values()) {
			userIndicesByFavorited.put(t, new ArrayList<Integer>());
			userIndicesByRetweeted.put(t, new ArrayList<Integer>());
			mentionsIndicesByFavorited.put(t, new ArrayList<Integer>());
			mentionsIndicesByRetweeted.put(t, new ArrayList<Integer>());

		}

	}

	public ArrayList<TweetEntry> getTweetEntries(sourceType st, orderType ot, timeFrame tf, int amount)
			throws ArrayIndexOutOfBoundsException {

		ArrayList<Status> statuses = getTweets(st, ot, tf, amount);
		ArrayList<TweetEntry> tweetEntries = new ArrayList<TweetEntry>();
		for (Status status : statuses) {
			tweetEntries.add(new TweetEntry(status));
		}
		return tweetEntries;

	}

	public ArrayList<TweetEntry> getTweetEntries(sourceType st, String ot, String tf, int amount)
			throws ArrayIndexOutOfBoundsException {

		return getTweetEntries(st, orderTypesByString.get(ot), timeFramesByString.get(tf), amount);

	}

	public ArrayList<Status> getTweets(sourceType st, orderType ot, timeFrame tf, int amount)
			throws ArrayIndexOutOfBoundsException {
		ArrayList<Integer> selectedList;

		if (st == sourceType.USERTIMELINE) {
			if (ot == orderType.FAVORITED) {
				selectedList = userIndicesByFavorited.get(tf);
			} else {
				selectedList = userIndicesByRetweeted.get(tf);
			}
		} else { // MENTIONSTIMELINE
			if (ot == orderType.FAVORITED) {
				selectedList = mentionsIndicesByFavorited.get(tf);
			} else {
				selectedList = mentionsIndicesByRetweeted.get(tf);
			}
		}

		List<Integer> selectedSubList;
		if (selectedList.size() == 0) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (selectedList.size() < amount) {
			selectedSubList = selectedList.subList(0, selectedList.size());

		} else {
			selectedSubList = selectedList.subList(0, amount);
		}

		ArrayList<Status> selectedStatuses = new ArrayList<Status>();
		for (int index : selectedSubList) {
			selectedStatuses.add(this.getUserTweetAt(index));
		}

		return selectedStatuses;

	}

	public ArrayList<Status> getTweets(sourceType st, orderType ot, String tf, int amount)
			throws ArrayIndexOutOfBoundsException {

		return getTweets(st, ot, timeFramesByString.get(tf), amount);

	}

	public ArrayList<Status> getTweets(sourceType st, String ot, String tf, int amount)
			throws ArrayIndexOutOfBoundsException {

		return getTweets(st, orderTypesByString.get(ot), timeFramesByString.get(tf), amount);

	}

	public void setIndices(sourceType st, orderType ot, timeFrame tf, ArrayList<Integer> indices) {
		if (st == sourceType.USERTIMELINE) {
			if (ot == orderType.FAVORITED) {
				userIndicesByFavorited.replace(tf, indices);
			} else if (ot == orderType.RETWEETED) {
				System.out.println("setting retweeted tweets");
				userIndicesByRetweeted.replace(tf, indices);
				System.out.println(userIndicesByRetweeted.get(tf).size());
			}
		} else if (st == sourceType.MENTIONSTIMELINE) {
			if (ot == orderType.FAVORITED) {
				mentionsIndicesByFavorited.replace(tf, indices);
			} else if (ot == orderType.RETWEETED) {
				System.out.println("setting retweeted mentions");
				mentionsIndicesByRetweeted.replace(tf, indices);
				System.out.println(mentionsIndicesByRetweeted.get(tf).size());
			}
		}

	}

	public void setIndices(sourceType st, orderType ot, Map<TwitterInformation.timeFrame, ArrayList<Integer>> indices) {
		if (st == sourceType.USERTIMELINE) {
			if (ot == orderType.FAVORITED) {

				userIndicesByFavorited = indices;
			} else if (ot == orderType.RETWEETED) {
				userIndicesByRetweeted = indices;
			}
		} else if (st == sourceType.MENTIONSTIMELINE) {
			if (ot == orderType.FAVORITED) {
				mentionsIndicesByFavorited = indices;
			} else if (ot == orderType.RETWEETED) {
				System.out.println("setting retweeted mentions");

				mentionsIndicesByRetweeted = indices;
				System.out.println(mentionsIndicesByRetweeted.get(timeFrame.MONTH));
			}
		}
	}

	public int getAmountUserTweets() {
		return amountUserTweets;
	}

	public int getAmountMentionsTweets() {
		return amountMentionsTweets;
	}

	public void setAmountUserTweets(int amountTweets) {
		this.amountUserTweets = amountTweets;
	}

	public void setAmountMentionsTweets(int amountTweets) {
		this.amountMentionsTweets = amountTweets;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public int getAmountFollowers() {
		return amountFollowers;
	}

	public void setAmountFollowers(int amountFollowers) {
		this.amountFollowers = amountFollowers;
	}

	public int getAmountFollowed() {
		return amountFollowed;
	}

	public void setAmountFollowed(int amountFollowed) {
		this.amountFollowed = amountFollowed;
	}

	public ArrayList<Status> getStatuses() {
		return userTweets;
	}

	public void setUserTweets(ArrayList<Status> statuses) {
		this.userTweets = statuses;
	}

	public void setMentionsTweets(ArrayList<Status> statuses) {
		this.mentionsTweets = statuses;
	}

	public void setTweets(sourceType st, ArrayList<Status> statuses) {
		if (st == sourceType.USERTIMELINE) {
			this.userTweets = statuses;
		} else if (st == sourceType.MENTIONSTIMELINE) {
			this.mentionsTweets = statuses;
		}
	}

	public Status getUserTweetAt(int index) {
		return this.userTweets.get(index);
	}

	public Status getMentionsTweetAt(int index) {
		return this.mentionsTweets.get(index);
	}

	public int getAmountLiked() {
		return amountLiked;
	}

	public void setAmountLiked(int amountLiked) {
		this.amountLiked = amountLiked;
	}

	public int getAmountRetweeted() {
		return amountRetweeted;
	}

	public void setAmountRetweeted(int amountRetweeted) {
		this.amountRetweeted = amountRetweeted;
	}

	public ArrayList<User> getFollowers() {
		return followers;
	}

	public void setFollowers(ArrayList<User> followers) {
		this.followers = followers;
	}

	public ArrayList<UserEntry> getFollowersUserEntries() {
		System.out.println("entering getfollowersuerentries");
		ArrayList<User> followers = this.followers;
		ArrayList<UserEntry> userEntries = new ArrayList<UserEntry>();
		for (User follower : this.followers) {
			userEntries.add(new UserEntry(follower));
			System.out.println(follower.getScreenName());
		}
		return userEntries;

	}

}
