package it.unibo.ai.beliefobjects;


import asp4j.mapping.annotations.DefAtom;


@DefAtom("und") 
public class Understood extends SingleSentenceBelief{

	public Understood(){super();}

	public Understood(String sentenceId) {
		super(sentenceId);
	}

	@Override
	public String toString() {
		return "und("+getSentenceId()+")";
	}

	@Override
	public Boolean isUtterable() {
		return false;
	}

}
