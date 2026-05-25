package app;

import controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ParkingApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1. Instantiate the context containing the full graph and all core managers
        AppContext context = new AppContext();

        // 2. Load the main window skeleton FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
        Parent root = loader.load();

        // 3. Acquire reference to the main controller and perform global environment injection
        MainController mainController = loader.getController();
        mainController.initContext(context);

        // 4. Assemble the stage and render
        Scene scene = new Scene(root, 1100, 740);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        primaryStage.setTitle("Smart Parking System - Sprint 3 Production");
        primaryStage.setResizable(false); // Keep fixed layout
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
