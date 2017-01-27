package angrybirdsreload.entity;

import angrybirdsreload.settings.ISettings;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class BirdRed extends Bird {

    BirdRed(ISettings gameSettings, Pane layer) {
        super();

        Double radius = Double.parseDouble(gameSettings.get("stage", "birdRedRadius")),
                health = Double.parseDouble(gameSettings.get("stage", "birdRedHealth")),
                damage = Double.parseDouble(gameSettings.get("stage", "birdRedDamage")),
                birdOnSlingshotX = Double.parseDouble(gameSettings.get("stage", "birdOnSlingshotX")),
                birdOnSlingshotY = Double.parseDouble(gameSettings.get("stage", "birdOnSlingshotY")),
                birdWaitingX = Double.parseDouble(gameSettings.get("stage", "birdWaitingX")),
                birdWaitingY = Double.parseDouble(gameSettings.get("stage", "birdWaitingY")),
                maxY = Double.parseDouble(gameSettings.get("stage", "maxY")),
                maxX = Double.parseDouble(gameSettings.get("stage", "maxX"));

        Image image = new Image(getClass().getResource("/img/birds/red.png").toExternalForm());
        this.imageKo = new Image(getClass().getResource("/img/birds/red_ko.png").toExternalForm());
        this.imageFlying = new Image(getClass().getResource("/img/birds/red_flying.png").toExternalForm());

        super.init(gameSettings, image, layer,
            birdWaitingX, birdWaitingY,
            0, maxX, 0, maxY,
            radius, health, damage);
    }
}
