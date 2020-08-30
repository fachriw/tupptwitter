package tupp_twitter;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class TweetDisplay extends VBox {
	private HBox hbox_settingsArea;
	public static String timeFrames[] = TwitterInformation.timeFrameStrings;
	public static String orderType[] = TwitterInformation.orderTypeStrings;

	private String tableColumnTitles[];
	private int tableColumnWidths[];

	public Label label_header;
	public ComboBox comboBox_timeFrameSelector;
	public ComboBox comboBox_orderTypeSelector;

	public Label label_status;
	private GridPane gridPane_titleTable;
	public GridPane gridPane_tweetTable;
	private boolean showUsername = false;

	TweetDisplay(String header) {
		tableColumnTitles = new String[] { "Text", "Likes", "Retweets", "Link" };
		tableColumnWidths = new int[] { 60, 10, 10, 20 };

		Insets insets = new Insets(10);
		this.setPadding(insets);

		label_header = new Label(header);
		label_header.setStyle("-fx-font: 24 arial;");

		hbox_settingsArea = new HBox();
		hbox_settingsArea.getChildren().add(new Label("Show up to 100 tweets since"));
		comboBox_timeFrameSelector = new ComboBox(FXCollections.observableArrayList(timeFrames));
		comboBox_timeFrameSelector.getSelectionModel().selectFirst();
		hbox_settingsArea.getChildren().add(comboBox_timeFrameSelector);
		hbox_settingsArea.getChildren().add(new Label(", sorted by"));
		comboBox_orderTypeSelector = new ComboBox(FXCollections.observableArrayList(orderType));
		comboBox_orderTypeSelector.getSelectionModel().selectFirst();

		hbox_settingsArea.getChildren().add(comboBox_orderTypeSelector);

		label_status = new Label();

		ScrollPane sPane = new ScrollPane();

		gridPane_titleTable = new GridPane();
		gridPane_titleTable.setHgap(10);
		gridPane_titleTable.setVgap(0);
		gridPane_titleTable.setPadding(new Insets(2, 10, 0, 10));

		gridPane_tweetTable = new GridPane();
		gridPane_tweetTable.setHgap(10); // horizontal gap in pixels => that's what you are asking for
		gridPane_tweetTable.setVgap(0); // vertical gap in pixels
		gridPane_tweetTable.setPadding(new Insets(2, 10, 0, 10));
		sPane.setContent(gridPane_tweetTable);
		sPane.setFitToWidth(true);

		setupDisplay();

		this.getChildren().addAll(label_header, hbox_settingsArea, label_status, gridPane_titleTable, sPane);
		sPane.setFitToHeight(true);
		sPane.setFitToWidth(true);
	}

	private void setupDisplay() {
		this.gridPane_titleTable.getChildren().clear();

		this.gridPane_titleTable.getColumnConstraints().clear();
		this.gridPane_tweetTable.getColumnConstraints().clear();
		for (int i = 0; i < tableColumnTitles.length; i++) {
			ColumnConstraints cc = new ColumnConstraints();
			cc.setPercentWidth(tableColumnWidths[i]);
			gridPane_titleTable.getColumnConstraints().add(cc);
			gridPane_tweetTable.getColumnConstraints().add(cc);
			gridPane_titleTable.add(new Label(tableColumnTitles[i]), i % tableColumnTitles.length, 0);

		}
		this.gridPane_tweetTable.getRowConstraints().clear();
		RowConstraints rc = new RowConstraints();
		rc.setMinHeight(0);
		this.gridPane_tweetTable.getRowConstraints().add(rc);
	}

	public void enableUsernameColumn() {
		if (showUsername == false) {

			tableColumnTitles = new String[] { "Username", "Text", "Likes", "Retweets", "Link" };
			tableColumnWidths = new int[] { 10, 50, 10, 10, 20 };

			showUsername = true;
			setupDisplay();

		}

	}

	public void clear() {
		this.gridPane_tweetTable.getChildren().clear();
		this.label_status.setText("");

	}

	public void showMessage(String message) {
		this.label_status.setText(message);
	}

	public void setTweetEntries(ArrayList<TweetEntry> tweetEntries) {
		this.clear();
		int row = 1;
		this.setupDisplay();

		for (TweetEntry tweet : tweetEntries) {
			int col = 0;
			if (this.showUsername) {
				Hyperlink usernameLabel = new Hyperlink(tweet.getUsername());
				usernameLabel.setWrapText(true);

				usernameLabel.setOnAction(e -> {
					if (Desktop.isDesktopSupported()) {
						new Thread(() -> {
							try {
								Desktop.getDesktop().browse(new URI("https://twitter.com/" + tweet.getUsername()));
							} catch (IOException | URISyntaxException e1) {
								e1.printStackTrace();
							}
						}).start();
					}
				});
				this.gridPane_tweetTable.add(usernameLabel, col++, row);

			}
			Label textLabel = new Label(tweet.getText());
			textLabel.setWrapText(true);

			this.gridPane_tweetTable.add(textLabel, col++, row);
			this.gridPane_tweetTable.add(new Label(tweet.getFavorited()), col++, row);
			this.gridPane_tweetTable.add(new Label(tweet.getRetweeted()), col++, row);
			Hyperlink linkLabel = new Hyperlink(tweet.getLink());
			linkLabel.setWrapText(true);
			this.gridPane_tweetTable.add(linkLabel, col++, row);

			linkLabel.setOnAction(e -> {
				if (Desktop.isDesktopSupported()) {
					new Thread(() -> {
						try {
							Desktop.getDesktop().browse(new URI(tweet.getLink()));
						} catch (IOException | URISyntaxException e1) {
							e1.printStackTrace();
						}
					}).start();
				}
			});

			RowConstraints rc = new RowConstraints();
			rc.setVgrow(Priority.ALWAYS);
			rc.setMinHeight(100);
			this.gridPane_tweetTable.getRowConstraints().add(rc);

			row++;

		}
	}
}
