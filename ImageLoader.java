import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(ImageLoader.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public static BufferedImage[] loadImagesFromSpriteSheet(String path, int width, int height) {
        int x = 0;
        int y = 0;
        BufferedImage image = loadImage(path);
        BufferedImage[] images = new BufferedImage[(image.getWidth() / width) * (image.getHeight() / height)];
        for (int i = 0; i < image.getHeight() / height; i++) {
            for (int j = 0; j < image.getWidth() / width; j++) {
                images[i * image.getWidth() / width + j] = image.getSubimage(x, y, width, image.getHeight());
                x += width;
            }
            y += height;
        }
        return images;
    }
}
