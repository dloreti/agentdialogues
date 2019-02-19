package it.unibo.ai.entities;

public class Sentence {
	private String id;
	private String humanReadable;
	
	public Sentence(String id, String humanReadable) {
		super();
		this.id = id;
		this.humanReadable = humanReadable;
	}
	
	public String getId() {
		return id;
	}
	public String getHumanReadable() {
		return humanReadable;
	}
}
