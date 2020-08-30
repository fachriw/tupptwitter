module tupp_twitter
{
    requires org.twitter4j.core;

    requires javafx.controls;

    requires javafx.fxml;

    requires java.scripting;

    requires transitive javafx.graphics;

    requires javafx.base;
    requires javafx.web;
	requires java.desktop;

    exports tupp_twitter;

    opens tupp_twitter;
    // opens gui.pizza;

}