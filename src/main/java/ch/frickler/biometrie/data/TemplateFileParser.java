package ch.frickler.biometrie.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TemplateFileParser {
	
	private final static String TEMPLATE_TAG = "RecordHeader of Isotemplate";
	private final static String TEMPLATE_IMAGE_WIDTH = "Image Width";
	private final static String TEMPLATE_IMAGE_HEIGHT = "Image Height";
	private final static String TEMPLATE_X_RESOULTION = "X Resolution";
	private final static String TEMPLATE_Y_RESOLUTION = "Y Resolution";
	
	private final static String MINUTIA_INDEX = "MinutiaIndex";
	private final static String MINUTIA_TYPE = "minType";
	private final static String MINUTIA_X_COORD = "xCoord";
	private final static String MINUTIA_Y_COORD = "yCoord";
	private final static String MINUTIA_ANGLE = "minAngle";
	private final static String MINUTIA_QUALITY = "minQuality";
	
	
	public static List<Template> parseTemplateFile(String templateFilePath) throws IOException {
		List<Template> templates = new ArrayList<Template>();
		BufferedReader br = new BufferedReader(new FileReader(templateFilePath));
		
		String line = null;
		Template template = null;
		MinutiaPoint minutiaPoint = null;
		
		while ((line = br.readLine()) != null)   {
			if (line.contains(TEMPLATE_TAG)) {
				System.out.println("new template: "+line);
				template = new Template();
				templates.add(template);
			}
			
			if (line.contains(TEMPLATE_IMAGE_WIDTH)) {
				template.setImageWidth(getIntValue(line));
			}
			
			if (line.contains(TEMPLATE_IMAGE_HEIGHT)) {
				template.setImageHeight(getIntValue(line));
			}
			
			if (line.contains(TEMPLATE_X_RESOULTION)) {
				template.setxResolution(getIntValue(line));
			}
			
			if (line.contains(TEMPLATE_Y_RESOLUTION)) {
				template.setyResolution(getIntValue(line));
			}
			
			if (line.contains(MINUTIA_INDEX)) {
				minutiaPoint = new MinutiaPoint();
				template.addMinutiaPoint(minutiaPoint);
			}
			
			if (line.contains(MINUTIA_TYPE)) {
				minutiaPoint.setType(getIntValue(line));
			}
			
			if (line.contains(MINUTIA_X_COORD)) {
				minutiaPoint.setxCoord(getIntValue(line));
			}
			
			if (line.contains(MINUTIA_Y_COORD)) {
				minutiaPoint.setyCoord(getIntValue(line));
			}
			
			if (line.contains(MINUTIA_ANGLE)) {
				minutiaPoint.setAngle(getIntValue(line));
			}
			
			if (line.contains(MINUTIA_QUALITY)) {
				minutiaPoint.setQuality(getIntValue(line));
			}
		  }
		
		return templates;
	}
	
	private static int getIntValue(String line) {
		String numberString = line.split(":")[1].trim();
		int number = Integer.parseInt(numberString);
		return number;
	}
	
}
