/** ComparePaintings.java
 *  a class for  .
 * 
 * by Baihan Lin
 * for CSE 373 Assignment 3, Autumn, 2016.
 * Section BC. 
 * 
 * @author Baihan Lin  < sunnylin@uw.edu >
 * @version 	1.2
 * @since 		1.0 2016-10-25
 *
 */

public class ComparePaintings {

	// Fields
	private ColorHash ch;		// ColorHash
	private FeatureVector fv;	// FeatureVector
	private long sumCol;		// sum of collisions
	private String pm;			// probing method

	// Constants
	private static final int DEFAULT_BPP = 6;		// default bits Per Pixel
	private static final int DEFAULT_TBS = 11;		// default table size
	private static final double DEFAULT_RLF = 0.4;	// default rehash load factor
	public static final String IMG_01 = "MonaLisa.jpg"; 
	public static final String IMG_02 = "StarryNight.jpg"; 
	public static final String IMG_03 = "ChristinasWorld.jpg"; 
	public static final String IMG_04 = "WaterLilies.jpg"; 
	public static final String IMG_05 = "ParisAutumn.jpg"; 
	public static final String IMG_06 = "ButterflyShip.jpg"; 
	public static final String IMG_07 = "Kiss.jpg"; 
	public static final String IMG_08 = "StJeromeReading.jpg"; 
	public static final String IMG_09 = "CityRises.jpg"; 
	public static final String IMG_10 = "Untitled.jpg"; 


	// constructor.
	public ComparePaintings(){
		pm = ColorHash.LINEAR_PROBING;
		sumCol = 0;
	}; 

	// Load the image, construct the hash table, count the colors.
	ColorHash countColors(String filename, int bitsPerPixel) throws Exception {

		sumCol = 0;
		ImageLoader il = new ImageLoader(filename);
		ResponseItem ri = null;
		ch = new ColorHash(DEFAULT_TBS, bitsPerPixel, pm, DEFAULT_RLF);
		for (int x = 0; x < il.getWidth(); x++) {
			for (int y = 0; y < il.getHeight(); y++) {
				ri = ch.increment(il.getColorKey(x, y, bitsPerPixel));
				sumCol += ri.nCollisions;
			}
		}
		fv = new FeatureVector(bitsPerPixel);
		fv.getTheCounts(ch);
		return ch;
	}

	//	Starting with two hash tables of color counts, compute a measure of similarity based on the cosine distance of two vectors.
	double compare(ColorHash painting1, ColorHash painting2) throws Exception {
		FeatureVector fv1 = new FeatureVector(DEFAULT_BPP);
		FeatureVector fv2 = new FeatureVector(DEFAULT_BPP);
		fv1.getTheCounts(painting1);
		fv2.getTheCounts(painting2);
		double cosSim = fv1.cosineSimilarity(fv2);
		return cosSim;
	}

	//	A basic test for the compare method: S(x,x) should be 1.0, so you should compute the similarity of an image with itself and print out the answer. If it comes out to be 1.0, that is a good sign for your implementation so far.
	void basicTest(String filename) throws Exception {
		ComparePaintings cp2 = new ComparePaintings();
		cp2.countColors(filename, DEFAULT_BPP);
		System.out.print("Cosine Similarity of the two images is ");
		System.out.println(fv.cosineSimilarity(cp2.fv));		
	}

	//		Using the three given painting images and a variety of bits-per-pixel values, compute and print out a table of collision counts in the following format:
	void CollisionTests() throws Exception {

		System.out.println("Bits Per Pixel   C(Mona,linear)  C(Mona,quadratic)  C(Starry,linear) "
				+ "C(Starry,quadratic) C(Christina,linear) C(Christina,quadratic)");
		ComparePaintings cp01 = new ComparePaintings();
		ComparePaintings cp02 = new ComparePaintings();
		ComparePaintings cp03 = new ComparePaintings();
		for (int bpp = 24; bpp >= 3; bpp-=3) {
			
			System.out.format("%-17d", bpp);
			cp01.pm = ColorHash.LINEAR_PROBING;
			cp01.countColors(IMG_01, bpp);
			System.out.format("%-16d", cp01.sumCol);
			cp01.pm = ColorHash.QUADRATIC_PROBING;
			cp01.countColors(IMG_01, bpp);
			System.out.format("%-19d", cp01.sumCol);
			
			cp02.pm = ColorHash.LINEAR_PROBING;
			cp02.countColors(IMG_02, bpp);
			System.out.format("%-17d", cp02.sumCol);
			cp02.pm = ColorHash.QUADRATIC_PROBING;
			cp02.countColors(IMG_02, bpp);
			System.out.format("%-20d", cp02.sumCol);
			
			cp03.pm = ColorHash.LINEAR_PROBING;
			cp03.countColors(IMG_03, bpp);
			System.out.format("%-20d", cp03.sumCol);
			cp03.pm = ColorHash.QUADRATIC_PROBING;
			cp03.countColors(IMG_03, bpp);
			System.out.format("%-22d%n", cp03.sumCol);
		}
	}

	void fullSimilarityTests() {
		// TODO Auto-generated method stub

	}

	void extraCredit10ImagesTest() {
		// TODO Auto-generated method stub

	}

	// This simply checks that the images can be loaded, so you don't have 
	// an issue with missing files or bad paths.
	void imageLoadingTest() {
		ImageLoader mona = new ImageLoader("MonaLisa.jpg");
		ImageLoader starry = new ImageLoader("StarryNight.jpg");
		ImageLoader christina = new ImageLoader("ChristinasWorld.jpg");
		System.out.println("It looks like we have successfully loaded all three test images.");

	}

	/**
	 * This is a basic testing function, and can be changed.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ComparePaintings cp = new ComparePaintings();
		cp.imageLoadingTest();
		//		String filename = ".jpg";
		//		cp.basicTest(filename);
		cp.fullSimilarityTests();
		cp.CollisionTests();
		cp.extraCredit10ImagesTest();
		ComparePaintings cp1 = new ComparePaintings();


	}

}
