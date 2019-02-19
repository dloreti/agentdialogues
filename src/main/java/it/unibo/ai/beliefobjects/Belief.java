package it.unibo.ai.beliefobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.unibo.ai.entities.ProblemSentences;

public abstract class Belief implements IBelief {

	private List<String> sentences;
	
	public Belief(){
		this(new ArrayList<String>());
	}
	
	public Belief(List<String> sentences) {
		super();
		this.sentences = sentences;
	}

	public List<String> getSentences() {
		return sentences;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null  || getClass() != obj.getClass()) 
			return false;

		final Belief other = (Belief) obj;
		if (sentences.size()!=other.sentences.size())
			return false;

		for (String sentenceId : sentences) {
			if (!other.sentences.contains(sentenceId))
				return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = this.getClass().hashCode();
		hash = 59 * hash + Objects.hashCode(sentences);
		return hash;
	}
	
	public String exaustiveToString(ProblemSentences ps){
		String c = this.getClass().getName() +"(";
		int i =0;
		 for (String s : sentences) {
			c = c+ps.getSentences().get(s).getHumanReadable();
			if (i<sentences.size()-1) 
				c = c+",";	
			i++;
		}
		return c+ ")";
	}
}
