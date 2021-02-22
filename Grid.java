import java.util.HashMap;

public class Grid {
    public boolean safe;
    public boolean visited;
    private HashMap<String, String> flags; 

    public Grid(){
        safe = false;
        visited = false;
        flags = new HashMap<>();
        flags.put(Utils.pit, "O");
        flags.put(Utils.wumpus, "O");
     //   flags.put(Utils.gold, "O");
     //   flags.put(Utils.wall, "O");

    }

    public void updateFlags(String obj, String flag){
        flags.put(obj, flag);
    }
    public HashMap<String, String> getFlags() {
        return flags;
    }

}
