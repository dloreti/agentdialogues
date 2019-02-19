package it.unibo.ai.beliefobjects;


import asp4j.mapping.annotations.DefAtom;


@DefAtom("bel")
public class Believed extends SingleSentenceBelief{

	public Believed(){super();}

	public Believed(String sentenceId) {
		super(sentenceId);
	}

	@Override
	public String toString() {
		return "bel("+getSentenceId()+")";
	}

	@Override
	public Boolean isUtterable() {
		return true;
	}
	
}
