public class Ray extends Entity {

    double distance;
    int value;
    int side;
    double offset; // prob should be int

    Ray portal;

    int dof;

    //find way to set execute constructor in another constructorz
    //add dof to raycast
    public Ray(Assets assets, double x, double y, double angle, int dof, double screenX) {
        super(assets, x, y);
        this.angle = angle;
        this.dof = dof;

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
                    break;
                }
                xAddH += xStepH;
                yAddH += yStepH;
            }
        }

        if(dof < 100) {
            if (value == 5) {
                double exitX = 10 * getCellSize();
                double exitY = 10 * getCellSize() + (distance * Math.sin(angle) + y) % getCellSize();
                portal = new Ray(assets, exitX, exitY, angle, dof + 1, screenX);
                distance += portal.distance;
                value = portal.value;
                side = portal.side;
                offset = portal.offset;
            }
        }
    }

    public boolean check(double x, double y) {
        if(getMap()[(int)(y/getCellSize())][(int)(x/getCellSize())] != 0) {
            return true;
        }
        else {
            return false;
        }
    }
}
