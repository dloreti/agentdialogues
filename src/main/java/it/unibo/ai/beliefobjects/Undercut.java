package it.unibo.ai.beliefobjects;

import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.DefAtom;


@DefAtom("undercut")
public class Undercut extends Belief implements IReply{
	
	private Because because;
	
	public Undercut(){super();}

	public Undercut(String opposed, String reason) {
		super();
		this.setOpposed(opposed);//this.getSentences().set(0, opposed);
		this.because = new Because(reason);
	}


	@Arg(0)
	public String getOpposed() {
		if (this.getSentences().size() == 0) return null;
		return this.getSentences().get(0);
	}
	
	public void setOpposed(String opposed) {
		if (this.getSentences().size() == 0)
			this.getSentences().add(opposed);
		else
			this.getSentences().set(0, opposed);
	}
	
	
	@Override
	@Arg(1)
	public Because getBecause() {
		return this.because;
	}
	public void setBecause(Because because) {
		this.because=because;
	}

	@Override
	public String getReason() {
		return this.because.getSentenceId();
	}
	public void setReason(String reason) {
		this.because.setSentenceId(reason);
	}

	@Override
	public String toString() {
		return "undercut("+getOpposed()+","+because+")";
	}

	@Override
	public Boolean isUtterable() {
		return false;
	}
}
