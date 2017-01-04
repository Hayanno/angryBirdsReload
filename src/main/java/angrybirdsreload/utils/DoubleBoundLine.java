package angrybirdsreload.utils;

public class DoubleBoundLine {
    private BoundLine boundLineBack;
    private BoundLine boundLineFront;

    public DoubleBoundLine() {
        setBack(new BoundLine());
        setFront(new BoundLine());
    }

    public BoundLine getBack() { return boundLineBack; }
    public BoundLine getFront() { return boundLineFront; }

    public void setBack(BoundLine boundLineBack) { this.boundLineBack = boundLineBack; }
    public void setFront(BoundLine boundLineFront) { this.boundLineFront = boundLineFront; }
}