package it.unibo.ai.strategies;

import java.util.List;
import java.util.ArrayList;
import it.unibo.ai.beliefobjects.Understood;

/**
 * @author Daniela Loreti
 * The understanding strategy selects a all the believes in the list. The agent always understands everything he is told.
 *
 */
public class AllUnderstandingStrategy implements IUnderstandingStrategy{
	
	
	public AllUnderstandingStrategy() {
		super();
	}

	@Override
	public List<Understood> selectiveUnderstand(List<String> sentenceIds) {
		List<Understood> outList = new ArrayList<Understood>();
		for (int i = 0; i < sentenceIds.size(); i++) {
			outList.add(new Understood(sentenceIds.get(i)));
		}
		return outList;
	}

	
}
