import net.sourceforge.jswarm_pso.Particle;

public class MyParticle extends Particle {
	final static int size = Attributes.getNumAttributes()+2;

	public MyParticle() { 
		super(size); 
	}
} 

