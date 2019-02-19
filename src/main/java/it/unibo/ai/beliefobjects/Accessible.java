package it.unibo.ai.beliefobjects;

import asp4j.mapping.annotations.DefAtom;


@DefAtom("acc")
public class Accessible extends SingleSentenceBelief{
	
	public Accessible(){super();}

	public Accessible(String sentenceId) {
		super(sentenceId);
	}
	

	@Override
	public String toString() {
		return "acc("+getSentenceId()+")";
	}

	@Override
	public Boolean isUtterable() {
		return false;
	}
}
