package angrybirdsreload.entity;


import angrybirdsreload.settings.ISettings;
import com.google.inject.Inject;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Pig extends SpriteBase {

    @Inject
    private ISettings gameSettings;

    public Pig() {
        super();
    }

    public void init(Image image, Pane layer, double x, double y, double radius, double health, double damage) {
        double finalWidth = Double.parseDouble(gameSettings.get("resolutionWidth"));
        double finalHeight = Double.parseDouble(gameSettings.get("resolutionHeight"));

        x *= finalWidth;
        y = finalHeight - (y * finalHeight);

        super.init(image, layer, x, y, 0, 0, radius, health, damage);
    }
}
