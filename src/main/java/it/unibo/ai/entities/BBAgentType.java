package it.unibo.ai.entities;

public enum BBAgentType implements AgentType{
	X, //the agent that has the wrong intuitive answer (75% of the total agents)
	Y, //the agent that has the correct answer (20%)
	Z, //the agent that has the correct answer but did not really understand the problem (5%)
	K, //an agent who believes both a and b are correct
	W; //an agent who only knows that 10c is not a good answer (only knows c)
}