package angrybirdsreload.entity;

import angrybirdsreload.settings.ISettings;
import angrybirdsreload.utils.DoubleBoundLine;
import angrybirdsreload.utils.Input;
import com.google.inject.Inject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Slingshot {

    @Inject
    private ISettings gameSettings;

    private Pane playField;
    private DoubleBoundLine doubleBoundLine;
    private ImageView backImageView, frontImageView, harnessImageView;

    private double dx = 0, dy = 0;
    private boolean movable = true;

    public Slingshot() {}

    public void init(Image imageBack, Image imageFront, Image harnessImage, Pane layer,
                     double backX, double backY, double frontX, double frontY) {
        double finalHeight = Double.parseDouble(gameSettings.get("game", "resolutionHeight")),
                finalWidth = Double.parseDouble(gameSettings.get("game", "resolutionWidth"));

        this.playField = layer;
        this.doubleBoundLine = new DoubleBoundLine();
        this.backImageView = new ImageView(imageBack);
        this.frontImageView = new ImageView(imageFront);
        this.harnessImageView = new ImageView(harnessImage);

        backX = backX * finalWidth;
        backY = finalHeight - (backY * finalHeight);
        frontX = frontX * finalWidth;
        frontY = finalHeight - (frontY * finalHeight);

        backImageView.relocate(backX, backY);
        frontImageView.relocate(frontX, frontY);
        frontImageView.setRotate(-2);

        doubleBoundLine.getBack().setAllProperty(backImageView, 30, 25);
        doubleBoundLine.getFront().setAllProperty(frontImageView, 18, 30);

        harnessImageView.setVisible(false);

        addToLayer();
    }

    public void addToLayer() {
        playField.getChildren().add(doubleBoundLine.getBack());
        playField.getChildren().add(backImageView);
        playField.getChildren().add(doubleBoundLine.getFront());
        playField.getChildren().add(harnessImageView);
        playField.getChildren().add(frontImageView);
    }

    public void removeFromLayer() {
        playField.getChildren().remove(doubleBoundLine.getBack());
        playField.getChildren().remove(backImageView);
        playField.getChildren().remove(doubleBoundLine.getFront());
        playField.getChildren().remove(harnessImageView);
        playField.getChildren().remove(frontImageView);
    }

    public void processInput(Input input) {
        double x, y,
            radius = Double.parseDouble(gameSettings.get("stage", "elasticRadius"));;

        if(input.isOutOfRange(radius)) {
            x = input.getMaxRangeX(radius) + input.getDragStartX();
            y = input.getMaxRangeY(radius) + input.getDragStartY();
        }
        else {
            x = input.getSceneX() + input.getDragStartX();
            y = input.getSceneY() + input.getDragStartY();
        }

        if(input.isMouseDragging()) {
            harnessImageView.setVisible(true);
            harnessImageView.setLayoutX(x);
            harnessImageView.setLayoutY(y + 15);

            getDoubleBoundLine().getBack().setEndProperty(x + 10, y + 35);
            getDoubleBoundLine().getFront().setEndProperty(x + 10, y + 35);
        }

        if(input.isMouseReleased() && movable) {
            // on arrête le mouvement lorsque l'on arrive au centre du lance-pierre
            if(almostEqual(x + 10 + dx, input.getImageStartX())
                    && almostEqual(y + 35 + dy, input.getImageStartY())) {
                movable = false;
                harnessImageView.setVisible(false);

                doubleBoundLine.getBack().setEndProperty(frontImageView.getLayoutX() + 18, frontImageView.getLayoutY() + 35);
                doubleBoundLine.getFront().setEndProperty(frontImageView.getLayoutX() + 18, frontImageView.getLayoutY() + 35);

                dx = 0;
                dy = 0;
            }
            else {
                dx += ((input.getImageStartX() - input.getSceneX()) / Double.parseDouble(gameSettings.get("game", "speedX")));
                dy += ((input.getImageStartY() - input.getSceneY()) / Double.parseDouble(gameSettings.get("game", "speedY")));

                harnessImageView.setLayoutX(x + dx);
                harnessImageView.setLayoutY(y + 15 + dy);

                getDoubleBoundLine().getBack().setEndProperty(
                        x + 10 + dx,
                        y + 35 + dy);
                getDoubleBoundLine().getFront().setEndProperty(
                        x + 10 + dx,
                        y + 35 + dy);
            }
        }
    }

    public DoubleBoundLine getDoubleBoundLine() { return doubleBoundLine; }
    public ImageView getBack() { return backImageView; }
    public ImageView getFront() { return frontImageView; }
    public ImageView getHarness() { return harnessImageView; }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    private static boolean almostEqual(double a, double b){
        return Math.abs(a - b) < 40.0;
    }
}
