package au.edu.unimelb.plantcell.gwtphylo.server;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import au.edu.unimelb.plantcell.gwtphylo.shared.ConfigurationConstants;

/**
 * Sends clients a copy of the FASTA file (alignment) of the current tree. 
 * 
 * @author http://www.plantcell.unimelb.edu.au/bioinformatics
 *
 */
public class GetAlignmentServlet extends AbstractHttpServlet {
	private static final long serialVersionUID = -898375516378104977L;


	public GetAlignmentServlet() {
		super(ConfigurationConstants.ALIGNMENT_ROOT_FOLDER);
	}
	
	
	@Override
	protected void initHttpHeaders(final HttpServletResponse resp, final File out) {
		 resp.setContentType("text/plain");
		 resp.addHeader("Content-Type", "text/plain");
		 resp.addHeader("Content-Disposition", "attachment; filename=" + out.getName());
	}
	
	@Override
	public File findFileToSend(final Map<String,String> form_data) throws IOException {
		String superfamily = form_data.get(AbstractHttpServlet.FIELD_SUPERFAMILY);
		String category    = form_data.get(AbstractHttpServlet.FIELD_CATEGORY);
		String tree        = form_data.get(AbstractHttpServlet.FIELD_TREE).toLowerCase();
		
		/*
		 * since tree is the phyloxml filename, and includes .phyloxml (and other) extensions, we have to search the corresponding alignment folder
		 * to locate the underlying alignment for the tree. We catch weird conditions and throw here if we find something wrong.
		 */
		File superfamily_folder = new File(getBaseDirectory(), superfamily);
		validateFolder(superfamily_folder);
		File category_folder = new File(superfamily_folder, category);
		validateFolder(category_folder);
		
		File[] fasta_files = category_folder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File f) {
				String lower_name = f.getName().toLowerCase();
				if (lower_name.endsWith(".fasta") || lower_name.endsWith(".fa"))
					return true;
				return false;
			}
			
		});
		
		if (fasta_files.length < 1)
			throw new IOException("No matching FASTA files!");
		
		ArrayList<File> hits = new ArrayList<File>();
		for (File f : fasta_files) {
			String name = f.getName().toLowerCase().replaceAll("\\s", "_");
			if (tree.startsWith(name))
				hits.add(f);
		}
		
		if (hits.size() != 1)
			throw new IOException("Expected to find 1 alignment, but found: "+hits.size());
		
		return hits.get(0);
	}
	
	private void validateFolder(final File folder) throws IOException {
		if (folder == null || !folder.exists())
			throw new IOException("No such folder: "+folder.getAbsolutePath());
	}
}
