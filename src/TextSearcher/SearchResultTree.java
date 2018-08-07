package TextSearcher;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class SearchResultTree extends JScrollPane implements TreeModel {
    private final SearchResultTreeNode root = new SearchResultTreeNode();
    private final EventListenerList listenerList = new EventListenerList();

    private final DefaultTreeModel defaultTreeModel = new DefaultTreeModel(root);
    private final JTree tree = new JTree(this);

    private final TextSearcher textSearcher;

    private final MouseInputAdapter mouseListener = new MouseInputAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            TreePath treePath = tree.getPathForLocation(e.getX(), e.getY());
            if (e.getClickCount() == 2 && treePath != null) {
                Path path = ((SearchResultTreeNode)treePath.getLastPathComponent()).getPath();
                if (!Files.isDirectory(path)) {
                    textSearcher.getSearchResultTabs().open(path);
                }
            }
        }
    };

    public SearchResultTree(TextSearcher textSearcher) {
        super();
        this.setViewportView(tree);

        tree.setRootVisible(false);
        tree.addMouseListener(mouseListener);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        this.textSearcher = textSearcher;
    }

    public SearchResultTreeNode addResultTree(Path directory, String filter, String what) {
        String name = "Search \"" + what + "\" in \"" + directory.toString() + "\" with filter(\"" + filter + "\")";
        SearchResultTreeNode node = root.addResult(0, directory, name);
        reload();
        return node;
    }

    public void scrollPathToVisible(TreeNode node) {
        tree.scrollPathToVisible(getPath(node));
    }

    public void reload() {
        treeStructureChanged(getPath(root));
    }

    public void reload(TreeNode node) {
        treeStructureChanged(getPath(node));
    }

    private void treeStructureChanged(TreePath path) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent event = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==TreeModelListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new TreeModelEvent(this, path);
                }
                ((TreeModelListener)listeners[i+1]).treeStructureChanged(event);
            }
        }
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        return ((TreeNode)parent).getChildAt(index);
    }

    @Override
    public int getChildCount(Object parent) {
        return ((TreeNode)parent).getChildCount();
    }

    @Override
    public boolean isLeaf(Object node) {
        return ((TreeNode)node).isLeaf();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        MutableTreeNode aNode = (MutableTreeNode)path.getLastPathComponent();

        aNode.setUserObject(newValue);
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if(parent == null || child == null)
            return -1;
        return ((TreeNode)parent).getIndex((TreeNode)child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
    }

    private TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
        TreeNode[] retNodes;
        if(aNode == null) {
            if(depth == 0) {
                return null;
            } else {
                retNodes = new TreeNode[depth];
            }
        } else {
            depth++;
            if(aNode == root) {
                retNodes = new TreeNode[depth];
            } else {
                retNodes = getPathToRoot(aNode.getParent(), depth);
            }
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }

    public TreeNode[] getPathToRoot(TreeNode aNode) {
        return getPathToRoot(aNode, 0);
    }

    public TreePath getPath(TreeNode treeNode) {
        TreeNode[] retNodes = getPathToRoot(treeNode, 0);
        return retNodes == null ? null : new TreePath(retNodes);
    }
}
