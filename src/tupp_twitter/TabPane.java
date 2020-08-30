package tupp_twitter;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TabPane {
	public static ToggleGroup toggleGroup = new ToggleGroup();
	private HBox hbox_tabButtonAnchor;
	private VBox vbox_tabPaneAnchor;
	private static VBox vbox_tabPaneDisplay = new VBox();
	public ArrayList<Node> tabPaneContent;
	public ToggleButton toggleButton_tabButton;
	private static boolean staticFieldsInitialized = false;

	public TabPane(HBox tabButtonAnchor, VBox tabPaneAnchor, String buttonTitle) {
		tabPaneContent = new ArrayList<Node>();
		this.hbox_tabButtonAnchor = tabButtonAnchor;
		this.vbox_tabPaneAnchor = tabPaneAnchor;
		toggleButton_tabButton = new ToggleButton(buttonTitle);
		toggleButton_tabButton.setToggleGroup(toggleGroup);

		hbox_tabButtonAnchor.getChildren().add(toggleButton_tabButton);
		if (staticFieldsInitialized == false) {
			Insets insets = new Insets(10);
			vbox_tabPaneDisplay.setPadding(insets);
			tabPaneAnchor.getChildren().add(vbox_tabPaneDisplay);
			staticFieldsInitialized = true;
		}

		EventHandler<ActionEvent> toggleButton_tabButton_eventHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				setSelected((toggleGroup.getSelectedToggle() == toggleButton_tabButton));

			}
		};
		toggleButton_tabButton.setOnAction(toggleButton_tabButton_eventHandler);
	}

	public void setSelected(boolean select) {
		if (select == true) {
			System.out.println("ay");
			toggleGroup.selectToggle(this.toggleButton_tabButton);
			vbox_tabPaneDisplay.getChildren().clear();
			for (Node node : tabPaneContent) {
				vbox_tabPaneDisplay.getChildren().add(node);
			}

		} else {
			System.out.println("nay");

			toggleGroup.selectToggle(null);
			vbox_tabPaneDisplay.getChildren().clear();

		}
	}
}
