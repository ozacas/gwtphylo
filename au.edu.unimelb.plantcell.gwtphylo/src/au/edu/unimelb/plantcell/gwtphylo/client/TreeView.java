package au.edu.unimelb.plantcell.gwtphylo.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Invoked when a tree item is selected, this listener only responds when a PhyloXML file is chosen for display.
 * 
 * @author http://www.plantcell.unimelb.edu.au/bioinformatcs
 *
 */
public class TreeView implements TreeViewModelListener {

	public static void showLoadingBanner(String message){
		DOM.setInnerHTML(RootPanel.get("loadingMessage").getElement(), message + " &nbsp;<img src=\"images/ajax-loader.gif\" height=15>&nbsp;");
	}

	public static void hideLoadingBanner(){
		DOM.setInnerHTML(RootPanel.get("loadingMessage").getElement(), "<br>");
	}
		
	@Override
	public void modelChanged(final TreeViewModel mdl) {
		assert(mdl != null);
		
		if (!mdl.hasCurrentTree())
			return;
		
		// schedule the tree for display in the browser... again ASYNC
		try {
			RequestBuilder phyloxml_server = new RequestBuilder(RequestBuilder.GET, mdl.getCurrentTreeURL(TreeViewModel.SupportedFormats.TREE_FORMAT_PHYLOXML));
			phyloxml_server.setHeader("Content-type", "application/x-www-form-urlencoded");
			phyloxml_server.sendRequest(null, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request,
						Response response) {
					if (200 == response.getStatusCode()) {
						hideLoadingBanner();
						setCurrentTreePath(mdl);
						setLabelFontSize(mdl.getCurrentTextSize());
						
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setCurrentTreePath(final TreeViewModel mdl) {
		Element e = DOM.getElementById(Au_edu_unimelb_plantcell_gwtphylo.CURRENT_TREE_LABEL_ID);
		if (e == null)
			return;
	
		e.setInnerText(" " + mdl.getSuperFamily()+ " > "+ mdl.getCategory() + " > " + mdl.getPhyloXMLTree());
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
	 * Set the label font size for display
	 */
	public native void setLabelFontSize(int size) /*-{
		$wnd.Smits.PhyloCanvas.Render.Style.text["font-size"] = size;
	}-*/;
	
	/**
	 * This code will plot a rectangular tree view from the specified PhyloXML. Note the method signature for GWT 2.5.1 (or compat) to use!
	 * @param xml
	 */
	public native void showPhyloSVG(String xml) /*-{
		phylocanvas = new $wnd.Smits.PhyloCanvas({ phyloxml: xml },  'scrollableCanvas', 1000, 1000 , 'circular');
	}-*/;
	
}
