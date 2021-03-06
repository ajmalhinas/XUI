package org.purnamaproject.xui.impl;

/**
 * @(#)XUINodeImpl.java 0.5 18/08/2003
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

import java.awt.Component;
import java.awt.Container;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;
import org.purnamaproject.xui.XUINode;
import org.purnamaproject.xui.component.XUIComponent;

/**
 * Concrete class that implements XUINode which represents a node within the XUI document
 * object model (DOM). Each node can contain other nodes, attributes and be the root node.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
 public class XUINodeImpl implements XUINode
 {
    /**
     * Vector of child nodes that belong to this node.
     */
    private List childNodes;

    /**
     * Map of attributes that belong to this node.
     */
    private AttributesImpl attributes;

    /**
     * The Swing GUI component that is mapped to the XUI component.
     */
    private XUIComponent component;

    /**
     * The character data that belongs to this node.
     */
    private StringBuffer cdata;

    /**
     * The XML processing instruction.
     */
    private static String XMLPI = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    /**
     * The comment placed at the beginning of the document.
     */
    private static String API_COMMENT = "<!-- Generated by The Purnama Project XUI API version 0.5 -->";

    /**
     * Denotes the beginning of a namespace.
     */
    private boolean beginOfNamespace = false;

    /**
     * Whether or not this node is the root node of the document
     */
    private boolean isRoot = false;

    /**
     * The hashtable of namespaces using the URI as the key.
     */
    private Hashtable nameSpaces;

    /**
     * The parent node of this current node.
     */
    private XUINode parent = null;

    /**
     * Indicates that this node has gone through the Realizer.
     */
    private boolean viaRealizer = false;

    /**
     * The name of this node.
     */
    private String name;

    /**
     * The level within the document hierarchy that this node sits at.
     */
    private int level = 0;

    /**
     * Default constructor.
     */
    public XUINodeImpl()
    {
        childNodes = new Vector();
        attributes = new AttributesImpl();
        nameSpaces = new Hashtable(2);
        name = "noname";
        cdata = new StringBuffer(500);
    }

    /**
     * Constructor that accepts the name of the node.
     *
     * @param newName the name of this node.
     */
    public XUINodeImpl(String newName)
    {
        this();
        if(newName != null)
            name = newName;
    }

    /**
     * Constructor that accepts the name of the node as well as it's namespace.
     *
     * @param newName the name of this node.
     * @param newNamespace the namespace of this node.
     * @param newNamespaceURI the namespace URI of this node.
     */
    public XUINodeImpl(String newName, String newNamespace, String newNamespaceURI)
    {
        this(newName);
        if(newNamespace == null || newNamespaceURI == null)
        {
            newNamespace = "";
            newNamespaceURI = "";
        }
        addNamespace(newNamespace, newNamespaceURI);
    }

    /**
     * Constructor that accepts the name of the node as well as it's namespace.
     *
     * @param newName the name of this node.
     * @param newNamespace the namespace of this node.
     * @param newNamespaceURI the namespace URI of this node.
     * @param isRoot whether or not this node is the root node of the node.
     */
    public XUINodeImpl(String newName, String newNamespace, String newNamespaceURI, boolean isRoot)
    {
        this(newName, newNamespace, newNamespaceURI);
        this.isRoot = isRoot;
    }

    /**
     * Constructor that accepts the name of the node as well as it's namespace.
     *
     * @param newName the name of this node.
     * @param newNamespace the namespace of this node.
     * @param newNamespaceURI the namespace URI of this node.
     * @param isRoot whether or not this node is the root node of the document.
     * @param beginNamespace whether or not this node is the beginning of a namespace.
     */
    public XUINodeImpl(String newName, String newNamespace, String newNamespaceURI,
        boolean isRoot, boolean beginNamespace)
    {
        this(newName, newNamespace, newNamespaceURI, isRoot);
        this.beginOfNamespace = beginNamespace;
    }

    /**
     * Returns the XUI component associated with this node.
     *
     * @return the XUI component of this node.
     */
    public XUIComponent getXUIComponent()
    {
        return component;
    }

    /**
     * Sets the XUI component of this node. If component is null, no action is taken.
     * This method should never be called directly as it is part of the callback
     * mechanism.
     *
     * @param newComponent the new XUI component to assign to this node.
     */
    public void setXUIComponent(XUIComponent newComponent)
    {
        // retrieve all values from the GUI component.
        if(newComponent == null)
            return;
        component = newComponent;
    }

    /**
     * Returns true or false based on whether or not this node came from the realizer
     * (which means it has been unmarshalled). This method should not be called up by
     * any class other than the Realizer itself.
     *
     * @param state the state (true/false) of whether or not this node was set from the Realizer
     * class. By default this value is false. It is only set to true if the Realizer calls
     * it.
     */
    public void setFromRealizer(boolean state)
    {
        viaRealizer = state;
    }

    /**
     * Returns whether or not this node has gone through the realizer.
     *
     * @return true if it has gone through the Realizer and false otherwise.
     * @see #setFromRealizer(boolean)
     */
    public boolean getFromRealizer()
    {
        return viaRealizer;
    }

    /**
     * Returns the parent of this node. This may be null if it has not been
     * previously set.
     *
     * @return the parent of this node.
     */
    public XUINode getParent()
    {
        return parent;
    }

    /**
     * Sets the parent of this node. A parent node is of the same type. If the
     * value is null, no change is made.
     *
     * @param newParent the new parent to assign to this node.
     */
    public void setParent(XUINode newParent)
    {
        if(newParent != null)
            parent = newParent;
    }

    /**
     * Returns the name of this node.
     *
     * @return the name of this node.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name to the new string being given to it. If the string being passed in
     * is null, then no change is made to the underlying textual data.
     *
     * @param newName the new name to assign to this node.
     */
    public void setName(String newName)
    {
        if(newName != null)
            name = newName;
    }

    /**
     * Returns a Boolean value that states whether this node is the root node or not. By default,
     * when a node is created, it is set to false. Making a call to setToRoot must be made in order
     * to make this node a root node.
     *
     * @see #setToRoot(boolean)
     * @return true if it is the root node and false otherwise.
     */
    public boolean isRoot()
    {
        return isRoot;
    }

    /**
     * Sets this node as the beginning of a particular namespace.
     *
     * @param whetherOrNotBeginOfNamespace whether or not this is the beginning of a namespace.
     * If the beginning of a namespace then this node will display that namespace using the
     * XML namespace attribute.
     */
    public void setIsBeginOfNamespace(boolean whetherOrNotBeginOfNamespace)
    {
        this.beginOfNamespace = whetherOrNotBeginOfNamespace;
    }

    /**
     * Sets this value as being the root. This does not reshuffle the nodes around
     * but rather this value is used when this node marshals itself out to display
     * the XML processing instruction.
     *
     * @param whetherOrNotRootnode determines whether or not this node is to be considered
     * as root.
     */
    public void setToRoot(boolean whetherOrNotRootnode)
    {
        this.isRoot = whetherOrNotRootnode;
    }

    /**
     * Adds a new namespace to this element. If either string is null, then the
     * namespace is not added.
     *
     * @param newNamespace the new namespace prefix.
     * @param newNamespaceURI the new namespace URI.
     */
    public void addNamespace(String newNamespace, String newNamespaceURI)
    {
        if(newNamespace == null || newNamespaceURI == null)
        {
            newNamespace = "";
            newNamespaceURI = "";
        }
        nameSpaces.put(newNamespaceURI, newNamespace);
    }

    /**
     * Returns the level within the hierarchy at which this node sits at.
     *
     * @return the level.
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * Returns the number of child nodes that this node has. The number is the
     * direct child nodes only.
     *
     * @return the number of direct child nodes.
     */
    public int getNumberOfChildren()
    {
        return childNodes.size();
    }

    /**
     * Recursively searches and returns all direct child nodes only.
     *
     * @return the list of child nodes.
     */
    public List getDirectChildren()
    {
        return childNodes;
    }

    /**
     * Recursively searches and returns all direct and indirect child nodes. This list
     * is a flattened view of the tree.
     *
     * @return the list of child nodes.
     */
    public List getAllChildNodes()
    {
        List list = new Vector();
        int n = childNodes.size();
        for(int i = 0; i < n; i++)
        {
            XUINode child = (XUINode)childNodes.get(i);
            list.add(child);
            if(child.getNumberOfChildren() > 0)
            {
                List grandChildren = child.getAllChildNodes();
                list.addAll(grandChildren);

            }
        }
        return list;
    }

    /**
     * Remove all child nodes from the current node. This is an undoable operation.
     */
    public void deleteChildren()
     {
        int n = childNodes.size();
        for(int i = 0; i < n; i++)
        {
            childNodes.remove(i);
        }
     }

    /**
     * Removes a child node from the current node using a reference to that object.
     *
     * @param node the child node to remove.
     */
    public void removeChildNode(XUINode node)
     {
        // first delete the GUI component that makes up this element.
        XUIComponent xcomponent = node.getXUIComponent();
        if(xcomponent != null)
         {
            Component component = xcomponent.getPeer();
            if(component != null)
            {
                Container parent = component.getParent();
                if(parent != null)
                {
                    // then there is a parent and so remove this component from it
                    parent.remove(component);
                    parent.invalidate();
                    parent.validate();
                    parent.repaint();
                }
                childNodes.remove(node);
                component = null;
                node = null;
            }
        } else
        {
            childNodes.remove(node);
        }
     }

    /**
     * Removes a child node from the current node using an index.
     *
     * @param index the index of the child to remove
     */
    public void removeChildNode(int index)
     {
        childNodes.remove(index);
     }

    /**
     * Returns the namespaces of this node as a hashtable.
     *
     * @return the namespace of this node.
     */
    public Hashtable getNamespaces()
    {
        return nameSpaces;
    }

    /**
     * Sets the level within the hierarchy at which this node sits at.
     *
     * @param newLevel the level.
     */
    public void setLevel(int newLevel)
    {
        if(newLevel > -1)
            level = newLevel;
    }

    /**
     * Returns the character data for this node as a String buffer.
     *
     * @return the character data as a string.
     */
    public String getCDATA()
    {
        return cdata.toString();
    }

    /**
     * Appends to this node's character data.
     *
     * @param newData the new data to append this node's character data to.
     */
    public void appendCDATA(String newData)
    {
        if(newData != null)
            cdata.append(newData);
    }

    /**
     * Sets the string data for this node. If the new string coming in is null then
     * no change is made. If the string is non-null, then the old textual data is
     * abandoned and the new textual data becomes current.
     *
     * @param newData the new data to set this node's character data to.
     */
    public void setCDATA(String newData)
    {
        if(newData != null)
        {
            cdata = null;
            cdata = new StringBuffer(newData);
        }
    }

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
    public void addAttribute(String uri, String localName, String qName, String type, String value)
    {
        if(uri == null || localName == null || qName == null || type == null || value == null)
            return;
        String name = "";
        if(localName.length() == 0)
            localName = qName;
        if(qName.length() == 0)
            qName = localName;
        int index = attributes.getIndex(localName);
        if(index == -1)
            attributes.addAttribute(uri, localName, qName, type, value);
        else
        {
            attributes.removeAttribute(index);
            attributes.addAttribute(uri, localName, qName, type, value);
        }
    }

    /**
     * Returns the attribute's id value. The id attribute is required however it can be left as
     * a 0 length string although this is not encouraged as it will limit the ability of the XUI
     * object to search the DOM for nodes (by ID)
     *
     * @see org.purnamaproject.xui.XUI
     * @return the attribute ID of this node.
     */
    public String getAttributeID()
    {
        return attributes.getValue("id");
    }

    /**
     * Returns an attribute based on its name.
     *
     * @param attributeName the name of the attribute to use to retrieve its value.
     * @return the value of the attribute searched.
     */
    public String getAttributeValue(String attributeName)
     {
        return attributes.getValue(attributeName);
     }

    /**
     * Clears the current attributes.
     */
    public void clearAttributes()
    {
        attributes = null;
        attributes = new AttributesImpl();
    }

    /**
     * Sets the attributes for this node. If attributes is null, then no change is made.
     *
     * @param newAttributes the name of the attribute.
     */
    public void setAttributes(Attributes newAttributes)
    {
        if(newAttributes != null)
            attributes = (AttributesImpl)newAttributes;
    }

    /**
     * Adds a child node to this node. This is a direct child. If newNode is null,
     * then no change is made.
     *
     * @param newNode the new node to add to this node.
     */
    public void addChildNode(XUINode newNode)
    {
        if(newNode != null)
        {
            childNodes.add(newNode);
            newNode.setParent(this);
        }
    }

    /**
     * Returns a list of child nodes based on a particular element name.
     *
     * @param name the name of the element type to look for. All direct child elements
     * of that type (name) will be returned in the list.
     */
    public List getChildNodesByName(String name)
    {
        List nodesByName = new Vector();
        for(int i = 0; i < childNodes.size(); i++)
        {
            XUINode node = (XUINode)childNodes.get(i);
            if(node.getName().equals(name))
                nodesByName.add(node);
        }
        return nodesByName;
    }

    /**
     * Removes a child node based on its id attribute value.
     *
     * @param idValue the id value of the id attribute. Used as a key to reference this
     * node.
     * @return XUINode if the node was found or returns null if the node was not found.
     */
    public XUINode removeChildNode(String idValue)
    {
        int s = childNodes.size();
        for(int i = 0; i < s; i++)
        {
            XUINode n = (XUINode)childNodes.get(i);
            if(n.getAttributeID().equals(idValue))
                return n;
        }
        return null;
    }

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
    public XUINode removeChildNodeOnLooseIDMatch(String idExpressionValue)
    {
        int s = childNodes.size();
        for(int i = 0; i < s; i++)
        {
            XUINode n = (XUINode)childNodes.get(i);
            if(n.getAttributeID().matches(idExpressionValue))
                return n;
        }
        return null;
    }

    /**
     * Returns the attributes as a <code>Map</code>.
     *
     * @return a map of the attributes.
     */
    public Attributes getAtributes()
    {
        return attributes;
    }

    /**
     * Returns this node as string data. The data is the entire node including markup,
     * attributes, character data but without any of its children. If the node is the root
     * it will print the XML processing instruction.
     *
     * @return the node without the children.
     */
    public String toStringNoChildren()
    {
        StringBuffer sb = new StringBuffer();
        String namespacePrefix = "";
        // insert indenting ... 2 spaces for now.
        if(isRoot)
        {
            sb.append(XMLPI + "\n");
            sb.append(API_COMMENT + "\n");
        } else
        {
            sb.append("\n");
            for(int s = 0; s < level; s++)
            {
                sb.append("  ");
            }
        }
        sb.append("<");
        // get namespaces for this node
        Enumeration keys = nameSpaces.keys();

        String names = "";
        while(keys.hasMoreElements())
        {
            String uri = (String)keys.nextElement();
            String prefix = (String)nameSpaces.get(uri);
            /* if its the xsi namespace (XML Schema Instance),
             * ignore it, we aren't part of that namespace but rather
             * we need it for the XML Schema validator to work. */
            if(!(prefix.equals("xsi")))
            {
                sb.append(prefix + ":");
                namespacePrefix = prefix;
            }
            names += (" " + "xmlns:" + prefix + "=\"" + uri + "\"");
        }
        if(beginOfNamespace)
            sb.append(name + names);
        else
            sb.append(name);

        // do attributes if there are any
        if(attributes.getLength() > 0)
        {
            int length = attributes.getLength();
            for(int i = 0; i < length; i++)
            {
                String attributeValue = attributes.getValue(i);
                String attributeQName = attributes.getQName(i);
                sb.append(" " + attributeQName + "=\"" + attributeValue + "\"");
            }
        }
        sb.append(">");
        sb.append(cdata);
        int size = childNodes.size();
        if(size > 0)
        {
            sb.append("\n");
            for(int s = 0; s < (level); s++)
                sb.append("  ");
        }
        if(namespacePrefix.length() > 0)
            sb.append("</" + namespacePrefix + ":" + name + ">");
        else
            sb.append("</" + name + ">");

        return sb.toString();
    }

    /**
     * Returns this node as string data. The data is the entire node including markup,
     * attributes, character data and all other child nodes. If the node is the root
     * it will print the XML processing instruction.
     *
     * @return the node and all it's children.
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        String namespacePrefix = "";
        // insert indenting ... 2 spaces for now.
        if(isRoot)
        {
            sb.append(XMLPI + "\n");
            sb.append(API_COMMENT + "\n");
        } else
        {
            sb.append("\n");
            for(int s = 0; s < level; s++)
            {
                sb.append("  ");
            }
        }
        sb.append("<");
        // get namespaces for this node
        Enumeration keys = nameSpaces.keys();

        String names = "";
        while(keys.hasMoreElements())
        {
            String uri = (String)keys.nextElement();
            String prefix = (String)nameSpaces.get(uri);
            /* if its the xsi namespace (XML Schema Instance),
             * ignore it, we aren't part of that namespace but rather
             * we need it for the XML Schema validator to work. */
            if(!(prefix.equals("xsi")))
            {
                sb.append(prefix + ":");
                namespacePrefix = prefix;
            }
            names += (" " + "xmlns:" + prefix + "=\"" + uri + "\"");
        }
        if(beginOfNamespace)
            sb.append(name + names);
        else
            sb.append(name);

        // do attributes if there are any
        if(attributes.getLength() > 0)
        {
            int length = attributes.getLength();
            for(int i = 0; i < length; i++)
            {
                String attributeValue = attributes.getValue(i);
                String attributeQName = attributes.getQName(i);
                sb.append(" " + attributeQName + "=\"" + attributeValue + "\"");
            }
        }
        sb.append(">");
        sb.append(cdata);
        int size = childNodes.size();
        for(int i = 0; i < size; i++)
        {
            XUINode e = (XUINode)childNodes.get(i);
            sb.append(e.toString());
        }
        if(size > 0)
        {
            sb.append("\n");
            for(int s = 0; s < (level); s++)
                sb.append("  ");
        }
        if(namespacePrefix.length() > 0)
            sb.append("</" + namespacePrefix + ":" + name + ">");
        else
            sb.append("</" + name + ">");

        return sb.toString();
    }


}
