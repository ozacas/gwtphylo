package au.edu.unimelb.plantcell.gwtphylo.server;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import au.edu.unimelb.plantcell.gwtphylo.shared.ConfigurationConstants;

/**
 * Responsible for returning the phyloxml of the chosen tree (see form parameters) to the caller
 * 
 * @author http://www.plantcell.unimelb.edu.au/bioinformatics
 * 
 */
@RemoteServiceRelativePath("phyloxml")
public class GetPhyloXMLServlet extends AbstractHttpServlet {
	/**
	 * not used
	 */
	private static final long serialVersionUID = -658313887447934760L;

	
	public GetPhyloXMLServlet() {
		super(ConfigurationConstants.PHYLOXML_ROOT_FOLDER);
	}
	
	@Override
	protected void initHttpHeaders(final HttpServletResponse resp, final File out) {
		 resp.setContentType("application/xml");
		 resp.addHeader("Content-Type", "application/xml");
		 resp.addHeader("Content-Disposition", "attachment; filename=" + out.getName());
	}
}
