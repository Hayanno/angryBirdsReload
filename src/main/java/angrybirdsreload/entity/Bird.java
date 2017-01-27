package angrybirdsreload.entity;

import angrybirdsreload.settings.ISettings;
import angrybirdsreload.utils.Input;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public abstract class Bird extends SpriteBase {

    private ISettings gameSettings;

    private double minX, maxX, minY, maxY, finalWidth, finalHeight;

    Image imageKo, imageFlying;

    public Bird() {
        super();
    }

    public void init(ISettings gameSettings, Image birdImage, Pane layer,
                double x, double y,
                double minX, double maxX, double minY, double maxY,
                double radius, double health, double damage) {
        this.finalHeight = Double.parseDouble(gameSettings.get("game", "resolutionHeight"));
        this.finalWidth = Double.parseDouble(gameSettings.get("game", "resolutionWidth"));
        this.gameSettings = gameSettings;
        x *= finalWidth;
        y = finalHeight - (y * finalHeight);
        this.minX = minX;
        this.minY = minY;
        this.maxX = finalWidth - maxX;
        this.maxY = finalHeight - (maxY * finalHeight);

        super.init(birdImage, layer, x, y, radius, health, damage);
    }

    @Override
    public void move() {
        if(!outOfBounds()) {
            super.move();

            if(isMoving())
                setDy(getDy() + Double.parseDouble(gameSettings.get("game", "gravity")));
        }
    }

    @Override
    public void moveTo(double moveX, double moveY) {
        moveX *= finalWidth;
        moveY = finalHeight - (moveY * finalHeight);

        super.moveTo(moveX, moveY);
    }

    @Override
    public void getDamagedBy(SpriteBase sprite) {
        getView().setImage(imageKo);

        super.getDamagedBy(sprite);
    }

    @Override
    public void removeFromLayer() {
        Timeline explosion = new Timeline(
                new KeyFrame(Duration.ZERO, event -> getView().setImage(new Image(getClass().getResource("/img/animation/little-explosion-1.png").toExternalForm()))),
                new KeyFrame(Duration.seconds(0.2), event -> getView().setImage(new Image(getClass().getResource("/img/animation/little-explosion-2.png").toExternalForm()))),
                new KeyFrame(Duration.seconds(0.4), event -> getView().setImage(new Image(getClass().getResource("/img/animation/little-explosion-3.png").toExternalForm()))),
                new KeyFrame(Duration.seconds(0.6), event -> super.removeFromLayer())
        );

        explosion.play();
    }

    public boolean outOfBounds() {
        if(Double.compare(getX(), minX) < 0 || Double.compare(getX(), maxX - 48) > 0)
            return true;

        // On gère le rebond
        if(Double.compare(getY(), maxY) > 0) {
            // On s'arrête si le rebond est minimal
            if(bounceIsTooWeak())
                return true;

            setRemovable(true);
            setY(maxY - 1);
            setDy(-getDy() / 2);
            setDx(getDx() / 2);
        }

        return false;
    }

    public void processInput(Input input) {
        double radius = Double.parseDouble(gameSettings.get("stage", "elasticRadius"));

        if(input.isMouseEntered() && isMovable())
            getView().setCursor(Cursor.HAND);

        if(input.isMousePressed() && isMovable()) {
            input.setDragStartX(getView().getLayoutX());
            input.setDragStartY(getView().getLayoutY());
            getView().setCursor(Cursor.CLOSED_HAND);
        }

        if(input.isMouseDragging() && isMovable()) {
            if(input.isOutOfRange(radius)){
                setX(input.getMaxRangeX(radius) + input.getDragStartX());
                setY(input.getMaxRangeY(radius) + input.getDragStartY());
            }
            else {
                setX(input.getSceneX() + input.getDragStartX());
                setY(input.getSceneY() + input.getDragStartY());
            }
        }

        if(input.isMouseReleased() && isMovable()) {
            setMoving(true);
            setMovable(false);
            setRemovable(true);

            getView().setCursor(Cursor.DEFAULT);

            getView().setImage(imageFlying);

            if(input.isOutOfRange(radius)){
                setDx((input.getImageStartX() - input.getMaxRangeX(radius)) / Double.parseDouble(gameSettings.get("game", "speedX")));
                setDy((input.getImageStartY() - input.getMaxRangeY(radius)) / Double.parseDouble(gameSettings.get("game", "speedY")));
            }
            else {
                setDx((input.getImageStartX() - input.getSceneX()) / Double.parseDouble(gameSettings.get("game", "speedX")));
                setDy((input.getImageStartY() - input.getSceneY()) / Double.parseDouble(gameSettings.get("game", "speedY")));
            }
        }
    }

    private boolean bounceIsTooWeak() {
        return Double.compare(Math.abs(getDx()), 1) < 0 || Double.compare(Math.abs(getDy()), 1) < 0;
    }
}
