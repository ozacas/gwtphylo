package au.edu.unimelb.plantcell.gwtphylo.server;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

/**
 * Responsible for returning the phyloxml of the chosen tree (see form parameters) to the caller
 * 
 * @author http://www.plantcell.unimelb.edu.au/bioinformatics
 * 
 */
public class GetPhyloXMLServlet extends AbstractHttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -658313887447934760L;
	/**
	 * where to read phyloxml files from...
	 */
	public final static File BASE_DIR = new File("c:/www/1kp");
	
	
	public GetPhyloXMLServlet() {
		super(BASE_DIR);
	}
	
	@Override
	protected void initHttpHeaders(final HttpServletResponse resp, final File out) {
		 resp.setContentType("application/xml");
		 resp.addHeader("Content-Type", "application/xml");
		 resp.addHeader("Content-Disposition", "inline; filename=" + out.getName());
	}
}
