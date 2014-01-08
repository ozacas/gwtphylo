package au.edu.unimelb.plantcell.gwtphylo.client;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The async counterpart of <code>PhyloXMLService</code>.
 */
public interface PhyloXMLServiceAsync {
	void getSuperfamilies(AsyncCallback<String[]> cb);
	
	void getCategories(String superfamily, AsyncCallback<String[]> cb);

	void getTrees(String superfamily, String category,
			AsyncCallback<String[]> cb);
	
	void getPhyloXML(String superfamily, String category, String name, AsyncCallback<String> cb);
}
