package angrybirdsreload.entity;

import angrybirdsreload.settings.GameSettings;
import angrybirdsreload.settings.ISettings;
import com.google.inject.Inject;
import javafx.scene.layout.Pane;

public class EntityFactory {

    @Inject
    private ISettings gameSettings;

    private Pane layer;

    public EntityFactory() {}

    public void init(Pane layer) {
        this.layer = layer;
    }

    public SpriteBase getSprite(String mainType, String subType) {
        if(mainType.equals("bird")) {
            if (subType.equals("red"))
                return new BirdRed(gameSettings, layer);
        }
        else if(mainType.equals("pig")) {
            if (subType.equals("green_medium"))
                return new PigGreenMedium(gameSettings, layer);
        }

        return null;
    }
}
