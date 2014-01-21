package au.edu.unimelb.plantcell.gwtphylo.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Au_edu_unimelb_plantcell_gwtphylo implements EntryPoint {
	public static final String  CURRENT_TREE_LABEL_ID= "current-tree-label";
	private static final String CURRENT_FONTSIZE_LIST= "font-size-list";
	private static final int[]             font_sizes= new int[] { 8, 9, 10, 11, 12, 14, 16, 18, 28 };
	private static final TreeViewModel     tree_model= TreeViewModel.getInstance();
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Tree t = new Tree();
		t.addOpenHandler(new OpenTreeItem());
		t.addSelectionHandler(tree_model);
		
		tree_model.getListeners().add(new TreeView());
		
		TreeItem dummy = new TreeItem();
		dummy.setVisible(false);
		t.addItem(dummy);
		
		// setup for the RPC call
		OneKPServiceAsync  service = (OneKPServiceAsync) GWT.create(OneKPService.class);
		SuperfamiliesCallback sf_cb = new SuperfamiliesCallback(t);
		
		// add tree onto webpage
		RootPanel.get("1kpContainer").add(t);
		
		// add scrollable canvas to display the SVG rendering to the webpage
		ScrollPanel sp = new ScrollPanel();
		sp.setSize("600px", "600px");
		sp.getElement().setId("scrollableCanvas");
		RootPanel.get("svgCanvas").add(sp);
		
		HorizontalPanel    controlPanel = new HorizontalPanel();
		HorizontalPanel    labelPanel = new HorizontalPanel();
		
		Button phyloxml_download_button = new Button("Download PhyloXML");
		phyloxml_download_button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (tree_model.hasCurrentTree()) {
					try {
						Window.open(tree_model.getCurrentTreeURL(TreeViewModel.SupportedFormats.TREE_FORMAT_PHYLOXML), "phyloxml", "");
					} catch (Exception e) {
						Window.alert(e.getMessage());
					}
				}
			}
			
		});
		Button      svg_download_button = new Button("Download SVG");
		svg_download_button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (tree_model.hasCurrentTree()) {
					
				}
			}
			
		});
		Label              current_tree = new Label("");
		current_tree.getElement().setId(CURRENT_TREE_LABEL_ID);	// needed by DisplayTreeItem to update the path
		final ListBox font_size = new ListBox(false);
		font_size.getElement().setId(CURRENT_FONTSIZE_LIST);
		for (int i=0; i<font_sizes.length; i++) {
			font_size.addItem(""+font_sizes[i]+ " pt.");
		}
		font_size.setSelectedIndex(0);
		font_size.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				int idx = font_size.getSelectedIndex();
				if (idx >= 0) {
					tree_model.setCurrentTextSize(font_sizes[idx]);
				}
			}
			
		});
		
		controlPanel.add(phyloxml_download_button);
		controlPanel.add(svg_download_button);
		controlPanel.add(font_size);
		
		labelPanel.add(current_tree);
		
		RootPanel.get("controlPanel").add(controlPanel);
		RootPanel.get("labelPanel").add(labelPanel);
		
		// async: will return immediately (callback is responsible for populating the tree)
		service.getSuperfamilies(sf_cb);
	}
}
