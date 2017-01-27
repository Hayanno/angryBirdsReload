package angrybirdsreload.controller;

import angrybirdsreload.entity.Bird;
import angrybirdsreload.entity.EntityFactory;
import angrybirdsreload.entity.Pig;
import angrybirdsreload.decor.Slingshot;
import angrybirdsreload.entity.SpriteBase;
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
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameController {

    private Bird currentBird;
    private List<Pig> pigs = new ArrayList<>();
    private List<Bird> birds = new ArrayList<>();
    private Injector applicationSettingsInjector;
    private ISettings applicationSettings;
    private EntityFactory entityFactory;
    private AnimationTimer gameLoop;
    private Slingshot slingshot;
    private Input input;

    private int score = 0;

    @FXML private Button backMenuButton;
    @FXML private AnchorPane body;
    @FXML private Pane playField;
    @FXML private Text scoreLayer;
    @FXML private Text popupText;
    @FXML private HBox popup;

    @FXML
    private void menuButtonAction() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/menu.fxml"));

        Stage primaryStage = (Stage) backMenuButton.getScene().getWindow();
        primaryStage.getScene().setRoot(root);
    }

    @FXML
    void startGame() {
        applicationSettingsInjector = Guice.createInjector(new GameSettingsModule());
        applicationSettings = applicationSettingsInjector.getInstance(ISettings.class);
        applicationSettings.load();

        entityFactory = applicationSettingsInjector.getInstance(EntityFactory.class);
        entityFactory.init(playField);

        createDecor();
        createPigs();
        createBirds();
        loadNextBird();



        gameLoop = new AnimationTimer() {

            @Override
            public void handle(long currentNanoTime) {
                if(gameOver()) {
                    System.out.println("Game terminé");

                    popup.setVisible(true);

                    gameLoop.stop();
                }
                else {
                    SpriteBase bird = currentBird();

                    bird.processInput(input);

                    slingshot.processInput(input);

                    bird.move();

                    checkCollisions();

                    bird.updateUI();

                    removeSprites();

                    updateScore();
                }
            }
        };

        gameLoop.start();
    }

    private boolean gameOver() { return (birds.isEmpty()) || (pigs.isEmpty() && !currentBird().isMoving()); }

    private void updateScore() {
        scoreLayer.setText("Score : " + score);
    }

    private void createDecor() {
        slingshot = applicationSettingsInjector.getInstance(Slingshot.class);
        slingshot.init(playField);

        scoreLayer.setText("Score : " + score);
    }

    private void createBirds() {
        for(int index = 1; index <= Double.parseDouble(applicationSettings.get("level", "birdNumber-red")); index++) {
            Bird bird = (Bird) entityFactory.getSprite("bird", "red");

            birds.add(bird);
        }
    }

    private void createPigs() {
        for(int index = 1; index <= Double.parseDouble(applicationSettings.get("level", "pigNumber")); index++) {
            double pigX = Double.parseDouble(applicationSettings.get("level", "pigX-" + index)),
                    pigY = Double.parseDouble(applicationSettings.get("level", "pigY-" + index));

            Pig pig = (Pig) entityFactory.getSprite("pig", "green_medium");

            pig.moveTo(pigX, pigY);

            pigs.add(pig);
        }
    }

    private void loadNextBird() {
        double birdOnSlingshotX = Double.parseDouble(applicationSettings.get("stage", "birdOnSlingshotX")),
                birdOnSlingshotY = Double.parseDouble(applicationSettings.get("stage", "birdOnSlingshotY"));

        slingshot.setMovable(true);
        currentBird().moveTo(birdOnSlingshotX, birdOnSlingshotY);

        input = new Input(currentBird().getView());

        slingshot.getDoubleBoundLine().getFront().toFront();
        slingshot.getHarness().toFront();
        slingshot.getFront().toFront();
    }

    private void checkCollisions() {
        for(Pig pig: pigs)
            if(pig.collidesWith(currentBird())) {
                System.out.println("Collision");

                score += pig.getScoreValue();

                pig.getDamagedBy(currentBird());
                currentBird().getDamagedBy(pig);
            }
    }

    private void removeSprites() {
        int initialBirdsNbr = birds.size();

        birds.stream().filter(bird -> bird.isRemovable() && bird.outOfBounds()).forEach(SpriteBase::removeFromLayer);
        pigs.stream().filter(SpriteBase::isRemovable).forEach(SpriteBase::removeFromLayer);

        birds = birds.stream().filter(bird -> !bird.isRemovable() || !bird.outOfBounds()).collect(Collectors.toList());
        pigs = pigs.stream().filter(pig -> !pig.isRemovable()).collect(Collectors.toList());

        int newBirdsNbr = birds.size();

        if(newBirdsNbr < initialBirdsNbr && newBirdsNbr != 0) {
            loadNextBird();
        }
    }

    private Bird currentBird() {
        if(birds.get(0) == null)
            return currentBird;

        currentBird = birds.get(0);

        return birds.get(0);
    }
}
