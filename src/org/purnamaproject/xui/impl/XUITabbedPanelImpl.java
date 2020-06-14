package org.purnamaproject.xui.impl;

/**
 * @(#)XUITabbedPanelImpl.java  0.5 18/08/2003
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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import org.purnamaproject.xui.helpers.IDFactory;
import org.purnamaproject.xui.binding.XUIModel;
import org.purnamaproject.xui.component.XUIComponent;
import org.purnamaproject.xui.component.container.XUIContainer;
import org.purnamaproject.xui.component.container.intermediate.XUITabbedPanel;
import org.purnamaproject.xui.component.container.intermediate.XUIPanel;
import org.purnamaproject.xui.GridMapping;
import org.purnamaproject.xui.impl.XUINodeImpl;
import org.purnamaproject.xui.XUIDisplayException;
import org.purnamaproject.xui.XUINode;
import org.purnamaproject.xui.XUITypeFormatException;

/**
 * The implementation of a XUITabbedPanel.
 *
 * @version 0.5 18/08/2003
 * @author  Arron Ferguson
 */
public class XUITabbedPanelImpl implements XUITabbedPanel
{
    /**
     * List of supported components that can be placed inside of this one.
     */
    private static LinkedList supportedComponents = null;

    static
    {
        supportedComponents = new LinkedList();
        supportedComponents.add("Panel");
    }

    /**
     * The native representation of this component.
     */
    private JTabbedPane panel;

    /**
     * Node associated with this component.
     */
    private XUINode tabbedNode = null;

    /**
     * The hashtable containing rectangles representing the regions that are mapped to child components.
     */
    private Hashtable childComponentMappings = null;

    /**
     * The grid width of this component (in grid cells).
     */
    private int gridWidth = 1;

    /**
     * The grid height of this component (in grid cells).
     */
    private int gridHeight = 1;

    /**
     * The grid x coordinate of this component (within the grid).
     */
    private int gridX = 0;

    /**
     * The grid y coordinate of this component (within the grid).
     */
    private int gridY = 0;

    /**
     * <p>Constructs a new XUITabbedPanel implementation and passing it its XML node rather
     * than requiring the XUITabbedPanel implementation to build it from scratch. This
     * method is called by the Realizer when it reads the XML document from the file
     * system.</p>
     *
     * <p><b><u>Note:</u></b> This constructor is not normally something that should be called up.
     * This constructor is required when creating a XUI TabbedPanel where the node has already been built
     * by the realizer or a subclass thereof.</p>
     *
     * <p><b><u>Note:</u></b> Schema checking is assumed at this point. Any node passed to this method
     * will be assumed to have passed through the schema validator.</p>
     *
     * @see #setComponentSize(int, int)
     * @see #setComponentLocation(int, int)
     * @throws org.purnamaproject.xui.XUIDisplayException if the component is being placed overtop of an existing component on the
     * layout.
     * @param node the new node representing this GUI component.
     */
    public XUITabbedPanelImpl(XUINode node)
    {
        gridX = Integer.parseInt(node.getAttributeValue("x"));
        gridY = Integer.parseInt(node.getAttributeValue("y"));
        gridWidth = Integer.parseInt(node.getAttributeValue("width"));
        gridHeight = Integer.parseInt(node.getAttributeValue("height"));

        if(!(node.getName().equals("TabbedPanel")))
            throw new XUIDisplayException("Node for XUITabbedPanel must be named 'TabbedPanel' and conform to the XUI schema.");
        else
        {
            // gui component
            tabbedNode = node;

            // make the new mappings table
            childComponentMappings = new Hashtable();

            panel = new JTabbedPane();



            // add as XUI component
            tabbedNode.setXUIComponent(this);

            setComponentLocation(gridX, gridY);

        }

    }

    /**
     * Default XUITabbedPanel with a size of 1 x 1 cells within the
     * layout in the top left corner of the container.
     */
    public XUITabbedPanelImpl()
    {
        panel = new JTabbedPane();


        // make the new mappings table
        childComponentMappings = new Hashtable();

        // now create the XML version of this element
        tabbedNode = new XUINodeImpl("TabbedPanel");
        tabbedNode.setLevel(2);
        tabbedNode.addNamespace("xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
        tabbedNode.setXUIComponent(this);

        // add window specific attributes
        tabbedNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "id", "id", "xs:ID",
            IDFactory.getInstance().generateID("panel"));
        tabbedNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "x", "x", "xs:unsignedShort",
            "0");
        tabbedNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "y", "y", "xs:unsignedShort",
            "0");
        tabbedNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
            "1");
        tabbedNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
            "1");

    }

    /**
     * Constructor for creating a new XUITabbedPanel component. Component will be centered using the width
     * and height given.
     *
     * @param width the width of this XUITabbedPanel.
     * @param height the height of this XUITabbedPanel.
     * @throws org.purnamaproject.xui.XUITypeFormatException if the width and the height are not within the parent's grid.
     */
    public XUITabbedPanelImpl(int width, int height) throws XUIDisplayException
    {
        this();

        panel.setSize(width, height);

        // XML update
        tabbedNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
            "" + width);
        tabbedNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
            "" + height);

    }

    /**
     * Returns the XUI id reference of this component.
     *
     * @return the id reference of this component as a string.
     * @see #setIDRef(String)
     */
    public String getIDRef()
    {
        return tabbedNode.getAttributeValue("idref");
    }

    /**
     * Sets the XUI id reference of this component. This value is a reference to parent container that
     * this component is a child of. The string values allowed can contain both alphabetic and numeric
     * characters. The first character must start with an alphabetic character. Real-time validation
     * is performed and therefor if a wrong value is entered, an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#IDREF">W3C XML Schema, section 3.3.9</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema IDREF
     * type, or if the string is null, an exception occurs.
     * @param newIDRef the new ID reference value to assign to this component.
     */
    public void setIDRef(String newIDRef) throws XUITypeFormatException
    {
        if(!(IDFactory.getInstance().containsID(newIDRef)))
            throw new XUITypeFormatException("The ID reference must reference an existing ID.");
        if(newIDRef.matches("[^a-zA-Z]"))
            tabbedNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "idref", "idref", "xs:IDREF",
                newIDRef);
        else
            throw new XUITypeFormatException("ID reference must start with alphabetic only: a - z or A - Z.");
    }

    /**
     * Returns the x coordinate of this component. For a top level container, this coordinate is the actual
     * x coordinate value (in pixels) that appears on the desktop. For an intermediate component, this x
     * coordinate is the position within the layout grid that is assigned to the top level container.
     *
     * @return the x coordinate of this component.

     */
    public int getX()
    {
        return gridX;
    }

    /**
     * Returns the y coordinate of this component. For a top level container, this coordinate is the actual
     * y coordinate value (in pixels) that appears on the desktop. For an intermediate component, this y
     * coordinate is the position within the layout grid that is assigned to the top level container.
     *
     * @return the y coordinate of this component.

     */
    public int getY()
    {
        return gridY;
    }

    /**
     * Returns the width of this component. For a top level container, this value is the actual width
     * (in pixels) that appears on the desktop. For an intermediate component, this width value is the
     * position within the layout grid that is assigned to the top level container (e.g. 2 would be two
     * cells wide.
     *
     * @return the width of this component.

     */
    public int getWidth()
    {
        return gridWidth;
    }

    /**
     * Returns the height of this component. For a top level container, this value is the actual height
     * (in pixels) that appears on the desktop. For an intermediate component, this height value is the
     * position within the layout grid that is assigned to the top level container (e.g. 2 would be two
     * cells high.
     *
     * @return the height of this component.

     */
    public int getHeight()
    {
        return gridHeight;
    }

    /**
     * Removes all of the components from this container. If no components found then this method
     * does nothing.
     */
    public void removeAllComponents()
    {
        panel.removeAll();
        childComponentMappings.clear();
        // and same for all the nodes in the tree:
        List list = tabbedNode.getAllChildNodes();
        for(int i = 0; i < list.size(); i++)
        {
            XUINode node = (XUINode)list.get(i);
            if(node.getName().equals("GridLayout"))
                ; // keep
            else
                list.remove(node);
        }
    }

    /**
     * <p>Adds a new component to this container. The type of component must adhere to the XUI specification.
     * Real-time validation is performed and therefor if the wrong type of component is added, an exception
     * is generated. For each type of container (each concrete class), there will be a listed set of
     * components supported by that particular container.</p>
     *
     * <p><b>This method appends the new component (in this case a XUIPanel) to a new tab. Uses the
     * XUIPanel's name for the tab title.</b></p>
     *
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @see #getSupportedComponents()
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the
     * <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>.
     * @param component the component to add.
     */
    public void addComponent(XUIComponent component) throws XUITypeFormatException
    {
        if(component == null)
            throw new XUITypeFormatException("Component must not be null.");
        else if(!(component instanceof XUIPanel))
            throw new XUITypeFormatException("Component must be a XUIPanel.");
        else
        {
            panel.add(component.getPeer(), component.getNodeRepresentation().getAttributeValue("name"));
        }
    }

    /**
     * Removes the component based on its object reference. If no component found by that reference,
     * no action is taken.
     *
     * @param component the component to remove.
     */
    public void removeComponent(XUIComponent component)
    {
        // flattened list
        List list = tabbedNode.getAllChildNodes();
        for(int i = 0; i < list.size(); i++)
        {
            // get the node
            XUINode node = (XUINode)list.get(i);
            // get the component
            XUIComponent c = node.getXUIComponent();
            if(c == component)
            {
                childComponentMappings.remove(c);
                // same component so, remove it from the GUI
                Component swingComponent = c.getPeer();
                panel.remove(swingComponent);
                panel.invalidate();
                // force the GUI to repaint itself
                if(panel.getParent() != null)
                    panel.getParent().validate();
                // remove the node from the XUI document
                tabbedNode.removeChildNode(node);
            }
        }
    }

    /**
     * Removes the component based on its id. If no component found by that id, no action is taken.
     *
     * @param id the id of the component to remove.
     */
    public void removeComponent(String id)
    {
        // flattened list
        List list = tabbedNode.getAllChildNodes();
        for(int i = 0; i < list.size(); i++)
        {
            // get the node
            XUINode node = (XUINode)list.get(i);
            // get the component
            String nodeID = node.getAttributeID();
            XUIComponent c = node.getXUIComponent();

            if(nodeID.equals(id))
            {
                childComponentMappings.remove(c);
                // same component so, remove it from the GUI
                Component swingComponent = c.getPeer();
                panel.remove(swingComponent);
                panel.invalidate();
                // force the GUI to repaint itself
                if(panel.getParent() != null)
                    panel.getParent().validate();
                // remove the node from the XUI document
                tabbedNode.removeChildNode(node);
            }
        }
    }

    /**
     * Gets the component based on its id. If no component found by that id, null is returned.
     *
     * @param id the id of the component to get. Component remains.
     */
    public XUIComponent getComponent(String id)
    {
        // flattened list
        List list = tabbedNode.getAllChildNodes();
        for(int i = 0; i < list.size(); i++)
        {
            // get the node
            XUINode node = (XUINode)list.get(i);
            // get the component
            String nodeID = node.getAttributeID();
            XUIComponent c = node.getXUIComponent();

            if(nodeID.equals(id))
                return c;
        }
        return null;
    }

    /**
     * Gets all the components in this container. If no components found, null is returned.
     *
     * @return a list of the components that are currently children of this container.
     */
    public List getChildrenComponents()
    {
        List components = new LinkedList();
        List list = tabbedNode.getAllChildNodes();
        for(int i = 0; i < list.size(); i++)
        {
            XUINode node = (XUINode)list.get(i);
            components.add(node.getXUIComponent());
        }
        return components;
    }

    /**
     * This method returns a list of strings. Each string is a fully qualified name of the components that
     * are supported. Supported means that the component can be added to this component. It is an error to add
     * a component that is not supported. An exception is generated.
     *
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @return a list of Strings where each string is the fully qualified name of each component supported.
     */
    public List getSupportedComponents()
    {
        return supportedComponents;
    }

    /**
     * Returns the XUI id of this component.
     *
     * @return the id of this component as a string.

     */
    public String getID()
    {
        return tabbedNode.getAttributeValue("id");
    }

    /**
     * Represents the XUI window component as a XUI node.
     *
     * @return org.purnamaproject.xui.XUINode the XML element as a XUI node.
     */
    public XUINode getNodeRepresentation()
    {
        return tabbedNode;
    }

    /**
     * This method returns the peer within the host operating system and user interface environment.
     * For each environment, this will return a different object type and may not be represented as
     * a class called component.
     *
     * @return Component which is the native representation of the GUI component.
     */
    public Component getPeer()
    {
        return panel;
    }

    /**
     * <p>Sets the location of this component using x and y coordinates. This value will be different for top-level
     * containers as compared to intermediate level containers and atomic and composite components. For example,
     * for intermediate level containers, atomic components and composite components, the x and y coordinate
     * is the position within the layout grid. The x and y values must fall within the XUI schema limits
     * (0 to 65,655) as well as within the limits of the layout.</p>
     *
     * <p>If the integer number does not fall within the number 0 to 65,535, then a <code>XUIDisplayException</code>
     * is thrown and no change is made. If the integer number falls within this schema limit but outside of the
     * range of the grid, (i.e. grid is 5 x 5 but the value given starts at coordinate 8), then no update to the
     * layout is made (i.e. the component is not added to the layout.)</p>
     *
     * <p>If the coordinate values fall within the xml schema limits and falls within the grid range but another
     * component is currently inhabiting that area on the grid, then a <code>XUIDisplayException</code> is
     * thrown and no change is made (i.e. the component is not added to the layout.)</p>
     *
     * <p>If the component is a top-level container, then the x and y coordinate values refer to the screen
     * coordinates on the screen. The same rules apply to adhering to the integer range constraint. If the
     * top-level container is placed outside of the screen resolution of the host system, then a
     * <code>XUIDisplayException</code> is thrown and no change is made.</p>
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#unsignedShort">W3C XML Schema, section 3.3.23</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUIDisplayException if the component is being placed overtop of an existing component on the
     * layout.
     * @param yCoordinate the y coordinate.
     * @param xCoordinate the x coordinate.
     */
    public void setComponentLocation(int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        if(xCoordinate < 0 || xCoordinate > 65535)
            throw new XUIDisplayException("x and y values must be within the range of 0 - 65535.");
        if(yCoordinate < 0 || yCoordinate > 65535)
            throw new XUIDisplayException("x and y values must be within the range of 0 - 65535.");
        else
        {
            Container container = null;
            XUINode parentNode = tabbedNode.getParent();
            GridMapping parent = null;
            if(parentNode != null)
                parent = (GridMapping)parentNode.getXUIComponent();

            // proposed space to occupy
            Rectangle spaceToOccupy = new Rectangle(yCoordinate, xCoordinate, gridWidth, gridHeight);


            if(parent != null)
            {

                // collision detection
                Hashtable grid = parent.getGridMapping();

                Enumeration keys = grid.keys();

                container = (Container)((XUIComponent)parent).getPeer();

                // need to check if the space is occupied AND if that space if occupied by
                // someone OTHER than the current proposed component
                while(keys.hasMoreElements())
                {
                    XUIContainer someComponent = (XUIContainer)keys.nextElement();
                    Rectangle region = (Rectangle)grid.get(someComponent);
                    if(region.intersects(spaceToOccupy) && someComponent != this)
                        throw new XUIDisplayException("Component:\n" + someComponent + " is already in the area x: "
                            + yCoordinate + ", y: " + xCoordinate + ", width: " + gridWidth + ", height: "
                            + gridHeight);
                }

                // now check if the atomic falls within the grid as specified by the container.
                Dimension d = parent.getGrid();
                if(yCoordinate < 0 || (yCoordinate + gridWidth) > (d.getWidth()))
                    throw new XUIDisplayException("x & y coordinates must fall within the grid layout of the container.");
                else if(xCoordinate < 0 || (xCoordinate + gridHeight) > (d.getHeight()))
                    throw new XUIDisplayException("x & y coordinates must fall within the grid layout of the container.");
                else
                {
                    gridX = xCoordinate;
                    gridY = yCoordinate;
                    container.remove(panel);
                    if(container instanceof JFrame)
                    {
                        JFrame f = (JFrame)container;
                        f.getContentPane().add(panel, new Rectangle(gridY, gridX, gridWidth, gridHeight));
                    }
                    else
                        container.add(panel, new Rectangle(gridY, gridX, gridWidth, gridHeight));
                    panel.invalidate();
                    container.validate();

                    tabbedNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "x", "x", "xs:unsignedShort",
                        String.valueOf(xCoordinate));
                    tabbedNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "y", "y", "xs:unsignedShort",
                        String.valueOf(yCoordinate));
                }
            }

        }
    }

    /**
     * <p>Sets the size of this component using width and height integers. This value will be different for top-level
     * containers as compared to intermediate level containers and atomic and composite components. For example,
     * for intermediate level containers, atomic components and composite components, the width and height integers
     * are the size in cells within the layout grid. The width and height values must fall within the XUI schema
     * limits (0 to 65,655) as well as within the limits of the layout.</p>
     *
     * <p>If the integer number does not fall within the number 0 to 65,535, then a <code>XUIDisplayException</code>
     * is thrown and no change is made. If the integer number falls within this schema limit but outside of the
     * range of the grid, (i.e. grid is 5 x 5 but the value given for width is 8), then no update to the
     * layout is made (i.e. the component is not added to the layout.)</p>
     *
     * <p>If the dimension values falls within the xml schema limits and falls within the grid range but another
     * component is currently inhabiting that area on the grid, then a <code>XUIDisplayException</code> is
     * thrown and no change is made (i.e. the component is not added to the layout.)</p>
     *
     * <p>If the component is a top-level container, then the width and height values refer to the size in pixels
     * instead of grid cells. The same rules apply to adhering to the integer range constraint. If the
     * top-level container is wider or taller than the screen resolution of the host system, then a
     * <code>XUIDisplayException</code> is thrown and no change is made.</p>
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#unsignedShort">W3C XML Schema, section 3.3.23</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUIDisplayException if the component is being placed overtop of an existing component on the
     * layout.
     * @param width the width of the component.
     * @param height the height of the component.
     */
    public void setComponentSize(int width, int height) throws XUIDisplayException
    {
        if(width < 0 || width > 65535)
            throw new XUIDisplayException("width and height values must be within the range of 0 - 65535.");
        if(height < 0 || height > 65535)
            throw new XUIDisplayException("width and height values must be within the range of 0 - 65535.");
        else
        {

            Container container = null;
            XUINode parentNode = tabbedNode.getParent();
            GridMapping parent = null;
            if(parentNode != null)
                parent = (GridMapping)parentNode.getXUIComponent();

            // proposed space to occupy
            Rectangle spaceToOccupy = new Rectangle(gridY, gridX, width, height);


            if(parent != null)
            {

                // collision detection
                Hashtable grid = parent.getGridMapping();

                Enumeration keys = grid.keys();

                container = (Container)((XUIComponent)parent).getPeer();

                // need to check if the space is occupied AND if that space if occupied by
                // someone OTHER than the current proposed component
                while(keys.hasMoreElements())
                {
                    XUIContainer someComponent = (XUIContainer)keys.nextElement();
                    Rectangle region = (Rectangle)grid.get(someComponent);
                    if(region.intersects(spaceToOccupy) && someComponent != this)
                        throw new XUIDisplayException("Component:\n" + someComponent + "\nis already in the area x: "
                            + gridY + ", y: " + gridX + ", width: " + width + ", height: "
                            + height);
                }


                // now check if the atomic falls within the grid as specified by the container.
                Dimension d = parent.getGrid();
                if(width < 0 || (gridY + width) > (d.getWidth()))
                    throw new XUIDisplayException("x & y coordinates must fall within the grid layout of the container.");
                else if(height < 0 || (gridX + height) > (d.getHeight()))
                    throw new XUIDisplayException("x & y coordinates must fall within the grid layout of the container.");
                else
                {
                    gridWidth = width;
                    gridHeight = height;
                    container.remove(panel);
                    if(container instanceof JFrame)
                    {
                        JFrame f = (JFrame)container;
                        f.getContentPane().add(panel, new Rectangle(gridY, gridX, gridWidth, gridHeight));
                    }
                    else
                        container.add(panel, new Rectangle(gridY, gridX, gridWidth, gridHeight));

                    panel.invalidate();
                    container.validate();

                    tabbedNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
                        String.valueOf(width));
                    tabbedNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
                        String.valueOf(height));
                }
            }
        }
    }
}
