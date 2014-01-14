package au.edu.unimelb.plantcell.gwtphylo.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TreeItem;


public class OpenTreeItem implements OpenHandler<TreeItem> {
	
	@Override
	public void onOpen(final OpenEvent<TreeItem> event) {
		TreeItem item = event.getTarget();
		System.err.println("Open "+item.getText());
		if (TreeItemUtils.isSuperFamilyTreeItem(item)) { 
			loadCategories(event);
		} else if (TreeItemUtils.isCategoryItem(item)) {
			loadTrees(event);
		}
		
	}

	private void loadTrees(final OpenEvent<TreeItem> event) {
		PhyloXMLServiceAsync service = (PhyloXMLServiceAsync) GWT.create(PhyloXMLService.class);
		TreeItem ti = event.getTarget();
		
		String category = ti.getText();
		final String superfamily = ti.getParentItem().getText();
		AsyncCallback<String[]> cb = new AsyncCallback<String[]>() {

			@Override
			public void onFailure(Throwable caught) {				
				// display nothing underneath the item
				TreeItem kid = event.getTarget();
				kid.removeItems();
				kid.addItem(dummyItem());
			}

			@Override
			public void onSuccess(String[] result) {
				TreeItem ti = event.getTarget();
				ti.removeItems();
				if (result.length < 1) {
					ti.addItem(dummyItem());
				}
				for (String tree : result) {
					TreeItem kid = new PhyloXMLTreeItem(tree);
					ti.addItem(kid);
				}
			}
			
		};
		service.getTrees(superfamily, category, cb);
	}

	private TreeItem dummyItem() {
		TreeItem it = new TreeItem();
		it.setVisible(false);
		return it;
	}
	
	private void loadCategories(final OpenEvent<TreeItem> event) {
		PhyloXMLServiceAsync  service = (PhyloXMLServiceAsync) GWT.create(PhyloXMLService.class);
		AsyncCallback<String[]> cb = new AsyncCallback<String[]>() {
			@Override
			public void onFailure(Throwable caught) {
				// display nothing underneath the item
				TreeItem kid = event.getTarget();
				kid.removeItems();
				kid.addItem(dummyItem());
			}
			
			@Override
			public void onSuccess(String[] categories) {
				TreeItem ti = event.getTarget();
				ti.removeItems();
				if (categories.length < 1) {
					ti.addItem(dummyItem());
				}
				for (String category : categories) {
					SafeHtml item = SafeHtmlUtils.fromString(category);
					TreeItem kid = ti.addItem(item);
					kid.addItem(dummyItem());
				}
			}
		};
		String superfamily = event.getTarget().getText();
		service.getCategories(superfamily, cb);
	}
}
