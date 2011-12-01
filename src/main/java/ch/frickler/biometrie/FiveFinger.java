package ch.frickler.biometrie;

import java.util.List;

import javax.swing.JFrame;

import ch.frickler.biometrie.data.Template;
import ch.frickler.biometrie.data.TemplateFileParser;
import ch.frickler.biometrie.gui.FingerPrintFrame;


public class FiveFinger {

	private static final String TEMPLATE_FILE = "res/test.txt";
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		List<Template> templates = TemplateFileParser.parseTemplateFile(TEMPLATE_FILE);
		FingerPrintFrame frame = new FingerPrintFrame();
		frame.setTemplate(templates.get(0));
		
	}
}
