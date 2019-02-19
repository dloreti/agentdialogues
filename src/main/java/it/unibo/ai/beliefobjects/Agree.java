package it.unibo.ai.beliefobjects;


import asp4j.mapping.annotations.DefAtom;


@DefAtom("agree") 
public class Agree extends SingleSentenceBelief{

	public Agree(){super();}

	public Agree(String sentenceId) {
		super(sentenceId);
	}

	@Override
	public String toString() {
		return "agree("+getSentenceId()+")";
	}

	@Override
	public Boolean isUtterable() {
		return false;
	}
}
