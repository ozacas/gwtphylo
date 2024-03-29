package au.edu.unimelb.plantcell.gwtphylo.client;

import com.google.gwt.user.client.ui.TreeItem;

/**
 * A collection of static methods for evaluating selected tree items rather than scattering the logic throughout the system
 * 
 * @author http://www.plantcell.unimelb.edu.au/bioinformatics
 *
 */
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

	/**
	 * If its not a category (eg. by_clade) or a superfamily (eg. agpeptide) then it must be a PhyloXML file for display (the tree has no other visible items)
	 * @param selectedItem
	 * @return
	 */
	public static boolean isPhyloXMLItem(TreeItem selectedItem) {
		return (!(isCategoryItem(selectedItem) || isSuperFamilyTreeItem(selectedItem)));
	}
}
