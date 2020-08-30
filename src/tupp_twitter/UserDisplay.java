package tupp_twitter;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UserDisplay extends VBox {
	private HBox hbox_settingsArea;

	private GridPane gridPane_userTable;
	private Label label_status;
	private Label label_header;

	UserDisplay(String header) {

		Insets insets = new Insets(10);
		this.setPadding(insets);

		label_header = new Label(header);
		label_header.setStyle("-fx-font: 24 arial;");

		hbox_settingsArea = new HBox();
		hbox_settingsArea.getChildren().add(new Label("Showing up to 100 latest followers"));

		label_status = new Label();

		ScrollPane sPane = new ScrollPane();

		gridPane_userTable = new GridPane();
		gridPane_userTable.setHgap(10);
		gridPane_userTable.setVgap(50);
		gridPane_userTable.setPadding(new Insets(2, 10, 0, 10));

		for (int i = 0; i < 4; i++) {
			ColumnConstraints cc = new ColumnConstraints();
			cc.setPercentWidth(25);

			gridPane_userTable.getColumnConstraints().add(cc);

		}

		sPane.setContent(gridPane_userTable);

		this.getChildren().addAll(label_header, hbox_settingsArea, label_status, sPane);
		sPane.setFitToHeight(true);
		sPane.setFitToWidth(true);
	}

	public void clear() {
		this.gridPane_userTable.getChildren().clear();

	}

	public void showMessage(String message) {
		this.label_status.setText(message);
	}

	public void setUserEntries(ArrayList<UserEntry> userEntries) {
		this.clear();
		int row = 0;
		int col = 0;
		for (UserEntry user : userEntries) {

			if (col == 4) {
				col = 0;
				row++;
			}

			Hyperlink linkLabel = new Hyperlink(user.getUsername());
			linkLabel.setWrapText(true);
			VBox entryCellBox = new VBox();

			Image image = new Image(user.getProfilePictureURL(), true);

			ImageView imageView = new ImageView(image);
			imageView.setFitWidth(100);
			imageView.setFitHeight(100);

			entryCellBox.getChildren().add(imageView);
			entryCellBox.getChildren().add(linkLabel);
			gridPane_userTable.add(entryCellBox, col++, row);

			linkLabel.setOnAction(e -> {
				if (Desktop.isDesktopSupported()) {
					new Thread(() -> {
						try {
							Desktop.getDesktop().browse(new URI(user.getLink()));
						} catch (IOException | URISyntaxException e1) {
							e1.printStackTrace();
						}
					}).start();
				}
			});

			imageView.setOnMouseClicked(e -> {
				if (Desktop.isDesktopSupported()) {
					new Thread(() -> {
						try {
							Desktop.getDesktop().browse(new URI(user.getLink()));
						} catch (IOException | URISyntaxException e1) {
							e1.printStackTrace();
						}
					}).start();
				}
			});
			imageView.setCursor(Cursor.HAND);

		}
	}
}
