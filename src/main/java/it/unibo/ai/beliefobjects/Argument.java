package it.unibo.ai.beliefobjects;


import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.DefAtom;


@DefAtom("arg")
public class Argument extends Belief{

	public Argument(){
		super();
	}

	public Argument(String premise, String reasoning, String conclusion) {
		super();
		this.setPremise(premise);
		this.setReasoning(reasoning);
		this.setConclusion(conclusion);
		/*this.getSentences().set(0, premise);
		this.getSentences().set(1, reasoning);
		this.getSentences().set(2, conclusion);*/
	}

	@Arg(0)
	public String getPremise() {
		if (this.getSentences().size() == 0) return null;
		return this.getSentences().get(0);
	}
	public void setPremise(String premise) {
		if (this.getSentences().size() == 0)
			this.getSentences().add(premise);
		else
			this.getSentences().set(0, premise);
	}

	@Arg(1)
	public String getReasoning() {
		if (this.getSentences().size() <= 1) return null;
		return this.getSentences().get(1);
	}
	public void setReasoning(String reasoning) {
		if (this.getSentences().size() == 0)
			this.getSentences().add("");
		if (this.getSentences().size() == 1)
			this.getSentences().add(reasoning);
		else
			this.getSentences().set(1, reasoning);
	}

	@Arg(2)
	public String getConclusion() {
		if (this.getSentences().size() <= 2) return null;
		return this.getSentences().get(2);
	}
	public void setConclusion(String conclusion) {
		if (this.getSentences().size() == 0)
			this.getSentences().add("");
		if (this.getSentences().size() == 1)
			this.getSentences().add("");
		if (this.getSentences().size() == 2)
			this.getSentences().add(conclusion);
		else
			this.getSentences().set(2, conclusion);
	}

	@Override
	public String toString() {
		return "arg("+getPremise()+","+getReasoning()+","+getConclusion()+")";
	}

	@Override
	public Boolean isUtterable() {
		return false;
	}
}
