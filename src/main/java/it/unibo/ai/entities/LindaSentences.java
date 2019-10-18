package it.unibo.ai.entities;

import java.util.Arrays;
import java.util.List;
import it.unibo.ai.beliefobjects.Applicable;
import it.unibo.ai.beliefobjects.Belief;
import it.unibo.ai.beliefobjects.Believed;

public class LindaSentences extends ProblemSentences{
	
	private final List<String> ARGUMENT_FIRST_ORDERING = Arrays.asList(
			"i","e","b","f","a","u");
	private final List<String> CONCLUSION_FIRST_ORDERING = Arrays.asList(
			"b","e","a","f","u","i");
		
	
	public LindaSentences(ORDERING ordering) {
		super();
		possibleAnswers = Arrays.asList("b","a");
		if (ordering==ORDERING.ARGUMENT_FIRST)
			ORDERED_ENTRIES=ARGUMENT_FIRST_ORDERING;
		else
			ORDERED_ENTRIES=CONCLUSION_FIRST_ORDERING;
	}

	public void buildExample(){
		getSentences().put("a",new Sentence("a","yes"));
		getSentences().put("b",new Sentence("b","cannot be determined"));
		getSentences().put("e",new Sentence("e","since we don't know if Linda is married or not married, the answer is cannot be determined"));
		getSentences().put("f",new Sentence("f","Linda is either married or not married. If Linda is married, she is looking at a person who is not married; otherwise Paul is looking at a person who is not married"));
		getSentences().put("u",new Sentence("u","it doesn't matter that we don't know if Linda is married or not married"));
		getSentences().put("i",new Sentence("i","Paul is looking at Linda and Linda is looking at Patrick. Paul is married and Patrick is not"));
	}

	@Override
	public String checkStopTalking(Agent a1, Agent a2, Dialogue.CONDITION condition) {
		if (condition==Dialogue.CONDITION.DISCUSS){
			//*** AgentType-based stopping condition
			LindaAgentType a1type = (LindaAgentType)checkAgentType(a1);  
			LindaAgentType a2type = (LindaAgentType)checkAgentType(a2);
			if (a1type==LindaAgentType.X && a2type==LindaAgentType.X )
				return "THEY BOTH AGREE ON THE WRONG INTUITIVE ANSWER";		
			if (a1type==LindaAgentType.Y && a2type==LindaAgentType.Y )
				return "THEY BOTH AGREE ON THE CORRECT ANSWER";
			if ( (a1type==LindaAgentType.Z && a2type==LindaAgentType.Z ) )// || (a1type==LindaAgentType.K && a2type==LindaAgentType.K) )
				return "THEY BOTH AGREE ON THE CORRECT ANSWER (BUT THEY CANNOT CONVINCE ANYONE ELSE)";
			else
				return null;
		}else if (condition==Dialogue.CONDITION.SILENT){
			//*** lastUttered-based stopping condition
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
		//X: bel(b), not bel(a) 
		if (believes.contains(new Believed("b")) && ! believes.contains(new Believed("a") )) 
			return LindaAgentType.X;
		//Y: bel(a), app(f), not bel(b)
		if (believes.contains(new Believed("a")) && believes.contains(new Applicable("f")) && ! believes.contains(new Believed("b")) )  //!contain(b) is probably redundant
			return LindaAgentType.Y;
		//Z: bel(a), not app(f), not bel(b)
		if (believes.contains(new Believed("a")) && ! believes.contains(new Believed("b")) && ! believes.contains(new Applicable("f")) )  
			return LindaAgentType.Z;
		//Z: bel(a), not app(f), bel(b)
		if (believes.contains(new Believed("a")) && believes.contains(new Believed("b")) && ! believes.contains(new Applicable("f")) )  //!contain(f) is probably redundant
			return LindaAgentType.K;
		
		return null;
	}

}
