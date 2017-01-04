package angrybirdsreload.controller;

import angrybirdsreload.entity.Bird;
import angrybirdsreload.entity.Pig;
import angrybirdsreload.entity.Slingshot;
import angrybirdsreload.settings.GameSettingsModule;
import angrybirdsreload.settings.ISettings;
import angrybirdsreload.utils.Input;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private Injector applicationSettingsInjector;

    private Input input;
    private Slingshot slingshot;
    private List<Pig> pigs = new ArrayList<>();
    private List<Bird> birds = new ArrayList<>();

    private AnimationTimer gameLoop;
    private Image birdImage, pigImage, harnessImage, slingshotBackImage, slingshotFrontImage;

    @FXML
    private Button backMenuButton;

    @FXML
    private Pane playField;

    @FXML
    private void menuButtonAction() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/menu.fxml"));

        Stage primaryStage = (Stage) backMenuButton.getScene().getWindow();
        primaryStage.getScene().setRoot(root);
    }

    @FXML
    void startGame() {
        applicationSettingsInjector = Guice.createInjector(new GameSettingsModule());
        ISettings applicationSettings = applicationSettingsInjector.getInstance(ISettings.class);
        applicationSettings.load();

        loadGame();
        createDecor();
        createPigs();
        createBirds();
        loadNextBird();

        gameLoop = new AnimationTimer() {

            @Override
            public void handle(long currentNanoTime) {
                Bird bird = currentBird();

                bird.processInput(input);
                slingshot.processInput(input);
                bird.move();

                checkCollisions();

                if(birds.isEmpty()) {
                    System.out.println("Game terminé");

                    gameLoop.stop();
                }
                else
                    bird.updateUI();

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
        slingshotBackImage = new Image(getClass().getResource("/img/static/slingshot_back.png").toExternalForm());
        slingshotFrontImage = new Image(getClass().getResource("/img/static/slingshot_front.png").toExternalForm());
    }

    private void createDecor() {
        // TODO : dans les settings ça monsieur
        Double slingshotBackX = 17.0/100,
                slingshotBackY = 36.0/100,
                slingshotFrontX = 15.1/100,
                slingshotFrontY = 36.6/100;

        slingshot = applicationSettingsInjector.getInstance(Slingshot.class);
        slingshot.init(slingshotBackImage, slingshotFrontImage, harnessImage, playField,
                slingshotBackX, slingshotBackY, slingshotFrontX, slingshotFrontY);
    }

    /*
    private void moveSlingshotLines() {
        boundLineBack.setEndProperty(event.getSceneX() + dragStartX + 10, event.getSceneY() + dragStartY + 35);
        boundLineFront.setEndProperty(event.getSceneX() + dragStartX + 10, event.getSceneY() + dragStartY + 35);
    }
    */

    private void createBirds() {
        // TODO : dans les settings ça aussi
        double birdX = 15.6/100,
                birdY = 35.9/100,
                maxY = 18.8/100,
                maxX = 0;

        // TODO : Factory ici avec boucle de création en fonction du niveau
        Bird bird = applicationSettingsInjector.getInstance(Bird.class);
        bird.init(birdImage, playField,
                slingshot.getDoubleBoundLine(),
                birdX, birdY,
                0, maxX, 0, maxY,
                9, 1, 1);

        birds.add(bird);
    }

    private void createPigs() {
        // TODO : LevelData
        double pigX = 55.6/100,
                pigY = 21.8/100;

        // TODO : Factory ici avec boucle de création en fonction du niveau
        Pig pig = applicationSettingsInjector.getInstance(Pig.class);
        pig.init(pigImage, playField, pigX, pigY, 17, 1, 0);

        pigs.add(pig);
    }

    private void loadNextBird() {
        this.input = new Input(currentBird().getView());

        slingshot.getDoubleBoundLine().getFront().toFront();
        slingshot.getHarness().toFront();
        slingshot.getFront().toFront();
    }

    private void checkCollisions() {
        for(Pig pig: pigs)
            if(pig.collidesWith(currentBird())) {
                System.out.println("Collision");
                pig.getDamagedBy(currentBird());
                currentBird().setRemovable(true);
                birds.remove(0);
            }
    }

    private Bird currentBird() {
        return birds.get(0);
    }
}
