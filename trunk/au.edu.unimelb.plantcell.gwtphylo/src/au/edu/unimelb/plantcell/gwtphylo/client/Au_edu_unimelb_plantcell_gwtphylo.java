package au.edu.unimelb.plantcell.gwtphylo.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Au_edu_unimelb_plantcell_gwtphylo implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Tree t = new Tree();
		
		TreeItem dummy = new TreeItem();
		dummy.setVisible(false);
		t.addItem(dummy);
		
		// setup for the RPC call
		PhyloXMLServiceAsync  service = (PhyloXMLServiceAsync) GWT.create(PhyloXMLService.class);
		AsyncCallback<String[]>    cb = new AsyncCallback<String[]>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(SERVER_ERROR+" Message: "+caught.getMessage());
			}

			@Override
			public void onSuccess(String[] superfamilies) {
				for (String superfamily : superfamilies) {
					SafeHtml html = SafeHtmlUtils.fromString(superfamily);
					TreeItem ti = new TreeItem(html);
					t.addItem(ti);
					
					html = SafeHtmlUtils.fromString("By 1kp clade");
					ti.addItem(html);
					html = SafeHtmlUtils.fromString("Clustered 100%");
					ti.addItem(html);
				}
			}
		};
		
		// add tree onto webpage
		RootPanel.get("1kpContainer").add(t);
		
		// async: will return immediately (callback is responsible for populating the tree)
		service.getSuperfamilies(cb);
	}
	
	
}