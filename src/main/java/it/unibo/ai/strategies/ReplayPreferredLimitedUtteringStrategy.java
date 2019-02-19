package it.unibo.ai.strategies;

import java.util.List;
import java.util.ArrayList;
import it.unibo.ai.beliefobjects.Belief;
import it.unibo.ai.beliefobjects.IReply;

/**
 * @author Daniela Loreti
 * The strategy selects a limited number of sentences from a collection of agent's believes, 
 * always preferring the sentences in the Reply believes (i.e. the sentences in a rebut or undercut if they are present).
 * NB: This strategy still needs to be tested!!
 *
 */
@Deprecated
public class ReplayPreferredLimitedUtteringStrategy implements IUtteringStrategy{

	private int maxUtterablePerTurn;

	public ReplayPreferredLimitedUtteringStrategy(int maxUtterablePerTurn){
		this.maxUtterablePerTurn = maxUtterablePerTurn;
	}

	@Override
	public void insertBelief(Belief belief, List<Belief> believes){
		if (!believes.contains(belief)){
			if (belief instanceof IReply){
				believes.add(0,belief);  //if a replay put on top of the list
			}else{
				if (!belief.isUtterable()){
					believes.add(belief); //if not utterable put at the end
				}else{
					int i=0;
					for (Belief b : believes) {  //if utterable put on top of the utterables
						if (b.isUtterable() ){
							believes.add(i,belief);
							break;
						}
						i++;
					}
				}
			}
		}
	}

	@Override
	public List<String> selectiveUtter(List<Belief> believes){
		List<String> utters = new ArrayList<String>();
		
		for (Belief belief : believes) {
			if (belief instanceof IReply && !utters.contains(((IReply) belief).getReason()) && utters.size()<maxUtterablePerTurn )
				utters.add(((IReply) belief).getReason());
			else if(belief.isUtterable() ){
				for (String sentenceId : belief.getSentences()) {  //actually only one 
					if (!utters.contains(sentenceId) && utters.size()<maxUtterablePerTurn){
						utters.add(sentenceId);
					}
				}
			}else{
				break;  //if there are no other utterables, then you can stop looking for something to say
			}
			if (utters.size()>=maxUtterablePerTurn)
				break;
		}
		return utters;
	}

	@Override
	public List<String> getJustUttered() {
		// TODO Auto-generated method stub
		return null;
	}


}
