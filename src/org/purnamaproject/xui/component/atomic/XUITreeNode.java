package org.purnamaproject.xui.component.atomic;

/**
 * @(#)XUITreeNode.java 0.5 18/08/2003
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

import java.util.LinkedList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import org.purnamaproject.xui.XUINode;
import org.purnamaproject.xui.impl.XUINodeImpl;

/**
 * <p>This class is really just a wrapper that is placed around both the XUI nodes and the JTree
 * nodes. It is required since they both need to be syncronized and exposing either of them
 * would significantly complicate the API as well as force this API to cater to a particular
 * programming language.</p>
 * @version 0.5 18/08/2003
 * @author  Arron Ferguson
 */
public class XUITreeNode
{
    /**
     * The XUI node representation of this node.
     */
    private XUINode xuiNode;

    /**
     * The parent node of this node.
     */
    private XUITreeNode parent;

    /**
     * The GUI node that fits into the native peer of a JTree within the
     * Java API.
     */
    private DefaultMutableTreeNode guiNode;

    /**
     * The list of child nodes directly underneath this node.
     */
    private List childNodes;

    /**
     * Constructs both the XUI node and the GUI node.
     */
    public XUITreeNode()
    {
        xuiNode = new XUINodeImpl("TreeNode");
        xuiNode.setLevel(5);
        xuiNode.addNamespace("xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
        xuiNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "text", "text", "xs:string",
            "node");
        guiNode = new DefaultMutableTreeNode("node");
        childNodes = new LinkedList();
    }

    /**
     * Constructs the XUI node and GUI node based on the label given.
     *
     * @param label the label to give this newly constructed node.
     */
    public XUITreeNode(String label)
    {
        this();
        xuiNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "text", "text", "xs:string",
            label);
        guiNode.setUserObject(label);
    }

    /**
     *
     */
    public XUITreeNode(XUINode node)
    {
        xuiNode = node;
        guiNode = new DefaultMutableTreeNode(xuiNode.getAttributeValue("text"));
        childNodes = new LinkedList();
    }

    /**
     * Adds a new gui node to this XUINode.
     *
     * @param node the GUI peer to add to this XUITreeNode.
     */
    public void setGUIPeer(DefaultMutableTreeNode node)
    {
        guiNode = node;
    }

    /**
     * Returns this object as a string value.
     *
     * @return the string value of this object.
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer(100);
        sb.append(super.toString() + "\n");
        sb.append("text: " + xuiNode.getAttributeValue("text") + "\n");
        sb.append("id: " + xuiNode.getAttributeValue("id") + "\n");
        sb.append("idref: " + xuiNode.getAttributeValue("idref") + "\n");
        sb.append("XML Node name: " + xuiNode.getName() + "\n");
        sb.append("GUI Node name: " + guiNode);
        return sb.toString();
    }

    /**
     * Sets the parent of this node.
     *
     * @param newParent the parent of this node.
     */
    public void setParent(XUITreeNode newParent)
    {
        parent = newParent;
    }

    /**
     * Returns the actual list of child nodes.
     *
     * @return the child nodes as a list.
     */
    public List getChildNodeList()
    {
        return childNodes;
    }

    /**
     * Returns the parent node of this node. Can be null.
     *
     * @return the parent node of this node.
     */
    public XUITreeNode getParent()
    {
        return parent;
    }

    /**
     * Adds a child to this node's list of direct children.
     */
    public void addChild(XUITreeNode child)
    {
        childNodes.add(child);
        if(xuiNode == null)
            xuiNode.addChildNode(child.xuiNode);
        guiNode.add(child.guiNode);
        child.setParent(this);
    }

    /**
     * Returns the native GUI representation of this node.
     *
     * @return the tree node that is used to place into the native GUI component.
     */
    public TreeNode getGUINodeRepresentation()
    {
        return guiNode;
    }

    /**
     * Returns the XUI node.
     *
     * @return the xml node.
     */
    public XUINode getXUINodeRepresentation()
    {
        return xuiNode;
    }

    /**
     * Returns the ID value based on the XML representation of this node.
     *
     * @return the ID attribute of the XML node.
     */
    public String getIDAttribute()
    {
        return xuiNode.getAttributeID();
    }

    /**
     * Returns the name based on the XML representation of this node.
     *
     * @return the name of the XML node.
     */
    public String getName()
    {
        return xuiNode.getName();
    }

    /**
     * Removes the child node based on the index given.
     *
     * @param index the index of the child to remove.
     * @return the child being removed.
     */
    public XUITreeNode removeChild(int index)
    {
        return (XUITreeNode)childNodes.remove(index);
    }

    /**
     * Removes the child node based on the reference given.
     *
     * @param childNode the child node to remove.
     * @return true if the child was removed, false otherwise.
     */
    public boolean removeChild(XUITreeNode childNode)
    {
        return childNodes.remove(childNode);
    }

    /**
     * Returns all of the children of this current node.
     *
     * @return the list of direct child nodes.
     */
    public List getChildren()
    {
        return childNodes;
    }

    /**
     * Returns the child node at a specific index. May throw an ArrayIndexOutOfBoundsException
     * if the index is out of the bounds of the collection.
     *
     * @param index the index of the child node being referred to.
     */
    public XUITreeNode getChildAt(int index)
    {
        return (XUITreeNode)childNodes.get(index);
    }

    /**
     * Returns the number of child nodes directly found in this node.
     *
     * @return the number of child nodes directly under this node.
     */
    public int getChildCount()
    {
        return childNodes.size();
    }



}
