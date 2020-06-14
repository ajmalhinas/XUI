package org.purnamaproject.xui.impl;

/**
 * @(#)XUIPanelImp.java 0.5 18/08/2003
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
import javax.swing.JPanel;
import org.purnamaproject.xui.helpers.IDFactory;
import org.purnamaproject.xui.component.container.intermediate.XUIPanel;
import org.purnamaproject.xui.component.container.XUIContainer;
import org.purnamaproject.xui.component.atomic.XUIButton;
import org.purnamaproject.xui.component.atomic.XUICheckBox;
import org.purnamaproject.xui.component.atomic.XUIComboBox;
import org.purnamaproject.xui.component.atomic.XUIImage;
import org.purnamaproject.xui.component.atomic.XUILabel;
import org.purnamaproject.xui.component.atomic.XUIList;
import org.purnamaproject.xui.component.atomic.XUIPasswordField;
import org.purnamaproject.xui.component.atomic.XUIProgressBar;
import org.purnamaproject.xui.component.atomic.XUIRadioButton;
import org.purnamaproject.xui.component.atomic.XUISliderBar;
import org.purnamaproject.xui.component.atomic.XUITable;
import org.purnamaproject.xui.component.atomic.XUITextArea;
import org.purnamaproject.xui.component.atomic.XUITextField;
import org.purnamaproject.xui.component.atomic.XUITree;
import org.purnamaproject.xui.component.composite.XUICalendar;
import org.purnamaproject.xui.component.composite.XUIHypertextPane;
import org.purnamaproject.xui.impl.XUINodeImpl;
import org.purnamaproject.xui.XUIDisplayException;
import org.purnamaproject.xui.component.XUIComponent;
import org.purnamaproject.xui.XUINode;
import org.purnamaproject.xui.GridMapping;
import org.purnamaproject.xui.XUITypeFormatException;
import org.purnamaproject.xui.peer.GraphPaperLayout;

/**
 * A XUI Panel. This implementation class represents a XUI Panel that displays other components.
 * The abstraction of a XUI panel. A panel is an intermediate container that holds atomic
 * components. It does not remain visible but rather functions between an intermediate between
 * other containers (either top level or intermediate level). This class should never be directly
 * instantiated. The XUIComponentFactory should be used instead.
 *
 * The following components can be placed into the grid of this container:
 * <ul>
 *   <li>XUIButton</li>
 *   <li>XUICalendar</li>
 *   <li>XUICheckBox</li>
 *   <li>XUIComboBox</li>
 *   <li>XUIHypertextPane</li>
 *   <li>XUIImage</li>
 *   <li>XUILabel</li>
 *   <li>XUIList</li>
 *   <li>XUIPasswordField</li>
 *   <li>XUIProgressBar</li>
 *   <li>XUIRadioButton</li>
 *   <li>XUIRadioGroup</li>
 *   <li>XUISliderBar</li>
 *   <li>XUITable</li>
 *   <li>XUITextArea</li>
 *   <li>XUITextField</li>
 *   <li>XUITree</li>
 * </ul>
 *
 * @see org.purnamaproject.xui.helpers.XUIComponentFactory
 * @see org.purnamaproject.xui.component.container.intermediate.XUIPanel
 * @version 0.5 18/08/2003
 * @author  Arron Ferguson
 */
public class XUIPanelImpl implements XUIPanel
{
    /**
     * List of supported components that can be placed inside of this one.
     */
    private static LinkedList supportedComponents = null;

    static
    {
        supportedComponents = new LinkedList();
        supportedComponents.add("Button");
        supportedComponents.add("Calendar");
        supportedComponents.add("CheckBox");
        supportedComponents.add("ComboBox");
        supportedComponents.add("HyptertextPane");
        supportedComponents.add("Image");
        supportedComponents.add("Label");
        supportedComponents.add("List");
        supportedComponents.add("PasswordField");
        supportedComponents.add("ProgressBar");
        supportedComponents.add("RadioButton");
        supportedComponents.add("SliderBar");
        supportedComponents.add("Table");
        supportedComponents.add("TextArea");
        supportedComponents.add("TextField");
        supportedComponents.add("Tree");
    }

    /**
     * The native representation of this component.
     */
    private JPanel panel;

    /**
     * Node associated with this component.
     */
    private XUINode pan = null;

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
     * <p>Constructs a new XUIPanel implementation and passing it its XML node rather
     * than requiring the XUIPanel implementation to build it from scratch. This
     * method is called by the Realizer when it reads the XML document from the file
     * system.</p>
     *
     * <p><b><u>Note:</u></b> This constructor is not normally something that should be called up.
     * This constructor is required when creating a XUI Panel where the node has already been built
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
    public XUIPanelImpl(XUINode node)
    {
        gridX = Integer.parseInt(node.getAttributeValue("x"));
        gridY = Integer.parseInt(node.getAttributeValue("y"));
        gridWidth = Integer.parseInt(node.getAttributeValue("width"));
        gridHeight = Integer.parseInt(node.getAttributeValue("height"));

        if(!(node.getName().equals("Panel")))
            throw new XUIDisplayException("Node for XUIButton must be named 'Button' and conform to the XUI schema.");
        else
        {
            // gui component
            panel = new JPanel();
            pan = node;
            pan.setXUIComponent(this);

            // make the new mappings table
            childComponentMappings = new Hashtable();
            setComponentLocation(gridX, gridY);

            // get the layout for width and height
            List layoutNodes = node.getChildNodesByName("GridLayout");
            // we know it's here so we can hard code this.
            XUINode layoutNode = (XUINode)layoutNodes.get(0);
            panel.setLayout(new GraphPaperLayout(new Dimension(
                Integer.parseInt(layoutNode.getAttributeValue("width")),
                Integer.parseInt(layoutNode.getAttributeValue("height")))));

            panel.setName(node.getAttributeValue("name"));
        }

    }

    /**
     * Default Panel created with a size of 1 x 1 cells within the layout
     * in the top left corner of the container.
     */
    public XUIPanelImpl()
    {
        panel = new JPanel();
        panel.setLayout(new GraphPaperLayout(new Dimension(1,1)));
        // make the new mappings table
        childComponentMappings = new Hashtable();
        // now create the XML version of this element
        pan = new XUINodeImpl("Panel");
        pan.setLevel(2);
        pan.addNamespace("xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
        pan.setXUIComponent(this);

        // add window specific attributes
        pan.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "id", "id", "xs:ID",
            IDFactory.getInstance().generateID("panel"));
        pan.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "x", "x", "xs:unsignedShort",
            "0");
        pan.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "y", "y", "xs:unsignedShort",
            "0");
        pan.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
            "1");
        pan.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
            "1");
        pan.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "name", "name", "xs:string",
            panel.getName());

        // add the layout
        XUINode layout = new XUINodeImpl("GridLayout");
        layout.addNamespace("xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
        Dimension dimension = this.getGrid();
        layout.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
            "1");
        layout.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
            "1");
        layout.setLevel(3);
        pan.addChildNode(layout);
    }

    /**
     * Constructor for creating a new XUIPanel component. Component will be centered using the width
     * and height given.
     *
     * @param title the title of this XUIPanel.
     * @param width the width of this XUIPanel.
     * @param height the height of this XUIPanel.
     * @throws org.purnamaproject.xui.XUITypeFormatException if the width and the height are not within the parent's grid.
     */
    public XUIPanelImpl(String title, int width, int height) throws XUIDisplayException
    {
        this();
        panel.setName(title);
        pan.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "name", "name", "xs:string",
            title);
        panel.setLayout(new GraphPaperLayout(new Dimension(width, height), 2, 2));
        panel.setSize(width, height);

        // XML update
        pan.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
            "" + width);
        pan.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
            "" + height);

    }

    /**
     * Sets the grid (rows x columns) of this component. The value allowed for this must fall within the
     * range from 0 to 65,535. Real-time validation is performed and therefor if a wrong value is entered,
     * an exception is generated. If this method is not called or any constructors provided that allow the
     * grid to be set, a default grid size of 1 x 1 will be given.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#unsignedShort">W3C XML Schema, section 3.3.23</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema unsignedshort type
     * or if the width and the height are not within the parent's grid.
     * @param rows the number of rows within the grid.
     * @param columns the number of columns within the grid.
     */
    public void setGrid(int rows, int columns) throws XUIDisplayException
    {
        if(rows > 65535 || columns > 65535)
            throw new XUIDisplayException("Grid values must fall within from 0 - 65535");
        else
        {
            pan.deleteChildren();
            XUINode grid = new XUINodeImpl("GridLayout", "xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
            grid.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
                "" + columns);
            grid.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
                "" + rows);
            int level = pan.getLevel();
            grid.setLevel(++level);
            // now update the GUI with the same
            panel.setLayout(null);
            panel.setLayout(new GraphPaperLayout(new Dimension(columns, rows), 2, 2));
            // now update the grid as well.
            pan.addChildNode(grid);

        }
    }

    /**
     * Returns the grid height and width.
     *
     * @return the grid height and width as a <code>java.awt.Dimension</code> object.
     */
    public Dimension getGrid()
    {
        GraphPaperLayout gpl = (GraphPaperLayout)panel.getLayout();
        return gpl.getGridSize();
    }

    /**
     * Returns the XUI name of this component as a string.
     *
     * @return the name of this component.
     * @see #setName(String)
     */
    public String getName()
    {
        return panel.getName();
    }

    /**
     * Sets the XUI name of this component. The string values allowed can contain
     * both alphabetic and numerica characters. Real-time validation is performed and
     * therefor if a wrong value is entered, an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#string">W3C XML Schema, section 3.2.1</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema string
     * type, or if the string is null, an exception occurs.
     * @param newName the new name to assign to this component.
     */
    public void setName(String newName) throws XUITypeFormatException
    {
        if(newName == null)
            throw new XUITypeFormatException("Panel name cannot be a null string.");
        else
        {
            panel.setName(newName);
            pan.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "name", "name", "xs:string",
            pan.getName());
        }
    }

    /**
     * Returns the XUI id reference of this component.
     *
     * @return the id reference of this component as a string.
     * @see #setIDRef(String)
     */
    public String getIDRef()
    {
        return pan.getAttributeValue("idref");
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
            pan.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "idref", "idref", "xs:IDREF",
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
     * <p>Adds a new component to this container. The type of component must adhere to the XUI specification.
     * Real-time validation is performed and therefor if the wrong type of component is added, an exception
     * is generated. For each type of container (each concrete class), there will be a listed set of
     * components supported by that particular container.</p>
     *
     * <p>The following types of components are allowed to be placed inside of a XUIPanel:</p>
     * <ul>
     *   <li>XUIButton</li>
     *   <li>XUICalendar</li>
     *   <li>XUICheckBox</li>
     *   <li>XUIComboBox</li>
     *   <li>XUIHypertextPane</li>
     *   <li>XUIImage</li>
     *   <li>XUILabel</li>
     *   <li>XUIList</li>
     *   <li>XUIPasswordField</li>
     *   <li>XUIProgressBar</li>
     *   <li>XUIRadioButton</li>
     *   <li>XUIRadioGroup</li>
     *   <li>XUISliderBar</li>
     *   <li>XUITextArea</li>
     *   <li>XUITable</li>
     *   <li>XUITextField</li>
     *   <li>XUITree</li>
     * </ul>
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
            throw new XUITypeFormatException("XUIComponent cannot be null.");
        // check for only the components listed in the XUI specification. If a component
        // is added that is not supported by this panel, throw an exception. Else, add it.
        if(component instanceof XUIButton            || component instanceof XUICalendar
            || component instanceof XUICheckBox      || component instanceof XUIComboBox
            || component instanceof XUIHypertextPane || component instanceof XUIImage
            || component instanceof XUILabel         || component instanceof XUIList
            || component instanceof XUIPasswordField || component instanceof XUIProgressBar
            || component instanceof XUIRadioButton   || component instanceof XUITree
            || component instanceof XUISliderBar     || component instanceof XUITextArea
            || component instanceof XUITable         || component instanceof XUITextField)
        {
            // get the node
            XUINode node = component.getNodeRepresentation();

            int x = Integer.parseInt(node.getAttributeValue("x"));
            int y = Integer.parseInt(node.getAttributeValue("y"));
            int width = Integer.parseInt(node.getAttributeValue("width"));
            int height = Integer.parseInt(node.getAttributeValue("height"));

            // check to make sure it fits within the grid.
            Dimension localGrid = this.getGrid();
            if(width > localGrid.getWidth() || height > localGrid.getHeight())
                throw new XUITypeFormatException(node.getName() + " (id: " + node.getAttributeID()
                    + ") must be within this window's grid width and height (w: " + localGrid.getWidth()
                    + " + h: " + localGrid.getHeight() + ")");


            Rectangle rect = new Rectangle(y, x, width, height);

            panel.add(component.getPeer(), rect);
            component.getPeer().invalidate();
            panel.validate();

            // for mapping components to the regions they occupy within the grid
            childComponentMappings.put(component, rect);


            // add the component's a node
            int level = pan.getLevel();
            node.setLevel(++level);

            if(panel.getParent() == null && node.getFromRealizer())
                pan.addChildNode(node);
            else if(!(node.getFromRealizer()))
                pan.addChildNode(node);

            component.setComponentLocation(x, y);

        } else
        {
            StringBuffer sb = new StringBuffer();
            sb.append("Type not supported in XUIPanel. The following types are supported:\n");

            for(int i = 0; i < supportedComponents.size(); i++)
            {
                String s = (String)supportedComponents.get(i);
                sb.append("- " + s + "\n");
            }
            throw new XUITypeFormatException(sb.toString());
        }
    }

    /**
     * Removes all of the components from this container. If no components found then this method
     * does nothing. The layout is kept both in the native GUI environment as well as the XUI document.
     */
    public void removeAllComponents()
    {
        panel.removeAll();
        childComponentMappings.clear();
        // and same for all the nodes in the tree:
        List list = pan.getAllChildNodes();
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
     * Removes the component based on its object reference. If no component found by that reference,
     * no action is taken. Removal will take place if the component is a direct or indirect child.
     *
     * @param component the component to remove.
     */
    public void removeComponent(XUIComponent component)
    {
        // flattened list
        List list = pan.getAllChildNodes();
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
                pan.removeChildNode(node);
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
        List list = pan.getAllChildNodes();

        for(int i = 0; i < list.size(); i++)
        {
            // get the node
            XUINode node = (XUINode)list.get(i);

            // get the component
            String nodeID = node.getAttributeID();
            XUIComponent c = node.getXUIComponent();
            if(nodeID != null)
                if(nodeID.equals(id))
                {
                    childComponentMappings.remove(c);

                    // same component so, remove it from the GUI
                    Component swingComponent = c.getPeer();
                    panel.remove(swingComponent);
                    panel.invalidate();
                    panel.repaint();
                    // force the GUI to repaint itself
                    if(panel.getParent() != null)
                        panel.getParent().validate();
                    // remove the node from the XUI document
                    pan.removeChildNode(node);
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
        List list = pan.getAllChildNodes();
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
     * Gets all the components in this container. If no components found, an empty list is returned.
     *
     * @return a list of the components that are currently children of this container.
     */
    public List getChildrenComponents()
    {
        List components = new LinkedList();
        List list = pan.getAllChildNodes();
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
        return pan.getAttributeValue("id");
    }

    /**
     * Represents the XUI window component as a XUI node.
     *
     * @return org.purnamaproject.xui.XUINode the XML element as a XUI node.
     */
    public XUINode getNodeRepresentation()
    {
        return pan;
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
     * Returns a hashtable which contains XUIComponents as keys and rectangles as the regions
     * occupied on the grid by child components. Used for components to determine if they are
     * attempting to take up space on the container where another component already exists.
     *
     * @return the hashtable of the components and their mapped rectangle regions.
     */
    public Hashtable getGridMapping()
    {
        return childComponentMappings;
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
    public void setComponentLocation(int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        if(xCoordinate < 0 || xCoordinate > 65535)
            throw new XUIDisplayException("x and y values must be within the range of 0 - 65535.");
        if(yCoordinate < 0 || yCoordinate > 65535)
            throw new XUIDisplayException("x and y values must be within the range of 0 - 65535.");
        else
        {

            XUINode parentNode = pan.getParent();

            // proposed space to occupy
            Rectangle spaceToOccupy = new Rectangle(yCoordinate, xCoordinate, gridWidth, gridHeight);

            if(parentNode != null)
                if(parentNode.getName().equals("Window") || parentNode.getName().equals("CustomDialog"))
                {

                    GridMapping parent = (GridMapping)parentNode.getXUIComponent();

                    Container container = null;

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
                            throw new XUIDisplayException(".x & y coordinates must fall within the grid layout of the container.");
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

                            pan.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "x", "x", "xs:unsignedShort",
                                String.valueOf(xCoordinate));
                            pan.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "y", "y", "xs:unsignedShort",
                                String.valueOf(yCoordinate));
                        }
                    }

                } else if(parentNode.getName().equals("SplitPanel"))
                {
                    ;

                } else if(parentNode.getName().equals("TabbedPanel"))
                {
                    ;
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
    public void setComponentSize(int width, int height)
        throws XUIDisplayException
    {
        if(width < 0 || width > 65535)
            throw new XUIDisplayException("width and height values must be within the range of 0 - 65535.");
        if(height < 0 || height > 65535)
            throw new XUIDisplayException("width and height values must be within the range of 0 - 65535.");
        else
        {
            XUINode parentNode = pan.getParent();

            // proposed space to occupy
            Rectangle spaceToOccupy = new Rectangle(gridY, gridX, width, height);


            // if Window or CustomDialog, then it needs to be dealing with a grid
            if(parentNode != null)
                if(parentNode.getName().equals("Window") || parentNode.getName().equals("CustomDialog"))
                {

                    GridMapping parent = (GridMapping)parentNode.getXUIComponent();

                    Container container = null;

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

                            pan.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
                                String.valueOf(width));
                            pan.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
                                String.valueOf(height));
                        }
                    }

                } else if(parentNode.getName().equals("SplitPanel"))
                {
                    ;

                } else if(parentNode.getName().equals("TabbedPanel"))
                {
                    ;
                }


        }
    }
}
