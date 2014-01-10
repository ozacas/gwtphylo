package au.edu.unimelb.plantcell.gwtphylo.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Invoked when a tree item is selected, this listener only responds when a PhyloXML file is chosen for display.
 * 
 * @author http://www.plantcell.unimelb.edu.au/bioinformatcs
 *
 */
public class DisplayTreeItem implements SelectionHandler<TreeItem> {

	@Override
	public void onSelection(SelectionEvent<TreeItem> event) {
		if (!TreeItemUtils.isPhyloXMLItem(event.getSelectedItem()))
			return;
			
		// schedule the tree for display in the browser... again ASYNC
		TreeItem category_item = event.getSelectedItem().getParentItem();
		TreeItem superfamily_item = category_item.getParentItem();
		if (category_item == null || superfamily_item == null)
			return;
		String category = category_item.getText();
		String name     = event.getSelectedItem().getText();
		String superfamily = superfamily_item.getText();
		
		PhyloXMLServiceAsync  service = (PhyloXMLServiceAsync) GWT.create(PhyloXMLService.class);
		AsyncCallback<String> cb = new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(String xml) {
				showPhyloSVG(xml);
			}
			
		};
		
		service.getPhyloXML(superfamily, category, name, cb);
	}

	/**
	 * This code will plot a rectangular tree view from the specified PhyloXML. Note the method signature for GWT 2.5.1 (or compat) to use!
	 * @param xml
	 */
	public native void showPhyloSVG(String xml) /*-{
		phylocanvas = new $wnd.Smits.PhyloCanvas(xml,  'svgCanvas',   1000, 1000);
	}-*/;
}
