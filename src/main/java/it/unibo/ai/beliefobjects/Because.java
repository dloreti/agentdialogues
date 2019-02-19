package it.unibo.ai.beliefobjects;


import asp4j.mapping.annotations.DefTerm;


@DefTerm("because") 
public class Because extends SingleSentenceBelief{

	public Because(){super();}

	public Because(String sentenceId) {
		super(sentenceId);
	}


	@Override
	public String toString() {
		return "because("+getSentenceId()+")";
	}

	@Override
	public Boolean isUtterable() {
		return false;
	}
}
