package au.edu.unimelb.plantcell.gwtphylo.client;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The async counterpart of <code>PhyloXMLService</code>.
 */
public interface PhyloXMLServiceAsync {
	void getSuperfamilies(AsyncCallback<String[]> cb);
}
