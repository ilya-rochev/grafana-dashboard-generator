package i.r.grafana.json.processor.model;

public class GridPos {

    private int h;

    private int w;

    private int x;

    private int y;

    public GridPos(int h, int w, int x, int y) {
        this.h = h;
        this.w = w;
        this.x = x;
        this.y = y;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
