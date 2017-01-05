package angrybirdsreload.entity;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class SpriteBase {

    private ImageView imageView;
    private Pane layer;

    private double health, damage;
    private double dx, dy, dr;
    private double radius;
    private double x, y;

    private boolean movable = true,
            removable = false,
            moving = false;

    public SpriteBase() { }

    public void init(Image image, Pane layer, double x, double y, double dx, double dy, double radius, double health, double damage) {
        this.layer = layer;

        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.radius = radius;
        this.health = health;
        this.damage = damage;

        this.imageView = new ImageView(image);
        this.imageView.relocate(x, y);

        addToLayer();
    }

    public void addToLayer() {
                                                                                        getLayer().getChildren().add(this.imageView);
    }
    public void removeFromLayer() {
            getLayer().getChildren().remove(this.imageView);
    }

    public Pane getLayer() {
        return layer;
    }
    public void setLayer(Pane layer) {
        this.layer = layer;
    }

    public double getX() { return x; }
    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }

    public double getDx() {
        return dx;
    }
    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }
    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getDr() {
        return dr;
    }
    public void setDr(double dr) {
        this.dr = dr;
    }

    public double getHealth() {
        return health;
    }
    public void setHealth(double health) {
        this.health = health;
    }

    public double getDamage() {
        return damage;
    }
    public void setDamage(double damage) {
        this.damage = damage;
    }

    public boolean isRemovable() {
        return removable;
    }
    public void setRemovable(boolean removable) {
        this.removable = removable;
    }

    public boolean isMovable() {
        return this.movable;
    }
    public void setMovable(boolean movable) { this.movable = movable; }

    public boolean isMoving() { return this.moving; }
    public void setMoving(boolean moving) { this.moving = moving; }

    public boolean isAlive() {
        return Double.compare(health, 0) > 0;
    }

    public ImageView getView() {
        return imageView;
    }

    public double getRadius() {
        return radius;
    }

    public void updateUI() {
        imageView.relocate(x, y);
    }

    public void moveTo(double moveX, double moveY) { setX(moveX); setY(moveY); }

    public void move() {
        x += getDx();
        y += getDy();
    }

    public boolean collidesWith(SpriteBase otherSprite) {
        return Math.hypot(this.getX() - otherSprite.getX(),
                this.getY() - otherSprite.getY()) <= 2 * this.getRadius();
    }

    /**
     * Reduce health by the amount of damage that the given sprite can inflict
     * minimum fixed to 0
     * @param sprite
     */
    public void getDamagedBy(SpriteBase sprite) {
        health -= sprite.getDamage();

        if(getHealth() <= 0) {
            kill();
            setRemovable(true);
        }
    }

    /**
     * Set health to 0
     */
    public void kill() {
        setHealth(0);
        setRemovable(true);
    }

    /**
     * Set flag that the sprite can be removed from the UI.
     */
    public void remove() {
        setRemovable(true);
    }
}
