/**
 * ImageComponents.java
 * UF Solution by 
 * @author  Baihan Lin, 1360521, <sunnylin@uw.edu>.
 * @date    Nov 25 2016
 * @version 1.3
 * 
 * CSE 373, University of Washington, Autumn 2016.
 * 
 * Based on Starter Code for CSE 373 Optional Assignment UF, Part II.    
 * Starter Code Version 1.
 * S. Tanimoto
 * 
 */ 

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ByteLookupTable;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageComponents extends JFrame implements ActionListener {
	public static ImageComponents appInstance; // Used in main().

	String startingImage = "gettysburg-address-p1.png";
	BufferedImage biTemp, biWorking, biFiltered; // These hold arrays of pixels.
	Graphics gOrig, gWorking; // Used to access the drawImage method.
	int w; // width of the current image.
	int h; // height of the current image.
	int nUF; // number of union-find operations performed

	int[][] parentID; // For your forest of up-trees.

	
	/** TODO
	 * Public Method to find in UNION-Find
	 * @param pixelID to start with
	 * @return pixelID parent up the entire tree
	 */
	int find(int pixelID) {

		int x = getXcoord(pixelID);
		int y = getYcoord(pixelID);

		while(parentID[y][x] >= 0){
			pixelID = parentID[y][x];
			x = getXcoord(pixelID);
			y = getYcoord(pixelID);
		}

		return pixelID;

	}         

	/** TODO
	 * Public Method to union in UNION-Find
	 * @param two pixelID to union
	 * @return nothing, but the two pixelIDs are unioned
	 */
	void union(int pixelID1, int pixelID2) {

		pixelID1 = find(pixelID1);
		pixelID2 = find(pixelID2);

		int x1 = getXcoord(pixelID1);
		int y1 = getYcoord(pixelID1);

		int x2 = getXcoord(pixelID2);
		int y2 = getYcoord(pixelID2);

		if (pixelID1 < pixelID2) {
			parentID[y2][x2] = pixelID1;
		} else {
			parentID[y1][x1] = pixelID2;
		}
		this.nUF++;

	}  

	/** TODO
	 * Private Method to get X coordinate
	 * @param  pixelID
	 * @return X coordinate
	 */
	private int getXcoord(int pixelID) {
		return pixelID % this.w;
	}

	/** TODO
	 * Private Method to get Y coordinate
	 * @param  pixelID
	 * @return Y coordinate
	 */
	private int getYcoord(int pixelID) {
		return pixelID / this.w;
	}

	JPanel viewPanel; // Where the image will be painted.
	JPopupMenu popup;
	JMenuBar menuBar;
	JMenu fileMenu, imageOpMenu, ccMenu, helpMenu;
	JMenuItem loadImageItem, saveAsItem, exitItem;
	JMenuItem lowPassItem, highPassItem, photoNegItem, RGBThreshItem;

	JMenuItem CCItem1;
	JMenuItem aboutItem, helpItem;

	JFileChooser fileChooser; // For loading and saving images.

	public class Color {
		int r, g, b;

		Color(int r, int g, int b) {
			this.r = r; this.g = g; this.b = b;    		
		}

		/** TODO
		 * Public Method to get Euclidean Distance of two colors
		 * @param  color c2
		 * @return Euclidean Distance of two colors
		 */
		double euclideanDistance(Color c2) {

			int diffR = (this.r - c2.r) * (this.r - c2.r);
			int diffG = (this.g - c2.g) * (this.g - c2.g);
			int diffB = (this.b - c2.b) * (this.b - c2.b);

			return Math.sqrt(diffR + diffG + diffB);

		}
	}

	// Some image manipulation data definitions that won't change...
	static LookupOp PHOTONEG_OP, RGBTHRESH_OP;
	static ConvolveOp LOWPASS_OP, HIGHPASS_OP;

	public static final float[] SHARPENING_KERNEL = { // sharpening filter kernel
			0.f, -1.f,  0.f,
			-1.f,  5.f, -1.f,
			0.f, -1.f,  0.f
	};

	public static final float[] BLURRING_KERNEL = {
			0.1f, 0.1f, 0.1f,    // low-pass filter kernel
			0.1f, 0.2f, 0.1f,
			0.1f, 0.1f, 0.1f
	};

	public ImageComponents() { // Constructor for the application.

		setTitle("Image Analyzer"); 
		addWindowListener(new WindowAdapter() { // Handle any window close-box clicks.
			public void windowClosing(WindowEvent e) {System.exit(0);}
		});

		// Create the panel for showing the current image, and override its
		// default paint method to call our paintPanel method to draw the image.
		viewPanel = new JPanel(){public void paint(Graphics g) { paintPanel(g);}};
		add("Center", viewPanel); // Put it smack dab in the middle of the JFrame.

		// Create standard menu bar
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		fileMenu = new JMenu("File");
		imageOpMenu = new JMenu("Image Operations");
		ccMenu = new JMenu("Connected Components");
		helpMenu = new JMenu("Help");
		menuBar.add(fileMenu);
		menuBar.add(imageOpMenu);
		menuBar.add(ccMenu);
		menuBar.add(helpMenu);

		// Create the File menu's menu items.
		loadImageItem = new JMenuItem("Load image...");
		loadImageItem.addActionListener(this);
		fileMenu.add(loadImageItem);
		saveAsItem = new JMenuItem("Save as full-color PNG");
		saveAsItem.addActionListener(this);
		fileMenu.add(saveAsItem);
		exitItem = new JMenuItem("Quit");
		exitItem.addActionListener(this);
		fileMenu.add(exitItem);

		// Create the Image Operation menu items.
		lowPassItem = new JMenuItem("Convolve with blurring kernel");
		lowPassItem.addActionListener(this);
		imageOpMenu.add(lowPassItem);
		highPassItem = new JMenuItem("Convolve with sharpening kernel");
		highPassItem.addActionListener(this);
		imageOpMenu.add(highPassItem);
		photoNegItem = new JMenuItem("Photonegative");
		photoNegItem.addActionListener(this);
		imageOpMenu.add(photoNegItem);
		RGBThreshItem = new JMenuItem("RGB Thresholds at 128");
		RGBThreshItem.addActionListener(this);
		imageOpMenu.add(RGBThreshItem);

		// Create CC menu stuff.
		CCItem1 = new JMenuItem("Compute Connected Components and Recolor");
		CCItem1.addActionListener(this);
		ccMenu.add(CCItem1);

		// Create the Help menu's item.
		aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(this);
		helpMenu.add(aboutItem);
		helpItem = new JMenuItem("Help");
		helpItem.addActionListener(this);
		helpMenu.add(helpItem);

		// Initialize the image operators, if this is the first call to the constructor:
		if (PHOTONEG_OP==null) {
			byte[] lut = new byte[256];
			for (int j=0; j<256; j++) {
				lut[j] = (byte)(256-j); 
			}
			ByteLookupTable blut = new ByteLookupTable(0, lut); 
			PHOTONEG_OP = new LookupOp(blut, null);
		}
		if (RGBTHRESH_OP==null) {
			byte[] lut = new byte[256];
			for (int j=0; j<256; j++) {
				lut[j] = (byte)(j < 128 ? 0: 200);
			}
			ByteLookupTable blut = new ByteLookupTable(0, lut); 
			RGBTHRESH_OP = new LookupOp(blut, null);
		}
		if (LOWPASS_OP==null) {
			float[] data = BLURRING_KERNEL;
			LOWPASS_OP = new ConvolveOp(new Kernel(3, 3, data),
					ConvolveOp.EDGE_NO_OP,
					null);
		}
		if (HIGHPASS_OP==null) {
			float[] data = SHARPENING_KERNEL;
			HIGHPASS_OP = new ConvolveOp(new Kernel(3, 3, data),
					ConvolveOp.EDGE_NO_OP,
					null);
		}
		loadImage(startingImage); // Read in the pre-selected starting image.
		setVisible(true); // Display it.

		// create a collection of trees 
		parentID = new int[this.h][this.w];

		for (int i = 0; i < this.h; i++) {
			for (int j = 0; j < this.w; j++) {
				parentID[i][j] = -1; // with -1 as all its elements
			}
		}

	}

	/*
	 * Given a path to a file on the file system, try to load in the file
	 * as an image.  If that works, replace any current image by the new one.
	 * Re-make the biFiltered buffered image, too, because its size probably
	 * needs to be different to match that of the new image.
	 */
	public void loadImage(String filename) {
		try {
			biTemp = ImageIO.read(new File(filename));
			w = biTemp.getWidth();
			h = biTemp.getHeight();
			viewPanel.setSize(w,h);
			biWorking = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			gWorking = biWorking.getGraphics();
			gWorking.drawImage(biTemp, 0, 0, null);
			biFiltered = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			pack(); // Lay out the JFrame and set its size.
			repaint();
		} catch (IOException e) {
			System.out.println("Image could not be read: "+filename);
			System.exit(1);
		}
	}

	/* Menu handlers
	 */
	void handleFileMenu(JMenuItem mi){
		System.out.println("A file menu item was selected.");
		if (mi==loadImageItem) {
			File loadFile = new File("image-to-load.png");
			if (fileChooser==null) {
				fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(loadFile);
				fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", new String[] { "JPG", "JPEG", "GIF", "PNG" }));
			}
			int rval = fileChooser.showOpenDialog(this);
			if (rval == JFileChooser.APPROVE_OPTION) {
				loadFile = fileChooser.getSelectedFile();
				loadImage(loadFile.getPath());
			}
		}
		if (mi==saveAsItem) {
			File saveFile = new File("savedimage.png");
			fileChooser = new JFileChooser();
			fileChooser.setSelectedFile(saveFile);
			int rval = fileChooser.showSaveDialog(this);
			if (rval == JFileChooser.APPROVE_OPTION) {
				saveFile = fileChooser.getSelectedFile();
				// Save the current image in PNG format, to a file.
				try {
					ImageIO.write(biWorking, "png", saveFile);
				} catch (IOException ex) {
					System.out.println("There was some problem saving the image.");
				}
			}
		}
		if (mi==exitItem) { this.setVisible(false); System.exit(0); }
	}

	void handleEditMenu(JMenuItem mi){
		System.out.println("An edit menu item was selected.");
	}

	void handleImageOpMenu(JMenuItem mi){
		System.out.println("An imageOp menu item was selected.");
		if (mi==lowPassItem) { applyOp(LOWPASS_OP); }
		else if (mi==highPassItem) { applyOp(HIGHPASS_OP); }
		else if (mi==photoNegItem) { applyOp(PHOTONEG_OP); }
		else if (mi==RGBThreshItem) { applyOp(RGBTHRESH_OP); }
		repaint();
	}

	void handleCCMenu(JMenuItem mi) {
		System.out.println("A connected components menu item was selected.");
		if (mi==CCItem1) { computeConnectedComponents(); }
	}
	void handleHelpMenu(JMenuItem mi){
		System.out.println("A help menu item was selected.");
		if (mi==aboutItem) {
			System.out.println("About: Well this is my program.");
			JOptionPane.showMessageDialog(this,
					"Image Components, Starter-Code Version.",
					"About",
					JOptionPane.PLAIN_MESSAGE);
		}
		else if (mi==helpItem) {
			System.out.println("In case of panic attack, select File: Quit.");
			JOptionPane.showMessageDialog(this,
					"To load a new image, choose File: Load image...\nFor anything else, just try different things.",
					"Help",
					JOptionPane.PLAIN_MESSAGE);
		}
	}

	/*
	 * Used by Swing to set the size of the JFrame when pack() is called.
	 */
	public Dimension getPreferredSize() {
		return new Dimension(w, h+50); // Leave some extra height for the menu bar.
	}

	public void paintPanel(Graphics g) {
		g.drawImage(biWorking, 0, 0, null);
	}

	public void applyOp(BufferedImageOp operation) {
		operation.filter(biWorking, biFiltered);
		gWorking.drawImage(biFiltered, 0, 0, null);
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource(); // What Swing object issued the event?
		if (obj instanceof JMenuItem) { // Was it a menu item?
			JMenuItem mi = (JMenuItem)obj; // Yes, cast it.
			JPopupMenu pum = (JPopupMenu)mi.getParent(); // Get the object it's a child of.
			JMenu m = (JMenu) pum.getInvoker(); // Get the menu from that (popup menu) object.
			//System.out.println("Selected from the menu: "+m.getText()); // Printing this is a debugging aid.

			if (m==fileMenu)    { handleFileMenu(mi);    return; }  // Handle the item depending on what menu it's from.
			if (m==imageOpMenu) { handleImageOpMenu(mi); return; }
			if (m==ccMenu)      { handleCCMenu(mi);      return; }
			if (m==helpMenu)    { handleHelpMenu(mi);    return; }
		} else {
			System.out.println("Unhandled ActionEvent: "+e.getActionCommand());
		}
	}

	// Use this to put color information into a pixel of a BufferedImage object.
	void putPixel(BufferedImage bi, int x, int y, int r, int g, int b) {
		int rgb = (r << 16) | (g << 8) | b; // pack 3 bytes into a word.
		bi.setRGB(x,  y, rgb);
	}

	/** TODO
	 * Public Method to compute number of connected trees and re-color the image
	 */
	void computeConnectedComponents() {
		this.nUF = 0;

		for (int j = 0; j < this.h; j++) {
			for (int i = 0; i < this.w; i++) {

				int curColor = biWorking.getRGB(i, j);
				int curPixelID = getPixelID(i, j);

				// check right pixel
				if (i + 1 < this.w) {
					if (curColor == biWorking.getRGB(i + 1, j)) {
						int rPixelID = getPixelID(i + 1, j);
						if (find(curPixelID) != find(rPixelID)) {
							union(curPixelID, rPixelID);
						}
					}      				
				}

				// check up pixel
				if (j + 1 < this.h) {
					if (curColor == biWorking.getRGB(i, j + 1)) {
						int uPixelID = getPixelID(i, j + 1);
						if (find(curPixelID) != find(uPixelID)) {
							union(curPixelID, uPixelID);
						}
					}
				}

			}
		}

		System.out.println("The number of times that the method UNION was called for this image is: " + this.nUF);

		// count number of components & re-color image based on connectivity
		int nComp = 0;
		Map<Integer, Integer> comps = new HashMap<Integer, Integer>();
		for (int j = 0; j < this.h; j++) {
			for (int i = 0; i < this.w; i++) {
				int pixelID = getPixelID(i, j);
				if (parentID[j][i] == -1) {
					comps.put(pixelID, nComp);
					nComp++;
				}		
				pixelID = find(pixelID);
				int k = (int) comps.get(pixelID);
				ProgressiveColors pc = new ProgressiveColors();
				int[] newColor = pc.progressiveColor(k);
				putPixel(biWorking, i, j, newColor[0], newColor[1], newColor[2]);
			}
		}

		System.out.println("The number of connected components in this image is: " + nComp);
		repaint();

		// re-initialize for next re-color 
		for (int j = 0; j < this.h; j++) {
			for (int i = 0; i < this.w; i++) {
				parentID[j][i] = -1;
			}
		}
	}

	/** TODO
	 * Private Method to get pixelID
	 * @param  x, y
	 * @return pixelID
	 */
	private int getPixelID(int x, int y) {
		return y * this.w + x;
	}

	/* This main method can be used to run the application. */
	public static void main(String s[]) {
		appInstance = new ImageComponents();
	}
}
