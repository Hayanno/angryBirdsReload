package angrybirdsreload.entity;

import angrybirdsreload.settings.ISettings;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class PigGreenMedium extends Pig {

    PigGreenMedium(ISettings gameSettings, Pane layer) {
        super();

        Double radius = Double.parseDouble(gameSettings.get("stage", "pigGreenMediumRadius")),
                health = Double.parseDouble(gameSettings.get("stage", "pigGreenMediumHealth")),
                damage = Double.parseDouble(gameSettings.get("stage", "pigGreenMediumDamage"));

        int scoreValue = Integer.parseInt(gameSettings.get("stage", "pigGreenMediumScore"));

        Image image = new Image(getClass().getResource("/img/pigs/green_medium.png").toExternalForm());

        super.init(gameSettings, image, layer, radius, health, damage, scoreValue);
    }
}
