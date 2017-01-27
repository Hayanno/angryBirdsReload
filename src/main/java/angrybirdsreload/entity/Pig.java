package angrybirdsreload.entity;


import angrybirdsreload.settings.ISettings;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public abstract class Pig extends SpriteBase {

    private ISettings gameSettings;

    private int scoreValue = 0;

    public Pig() {
        super();
    }

    public void init(ISettings gameSettings, Image pigImage, Pane layer, double radius, double health, double damage, int scoreValue) {
        this.scoreValue = scoreValue;
        this.gameSettings = gameSettings;

        super.init(pigImage, layer, 0, 0, radius, health, damage);
    }

    public int getScoreValue() {
        return scoreValue;
    }

    @Override
    public void moveTo(double x, double y) {
        double finalWidth = Double.parseDouble(gameSettings.get("game", "resolutionWidth"));
        double finalHeight = Double.parseDouble(gameSettings.get("game", "resolutionHeight"));

        x *= finalWidth;
        y = finalHeight - (y * finalHeight);

        super.moveTo(x, y);
        getView().relocate(x, y);
    }

    @Override
    public void removeFromLayer() {
        DoubleProperty scale = new SimpleDoubleProperty(1);
        getView().scaleXProperty().bind(scale);
        getView().scaleYProperty().bind(scale);

        Timeline explosion = new Timeline(
                new KeyFrame(Duration.ZERO, event -> {
                    getView().setImage(new Image(getClass().getResource("/img/animation/little-explosion-1.png").toExternalForm()));
                    scale.setValue(1.2); }),
                new KeyFrame(Duration.seconds(0.2), event -> getView().setImage(new Image(getClass().getResource("/img/animation/little-explosion-2.png").toExternalForm()))),
                new KeyFrame(Duration.seconds(0.4), event -> getView().setImage(new Image(getClass().getResource("/img/animation/little-explosion-3.png").toExternalForm()))),
                new KeyFrame(Duration.seconds(0.6), event -> super.removeFromLayer())
        );

        explosion.play();
    }
}
