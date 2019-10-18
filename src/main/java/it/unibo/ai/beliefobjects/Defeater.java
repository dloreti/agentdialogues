package it.unibo.ai.beliefobjects;

import java.util.ArrayList;

import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.DefAtom;


@DefAtom("def")
public class Defeater extends Belief{
	
	public Defeater(){}

	public Defeater(String defeater, String reasoning, String defeated) {
		super();
		this.setDefeater(defeater);
		this.setReasoning(reasoning);
		this.setDefeated(defeated);
		/*super(new ArrayList<String>());
		this.getSentences().set(0, defeater);
		this.getSentences().set(1, reasoning);
		this.getSentences().set(2, defeated);*/
	}
	
	@Arg(0)
	public String getDefeater() {
		if (this.getSentences().size() == 0) return null;
		return this.getSentences().get(0);
	}
	public void setDefeater(String defeater) {
		if (this.getSentences().size() == 0)
			this.getSentences().add(defeater);
		else
			this.getSentences().set(0, defeater);
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
	public String getDefeated() {
		if (this.getSentences().size() <= 2) return null;
		return this.getSentences().get(2);
	}
	public void setDefeated(String defeated) {
		if (this.getSentences().size() == 0)
			this.getSentences().add("");
		if (this.getSentences().size() == 1)
			this.getSentences().add("");
		if (this.getSentences().size() == 2)
			this.getSentences().add(defeated);
		else
			this.getSentences().set(2, defeated);
	}
	
	
	@Override
	public String toString() {
		return "def("+getDefeater()+","+getReasoning()+","+getDefeated()+")";
	}

	@Override
	public Boolean isUtterable() {
		return false;
	}
}
