package org.purnamaproject.xui;

/**
 * @(#)XUI.java    0.1 18/08/2003
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
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import org.purnamaproject.xui.helpers.Realizer;
import org.purnamaproject.xui.binding.BindingFactory;
import org.purnamaproject.xui.binding.XUIEventSource;
import org.purnamaproject.xui.component.XUIComponent;
import org.purnamaproject.xui.helpers.IDFactory;
import org.purnamaproject.xui.impl.XUINodeImpl;
import org.purnamaproject.xui.component.container.toplevel.XUIWindow;
import org.purnamaproject.xui.binding.XUIBindingException;


/**
 * <p>This Class represents an abstraction of the XUI document. It is not a node but rather a container
 * that holds the root node. There are many convenience methods of the XUI that can be used to access
 * certain types of components.
 *
 * <p>A XUI has one root node. When using the XUIBuilder to parse, the XUIBuilder, when finished, will
 * allow access to a fully created document by passing a XUI object. This Does not mean that the only
 * way to create or retrieve a XUI object is by using a XUIBuilder. A XUI can be created manually or
 * by using another API.
 *
 * @version    0.1 18/08/2003
 * @author     Arron Ferguson
 */
public class XUI
{

    /**
     * The root element of the XUI DOM.
     */
    private XUINode root = null;

    /**
     * Default constructor. By calling this default constructor.
     */
    public XUI()
    {
        // create a default empty DOM
        root = new XUINodeImpl("XUI", "xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
        root.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.addAttribute("", "xsi:schemaLocation", "", "CDATA", "http://xml.bcit.ca/PurnamaProject/2003/xui xui.xsd");
        root.addAttribute("", "id", "", "xs:ID", "http://xml.bcit.ca/PurnamaProject/components");
        List list = getFlattenedView();
        root.setLevel(0);
        root.setToRoot(true);
        root.setIsBeginOfNamespace(true);
        for(int i = 0; i < list.size(); i++)
        {
            XUINode node = (XUINode) list.get(i);
            String id = node.getAttributeID();
            IDFactory.getInstance().addID(id);
        }
    }

    /**
     * Accepts a new XUINode at instantiation time. The node passed in must be a XUI node
     * (which is the root node) otherwise an exception is thrown.
     *
     * @param node the new node to be used as root of this XUI DOM.
     * @throws XUIValidationException if the node passed in is not the root XUI node
     */
    public XUI(XUINode node) throws XUIValidationException
    {
        this();
        if(node == null || !(node.getName().equals("XUI")))
            throw new XUIValidationException("Root node must be a XUI element");
        else
        {
            root = null;
            root = node;
        }
        List list = getFlattenedView();
        for(int i = 0; i < list.size(); i++)
        {
            XUINode n = (XUINode) list.get(i);
            String id = n.getAttributeID();
            IDFactory.getInstance().addID(id);
        }
    }

    /**
     * Adds a new node as root. If this object is null, then no action is taken. If
     * this object is not null then the old root is put up for garbage collection.
     *
     * @param node the new node to be used as root of this XUI DOM.
     */
    public void setRoot(XUINode node) throws XUIValidationException
    {
        if(node == null || !(node.getName().equals("XUI")))
            throw new XUIValidationException("Root node must be an instantiated XUI element");
        else
        {
            root = null;
            root = node;
        }
    }

    /**
     * Returns whether or not the sought after ID exists. This is required for ID references using the
     * xs:IDREF attribute type within the XML Schema. XUI needs to check these in order to adhere to the
     * XML Schema recommendation.
     *
     * @return true if the id exists and false if it does not.
     * @param id the id to check.
     */
    public static boolean containsID(String id)
    {
        return IDFactory.getInstance().containsID(id);
    }

    /**
     * <p>This method adds a resource binding to the XUI document.</p>
     *
     * <p><b>Note: This method does not check any runtime bindings. This is because
     * runtime bindings may not be available during the creation time of this document.</b></p>
     *
     * <p>The name of the class can be any valid name that is accepted within the Java platform. There
     * is only two assumptions made:</p>
     *
     * <ul>
     *  <li>The class specified is found in the jar file from the specified URI</li>
     *  <li>The class specified has a method that matches the signature <code>public void init(XUI)</code></li>
     * <ul>
     *
     * @param className the name of the class that will handle all calls from the user interface.
     */
    public void addBinding(String className, String uri)
    {
        XUINode resource = new XUINodeImpl("Resource");
        resource.setLevel(1);
        resource.addNamespace("xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
        resource.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "type", "type", "xs:string",
            "java");
        resource.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "class", "class", "xs:string",
            className);
        resource.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "uri", "uri", "xs:anyURI",
            uri);

    }

    /**
     * Uses the XUI document's resource element to retrieve the resource. This may throw a
     * <code>MalformedURLException</code> or a <code>XUIBindingException</code> if the code
     * cannot be found.
     *
     * @throws XUIBindingException if the code cannot be bound.
     * @throws MalformedURLException if the url given is malformed.
     */
    public void bind() throws XUIBindingException, MalformedURLException, IOException
    {
        // retrieve the Resource ... if any
        List resourceList = root.getChildNodesByName("Resource");
        if(resourceList != null)
        {
            if(resourceList.size() > 0)
            {
                XUINode resource = (XUINode)resourceList.get(0);
                BindingFactory.getInstance().doBinding(resource, this);
            }
        }
    }

    /**
     * Returns a list of XUIComponents.
     *
     * @return list of XUIComponents.
     */
    public List getXUIComponents()
    {
        List componentNodes = root.getAllChildNodes();
        List components = new LinkedList();
        int size = componentNodes.size();
        for(int i = 0; i < size; i++)
        {
            XUINode node = (XUINode)componentNodes.get(i);
            XUIComponent c = node.getXUIComponent();
            if(c != null && c instanceof XUIComponent)
                components.add(c);
        }
        return components;
    }

    /**
     * Returns a XUIComponent that has implemented the XUIEventSource interface. This allows
     * for code to link to the XUIComponents and 'listen' for events. If no component is found
     * based on that id or if the component found is not of type <code>XUIEventSource</code>,
     * then null is returned.
     *
     * @param id the id of the XUINode.
     * @return an XUIEventSource object which is also a XUIComponent.
     */
    public XUIEventSource getXUIEventSource(String id)
    {
        List eventSources = root.getAllChildNodes();
        int size = eventSources.size();
        for(int i = 0; i < size; i++)
        {
            XUINode node = (XUINode)eventSources.get(i);
            XUIComponent component = node.getXUIComponent();
            if(component != null)
                if(component instanceof XUIEventSource)
                    if(node.getAttributeID().equals(id))
                        return (XUIEventSource)component;
        }
        return null;
    }

    /**
     * Returns the component based on its id value. Returns null if no component found
     * based on that id.
     *
     * @param id the id of the component.
     * @return the XUIComponent.
     */
    public XUIComponent getXUIComponent(String id)
    {
        List components = root.getAllChildNodes();
        int number = components.size();

        for(int i = 0; i < number; i++)
        {
            XUINode node = (XUINode)components.get(i);
            if(node.getAttributeID() != null)
                if(node.getAttributeID().equals(id))
                    return node.getXUIComponent();

        }
        return null;
    }

    /**
     * <p>Adds a new XUI Window to the current XUI DOM that exists. This information comes from
     * the XUI Window - the XUI Window XML content will be used to update this XUI document.</p>
     *
     * @param window the new XUI window to add to this XUI DOM.
     */
    public void addWindow(XUIWindow window) throws XUITypeFormatException
    {
        // add the node
        root.addChildNode(window.getNodeRepresentation());

    }

    /**
     * Returns the root node.
     *
     * @return root node.
     */
    public XUINode getRoot()
    {
        return root;
    }

    /**
     * <p>Calls the native libraries that are used for this API to display the GUI on the
     * desktop. If there is a problem during the creation of the user interface being
     * displayed, an exception will be generated.</p>
     *
     * <p>All top level containers that have been set to visible will be displayed. All
     * top level containers that are not set to visible will still remain in memory but
     * will not be displayed. At any point a component can have its visibility turned off.
     * This will hide the container - it still remains.</p>
     *
     * @throws org.purnamaproject.xui.XUIDisplayException If the user interface could not be displayed, this
     * exception is thrown.
     */
    public void visualize() throws XUIDisplayException
    {
        // call realizer to build the gui
        Realizer.getInstance(this).realize();
    }

    /**
     * Marshals the XUI DOM to the file name specified.
     *
     * @param fileName the name of the file to parse (complete with path). Given as a
     * <code>String</code>.
     * @throws FileNotFoundException if the document cannot be found or if permissions
     * are not allowed.
     */
    public void marshalXUI(String fileName) throws FileNotFoundException, IOException
    {
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(root.toString().getBytes());
        fos.flush();
    }

    /**
     * Marshals the XUI DOM to the file name specified.
     *
     * @param fileName the name of the file to parse (complete with path). Given as a
     * <code>File</code>.
     * @throws FileNotFoundException if the document cannot be found or if permissions
     * are not allowed.
     */
    public void marshalXUI(File fileName) throws FileNotFoundException, IOException
    {
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(root.toString().getBytes());
        fos.flush();
    }

    /**
     * Marshals the XUI DOM to the file output stream specified.
     *
     * @param stream the file output stream to parse to. Given as a
     * <code>FileOutputStream</code>.
     * @throws FileNotFoundException if the document cannot be found or if permissions
     * are not allowed.
     */
    public void marshalXUI(FileOutputStream stream) throws IOException
    {
        stream.write(root.toString().getBytes());
        stream.flush();
    }

    /**
     * Marshals the XUI DOM to the file name specified.
     *
     * @param fileName the name of the file to parse (complete with path). Given as a
     * <code>URL</code>.
     * @throws FileNotFoundException if the document cannot be found or if permissions
     * are not allowed.
     * @throws MalformedURLException if the URL is malformed.
     */
    public void marshalXUI(URL fileName) throws MalformedURLException, FileNotFoundException,
        IOException
    {
        FileOutputStream fos = new FileOutputStream(fileName.toString());
        fos.write(root.toString().getBytes());
        fos.flush();
    }

    /**
     * <p>Returns a list of references to nodes based on their name within the
     * hierarchy. Returns <code>null</code> if there are no nodes based on that
     * level.
     *
     * <p> The references to these nodes are not new objects but rather new
     * references to the existing nodes within the hierarchy. The nodes also
     * have their existing child node lists.
     *
     * @param searchName the node name used to determine which nodes are required.
     * @return a <code>java.util.List</code> of nodes from the specified level.
     */
    public List getNodesByName(String searchName)
    {
        List levelView = new LinkedList();
        List all = (List)root.getAllChildNodes();
        all.add(root);
        int number = all.size();
        for(int i = 0; i < number; i++)
        {
            XUINode node = (XUINode)all.get(i);
            if(node.getName().equals(searchName))
            {
                levelView.add(node);
            }
        }
        return levelView;
    }

    /**
     * <p>Returns a list of references to nodes based on their level within the
     * hierarchy. Returns <code>null</code> if there are no nodes based on that
     * level.
     *
     * <p> The references to these nodes are not new objects but rather new
     * references to the existing nodes within the hierarchy. The nodes also
     * have their existing child node lists.
     *
     * @param level the node level used to determine which nodes are required.
     * @return a <code>java.util.List</code> of nodes from the specified level.
     */
    public List getNodesByLevel(int level)
    {
        List levelView = new LinkedList();
        List all = (List)root.getAllChildNodes();
        all.add(root);
        int number = all.size();
        for(int i = 0; i < number; i++)
        {
            XUINode node = (XUINode)all.get(i);
            if(node.getLevel() == level)
            {
                levelView.add(node);
            }
        }
        return levelView;
    }

    /**
     * <p>Returns a list of references to nodes based on their id attribute
     * within the hierarchy. Returns <code>null</code> if there are no nodes
     * based on that level.
     *
     * <p> The references to these nodes are not new objects but rather new
     * references to the existing nodes within the hierarchy. The nodes also
     * have their existing child node lists.
     *
     * @param id the value of the id attribute. Since the id attribute is a string,
     * all attributes can be searched based on this value.
     * @return a <code>java.util.List</code> of nodes from the specified level.
     */
    public List getNodeByID(String id)
    {
        List levelView = new LinkedList();
        List all = (LinkedList)root.getAllChildNodes();
        all.add(root);
        int number = all.size();
        for(int i = 0; i < number; i++)
        {
            XUINode node = (XUINode)all.get(i);
            if(node.getAttributeID().equals(id))
            {
                levelView.add(node);
            }
        }
        return levelView;
    }

    /**
     * Returns all of the elements within the document as a flat list.
     * This is not a new set of nodes but rather a list that contains
     * a different view of the existing nodes.
     *
     * @return a list (view) of all the elements within the document.
     */
    public List getFlattenedView()
    {
        List flatView = null;
        flatView = (List)root.getAllChildNodes();
        // don't forget to add the root element
        flatView.add(root);
        return flatView;
    }

    /**
     * Returns a list of elements based on their level within the hierarchy.
     * Returns <code>null</code> if there are no elements based on that level.
     *
     * @param level the element level used to determine which elements are required.
     * @return a <code>java.util.Vector</code> of elements from the specified level.
     */
    public List getElementsByLevel(int level)
    {
        List levelView = new LinkedList();
        List all = (List)root.getAllChildNodes();
        all.add(root);
        int number = all.size();
        for(int i = 0; i < number; i++)
        {
            XUINode node = (XUINode)all.get(i);
            if(node.getLevel() == level)
            {
                levelView.add(node);
            }
        }
        return levelView;
    }

}
