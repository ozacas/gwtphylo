package au.edu.unimelb.plantcell.gwtphylo.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Tree items for phyloxml documents. The userobject holds the original name as supplied by the server, the
 * getText() method returns a "cleaned up" and abbreviated name suitable for display in the tree
 * 
 * @author http://www.plantcell.unimelb.edu.au/bioinformatics
 *
 */
public class PhyloXMLTreeItem extends TreeItem {
	private static final Pattern alignment_prog_match = Pattern.compile("_?(?:mafft)|(?:muscle)_?");
	
	public PhyloXMLTreeItem(final String name) {
		super(cleanSafeName(name));
		setUserObject(name);		// this must be set 
	}
	
	private static SafeHtml cleanSafeName(final String name) {
		assert(name != null);
		String clean_name = remove_extension(name, ".phyloxml");
		clean_name        = remove_extension(clean_name, ".newick");
		clean_name        = remove_extension(clean_name, ".fasta_");
		clean_name        = remove_extension(clean_name, ".fasta");
		clean_name        = remove_aligner(clean_name);
		return SafeHtmlUtils.fromString(clean_name);
	}
	
	private static String remove_extension(final String name, final String ext) {
		assert(name != null);
		if (name.endsWith(ext)) 
			return name.substring(0, name.length() - ext.length());
		return name;
	}
	
	private static String remove_aligner(final String name) {
		assert(name != null);
		Matcher m = alignment_prog_match.matcher(name);
		if (m.find()) {
			return name.replaceAll(alignment_prog_match.pattern(), "");
		}
		return name;
	}
}
