package ch.frickler.biometrie.test;

import java.util.List;

import org.junit.Test;

import ch.frickler.biometrie.data.MinutiaPoint;
import ch.frickler.biometrie.data.Template;
import ch.frickler.biometrie.data.TemplateFileParser;

import junit.framework.TestCase;

public class TestParser extends TestCase {
	

	@Test
	public void testParsing() throws Exception {
		List<Template> templates = null;

		templates = TemplateFileParser.parseTemplateFile("res/test.txt");

		assertEquals(templates.size(), 60);
		
		Template template = templates.get(0);
		assertEquals(288,template.getImageWidth());
		assertEquals(384,template.getImageHeight());
		assertEquals(197,template.getxResolution());
		assertEquals(197,template.getyResolution());
		
		assertEquals(32, template.getMinutiaPoints().size());
		
		/*
	minType:            2
    xCoord:             32
    yCoord:             57
    minAngle:           169
    minQuality:         0
		 */
		
		MinutiaPoint point = template.getMinutiaPoints().get(0);
		assertEquals(2, point.getType());
		assertEquals(32, point.getxCoord());
		assertEquals(57, point.getyCoord());
		assertEquals(169, point.getAngle());
		assertEquals(0, point.getQuality());
		

		
	}
}
