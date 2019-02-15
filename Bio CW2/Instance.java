import java.util.*;

public class Instance {
	int[] nominalValues;
	double []realValues;
	int instanceClass;
	boolean isTrain;
	int numAtt;
	
	public Instance(String def,boolean _isTrain) {
		StringTokenizer st=new StringTokenizer(def,",");
		numAtt = Attributes.getNumAttributes()+1;
		nominalValues=new int[numAtt];
		realValues=new double[numAtt];
		isTrain=_isTrain;

		int attributeCount=0;
		while (st.hasMoreTokens()) {
			if(attributeCount>numAtt) {
				System.out.println("Instance "+def+" has more attributes than defined "+numAtt+"<->"+attributeCount);
				System.exit(1);
			}
			String att=st.nextToken();

			if(Attributes.getAttribute(attributeCount).getType()==Attribute.REAL) {
				try {
					realValues[attributeCount]=Double.parseDouble(att);
				} catch(NumberFormatException e) {
					System.out.println("Attribute "+attributeCount+" of "+def+" is not a real value");
					e.printStackTrace();
					System.exit(1);
				}
			} else if(Attributes.getAttribute(attributeCount).getType()==Attribute.NOMINAL) {
				nominalValues[attributeCount]=Attributes.getAttribute(attributeCount).convertNominalValue(att);
				if(nominalValues[attributeCount]==-1) {
					System.out.println("Attribute "+attributeCount+" of "+def+" is not a valid nominal value");
					System.exit(1);
				}
			}
			attributeCount++;
		}

		if(attributeCount!=numAtt) {
			System.out.println("Instance "+def+" has less attributes than defined");
			System.exit(1);
		}

		instanceClass=nominalValues[numAtt-1];

		double []newRealValues = new double[numAtt-1];
		int []newNominalValues = new int[numAtt-1];
		numAtt--;
		for(int i=0;i<numAtt;i++) {
			newRealValues[i] = realValues[i];
			newNominalValues[i] = nominalValues[i];
		}
		realValues = newRealValues;
		nominalValues = newNominalValues;
		
		if(isTrain) {
			for(int i=0;i<numAtt;i++) {
				if(Attributes.getAttribute(i).getType()==Attribute.REAL) {
					Attributes.getAttribute(i).enlargeBounds(realValues[i]);
				}
			}
		}
	}

	public double getAttribute(int attr) {
		return realValues[attr];
	}

	public double[] getAttributes() {
		return realValues;
	}

	public int getClassValue() {
		return instanceClass;
	}

	public String toString() {
		String ins="";
		for(int i=0;i<numAtt-1;i++) {
			if(Attributes.getAttribute(i).getType()
				== Attribute.REAL) {
				ins+=realValues[i]+",";
			} else if(Attributes.getAttribute(i).getType()
				== Attribute.NOMINAL) {
				ins+=nominalValues[i]+",";
			}
		}
		ins+=instanceClass;
		return ins;
	}
}
