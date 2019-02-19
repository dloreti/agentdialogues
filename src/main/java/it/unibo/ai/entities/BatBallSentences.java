package it.unibo.ai.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import it.unibo.ai.beliefobjects.Agree;
import it.unibo.ai.beliefobjects.Applicable;
import it.unibo.ai.beliefobjects.Belief;
import it.unibo.ai.beliefobjects.Believed;

public class BatBallSentences extends ProblemSentences{

	public enum ORDERING { ARGUMENT_FIRST, CONCLUSION_FIRST };

	private final List<String> ARGUMENT_FIRST_ORDERING = Arrays.asList(
			"i","e","b","f","a","d","u","c");
	private final List<String> CONCLUSION_FIRST_ORDERING = Arrays.asList(
			"b","e","a","f","c","d","u","i");
	private List<String> ORDERED_ENTRIES;

	private List<String> possibleAnswers = Arrays.asList("b","a");

	public BatBallSentences(ORDERING ordering) {
		super();
		if (ordering==ORDERING.ARGUMENT_FIRST)
			ORDERED_ENTRIES=ARGUMENT_FIRST_ORDERING;
		else
			ORDERED_ENTRIES=CONCLUSION_FIRST_ORDERING;

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

	public void buildExample(){
		getSentences().put("a",new Sentence("a","the ball costs 5¢"));
		getSentences().put("b",new Sentence("b","the ball costs 10¢"));
		getSentences().put("c",new Sentence("c","10¢ is not a good answer"));
		getSentences().put("d",new Sentence("d","the difference between 10¢ and $1 is not $1"));
		getSentences().put("e",new Sentence("e","$1 + 10¢ is $1.10, therefore the ball is 10¢"));
		getSentences().put("f",new Sentence("f","if we have a ball at 5¢, and a bat at $1.05, we get 1 dollar more for the ball, and the total is $1.10"));
		getSentences().put("i",new Sentence("i","the bat and ball together cost $1.10 and the difference is $1"));
		getSentences().put("u",new Sentence("u","the bat cannot cost $1 because the difference between 10¢ and $1 is not $1"));	
	}

	public String checkStopTalking(Agent a1, Agent a2, Dialogue.CONDITION condition){
		if (condition==Dialogue.CONDITION.DISCUSS){
			//*** AgentType-based stopping condition
			BBAgentType a1type = (BBAgentType)checkAgentType(a1);  
			BBAgentType a2type = (BBAgentType)checkAgentType(a2);
			if (a1type==BBAgentType.X && a2type==BBAgentType.X )
				return "THEY BOTH AGREE ON THE WRONG INTUITIVE ANSWER";		
			if (a1type==BBAgentType.Y && a2type==BBAgentType.Y )
				return "THEY BOTH AGREE ON THE CORRECT ANSWER";
			if ( (a1type==BBAgentType.Z && a2type==BBAgentType.Z ) || (a1type==BBAgentType.K && a2type==BBAgentType.K) )
				return "THEY BOTH AGREE ON THE CORRECT ANSWER (BUT THEY CANNOT CONVINCE ANYONE ELSE)";
			else
				return null;
			/*** Discussion-based stopping condition
			//STOP if they both agree(a) agree(f):
			if (a1.getBeliefCollection().getBelieves().contains(new Agree("a")) && a1.getBeliefCollection().getBelieves().contains(new Agree("f"))
					&& a2.getBeliefCollection().getBelieves().contains(new Agree("a")) && a2.getBeliefCollection().getBelieves().contains(new Agree("f")))
				return "THEY BOTH AGREE ON THE CORRECT ANSWER";
			//STOP if they both agree(b) agree(e):
			if (a1.getBeliefCollection().getBelieves().contains(new Agree("b")) && a1.getBeliefCollection().getBelieves().contains(new Agree("e"))
					&& a2.getBeliefCollection().getBelieves().contains(new Agree("b")) && a2.getBeliefCollection().getBelieves().contains(new Agree("e")))
				return "THEY BOTH AGREE ON THE WRONG INTUITIVE ANSWER";	
			else
				return null; */
		}else if (condition==Dialogue.CONDITION.SILENT){
			String a1Uttered = a1.getJustUttered().isEmpty() ? "" : a1.getJustUttered().get(0);
			String a2Uttered = a2.getJustUttered().isEmpty() ? "" : a2.getJustUttered().get(0);
			if (a1Uttered.equals(a2Uttered) ) {
				if (a1Uttered.equals("b")) return "THEY BOTH AGREE ON THE WRONG INTUITIVE ANSWER";	
				if (a1Uttered.equals("a")) return "THEY BOTH AGREE ON THE CORRECT ANSWER";	
			}
			return null ; //"THEY DO NOT AGREE"; //in silent condition the agents stop talking after the first give and take no matter what their answers are
			//we could leave return null, if we force maxGiveAndTake to 1 in case of SILENT
		}
		return null;
	}

	/*public enum BBAgentType {
		X, //the agent that has the wrong intuitive answer (75% of the total agents)
		Y, //the agent that has the correct answer (20%)
		Z, //the agent that has the correct answer but did not really understand the problem (5%)
		K, //an agent who believes both a and b are correct
		W; //an agent who only knows that 10c is not a good answer (only knows c)
	}*/

	@Override
	public AgentType checkAgentType(Agent a){
		List<Belief> believes = a.getBeliefCollection().getBelieves();
		if (believes.contains(new Believed("b")) && believes.contains(new Believed("a")))
			return BBAgentType.K;
		if (believes.contains(new Believed("b")) && ! believes.contains(new Believed("a")) )
			return BBAgentType.X;
		if ((! believes.contains(new Believed("b")) && ! believes.contains(new Believed("c")) && believes.contains(new Believed("a"))) ||
				(believes.contains(new Believed("a")) && believes.contains(new Believed("c")) && ! believes.contains(new Applicable("d"))))
			return BBAgentType.Z;
		if (believes.contains(new Believed("a")) && believes.contains(new Believed("c")) && believes.contains(new Applicable("d"))) 
			return BBAgentType.Y;
		if (believes.contains(new Believed("c")) && ! believes.contains(new Believed("a")) )
			return BBAgentType.W;
		/*if (believes.contains(new Believed("b")) && believes.contains(new Believed("a")))
			return BBAgentType.K;
		if (believes.contains(new Believed("b")) && ! believes.contains(new Believed("a")) )
			return BBAgentType.X;
		if (! believes.contains(new Believed("b")) && ! believes.contains(new Believed("c")) && believes.contains(new Believed("a")))
			return BBAgentType.Z;
		if (believes.contains(new Believed("a")) && believes.contains(new Believed("c")) ) 
			return BBAgentType.Y;
		if (believes.contains(new Believed("c")) && ! believes.contains(new Believed("a")) )
			return BBAgentType.W;*/
		return null;
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

	public List<String> getPossibleAnswers() {
		return possibleAnswers;
	}

}
