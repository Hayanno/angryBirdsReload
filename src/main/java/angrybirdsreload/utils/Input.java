package angrybirdsreload.utils;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

public class Input {

    Scene scene;
    double mouseX, mouseY;

    private boolean mousePressed, mouseReleased;

    public Input(Scene scene) {
        this.scene = scene;
        this.mousePressed = false;
        this.mouseReleased = true;
    }

    public void addListeners() {
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedEventHandler);
        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleasedEventHandler);
    }

    public void removeListeners() {
        scene.removeEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedEventHandler);
        scene.removeEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleasedEventHandler);
    }

    private EventHandler<MouseEvent> mousePressedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            mouseReleased = false;
            mousePressed = true;
            mouseX = event.getX();
            mouseY = event.getY();
        }
    };

    private EventHandler<MouseEvent> mouseReleasedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            mousePressed = false;
            mouseReleased = true;
        }
    };

    public boolean isMousePressed() {
        return mousePressed;
    }

    public boolean isMouseReleased() {
        if(mouseReleased) {
            mouseReleased = false;
            return true;
        }

        return false;
    }
}