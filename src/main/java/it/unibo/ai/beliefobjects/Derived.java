package it.unibo.ai.beliefobjects;


import asp4j.mapping.annotations.DefAtom;


@DefAtom("der") 
public class Derived extends SingleSentenceBelief{

	public Derived(){super();}

	public Derived(String sentenceId) {
		super(sentenceId);
	}


	@Override
	public String toString() {
		return "der("+getSentenceId()+")";
	}

	@Override
	public Boolean isUtterable() {
		return false;
	}
}
