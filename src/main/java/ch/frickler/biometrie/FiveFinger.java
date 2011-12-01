package ch.frickler.biometrie;

import ch.frickler.biometrie.data.TemplateFileParser;


public class FiveFinger {

	private static final String TEMPLATE_FILE = "res/test.txt";
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		TemplateFileParser.parseTemplateFile(TEMPLATE_FILE);

	}
}
