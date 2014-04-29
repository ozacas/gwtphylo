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
	public static final File PHYLOXML_ROOT_FOLDER  = new File(getDataRoot(), "1kp-phyloxml");
	
	/**
	 * Where are the alignments on the server? (used by the alignment servlet). This root folder must EXACTLY match
	 * the structure of the folders within {@link PHYLOXML_ROOT_FOLDER} or the alignment servlet wont be able to
	 * find the alignments which produced the trees. The folder MUST be outside the PHYLOXML_ROOT_FOLDER for correct operations.
	 */
	public static final File ALIGNMENT_ROOT_FOLDER = new File(getDataRoot(), "1kp-alignments");
	
	/**
	 * Where are the assembled (possibly partial) coding sequences located? (not currently used, but intended for the future)
	 */
	public static final File CDNA_ROOT_FOLDER = new File(getDataRoot(), "1kp-transcripts");
	
	/**
	 * We test for development folders first (which will fail on a production machine) and then
	 * finally for the production server folder to locate alignments, sequence databases and trees
	 * 
	 * @return file representing the root data folder or null if none can be found (unlikely)
	 */
	private static File getDataRoot() {
		String[] items = new String[] { "c:\\www\\", "/mnt/1kp/" };
		for (String item : items) {
			File f = new File(item);
			if (f.exists() && f.isDirectory())
				return f;
		}
		return null;
	}
}
