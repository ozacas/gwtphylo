package au.edu.unimelb.plantcell.gwtphylo.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Invoked when a tree item is selected, this listener only responds when a PhyloXML file is chosen for display.
 * 
 * @author http://www.plantcell.unimelb.edu.au/bioinformatcs
 *
 */
public class DisplayTreeItem implements SelectionHandler<TreeItem> {

	public static void showLoadingBanner(String message){
		DOM.setInnerHTML(RootPanel.get("loadingMessage").getElement(), message + " &nbsp;<img src=\"images/ajax-loader.gif\" height=15>&nbsp;");
	}

	public static void hideLoadingBanner(){
		DOM.setInnerHTML(RootPanel.get("loadingMessage").getElement(), "<br>");
	}
		
	@Override
	public void onSelection(SelectionEvent<TreeItem> event) {
		TreeItem selected_item    = event.getSelectedItem();

		if (!TreeItemUtils.isPhyloXMLItem(selected_item)) {
			System.err.println(selected_item.getText() + " is not a phyloxml tree, not displaying tree");
			return;
		}
		
		// schedule the tree for display in the browser... again ASYNC
		RequestBuilder phyloxml_server = new RequestBuilder(RequestBuilder.POST, GWT.getModuleBaseURL()+"phyloxml");
		phyloxml_server.setHeader("Content-type", "application/x-www-form-urlencoded");
		
		try {
			phyloxml_server.sendRequest(getRequestParam(selected_item), new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request,
						Response response) {
					if (200 ==  response.getStatusCode()) {
						hideLoadingBanner();
						// remove all existing tree(s) from canvas
						emptySVGCanvas();	
						// add new tree to canvas
						showPhyloSVG(response.getText());
					}
				}

				@Override
				public void onError(Request request, Throwable exception) {
					hideLoadingBanner();
					Window.alert(exception.getMessage());
				}
				
			});
			//showLoadingBanner("Loading "+name);
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}

	private String getRequestParam(final TreeItem selected_item) {
		// figure out where in the tree we are...
		if (selected_item == null) {
			System.err.println("Wierd nothing selected!");
			return "";
		}
		TreeItem category_item    = selected_item.getParentItem();
		if (category_item == null) {
			System.err.println("Cannot find category for "+selected_item.getText());
			return "";
		}
		TreeItem superfamily_item = category_item.getParentItem();
		if (superfamily_item == null) {
			System.err.println("Cannot find superfamily for "+category_item.getText());
			return "";
		}
				
		String category    = category_item.getText();
		String name        = getTreeItemName(selected_item);
		String superfamily = superfamily_item.getText();
		return makeHTTPPostData(superfamily, category, name);
	}
	
	private String makeHTTPPostData(final String superfamily, final String category, final String name) {
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

	private String getTreeItemName(TreeItem item) {
		assert(item != null);
		return item.getUserObject().toString();
	}
	
	/**
	 * Ugly hack using DOM manipulation. Seems ok with firefox, but not sure about other browsers? Memory leaks?
	 */
	protected void emptySVGCanvas() {
		Element parent = DOM.getElementById("scrollableCanvas");
		Element kid;
		while ((kid = DOM.getFirstChild(parent)) != null) {
			//System.err.println(kid.getTitle());
			DOM.removeChild(parent, kid);
		}
	}

	/**
	 * This code will plot a rectangular tree view from the specified PhyloXML. Note the method signature for GWT 2.5.1 (or compat) to use!
	 * @param xml
	 */
	public native void showPhyloSVG(String xml) /*-{
		phylocanvas = new $wnd.Smits.PhyloCanvas({ phyloxml: xml },  'scrollableCanvas', 1000, 1000 , 'circular');
	}-*/;
}
