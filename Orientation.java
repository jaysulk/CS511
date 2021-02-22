public class Orientation {

    public static int NORTH = 0;
    public static int EAST = 1;
    public static int SOUTH = 2;
    public static int WEST = 3;

    public Orientation() {

        // nothing to construct...

    }

    public static String printOrientation(int direction) {

        if (direction == 0) return "NORTH";
        else if (direction == 1) return "EAST";
        else if (direction == 2) return "SOUTH";
        else return "WEST";

    }


}
