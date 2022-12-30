import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

public class Game implements Runnable {

    private String title;
    private int width;
    private int height;

    private JFrame frame;
    private Canvas canvas;

    private Input input;

    private Graphics2D g;
    private BufferStrategy bs;

    private Thread thread;
    private boolean running = false;

    BufferedImage image;
    BufferedImage[] lines;

    int miniMapScale = 10;
    public int[][] map =
            {
                    {1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2},
                    {1, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9},
                    {1, 0, 3, 3, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {1, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 9, 2, 0, 0, 2},
                    {1, 0, 3, 0, 0, 0, 3, 0, 2, 2, 2, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {1, 0, 3, 0, 0, 0, 3, 0, 2, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 2},
                    {1, 0, 3, 3, 0, 3, 3, 0, 2, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 2},
                    {1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 4, 0, 1, 9, 1, 0, 0, 2},
                    {1, 1, 1, 9, 1, 1, 1, 1, 4, 4, 4, 0, 4, 4, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2},
                    {1, 0, 0, 0, 0, 0, 1, 4, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {1, 0, 0, 0, 0, 0, 1, 4, 0, 0, 0, 0, 0, 1, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {1, 0, 0, 0, 0, 0, 1, 4, 0, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 0, 0, 0, 0, 2},
                    {1, 0, 0, 0, 0, 0, 1, 4, 0, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 1, 0, 2},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 2},
                    {1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 2, 1, 9, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 2}
            };


    private int cellSize = 5;

    //private int[][] map = multiplyMap(miniMap, cellSize);

    private BufferedImage mapImage = getMinimap(map);

    private Player player;
    private ImageEntity sprite;

    private Color color;

    public Game(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;

        frame = new JFrame();
        canvas = new Canvas();
        input = new Input();

        player = new Player(this, cellSize, cellSize);
        sprite = new ImageEntity(this, cellSize + getCellSize()/2, cellSize + cellSize/2, "knight.png");

        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setFocusable(false);
        canvas.setBackground(Color.BLACK);

        frame.setResizable(false);
        frame.setSize(width, height);
        frame.setTitle(title);
        frame.add(canvas);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addKeyListener(input);


        image = ImageLoader.loadImage("tile.png");
        lines = new BufferedImage[image.getWidth()];

        for(int i = 0; i < lines.length; i++) {
            lines[i] = image.getSubimage(i, 0, 1, image.getHeight());
        }

    }

    public void run() {
        int ticks = 0;
        int frames = 0;

        double tps = 60.0;
        double nsPerTick = 1000000000.0 / tps;
        long lastTime = System.nanoTime();
        long now;
        long timer = 0;
        double delta = 0;

        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            timer += now - lastTime;
            lastTime = now;
            if (delta >= 1) {
                tick();
                ticks++;
                delta--;
            }
	        Toolkit.getDefaultToolkit().sync();
            render();
            frames++;
            if (timer >= 1000000000) {
                System.out.println(ticks + " tps, " + frames + " fps");
                frame.setTitle(title + "  |  " + ticks + " tps, " + frames + " fps");
                ticks = 0;
                frames = 0;
                timer = 0;
            }
        }
        stop();
    }

    public void tick() {
        input.tick();
        player.tick();
    }

    public void render() {

        bs = canvas.getBufferStrategy();
        if (bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }
        g = (Graphics2D) bs.getDrawGraphics();

        // Clear

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, width, height / 2);
        g.setColor(Color.BLACK);
        g.fillRect(0, height / 2, width, height / 2);

        // Draw

        mainRender();

        // End

        bs.show();
        g.dispose();
    }

    public synchronized void start() {
        if (running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Input getInput() {
        return input;
    }

    public int[][] getMap() {
        return map;
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

    public double[] castRay(double x, double y, double angle) {
        double result[] = new double[4];
        double xAddV, yAddV, xAddH, yAddH,  xStepV, yStepV, xStepH, yStepH;
        if(angle < Math.PI/2 || angle > 3 * Math.PI/2) {
            xAddV = cellSize - (x - (int)(x/cellSize) * cellSize - 0.000001);
            xStepV = cellSize;
        }
        else {
            xAddV =  -(x - (int)(x/cellSize) * cellSize + 0.000001);
            xStepV = -cellSize;
        }
        yAddV = xAddV * Math.tan(angle);
        yStepV = xStepV * Math.tan(angle);
        if(angle < Math.PI) {
            yAddH = cellSize - (y - (int)(y/cellSize) * cellSize - 0.000001);
            yStepH = cellSize;
        }
        else {
            yAddH = -(y - (int)(y/cellSize) * cellSize + 0.000001);
            yStepH = -cellSize;
        }
        xAddH = yAddH/Math.tan(angle);
        xStepH = yStepH/Math.tan(angle);

        while(true) {
            if(Math.abs(xAddV) <= Math.abs(xAddH)) {
                if(check(x + xAddV, y + yAddV)) {
                    result[0] = Math.hypot(xAddV, yAddV);
                    result[1] = map[(int)(y + yAddV)/cellSize][(int)(x + xAddV)/cellSize];
                    result[2] = 1;
                    //result[3] = (y + yAddV)%cellSize;
                    if(xStepV>0) result[3] = (y + yAddV)%cellSize;
                    else result[3] = cellSize - (y + yAddV)%cellSize;
                    break;
                }
                xAddV += xStepV;
                yAddV += yStepV;
            }
            else {
                if(check(x + xAddH, y + yAddH)) {
                    result[0] = Math.hypot(xAddH, yAddH);
                    result[1] = map[(int)(y + yAddH)/cellSize][(int)(x + xAddH)/cellSize];
                    result[2] = 0;
                    //result[3] = (x + xAddH)%cellSize;
                    if(yStepH>0) result[3] = (x + xAddH)%cellSize;
                    else result[3] = cellSize - (x + xAddH)%cellSize;
                    break;
                }
                xAddH += xStepH;
                yAddH += yStepH;
            }
        }
        return result;
    }

    public boolean check(double x, double y) {
        if(map[(int)(y/cellSize)][(int)(x/cellSize)] != 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public int getCellSize() {
        return cellSize;
    }

    public boolean angleBetween(double target, double angle1, double angle2) {
        if(angle1 <= angle2) {
            if(angle2 - angle1 <= Math.PI) {
                return angle1 <= target && target <= angle2;
            } else {
                return angle2 <= target || target <= angle1;
            }
        } else {
            if(angle1 - angle2 <= Math.PI) {
                return angle2 <= target && target <= angle1;
            } else {
                return angle1 <= target || target <= angle2;
            }
        }
    }

    public Color darken(Color color, int amount) {
        int red = color.getRed() - amount;
        int green = color.getGreen() - amount;
        int blue = color.getBlue() - amount;
        if (red < 0) red = 0;
        if (red > 255) red = 255;
        if (green < 0) green = 0;
        if (green > 255) green = 255;
        if (blue < 0) blue = 0;
        if (blue > 255) blue = 255;

        return new Color(red, green, blue);
    }

    public void mainRender() {
        double angleBetweenRays = Math.toRadians(0.2);
        double fov = Math.toRadians(80);
        int rays = (int) (fov / angleBetweenRays);
        //int rays = (int) Math.round((fov/angleBetweenRays) / 2) * 2;
        int lineW = width / rays;

        double angle = 0;

        ArrayList<Double[]> savedRays = new ArrayList<Double[]>();

        for (int i = -rays / 2; i < rays / 2; i++) {

            angle = player.angle + angleBetweenRays * i;

            if (angle < 0) angle += 2 * Math.PI;
            if (angle > 2 * Math.PI) angle -= 2 * Math.PI;

            double[] ray = castRay(player.x, player.y, angle);
            double distance = ray[0];

            /*
            if (ray[1] == 9) {
                if(ray[2] == 1) {
                    double exitX = 10 * cellSize;
                    double exitY = 10 * cellSize + (distance * Math.sin(angle) + player.y) % cellSize;
                    double[] ray2 = castRay(exitX, exitY, angle);
                    distance += ray2[0];
                    ray[1] = ray2[1];
                    ray[2] = ray2[2];
                }
                else {
                    double exitX = 8 * cellSize + (distance * Math.cos(angle) + player.x) % cellSize;;
                    double exitY = 12 * cellSize;
                    double[] ray2 = castRay(exitX, exitY, angle);
                    distance += ray2[0];
                    ray[1] = ray2[1];
                    ray[2] = ray2[2];
                }
            }

             */

            double darknessScale = 3;

            //if(ray[1] == 1) color = Color.RED;
            //if(ray[1] == 2) color = Color.BLUE;
            //if(ray[1] == 3) color = Color.GREEN;
            //if(ray[1] == 4) color = Color.PINK;


            if(ray[2] == 1) color = Color.RED;
            else color = Color.BLUE;
            color = darken(color, (int) (distance * darknessScale));

            double ca = player.angle - angle;
            if (ca < 0) ca += 2 * Math.PI;
            if (ca > 2 * Math.PI) ca -= 2 * Math.PI;
            distance *= Math.cos(ca);

            int lineH = (int) ((cellSize * height) / distance);
            if (lineH > height) lineH = height;
            int lineO = height / 2 - lineH / 2;




            g.setColor(color);

            //g.fillRect((lineW * i) + width / 2, lineO, lineW, lineH);

            /*
            BufferedImage image = ImageLoader.loadImage("tile.png");
            int imageHeight = image.getHeight();

            while(true) {
                g.fillRect();
            }

            */




            Double[] thisRay = {ray[0], (double) ((lineW * i) + width / 2), distance, ray[3]};
            savedRays.add(thisRay);
        }


        g.drawImage(mapImage, 0, 0, mapImage.getWidth() * miniMapScale, mapImage.getHeight() * miniMapScale, null);
        g.setColor(Color.BLUE);
        g.fillRect((int) (player.x / cellSize * miniMapScale - 2), (int) (player.y / cellSize * miniMapScale - 2), 4, 4);
        g.drawLine((int) (player.x / cellSize * miniMapScale), (int) (player.y / cellSize * miniMapScale), (int) (player.xDir * 10 + player.x / cellSize * miniMapScale), (int) (player.yDir * 10 + player.y / cellSize * miniMapScale));

        //g.drawLine((int) player.x, (int) player.y, getWall(player.x, player.y, player.angle).x, getWall(player.x, player.y, player.angle).y);

        //double tempRay[] = castRay(player.x, player.y, player.angle);

        //g.drawLine((int) (player.x / cellSize * miniMapScale), (int) (player.y / cellSize * miniMapScale), (int) ((player.xDir * tempRay[0] + player.x) / cellSize * miniMapScale), (int) ((player.yDir * tempRay[0] + player.y) / cellSize * miniMapScale));
        //g.drawLine((int) (player.x / cellSize * miniMapScale), (int) (player.y / cellSize * miniMapScale), (int) (Math.cos((player.angle + 0.7)) * 20 + player.x / cellSize * miniMapScale), (int) (Math.sin((player.angle + 0.7)) * 20 + player.y / cellSize * miniMapScale));

        /*
        for (int i = -rays / 2; i < rays / 2; i++) {
            angle = player.angle + angleBetweenRays * i;
            g.setColor(Color.BLUE);
            g.drawLine((int) player.x, (int) player.y, getWall(player.x, player.y, angle).x, getWall(player.x, player.y, angle).y);
        }
        */

        //g.setColor(Color. RED);
        //g.drawString("x: " + player.x, 10, 170);
        //g.drawString("y: " + player.y, 10, 180);
        //g.drawString("angle: " + player.angle, 10, 190);

        double x = player.x - sprite.x;
        double y = player.y - sprite.y;
        double distance = Math.hypot(y, x);
        double spriteAngle = Math.atan(y/x);
        //while (spriteAngle < 0) spriteAngle += 2 * Math.PI;
        //while (spriteAngle > 2 * Math.PI) spriteAngle -= 2 * Math.PI;
        if(x < 0 && y >= 0) spriteAngle += Math.PI;
        if(x < 0 && y < 0) spriteAngle -= Math.PI;
        double firstAngle = player.angle - fov/2;
        double secondAngle = player.angle + fov/2;

        if(angleBetween(spriteAngle + Math.PI, firstAngle, secondAngle)) {
            int screenX = (int) (width/2 + lineW*(spriteAngle + Math.PI - player.angle)/angleBetweenRays);

            int spriteWidth = sprite.image.getWidth();
            int spriteHeight = sprite.image.getHeight();
            spriteWidth = (int) (spriteWidth/(distance/cellSize));
            spriteHeight = (int) (spriteHeight/(distance/cellSize));


            //g.setColor(Color.pink);
            //g.fillOval(screenX - spriteWidth/2, height/2 - spriteHeight/2, spriteWidth, spriteHeight);
            //g.drawImage(sprite.image.getScaledInstance(spriteWidth, spriteHeight, Image.SCALE_DEFAULT), screenX - spriteWidth/2, height/2 - spriteHeight/2, null);
        }



        int i = 0;

        // should add check here to see if sprite wall render logic should be used
        while(i < savedRays.size()) {
            if(savedRays.get(i)[0] > distance) {
                int lineH = (int) ((cellSize * height) / savedRays.get(i)[2]);
                //if (lineH > height) lineH = height;
                int lineO = height / 2 - lineH / 2;
                //g.setColor(Color.RED);
                //g.fillRect(savedRays.get(i)[1].intValue(), lineO, lineW, lineH);
                //BufferedImage image = ImageLoader.loadImage("tile.png");
                //image = image.getSubimage((int) (image.getWidth() * (savedRays.get(i)[3]/cellSize)), 0, 1, image.getHeight());
                //g.drawImage(image, savedRays.get(i)[1].intValue(), lineO, lineW, lineH, null);
                g.drawImage(lines[(int) (image.getWidth() * (savedRays.get(i)[3]/cellSize))], savedRays.get(i)[1].intValue(), lineO, lineW, lineH, null);
                savedRays.remove(i);
            }
            else {
                i++;
            }
        }
        //System.out.println(savedRays.size());
        if(angleBetween(spriteAngle + Math.PI, firstAngle, secondAngle)) {
            int screenX = (int) (width/2 + lineW*(spriteAngle + Math.PI - player.angle)/angleBetweenRays);

            int spriteWidth = sprite.image.getWidth();
            int spriteHeight = sprite.image.getHeight();
            spriteWidth = (int) (spriteWidth/(distance/cellSize));
            spriteHeight = (int) (spriteHeight/(distance/cellSize));
            if(spriteWidth > sprite.image.getWidth() || spriteHeight > sprite.image.getHeight()) {
                spriteWidth = sprite.image.getWidth();
                spriteHeight = sprite.image.getHeight();
            }


            //g.setColor(Color.pink);
            //g.fillOval(screenX - spriteWidth/2, height/2 - spriteHeight/2, spriteWidth, spriteHeight);
            if(spriteWidth != sprite.image.getWidth() || spriteHeight != sprite.image.getHeight()) {
                //g.drawImage(sprite.image.getScaledInstance(spriteWidth, spriteHeight, Image.SCALE_DEFAULT), screenX - spriteWidth / 2, height / 2 - spriteHeight / 2, null);
                g.drawImage(sprite.image, screenX - spriteWidth / 2, height / 2 - spriteHeight / 2, spriteWidth, spriteHeight, null);
            }
            else {
                g.drawImage(sprite.image, screenX - spriteWidth / 2, height / 2 - spriteHeight / 2, null);
            }
            //g.drawImage(resizeImage(sprite.image, spriteWidth, spriteHeight), screenX - spriteWidth/2, height/2 - spriteHeight/2, null);
            //g.drawImage(sprite.image, screenX - 256/2, height/2 - 256/2, null);
            //System.out.println(screenX);
        }

        for(int j = 0; j < savedRays.size(); j++) {
            int lineH = (int) ((cellSize * height) / savedRays.get(j)[2]);
            //if (lineH > height) lineH = height;
            int lineO = height / 2 - lineH / 2;
            //g.fillRect(savedRays.get(j)[1].intValue(), lineO, lineW, lineH);
            g.drawImage(lines[(int) (image.getWidth() * (savedRays.get(j)[3]/cellSize))], savedRays.get(j)[1].intValue(), lineO, lineW, lineH, null);
        }
        savedRays.clear();


    }

    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TRANSLUCENT);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
}
