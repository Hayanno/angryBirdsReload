package angrybirdsreload.utils;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class BoundLine extends Line {
    public BoundLine() {
        setStrokeWidth(10);
        setStroke(Color.web("#301608"));
        setMouseTransparent(true);
    }

    public void setStartProperty(DoubleProperty startX, DoubleProperty startY) {
        startXProperty().bind(startX);
        startYProperty().bind(startY);
    }

    public void setEndProperty(DoubleProperty endX, DoubleProperty endY) {
        endXProperty().bind(endX);
        endYProperty().bind(endY);
    }
}