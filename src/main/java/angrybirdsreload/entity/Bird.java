package angrybirdsreload.entity;

import angrybirdsreload.settings.ISettings;
import angrybirdsreload.utils.DoubleBoundLine;
import angrybirdsreload.utils.Input;
import com.google.inject.Inject;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Bird extends SpriteBase {

    @Inject
    private ISettings gameSettings;

    private DoubleBoundLine dbl;
    private double minX, maxX, minY, maxY;

    public Bird() {
        super();
    }

    public void init(Image birdImage, Pane layer,
                DoubleBoundLine dbl,
                double x, double y,
                double minX, double maxX, double minY, double maxY,
                double radius, double health, double damage) {
        double finalHeight = Double.parseDouble(gameSettings.get("resolutionHeight"));
        double finalWidth = Double.parseDouble(gameSettings.get("resolutionWidth"));

        x *= finalWidth;
        y = finalHeight - (y * finalHeight);
        this.minX = minX;
        this.minY = minY;
        this.maxX = finalWidth - maxX;
        this.maxY = finalHeight - (maxY * finalHeight);

        super.init(birdImage, layer, x, y, 0, 0, radius, health, damage);

        this.dbl = dbl;
    }

    @Override
    public void move() {
        if(!outOfBounds()) {
            super.move();

            if(isMoving())
                setDy(getDy() + Double.parseDouble(gameSettings.get("gravity")));
        }
    }

    private boolean outOfBounds() {
        if(Double.compare(getX(), minX) < 0
                //|| Double.compare(getY(), minY) < 0
                || Double.compare(getX(), maxX) > 0)
            return true;

        // TODO : SETTINGS
        // On gère le rebond
        if(Double.compare(getY(), maxY) > 0) {
            // On s'arrête si le rebond est minimal
            if(bounceIsTooWeak())
                return true;

            setY(maxY - 1);
            setDy(-getDy() / 2);
            setDx(getDx() / 2);
        }

        return false;
    }

    public void processInput(Input input) {
        double vX = input.getSceneX() - input.getImageStartX();
        double vY = input.getSceneY() - input.getImageStartY();
        double magV = Math.sqrt(vX*vX + vY*vY);
        double aX = input.getImageStartX() + vX / magV * 2 * 70;
        double aY = input.getImageStartY() + vY / magV * 2 * 70;

        if(input.isMouseEntered() && isMovable())
            getView().setCursor(Cursor.HAND);

        if(input.isMousePressed() && isMovable()) {
            input.setDragStartX(getView().getLayoutX());
            input.setDragStartY(getView().getLayoutY());
            getView().setCursor(Cursor.CLOSED_HAND);
        }

        if(input.isMouseDragging() && isMovable()) {
            if(isOutOfRange(input)){
                setX(aX + input.getDragStartX());
                setY(aY + input.getDragStartY());
            }
            else {
                setX(input.getSceneX() + input.getDragStartX());
                setY(input.getSceneY() + input.getDragStartY());
            }
        }

        if(input.isMouseReleased() && isMovable()) {
            setMoving(true);
            setMovable(false);

            getView().setCursor(Cursor.DEFAULT);

            // TODO : SETTINGS
            if(isOutOfRange(input)){
                setDx((input.getImageStartX() - aX) / 12.0);
                setDy((input.getImageStartY() - aY) / 12.0);
            }
            else {
                setDx((input.getImageStartX() - input.getSceneX()) / 12.0);
                setDy((input.getImageStartY() - input.getSceneY()) / 12.0);
            }
        }
    }

    private boolean bounceIsTooWeak() {
        return Double.compare(Math.abs(getDx()), 1) < 0 || Double.compare(Math.abs(getDy()), 1) < 0;
    }

    private boolean isOutOfRange(Input input) {
        return Math.hypot(input.getSceneX() - input.getImageStartX(),
                input.getSceneY() - input.getImageStartY()) >= 2 * 60;
    }
}
