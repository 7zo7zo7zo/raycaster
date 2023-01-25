public class Player extends Entity {

    private int speed;

    private double threshold, relMoveAngle, moveAngle;
    private double nextX, nextY;

    private boolean moving;
    int invertX, invertY;

    public Player(Assets assets, double x, double y) {
        super(assets, x, y);
        angle = 0;
        speed = 1;
        moving = false;
        threshold = 0.5;
    }

    public void tick() {
        if(assets.getInput().left) {
            angle -= Math.PI / 64;
        }
        if(assets.getInput().right) {
            angle += Math.PI / 64;
        }
        while (angle < 0) angle += 2 * Math.PI;
        while (angle > 2 * Math.PI) angle -= 2 * Math.PI;

        if(assets.getInput().up) {
            relMoveAngle = 0;
            moving = true;
            //nextX = x + Math.cos(angle) * speed;
            //nextY = y + Math.sin(angle) * speed;
        }
        if(assets.getInput().down) {
            relMoveAngle = Math.PI;
            moving = true;
            //nextX = x - Math.cos(angle) * speed;
            //nextY = y - Math.sin(angle) * speed;
        }

        if(moving) {
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
        }
        moving = false;
    }
    public int getValue(double x, double y) {
        return getMap()[(int) y / getCellSize()][(int) x / getCellSize()];
    }

    public boolean check(double x, double y) {
        return getValue(x, y) == 0;
    }
}
