import java.util.HashMap;

public class Cell {
    private boolean safe;
    private boolean visited;
    private HashMap<String, String> flags; //pit - ?/!, wumpus - ?/!

    public Cell(){
        safe = false;
        visited = false;
        flags = new HashMap<>();
        flags.put(Utils.pit, "");
        flags.put(Utils.wumpus, "");
    }

    public void updateFlags(String obj, String flag){
        flags.put(obj, flag);
    }

    public boolean isSafe() {
        return safe;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    public HashMap<String, String> getFlags() {
        return flags;
    }

    public void setFlags(HashMap<String, String> flags) {
        this.flags = flags;
    }
}
