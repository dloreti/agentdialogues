package it.unibo.ai.strategies;

import java.util.List;
import java.util.ArrayList;
import it.unibo.ai.beliefobjects.Understood;

/**
 * @author Daniela Loreti
 * Strategy for DISCUSS or SILENT condition.
 * Selects only the first sentence in the given list.
 * i.e., the agent always understands only the first sentence that she is told.
 *
 */
public class FirstOneUnderstandingStrategy implements IUnderstandingStrategy{
	
	
	public FirstOneUnderstandingStrategy() {
		super();
	}

	@Override
	public List<Understood> selectiveUnderstand(List<String> sentenceIds) {
		List<Understood> outList = new ArrayList<Understood>();
		if (sentenceIds.size()>0) {
			outList.add(new Understood(sentenceIds.get(0)));
		}
		return outList;
	}

	
}
