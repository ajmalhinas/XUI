package org.purnamaproject.xui.peer;

/**
 * @(#)JScrollTree.java 0.5 18/08/2003
 *
 * The Purnama Project XUI (XML-based User Interface) API is an set of program
 * calls that utilize the XUI tagset and perform the task of creating a user
 * interface. The Purnama version of this API is specific using Java as the platform
 * but other XUI APIs may use other libraries and platforms. The Purnama XUI API
 * supports the creation of Swing components, adding, deleting and laying out
 * components. It also supports dynamic binding of business logic to the user
 * interface.
 *
 * Copyright (c) 2003 Arron Ferguson
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * For more information, please contact Arron Ferguson at:
 * e-mail: arron_ferguson@bcit.ca
 * snail-mail: SW2 - 124, 3700 Willingdon Avenue, Burnaby, British Columbia, Canada, V5G 3H2
 * Web: http://xml.bcit.ca/PurnamaProject/
 *
 */

import java.awt.BorderLayout;
import java.util.List;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.purnamaproject.xui.component.atomic.XUITreeNode;


/**
 * Represents a tree that automatically supports scrolling when is needed.
 * @version 0.5 18/08/2003
 * @author  Arron Ferguson
 */
public class JScrollTree extends JPanel
{

    /**
     * the tree.
     */
    private JTree tree;

    /**
     * The root node of this tree.
     */
    private XUITreeNode root;

    /**
     * The return node used in the recursive lookup for a particular node
     * based on a GUI peer.
     */
    private XUITreeNode returnNode = null;

    /**
     * the scroll pane.
     */
    private JScrollPane scroller;

    /**
     * Configures the scroll pane as well as the tree.
     */
    public JScrollTree()
    {
        super();
        setLayout(new BorderLayout());

        root = new XUITreeNode("root");

        // config the selection settings with the table:
        tree = new JTree(root.getGUINodeRepresentation());
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        tree.setEditable(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        scroller = new JScrollPane(tree);

        setRoot(root);

        // add them to this component
        add(scroller, BorderLayout.CENTER);
    }

    /**
     * Configures the scroll pane as well as the tree and adds the
     * root node to the tree.
     *
     * @param newRoot the new root node of this tree.
     */
    public JScrollTree(XUITreeNode newRoot)
    {
        this();
        setRoot(newRoot);

    }

    /**
     * Based on the GUI Node, match the same within the XUITreeNode and return
     * that XUITreeNode. If no match is found, this method returns null.
     *
     * @param guiNodeToMatch the Swing based JTreeNode which is of type <code>DefaultMutableTreeNode</code>.
     * @return the XUITreeNode whose DefaultMutableTreeNode is the same as the one passed to this method.
     */
    public XUITreeNode matchTreeNode(DefaultMutableTreeNode guiNodeToMatch)
    {
        List childTreeNodes = root.getChildren();
        int size = childTreeNodes.size();

        for(int i = 0; i < size; i++)
        {
            XUITreeNode node = (XUITreeNode)childTreeNodes.get(i);

            // recursive search
            doNodeMatch(node, guiNodeToMatch);

        }
        XUITreeNode temp = returnNode;
        returnNode = null;
        return temp;
    }

    /**
     * Recursivly search for the node.
     *
     * @param guiNodeToMatch the GUI node to match within the tree hierarchy.
     * @param the starting node to start using to match with.
     * @return the XUITreeNode that matches the guiMatchNode. If no node then returns null.
     */
    private void doNodeMatch(XUITreeNode node, DefaultMutableTreeNode guiNodeToMatch)
    {
        DefaultMutableTreeNode peer = (DefaultMutableTreeNode)node.getGUINodeRepresentation();

        if(guiNodeToMatch == peer)
        {

            returnNode = node;
            return;
        }

        List childTreeNodes = node.getChildren();
        int size = childTreeNodes.size();
        for(int i = 0; i < size; i++)
        {
            XUITreeNode anotherNode = (XUITreeNode)childTreeNodes.get(i);
            DefaultMutableTreeNode guiPeer = (DefaultMutableTreeNode)anotherNode.getGUINodeRepresentation();

            if(guiNodeToMatch == guiPeer)
            {

                returnNode = anotherNode;
                return;
            }

            // recursive search
            doNodeMatch(anotherNode, guiNodeToMatch);

        }
    }

    /**
     * Returns the root node of this tree.
     *
     * @return the root node.
     */
    public XUITreeNode getRootNode()
    {
        return root;
    }

    /**
     * Sets the new root node.
     *
     * @param newRoot the new root node.
     */
    public void setRoot(XUITreeNode newRoot)
    {
        root = newRoot;
        ((DefaultTreeModel)tree.getModel()).setRoot(newRoot.getGUINodeRepresentation());
        tree.invalidate();
        scroller.validate();
    }

    /**
     * Returns the JTable that sits within this JScrollPane.
     *
     * @return the table.
     */
    public JTree getTree()
    {
        return tree;
    }



}
