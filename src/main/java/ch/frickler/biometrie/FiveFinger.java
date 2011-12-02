package ch.frickler.biometrie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import ch.frickler.biometrie.data.Template;
import ch.frickler.biometrie.data.TemplateFileParser;
import ch.frickler.biometrie.gui.FingerPrintPanel;


public class FiveFinger {

	private List<Template> templates;
	private int currentIndex = 0;
	
	private FingerPrintPanel fingerPrinter;
	private JTextArea resultArea;
	private JLabel pagination;
	
	public void initGui(int width,int height, List<Template>templates) {
		this.templates = templates;
		
		JFrame frame = new JFrame("Finger Matcher");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true); 
		
    	JPanel panel = new JPanel(new BorderLayout());
       
    	JPanel printPanel = new JPanel();
    	printPanel.setBackground(Color.gray);
    	printPanel.setBorder(new EmptyBorder(new Insets(20, 20, 20, 20)));
    	printPanel.setPreferredSize(new Dimension(Math.max(400, width),height + 40));
    	
    	fingerPrinter = new FingerPrintPanel();
    	fingerPrinter.setBackground(Color.WHITE);
        fingerPrinter.setPreferredSize(new Dimension(width, height));
        printPanel.add(fingerPrinter);
        panel.add(printPanel,BorderLayout.CENTER);
        //panel.setBorder(new EmptyBorder(new Insets(20, 20, 20, 20)));
        
        //add buttons
        JPanel buttonPanel = new JPanel();
        JButton prevButton = new JButton("Prev");
        prevButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				printPrevTemplate();
				
			}
		});
        buttonPanel.add(prevButton);
        
        pagination = new JLabel();
        
        buttonPanel.add(pagination);
        
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				printNextTemplate();
				
			}
		});
        buttonPanel.add(nextButton);
        
        JButton matchButton = new JButton("Match");
        matchButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				matchTemplate();
				
			}
		});
        buttonPanel.add(matchButton);
       
        
        panel.add(buttonPanel,BorderLayout.SOUTH);
        
        //add result area
        resultArea = new JTextArea();
        resultArea.setPreferredSize(new Dimension(300,500));
        panel.add(resultArea,BorderLayout.EAST);
        
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        //init default with template
        printFinger(currentIndex);
	}
	
	private void updatePagination() {
		pagination.setText(String.format(" %d from %d ",currentIndex + 1,templates.size()));
	}
	
	private void printFinger(int index) {
		fingerPrinter.setTemplate(templates.get(index));
		updatePagination();
	}
	
	private void printNextTemplate() {
		if (currentIndex < templates.size()-1) {
			printFinger(++currentIndex);
		}
	}
	
	private void printPrevTemplate() {
		if (currentIndex > 0) {
			printFinger(--currentIndex);
		}
	}
	
	private void matchTemplate() {
		//TODO create our matching algorithm, print result in textarea
		
		resultArea.setText("matching results:\n\n index 4 (rating 76)");
		
	}
	
	
	private static final String TEMPLATE_FILE = "res/test.txt";
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		List<Template> templates = TemplateFileParser.parseTemplateFile(TEMPLATE_FILE);
		int width = 0;
		int height = 0;
		for (Template t : templates) {
			width = Math.max(width, t.getImageWidth());
			height = Math.max(height, t.getImageHeight());
		}
		FiveFinger application = new FiveFinger();
		application.initGui(width, height, templates);
	}
}
