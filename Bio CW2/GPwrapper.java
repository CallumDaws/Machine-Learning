import java.util.*;
import java.lang.*;

import static org.epochx.stats.StatField.*;

import org.epochx.gp.model.*;
import org.epochx.gp.op.crossover.KozaCrossover;
import org.epochx.life.*;
import org.epochx.op.selection.TournamentSelector;
import org.epochx.stats.*;
import org.epochx.tools.random.MersenneTwisterFast;

import org.epochx.epox.*;
import org.epochx.epox.math.*;
import org.epochx.epox.lang.*;
import org.epochx.gp.representation.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.util.BoolUtils;
import org.epochx.tools.random.MersenneTwisterFast;

public class GPwrapper extends GPModel  
{
	private int numAtts;
	private InstanceSet is;
	private Variable[] vars;
	private int numConstants = 20;

	public GPwrapper(InstanceSet pIs) {
		numAtts=Attributes.getNumAttributes();
		is=pIs;
	}

	@Override
	public double getFitness(CandidateProgram p) {
		GPCandidateProgram program = (GPCandidateProgram) p;

		ClassifierGP cGP = new ClassifierGP(program,vars);

		Instance[] instances = is.getInstances();
		int numErrors = 0;
		for (int i=0; i<instances.length; i++) {
			int pred = cGP.classifyInstance(instances[i]);
			if(pred != instances[i].getClassValue()) {
				numErrors++;
			}
		}

	   	return numErrors + 0.01 * program.getProgramLength();
	}

	@Override
	public Class<?> getReturnType() {
		return Double.class;
	}

	public Classifier generateClassifier() {
		vars = new Variable[numAtts];
		double min=0,max=0;
		for(int i=0;i<numAtts;i++) {
			Attribute att = Attributes.getAttribute(i);
			if(i==0) {
				min = att.minAttribute();
				max = att.maxAttribute();
			} else {
				if(att.minAttribute()<min) {
					min = att.minAttribute();
				}
				if(att.maxAttribute()>max) {
					max = att.maxAttribute();
				}
			}
			vars[i] = new Variable("var"+i,Double.class);
		}

		List<Node> syntax = new ArrayList<Node>();
		// Terminals. First, the placeholders for the data attributes
		for(int i=0;i<numAtts;i++) {
			syntax.add(vars[i]);
		}

		// Functions.
		syntax.add(new AddFunction());
		syntax.add(new MultiplyFunction());
		syntax.add(new DivisionProtectedFunction());
		syntax.add(new SubtractFunction());

		//syntax.add(new AbsoluteFunction());
		//syntax.add(new CubeFunction());
		//syntax.add(new CubeRootFunction());
		//syntax.add(new ExponentialFunction());
		//syntax.add(new FactorialFunction());
		//syntax.add(new GreaterThanFunction());
		//syntax.add(new InvertProtectedFunction());
		//syntax.add(new LessThanFunction());
		//syntax.add(new Log10Function());
		//syntax.add(new LogFunction());
		//syntax.add(new Max2Function());
		//syntax.add(new Max3Function());
		//syntax.add(new Min2Function());
		//syntax.add(new Min3Function());
		//syntax.add(new ModuloProtectedFunction());
		//syntax.add(new PowerFunction());
		//syntax.add(new SquareFunction());
	

		// And now the random constants, set within the domain of the data variable
		//MersenneTwisterFast prng = new MersenneTwisterFast();
		//DoubleERC generator = new DoubleERC(prng,min,max,5);
		//for(int i=0;i<numConstants;i++) {
		//	syntax.add(generator.newInstance());
		//}

		//double constant = min;
		//double step = (max-min)/(double)(numConstants-1);
		//for(int i=0;i<numConstants;i++) {
		//	Literal lit = new Literal(constant);
		//	syntax.add(lit);
		//	constant+=step;
		//}

		setSyntax(syntax);

		// Set parameters.
		setPopulationSize(250);
		setNoGenerations(50);
		setNoElites(10);
		setCrossoverProbability(0.9);
		setMutationProbability(0.2);
		setReproductionProbability(0.2);
		// Set operators and components.
		setProgramSelector(new TournamentSelector(this, 10));

		Life.get().addGenerationListener(new GenerationAdapter(){
			public void onGenerationEnd() {
					Stats.get().print(GEN_NUMBER, GEN_FITNESS_MIN, GEN_FITTEST_PROGRAM);
			}
		});

		// Run the model.
		run();
		GPCandidateProgram bestProgram = (GPCandidateProgram) Stats.get().getStat(RUN_FITTEST_PROGRAM);
		System.out.println("Best program:");
		System.out.println(bestProgram);

		return new ClassifierGP(bestProgram,vars);
	}
}
