package tupp_twitter;

import java.util.ArrayList;

import twitter4j.TwitterException;

public class Presenter {
	private View view;

	private Model model;

	public Presenter() {
	}

	public void setView(View view) {
		this.view = view;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void init() {
		try {
			this.model.useExistingTwitterAuthentication();

			this.view.showAccountDisplay();
		} catch (AuthenticationException e) {
			try {
				this.view.showAuthenticationDisplay(this.model.getTwitterAuthenticationURL());
			} catch (TwitterException e1) {

				e1.printStackTrace();
				view.showFatalError();
			}

		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void requestAuthentication(String pin) {
		try {
			this.model.useTwitterAuthenticationPin(pin);
			this.view.showAccountDisplay();

		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.view.showAuthenticationFailedMessage();
			try {
				this.view.showAuthenticationDisplay(this.model.getTwitterAuthenticationURL());
			} catch (TwitterException e1) {

				e1.printStackTrace();
				view.showFatalError();
			}

		}

	}

	public void requestUserTweets(String orderType, String timeFrame, int amount, boolean fromSelf) {
		TwitterInformation ti;
		try {
			ti = model.getTwitterInfo(fromSelf);
			ArrayList<TweetEntry> tweetTableEntries = ti.getTweetEntries(TwitterInformation.sourceType.USERTIMELINE,
					orderType, timeFrame, amount);
			view.showMostFavoritedUserTweets(tweetTableEntries);

		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			view.showUserTweetDisplayMessage("No new tweets since " + timeFrame);
		}

	}

	public void requestMentionsTweets(String orderType, String timeFrame, int amount, boolean fromSelf) {
		TwitterInformation ti;
		try {
			ti = model.getTwitterInfo(fromSelf);
			ArrayList<TweetEntry> tweetTableEntries = ti.getTweetEntries(TwitterInformation.sourceType.MENTIONSTIMELINE,
					orderType, timeFrame, amount);
			view.showMostFavoritedMentionsTweets(tweetTableEntries);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			view.showMentionsTweetDisplayMessage("No new mentions since " + timeFrame);
		}

	}

	public void requestInfo() {
		try {
			view.resetAccountDisplay();
			TwitterInformation ti = model.getTwitterInfo(true);
			this.view.showAmountFollowed(ti.getAmountFollowed());
			this.view.showAmountFollowers(ti.getAmountFollowers());
			this.view.showBio(ti.getBio());
			this.view.showAmountTweets(ti.getAmountUserTweets());
			this.view.showAmountMentioned(ti.getAmountMentionsTweets());
			this.view.showAmountLiked(ti.getAmountLiked());
			this.view.showAmountRetweeted(ti.getAmountRetweeted());
			this.view.showFollowers(ti.getFollowersUserEntries());

		} catch (TwitterException e) {
			view.showErrorMessage(e.getMessage());
		}

	}

	public void requestInfo(String username) {
		// TODO Auto-generated method stub
		try {
			view.resetAccountDisplay();
			TwitterInformation ti = model.getTwitterInfo(username);
			this.view.showAmountFollowed(ti.getAmountFollowed());
			this.view.showAmountFollowers(ti.getAmountFollowers());
			this.view.showBio(ti.getBio());
			this.view.showAmountTweets(ti.getAmountUserTweets());
			this.view.showAmountMentioned(ti.getAmountMentionsTweets());
			this.view.showAmountLiked(ti.getAmountLiked());
			this.view.showAmountRetweeted(ti.getAmountRetweeted());

		} catch (TwitterException e) {
			view.showErrorMessage(e.getMessage());
		}

	}

}
