package it.unibo.ai.entities;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import it.unibo.ai.beliefobjects.Belief;
import it.unibo.ai.beliefobjects.Believed;

public class LindaSentences extends ProblemSentences{
	
	private List<String> possibleAnswers = Arrays.asList("b","a");
	
	
	public LindaSentences() {
		super();
		//this.correctAnswer="a";
		//this.wrongAnswer="b";
		
		this.setSentenceComparator(new Comparator<String>(){
			private final List<String> ORDERED_ENTRIES = Arrays.asList(
					"c","d","e","a","i","f","b");
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

	public void buildExample(){
		getSentences().put("a",new Sentence("a","yes"));
		getSentences().put("b",new Sentence("b","cannot be determined"));
		getSentences().put("c",new Sentence("c","we don't know if linda is married or not married"));
		getSentences().put("d",new Sentence("d","linda is either married or not married"));
		getSentences().put("e",new Sentence("e","if linda is married, linda is looking at a person who is not married; otherwise paol is looking at a person who is not married"));
		getSentences().put("f",new Sentence("f","since we don't know if linda is married or not married, the answer is cannot be determined"));
		getSentences().put("i",new Sentence("i","it doesn't matter that we don't know if linda is married or not married"));
		//sentences.put("u",new Sentence("u","the bat cannot cost $1 because the difference between 10¢ and $1 is not $1"));	
	}

	@Override
	public String checkStopTalking(Agent a1, Agent a2, Dialogue.CONDITION condition) {
		List<Belief> b1 = a1.getBeliefCollection().getBelieves();
		List<Belief> b2 = a2.getBeliefCollection().getBelieves();
		if (b1.contains(new Believed("b")) &&
				b2.contains(new Believed("b")) ){
			return "THEY BOTH AGREE ON THE WRONG INTUITIVE ANSWER";		
		}
		if (b1.contains(new Believed("a")) &&
				b2.contains(new Believed("a")) ){
			return "THEY BOTH AGREE ON THE CORRECT ANSWER";
		}else
			return null;
	}

	@Override
	public List<String> getPossibleAnswers() {
		return possibleAnswers;
	}

	@Override
	public List<String> getAgentClaimNL(Agent a) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public AgentType checkAgentType(Agent a){
		// TODO Auto-generated method stub
		return null;
	}

}
