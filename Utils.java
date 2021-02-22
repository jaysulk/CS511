import java.util.HashMap;

public class Utils {

    public static String pit = "pit";
    public static String wumpus = "wumpus";
    public static String gold = "gold";
    public static String wall = "wall";

    public static boolean isNeighbor(int[] agentLocation, int row, int col) {
        if((agentLocation[0] + 1 == row && agentLocation[1] == col) || (agentLocation[0] - 1 == row && agentLocation[1] == col) ||
                (agentLocation[0] == row && agentLocation[1] + 1 == col) || (agentLocation[0]== row && agentLocation[1] - 1 == col))
            return true;
        return false;
    }

    public static void resolve(Model model, int row, int col, TransferPercept tp) {

        HashMap<String, String> flags = model.getWorld()[row][col].getFlags();

        if(model.getWorld()[row][col].isSafe())
            return;

        if(flags.get(pit).equals("X") &&
                flags.get(wumpus).equals("X")) {
            if (!(tp.getBreeze() || tp.getStench())) {
                model.getWorld()[row][col].setSafe(true);
                flags.put(pit, "");
                flags.put(wumpus, "");
            }
        }

        if(flags.get(pit).equals("X") && !flags.get(wumpus).equals("X")){
            if(!tp.getBreeze()){
                model.getWorld()[row][col].setSafe(true);
                flags.put(pit, "");
            }
        }

        if(!flags.get(pit).equals("X") && flags.get(wumpus).equals("X")){
            if(!tp.getStench()){
                model.getWorld()[row][col].setSafe(true);
                flags.put(wumpus, "");
            }
        }
    }

    public static boolean check(Model model, int[] location) {

        switch (model.getAgentOrientation()){
            case 0:
                return model.getAgentLocation()[0] - 1 == location[0] && model.getAgentLocation()[1] == location[1];
            case 1:
                return model.getAgentLocation()[0] == location[0] && model.getAgentLocation()[1] + 1 == location[1];
            case 2:
                return model.getAgentLocation()[0] + 1 == location[0] && model.getAgentLocation()[1] == location[1];
            case 3:
                return model.getAgentLocation()[0] == location[0] && model.getAgentLocation()[1] - 1 == location[1];
        }
        return false;
    }

    public static void setFlags(Model model, String obj, String flag, boolean safe){
        Cell[][] map = model.getWorld();
        int[] agentLocation = model.getAgentLocation();
        
        if(agentLocation[0] + 1 < map.length && !map[agentLocation[0] + 1][agentLocation[1]].isSafe()){
            if(safe) {
                map[agentLocation[0] + 1][agentLocation[1]].setSafe(safe);
                map[agentLocation[0] + 1][agentLocation[1]].getFlags().put(wumpus, "");
                map[agentLocation[0] + 1][agentLocation[1]].getFlags().put(pit, "");
            }else
                map[agentLocation[0] + 1][agentLocation[1]].updateFlags(obj, flag);
        }
        if(agentLocation[0] - 1 >= 0 && !map[agentLocation[0] - 1][agentLocation[1]].isSafe()) {
            if(safe) {
                map[agentLocation[0] - 1][agentLocation[1]].setSafe(safe);
                map[agentLocation[0] - 1][agentLocation[1]].getFlags().put(wumpus, "");
                map[agentLocation[0] - 1][agentLocation[1]].getFlags().put(pit, "");
            }else
                map[agentLocation[0] - 1][agentLocation[1]].updateFlags(obj, flag);
        }
        if(agentLocation[1] + 1 < map[0].length && !map[agentLocation[0]][agentLocation[1] + 1].isSafe()){
            if(safe) {
                map[agentLocation[0]][agentLocation[1] + 1].setSafe(safe);
                map[agentLocation[0]][agentLocation[1] + 1].getFlags().put(wumpus, "");
                map[agentLocation[0]][agentLocation[1] + 1].getFlags().put(pit, "");
            }else
                map[agentLocation[0]][agentLocation[1] + 1].updateFlags(obj, flag);
        }
        if(agentLocation[1] - 1 >= 0 && !map[agentLocation[0]][agentLocation[1] - 1].isSafe()){
            if(safe) {
                map[agentLocation[0]][agentLocation[1] - 1].setSafe(safe);
                map[agentLocation[0]][agentLocation[1] - 1].getFlags().put(wumpus, "");
                map[agentLocation[0]][agentLocation[1] - 1].getFlags().put(pit, "");
            }else
                map[agentLocation[0]][agentLocation[1] - 1].updateFlags(obj, flag);
        }
    }

    public static int[] getNextSafeCell(Model model) {

        Cell[][] map = model.getWorld();
        int[] agentLocation = model.getAgentLocation();
        int[] nextAgentLocation = model.getNextAgentLocation();
        
        if(nextAgentLocation[0] != -1){
            return nextAgentLocation;
        }

        boolean flag = false;

        if(agentLocation[1] + 1 < map[0].length && map[agentLocation[0]][agentLocation[1] + 1].isSafe()){
            if(!map[agentLocation[0]][agentLocation[1] + 1].isVisited())
                flag = true;

            nextAgentLocation[0] = agentLocation[0];
            nextAgentLocation[1] = agentLocation[1] + 1;
        }
        if(!flag && agentLocation[0] - 1 >= 0 && map[agentLocation[0] - 1][agentLocation[1]].isSafe()) {
            if(!map[agentLocation[0] - 1][agentLocation[1]].isVisited())
                flag = true;

            nextAgentLocation[0] = agentLocation[0] - 1;
            nextAgentLocation[1] = agentLocation[1];
        }
        if(!flag && agentLocation[0] + 1 < map.length && map[agentLocation[0] + 1][agentLocation[1]].isSafe()){
            if(!map[agentLocation[0] + 1][agentLocation[1]].isVisited())
                flag = true;

            nextAgentLocation[0] = agentLocation[0] + 1;
            nextAgentLocation[1] = agentLocation[1];
        }
        if(!flag && agentLocation[1] - 1 >= 0 && map[agentLocation[0]][agentLocation[1] - 1].isSafe()){
            nextAgentLocation[0] = agentLocation[0];
            nextAgentLocation[1] = agentLocation[1] - 1;
        }

        return nextAgentLocation;
    }

}