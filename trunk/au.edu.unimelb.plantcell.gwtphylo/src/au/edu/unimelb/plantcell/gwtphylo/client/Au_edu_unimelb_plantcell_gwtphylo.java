package au.edu.unimelb.plantcell.gwtphylo.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Au_edu_unimelb_plantcell_gwtphylo implements EntryPoint {
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Tree t = new Tree();
		t.addOpenHandler(new OpenTreeItem());
		t.addSelectionHandler(new DisplayTreeItem());
		
		TreeItem dummy = new TreeItem();
		dummy.setVisible(false);
		t.addItem(dummy);
		
		// setup for the RPC call
		PhyloXMLServiceAsync  service = (PhyloXMLServiceAsync) GWT.create(PhyloXMLService.class);
		SuperfamiliesCallback sf_cb = new SuperfamiliesCallback(t);
		
		// add tree onto webpage
		RootPanel.get("1kpContainer").add(t);
		
		// async: will return immediately (callback is responsible for populating the tree)
		service.getSuperfamilies(sf_cb);
	}
	
	
}
