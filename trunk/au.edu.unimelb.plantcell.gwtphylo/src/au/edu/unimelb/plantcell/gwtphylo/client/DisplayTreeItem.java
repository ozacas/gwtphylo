package au.edu.unimelb.plantcell.gwtphylo.client;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
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
		
	}

}
