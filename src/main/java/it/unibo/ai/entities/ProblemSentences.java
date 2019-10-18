package it.unibo.ai.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import it.unibo.ai.beliefobjects.Belief;
import it.unibo.ai.beliefobjects.Believed;

public abstract class ProblemSentences {
	
	
	public enum ORDERING { ARGUMENT_FIRST, CONCLUSION_FIRST };
	protected List<String> ORDERED_ENTRIES;

	private HashMap<String,Sentence> sentences = new HashMap<>();
	private Comparator<String> sentenceComparator;
	
	protected List<String> possibleAnswers;
	
	public ProblemSentences() {
		this.setSentenceComparator(new Comparator<String>(){
			@Override
			public int compare(String o1, String o2) {
				if (ORDERED_ENTRIES.contains(o1) && ORDERED_ENTRIES.contains(o2)) 
					// Both objects are in our ordered list. Compare them by their position in the list
					return ORDERED_ENTRIES.indexOf(o1) - ORDERED_ENTRIES.indexOf(o2);
				if (ORDERED_ENTRIES.contains(o1)) // o1 is in the ordered list, but o2 isn't. o1 is smaller (i.e. first)
					return -1;
				if (ORDERED_ENTRIES.contains(o2)) // o2 is in the ordered list, but o1 isn't. o2 is smaller (i.e. first)
					return 1;
				return o1.toString().compareTo(o2.toString()); //not sure if correct
			}
		});
	}
	
	public Comparator<String> getSentenceComparator() {
		return sentenceComparator;
	}

	public void setSentenceComparator(Comparator<String> sentenceComparator) {
		this.sentenceComparator = sentenceComparator;
	}

	public HashMap<String,Sentence> getSentences() {
		return sentences;
	}

	public void setSentences(HashMap<String,Sentence> sentences) {
		this.sentences = sentences;
	}

	public List<String> getPossibleAnswers() {
		return possibleAnswers;
	}
		
	public List<String> getAgentClaimNL(Agent a){
		List<Belief> believes = a.getBeliefCollection().getBelieves();
		List<String> claims = new ArrayList<>();
		for (Belief belief : believes) {
			if (belief instanceof Believed) {
				if (getPossibleAnswers().contains( ((Believed) belief).getSentenceId()  ) )
					claims.add(((Believed) belief).getSentenceId());
			}
		}
		return claims;
	}

	public abstract void buildExample();

	public abstract String checkStopTalking(Agent a1, Agent a2, Dialogue.CONDITION condition);

	public abstract AgentType checkAgentType(Agent a);
	
}
