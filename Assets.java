import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

public class Assets {

    private Game game;
    private Input input;

    private int cellSize = 5;
    private int[][] map =
            {
                    {1,1,1,1,1,1,1,1,2,2,2,2,2,2,2},
                    {1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
                    {1,0,3,6,3,3,3,0,0,0,0,0,0,0,2},
                    {1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
                    {1,0,5,0,0,0,3,0,6,2,2,0,2,2,2},
                    {1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
                    {1,0,6,3,0,3,0,0,2,0,0,0,0,0,2},
                    {1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
                    {1,1,1,1,1,1,1,1,4,4,4,0,4,4,4},
                    {1,0,0,0,0,0,1,4,0,4,3,0,0,0,4},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,5},
                    {1,0,0,2,0,0,1,4,0,3,3,3,3,0,4},
                    {1,0,0,0,0,0,1,4,0,3,3,3,3,0,4},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                    {1,1,1,1,1,1,1,4,4,4,4,4,4,4,4}
            };
    private Player player;

    private int miniMapScale;
    private BufferedImage mapImage;

    private BufferedImage texture;
    private BufferedImage[] lines;

    public Assets(Game game) {
        this.game = game;
        input = game.getInput();

        player = new Player(this, cellSize * 1.5, cellSize * 1.5);

        miniMapScale = 10;
        mapImage = mapImage = getMinimap(map);

        texture = ImageLoader.loadImage("tiled_bricks.jpg");
        lines = new BufferedImage[texture.getWidth()];

        for(int i = 0; i < lines.length; i++) {
            lines[i] = texture.getSubimage(i, 0, 1, texture.getHeight());
        }
    }

    public void tick() {
        player.tick();
    }

    public void render(Graphics2D g2) {
        //Toolkit.getDefaultToolkit().sync();

        double fov = Math.toRadians(90);
        int lineW = 1;
        int rays = getWidth()/lineW;
        double angle;
        double toAdd;

        int f = 0;
        for (int i = -rays / 2; i < rays / 2; i++) {
            toAdd = Math.atan(2 * (getWidth() / 2 - lineW * f) * Math.tan(-fov/2) / getWidth());
            angle = player.angle + toAdd;

            if (angle < 0) angle += 2 * Math.PI;
            if (angle > 2 * Math.PI) angle -= 2 * Math.PI;

            Ray ray = new Ray(this, player.x, player.y, angle, 0, (lineW * i) + getWidth());
            double distance = ray.distance;

            double ca = player.angle - angle;
            if (ca < 0) ca += 2 * Math.PI;
            if (ca > 2 * Math.PI) ca -= 2 * Math.PI;
            distance *= Math.cos(ca);

            int lineH = (int) ((cellSize * getHeight()) / distance);
            //if (lineH > getHeight()) lineH = getHeight();
            int lineO = getHeight() / 2 - lineH / 2;

            BufferedImage line = lines[(int) (texture.getWidth() * (ray.offset / cellSize))];

            int darkness = 0;
            BufferedImage line2;

            if(darkness != 0) {
                line2 = new BufferedImage(line.getWidth(), line.getHeight(), BufferedImage.TYPE_INT_RGB);
                int[] pixels = ((DataBufferInt) line2.getRaster().getDataBuffer()).getData();

                for (int j = 0; j < line.getHeight(); j++) {
                    pixels[j] = darken(line.getRGB(0, j), (int) (darkness * distance / cellSize));
                }
            }
            else line2 = line;


            /*
            if (ray.yee) {
                for (int j = 0; j < line.getHeight(); j++) {
                    pixels[j] = darken(line.getRGB(0, j), (int) (-20 / cellSize));
                }
            }
            */

            //g2.drawImage(line, (lineW * i) + getWidth() / 2, lineO, lineW, lineH, null);
            g2.drawImage(line2, (lineW * i) + getWidth() / 2, lineO, lineW, lineH, null);
            //ray.render(g2);
            // maybe use render method inside the Ray object
            f++;
        }

        //mapImage = getMinimap(map);
        g2.drawImage(mapImage, 0, 0, mapImage.getWidth() * miniMapScale, mapImage.getHeight() * miniMapScale, null);
        g2.setColor(Color.BLUE);
        g2.drawLine((int) (player.x / cellSize * miniMapScale), (int) (player.y / cellSize * miniMapScale), (int) (Math.cos(player.angle) * 10 + player.x / cellSize * miniMapScale), (int) (Math.sin(player.angle) * 10 + player.y / cellSize * miniMapScale));

        /*
        f = 0;
        for (int i = -rays / 2; i < rays / 2; i++) {
            toAdd = Math.atan(2 * (getWidth() / 2 - lineW * f) * Math.tan(-fov/2) / getWidth());
            angle = player.angle + toAdd;

            while (angle < 0) angle += 2 * Math.PI;
            while (angle > 2 * Math.PI) angle -= 2 * Math.PI;

            Ray ray = new Ray(this, player.x, player.y, angle, 0, (lineW * i) + getWidth());
            g2.drawLine((int) (player.x / cellSize * miniMapScale), (int) (player.y / cellSize * miniMapScale), (int) ((Math.cos(angle) * ray.distance + player.x) / cellSize * miniMapScale), (int) ((Math.sin(angle) * ray.distance + player.y) / cellSize * miniMapScale));
            f++;
        }
        */
        g2.fillRect((int) (player.x / cellSize * miniMapScale - 2), (int) (player.y / cellSize * miniMapScale - 2), 4, 4);
    }

    public Input getInput() {
        return input;
    }

    public int getWidth() {
        return game.getWidth();
    }

    public int getHeight() {
        return game.getHeight();
    }

    public int getCellSize() {
        return cellSize;
    }

    public int[][] getMap() {
            return map;
    }

    public int darken(int value, int amount) {
        int red = (value >> 16) & 0xFF;
        int green = (value >> 8) & 0xFF;
        int blue = (value) & 0xFF;

        red -= amount;
        green -= amount;
        blue -= amount;

        if(red < 0) red = 0;
        if(red > 255) red = 255;
        if(green < 0) green = 0;
        if(green > 255) green = 255;
        if(blue < 0) blue = 0;
        if(blue > 255) blue = 255;

        return (red << 16) + (green << 8) + blue;
    }

    public BufferedImage getMinimap(int[][] map) {
        BufferedImage image = new BufferedImage(map[0].length, map.length, BufferedImage.TYPE_INT_RGB);
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        int count = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] != 0) {
                    pixels[count] = Color.RED.getRGB();
                } else {
                    pixels[count] = Color.WHITE.getRGB();
                }
                count++;
            }
        }
        return image;
    }
}
