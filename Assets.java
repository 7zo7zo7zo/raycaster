import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Assets {

    private Game game;
    private Input input;

    private int cellSize = 5;
    /*
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

     */

    //private int[][] map;
    private int[][] map = readMapFromFile("/home/steve/IdeaProjects/FinalFingRaycaster/src/map.txt");

    private Player player;

    private int miniMapScale;
    private BufferedImage mapImage;

    private ArrayList<Entity> objects;

    private Sprite sprite;


    int numWallTextures = 3;
    ArrayList<BufferedImage[]> textureStrips;

    private BufferedImage texture;
    private BufferedImage[] lines;

    //JFileChooser fileChooser;

    public Assets(Game game) {
        this.game = game;
        input = game.getInput();

        player = new Player(this, cellSize * 1.5, cellSize * 1.5);

        miniMapScale = 10;
        //mapImage = getMinimap(map);

        textureStrips = new ArrayList<BufferedImage[]>();
        for(int i = 0; i < numWallTextures; i++) {
            String name = "wall" + (i+1) + ".png";
            BufferedImage texture = ImageLoader.loadImage(name);
            BufferedImage strips[] = new BufferedImage[texture.getWidth()];
            for(int j = 0; j < texture.getWidth(); j++) {
                strips[j] = texture.getSubimage(j, 0, 1, texture.getHeight());
            }
            textureStrips.add(strips);
        }

        texture = ImageLoader.loadImage("wall1.png");
        lines = new BufferedImage[texture.getWidth()];

        sprite = new Sprite(this, cellSize * 2.5, cellSize * 1.5, "s_barrel_0.png");

        objects = new ArrayList<Entity>();



        for(int i = 0; i < lines.length; i++) {
            lines[i] = texture.getSubimage(i, 0, 1, texture.getHeight());
        }
        /*
        fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            //System.out.println("HI");
            File file = fileChooser.getSelectedFile();
            map = readMapFromFile(file.getAbsolutePath());
            // Use the map array
        }

         */
    }

    public void tick() {
        player.tick();
        sprite.tick(0, player.angle, player.x, player.y, 1, Math.toRadians(90));
    }

    public void render(Graphics2D g2) {
        //Toolkit.getDefaultToolkit().sync();

        double fov = Math.toRadians(90);
        int lineW = 2;
        int rays = getScreenWidth()/lineW;
        double angle;
        double toAdd;

        int f = 0;
        for (int i = -rays / 2; i < rays / 2; i++) {
            toAdd = Math.atan(2 * (getScreenWidth() / 2 - lineW * f) * Math.tan(-fov/2) / getScreenWidth());
            angle = player.angle + toAdd;

            if (angle < 0) angle += 2 * Math.PI;
            if (angle > 2 * Math.PI) angle -= 2 * Math.PI;

            //Ray ray = new Ray(this, player.x, player.y, angle, 0);
            //Ray ray = new Ray(this, player.x, player.y, angle, 0, (lineW * i) + getWidth());

            Ray ray = new Ray(this, player.x, player.y, angle, player.angle, lineW, (lineW * i) + getScreenWidth() / 2, 0);

            //double distance = ray.distance;

            //double ca = player.angle - angle;
            //if (ca < 0) ca += 2 * Math.PI;
            //if (ca > 2 * Math.PI) ca -= 2 * Math.PI;
            //distance *= Math.cos(ca);

            //int lineH = (int) ((cellSize * getHeight()) / distance);
            ////if (lineH > getHeight()) lineH = getHeight();
            //int lineO = getHeight() / 2 - lineH / 2;

            //BufferedImage line = lines[(int) (texture.getWidth() * (ray.offset / cellSize))];

            /*
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

             */


            /*
            if (ray.yee) {
                for (int j = 0; j < line.getHeight(); j++) {
                    pixels[j] = darken(line.getRGB(0, j), (int) (-20 / cellSize));
                }
            }
            */

            //g2.drawImage(line, (lineW * i) + getWidth() / 2, lineO, lineW, lineH, null);

            ////g2.drawImage(line2, (lineW * i) + getWidth() / 2, lineO, lineW, lineH, null);
            //ray.render(g2);
            objects.add(ray);
            // maybe use render method inside the Ray object
            f++;
        }
        objects.add(sprite);
        Collections.sort(objects, new Comparator<Entity>() {
            @Override
            public int compare(Entity e1, Entity e2) {
                return Double.compare(e2.distance, e1.distance);
            }
        });

        // Print the sorted ArrayList
        for (Entity e : objects) {
            if(e instanceof Sprite) {
                if(!AngleUtils.isAngleBetween(AngleUtils.normalizeAngle(((Sprite) e).angleToP + Math.PI), AngleUtils.normalizeAngle(player.angle - fov/2), AngleUtils.normalizeAngle(player.angle + fov/2))) {
                    continue;
                }
            }
            e.render(g2);
        }
        objects.clear();

        //sprite.render(g2);

        mapImage = getMinimap(map);
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

    public int getScreenWidth() {
        return game.getWidth();
    }

    public int getScreenHeight() {
        return game.getHeight();
    }

    public int getX() {
        return game.getX();
    }

    public int getY() {
        return game.getY();
    }

    public void setCursor(Cursor cursor) {
        game.setCursor(cursor);
    }

    public Cursor getCursor() {
        return game.getCursor();
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
    public BufferedImage[] getLines() {
        return lines;
    }
    public Player getPlayer() {
        return player;
    }

    public ArrayList<BufferedImage[]> getTextureStrips() {
        return textureStrips;
    }

    public int[][] readMapFromFile(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            List<String> linez = new ArrayList<String>();
            String line3 = null;
            while ((line3 = reader.readLine()) != null) {
                linez.add(line3);
                //lineCount++;
            }
            reader.close();

            int numRows = linez.size();
            int numCols = linez.get(0).length();

            int[][] map = new int[numRows][numCols];

            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    char c = linez.get(i).charAt(j);
                    int num = Character.getNumericValue(c);
                    map[i][j] = num;
                }
            }

            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
