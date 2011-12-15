package ch.frickler.biometrie.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.frickler.biometrie.data.ResultsetByNearest;
import ch.frickler.biometrie.data.Template;
import ch.frickler.biometrie.data.TemplateFileParser;

public class ConsoleTestResultsetByNearest {

	/**
	 * @param args
	 */
	
	private static final String TEMPLATE_FILE = "res/test.txt";
	
	public static void main(String[] args) throws IOException {
		
		List<Template> templates = TemplateFileParser
				.parseTemplateFile(TEMPLATE_FILE);
		int width = 0;
		int height = 0;
		List<int[]> histograms = new ArrayList<int[]>();
		 /*
		FileWriter fstream = new FileWriter("c:\\\\temp\\fivefingerfrckleroutput.txt");
		  BufferedWriter out = new BufferedWriter(fstream);
		  
		  //Close the output stream
		
		  
		for (Template t : templates) {
				ResultsetByNearest near = new ResultsetByNearest(t);
				out.write(near.toStringByAngle(false));
				out.write("\n");
		}
		
		 out.close();

		
	*/	
		  for (Template t : templates) {
				ResultsetByNearest near = new ResultsetByNearest(t);
				HistogrammPanel frm = new HistogrammPanel();
				frm.getAngleHistogramm(near.getPairs());
				histograms.add(frm.getAngleHistogramm(near.getPairs()));
				//System.out.println("#");
		}
		  
	
		for (int i = 0;i<histograms.size();i++) {
			int[] histCurrent = histograms.get(i);
			for (int compare = 0;compare<histograms.size();compare++) {
				if(i == compare) // do not compare with himself 
					continue;
				int[] histCompare =  histograms.get(compare);
				
				float match = 0;
				
				for(int x = 0; x < histCompare.length ; x++){
				
					
					
					if(histCompare[x] == 0 && histCurrent[x] == 0){
						match += 1;
					}else if(histCompare[x] == 0 || histCurrent[x] == 0){
						// zero division what to do?
						match += 0.7;
					}else{					
						if(histCompare[x]<histCurrent[x]){
							match += histCompare[x]/histCurrent[x];
						}else{
							match += histCurrent[x]/histCompare[x];
						}
					}
				}
				float result = match / (float)histCompare.length;
				if(result > 0.8)
				System.out.println("Template "+i+" m�tscht zu "+result+" zu template "+compare);
			}  
		}  
		
	}

}
