package angrybirdsreload.controller;

import angrybirdsreload.entity.Bird;
import angrybirdsreload.entity.Pig;
import angrybirdsreload.entity.Slingshot;
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
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameController {

    private List<Pig> pigs = new ArrayList<>();
    private List<Bird> birds = new ArrayList<>();

    private Image birdImage, pigImage, harnessImage, slingshotBackImage, slingshotFrontImage;
    private Injector applicationSettingsInjector;
    private ISettings applicationSettings;
    private AnimationTimer gameLoop;
    private Slingshot slingshot;
    private Input input;

    private int score = 0;

    @FXML private Button backMenuButton;
    @FXML private Pane playField;
    @FXML private Text scoreLayer;

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

        loadGame();
        createDecor();
        createPigs();
        createBirds();
        loadNextBird();

        gameLoop = new AnimationTimer() {

            @Override
            public void handle(long currentNanoTime) {
                if(birds.isEmpty() || pigs.isEmpty()) {
                    System.out.println("Game terminé");

                    gameLoop.stop();
                }
                else {
                    Bird bird = currentBird();

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

    private void updateScore() {
        scoreLayer.setText("Score : " + score);
    }

    private void loadGame() {
        pigImage = new Image(getClass().getResource("/img/pigs/green_medium.png").toExternalForm());
        birdImage = new Image(getClass().getResource("/img/birds/red.png").toExternalForm());
        harnessImage = new Image(getClass().getResource("/img/static/harness.png").toExternalForm());
        slingshotBackImage = new Image(getClass().getResource("/img/static/slingshot_back.png").toExternalForm());
        slingshotFrontImage = new Image(getClass().getResource("/img/static/slingshot_front.png").toExternalForm());
    }

    private void createDecor() {
        Double slingshotBackX = Double.parseDouble(applicationSettings.get("stage", "slingshotBackX")),
                slingshotBackY = Double.parseDouble(applicationSettings.get("stage", "slingshotBackY")),
                slingshotFrontX = Double.parseDouble(applicationSettings.get("stage", "slingshotFrontX")),
                slingshotFrontY = Double.parseDouble(applicationSettings.get("stage", "slingshotFrontY"));

        slingshot = applicationSettingsInjector.getInstance(Slingshot.class);
        slingshot.init(slingshotBackImage, slingshotFrontImage, harnessImage, playField,
                slingshotBackX, slingshotBackY, slingshotFrontX, slingshotFrontY);

        scoreLayer.setText("Score : " + score);
    }

    private void createBirds() {
        double birdOnSlingshotX = Double.parseDouble(applicationSettings.get("stage", "birdOnSlingshotX")),
                birdOnSlingshotY = Double.parseDouble(applicationSettings.get("stage", "birdOnSlingshotY")),
                birdWaitingX = Double.parseDouble(applicationSettings.get("stage", "birdWaitingX")),
                birdWaitingY = Double.parseDouble(applicationSettings.get("stage", "birdWaitingY")),
                maxY = Double.parseDouble(applicationSettings.get("stage", "maxY")),
                maxX = Double.parseDouble(applicationSettings.get("stage", "maxX"));

        // TODO : Factory ici avec boucle de création en fonction du niveau
        Bird bird = applicationSettingsInjector.getInstance(Bird.class);
        bird.init(birdImage, playField,
                birdOnSlingshotX, birdOnSlingshotY,
                0, maxX, 0, maxY,
                9, 1, 1);

        birds.add(bird);

        Bird bird2 = applicationSettingsInjector.getInstance(Bird.class);
        bird2.init(birdImage, playField,
                birdWaitingX, birdWaitingY,
                0, maxX, 0, maxY,
                9, 1, 1);

        birds.add(bird2);
    }

    private void createPigs() {
        for(int index = 1; index <= Double.parseDouble(applicationSettings.get("level", "pigNumber")); index++) {
            Pig pig = applicationSettingsInjector.getInstance(Pig.class);
            double pigX = Double.parseDouble(applicationSettings.get("level", "pigX-" + index)),
                    pigY = Double.parseDouble(applicationSettings.get("level", "pigY-" + index));

            // TODO : Factory ici
            pig.init(pigImage, playField, pigX, pigY, 17, 1, 0, 1000);

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

                pig.setRemovable(true);
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
        return birds.get(0);
    }
}
