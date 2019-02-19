package it.unibo.ai.entities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import asp4j.program.ProgramBuilder;
import asp4j.solver.ReasoningMode;
import asp4j.solver.SolverException;
import asp4j.solver.object.Filter;
import asp4j.solver.object.ObjectSolver;
import it.unibo.ai.beliefobjects.Belief;
import it.unibo.ai.beliefobjects.Believed;
import it.unibo.ai.beliefobjects.Understood;


public class Agent {
	private String name;
	private AgentBeliefCollection agentBeliefCollection; 
	private ProgramBuilder<Object> pb;
	private ReasoningMode reasoningMode;
	private Filter filter;
	private ObjectSolver solver;
	
	public Agent(String name, AgentBeliefCollection agentBeliefCollection ,
			String rulefile, ReasoningMode reasoningMode,Filter filter, ObjectSolver solver
			) {
		super();
		this.name = name;
		this.agentBeliefCollection = agentBeliefCollection;
		this.pb = new ProgramBuilder<Object>().add(new File(rulefile));
		this.reasoningMode = reasoningMode;
		this.filter = filter;
		this.solver = solver;
	}

	public List<String> selectiveUtter(){
		return agentBeliefCollection.selectiveUtter();
	}

	public List<Understood> selectiveUnderstand(List<String> utteredSentences) throws SolverException{
		List<Understood> undList = agentBeliefCollection.selectiveUnderstand(utteredSentences);
		//forse dovrebbe subito pensare...
		think(undList);
		return undList;
	}

	private void think(List<Understood> understoods) throws SolverException{
		
		
		for (Belief b : understoods) {
			pb.add(b);
		}
		//System.out.println("Agent "+this.name+": solver="+solver+" pb="+pb+" reasoningMode="+reasoningMode+" filter="+filter);
		Set<Object> consequence = solver.getConsequence(pb.build(), reasoningMode, filter);
		agentBeliefCollection.clearBelieves(); //otherwise the java Agent could still have some bel(x) even if the solver has eliminated it.
		for (Object object : consequence) {
			agentBeliefCollection.insertBelief((Belief)object);
		}
	}
	
	public void think() throws SolverException{
		this.agentBeliefCollection.resetToAccessibleAndUndestood();
		for (Belief b : agentBeliefCollection.getBelieves()) {
			pb.add(b);
		}
		Set<Object> consequence = solver.getConsequence(pb.build(), reasoningMode, filter);
		//agentBeliefCollection.clearBelieves(); //otherwise the java Agent could still have some bel(x) even if the solver has eliminated it. //probably not needed
		for (Object object : consequence) {
			//System.out.println("Inserting:"+(Belief)object);
			agentBeliefCollection.insertBelief((Belief)object);
		}
	}
	
	public String getName() {
		return name;
	}

	public AgentBeliefCollection getBeliefCollection() {
		return agentBeliefCollection;
	}

	@Override
	public String toString() {
		return "Agent [" + name + ", " + agentBeliefCollection + "]";
	}

	public String exaustiveToString(ProblemSentences ps){
		return "Agent [" + name + ", " + agentBeliefCollection.exaustiveToString(ps) + "]";
	}

	public List<String> getJustUttered() {
		return this.agentBeliefCollection.getJustUtt();
	}

	/*public List<String> getClaimNL(){
		List<Belief> believes = getBeliefCollection().getBelieves();
		List<String> claims = new ArrayList<>();
		for (Belief belief : believes) {
			if (belief instanceof Believed) {
				claims.add(((Believed) belief).getSentenceId());
			}
		}
		return claims;
	}*/


}
