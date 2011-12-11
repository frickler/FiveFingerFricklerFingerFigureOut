package ch.frickler.biometrie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataListener;

import ch.frickler.biometrie.data.MinutiaNighbourPair;
import ch.frickler.biometrie.data.ResultsetByNearest;
import ch.frickler.biometrie.data.Template;
import ch.frickler.biometrie.data.TemplateFileParser;
import ch.frickler.biometrie.gui.FingerPrintFrame;
import ch.frickler.biometrie.gui.HistogrammFrame;

public class FiveFinger implements ComboBoxModel {

	private List<Template> templates;
	private int currentIndex = 0;

	private FingerPrintFrame fingerPrinter;
	private JTextArea resultArea;
	private JLabel pagination;
	private JLabel mouseInfo;
	private JLabel printerTitle;
	private JTextField textCurrent = new JTextField();
	
	// TODO: insert histogramm in main window
	private HistogrammFrame histogrammFrame = new HistogrammFrame("Histogramm");

	private int refTemplate = 60;

	public void initGui(int width, int height, List<Template> templates) {
		this.templates = templates;

		JFrame frame = new JFrame("Finger Matcher");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		JPanel panel = new JPanel(new BorderLayout());

		JPanel printPanel = new JPanel();
		printPanel.setLayout(new BorderLayout());
		printPanel.setBackground(Color.BLACK);
		printPanel.setBorder(new EmptyBorder(new Insets(20, 20, 20, 20)));
		printPanel.setSize((new Dimension(Math.max(600, width), height + 40)));

		printerTitle = new JLabel();
		printerTitle.setForeground(Color.WHITE);

		fingerPrinter = new FingerPrintFrame();
		fingerPrinter.setBackground(Color.WHITE);
		fingerPrinter.setPreferredSize(new Dimension(width, height));
		fingerPrinter.addMouseMotionListener(new MouseMotionListener() {

			public void mouseMoved(MouseEvent e) {
				mouseInfo.setText(String.format("x: %d / y: %d", e.getX(),
						fingerPrinter.getHeight() - e.getY()));
			}

			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		mouseInfo = new JLabel();
		mouseInfo.setForeground(Color.WHITE);

		printPanel.add(printerTitle, BorderLayout.NORTH);
		printPanel.add(fingerPrinter, BorderLayout.CENTER);
		printPanel.add(mouseInfo, BorderLayout.SOUTH);
		panel.add(printPanel, BorderLayout.CENTER);

		// add buttons
		JPanel buttonPanel = new JPanel();
		JButton prevButton = new JButton("Prev");
		prevButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				printPrevTemplate();

			}
		});
		buttonPanel.add(new JLabel("Reference Template:"));
		JComboBox refTemplate = new JComboBox(this);
		buttonPanel.add(refTemplate);
		buttonPanel.add(prevButton);
		textCurrent.setSize(new Dimension(20, 20));
		
		textCurrent.setText(currentIndex+"");
		
		textCurrent.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					int id =  Integer.parseInt(textCurrent.getText());
					if(id >0 && id < FiveFinger.this.templates.size()){
					currentIndex = id;
					
					printFinger(currentIndex);
					}else{
						textCurrent.setText(currentIndex+"");
					}
				}catch(Exception ex){
					textCurrent.setText(currentIndex+"");
				}
				
			}
		});
		buttonPanel.add(textCurrent);
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
		
		JButton rotateButton = new JButton("Rotate");
		rotateButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				rotateTemplate();

			}
		});
		buttonPanel.add(rotateButton);

		panel.add(buttonPanel, BorderLayout.SOUTH);

		// add result area in a scroll pane
		resultArea = new JTextArea();
		JScrollPane sp = new JScrollPane(resultArea); 
		sp.setPreferredSize(new Dimension(300, 500));
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(sp, BorderLayout.EAST);

		frame.add(panel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		// init default with template
		printFinger(currentIndex);
	}

	private void updatePagination() {
		textCurrent.setText(currentIndex+"");
		pagination.setText(String.format(" from %d ",templates.size()-1));
	}

	private void printFinger(int index) {
		fingerPrinter.setTemplate(templates.get(index));
		printerTitle.setText(String.format("template index %d", index));
		updatePagination();
	}

	private void printNextTemplate() {
		if (currentIndex < templates.size() - 1) {
			printFinger(++currentIndex);
		}
		matchTemplate();
	}

	private void printPrevTemplate() {
		if (currentIndex > 0) {
			printFinger(--currentIndex);
		}
		matchTemplate();
	}
	
	private void rotateTemplate() {
		fingerPrinter.rotateTemplate();
	}

	private void matchTemplate() {
		
		ResultsetByNearest result = new ResultsetByNearest(templates.get(currentIndex));
		
		resultArea.setText(result.toStringByAngle(true));

		List<List<MinutiaNighbourPair>> mnp = new ArrayList<List<MinutiaNighbourPair>>();
		mnp.add(result.getPairs());
		if (fingerPrinter.getRefTemplate() != null){
			ResultsetByNearest rmp = new ResultsetByNearest(fingerPrinter.getRefTemplate());
			mnp.add(rmp.getPairs());
		}
		histogrammFrame.plotPairs(mnp);

		//frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);

		histogrammFrame.setVisible(true);
		histogrammFrame.repaint();
	}

	private static final String TEMPLATE_FILE = "res/test.txt";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		List<Template> templates = TemplateFileParser
				.parseTemplateFile(TEMPLATE_FILE);
		int width = 0;
		int height = 0;
		for (Template t : templates) {
			width = Math.max(width, t.getImageWidth());
			height = Math.max(height, t.getImageHeight());
		}
		FiveFinger application = new FiveFinger();
		application.initGui(width, height, templates);
	}

	@Override
	public int getSize() {
		return templates.size() + 1;
	}

	@Override
	public String getElementAt(int index) {
		if (index == 60) {
			return "NONE";
		} else {
			return Integer.toString(index);
		}
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		// who cares
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		// who cares
	}

	@Override
	public void setSelectedItem(Object anItem) {
		if (anItem.equals("NONE")) {
			this.refTemplate = 60;
			fingerPrinter.setReferenceTemplate(null);
		} else {
			this.refTemplate = Integer.parseInt((String) anItem);
			fingerPrinter.setReferenceTemplate(templates.get(refTemplate));
		}
	}

	@Override
	public Object getSelectedItem() {
		if (this.refTemplate == 60) {
			return "NONE";
		} else {
			return Integer.toString(this.refTemplate);
		}
	}
}
