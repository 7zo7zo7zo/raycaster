import java.awt.*;
import java.awt.image.BufferedImage;

public class Ray extends Entity {

    //double distance;
    int value;
    int side;
    double offset; // prob should be int

    double hitX, hitY;

    double distanceCA;

    Ray portal;

    int dof;

    int screenX;

    int lineW, lineH, lineO;

    double ca, pAngle;

    BufferedImage line;

    //find way to set execute constructor in another constructorz
    //add dof to raycast
    public Ray(Assets assets, double x, double y, double angle, double pAngle, int lineW, int screenX, int dof) {
        super(assets, x, y);
        this.angle = angle;
        this.dof = dof;

        this.lineW = lineW;
        this.screenX = screenX;

        shouldRender = true;

        double xAddV, yAddV, xAddH, yAddH,  xStepV, yStepV, xStepH, yStepH;
        if(angle < Math.PI/2 || angle > 3 * Math.PI/2) {
            xAddV = getCellSize() - (x - (int)(x/getCellSize()) * getCellSize() - 0.000001);
            xStepV = getCellSize();
        }
        else {
            xAddV =  -(x - (int)(x/getCellSize()) * getCellSize() + 0.000001);
            xStepV = -getCellSize();
        }
        yAddV = xAddV * Math.tan(angle);
        yStepV = xStepV * Math.tan(angle);
        if(angle < Math.PI) {
            yAddH = getCellSize() - (y - (int)(y/getCellSize()) * getCellSize() - 0.000001);
            yStepH = getCellSize();
        }
        else {
            yAddH = -(y - (int)(y/getCellSize()) * getCellSize() + 0.000001);
            yStepH = -getCellSize();
        }
        xAddH = yAddH/Math.tan(angle);
        xStepH = yStepH/Math.tan(angle);

        while(true) {
            if(Math.abs(xAddV) <= Math.abs(xAddH)) {
                if(check(x + xAddV, y + yAddV)) {
                    distance = Math.hypot(xAddV, yAddV);
                    value = getMap()[(int)(y + yAddV)/getCellSize()][(int)(x + xAddV)/getCellSize()];
                    side = 1;
                    if(xStepV>0) offset = (y + yAddV) % getCellSize();
                    else offset = getCellSize() - (y + yAddV) % getCellSize();
                    hitX = x + xAddV;
                    hitY = y + yAddV;
                    break;
                }
                xAddV += xStepV;
                yAddV += yStepV;
            }
            else {
                if(check(x + xAddH, y + yAddH)) {
                    distance = Math.hypot(xAddH, yAddH);
                    value = getMap()[(int)(y + yAddH)/getCellSize()][(int)(x + xAddH)/getCellSize()];
                    side = 0;
                    if(yStepH>0) offset = (x + xAddH) % getCellSize();
                    else offset = getCellSize() - (x + xAddH) % getCellSize();
                    hitX = x + xAddH;
                    hitY = y + yAddH;
                    break;
                }
                xAddH += xStepH;
                yAddH += yStepH;
            }
        }

        if(dof < 5) {
            if (value == 5) {
                double exitX = 10 * getCellSize();
                double exitY = 10 * getCellSize() + (distance * Math.sin(angle) + y) % getCellSize();
                portal = new Ray(assets, exitX, exitY, angle, pAngle, lineW, screenX,dof + 1);
                //portal = new Ray(assets, exitX, exitY, angle, dof + 1, screenX);
                distance += portal.distance;
                value = portal.value;
                side = portal.side;
                offset = portal.offset;
            }
        }

        ca = pAngle - angle;
        if (ca < 0) ca += 2 * Math.PI;
        if (ca > 2 * Math.PI) ca -= 2 * Math.PI;
        distanceCA = distance * Math.cos(ca);
        lineH = (int) ((getCellSize() * assets.getScreenHeight()) / distanceCA);
        //if (lineH > assets.getHeight()) lineH = assets.getHeight();
        lineO = assets.getScreenHeight() / 2 - lineH / 2;
        //line = assets.getLines()[(int) (assets.getLines().length * (offset / getCellSize()))];
        if(value > assets.getTextureStrips().size()) value = 1;
        BufferedImage[] currTexture = assets.getTextureStrips().get(value - 1);
        line = currTexture[(int) (currTexture.length * (offset / getCellSize()))];
    }

    public boolean check(double x, double y) {
        if(getMap()[(int)(y/getCellSize())][(int)(x/getCellSize())] != 0) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(line, screenX, lineO, lineW, lineH, null);
    }

    /*
    @Override
    public double getX() {
        return hitX;
    }
    @Override
    public double getY() {
        return hitY;
    }

     */
}
