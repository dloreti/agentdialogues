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
			List<String> rulefiles, ReasoningMode reasoningMode,Filter filter, ObjectSolver solver
			) {
		super();
		this.name = name;
		this.agentBeliefCollection = agentBeliefCollection;
		this.pb = new ProgramBuilder<Object>();
		rulefiles.forEach(x -> pb.add(new File(x)));
		this.reasoningMode = reasoningMode;
		this.filter = filter;
		this.solver = solver;
	}

	public List<String> selectiveUtter(){
		return agentBeliefCollection.selectiveUtter();
	}

	public List<Understood> selectiveUnderstand(List<String> utteredSentences) throws SolverException{
		List<Understood> undList = agentBeliefCollection.selectiveUnderstand(utteredSentences);
		think(undList);
		return undList;
	}

	private void think(List<Understood> understoods) throws SolverException{
		//pb should already contain only acc and und. No need to clear it. 
		//The received und becomes a fact and is added to previous acc and und facts to compute novel terms.
		for (Belief b : understoods) {
			pb.add(b);
		}
		
		//pb.build().getInput().forEach(x -> {System.out.println(((Belief)x));});
		
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
		//we clear the belief collection although it only contains some facts (acc and und) which must be still present in the consequence set just computed.
		agentBeliefCollection.clearBelieves(); 
		for (Object object : consequence) {
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
