package au.edu.unimelb.plantcell.gwtphylo.server;

import au.edu.unimelb.plantcell.gwtphylo.client.PhyloXMLService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class PhyloXMLServiceImpl extends RemoteServiceServlet implements PhyloXMLService {
	
	/* list of gene superfamilies which we present the 1kp data for */
	private static final String[] superfamilies = new String[] { "CESA/CSL", "Classical AGP", "AGPeptide", 
			"FLA", "Extensin", "PRP", "Phospholipase-D", "GalT", "MUR3", "Adaptin-Like Kinases"  };
	
	public String[] getSuperfamilies() {
		return superfamilies;
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
