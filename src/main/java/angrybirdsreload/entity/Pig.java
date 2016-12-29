package angrybirdsreload.entity;


import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Pig extends SpriteBase {

    public Pig(Image image, Pane layer, double x, double y, double radius, double health, double damage) {
        super(image, layer, x, y, 0, 0, radius, health, damage);
    }
}
