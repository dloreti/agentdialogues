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
 * Strategy for SILENT condition.
 * When the agent has one answer, she states that.
 * When she has more than one, her answer is influenced by neighbourhood.
 *
 */
public class PeerPressureUtteringStrategy implements IUtteringStrategy{

	private List<String> answers;
	private MersenneTwisterFast random;

	private String last_uttered;
	private int counter;
	
	private INeighbourhoodHandler neighbourhoodHandler;


	public PeerPressureUtteringStrategy(List<String> answers, String last_uttered, int counter, MersenneTwisterFast random, INeighbourhoodHandler neighbourhoodHandler){
		this.answers = answers;
		this.random=random;
		this.last_uttered=last_uttered; // should be null at the first interaction
		this.counter=counter;			// should be 0 at the first interaction
		this.neighbourhoodHandler=neighbourhoodHandler;
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
			// at the moment we suppose only two possible answers: asw1 and asw2
			int nAns0=0,nAns1=0;
			
			List<String> neighAsws = neighbourhoodHandler.whatOtherThink();
			if (last_uttered == null && neighAsws.size()==0) {
				last_uttered = random.nextBoolean() ? utters.get(0) : utters.get(1);
			}else {
				for (String str : neighAsws) {
					if (str.equals(utters.get(0)) ) nAns0++;
					else if (str.equals(utters.get(1)) ) nAns1++;
				}
				if (last_uttered.equals(utters.get(0))) nAns0++; //nAns0+=neighAsws.size();//
				else if (last_uttered.equals(utters.get(1)) ) nAns1++; //nAns1+=neighAsws.size();
				
				if (nAns0>nAns1)  last_uttered = utters.get(0);
				else if (nAns0<nAns1) last_uttered = utters.get(1);
				//else if the number of a and b is the same, I stay with my last answer
				logger.debug("nAns0("+utters.get(0)+")="+nAns0+" nAns1("+utters.get(1)+")="+nAns1+" so I say:"+last_uttered);
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


	public int getInteractionCounter() {
		return counter;
	}



	@Override
	public List<String> getJustUttered() {
		return Arrays.asList(last_uttered); 
	}
}
