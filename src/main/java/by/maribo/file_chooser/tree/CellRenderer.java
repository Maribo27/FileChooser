package by.maribo.file_chooser.tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.io.File;

/**
 * Created by Maria on 20.06.2017.
 */
public class CellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Color getBackgroundNonSelectionColor() {
        return (null);
    }

    @Override
    public Color getBackgroundSelectionColor() {
        return Color.GRAY;
    }

    @Override
    public Color getBackground() {
        return (null);
    }

    @Override
    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean selected, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
        ImageIcon leafIcon = new ImageIcon("pictures\\pictures\\files\\file.png");
        this.setLeafIcon(leafIcon);

        ImageIcon folderIcon = new ImageIcon("pictures\\pictures\\files\\folder.png");
        this.setClosedIcon(folderIcon);

        ImageIcon openIcon = new ImageIcon("pictures\\pictures\\files\\open.png");
        this.setOpenIcon(openIcon);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        final Component component = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        this.setTextNonSelectionColor(Color.LIGHT_GRAY);
        this.setFont(new Font("Arial", Font.PLAIN, 14));

        Object[] o = node.getPath();
        StringBuilder filepath = new StringBuilder(node.toString());
        boolean checkRoot = false;
        DefaultMutableTreeNode tempNode = node;
        for (int level = node.getLevel(); level > 1; level--) {
            filepath.insert(0, tempNode.getParent().toString());
            tempNode = (DefaultMutableTreeNode) tempNode.getParent();
            checkRoot = true;
        }
        File tempFile = new File(filepath.toString());
        if (tempFile.isHidden() && checkRoot && tempFile.isDirectory())
            this.setIcon(new ImageIcon("pictures\\pictures\\files\\hide.png"));
        if (tempFile.isHidden() && checkRoot && tempFile.isFile())
            this.setIcon(new ImageIcon("pictures\\pictures\\files\\hideFile.png"));
        StringBuilder path = new StringBuilder();
        for (int pathCount = 1; pathCount < o.length-1; pathCount++) {
            path.append(o[pathCount]);
            if (path.charAt(path.length() - 1) != '\\')
                path.append('\\');
        }
        return component;
    }
}
