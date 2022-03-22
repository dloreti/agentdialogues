package it.unibo.ai.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import asp4j.solver.SolverException;
import it.unibo.ai.beliefobjects.Understood;

public class Dialogue {

	public enum CONDITION { SILENT, DISCUSS };
	private CONDITION condition;

	private Agent agent1 ;
	private Agent agent2 ;
	private int maxGiveAndTake;
	private ProblemSentences ps;



	public Dialogue(CONDITION condition, Agent agent1, Agent agent2, int maxGiveAndTake, ProblemSentences problemSentences) {
		super();
		this.condition = condition;
		this.agent1 = agent1;
		this.agent2 = agent2;
		this.ps = problemSentences;
		
		//maxGiveAndTake IS A VALID ARGUMENT ONLY IN CASE OF DISCUSS CONDITION
		if (condition==CONDITION.DISCUSS)     
			this.maxGiveAndTake = maxGiveAndTake;  
		else // IN CASE OF SILENT CONDITION, ONLY ONE GIVE AND TAKE IS PERFORMED
			this.maxGiveAndTake = 1;  
	}

	private void silenceDialogue() throws SolverException{
		Logger logger = LogManager.getLogger("agentdialogues");
		//logger.debug("before "+agent1.getName()+" thinks: "+agent1);
		agent1.think();
		logger.info("Beginning "+agent1.getName()+" Type="+ps.checkAgentType(agent1)+" thinks: "+agent1+ " claims: "+ps.getAgentClaimNL(agent1));
		//logger.debug("before "+agent2.getName()+" thinks: "+agent2);
		agent2.think();
		logger.info("Beginning "+agent2.getName()+" Type="+ps.checkAgentType(agent2)+" thinks: "+agent2+ " claims: "+ps.getAgentClaimNL(agent2));

		for (int i = 0; i < maxGiveAndTake; i++) { //maxGiveAndTake should be one in case of silence condition!!!

			//System.out.print("GT "+i+" "+agent1.getName()+ " ");
			List<String> utteredByA = agent1.selectiveUtter();
			
			//System.out.print("GT "+i+" "+agent2.getName()+ " ");
			List<String> utteredByB = agent2.selectiveUtter();
			
			List<Understood> understoodByB = agent2.selectiveUnderstand(utteredByA);
			//String s = "GT "+i+" "+agent2.getName()+" understands: ";
			String s = "GT "+i+" "+agent1.getName()+" says (actually "+agent2.getName()+" understands): ";
			List<String> t = new ArrayList<String>();
			for (Understood u : understoodByB) {
				t.add(u.getSentenceId());
			}
			Collections.sort(t, ps.getSentenceComparator());
			for (String x : t) 
				s+="\n\t"+x+": "+ps.getSentences().get(x).getHumanReadable(); 			
			logger.info(s);
			//t.forEach(x-> {System.out.println("\t"+x+": "+ps.getSentences().get(x).getHumanReadable()); });
			logger.info("GT "+i+" "+agent2.getName()+" Type="+ps.checkAgentType(agent2)+" thinks: "+agent2+ " claims: "+ps.getAgentClaimNL(agent2));	
			//System.out.println("GT "+i+" "+agent2.getName()+" thinks: "+agent2+ " claims: "+ps.getAgentClaimNL(agent2));	
			
			List<Understood> understoodByA = agent1.selectiveUnderstand(utteredByB);
			//s = "GT "+i+" "+agent1.getName()+" understands: ";
			s = "GT "+i+" "+agent2.getName()+" says (actually "+agent1.getName()+" understands): ";
			t = new ArrayList<String>();
			for (Understood u : understoodByA) {
				t.add(u.getSentenceId());
			}
			Collections.sort(t, ps.getSentenceComparator());
			for (String x : t) 
				s+="\n\t"+x+": "+ps.getSentences().get(x).getHumanReadable(); 
			logger.info(s);
			//t.forEach(x-> {System.out.println("\t"+x+": "+ps.getSentences().get(x).getHumanReadable()); });
			logger.info("GT "+i+" "+agent1.getName()+" Type="+ps.checkAgentType(agent1)+" thinks: "+agent1+ " claims: "+ps.getAgentClaimNL(agent1));	
			//System.out.println("GT "+i+" "+agent1.getName()+" thinks: "+agent1+ " claims: "+ps.getAgentClaimNL(agent1));	

			String res = ps.checkStopTalking(agent1, agent2, condition);
			if (res != null){
				logger.info(res);
				break;
			}
		}
	}

	private void discussDialogue() throws SolverException{
		Logger logger = LogManager.getLogger("agentdialogues");
		agent1.think();
		logger.info("Beginning "+agent1.getName()+" Type="+ps.checkAgentType(agent1)+" thinks: "+agent1+ " claims: "+ps.getAgentClaimNL(agent1));
		agent2.think();
		logger.info("Beginning "+agent2.getName()+" Type="+ps.checkAgentType(agent2)+" thinks: "+agent2+ " claims: "+ps.getAgentClaimNL(agent2));

		for (int i = 0; i < maxGiveAndTake; i++) {

			logger.debug("GT "+i+" "+agent1.getName()+ " calling selectiveUtter...");
			List<String> utteredByA = agent1.selectiveUtter();
			
			/*System.out.println("GT "+i+" "+agent1.getName()+" utters: ");
			utteredByA.forEach(x-> {
				System.out.println("\t"+x+": "+problemSentences.getSentences().get(x).getHumanReadable()); 
			});*/

			List<Understood> understoodByB = agent2.selectiveUnderstand(utteredByA);
			//String s = "GT "+i+" "+agent2.getName()+" understands: ";
			String s = "GT "+i+" "+agent1.getName()+" says (actually "+agent2.getName()+" understands): ";
			List<String> t = new ArrayList<String>();
			for (Understood u : understoodByB) {
				t.add(u.getSentenceId());
			}
			Collections.sort(t, ps.getSentenceComparator());
			for (String x : t) {
				s+="\n\t"+x+": "+ps.getSentences().get(x).getHumanReadable(); 
			}
			logger.info(s);
			
			logger.info("GT "+i+" "+agent2.getName()+" Type="+ps.checkAgentType(agent2)+" thinks: "+agent2+ " claims: "+ps.getAgentClaimNL(agent2));	

			logger.debug("GT "+i+" "+agent2.getName()+ " calling selectiveUtter...");
			List<String> utteredByB = agent2.selectiveUtter();
			//System.out.println(agent2.getName()+" just uttered: "+agent2.getBeliefCollection().getJustUttered());
			
			/*System.out.println("GT "+i+" "+agent2.getName()+" utters: ");
			utteredByB.forEach(x-> {
				System.out.println("\t"+x+": "+problemSentences.getSentences().get(x).getHumanReadable()); 
			});*/


			List<Understood> understoodByA = agent1.selectiveUnderstand(utteredByB);
			//s = "GT "+i+" "+agent1.getName()+" understands: ";
			s = "GT "+i+" "+agent2.getName()+" says (actually "+agent1.getName()+" understands): ";
			t = new ArrayList<String>();
			for (Understood u : understoodByA) {
				t.add(u.getSentenceId());
			}
			Collections.sort(t, ps.getSentenceComparator());
			for (String x : t) {
				s+="\n\t"+x+": "+ps.getSentences().get(x).getHumanReadable(); 
			}
			logger.info(s);
			logger.info("GT "+i+" "+agent1.getName()+" Type="+ps.checkAgentType(agent1)+" thinks: "+agent1+ " claims: "+ps.getAgentClaimNL(agent1));	

			String res = ps.checkStopTalking(agent1, agent2, condition);
			if (res != null){
				logger.info(res);
				break;
			}
		}
	}

	public void startDialogue() throws SolverException {
		switch (this.condition) {
		case  DISCUSS:
			discussDialogue();
			break;
		case SILENT:
			silenceDialogue();
			break;
		default:
			break;
		}
	}

}
