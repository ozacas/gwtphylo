package au.edu.unimelb.plantcell.gwtphylo.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * M in MVP for the tree view page
 * 
 * @author http://www.plantcell.unimelb.edu.au/bioinformatics
 *
 */
public class TreeViewModel implements SelectionHandler<TreeItem> {
	public enum SupportedFormats { TREE_FORMAT_PHYLOXML };
	
	private String               superfamily, category, tree; // or null if not chosen
	private int                  current_text_size;		      // determines the text size of the taxa labels (in points ie. 1/72 inch)
	private static TreeViewModel instance;
	private final List<TreeViewModelListener> listeners = new ArrayList<TreeViewModelListener>();
	
	private TreeViewModel() {
		current_text_size = 8;
	}
	
	public List<TreeViewModelListener> getListeners() {
		return listeners;
	}
	
	public static TreeViewModel getInstance() {
		if (instance != null)
			return instance;
		instance = new TreeViewModel();
		return instance;
	}
	
	public boolean hasCurrentTree() {
		return (superfamily != null && category != null && tree != null);
	}
	
	public String getSuperFamily() {
		return superfamily;
	}
	
	public String getCategory() {
		return category;		// eg. by clade, overall ...
	}
	
	public String getPhyloXMLTree() {
		return tree;	// eg. muscle_conifers_.... .phyloxml
	}
	
	/**
	 * In points (ie. 1/72nd of an inch)
	 * @return
	 */
	public int getCurrentTextSize() {
		return current_text_size;
	}
	
	private String makeURLEncodedParams(final String superfamily, final String category, final String name) {
		assert(superfamily != null && category != null && name != null);
		
		Map<String,String> k2v = new HashMap<String,String>();
		k2v.put("superfamily", superfamily);
		k2v.put("category", category);
		k2v.put("tree", name);
		
		StringBuilder sb = new StringBuilder();
		for ( String k: k2v.keySet() ) {
	        String vx = URL.encodeQueryString( k2v.get(k));
	        if ( sb.length() > 0 ) {
	            sb.append("&");
	        }
	        sb.append(k).append("=").append(vx);
	    }

		return sb.toString();
	}

	public String getCurrentTreeURL(SupportedFormats format) throws Exception {
		if (!hasCurrentTree())
			return null;
		if (format.equals(SupportedFormats.TREE_FORMAT_PHYLOXML))
			return GWT.getModuleBaseURL() + "phyloxml?"+makeURLEncodedParams(superfamily, category, tree);
		else 
			throw new Exception("Unsupported format: "+format.toString());
	}

	@Override
	public void onSelection(SelectionEvent<TreeItem> event) {
		TreeItem item = event.getSelectedItem();
		
		if (item == null || !TreeItemUtils.isPhyloXMLItem(item))
			return;
		tree        = getTreeItemName(item);
		category    = item.getParentItem().getText();
		superfamily = item.getParentItem().getParentItem().getText();
		fireModelChanged();
	}

	private void fireModelChanged() {
		for (TreeViewModelListener l : listeners) {
			l.modelChanged(this);
		}
	}

	private String getTreeItemName(TreeItem item) {
		assert(item != null);
		return item.getUserObject().toString();
	}
	
	public void setCurrentTextSize(int i) {
		assert(i > 0);
		
		current_text_size = i;
		fireModelChanged();
	}
	
	public static native String getPhyloSVG() /*-{
		var svgSource = phylocanvas.getSvgSource();
		return svgSource;
	}-*/;
}
