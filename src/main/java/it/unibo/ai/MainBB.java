package it.unibo.ai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.nlogo.api.MersenneTwisterFast;

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
import it.unibo.ai.beliefobjects.Get;
import it.unibo.ai.beliefobjects.Rebut;
import it.unibo.ai.beliefobjects.Undercut;
import it.unibo.ai.beliefobjects.Understood;
import it.unibo.ai.entities.Agent;
import it.unibo.ai.entities.AgentBeliefCollection;
import it.unibo.ai.entities.BatBallSentences;
import it.unibo.ai.entities.Dialogue;
import it.unibo.ai.entities.ProblemSentences;
import it.unibo.ai.strategies.FirstOneUnderstandingStrategy;
import it.unibo.ai.strategies.IUnderstandingStrategy;
import it.unibo.ai.strategies.IUtteringStrategy;
import it.unibo.ai.strategies.ReplayPreferredAllUtteringStrategy;
import it.unibo.ai.strategies.ProgressivelyMoreStubbornUtteringStrategy;
/**
 * @author Daniela Loreti
 * This main simulate 9 batball dialogues between pairs of the three agents: X, Y and Z. 
 * Here the agent utters anything is utterable among her believes 
 * and understands a random subset of what she is told (at least one belief)
 *
 */
public class MainBB  {
	
	private static final Logger logger = LogManager.getLogger("agentdialogues");
      
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException{
		Configurator.setLevel("agentdialogues", Level.INFO);

		
		int maxUtterablePerTurn = 2;
		int maxGiveAndTake = 0;
		Dialogue.CONDITION condition = Dialogue.CONDITION.SILENT; // Dialogue.CONDITION.DISCUSS; // 
		

		ProblemSentences ps = new BatBallSentences(BatBallSentences.ORDERING.CONCLUSION_FIRST);
		ps.buildExample();

		List<String> rulefiles = new ArrayList<>();
		rulefiles.add(MainBB.class.getResource("/bk.lp").getPath());
		rulefiles.add(MainBB.class.getResource("/batball.lp").getPath());
		
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
				.add(Believed.class)
				.add(Get.class);

		/************ AGENTS ****************/
		MersenneTwisterFast r = new MersenneTwisterFast();

		List<ArrayList<Belief>>  possible_i_acc = new ArrayList<ArrayList<Belief>>(); 
		ArrayList<Belief>  x_i_acc = new ArrayList<Belief>(); 
		x_i_acc.add(new Accessible("e")); 
		x_i_acc.add(new Accessible("i"));
		// newly added support to discernible arguments
		x_i_acc.add(new Get("d", "a"));
		
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

		List<String> agentTypes = new ArrayList<>();
		agentTypes.add("X");
		agentTypes.add("Y");
		agentTypes.add("Z");


		IUtteringStrategy utteringStrategyA = null;
		IUnderstandingStrategy understandingStrategyA = null;
		IUtteringStrategy utteringStrategyB = null;
		IUnderstandingStrategy understandingStrategyB = null;
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				
				if (condition==Dialogue.CONDITION.DISCUSS) {
					utteringStrategyA = new ReplayPreferredAllUtteringStrategy(maxUtterablePerTurn, ps.getSentenceComparator(),r);
					understandingStrategyA = new FirstOneUnderstandingStrategy();
					utteringStrategyB = new ReplayPreferredAllUtteringStrategy(maxUtterablePerTurn, ps.getSentenceComparator(),r);
					understandingStrategyB = new FirstOneUnderstandingStrategy();
					 maxGiveAndTake = 10;
				}else if (condition==Dialogue.CONDITION.SILENT) {
					utteringStrategyA = new ProgressivelyMoreStubbornUtteringStrategy(ps.getPossibleAnswers(),null, 0, r);
					understandingStrategyA = new FirstOneUnderstandingStrategy();
					utteringStrategyB = new ProgressivelyMoreStubbornUtteringStrategy(ps.getPossibleAnswers(),null, 0, r);
					understandingStrategyB = new FirstOneUnderstandingStrategy();
					 maxGiveAndTake = 1;
				}
				
				
				Agent One = new Agent("A1",//agentTypes.get(i)+"1", 
						new AgentBeliefCollection( (ArrayList<Belief>)possible_i_acc.get(i).clone(),
								utteringStrategyA,
								understandingStrategyA),
						rulefiles, ReasoningMode.CAUTIOUS, filter, solver
						);
				Agent Two = new Agent("A2",//agentTypes.get(j)+"2", 
						new AgentBeliefCollection( (ArrayList<Belief>) possible_i_acc.get(j).clone(),
								utteringStrategyB,
								understandingStrategyB),
						rulefiles, ReasoningMode.CAUTIOUS, filter, solver
						);
				try {
					logger.info("*********** DIALOGUE "+One.getName()+"[Type="+agentTypes.get(i)+"] - "+Two.getName()+"[Type="+agentTypes.get(j)+"] :");
					Dialogue d = new Dialogue(condition,One,Two,maxGiveAndTake,ps);
					d.startDialogue();
					logger.info("DIALOGUE ENDS WITH "+One.getName()+"[Type="+ps.checkAgentType(One)+"] - "+Two.getName()+"[Type="+ps.checkAgentType(Two)+"] :");
				} catch (SolverException e) {
					logger.error("",e);
				}
			}
		}

	}


}
