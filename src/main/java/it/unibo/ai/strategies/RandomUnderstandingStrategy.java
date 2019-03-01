package it.unibo.ai.strategies;

import java.util.List;
import java.util.ArrayList;
import it.unibo.ai.beliefobjects.Understood;
import java.util.Random;

import org.nlogo.api.MersenneTwisterFast;

/**
 * @author Daniela Loreti
 * The strategy randomly selects a random number of sentences from a collection of believes.
 * At least one belies in the list is always understood.
 *
 */
public class RandomUnderstandingStrategy implements IUnderstandingStrategy{
	
	MersenneTwisterFast random;
	
	
	
	public RandomUnderstandingStrategy(MersenneTwisterFast random) {
		super();
		this.random = random;
	}

	@Override
	public List<Understood> selectiveUnderstand(List<String> sentenceIds) {
		List<Understood> outList = new ArrayList<>();
		if (sentenceIds.isEmpty()) {
			return outList;
		}
		//int outListSize = random.nextInt(sentenceIds.size()-1)+1;  //at least one belief is understood
		int outListSize = random.nextInt(sentenceIds.size());  
		if (outListSize==0) outListSize=1; //at least one belief is understood
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<String> sentenceIdsCopy = (List<String>) ((ArrayList) sentenceIds).clone();
		for (int i = 0; i < outListSize; i++) {
			outList.add(new Understood(sentenceIdsCopy.remove(random.nextInt(sentenceIdsCopy.size()))));

			//System.out.println("outList(i="+i+")="+outList.get(i));
		}
		
		return outList;
	}

	
}
