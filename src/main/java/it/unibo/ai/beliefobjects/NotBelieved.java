package it.unibo.ai.beliefobjects;

import asp4j.mapping.annotations.DefAtom;


@DefAtom("-bel")
public class NotBelieved extends SingleSentenceBelief{

	public NotBelieved(){super();}

	public NotBelieved(String sentenceId) {
		super(sentenceId);
	}
	

	@Override
	public String toString() {
		return "-bel("+getSentenceId()+")";
	}

	@Override
	public Boolean isUtterable() {
		return true;
	}
}