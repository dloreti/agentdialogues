package it.unibo.ai.strategies;

import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import it.unibo.ai.beliefobjects.Belief;
import it.unibo.ai.beliefobjects.IReply;

/**
 * @author Daniela Loreti
 * The strategy selects to utter a limited number (maxUtterablePerTurn) of sentences from a collection of agent's believes.
 *
 */
public class ReplayPreferredAllUtteringStrategy implements IUtteringStrategy{

	private int maxUtterablePerTurn;
	private Comparator<String> comparator;
	private List<String> handler;
	private MersenneTwisterFast random;

	//public ReplayPreferredAllUtteringStrategy(int maxUtterablePerTurn, Comparator<String> comparator,Random random){
	public ReplayPreferredAllUtteringStrategy(int maxUtterablePerTurn, Comparator<String> comparator,MersenneTwisterFast random){
		this.maxUtterablePerTurn = maxUtterablePerTurn;
		this.comparator = comparator;
		this.random =random;
		this.handler=new ArrayList<String>();
	}

	public String handlerListToString() {
		String s = "[";
		for (int i=0; i<handler.size();i++) {
			s=s+handler.get(i) + " ";
		}
		s=s+"]";
		return s;
	}

	@Override
	public void insertBelief(Belief belief, List<Belief> believes){
		if (!believes.contains(belief))
			believes.add(belief);
	}

	private void orderedInsertSentenceIntoHandler(String in) {
		List<String> handler_clone = new ArrayList<String>();
		for (String h : handler) {
			handler_clone.add(h);
		}
		int index=0;
		for (String s : handler_clone) {
			if (comparator.compare(in, s)<0) {
				handler.add(index, in);
				return;
			}
			index++;
		}
	}
	

	@Override
	public List<String> selectiveUtter(List<Belief> believes){
		Logger logger = LogManager.getLogger("agentdialogues");
		
		//create a list of all the utterable sentences in the input list of believes
		List<String> believeSentences = new ArrayList<String>();
		for (Belief belief : believes) {
			if(belief.isUtterable() )
				for (String s : belief.getSentences()) 
					if (!believeSentences.contains(s)) 
						believeSentences.add(s);
		}


		List<String> replay = new ArrayList<String>();
		if(random.nextBoolean()){
			for (Belief belief : believes) {
				if(belief instanceof IReply)
					replay.add(((IReply) belief).getBecause().getSentenceId());
			}
		}
		if (handler.size()==0) {
			handler=believeSentences;
			Collections.sort(handler, comparator);

			//if any replay is present in the list of utterable, then 
			//the correspondent because sentence should be uttered before other utterables
			if (!replay.isEmpty()) {
				for (String r : replay) {
					if (handler.remove(r)){
						handler.add(0, r);
					}
				}
			}

		}else {
			List<String> handler_clone = new ArrayList<String>();
			for (String h : handler) {
				handler_clone.add(h);
			}
			for (String h : handler_clone) {
				if (! believeSentences.contains(h))
					handler.remove(h);
			}
			for (String inputS : believeSentences) {
				if (! handler.contains(inputS))	{
					//					System.out.println("Inserting "+inputS);
					//					if(replay.contains(inputS)) //e' la prima volta che appare questa rebut (?)
					//						handler.add(0, inputS);
					//					else
					orderedInsertSentenceIntoHandler(inputS);
				}
			}

			//quando l'agente ha una rebut/undercut risponde random con la motivazione per cui non concorda con l'altro
			//oppure con la motivazione per cui la sua risposta è corretta.
			if (!replay.isEmpty() ) {   //&& maxUtterablePerTurn<handler.size()
				for (String r : replay) {
					Boolean just_uttered_r=false;
					int min = (maxUtterablePerTurn<handler.size() ? maxUtterablePerTurn : handler.size()) ;
					for(int i=0;i<min;i++) {
						if(handler.get(handler.size()-1-i).equals(r))
							just_uttered_r=true;
					}
					if ( (!just_uttered_r || (just_uttered_r && maxUtterablePerTurn>=handler.size()) ) && handler.remove(r)){
						logger.debug("REMOVING "+r+ " AND PLACING IT AT THE BEGINNING OF THE LIST");
						handler.add(0, r);
					}
				}
			}
		}

		logger.debug("Ordered list of utterables BEFORE uttering: "+handlerListToString());

		List<String> utters = new ArrayList<String>();  // List of things to say at this GT
		
		if (maxUtterablePerTurn>=handler.size()) {
			for (String h : handler) 
				utters.add(h);			
		}else {
			int i=0;
			while (i<maxUtterablePerTurn) {
				String s = handler.remove(0);
				utters.add(s);
				handler.add(s); //insert what uttered at the end of the list
				//System.out.println("i="+i+" handler becomes:"+handler);
				i++;
			}
			
		}
		/*int min = (maxUtterablePerTurn<handler.size() ? maxUtterablePerTurn : handler.size()) ;
		System.out.println("maxUtterablePerTurn="+maxUtterablePerTurn+" handler.size()="+handler.size()+" min="+min);
		while (i<min) {
			String s = handler.remove(0);
			utters.add(s);
			handler.add(s); //insert what uttered at the end of the list
			System.out.println("i="+i+" handler becomes:"+handler);
			i++;
		}*/
		
		logger.debug("Ordered list of utterables AFTER uttering: "+handlerListToString());

		return utters;
	}

	@Override
	public List<String> getJustUttered() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
