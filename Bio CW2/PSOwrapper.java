import java.util.*;

import net.sourceforge.jswarm_pso.Neighborhood1D;
import net.sourceforge.jswarm_pso.FitnessFunction;
import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.Swarm;
import java.lang.Math;



public class PSOwrapper 
{
	private int numParticles = 500;
	private int numIters = 50;
	private double neighWeight = 1.0;
	private double inertiaWeight = 1.0;
	private double personalWeight = 1.0;
	private double globalWeight = 1.0;
	private double maxMinVelocity = 0.0001;

	private int numAtts;
	private InstanceSet is;
	private int particleSize;

	public PSOwrapper(InstanceSet pIs) {
		numAtts=Attributes.getNumAttributes();
		is=pIs;
		particleSize = numAtts+2;
	}


	public Classifier generateClassifier() {
		MyFitnessFunction mFF = new MyFitnessFunction(is);
		Swarm swarm = new Swarm(numParticles, new MyParticle(), mFF);
		// Set position (and velocity) constraints. 
		// i.e.: where to look for solutions

		// Use neighborhood
		Neighborhood1D neigh = new Neighborhood1D(numParticles / 10, true);
		swarm.setNeighborhood(neigh);
		swarm.setNeighborhoodIncrement(neighWeight);

		// Set weights of velocity update formula
		swarm.setInertia(inertiaWeight); // Previous velocity weight
                swarm.setParticleIncrement(personalWeight); // Personal best weight
                swarm.setGlobalIncrement(globalWeight); // Global best weight

		// Set limits to velocity value
		swarm.setMaxMinVelocity(maxMinVelocity);

		double minPositions[] = new double[numAtts+2];
		double maxPositions[] = new double[numAtts+2];
		int index;
		double maxD = 0;
		// We need to set the limits of the particle dimensions used to represent the center of the sphere. One attribute per dimension in the problem
		for(index=0;index<numAtts;index++) {
			Attribute att = Attributes.getAttribute(index);

			minPositions[index] = att.minAttribute();
			maxPositions[index] = att.maxAttribute();

			double size = att.maxAttribute()-att.minAttribute();
			if(size>maxD) {
				maxD = size;
			}
		}

		// The limits of the radius of the sphere
		minPositions[index] = 0;
		maxPositions[index++] = maxD;

		// The particle dimension that encodes for the class labels
		minPositions[index] = 0;
		maxPositions[index++] = Attributes.numClasses;

		swarm.setMinPosition(minPositions);
		swarm.setMaxPosition(maxPositions);

		// Optimize a few times
		for( int i = 0; i < numIters; i++ ) { 
			swarm.evolve();
			//System.out.println(swarm.toStringStats());
		}

		return mFF.buildClassifier(swarm.getBestPosition());
	}

}
