package tupp_twitter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import twitter4j.TwitterException;

public class main extends Application {

	public static void main(String[] args) throws TwitterException {
		// TODO Auto-generated method stub

		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		Presenter p = new Presenter();
		View v = new View(p);
		v.initView();

		Model m = new Model();
		p.setView(v);
		p.setModel(m);
		p.init();

		Scene scene = new Scene(v.getUI());
		arg0.setScene(scene);
		arg0.setTitle("Login");
		arg0.setMaxHeight(1080);
		arg0.setMaxWidth(1920);
		arg0.setWidth(500);
		arg0.setHeight(500);
		arg0.setMinHeight(500);
		arg0.setMinWidth(500);
		v.getUI().prefHeightProperty().bind(arg0.heightProperty());
		arg0.show();
	}

}
