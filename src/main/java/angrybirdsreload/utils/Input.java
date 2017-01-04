package angrybirdsreload.utils;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Input {

    private double dragStartX, dragStartY,
            imageStartX, imageStartY,
            sceneX, sceneY;

    private boolean mouseDragging = false,
            mouseEntered = false,
            mousePressed = false,
            mouseReleased = false;

    public Input(ImageView imageView) {
        imageView.setOnMouseEntered((MouseEvent event) -> {
            mouseEntered = true;
        });

        imageView.setOnMouseExited((MouseEvent event) -> {
            mouseEntered = false;
        });

        imageView.setOnMousePressed((MouseEvent event) -> {
            mouseEntered = false;

            mousePressed = true;

            imageStartX = event.getSceneX();
            imageStartY = event.getSceneY();

            sceneX = event.getSceneX();
            sceneY = event.getSceneY();
        });

        imageView.setOnMouseDragged((MouseEvent event) -> {
            mousePressed = false;
            mouseDragging = true;;

            sceneX = event.getSceneX();
            sceneY = event.getSceneY();
        });

        imageView.setOnMouseReleased((MouseEvent event) -> {
            mouseDragging = false;
            mouseReleased = true;;

            sceneX = event.getSceneX();
            sceneY = event.getSceneY();
        });
    }

    public void setDragStartX(double dragStartX) {
        this.dragStartX = dragStartX - getSceneX();
    }

    public void setDragStartY(double dragStartY) {
        this.dragStartY = dragStartY - getSceneY();
    }

    public double getDragStartX() {
        return dragStartX;
    }

    public double getDragStartY() {
        return dragStartY;
    }

    public double getSceneX() {
        return sceneX;
    }

    public double getSceneY() {
        return sceneY;
    }

    public double getImageStartX() {
        return imageStartX;
    }

    public double getImageStartY() {
        return imageStartY;
    }

    public boolean isMouseEntered() {
        return mouseEntered;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public boolean isMouseReleased() {
        return mouseReleased;
    }

    public boolean isMouseDragging() {
        return mouseDragging;
    }
}