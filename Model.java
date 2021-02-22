// Based upon https://github.com/sanjayr93/wumpus-ai-agents/tree/master/model-based-agent/wumpuslite
// Tweaked, updated, and fixed

public class Model {

    public Grid[][] map;
    public int agentOrientation;
    public int[] agentLocation, nextAgentLocation;
    public int previousAction;

    public Model(){
        int worldSize = 4;
        map = new Grid[worldSize][worldSize];
        for(int i = 0; i < worldSize; i++){
            for(int j = 0; j < worldSize; j++){
                map[i][j] = new Grid();
            }
        }

        agentOrientation = Orientation.EAST;
        agentLocation = new int[]{map.length - 1, 0}; 
        map[agentLocation[0]][agentLocation[1]].visited = true;
        nextAgentLocation = new int[]{-1, -1};
        previousAction = Action.NO_OP; // Initialize previous action to NO_OP since we have done nothing yet.
    }
    
    public void updateOnAction(){
        switch (previousAction){
            case 1: // GO_FORWARD
                updateAgentLocation();
                break;
            case 2: // TURN_RIGHT
                updateAgentOrientation(0);
                break;
            case 3: // TURN_LEFT
                updateAgentOrientation(1);
                break;
            case 4: // GRAB
                break;
        }
    }

    public void updateOnPercept(boolean breeze, boolean stench, boolean glitter){
        if(breeze){
            Utils.setFlags(this, Utils.pit, "X", false);
        }
        if(stench) {
            int worldSize = 4;
            int[] location = new int[]{-1, -1};
            for(int i = 0; i < worldSize; i++){
                for(int j = 0; j < worldSize; j++){
                    if(map[i][j].getFlags().get(Utils.wumpus).equals("X")){
                        if(Utils.isNeighbor(agentLocation, i , j)){
                            location[0] = i;
                            location[1] = j;
                        }else{
                            map[i][j].getFlags().put(Utils.wumpus, "O");
                            if(!map[i][j].getFlags().get(Utils.pit).equals("X")){
                                map[i][j].safe = true;
                            }
                        }
                    }
                }
            }
            if(location[0] == -1) {
                Utils.setFlags(this, Utils.wumpus, "X", false);
            }else{
                if(agentLocation[0] + 1 < map.length && !map[agentLocation[0] + 1][agentLocation[1]].safe
                        && (agentLocation[0] + 1 != location[0] && agentLocation[1] != location[1])){
                    if(!(map[agentLocation[0] + 1][agentLocation[1]].getFlags().get(Utils.pit).equals("X") ||
                            map[agentLocation[0] + 1][agentLocation[1]].getFlags().get(Utils.wumpus).equals("X")))
                        map[agentLocation[0] + 1][agentLocation[1]].safe = true;
                }
                if(agentLocation[0] - 1 >= 0 && !map[agentLocation[0] - 1][agentLocation[1]].safe && (agentLocation[0] - 1 != location[0] && agentLocation[1] != location[1])) {
                    if(!(map[agentLocation[0] - 1][agentLocation[1]].getFlags().get(Utils.pit).equals("X") ||
                            map[agentLocation[0] - 1][agentLocation[1]].getFlags().get(Utils.wumpus).equals("X")))
                        map[agentLocation[0] - 1][agentLocation[1]].safe = true;
                }
                if(agentLocation[1] + 1 < map[0].length && !map[agentLocation[0]][agentLocation[1] + 1].safe && (agentLocation[0] != location[0] && agentLocation[1] + 1 != location[1])){
                    if(!(map[agentLocation[0]][agentLocation[1] + 1].getFlags().get(Utils.pit).equals("X") ||
                            map[agentLocation[0]][agentLocation[1] + 1].getFlags().get(Utils.wumpus).equals("X")))
                        map[agentLocation[0]][agentLocation[1] + 1].safe = true;
                }
                if(agentLocation[1] - 1 >= 0 && !map[agentLocation[0]][agentLocation[1] - 1].safe && (agentLocation[0] != location[0] && agentLocation[1] - 1 != location[1])){
                    if(!(map[agentLocation[0]][agentLocation[1] - 1].getFlags().get(Utils.pit).equals("X") ||
                            map[agentLocation[0]][agentLocation[1] + 1].getFlags().get(Utils.wumpus).equals("X")))
                        map[agentLocation[0]][agentLocation[1] - 1].safe = true;
                }
            }
        }
//        if(glitter){
//            Utils.setFlags(this, Utils.gold, "X", true);
//        }
        map[agentLocation[0]][agentLocation[1]].safe = true;
        if(!breeze && !stench)
            Utils.setFlags(this, null, null, true);
    }

    private void updateAgentOrientation(int direction) {
        switch (agentOrientation){
            case 0:
                if(direction == 0){ 
                    agentOrientation = Orientation.EAST;
                }else{ 
                    agentOrientation = Orientation.WEST;
                }
                break;
            case 1:
                if(direction == 0){ 
                    agentOrientation = Orientation.SOUTH;
                }else{ 
                    agentOrientation = Orientation.NORTH;
                }
                break;
            case 2:
                if(direction == 0){ 
                    agentOrientation = Orientation.WEST;
                }else{ 
                    agentOrientation = Orientation.EAST;
                }
                break;
            case 3:
                if(direction == 0){ 
                    agentOrientation = Orientation.NORTH;
                }else{ 
                    agentOrientation = Orientation.SOUTH;
                }
                break;
        }

    }
    
    private void updateAgentLocation() {

        switch (agentOrientation){
            case 0:
                if(agentLocation[0] - 1 >= 0) 
                    agentLocation[0]--;
                break;
            case 1:
                if(agentLocation[1] + 1 < map[0].length)
                    agentLocation[1]++;
                break;
            case 2:
                if(agentLocation[0] + 1 < map.length) 
                    agentLocation[0]++;
                break;
            case 3:
                if(agentLocation[1] - 1 >= 0)
                    agentLocation[1]--;
                break;
        }

        if(!map[agentLocation[0]][agentLocation[1]].visited){
            map[agentLocation[0]][agentLocation[1]].visited = true;
        }
    }
    
}
