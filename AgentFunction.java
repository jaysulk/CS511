/*
 * Class that defines the agent function.
 *
 * Written by James P. Biagioni (jbiagi1@uic.edu)
 * for CS511 Artificial Intelligence II
 * at The University of Illinois at Chicago
 *
 * Last modified 2/19/07
 *
 * DISCLAIMER:
 * Elements of this application were borrowed from
 * the client-server implementation of the Wumpus
 * World Simulator written by Kruti Mehta at
 * The University of Texas at Arlington.
 *
 */

import java.util.Random;

class AgentFunction {

	// string to store the agent's name
	// do not remove this variable
	private String agentName = "Heisenberg";

	// all of these variables are created and used
	// for illustration purposes; you may delete them
 	// when implementing your own intelligent agent
	private int[] actionTable;
	private boolean bump;
	private boolean glitter;
	private boolean breeze;
	private boolean stench;
	private boolean scream;
	private Random rand;

	private Model model;

	public AgentFunction() {
		// for illustration purposes; you may delete all code
		// inside this constructor when implementing your
		// own intelligent agent

		// this integer array will store the agent actions
		actionTable = new int[8];

		actionTable[0] = Action.GO_FORWARD;
		actionTable[1] = Action.GO_FORWARD;
		actionTable[2] = Action.GO_FORWARD;
		actionTable[3] = Action.GO_FORWARD;
		actionTable[4] = Action.TURN_RIGHT;
		actionTable[5] = Action.TURN_LEFT;
		actionTable[6] = Action.GRAB;
		actionTable[7] = Action.SHOOT;

		// new random number generator, for
		// randomly picking actions to execute
		rand = new Random();
		model = new Model();
	}
	
	public int process(TransferPercept tp) {
		// To build your own intelligent agent, replace
		// all code below this comment block. You have
		// access to all percepts through the object
		// 'tp' as illustrated here:

		// read in the current percepts
		bump = tp.getBump();
		glitter = tp.getGlitter();
		breeze = tp.getBreeze();
		stench = tp.getStench();
		scream = tp.getScream();

		if(glitter){
			return Action.GRAB;
		}

		//updating the model based on last action
		model.updateModelOnAction();
		//updating the model based on current percepts
		model.updateModelOnPercept(breeze, stench, bump, scream, glitter);

		//Condition - Action Rules
		if (bump) {
			double probability = Math.random();
			if(probability < .5)
			{
				model.setPreviousAction(Action.TURN_RIGHT);
				return Action.TURN_RIGHT;
			}
			else
			{
				model.setPreviousAction(Action.TURN_LEFT);
				return Action.TURN_LEFT;
			}
		}

		int[] agentLocation = model.getAgentLocation();

		if(agentLocation[1] + 1 < model.getWorld()[0].length){
			Utils.resolve(model, agentLocation[0], agentLocation[1] + 1, tp);
		}

		if(agentLocation[1] - 1 >= 0){
			Utils.resolve(model, agentLocation[0], agentLocation[1] - 1, tp);
		}

		if(agentLocation[0] - 1 >= 0){
			Utils.resolve(model, agentLocation[0] - 1, agentLocation[1], tp);
		}

		if(agentLocation[0] + 1 < model.getWorld().length){
			Utils.resolve(model, agentLocation[0] + 1, agentLocation[1], tp);
		}

		int[] location = Utils.getNextSafeCell(model);

		if (Utils.check(model, location)) {
			model.resetNextAgentLocation() ;
			model.setPreviousAction(Action.GO_FORWARD);
			return Action.GO_FORWARD;
		}

		double probability = Math.random();
		if(probability < .5)
		{
			model.setPreviousAction(Action.TURN_RIGHT);
			return Action.TURN_RIGHT;
		}
		else
		{
			model.setPreviousAction(Action.TURN_LEFT);
			return Action.TURN_LEFT;
		}
	}

	// public method to return the agent's name
	// do not remove this method
	public String getAgentName() {
		return agentName;
	}
}