package au.edu.unimelb.plantcell.gwtphylo.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("PhyloXMLServiceImpl")
public interface PhyloXMLService extends RemoteService {
	String[] getSuperfamilies() throws IllegalArgumentException;
}
