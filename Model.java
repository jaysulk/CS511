// Based upon https://github.com/sanjayr93/wumpus-ai-agents/tree/master/model-based-agent/wumpuslite
public class Model {

    private Cell[][] map;
    private int agentOrientation;
    private int[] agentLocation, nextAgentLocation;
    private int previousAction;

    public Model(){
        int worldSize = 4;
        map = new Cell[worldSize][worldSize];
        for(int i = 0; i < worldSize; i++){
            for(int j = 0; j < worldSize; j++){
                map[i][j] = new Cell();
            }
        }

        agentOrientation = Orientation.EAST;
        agentLocation = new int[]{map.length - 1, 0}; 
        map[agentLocation[0]][agentLocation[1]].setVisited(true);
        nextAgentLocation = new int[]{-1, -1};
        previousAction = Action.NO_OP; // Initialize previous action to NO_OP since we have done nothing yet.
    }
    
    public void updateModelOnAction(){
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
            case 5: // SHOOT
                break;
            case 6: // NO_OP
                break;
        }
    }

    public void updateModelOnPercept(boolean breeze, boolean stench, boolean bump, boolean scream, boolean glitter){
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
                            map[i][j].getFlags().put(Utils.wumpus, "");
                            if(!map[i][j].getFlags().get(Utils.pit).equals("X")){
                                map[i][j].setSafe(true);
                            }
                        }
                    }
                }
            }
            if(location[0] == -1) {
                Utils.setFlags(this, Utils.wumpus, "X", false);
            }else{
                if(agentLocation[0] + 1 < map.length && !map[agentLocation[0] + 1][agentLocation[1]].isSafe()
                        && (agentLocation[0] + 1 != location[0] && agentLocation[1] != location[1])){
                    if(!(map[agentLocation[0] + 1][agentLocation[1]].getFlags().get(Utils.pit).equals("X") ||
                            map[agentLocation[0] + 1][agentLocation[1]].getFlags().get(Utils.wumpus).equals("X")))
                        map[agentLocation[0] + 1][agentLocation[1]].setSafe(true);
                }
                if(agentLocation[0] - 1 >= 0 && !map[agentLocation[0] - 1][agentLocation[1]].isSafe() && (agentLocation[0] - 1 != location[0] && agentLocation[1] != location[1])) {
                    if(!(map[agentLocation[0] - 1][agentLocation[1]].getFlags().get(Utils.pit).equals("X") ||
                            map[agentLocation[0] - 1][agentLocation[1]].getFlags().get(Utils.wumpus).equals("X")))
                        map[agentLocation[0] - 1][agentLocation[1]].setSafe(true);
                }
                if(agentLocation[1] + 1 < map[0].length && !map[agentLocation[0]][agentLocation[1] + 1].isSafe() && (agentLocation[0] != location[0] && agentLocation[1] + 1 != location[1])){
                    if(!(map[agentLocation[0]][agentLocation[1] + 1].getFlags().get(Utils.pit).equals("X") ||
                            map[agentLocation[0]][agentLocation[1] + 1].getFlags().get(Utils.wumpus).equals("X")))
                        map[agentLocation[0]][agentLocation[1] + 1].setSafe(true);
                }
                if(agentLocation[1] - 1 >= 0 && !map[agentLocation[0]][agentLocation[1] - 1].isSafe() && (agentLocation[0] != location[0] && agentLocation[1] - 1 != location[1])){
                    if(!(map[agentLocation[0]][agentLocation[1] - 1].getFlags().get(Utils.pit).equals("X") ||
                            map[agentLocation[0]][agentLocation[1] + 1].getFlags().get(Utils.wumpus).equals("X")))
                        map[agentLocation[0]][agentLocation[1] - 1].setSafe(true);
                }
            }
        }
//        if(bump){
//            Utils.setFlags(this, Utils.wall, "X", false);
//        }
//        if(glitter){
//            Utils.setFlags(this, Utils.gold, "X", true);
//        }
        map[agentLocation[0]][agentLocation[1]].setSafe(true);
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
                }else{ //left
                    agentOrientation = Orientation.SOUTH;
                }
                break;
        }

    }

    private void updateAgentLocation() {
        //Update the agent location for the Forward action..
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

        if(!map[agentLocation[0]][agentLocation[1]].isVisited()){
            map[agentLocation[0]][agentLocation[1]].setVisited(true);
        }
    }

    public void resetNextAgentLocation() {
        nextAgentLocation[0] = -1;
        nextAgentLocation[1] = -1;
    }

    public int[] getAgentLocation() {
        return agentLocation;
    }

    public int[] getNextAgentLocation() {
        return nextAgentLocation;
    }

//    public void setAgentLocation(int[] agentLocation) {
//        this.agentLocation = agentLocation;
//    }

    public Cell[][] getWorld() {
        return map;
    }

//    public void setWorld(Cell[][] map) {
//        this.map = map;
//    }

    public int getAgentOrientation() {
        return agentOrientation;
    }

//    public void setAgentOrientation(int agentOrientation) {
//        this.agentOrientation = agentOrientation;
//    }
//
//    public int getPreviousAction() {
//        return previousAction;
//    }

    public void setPreviousAction(int previousAction) {
        this.previousAction = previousAction;
    }
}
