package angrybirdsreload.entity;


import angrybirdsreload.settings.ISettings;
import com.google.inject.Inject;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Pig extends SpriteBase {

    @Inject
    private ISettings gameSettings;

    private int scoreValue = 0;

    public Pig() {
        super();
    }

    public void init(Image image, Pane layer, double x, double y, double radius, double health, double damage, int scoreValue) {
        double finalWidth = Double.parseDouble(gameSettings.get("game", "resolutionWidth"));
        double finalHeight = Double.parseDouble(gameSettings.get("game", "resolutionHeight"));

        x *= finalWidth;
        y = finalHeight - (y * finalHeight);

        this.scoreValue = scoreValue;

        super.init(image, layer, x, y, 0, 0, radius, health, damage);
    }

    public int getScoreValue() {
        return scoreValue;
    }
}
