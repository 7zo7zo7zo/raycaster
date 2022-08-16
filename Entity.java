import java.awt.*;

public class Entity {

    protected Assets assets;

    protected double x;
    protected double y;

    protected double angle;

    public Entity(Assets assets, double x, double y) {
        this.assets = assets;
        this.x = x;
        this.y = y;
    }

    public void tick() {

    }

    public void render(Graphics2D g2) {

    }

    public int getCellSize() {
        return assets.getCellSize();
    }
    public int[][] getMap() {
        return assets.getMap();
    }

    public double getDist(Entity e) {
        return Math.sqrt(Math.pow(x - e.x, 2) + Math.pow(y - e.y, 2));
    }
}
