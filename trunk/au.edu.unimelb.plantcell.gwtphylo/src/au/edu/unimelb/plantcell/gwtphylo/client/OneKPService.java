package au.edu.unimelb.plantcell.gwtphylo.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Service which is responsible for reporting which gene superfamilies, categories and trees are available. 
 * A separate service {@link GetPhyloXMLServlet} is responsible for transferring large XML documents for viewing more efficiently 
 * than GWT-RPC can.
 * 
 */
@RemoteServiceRelativePath("gwtphylo")
public interface OneKPService extends RemoteService {
	String[] getSuperfamilies() throws IllegalArgumentException;
	
	/**
	 * Returns all sub-categories (separate tree analyses) for the given superfamily. May vary from family to family.
	 * @param superfamily
	 * @return
	 * @throws IllegalArgumentException
	 */
	String[] getCategories(final String superfamily) throws IllegalArgumentException;
	
	/**
	 * Returns the list of available trees for the given category (ie. analysis) of a particular superfamily
	 */
	String[] getTrees(String superfamily, String category) throws IllegalArgumentException;

}
