package au.edu.unimelb.plantcell.gwtphylo.client;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The async counterpart of <code>OneKpService</code>.
 */
public interface OneKPServiceAsync {
	void getSuperfamilies(AsyncCallback<String[]> cb);
	
	void getCategories(String superfamily, AsyncCallback<String[]> cb);

	void getTrees(String superfamily, String category,
			AsyncCallback<String[]> cb);
}
