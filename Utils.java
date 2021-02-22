// Based upon https://github.com/sanjayr93/wumpus-ai-agents/tree/master/model-based-agent/wumpuslite
// Tweaked, updated, and fixed

import java.util.HashMap;

public class Utils {

    public static String pit = "pit";
    public static String wumpus = "wumpus";
//    public static String gold = "gold";
//    public static String wall = "wall";

    public static boolean isNeighbor(int[] agentLocation, int row, int col) {
        if((agentLocation[0] + 1 == row && agentLocation[1] == col) || (agentLocation[0] - 1 == row && agentLocation[1] == col) ||
                (agentLocation[0] == row && agentLocation[1] + 1 == col) || (agentLocation[0]== row && agentLocation[1] - 1 == col))
            return true;
        return false;
    }

    public static void resolvePercept(Model model, int row, int col, TransferPercept tp) {

        HashMap<String, String> flags = model.map[row][col].getFlags();

        if(model.map[row][col].safe)
            return;

        if(flags.get(pit).equals("X") &&
                flags.get(wumpus).equals("X")) {
            if (!(tp.getBreeze() || tp.getStench())) {
                model.map[row][col].safe = true;
                flags.put(pit, "");
                flags.put(wumpus, "");
            }
        }

        if(flags.get(pit).equals("X") && !flags.get(wumpus).equals("X")){
            if(!tp.getBreeze()){
                model.map[row][col].safe = true;
                flags.put(pit, "");
            }
        }

        if(!flags.get(pit).equals("X") && flags.get(wumpus).equals("X")){
            if(!tp.getStench()){
                model.map[row][col].safe = true;
                flags.put(wumpus, "");
            }
        }
    }

    public static boolean isSafe(Model model, int[] location) {

        switch (model.agentOrientation){
            case 0:
                return model.agentLocation[0] - 1 == location[0] && model.agentLocation[1] == location[1];
            case 1:
                return model.agentLocation[0] == location[0] && model.agentLocation[1] + 1 == location[1];
            case 2:
                return model.agentLocation[0] + 1 == location[0] && model.agentLocation[1] == location[1];
            case 3:
                return model.agentLocation[0] == location[0] && model.agentLocation[1] - 1 == location[1];
        }
        return false;
    }

    public static void setFlags(Model model, String obj, String flag, boolean safe){
        Grid[][] map = model.map;
        int[] agentLocation = model.agentLocation;
        
        if(agentLocation[0] + 1 < map.length && !map[agentLocation[0] + 1][agentLocation[1]].safe){
            if(safe) {
                map[agentLocation[0] + 1][agentLocation[1]].safe = safe;
                map[agentLocation[0] + 1][agentLocation[1]].getFlags().put(wumpus, "");
                map[agentLocation[0] + 1][agentLocation[1]].getFlags().put(pit, "");
            }else
                map[agentLocation[0] + 1][agentLocation[1]].updateFlags(obj, flag);
        }
        if(agentLocation[0] - 1 >= 0 && !map[agentLocation[0] - 1][agentLocation[1]].safe) {
            if(safe) {
                map[agentLocation[0] - 1][agentLocation[1]].safe = safe;
                map[agentLocation[0] - 1][agentLocation[1]].getFlags().put(wumpus, "");
                map[agentLocation[0] - 1][agentLocation[1]].getFlags().put(pit, "");
            }else
                map[agentLocation[0] - 1][agentLocation[1]].updateFlags(obj, flag);
        }
        if(agentLocation[1] + 1 < map[0].length && !map[agentLocation[0]][agentLocation[1] + 1].safe){
            if(safe) {
                map[agentLocation[0]][agentLocation[1] + 1].safe = safe;
                map[agentLocation[0]][agentLocation[1] + 1].getFlags().put(wumpus, "");
                map[agentLocation[0]][agentLocation[1] + 1].getFlags().put(pit, "");
            }else
                map[agentLocation[0]][agentLocation[1] + 1].updateFlags(obj, flag);
        }
        if(agentLocation[1] - 1 >= 0 && !map[agentLocation[0]][agentLocation[1] - 1].safe){
            if(safe) {
                map[agentLocation[0]][agentLocation[1] - 1].safe = safe;
                map[agentLocation[0]][agentLocation[1] - 1].getFlags().put(wumpus, "");
                map[agentLocation[0]][agentLocation[1] - 1].getFlags().put(pit, "");
            }else
                map[agentLocation[0]][agentLocation[1] - 1].updateFlags(obj, flag);
        }
    }

    public static int[] getNextSafeCell(Model model) {

        Grid[][] map = model.map;
        int[] agentLocation = model.agentLocation;
        int[] nextAgentLocation = model.nextAgentLocation;
        
        if(nextAgentLocation[0] != -1){
            return nextAgentLocation;
        }

        boolean flag = false;

        if(agentLocation[1] + 1 < map[0].length && map[agentLocation[0]][agentLocation[1] + 1].safe){
            if(!map[agentLocation[0]][agentLocation[1] + 1].visited)
                flag = true;

            nextAgentLocation[0] = agentLocation[0];
            nextAgentLocation[1] = agentLocation[1] + 1;
        }
        if(!flag && agentLocation[0] - 1 >= 0 && map[agentLocation[0] - 1][agentLocation[1]].safe) {
            if(!map[agentLocation[0] - 1][agentLocation[1]].visited)
                flag = true;

            nextAgentLocation[0] = agentLocation[0] - 1;
            nextAgentLocation[1] = agentLocation[1];
        }
        if(!flag && agentLocation[0] + 1 < map.length && map[agentLocation[0] + 1][agentLocation[1]].safe){
            if(!map[agentLocation[0] + 1][agentLocation[1]].visited)
                flag = true;

            nextAgentLocation[0] = agentLocation[0] + 1;
            nextAgentLocation[1] = agentLocation[1];
        }
        if(!flag && agentLocation[1] - 1 >= 0 && map[agentLocation[0]][agentLocation[1] - 1].safe){
            nextAgentLocation[0] = agentLocation[0];
            nextAgentLocation[1] = agentLocation[1] - 1;
        }

        return nextAgentLocation;
    }

}