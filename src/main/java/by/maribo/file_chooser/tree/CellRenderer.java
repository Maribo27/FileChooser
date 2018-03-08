package by.maribo.file_chooser.tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.io.File;
import java.util.Objects;

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
        this.setLeafIcon(getFileIconPath("file.png"));
        this.setClosedIcon(getFileIconPath("folder.png"));
        this.setOpenIcon(getFileIconPath("open.png"));

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
        if (tempFile.isHidden() && checkRoot && tempFile.isDirectory()) {
            this.setIcon(getFileIconPath("hide.png"));
        }
        if (tempFile.isHidden() && checkRoot && tempFile.isFile()) {
            this.setIcon(getFileIconPath("hideFile.png"));
        }
        StringBuilder path = new StringBuilder();
        for (int pathCount = 1; pathCount < o.length-1; pathCount++) {
            path.append(o[pathCount]);
            if (path.charAt(path.length() - 1) != '\\')
                path.append('\\');
        }
        return component;
    }

    private ImageIcon getFileIconPath(String fileName) {
        String folder = "pictures/files/";
        String path = Objects.requireNonNull(getClass().getClassLoader().getResource(folder + fileName)).getPath();
        path = path.replaceAll("%5b", "[");
        path = path.replaceAll("%5d", "]");
        path = path.replaceAll("%20", " ");
        return new ImageIcon(path);
    }
}
