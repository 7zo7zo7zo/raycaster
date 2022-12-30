public class Player extends Entity {
 
   private int speed;
 
   private double radius;
   private double nextX, nextY, nextXtest, nextYtest;
 
   public Player(Assets assets, double x, double y) {
       super(assets, x, y);
       angle = 0;
       speed = 1;
       radius = 0.5;
   }
 
   public void tick() {
       if(assets.getInput().left) {
           angle -= Math.PI/64;
       }
       if(assets.getInput().right) {
           angle += Math.PI/64;
       }
       while (angle < 0) angle += 2 * Math.PI;
       while (angle > 2 * Math.PI) angle -= 2 * Math.PI;
 
       if(assets.getInput().up) {
           nextX = x + Math.cos(angle) * speed;
           nextY = y + Math.sin(angle) * speed;
           nextXtest = nextX + radius * Math.signum(Math.cos(angle));
           nextYtest = nextY + radius * Math.signum(Math.sin(angle));
       }
       if(assets.getInput().down) {
           nextX = x - Math.cos(angle) * speed;
           nextY = y - Math.sin(angle) * speed;
           nextXtest = nextX - radius * Math.signum(Math.cos(angle));
           nextYtest = nextY - radius * Math.signum(Math.sin(angle));
       }
 
       if (getMap()[(int) y/getCellSize()][(int) nextXtest/getCellSize()] == 0) {
           x = nextX;
       }
 
       if (getMap()[(int) nextYtest/getCellSize()][(int) x/getCellSize()] == 0) {
           y = nextY;
       }
   }
   public int getValue(double x, double y) {
       return getMap()[(int) y/getCellSize()][(int) x/getCellSize()];
   }
 
   public boolean check(double x, double y) {
       return getValue(x, y) == 0;
   }
}
