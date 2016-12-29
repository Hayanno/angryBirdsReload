package angrybirdsreload.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/menu.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(getClass().getResource("/css/style.css").toExternalForm());

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/favicon.png")));
        primaryStage.setTitle("Angry Birds Reload | Nicolas Léotier");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(false);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
