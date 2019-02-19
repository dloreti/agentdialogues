package it.unibo.ai.strategies;

import java.util.List;
import it.unibo.ai.beliefobjects.Understood;

/**
 * @author Daniela Loreti
 * The strategy to select a list of sentences to be understood from a list of sentence ids 
 *
 */
public interface IUnderstandingStrategy {
	
	
	/**
	 * @param sentenceIds
	 * @return A list of @Understood elements
	 */
	public List<Understood> selectiveUnderstand(List<String> sentenceIds);   
	
}
