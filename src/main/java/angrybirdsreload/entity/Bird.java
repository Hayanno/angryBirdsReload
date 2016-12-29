package angrybirdsreload.entity;

import angrybirdsreload.utils.BoundLine;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Bird extends SpriteBase {

    BoundLine boundLineBack, boundLineFront;
    ImageView harnessImage;

    double minX, maxX, minY, maxY;
    double dragStartX, dragStartY;
    double imageStartX, imageStartY;

    public Bird(Image birdImage, Image harnessImage, Pane layer,
                BoundLine boundLineBack, BoundLine boundLineFront,
                double x, double y,
                double minX, double maxX, double minY, double maxY,
                double radius, double health, double damage) {
        super(birdImage, layer, x, y, 0, 0, radius, health, damage);

        this.harnessImage = new ImageView(harnessImage);

        this.boundLineBack = boundLineBack;
        this.boundLineFront = boundLineFront;

        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        this.harnessImage.setVisible(false);

        this.layer.getChildren().add(this.harnessImage);
    }

    @Override
    public void move() {
        if(!outOfBounds())
            super.move();
    }

    private boolean outOfBounds() {
        if(Double.compare(getX(), minX) < 0
        || Double.compare(getX(), maxX) > 0
        || Double.compare(getY(), minY) < 0)
            return true;

        // On gère le rebond
        if(Double.compare(getY(), maxY) > 0) {
            // On s'arrête si le rebond est minimal
            if(Double.compare(Math.sqrt(getDx()) + Math.sqrt(getDy()), 0.3) < 0)
                return true;

            setDy(-getDy() / 2);
            setDx(getDx() / 2);
        }

        return false;
    }

    public void addListeners() {
        imageView.setOnMouseEntered((MouseEvent event) -> {
            if(isMovable())
                imageView.setCursor(Cursor.HAND);
        });

        imageView.setOnMousePressed((MouseEvent event) -> {
            if(isMovable()) {
                imageView.setCursor(Cursor.CLOSED_HAND);

                // record a delta distance for the drag and drop operation.
                dragStartX = imageView.getLayoutX() - event.getSceneX();
                dragStartY = imageView.getLayoutY() - event.getSceneY();

                imageStartX = event.getSceneX();
                imageStartY = event.getSceneY();
            }
        });

        imageView.setOnMouseDragged((MouseEvent event) -> {
            if(isMovable()) {
                setX(event.getSceneX() + dragStartX);
                setY(event.getSceneY() + dragStartY);

                harnessImage.setLayoutX(event.getSceneX() + dragStartX);
                harnessImage.setLayoutY(event.getSceneY() + dragStartY + 15);

                DoubleProperty lineBackX = new SimpleDoubleProperty(event.getSceneX() + dragStartX + 10);
                DoubleProperty lineBackY = new SimpleDoubleProperty(event.getSceneY() + dragStartY + 35);
                DoubleProperty lineFrontX = new SimpleDoubleProperty(event.getSceneX() + dragStartX + 10);
                DoubleProperty lineFrontY = new SimpleDoubleProperty(event.getSceneY() + dragStartY + 35);

                boundLineBack.setEndProperty(lineBackX, lineBackY);
                boundLineFront.setEndProperty(lineFrontX, lineFrontY);

                this.harnessImage.setVisible(true);
            }
        });

        imageView.setOnMouseReleased((MouseEvent event) -> {
            if(isMovable()) {
                setMoving(true);

                imageView.setCursor(Cursor.DEFAULT);

                setDx((imageStartX - event.getSceneX()) / 10.0);
                setDy((imageStartY - event.getSceneY()) / 10.0);
            }
        });
    }
}
