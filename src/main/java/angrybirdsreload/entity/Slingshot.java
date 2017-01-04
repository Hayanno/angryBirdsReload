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
        double finalHeight = Double.parseDouble(gameSettings.get("resolutionHeight")),
                finalWidth = Double.parseDouble(gameSettings.get("resolutionWidth"));

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
        // TODO : SETTINGS
        double x = input.getSceneX() + input.getDragStartX(),
                y = input.getSceneY() + input.getDragStartY();

        double vX = input.getSceneX() - input.getImageStartX();
        double vY = input.getSceneY() - input.getImageStartY();
        double magV = Math.sqrt(vX*vX + vY*vY);
        double aX = input.getImageStartX() + vX / magV * 2 * 70;
        double aY = input.getImageStartY() + vY / magV * 2 * 70;

        if(input.isMouseDragging()) {
            harnessImageView.setVisible(true);

            if(isOutOfRange(input)){
                harnessImageView.setLayoutX(aX + input.getDragStartX());
                harnessImageView.setLayoutY(aY + input.getDragStartY() + 15);

                getDoubleBoundLine().getBack().setEndProperty(aX + input.getDragStartX() + 10, aY + input.getDragStartY() + 35);
                getDoubleBoundLine().getFront().setEndProperty(aX + input.getDragStartX() + 10, aY + input.getDragStartY() + 35);
            }
            else {
                harnessImageView.setLayoutX(x);
                harnessImageView.setLayoutY(y + 15);

                getDoubleBoundLine().getBack().setEndProperty(x + 10, y + 35);
                getDoubleBoundLine().getFront().setEndProperty(x + 10, y + 35);
            }
        }

        if(input.isMouseReleased() && movable) {
            // on arrête le mouvement lorsque l'on arrive au centre du lance-pierre

            if(isOutOfRange(input)){
                if(almostEqual(aX + input.getDragStartX() + 10 + dx, input.getImageStartX(), 40)
                        && almostEqual(aY + input.getDragStartY() + 35 + dy, input.getImageStartY(), 40)) {
                    movable = false;
                    harnessImageView.setVisible(false);

                    doubleBoundLine.getBack().setEndProperty(frontImageView.getLayoutX() + 18, frontImageView.getLayoutY() + 35);
                    doubleBoundLine.getFront().setEndProperty(frontImageView.getLayoutX() + 18, frontImageView.getLayoutY() + 35);
                }
                else {
                    dx += ((input.getImageStartX() - aX) / 12.0);
                    dy += ((input.getImageStartY() - aY) / 12.0);

                    harnessImageView.setLayoutX(aX + input.getDragStartX() + dx);
                    harnessImageView.setLayoutY(aY + input.getDragStartY() + 15 + dy);

                    getDoubleBoundLine().getBack().setEndProperty(
                            aX + input.getDragStartX() + 10 + dx,
                            aY + input.getDragStartY() + 35 + dy);
                    getDoubleBoundLine().getFront().setEndProperty(
                            aX + input.getDragStartX() + 10 + dx,
                            aY + input.getDragStartY() + 35 + dy);
                }
            }
            else {
                if(almostEqual(x + 10 + dx, input.getImageStartX(), 40)
                        && almostEqual(y + 35 + dy, input.getImageStartY(), 40)) {
                    movable = false;
                    harnessImageView.setVisible(false);

                    doubleBoundLine.getBack().setEndProperty(frontImageView.getLayoutX() + 18, frontImageView.getLayoutY() + 35);
                    doubleBoundLine.getFront().setEndProperty(frontImageView.getLayoutX() + 18, frontImageView.getLayoutY() + 35);
                }
                else {
                    dx += ((input.getImageStartX() - input.getSceneX()) / 12.0);
                    dy += ((input.getImageStartY() - input.getSceneY()) / 12.0);

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
    }

    public DoubleBoundLine getDoubleBoundLine() { return doubleBoundLine; }
    public ImageView getBack() { return backImageView; }
    public ImageView getFront() { return frontImageView; }
    public ImageView getHarness() { return harnessImageView; }

    private static boolean almostEqual(double a, double b, double eps){
        return Math.abs(a-b) < eps;
    }

    private static boolean isOutOfRange(Input input) {
        return Math.hypot(input.getSceneX() - input.getImageStartX(),
                input.getSceneY() - input.getImageStartY()) >= 2 * 60;
    }
}
