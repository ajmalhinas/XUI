package org.purnamaproject.xui.component.atomic;

/**
 * @(#)XUITree.java    0.5 18/08/2003
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

import javax.swing.tree.TreeNode;
import org.purnamaproject.xui.binding.XUIEventSource;
import org.purnamaproject.xui.component.atomic.XUITreeNode;

/**
 * The abstraction of a XUITree. Represents a graphical tree structure with nodes that can be
 * added, edited and deleted.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public interface XUITree extends XUIAtomic, XUIEventSource
{
    /**
     * Returns the root node of this tree.
     *
     * @return the root node.
     */
    public XUITreeNode getRootNode();

    /**
     * Sets the root node of this tree.
     *
     * @param rootNode the root node to set this component to.
     * @throws org.purnamaproject.xui.XUIDisplayException if the rootNode is null.
     */
    public void setRootNode(XUITreeNode rootNode);

    /**
     * Returns the selected XUITreeNode.
     *
     * @return the currently selected node of the tree. Returns null if there is
     * not selected node.
     */
    public XUITreeNode getSelectedNode();

    /**
     * Deletes the node given.
     *
     * @param nodeToDelete the node that is to be deleted. If this node is not found,
     * no action is taken.
     */
    public void deleteTreeNode(XUITreeNode nodeToDelete);

    /**
     * Finds the node given.
     *
     * @param nodeToFind the node that is being searched. If this node is not found,
     * null is returned.
     */
    public XUITreeNode findTreeNode(XUITreeNode nodeToFind);

    /**
     * Allows for a specific node to have its textual data edited.
     *
     * @param textualValue the text that will replace the old text of this node.
     * @param nodeToEdit the node that will be edited.
     */
    public void editTreeNode(XUITreeNode nodeToEdit, String textualValue);

    /**
     * Creates a new XUITreeNode and adds it to the parent node.
     *
     * @param parentNode the parent node that will receive a new node. A new node
     * will be added to this parent node.
     * @param textualValue the text value that will be added to the new node
     * created.
     */
    public XUITreeNode addTreeNode(XUITreeNode parentNode, String textualValue);
}
