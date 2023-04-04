public class AngleUtils {
    public static boolean isAngleBetween(double angle, double lowerBound, double upperBound) {
        /*
        double normalizedAngle = normalizeAngle(angle);
        double normalizedLowerBound = normalizeAngle(lowerBound);
        double normalizedUpperBound = normalizeAngle(upperBound);

        if (normalizedLowerBound <= normalizedUpperBound) {
            // Bounding angles do not cross 0 radians
            return (normalizedAngle >= normalizedLowerBound) && (normalizedAngle <= normalizedUpperBound);
        } else {
            // Bounding angles cross 0 radians
            return (normalizedAngle >= normalizedLowerBound) || (normalizedAngle <= normalizedUpperBound);
        }
        */

        if (lowerBound <= upperBound) {
            // Bounding angles do not cross 0 radians
            return (angle >= lowerBound) && (angle <= upperBound);
        } else {
            // Bounding angles cross 0 radians
            return (angle >= lowerBound) || (angle <= upperBound);
        }
    }


    /*
    public static double normalizeAngle(double angle) {
        double normalized = angle % (2 * Math.PI);
        if (normalized < 0) {
            normalized += 2 * Math.PI;
        }
        return normalized;
    }
    */


    public static double normalizeAngle(double angle) {
        while (angle > Math.PI) {
            angle -= 2 * Math.PI;
        }
        while (angle <= -Math.PI) {
            angle += 2 * Math.PI;
        }
        return angle;
    }

    public static double normalizeAngle2(double angle) {
        while (angle > 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        while (angle <= 0) {
            angle += 2 * Math.PI;
        }
        return angle;
    }
}
