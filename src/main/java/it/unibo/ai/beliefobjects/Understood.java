package it.unibo.ai.beliefobjects;


import asp4j.mapping.annotations.DefAtom;


@DefAtom("und") 
public class Understood extends SingleSentenceBelief{

	public Understood(){super();}

	public Understood(String sentenceId) {
		super(sentenceId);
	}
/*
	@Arg(0)
	public String getSentenceId() {
		return this.getSentences().get(0);
	}
	public void setSentenceId(String sentenceId) {
		this.getSentences().set(0, sentenceId);
	}*/

	@Override
	public String toString() {
		return "und("+getSentenceId()+")";
	}

	@Override
	public Boolean isUtterable() {
		return false;
	}

}
