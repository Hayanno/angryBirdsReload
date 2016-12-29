package angrybirdsreload.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private Button closeButton, startButton;

    @FXML
    private void closeButtonAction(){
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }

    @FXML
    private void startButtonAction() throws Exception {
        // It's ugly, but it's the only way, swing is not so good...
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/game.fxml"));
        Parent root = loader.load();
        GameController controller = loader.getController();

        Stage primaryStage = (Stage) startButton.getScene().getWindow();
        primaryStage.getScene().setRoot(root);

        controller.startGame();
    }
}


