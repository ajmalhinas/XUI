package org.purnamaproject.xui;

/**
 * @(#)XUINode.java 0.1 18/08/2003
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

import java.util.Hashtable;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;
import org.purnamaproject.xui.component.XUIComponent;

/**
 * Represents a node within the XUI document object model (DOM). Each node can contain
 * other nodes, attributes and be the root node. All concrete classes implementing this
 * interface must pay special care to making sure that the correct order of elements is
 * implemented as well as that all namespaces are added. The XML Schema namespace must
 * also be present.
 *
 * A XUINode has a mapping to XUIComponent which keeps track of the actual platform GUI
 * component.
 *
 * @version 0.1 18/08/2003
 * @author  Arron Ferguson
 * @see <a href="http://www.w3.org/TR/xmlschema-0/">XML Schema Primer</a>
 */
 public interface XUINode
 {

    /**
     * Returns the name of this node.
     *
     * @return the name of this node.
     */
    public String getName();

    /**
     * Sets the name to the new string being given to it. If the string being passed in
     * is null, then no change is made to the underlying textual data.
     *
     * @param newName the new name to assign to this node.
     */
    public void setName(String newName);

    /**
     * Returns the parent of this node.
     *
     * @return the parent of this node.
     */
    public XUINode getParent();

    /**
     * Sets the parent of this node. A parent node is of the same type.
     *
     * @param newParent the new parent to assign to this node.
     */
    public void setParent(XUINode newParent);

    /**
     * Returns the XUI component associated with this node.
     *
     * @return the XUI component of this node.
     */
    public XUIComponent getXUIComponent();

    /**
     * Sets the XUI component of this node. If component is null, no action is taken.
     * This method should never be called directly as it is part of the callback
     * mechanism.
     *
     * @param newComponent the new XUI component to assign to this node.
     */
    public void setXUIComponent(XUIComponent newComponent);

    /**
     * Clears the current attributes.
     */
    public void clearAttributes();

    /**
     * Returns a Boolean value that states whether this node is the root node or not.
     *
     * @return true if it is the root node and false otherwise.
     */
    public boolean isRoot();

    /**
     * Sets this node as the beginning of a particular namespace.
     *
     * @param whetherOrNotBeginOfNamespace whether or not this is the beginning of a namespace.
     * If the beginning of a namespace then this node will display that namespace using the
     * XML namespace attribute.
     */
    public void setIsBeginOfNamespace(boolean whetherOrNotBeginOfNamespace);

    /**
     * Sets this value as being the root. This does not reshuffle the nodes around
     * but rather this value is used when this node marshals itself out to display
     * the XML processing instruction.
     *
     * @param whetherOrNotRootElement determines whether or not this node is to be considered
     * as root.
     */
    public void setToRoot(boolean whetherOrNotRootElement);

    /**
     * Adds a new namespace to this element. If either string is null, then the
     * namespace is not added.
     *
     * @param newNamespace the new namespace prefix.
     * @param newNamespaceURI the new namespace URI.
     */
    public void addNamespace(String newNamespace, String newNamespaceURI);

    /**
     * Returns the level within the hierarchy at which this node sits at.
     *
     * @return the level.
     */
    public int getLevel();

    /**
     * Returns the number of child nodes that this node has. The number is the
     * direct child nodes only.
     *
     * @return the number of direct child nodes.
     */
    public int getNumberOfChildren();

    /**
     * Recursively searches and returns all direct and indirect child nodes. This list
     * is a flattened view of the tree.
     *
     * @return the list of child nodes.
     */
    public List getAllChildNodes();

    /**
     * Returns a list of child nodes based on a particular element name.
     *
     * @param name the name of the element type to look for. All direct child elements
     * of that type (name) will be returned in the list.
     */
    public List getChildNodesByName(String name);

    /**
     * Recursively searches and returns all direct child nodes only.
     *
     * @return the list of child nodes.
     */
    public List getDirectChildren();

    /**
     * Remove all child nodes from the current node. This is an undoable operation.
     */
    public void deleteChildren();

    /**
     * Returns the namespaces of this node as a hashtable.
     *
     * @return the namespace of this node.
     */
    public Hashtable getNamespaces();

    /**
     * Sets the level within the hierarchy at which this node sits at.
     *
     * @param newLevel the level.
     */
    public void setLevel(int newLevel);

    /**
     * Returns the character data for this node as a String buffer.
     *
     * @return the character data as a string.
     */
    public String getCDATA();

    /**
     * Appends to this node's character data.
     *
     * @param newData the new data to append this node's character data to.
     */
    public void appendCDATA(String newData);

    /**
     * Sets the string data for this node. If the new string coming in is null then
     * no change is made. If the string is non-null, then the old textual data is
     * abandoned and the new textual data becomes current.
     *
     * @param newData the new data to set this node's character data to.
     */
    public void setCDATA(String newData);

    /**
     * Adds an attribute to this node. If any of the parameters passed in are null, then the
     * operation is cancelled and no attribute is added. Empty strings are legal.
     *
     * @param uri the uri of the attribute (can be a URL).
     * @param localName the local name of the attribute without any namespace prefix.
     * @param qName the qualified name of the attribute. This includes the prefix of any namespace
     * associated with this attribute.
     * @param type the type of the attribute. (e.g. CDATA)
     * @param value the value of the attribute. This is the textual data stored in it.
     */
    public void addAttribute(String uri, String localName, String qName, String type, String value);

    /**
     * Returns the attribute's id value. The id attribute is required however it can be left as
     * a 0 length string although this is not encouraged as it will limit the ability of the XUI
     * object to search the DOM for nodes (by ID)
     *
     * @see org.purnamaproject.xui.XUI
     */
    public String getAttributeID();

    /**
     * Returns an attribute based on its name.
     *
     * @param attributeName the name of the attribute to use to retrieve its value.
     * @return the value of the attribute searched.
     */
    public String getAttributeValue(String attributeName);

    /**
     * Sets the attributes for this node. If attributes is null, then no change is made.
     *
     * @param newAttributes the name of the attribute.
     */
    public void setAttributes(Attributes newAttributes);

    /**
     * Adds a child node to this node. This is a direct child. If newNode is null,
     * then no change is made.
     *
     * @param node the new node to add to this node.
     */
    public void addChildNode(XUINode node);

    /**
     * Removes a child node based on its id attribute value.
     *
     * @param idValue the id value of the id attribute. Used as a key to reference this
     * node.
     * @return XUINode if the node was found or returns null if the node was not found.
     */
    public XUINode removeChildNode(String idValue);

    /**
     * Removes a child node based on a loose attribute value match. This uses regular
     * expressions allowing a removal based on only a partial match of the id value.
     * Finds, deletes and returns the first node that it finds based on the match.
     *
     * @param idExpressionValue the regular expression used to loosely match a node and
     * remove it. Used as a key to reference this node.
     * @return XUINode if the node was found or returns null if the node was not found.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/regex/Pattern.html#sum">
     * Purnama Project XUI Specification</a>
     */
    public XUINode removeChildNodeOnLooseIDMatch(String idExpressionValue);

    /**
     * Returns the attributes as a <code>Map</code>.
     *
     * @return a map of the attributes.
     */
    public Attributes getAtributes();

    /**
     * Removes a child node from the current node using a reference to that object.
     *
     * @param node the child node to remove.
     */
    public void removeChildNode(XUINode node);

    /**
     * Removes a child node from the current node using an index.
     *
     * @param index the index of the child to remove
     */
    public void removeChildNode(int index);

    /**
     * Returns this node as string data. The data is the entire node including markup,
     * attributes, character data but without any of its children. If the node is the root
     * it will print the XML processing instruction. Pretty serializing is not a requirement.
     *
     * @return the node without the children.
     */
    public String toStringNoChildren();

    /**
     * Returns this node as string data. The data is the entire node including markup,
     * attributes, character data and all other child nodes. If the node is the root
     * it will print the XML processing instruction. Pretty serializing is not a requirement.
     *
     * @return the node and all it's children.
     */
    public String toString();

    /**
     * Returns true or false based on whether or not this node came from the realizer
     * (which means it has been unmarshalled). This method should not be called up by
     * any class other than the Realizer itself.
     *
     * @param state the state (true/false) of whether or not this node was set from the Realizer
     * class. By default this value is false. It is only set to true if the Realizer calls
     * it.
     */
    public void setFromRealizer(boolean state);

    /**
     * Returns whether or not this node has gone through the realizer.
     *
     * @return true if it has gone through the Realizer and false otherwise.
     * @see #setFromRealizer(boolean)
     */
    public boolean getFromRealizer();
}
