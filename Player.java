import javax.swing.*;
import java.awt.*;

public class Player extends Entity {

    private double speed;

    private double threshold, relMoveAngle, moveAngle, vertMoveAngle, sideMoveAngle;
    private double nextX, nextY;

    private boolean moving;
    private byte invertX, invertY;
    Robot robot;

    private Cursor blankCursor;
    private Cursor defaultCursor;

    public Player(Assets assets, double x, double y) {
        super(assets, x, y);
        angle = 0;
        speed = 0.2;
        moving = false;
        threshold = 0.5;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(new byte[0]).getImage(), new Point(0,0), "blank cursor");
        defaultCursor = Cursor.getDefaultCursor();
    }

    public void tick() {
        if(assets.getInput().left) {
            angle -= Math.PI / 64;
        }
        if(assets.getInput().right) {
            angle += Math.PI / 64;
        }

        int middleX = assets.getScreenWidth() / 2;

        if (assets.getInput().mouseCaptured) {
            //assets.setCursor(blankCursor);
            if(assets.getCursor() == defaultCursor) assets.setCursor(blankCursor);
            if (assets.getInput().mouseX + 1 != middleX) {
                angle += (assets.getInput().mouseX - middleX) * 0.005;
                robot.mouseMove(assets.getX() + middleX, (int) (assets.getY() + assets.getScreenHeight() / 2));
            }
        }
        else {
            //assets.setCursor(blankCursor);
            if(assets.getCursor() == blankCursor) assets.setCursor(defaultCursor);
        }
        //System.out.println(assets.getInput().mouseX);
        //System.out.println(middleX);

        //angle += assets.getInput().xChange * 0.005;
        while (angle < 0) angle += 2 * Math.PI;
        while (angle > 2 * Math.PI) angle -= 2 * Math.PI;

        if(assets.getInput().forward) {
            vertMoveAngle = 1;
            moving = true;
        }
        if(assets.getInput().backward) {
            vertMoveAngle = -1;
            moving = true;
        }
        if(assets.getInput().strafeL) {
            sideMoveAngle = -1;
            moving = true;
        }
        if(assets.getInput().strafeR) {
            sideMoveAngle = 1;
            moving = true;
        }

        if(moving) {
            relMoveAngle = Math.atan2(sideMoveAngle, vertMoveAngle);
            nextX = Math.cos(moveAngle) * speed + x;
            nextY = Math.sin(moveAngle) * speed + y;

            moveAngle = angle + relMoveAngle;

            while (moveAngle < 0) moveAngle += 2 * Math.PI;
            while (moveAngle > 2 * Math.PI) moveAngle -= 2 * Math.PI;

            //if(moveAngle < Math.PI / 2 || moveAngle > 3 * Math.PI / 2) invertX = 1;
            //else invertX = -1;
            //if(moveAngle < Math.PI) invertY = 1;
            //else invertY = -1;

            if(Math.cos(moveAngle) < 0) invertX = -1;
            else invertX = 1;
            if(Math.sin(moveAngle) < 0) invertY = -1;
            else invertY = 1;

            if (getMap()[(int) y / getCellSize()][(int) (nextX + threshold * invertX) / getCellSize()] == 0) {
                x = nextX;
            }

            if (getMap()[(int) (nextY + threshold * invertY) / getCellSize()][(int) x / getCellSize()] == 0) {
                y = nextY;
            }

            moving = false;
            vertMoveAngle = 0;
            sideMoveAngle = 0;
        }
    }
    public int getValue(double x, double y) {
        return getMap()[(int) y / getCellSize()][(int) x / getCellSize()];
    }

    public boolean check(double x, double y) {
        return getValue(x, y) == 0;
    }
}
