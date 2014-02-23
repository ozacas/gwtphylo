package au.edu.unimelb.plantcell.gwtphylo.shared;

import java.io.File;

/**
 * Public constants which must be configured correctly for a production site. Global in scope. Really need to move this
 * into an operations config file...
 * 
 * @author http://www.plantcell.unimelb.edu.au/bioinformatics
 *
 */
public class ConfigurationConstants {
	/**
	 * Testing and production settings, must end in file path separator
	 */
	//private final static String DATA_ROOT = "c:/www/";		// for testing only
	private final static String DATA_ROOT = "/mnt/1kp/";	// REQUIRED for production
	
	/**
	 * Where are the trees on the server? (used by the phyloxml servlet). This folder must have the following structure:
	 * PHYLOXML_ROOT_FOLDER/
	 *    - superfamily1
	 *    	- category1
	 *    		- tree1.phyloxml
	 *    	- category2
	 *    		- tree2.phyloxml
	 *    		- tree3.phyloxml
	 *    - superfamily2
	 *    ....
	 *    
	 *    In other words the website will display (a modified) form of this file structure for the user to select.
	 */
	public static final File PHYLOXML_ROOT_FOLDER  = new File(DATA_ROOT + "1kp-phyloxml");
	
	/**
	 * Where are the alignments on the server? (used by the alignment servlet). This root folder must EXACTLY match
	 * the structure of the folders within {@link PHYLOXML_ROOT_FOLDER} or the alignment servlet wont be able to
	 * find the alignments which produced the trees. The folder MUST be outside the PHYLOXML_ROOT_FOLDER for correct operations.
	 */
	public static final File ALIGNMENT_ROOT_FOLDER = new File(DATA_ROOT + "1kp-alignments");
}
