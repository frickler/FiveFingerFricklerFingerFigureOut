package ch.frickler.biometrie.gui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
		
		FileWriter fstream = new FileWriter("c:\\\\temp\\fivefingerfrckleroutput.txt");
		  BufferedWriter out = new BufferedWriter(fstream);
		  
		  //Close the output stream
		 
		  
		for (Template t : templates) {
				ResultsetByNearest near = new ResultsetByNearest(t);
				out.write(near.toStringByAngle(false));
				out.write("\n");
		}
		
		 out.close();

	}

}
