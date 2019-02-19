package it.unibo.ai.strategies;

import java.util.List;
import it.unibo.ai.beliefobjects.Belief;

/**
 * @author Daniela Loreti
 * The strategy to select a list of sentences to be uttered from a list of believes
 *
 */
public interface IUtteringStrategy {
	
	public List<String> selectiveUtter(List<Belief> believes);   
	public void insertBelief(Belief belief, List<Belief> believes);
	public List<String> getJustUttered();
}
