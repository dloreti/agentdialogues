package it.unibo.ai.beliefobjects;


import asp4j.mapping.annotations.DefAtom;


@DefAtom("app") 
public class Applicable extends SingleSentenceBelief{

	public Applicable(){super();}

	public Applicable(String sentenceId) {
		super(sentenceId);
	}
	

	@Override
	public String toString() {
		return "app("+getSentenceId()+")";
	}

	@Override
	public Boolean isUtterable() {
		return true;
	}
}
