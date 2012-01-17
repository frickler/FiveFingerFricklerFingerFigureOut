package ch.frickler.biometrie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataListener;

import ch.frickler.biometrie.data.MinutiaNighbourPair;
import ch.frickler.biometrie.data.ResultsetByNearest;
import ch.frickler.biometrie.data.ResultsByTransformation;
import ch.frickler.biometrie.data.Template;
import ch.frickler.biometrie.data.TemplateFileParser;
import ch.frickler.biometrie.data.TheFiveFingerFricklerAlgorithm;
import ch.frickler.biometrie.gui.FingerPrintPanel;
import ch.frickler.biometrie.gui.HistogrammPanel;
import ch.frickler.biometrie.gui.MatchWithAllPopup;

public class FiveFinger implements ComboBoxModel {

	private static final String TEMPLATE_FILE = "res/test.txt";

	private List<Template> templates;
	private int currentIndex = 0;

	private FingerPrintPanel fingerPrintPanel;
	private HistogrammPanel histogrammPanel;
	private JTextArea resultArea;
	private JLabel pagination;
	private JLabel mouseInfo;
	private JLabel printerTitle;
	private JTextField textCurrent = new JTextField();
	private JFrame frame;
	private int refTemplate = 0;

	public void initGui(int width, int height, List<Template> templates) {
		this.templates = templates;
		this.refTemplate = getSize();
		frame = new JFrame("Finger Matcher");
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

		FingerPrintMouseHandler mh = new FingerPrintMouseHandler();
		fingerPrintPanel = new FingerPrintPanel();
		fingerPrintPanel.setBackground(Color.WHITE);
		fingerPrintPanel.setPreferredSize(new Dimension(width, height));
		fingerPrintPanel.addMouseListener(mh);
		fingerPrintPanel.addMouseMotionListener(mh);

		mouseInfo = new JLabel();
		mouseInfo.setForeground(Color.WHITE);

		printPanel.add(printerTitle, BorderLayout.NORTH);
		printPanel.add(fingerPrintPanel, BorderLayout.CENTER);
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

		textCurrent.setText(currentIndex + "");

		textCurrent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int id = Integer.parseInt(textCurrent.getText());
					if (id >= 0 && id < FiveFinger.this.templates.size()) {
						currentIndex = id;

						printFinger(currentIndex);
					} else {
						textCurrent.setText(currentIndex + "");
					}
				} catch (Exception ex) {
					textCurrent.setText(currentIndex + "");
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

		JButton matchWithAllButton = new JButton("MatchWithAll");
		matchWithAllButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				matchWithAll();
			}
		});
		buttonPanel.add(matchWithAllButton);

		panel.add(buttonPanel, BorderLayout.SOUTH);

		JPanel resultPanel = new JPanel(new BorderLayout());

		histogrammPanel = new HistogrammPanel();
		histogrammPanel.setBackground(Color.WHITE);
		histogrammPanel.setPreferredSize(new Dimension(500, height));
		resultPanel.add(histogrammPanel, BorderLayout.WEST);

		// add result area in a scroll pane
		resultArea = new JTextArea();
		JScrollPane sp = new JScrollPane(resultArea);
		sp.setPreferredSize(new Dimension(300, 500));
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		resultPanel.add(sp, BorderLayout.EAST);
		panel.add(resultPanel, BorderLayout.EAST);

		frame.add(panel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		frame.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				System.out.println("JFrame was resized");
				fingerPrintPanel.resize();
			}
		});

		// init default with template
		printFinger(currentIndex);
	}

	private void updatePagination() {
		textCurrent.setText(currentIndex + "");
		pagination.setText(String.format(" from %d ", templates.size() - 1));
	}

	private void printFinger(int index) {
		fingerPrintPanel.setTemplate(templates.get(index));
		printerTitle.setText(String.format("template index %d", index));
		updatePagination();

		redrawHistogramm();
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

	/**
	 * is called is prev next or match is pushed.
	 */
	private void matchTemplate() {

		ResultsetByNearest result = new ResultsetByNearest(
				templates.get(currentIndex));

		resultArea.setText(result.toStringByAngle(true));

		if (refTemplate < getSize()) {

			ResultsetByNearest referenceResults = new ResultsetByNearest(
					templates.get(refTemplate));

			ResultsByTransformation rc = new ResultsByTransformation(
					result.getPairs(), referenceResults.getPairs(),
					templates.get(refTemplate), templates.get(currentIndex),
					true);

			fingerPrintPanel.setTransformation(rc.getTransformation());

			System.out.println("-------");
			System.out.println("-- TransformationMatrix --");
			System.out.println("Compare template " + refTemplate + " with "
					+ currentIndex);
			if (rc != null) {
				rc.printTransformation();
				System.out.println("-------");
				System.out.println("Matchrate is in Percent: "
						+ rc.getMatchRate());
			}
		}

	}

	/**
	 * matches the reference Template with all available templates and displays
	 * them as a popup
	 */
	private void matchWithAll() {

		final Template curr = templates.get(currentIndex);
		final ResultsetByNearest currRes = new ResultsetByNearest(
				templates.get(currentIndex));

		int[] scores = new int[templates.size()];

		int t = -1;
		while (t < 0) {
			String threshold = JOptionPane
					.showInputDialog("Please Choose a Threshold between 0 and 100");
			try {
				t = Integer.parseInt(threshold);
			} catch (NumberFormatException e) {
				t = -1;
			}
		}

		for (int i = 0; i < templates.size(); i++) {
			ResultsetByNearest other = new ResultsetByNearest(templates.get(i));
			ResultsByTransformation tr = new ResultsByTransformation(
					currRes.getPairs(), other.getPairs(), curr,
					templates.get(i));
			// TODO why do I have to call getTransformation() to get a
			// matchrate?
			tr.getTransformation();
			scores[i] = tr.getMatchRate();
		}

		System.out.println("Above the threshold " + t
				+ " are the following templates:");
		for (int i = 0; i < scores.length; i++) {
			if (scores[i] >= t)
				System.out.println("Template #" + i + " with a score of "
						+ scores[i]);
		}

		// display them
		new MatchWithAllPopup(currentIndex, scores, t);
	}

	private void redrawHistogramm() {
		ResultsetByNearest result = new ResultsetByNearest(
				templates.get(currentIndex));

		resultArea.setText(result.toStringByAngle(true));
		if (fingerPrintPanel.getRefTemplate() != null) {
			ResultsetByNearest rmp = new ResultsetByNearest(
					fingerPrintPanel.getRefTemplate());
			histogrammPanel.plotPairs(result.getPairs(), rmp.getPairs());
		} else {
			histogrammPanel.plotPairs(result.getPairs());
		}
	}

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

		TheFiveFingerFricklerAlgorithm tFFFA = new TheFiveFingerFricklerAlgorithm(
				templates);
		tFFFA.process();
	}

	@Override
	public int getSize() {
		return templates.size() + 1;
	}

	@Override
	public String getElementAt(int index) {
		if (templates.size() <= index) {
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
			this.refTemplate = getSize();
			fingerPrintPanel.setReferenceTemplate(null);
		} else {
			this.refTemplate = Integer.parseInt((String) anItem);
			fingerPrintPanel.setReferenceTemplate(templates.get(refTemplate));
		}
		redrawHistogramm();
	}

	@Override
	public Object getSelectedItem() {
		if (this.refTemplate == getSize()) {
			return "NONE";
		} else {
			return Integer.toString(this.refTemplate);
		}
	}

	private class FingerPrintMouseHandler implements MouseMotionListener,
			MouseListener {

		private int buttonClicked = -1; // 3 = right click, 1 = left click -1 =
										// no click
		private int lastX;
		private int lastY;

		public void mouseMoved(MouseEvent e) {
			mouseInfo.setText(String.format("x: %d / y: %d", e.getX(),
					fingerPrintPanel.getHeight() - e.getY()));
		}

		public void mouseDragged(MouseEvent e) {
			int deltaX = e.getX() - lastX;
			int deltaY = e.getY() - lastY;
			if (buttonClicked == 1) {
				fingerPrintPanel.calculateTransformation(0.0, deltaX, -deltaY);

			} else if (buttonClicked == 3) {
				if (deltaY < 0)
					fingerPrintPanel.calculateTransformation(0.05, 0, 0);
				else if (deltaY > 0)
					fingerPrintPanel.calculateTransformation(-0.05, 0, 0);

			}
			fingerPrintPanel.repaint();
			lastX = e.getX();
			lastY = e.getY();
		}

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			buttonClicked = e.getButton();
			lastX = e.getX();
			lastY = e.getY();

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			buttonClicked = -1;
		}

	}
}
