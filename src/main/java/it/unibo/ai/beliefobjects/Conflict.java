package it.unibo.ai.beliefobjects;

import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.DefAtom;


@DefAtom("con")
public class Conflict extends Belief{
	
	public Conflict(){super();}

	public Conflict(String a, String b) {
		super();
		this.setA(a); this.setB(b);
//		this.getSentences().set(0, a);
//		this.getSentences().set(1, b);
	}

	@Arg(0)
	public String getA() {
		if (this.getSentences().size() == 0) return null;
		return this.getSentences().get(0);
	}
	public void setA(String a) {
		if (this.getSentences().size() == 0)
			this.getSentences().add(a);
		else
			this.getSentences().set(0, a);
	}

	@Arg(1)
	public String getB() {
		if (this.getSentences().size() <= 1) return null;
		return this.getSentences().get(1);
	}
	public void setB(String b) {
		if (this.getSentences().size() == 0)
			this.getSentences().add("");
		if (this.getSentences().size() == 1)
			this.getSentences().add(b);
		else
			this.getSentences().set(1, b);
	}
	
	
	/*@Arg(0)
	public String getA() {
		return this.getSentences().get(0);
	}
	public void setA(String a) {
		this.getSentences().set(0, a);
	}
	@Arg(1)
	public String getB() {
		return this.getSentences().get(1);
	}
	public void setB(String b) {
		this.getSentences().set(1, b);
	}*/

	@Override
	public String toString() {
		return "con("+getA()+","+getB()+")";
	}

	@Override
	public Boolean isUtterable() {
		return false;
	}
}
