package it.unibo.ai.entities;

public enum LindaAgentType implements AgentType{
	X, //the agent that has the wrong intuitive answer (75% of the total agents)
	Y, //the agent that has the correct answer (20%)
	Z, //the agent that has the correct answer but did not really understand the problem (5%)
	//W; //an agent that knows (accessible) both a and b, but does not know what to believe. claims=[]
	K; //an agent that believes both a and b. This can happen if neither a nor b have been derived. They must have been understood from dialogue without an explanation.
}