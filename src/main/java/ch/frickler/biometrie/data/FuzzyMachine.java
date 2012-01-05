package ch.frickler.biometrie.data;

public class FuzzyMachine {

	public static double geneateFuzzyValue(double[] weight, double[] scores) throws FuzzyMachineException {
		
		if(weight == null || scores == null)
			throw new FuzzyMachineException("weight and socre cannot be null.");
		
		if(weight.length != scores.length)
			throw new FuzzyMachineException("weight and socre must be of the same length.");
		double sumweight = 0;
		for(double d : weight){
			sumweight += d;
		}
		if(sumweight != 1){
			throw new FuzzyMachineException("weight must be 1 in total.");
		}
		for(double d : scores){
			if(d < 0 || d > 1){
				throw new FuzzyMachineException("a score value must be between 0 and 1");
			}
		}
		double fuzzyValue = 0;
		for(int i = 0; i < scores.length;i++){
			fuzzyValue += scores[i]*weight[i];
		}
		return fuzzyValue;
	}
	


}


