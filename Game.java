import java.awt.*;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Game implements Runnable {

    private String title;
    private int width;
    private int height;

    private JFrame frame;
    private Canvas canvas;

    private BufferStrategy bs;

    private Graphics2D g2;

    private Thread thread;
    private boolean running = false;

    private Input input;

    private Assets assets;

    public Game(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;

        frame = new JFrame();
        canvas = new Canvas();

        input = new Input();

        setupCanvas();
        setupJFrame();

        assets = new Assets(this);
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
        width = frame.getWidth();
        height = frame.getHeight();
        input.tick();
        assets.tick();
    }

    public void render() {
        //Toolkit.getDefaultToolkit().sync();

        bs = canvas.getBufferStrategy();
        if (bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }
        g2 = (Graphics2D) bs.getDrawGraphics();

        // Clear
        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, width, height/2);
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(0, height/2, width, height);

        // Draw
        Toolkit.getDefaultToolkit().sync();
        assets.render(g2);

        // End
        bs.show();
        g2.dispose();
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

    private void setupJFrame() {
        //frame.setResizable(false);
        frame.setSize(width, height);
        frame.setTitle(title);
        frame.add(canvas);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addKeyListener(input);
        //frame.addMouseMotionListener(input);
    }

    private void setupCanvas() {
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setFocusable(false);
        canvas.setBackground(Color.BLACK);
        canvas.addMouseMotionListener(input);
    }

    public Input getInput() {
        return input;
    }

    public int getWidth() {
        return frame.getWidth();
    }

    public int getHeight() {
        return frame.getHeight();
    }

    public int getX() {
        return frame.getX();
    }

    public int getY() {
        return frame.getY();
    }

    public void setCursor(Cursor cursor) {
        frame.setCursor(cursor);
    }
    public Cursor getCursor() {
        return frame.getCursor();
    }
}
