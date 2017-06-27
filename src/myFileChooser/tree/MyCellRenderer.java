package myFileChooser.tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Created by Maria on 20.06.2017.
 */
public class MyCellRenderer extends DefaultTreeCellRenderer {

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

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        final Component component = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        this.setTextNonSelectionColor(Color.WHITE);

        Object[] o = node.getPath();
        String path = "";
        for (int pathCount = 1; pathCount < o.length-1; pathCount++) {
            path += o[pathCount];
            if (path.charAt(path.length() - 1) != '\\')
                path += '\\';
        }
        path += o[o.length-1];
        return component;
    }
}
