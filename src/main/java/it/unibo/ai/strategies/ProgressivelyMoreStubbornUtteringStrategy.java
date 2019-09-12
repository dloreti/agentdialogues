package it.unibo.ai.strategies;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nlogo.api.MersenneTwisterFast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import it.unibo.ai.beliefobjects.Belief;

/**
 * @author Daniela Loreti
 * The strategy selects to utter a limited number (maxUtterablePerTurn) of sentences from a collection of agent's believes.
 *
 */
public class ProgressivelyMoreStubbornUtteringStrategy implements IUtteringStrategy{

	private List<String> answers;
	private MersenneTwisterFast random;

	private String last_uttered;
	private int counter;


	public ProgressivelyMoreStubbornUtteringStrategy(List<String> answers, String last_uttered, int counter, MersenneTwisterFast random){
		this.answers = answers;
		this.random=random;
		this.last_uttered=last_uttered; // should be null at the first interaction
		this.counter=counter;			// should be 0 at the first interaction
	}



	@Override
	public void insertBelief(Belief belief, List<Belief> believes){
		if (!believes.contains(belief))
			believes.add(belief);
	}

	@Override
	public List<String> selectiveUtter(List<Belief> believes){
		Logger logger = LogManager.getLogger("agentdialogues");
		counter++; //counter is increased each time I'm requested to provide an answer
		List<String> utters = new ArrayList<String>();

		for (Belief belief : believes) {
			if(belief.isUtterable() )
				for (String s : belief.getSentences()) {
					if (!utters.contains(s) && answers.contains(s)){
						utters.add(s);
					}
				}
		}
		//Collections.sort(utters, comparator);
		if (utters.size()==0) {
			last_uttered=null;

		}else if (utters.size()==1) {
			last_uttered=utters.get(0);

		}else if (utters.size()==2){ 
			// at the moment we suppose only two possible answers: last_uttered and otherAnswer
			String otherAnswer;
			if (last_uttered == null) {
				last_uttered = random.nextBoolean() ? utters.get(0) : utters.get(1);
			}else {
				if (last_uttered.equals(utters.get(0))) 
					otherAnswer=utters.get(1);
				else 
					otherAnswer=utters.get(0);
				//System.out.println("++++ utters.size()="+utters.size()+" last_uttered="+last_uttered+" otherAnswer="+otherAnswer);
				Boolean changeAnswer = false;
				for (int i = 0; i < counter; i++) {
					changeAnswer = random.nextBoolean(); 
					if (!changeAnswer) break;
				}

				if (changeAnswer) {  
					//at each interaction I change my answer only if the coin has given true for "counter" times 
					//System.out.println("CAMBIO IDEA. PRIMA CREDEVO "+last_uttered+" ORA CREDO "+otherAnswer);
					last_uttered=otherAnswer;
				}
			}
		}else {
			try {
				throw new Exception("Problems with more than 2 possible answers are not modelled yet.");
			} catch (Exception e) {
				logger.error("",e);
			}
		}
		return Arrays.asList(last_uttered); 
	}



	/*public String getLastUttered() {
		return last_uttered;
	}*/

	public int getInteractionCounter() {
		return counter;
	}



	@Override
	public List<String> getJustUttered() {
		List<String> lu = new ArrayList<>();
		lu.add(last_uttered);
		return lu;
	}
}
