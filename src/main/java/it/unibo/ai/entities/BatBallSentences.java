package it.unibo.ai.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.unibo.ai.MainBB;
import it.unibo.ai.beliefobjects.Applicable;
import it.unibo.ai.beliefobjects.Belief;
import it.unibo.ai.beliefobjects.Believed;

public class BatBallSentences extends ProblemSentences{

	
	private final List<String> ARGUMENT_FIRST_ORDERING = Arrays.asList(
			"i","e","b","f","a","d","u","c");
	private final List<String> CONCLUSION_FIRST_ORDERING = Arrays.asList(
			"b","e","a","f","c","d","u","i");
	

	public BatBallSentences(ORDERING ordering) {
		super();
		this.possibleAnswers = Arrays.asList("b","a");
		if (ordering==ORDERING.ARGUMENT_FIRST)
			ORDERED_ENTRIES=ARGUMENT_FIRST_ORDERING;
		else
			ORDERED_ENTRIES=CONCLUSION_FIRST_ORDERING;
		List<String> rulefiles = new ArrayList<>();
		rulefiles.add(MainBB.class.getResource("/bk.lp").getPath()); 
		rulefiles.add(MainBB.class.getResource("/batball.lp").getPath());
		this.setRulefiles(rulefiles);
	}
	
	public BatBallSentences(ORDERING ordering, List<String> rulefiles) {
		this(ordering);
		this.setRulefiles(rulefiles);
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
		return null;
	}
	

}
