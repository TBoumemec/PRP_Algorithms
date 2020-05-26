package Logic;

import java.util.HashMap;

public class PowerRelay {

    public static boolean isBlocked(double angle, String direction) {
        double boundUp = 90.0 / 180 * Math.PI;
        double boundDown = 270.0 / 180 * Math.PI;

        switch (direction) {
            case "left":
                return (angle < boundUp | angle > boundDown);
            case "right":

                return (angle > boundUp) & (angle < boundDown);
            default:
                return true;
        }
    }

}
