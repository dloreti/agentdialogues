package it.unibo.ai.entities;

import java.util.ArrayList;
import java.util.List;

import it.unibo.ai.beliefobjects.Accessible;
import it.unibo.ai.beliefobjects.Belief;
import it.unibo.ai.beliefobjects.Understood;
import it.unibo.ai.strategies.IUnderstandingStrategy;
import it.unibo.ai.strategies.IUtteringStrategy;

public class AgentBeliefCollection {

	private List<Belief> believes;
	private IUtteringStrategy utteringStrategy;
	private IUnderstandingStrategy undestandingStrategy;
	//private List<String> justUttered;

	

	public AgentBeliefCollection(IUtteringStrategy utteringStrategy, IUnderstandingStrategy undestandingStrategy ) {
		super();
		this.setBelieves(new ArrayList<Belief>());
		this.utteringStrategy = utteringStrategy;
		this.undestandingStrategy = undestandingStrategy;
		//this.setJustUttered(new ArrayList<String>());
	}

	public AgentBeliefCollection(List<Belief> believes, IUtteringStrategy utteringStrategy, IUnderstandingStrategy undestandingStrategy ) {
		super();
		this.setBelieves(believes);
		this.utteringStrategy = utteringStrategy;
		this.undestandingStrategy = undestandingStrategy;
		//this.setJustUttered(new ArrayList<String>());
	}


	protected void resetToAccessibleAndUndestood(){
		List<Belief> believescopy =new ArrayList<Belief>() ;
		for (Belief belief : believes) {
			if (belief instanceof Accessible || belief instanceof Understood )
				believescopy.add(belief);
		}
		this.setBelieves(believescopy);
	}

	public void insertBelief(Belief belief){
		utteringStrategy.insertBelief(belief, getBelieves());
	}


	public List<String> selectiveUtter(){
		//this.setJustUttered(utteringStrategy.selectiveUtter(getBelieves()));
		//return justUttered;
		return utteringStrategy.selectiveUtter(getBelieves());
	}

	public List<Understood> selectiveUnderstand(List<String> sentenceIds){
		List<Understood> newUnderstoods = undestandingStrategy.selectiveUnderstand(sentenceIds);
		//quanto segue è fatto successivamente anche da agent.think(List<Understood> understoods)
		//si può portare qui tutto il codice della agent.think(List<Understood> understoods) e tenere solo una operazione di agent.think() CON clearBelieves!!
		for (Understood understood : newUnderstoods) { 
			this.insertBelief(understood);
		}
		return newUnderstoods;
	}


	public List<Belief> getBelieves() {
		return believes;
	}
	
	protected List<Belief> getUnderstoods() {
		List<Belief> r = new ArrayList<Belief>();
		for (Belief b : getBelieves()) {
			if (b.getClass().equals(Understood.class)){
				r.add(b);
			}
		}
		return r;
	}


	protected void clearBelieves(){
		this.setBelieves(new ArrayList<Belief>());
	}
	
	private void setBelieves(List<Belief> believes) {
		this.believes = believes;
	}


	@Override
	public String toString() {
		return "believes=" + believes ;
	}


	public String exaustiveToString(ProblemSentences ps) {
		String c = "believes = {\n";
		for (Belief b : believes) {
			c = c+"\n"+b.exaustiveToString(ps)+"\n";
		}
		return c+ "}";
	}

	public IUtteringStrategy getUtteringStrategy() {
		return utteringStrategy;
	}



	public IUnderstandingStrategy getUndestandingStrategy() {
		return undestandingStrategy;
	}

	public List<String> getJustUtt() {
		return this.utteringStrategy.getJustUttered();
	}

	/*public List<String> getJustUttered() {
		return justUttered;
	}

	public void setJustUttered(List<String> justUttered) {
		this.justUttered = justUttered;
	}*/
}
