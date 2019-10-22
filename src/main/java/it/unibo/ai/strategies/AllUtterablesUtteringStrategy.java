package it.unibo.ai.strategies;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import it.unibo.ai.beliefobjects.Belief;

/**
 * @author Daniela Loreti
 * Strategy for DISCUSS condition.
 * The strategy selects to utter all (up to maxUtterablePerTurn) sentences from a collection of agent's believes.
 *
 */
public class AllUtterablesUtteringStrategy implements IUtteringStrategy{

	private int maxUtterablePerTurn;
	private Comparator<String> comparator;

	public AllUtterablesUtteringStrategy(int maxUtterablePerTurn, Comparator<String> comparator){
		this.maxUtterablePerTurn = maxUtterablePerTurn;
		this.comparator = comparator;
	}



	@Override
	public void insertBelief(Belief belief, List<Belief> believes){
		if (!believes.contains(belief))
			believes.add(belief);
	}

	@Override
	public List<String> selectiveUtter(List<Belief> believes){
		List<String> utters = new ArrayList<String>();
		int i=0;
		for (Belief belief : believes) {
			if(belief.isUtterable() && i<maxUtterablePerTurn )
				//				utters.addAll(belief.getSentences());
				for (String s : belief.getSentences()) {
					if (!utters.contains(s)){
						utters.add(s);
						i++;
					}
				}
		}

		Collections.sort(utters, comparator);

		return utters;
	}



	@Override
	public List<String> getJustUttered() {
		// TODO Auto-generated method stub
		return null;
	}




}
