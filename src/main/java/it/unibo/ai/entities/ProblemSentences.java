package it.unibo.ai.entities;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import it.unibo.ai.entities.Dialogue.CONDITION;

public abstract class ProblemSentences {

	private HashMap<String,Sentence> sentences = new HashMap<>();
	private Comparator<String> sentenceComparator;
	
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

	public abstract void buildExample();

	public abstract String checkStopTalking(Agent a1, Agent a2, Dialogue.CONDITION condition);

	public abstract List<String> getPossibleAnswers();
	
	public abstract List<String> getAgentClaimNL(Agent a);
	
	public abstract AgentType checkAgentType(Agent a);
		

}
