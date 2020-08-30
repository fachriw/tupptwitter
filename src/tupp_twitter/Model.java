package tupp_twitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import tupp_twitter.TwitterInformation.orderType;
import tupp_twitter.TwitterInformation.sourceType;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class Model {

	private User currentUser;
	private User otherUser;

	private Twitter twitter;

	private TwitterInformation ownTwitterInformation;
	private TwitterInformation otherTwitterInformation;

	public Model() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("29sYaMwTacaYv9GCiqsijEJIi")
				.setOAuthConsumerSecret("Wf3UzAgrUWk9jvswMEgt0Yepr7OPaaX3iS7ejJwncnBe3lgxCd");

		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();

	}

	public ArrayList<Status> API_REQUEST_getUserTweets(boolean isSelf) throws TwitterException {
		ArrayList<Status> statuses = new ArrayList<Status>();
		User targetUser;
		if (isSelf)
			targetUser = currentUser;
		else {
			targetUser = otherUser;
		}
		for (int i = 1; targetUser.getStatusesCount() > (i - 1) * 3200; i++) {
			Paging p;

			p = new Paging(i, 3200);

			ResponseList<Status> ps = twitter.getUserTimeline(targetUser.getId(), p);
			System.out.println(ps.getRateLimitStatus().getRemaining() + " remaining queries");
			for (Status s : ps) {

				if (!s.isRetweet()) {
					statuses.add(s);
				}

			}

		}
		return statuses;
	}

	public ArrayList<Status> API_REQUEST_getMentionsTweets(boolean isSelf) throws TwitterException {
		ArrayList<Status> statuses = new ArrayList<Status>();

		for (int i = 1;; i++) {
			Paging p;

			p = new Paging(i, 800);

			ResponseList<Status> ps = twitter.getMentionsTimeline(p);

			System.out.println(ps.getRateLimitStatus().getRemaining() + " remaining queries");
			for (Status s : ps) {

				statuses.add(s);

			}
			if (ps.size() < 800)
				break;
		}
		return statuses;
	}

	public ArrayList<User> API_REQUEST_getFollowers(boolean isSelf) throws TwitterException {
		ArrayList<User> followers = new ArrayList<User>();
		User targetUser;
		if (isSelf)
			targetUser = currentUser;
		else {
			targetUser = otherUser;
		}

		PagableResponseList<User> fo = twitter.getFollowersList(targetUser.getScreenName(), -1, 100);
		followers.addAll(fo);

		System.out.println(fo.getRateLimitStatus().getRemaining() + " remaining queries");

		return followers;

	}

	public ArrayList<Integer> getStatusIndicesByRetweeted(ArrayList<Status> statuses) {
		Integer statusIndices[] = new Integer[statuses.size()];

		for (int i = 0; i < statuses.size(); i++) {
			statusIndices[i] = i;
		}
		ArrayList<Integer> statusIndicesByRetweeted = new ArrayList<Integer>(Arrays.asList(statusIndices));
		Collections.sort(statusIndicesByRetweeted, new Comparator<Integer>() {

			@Override
			public int compare(Integer arg0, Integer arg1) {
				if (statuses.get(arg0).getRetweetCount() > statuses.get(arg1).getRetweetCount()) {
					return -1;
				} else if (statuses.get(arg0).getRetweetCount() < statuses.get(arg1).getRetweetCount()) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		return statusIndicesByRetweeted;
	}

	public ArrayList<Integer> getStatusIndicesByFavorited(ArrayList<Status> statuses) {
		Integer statusIndices[] = new Integer[statuses.size()];

		for (int i = 0; i < statuses.size(); i++) {
			statusIndices[i] = i;
		}
		ArrayList<Integer> statusIndicesByFavorited = new ArrayList<Integer>(Arrays.asList(statusIndices));

		Collections.sort(statusIndicesByFavorited, new Comparator<Integer>() {

			@Override
			public int compare(Integer arg0, Integer arg1) {
				if (statuses.get(arg0).getFavoriteCount() > statuses.get(arg1).getFavoriteCount()) {
					return -1;
				} else if (statuses.get(arg0).getFavoriteCount() < statuses.get(arg1).getFavoriteCount()) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		return statusIndicesByFavorited;
	}

	public Map<TwitterInformation.timeFrame, ArrayList<Integer>> getStatusIndicesByTimeFrame(Status[] statusArray,
			ArrayList<Integer> indices) {
		Map<TwitterInformation.timeFrame, ArrayList<Integer>> statuses = new HashMap<TwitterInformation.timeFrame, ArrayList<Integer>>();
		ArrayList<Integer> statusIndicesByFavorited_yesterday = new ArrayList<Integer>();
		ArrayList<Integer> statusIndicesByFavorited_7days = new ArrayList<Integer>();
		ArrayList<Integer> statusIndicesByFavorited_30days = new ArrayList<Integer>();
		ArrayList<Integer> statusIndicesByFavorited_3months = new ArrayList<Integer>();
		ArrayList<Integer> statusIndicesByFavorited_6months = new ArrayList<Integer>();
		ArrayList<Integer> statusIndicesByFavorited_12months = new ArrayList<Integer>();

		for (Integer index : indices) {
			Date now = new Date();
			Date then = statusArray[index].getCreatedAt();
			long startTime = then.getTime();
			long endTime = now.getTime();
			long diffTime = endTime - startTime;
			long diffDays = diffTime / (1000 * 60 * 60 * 24);
			if (diffDays <= 360) {
				statusIndicesByFavorited_12months.add(index);
			}
			if (diffDays <= 180) {
				statusIndicesByFavorited_6months.add(index);
			}
			if (diffDays <= 90) {
				statusIndicesByFavorited_3months.add(index);
			}
			if (diffDays <= 30) {
				statusIndicesByFavorited_30days.add(index);
			}
			if (diffDays <= 7) {
				statusIndicesByFavorited_7days.add(index);
			}
			if (diffDays <= 1) {
				statusIndicesByFavorited_yesterday.add(index);
			}

		}

		statuses.put(TwitterInformation.timeFrame.YESTERDAY, statusIndicesByFavorited_yesterday);
		statuses.put(TwitterInformation.timeFrame.WEEK, statusIndicesByFavorited_7days);
		statuses.put(TwitterInformation.timeFrame.MONTH, statusIndicesByFavorited_30days);
		statuses.put(TwitterInformation.timeFrame.THREE_MONTHS, statusIndicesByFavorited_3months);
		statuses.put(TwitterInformation.timeFrame.SIX_MONTHS, statusIndicesByFavorited_6months);
		statuses.put(TwitterInformation.timeFrame.TWELVE_MONTHS, statusIndicesByFavorited_12months);
		statuses.put(TwitterInformation.timeFrame.EVER, indices);

		return statuses;

	}

	public void packStatusIndices(TwitterInformation ti, sourceType st, ArrayList<Status> statuses) {

		ti.setTweets(st, statuses);

		ArrayList<Integer> statusIndicesByFavorited = getStatusIndicesByFavorited(statuses);
		ArrayList<Integer> statusIndicesByRetweeted = getStatusIndicesByRetweeted(statuses);

		Status[] statusesArray = statuses.toArray(new Status[0]);

		ti.setIndices(st, orderType.FAVORITED, getStatusIndicesByTimeFrame(statusesArray, statusIndicesByFavorited));
		ti.setIndices(st, orderType.RETWEETED, getStatusIndicesByTimeFrame(statusesArray, statusIndicesByRetweeted));
	}

	public TwitterInformation getTwitterInfo(boolean isSelf) throws TwitterException {
		// TODO Auto-generated method stub

		User targetUser;
		if (isSelf) {
			if (ownTwitterInformation != null)
				return ownTwitterInformation;
			else {
				targetUser = currentUser;
			}
		} else {
			if (otherTwitterInformation != null)
				return otherTwitterInformation;
			else {
				targetUser = otherUser;
			}
		}

		TwitterInformation t = new TwitterInformation();

		t.setBio(targetUser.getDescription());
		t.setAmountFollowers(targetUser.getFollowersCount());
		t.setAmountFollowed(targetUser.getFriendsCount());

		ArrayList<Status> ustatuses = API_REQUEST_getUserTweets(isSelf);

		int likes = 0;
		int retweets = 0;
		for (Status status : ustatuses) {
			likes += status.getFavoriteCount();
			retweets += status.getRetweetCount();
		}

		t.setAmountLiked(likes);
		t.setAmountRetweeted(retweets);

		packStatusIndices(t, sourceType.USERTIMELINE, ustatuses);

		ArrayList<Status> mstatuses = API_REQUEST_getMentionsTweets(isSelf);

		packStatusIndices(t, sourceType.MENTIONSTIMELINE, mstatuses);

		ArrayList<User> followersArrayList = API_REQUEST_getFollowers(isSelf);

		t.setFollowers(followersArrayList);

		if (isSelf) {
			ownTwitterInformation = t;
			return ownTwitterInformation;
		} else {
			otherTwitterInformation = t;
			return otherTwitterInformation;
		}

	}

	public void useExistingTwitterAuthentication() throws AuthenticationException, TwitterException {
		Twitter twitter = TwitterAuthentication.getTwitterInstance();
		if (twitter == null) {
			throw new AuthenticationException("Authentication failed");
		} else {
			this.twitter = twitter;
			currentUser = twitter.verifyCredentials();

		}
	}

	public String getTwitterAuthenticationURL() throws TwitterException {
		return TwitterAuthentication.getTwitterAuthenticationURL();

	}

	public void setAuthentication(Twitter twitter) throws TwitterException {
		this.twitter = twitter;
		currentUser = twitter.verifyCredentials();

	}

	public void useTwitterAuthenticationPin(String pin) throws TwitterException {
		Twitter twitter = TwitterAuthentication.getTwitterInstance(pin);
		this.twitter = twitter;
		currentUser = twitter.verifyCredentials();

	}

	public TwitterInformation getTwitterInfo(String username) throws TwitterException {
		// TODO Auto-generated method stub
		if (otherUser != null) {
			if (otherUser.getScreenName() != username) {
				otherUser = null;
			}
		}
		otherUser = twitter.showUser(username);

		return getTwitterInfo(false);
	}
}
