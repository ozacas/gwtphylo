package au.edu.unimelb.plantcell.gwtphylo.client;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class SuperfamiliesCallback implements AsyncCallback<String[]> {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";
	
	
	private final Tree t;
	
	public SuperfamiliesCallback(final Tree t) {
		this.t = t;
	}
	
	@Override
	public void onFailure(Throwable caught) {
		Window.alert(SERVER_ERROR+" Message: "+caught.getMessage());
	}
	
	/**
	 * Ensure every tree node has at least one item so that the '+' expand/collapse icon appears and is functional for the user
	 * @return
	 */
	private TreeItem dummyItem() {
		TreeItem dummy = new TreeItem();
		dummy.setVisible(false);
		return dummy;
	}
	
	@Override
	public void onSuccess(String[] superfamilies) {
		for (String superfamily : superfamilies) {
			TreeItem ti = new TreeItem(SafeHtmlUtils.fromString(superfamily));
			ti.addItem(dummyItem());
			t.addItem(ti);
		}
	} 

}
