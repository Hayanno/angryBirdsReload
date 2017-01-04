package angrybirdsreload.utils;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class BoundLine extends Line {
    public BoundLine() {
        setStrokeWidth(10);
        setStroke(Color.web("#301608"));
        setMouseTransparent(true);
    }

    public void setAllProperty(ImageView imageView, double layoutX, double layoutY) {
        double lineX = imageView.getLayoutX() + layoutX;
        double lineY = imageView.getLayoutY() + layoutY;

        setAllProperty(lineX, lineY);
    }

    public void setAllProperty(double x, double y) {
        setStartProperty(x, y);
        setEndProperty(x, y);
    }

    public void setStartProperty(double startX, double startY) {
        DoubleProperty startXProperty = new SimpleDoubleProperty(startX);
        DoubleProperty startYProperty = new SimpleDoubleProperty(startY);

        startXProperty().bind(startXProperty);
        startYProperty().bind(startYProperty);
    }

    public void setEndProperty(double endX, double endY) {
        DoubleProperty endXProperty = new SimpleDoubleProperty(endX);
        DoubleProperty endYProperty = new SimpleDoubleProperty(endY);

        endXProperty().bind(endXProperty);
        endYProperty().bind(endYProperty);
    }
}