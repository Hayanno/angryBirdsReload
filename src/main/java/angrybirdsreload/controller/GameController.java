package angrybirdsreload.controller;

import angrybirdsreload.entity.Bird;
import angrybirdsreload.entity.Pig;
import angrybirdsreload.utils.BoundLine;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    List<Pig> pigs = new ArrayList<>();
    List<Bird> birds = new ArrayList<>();

    AnimationTimer gameLoop;
    private Image birdImage, pigImage, harnessImage;
    private ImageView slingshotBackImageView, slingshotFrontImageView;
    private BoundLine boundLineBack, boundLineFront;

    double finalHeight, finalWidth;

    @FXML
    private Button backMenuButton;

    @FXML
    private Pane playField;

    @FXML
    private AnchorPane body;

    @FXML
    private void menuButtonAction() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/menu.fxml"));

        Stage primaryStage = (Stage) backMenuButton.getScene().getWindow();
        primaryStage.getScene().setRoot(root);
    }

    @FXML
    public void startGame() {
        computeFinalBodyDimension();
        loadGame();
        createDecor();
        createPigs();
        createBirds();
        loadNextBird();

        gameLoop = new AnimationTimer() {

            @Override
            public void handle(long currentNanoTime) {
                birds.get(0).move();

                checkCollisions();

                if(birds.isEmpty()) {
                    System.out.println("Game terminé");

                    gameLoop.stop();
                }
                else
                    birds.get(0).updateUI();

                //pigs.forEach(sprite -> sprite.checkRemovability());

                //removeSprites();

                //updateScore();
            }
        };

        gameLoop.start();
    }

    private void loadGame() {
        pigImage = new Image(getClass().getResource("/img/pigs/green_medium.png").toExternalForm());
        birdImage = new Image(getClass().getResource("/img/birds/red.png").toExternalForm());
        harnessImage = new Image(getClass().getResource("/img/static/harness.png").toExternalForm());

        slingshotBackImageView = new ImageView(new Image(getClass().getResource("/img/static/slingshot_back.png").toExternalForm()));
        slingshotFrontImageView = new ImageView(new Image(getClass().getResource("/img/static/slingshot_front.png").toExternalForm()));
    }

    private void createDecor() {
        boundLineBack = new BoundLine();
        boundLineFront = new BoundLine();

        //TODO : revoir toute les positions pour gérer la résolution de l'écran
        double slingshotBackX = 17.0/100 * finalWidth;
        double slingshotBackY = finalHeight - ((double) 36/100 * finalHeight);

        slingshotBackImageView.setLayoutX(slingshotBackX);
        slingshotBackImageView.setLayoutY(slingshotBackY);

        double slingshotFrontX = 15.1/100 * finalWidth;
        double slingshotFrontY = finalHeight - (36.6/100 * finalHeight);

        slingshotFrontImageView.setLayoutX(slingshotFrontX);
        slingshotFrontImageView.setLayoutY(slingshotFrontY);
        slingshotFrontImageView.setRotate(-2);

        DoubleProperty lineBackX = new SimpleDoubleProperty(slingshotBackImageView.getLayoutX() + 30);
        DoubleProperty lineBackY = new SimpleDoubleProperty(slingshotBackImageView.getLayoutY() + 25);
        DoubleProperty lineFrontX = new SimpleDoubleProperty(slingshotFrontImageView.getLayoutX() + 18);
        DoubleProperty lineFrontY = new SimpleDoubleProperty(slingshotFrontImageView.getLayoutY() + 30);

        boundLineBack.setStartProperty(lineBackX, lineBackY);
        boundLineBack.setEndProperty(lineBackX, lineBackY);
        boundLineFront.setStartProperty(lineFrontX, lineFrontY);
        boundLineFront.setEndProperty(lineFrontX, lineFrontY);


        playField.getChildren().add(boundLineBack);
        playField.getChildren().add(slingshotBackImageView);
        playField.getChildren().add(boundLineFront);
        playField.getChildren().add(slingshotFrontImageView);
    }

    private void createBirds() {
        double birdX = 15.6/100 * finalWidth;
        double birdY = finalHeight - (35.9/100 * finalHeight);

        double maxY = finalHeight - (18.8/100 * finalHeight);

        // TODO : Factory ici avec boucle de création en fonction du niveau
        Bird bird = new Bird(birdImage, harnessImage, playField,
                boundLineBack, boundLineFront,
                birdX, birdY,
                0, finalWidth, 0, maxY,
                9, 1, 1);

        birds.add(bird);
    }

    private void createPigs() {
        // TODO : tout ça dans les Settings mon cher ami
        double pigX = 55.6/100 * finalWidth;
        double pigY = finalHeight - (21.8/100 * finalHeight);

        // TODO : Factory ici avec boucle de création en fonction du niveau
        Pig pig = new Pig(pigImage, playField, pigX, pigY, 17, 1, 0);

        pigs.add(pig);
    }

    private void loadNextBird() {
        Bird bird = birds.get(0);

        boundLineFront.toFront();
        slingshotFrontImageView.toFront();
        bird.addListeners();
    }

    private void checkCollisions() {
        for(Pig pig: pigs)
            if(pig.collidesWith(birds.get(0))) {
                System.out.println("Collision");
                pig.getDamagedBy(birds.get(0));
                birds.get(0).setRemovable(true);
                birds.remove(0);
            }
    }

    private void computeFinalBodyDimension() {
        // TODO : le background doit être chargé dynamiquement (si on veut changer de background)
        Image backgroundImage = new Image(getClass().getResource("/img/background.jpg").toExternalForm());

        double windowHeight = body.getScene().getWindow().getHeight();
        double windowWidth = body.getScene().getWindow().getWidth();
        double windowRatio = windowHeight / windowWidth;

        double imgRatio = backgroundImage.getHeight() / backgroundImage.getWidth();

        if (windowRatio > imgRatio) {
            finalHeight = windowHeight;
            finalWidth = (windowHeight / imgRatio);
        }
        else {
            finalWidth = windowWidth;
            finalHeight = (windowWidth * imgRatio);
        }
    }
}
