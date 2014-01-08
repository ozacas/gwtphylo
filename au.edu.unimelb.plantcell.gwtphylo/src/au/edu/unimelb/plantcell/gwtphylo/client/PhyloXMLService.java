package au.edu.unimelb.plantcell.gwtphylo.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("PhyloXMLServiceImpl")
public interface PhyloXMLService extends RemoteService {
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
	
	/**
	 * Returns the phyloxml associated with the given selection
	 */
	String getPhyloXML(String superfamily, String category, String name) throws IllegalArgumentException;
	
	
}
