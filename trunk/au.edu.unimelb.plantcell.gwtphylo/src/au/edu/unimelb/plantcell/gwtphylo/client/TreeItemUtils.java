package au.edu.unimelb.plantcell.gwtphylo.client;

import com.google.gwt.user.client.ui.TreeItem;

public class TreeItemUtils {
	public static boolean isSuperFamilyTreeItem(final TreeItem it) {
		if (it == null)
			return false;
		return (it.getParentItem() == null);
	}
	
	public static boolean isCategoryItem(final TreeItem it) {
		if (it == null)
			return false;
		return (isSuperFamilyTreeItem(it.getParentItem()));
	}

	public static boolean isPhyloXMLItem(TreeItem selectedItem) {
		return (!(isCategoryItem(selectedItem) && isSuperFamilyTreeItem(selectedItem)));
	}
}
