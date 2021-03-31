package it.unibo.ai.beliefobjects;

import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.DefAtom;


@DefAtom("get")
public class Get extends Belief{
	
	public Get(){super();}

	
	public Get(String a, String b) {
		super();
		this.setDiscoverable(a); 
		this.setFrom(b);
	}

	@Arg(0)
	public String getDiscoverable() {
		if (this.getSentences().size() == 0) return null;
		return this.getSentences().get(0);
	}
	public void setDiscoverable(String a) {
		if (this.getSentences().size() == 0)
			this.getSentences().add(a);
		else
			this.getSentences().set(0, a);
	}

	@Arg(1)
	public String getFrom() {
		if (this.getSentences().size() <= 1) return null;
		return this.getSentences().get(1);
	}
	public void setFrom(String b) {
		if (this.getSentences().size() == 0)
			this.getSentences().add("");
		if (this.getSentences().size() == 1)
			this.getSentences().add(b);
		else
			this.getSentences().set(1, b);
	}
	
	
	@Override
	public String toString() {
		return "get("+getDiscoverable()+","+getFrom()+")";
	}

	@Override
	public Boolean isUtterable() {
		return false;
	}
}
