package au.edu.unimelb.plantcell.gwtphylo.server;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import au.edu.unimelb.plantcell.gwtphylo.client.OneKPService;
import au.edu.unimelb.plantcell.gwtphylo.shared.ConfigurationConstants;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class OneKPServiceImpl extends RemoteServiceServlet implements OneKPService {
	public class SubfolderFilter implements FileFilter {

		@Override
		public boolean accept(File f) {
			if (f != null && f.isDirectory() && f.canRead())
				return true;
			return false;
		}

	}

	/*
	 * where phyloxml data is located
	 */
	private final File onekp_phyloxml_folder = ConfigurationConstants.PHYLOXML_ROOT_FOLDER;
	
	/* list of gene superfamilies which we present the 1kp data for */
	public String[] getSuperfamilies() {
		File[] subfiles = onekp_phyloxml_folder.listFiles(new SubfolderFilter());
		
		return asFilenameArray(subfiles);
	}

	public String[] asFilenameArray(final File[] col) {
		ArrayList<String> ret = new ArrayList<String>();
		if (col != null) {
			for (File f : col) {
				ret.add(f.getName());
			}
		}
		return ret.toArray(new String[0]);
	}
	
	public String[] getCategories(String superfamily) {
		File    sf_folder= new File(onekp_phyloxml_folder, File.separatorChar+superfamily);
		File[] categories= sf_folder.listFiles(new SubfolderFilter());
		return asFilenameArray(categories);
	}
	
	public String[] getTrees(String superfamily, String category) {
		if (category == null || superfamily == null)
			return null;
		
		File category_folder = new File(onekp_phyloxml_folder, superfamily + File.separatorChar + category);
		if (category_folder == null || !category_folder.exists() || !category_folder.canRead()) 
			throw new IllegalArgumentException("No such category "+category+ " for "+superfamily+"!");
		
		File[] phyloxml = category_folder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.isFile() && pathname.canRead() && pathname.getName().endsWith(".xml") || pathname.getName().endsWith(".phyloxml"))
					return true;
				return false;
			}
			
		});
		
		return asFilenameArray(phyloxml);
	}
}
