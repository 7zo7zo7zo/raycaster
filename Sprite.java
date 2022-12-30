import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Sprite extends Entity {

    protected BufferedImage image;

    public Sprite(Assets assets, double x, double y, String path) {
        super(assets, x, y);
        image = ImageLoader.loadImage(path);
    }

    public void render(Graphics2D g2) {
        g2.drawImage(image, (int) x, (int) y, null);
        super.render(g2);
    }
}
