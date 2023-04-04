import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Sprite extends Entity {

    protected BufferedImage image;

    private int screenX;
    private int lineW;
    private double px, py, pAngle, toAdd, fov;

    private double width, height;

    public double spriteAngle2, angleToP;


    public Sprite(Assets assets, double x, double y, String path) {
        super(assets, x, y);
        /*
        this.pAngle = pAngle;
        this.px = px;
        this.py = py;
        this.lineW = lineW;
        this.fov = fov;

         */
        image = ImageLoader.loadImage(path);
    }

    public void tick(double angle, double pAngle, double px, double py, int lineW, double fov) {
        //pAngle = AngleUtils.normalizeAngle(pAngle);
        angleToP = Math.atan2(py - y, px - x);
        //angleToP = AngleUtils.normalizeAngle(angleToP);
        distance = Math.hypot(px - x, py - y);
        double spriteAngle = angleToP - pAngle;
        spriteAngle = AngleUtils.normalizeAngle(spriteAngle);
        /*
        double adj = (assets.getWidth()) / (2 * Math.tan(fov/2));
        double centerAngle = Math.atan2(assets.getWidth()/2, adj);
        //double adj2 = distance * Math.cos(angleToP - pAngle);
        screenX = (int) (adj * Math.tan(spriteAngle - centerAngle));

         */



        //if(spriteAngle > 2 * Math.PI) spriteAngle -= Math.PI;
        //if(px - x < 0 && py - y >= 0) spriteAngle += Math.PI;
        //if(px - x < 0 && py - y < 0) spriteAngle -= Math.PI;

        spriteAngle2 = spriteAngle + Math.PI;
        spriteAngle2 = AngleUtils.normalizeAngle(spriteAngle2);
        //System.out.println(spriteAngle2);
        //double angleBetweenRays = fov/assets.getWidth();
        width =  (image.getWidth()/(distance/getCellSize()));
        height =  (image.getHeight()/(distance/getCellSize()));
        //spriteAngle2 = Math.atan((toAdd) * Math.tan(-fov/2) / assets.getScreenWidth()/2);
        toAdd = Math.tan(spriteAngle2)/Math.tan(-fov/2) * assets.getScreenWidth()/2;
        screenX = (int) (assets.getScreenWidth()/2 - width/2 - toAdd);
        //System.out.println(screenX);
        //System.out.println(spriteAngle2);
        //System.out.println(spriteAngle);
        //System.out.println("spriteAngle: " + (spriteAngle + Math.PI));


        //screenX = (int) (adj * Math.tan(spriteAngle));
        //screenX = (int)((assets.getWidth() / 2) * (1 + spriteAngle / fov));
        //screenX = (int) (assets.getWidth()/2 + assets.getWidth() * (angleToP + Math.PI- pAngle)/fov);


    }


    public void render(Graphics2D g2) {
        g2.drawImage(image, (int) screenX, (int) (assets.getScreenHeight()/2 - height/2), (int) width, (int) height, null);
        //g2.drawImage(image, assets.getWidth() - image.getWidth()/2, assets.getHeight() - image.getHeight()/2, null);
        //super.render(g2);
    }
}
