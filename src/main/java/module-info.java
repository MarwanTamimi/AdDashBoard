module com.example.addashboardcw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.scripting;
    requires javafx.media;
    requires org.apache.logging.log4j;
    requires nv.websocket.client;
    requires java.desktop;
    requires javafx.swing;
  requires java.logging;

  opens com.example.addashboardcw to javafx.fxml;
    exports com.example.addashboardcw;
}