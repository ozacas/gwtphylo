package au.edu.unimelb.plantcell.gwtphylo.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.ArrayList;

import au.edu.unimelb.plantcell.gwtphylo.client.PhyloXMLService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class PhyloXMLServiceImpl extends RemoteServiceServlet implements PhyloXMLService {
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
	private final File onekp_phyloxml_folder = new File("c:/www/1kp/");
	
	/* list of gene superfamilies which we present the 1kp data for */
	public String[] getSuperfamilies() {
		File[] subfiles = onekp_phyloxml_folder.listFiles(new SubfolderFilter());
		
		return asFilenameArray(subfiles);
	}

	public String[] asFilenameArray(final File[] col) {
		ArrayList<String> ret = new ArrayList<String>();
		for (File f : col) {
			ret.add(f.getName());
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
	
	/**
	 * Returns the phyloxml: maybe large for large trees (10MB)
	 */
	public String getPhyloXML(String superfamily, String category, String name) {
		BufferedReader rdr = null;
		try {
			File phyloxml = new File(onekp_phyloxml_folder, superfamily + File.separatorChar + category + File.separatorChar + name);
			if (!(phyloxml != null && phyloxml.canRead() && phyloxml.isFile())) 
				throw new IllegalArgumentException("file cannot be read!");
			
			StringBuilder sb = new StringBuilder((int) phyloxml.length());
			rdr = new BufferedReader(new FileReader(phyloxml));
			String line;
			while ((line = rdr.readLine()) != null) {
				sb.append(line);
			}
			rdr.close();
			return sb.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
}
