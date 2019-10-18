package it.unibo.ai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import asp4j.solver.ReasoningMode;
import asp4j.solver.SolverClingo;
import asp4j.solver.SolverException;
import asp4j.solver.object.Filter;
import asp4j.solver.object.ObjectSolver;
import asp4j.solver.object.ObjectSolverImpl;
import it.unibo.ai.beliefobjects.Accessible;
import it.unibo.ai.beliefobjects.Agree;
import it.unibo.ai.beliefobjects.Applicable;
import it.unibo.ai.beliefobjects.Because;
import it.unibo.ai.beliefobjects.Belief;
import it.unibo.ai.beliefobjects.Believed;
import it.unibo.ai.beliefobjects.Derived;
import it.unibo.ai.beliefobjects.Rebut;
import it.unibo.ai.beliefobjects.Undercut;
import it.unibo.ai.beliefobjects.Understood;
import it.unibo.ai.entities.Agent;
import it.unibo.ai.entities.AgentBeliefCollection;
import it.unibo.ai.entities.BatBallSentences;
import it.unibo.ai.entities.ProblemSentences;
import it.unibo.ai.entities.Dialogue.CONDITION;
import it.unibo.ai.strategies.AllUnderstandingStrategy;
import it.unibo.ai.strategies.AllUtterablesUtteringStrategy;

/**
 * @author Daniela Loreti
 * This main simulate a specific batball dialogue between two specific agents: X and Y
 *
 */
public class MainBBFixedDialogue  {
	

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException{
		Logger logger = LogManager.getLogger("agentdialogues");
		
		if (args.length==0){
			System.out.println("Usage: \n"
					+ "/path/to/clingorulefile1.lp [/path/to/clingorulefile2.lp ...]");
		}

		int maxUtterablePerTurn = 6;
		
		List<String> rulefiles = new ArrayList<>();
		rulefiles.add(MainBB.class.getResource(args[0]).getPath());
		rulefiles.add(MainBB.class.getResource(args[1]).getPath());

		ProblemSentences ps = new BatBallSentences(BatBallSentences.ORDERING.CONCLUSION_FIRST);
		ps.buildExample();

		ObjectSolver solver = new ObjectSolverImpl(new SolverClingo());

		Filter filter = new Filter()
				.add(Accessible.class)
				.add(Applicable.class)
				.add(Understood.class)
				.add(Derived.class)
				.add(Agree.class)
				.add(Rebut.class)
				.add(Undercut.class)
				.add(Because.class)
				.add(Believed.class);

		/************ AGENTS ****************/
		List<ArrayList<Belief>>  possible_i_acc = new ArrayList<ArrayList<Belief>>(); 
		ArrayList<Belief>  x_i_acc = new ArrayList<Belief>(); 
		x_i_acc.add(new Accessible("e")); 
		x_i_acc.add(new Accessible("i"));
		ArrayList<Belief>  y_i_acc = new ArrayList<Belief>(); 
		y_i_acc.add(new Accessible("d")); 
		y_i_acc.add(new Accessible("f")); 
		y_i_acc.add(new Accessible("i"));
		ArrayList<Belief>  z_i_acc = new ArrayList<Belief>(); 
		z_i_acc.add(new Accessible("f"));
		z_i_acc.add(new Accessible("i"));
		possible_i_acc.add(x_i_acc);
		possible_i_acc.add(y_i_acc);
		possible_i_acc.add(z_i_acc);

		List<String> agentNames = new ArrayList<>();
		agentNames.add("X");
		agentNames.add("Y");
		agentNames.add("Z");

		System.out.println("STATIC DIALOGUE TEST");
		Agent agentX = new Agent("X", 
				new AgentBeliefCollection(
						(ArrayList<Belief>)possible_i_acc.get(0).clone(),
						new AllUtterablesUtteringStrategy(maxUtterablePerTurn, ps.getSentenceComparator()),
						new AllUnderstandingStrategy()
						),
				rulefiles, ReasoningMode.CAUTIOUS, filter, solver
				);
		Agent agentY = new Agent("Y", 
				new AgentBeliefCollection(
						(ArrayList<Belief>) possible_i_acc.get(1).clone(),
						new AllUtterablesUtteringStrategy(maxUtterablePerTurn, ps.getSentenceComparator()),
						new AllUnderstandingStrategy()
						),
				rulefiles, ReasoningMode.CAUTIOUS, filter, solver
				);
		try {
			System.out.println("\n*********** DIALOGUE X - Y :");
			agentX.think(); System.out.println("Start "+agentX.getName()+" thinks: "+agentX);	
			agentY.think(); System.out.println("Start "+agentY.getName()+" thinks: "+agentY);	

			List<String> utteredByX = new ArrayList<String>();
			utteredByX.add("b");
			List<Understood> understoodByY = agentY.selectiveUnderstand(utteredByX);
			System.out.println("GT 0 "+agentY.getName()+" understands: ");
			List<String> t = new ArrayList<String>();
			for (Understood u : understoodByY) {
				t.add(u.getSentenceId());
			}
			Collections.sort(t, ps.getSentenceComparator());
			t.forEach(x-> {
				System.out.println("\t"+x+": "+ps.getSentences().get(x).getHumanReadable()); 
			});
			System.out.println("GT 0 "+agentY.getName()+" thinks: "+agentY);	

			List<String> utteredByY = new ArrayList<String>();
			utteredByY.add("i");
			utteredByY.add("f");
			utteredByY.add("d");
			utteredByY.add("c");
			List<Understood> understoodByX = agentX.selectiveUnderstand(utteredByY);
			System.out.println("GT 0 "+agentX.getName()+" understands: ");
			t = new ArrayList<String>();
			for (Understood u : understoodByX) {
				t.add(u.getSentenceId());
			}
			Collections.sort(t, ps.getSentenceComparator());
			t.forEach(x-> {
				System.out.println("\t"+x+": "+ps.getSentences().get(x).getHumanReadable()); 
			});
			//agentX.think()
			System.out.println("GT 0 "+agentX.getName()+" thinks: "+agentX);	

			String res = ps.checkStopTalking(agentX, agentY, CONDITION.DISCUSS);
			if (res != null){
				System.out.println(res);
			}
			
		} catch (SolverException e) {
			logger.error("",e);
		}



	}


}
