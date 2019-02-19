package it.unibo.ai.beliefobjects;

import java.util.List;

import asp4j.mapping.annotations.Arg;

public abstract class SingleSentenceBelief extends Belief {

	public SingleSentenceBelief(){
		super();
	}
	
	public SingleSentenceBelief(List<String> sentences) {
		super(sentences);
	}
	
	public SingleSentenceBelief(String sentenceId) {
		super();
		this.setSentenceId(sentenceId); //this.getSentences().add(sentenceId);
	}
	
	
	@Arg(0)
	public String getSentenceId() {
		if (this.getSentences().size() == 0) return null;
		return this.getSentences().get(0);
	}
	
	public void setSentenceId(String sentenceId) {
		if (this.getSentences().size() == 0)
			this.getSentences().add(sentenceId);
		else
			this.getSentences().set(0, sentenceId);
	}

}
