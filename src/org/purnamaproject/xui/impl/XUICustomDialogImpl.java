package org.purnamaproject.xui.impl;

/**
 * @(#)XUICustomDialogImpl.java 0.5 18/08/2003
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
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.purnamaproject.xui.helpers.IDFactory;
import org.purnamaproject.xui.peer.GraphPaperLayout;
import org.purnamaproject.xui.component.container.intermediate.XUIPanel;
import org.purnamaproject.xui.component.container.toplevel.XUICustomDialog;
import org.purnamaproject.xui.component.container.toplevel.XUIWindow;
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
import org.purnamaproject.xui.binding.XUIModel;
import org.purnamaproject.xui.binding.WindowModel;

/**
 * The abstraction of a XUI CustomDialog. This component type acts like a window in that it
 * allows customization of the layout but it also adds the modality feature of basic dialogs
 * to this particular component.
 *
 * The following components can be placed into the grid of this container:
 * <ul>
 *   <li>XUIPanel</li>
 * </ul>
 *
 * @version 0.5 18/08/2003
 * @author  Arron Ferguson
 */
public class XUICustomDialogImpl implements XUICustomDialog, WindowListener
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
     * Value to determine whether or not this component should be displayed when visualize is called.
     */
    private boolean windowVisibility = false;

    /**
     * The native representation of this component.
     */
    private JDialog dialog;

    /**
     * Node associated with this component.
     */
    private XUINode dialogNode = null;

    /**
     * The hashtable containing rectangles representing the regions that are mapped to child components.
     */
    private Hashtable childComponentMappings = null;

    /**
     * The grid width of this component (in grid cells).
     */
    private int gridWidth = 2;

    /**
     * The grid height of this component (in grid cells).
     */
    private int gridHeight = 1;

    /**
     * The grid x coordinate of this component (within the grid).
     */
    private int gridX = 1;

    /**
     * The grid y coordinate of this component (within the grid).
     */
    private int gridY = 1;

    /**
     * The list of model objects that are ready to be listeners to any events generated by this
     * component.
     */
    private List windowModelList = new LinkedList();

    /**
     * <p>Constructs a new XUICustomDialog implementation and passing it its XML node rather
     * than requiring the XUICustomDialog implementation to build it from scratch. This
     * method is called by the Realizer when it reads the XML document from the file
     * system.</p>
     *
     * <p><b><u>Note:</u></b> This constructor is not normally something that should be called up.
     * This constructor is required when creating a XUI custom dialog where the node has already been built
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
    public XUICustomDialogImpl(XUINode node) throws XUIDisplayException
    {
        int x = Integer.parseInt(node.getAttributeValue("x"));
        int y = Integer.parseInt(node.getAttributeValue("y"));
        int width = Integer.parseInt(node.getAttributeValue("width"));
        int height = Integer.parseInt(node.getAttributeValue("height"));

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        if(!(node.getName().equals("CustomDialog")))
            throw new XUIDisplayException("Node for XUICustomDialog must be named CustomDialog and conform to the XUI schema.");
        else if(x > (d.getWidth() + width) || x < (0 - width))
            throw new XUIDisplayException("x and y values must fall within the desktop's resolution.");
        else if(y > (d.getHeight() + height) || y < (0 - height))
            throw new XUIDisplayException("x and y values must fall within the desktop's resolution.");
        else
        {
            dialogNode = node;

            // need to instantiate the peer first
            XUIWindow window = (XUIWindow)dialogNode.getParent().getXUIComponent();

            JFrame frame = (JFrame)window.getPeer();
            dialog = new JDialog(frame, node.getAttributeValue("name"));
            dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

            // make the new mappings table
            childComponentMappings = new Hashtable();

            // get the layout for width and height
            List layoutNodes = dialogNode.getChildNodesByName("GridLayout");
            // we know it's here so we can hard code this.
            XUINode layoutNode = (XUINode)layoutNodes.get(0);
            dialog.getContentPane().setLayout(new GraphPaperLayout(new Dimension(
                Integer.parseInt(layoutNode.getAttributeValue("width")),
                Integer.parseInt(layoutNode.getAttributeValue("height")))));
            windowVisibility = Boolean.valueOf(node.getAttributeValue("visible")).booleanValue();
            dialog.setModal(Boolean.valueOf(node.getAttributeValue("modal")).booleanValue());
            dialog.setSize(width, height);
            dialog.setLocation(x, y);

            dialogNode.setXUIComponent(this);

            // keep track of window positioning
            dialog.addComponentListener(new ComponentAdapter()
                {
                    /**
                     * Update XUI node to reflect the move.
                     */
                    public void componentMoved(ComponentEvent e)
                    {
                        Component c = e.getComponent();
                        setComponentLocation(c.getX(), c.getY());
                    }

                    /**
                     * Update XUI node to reflect the resize.
                     */
                    public void componentResized(ComponentEvent e)
                    {
                        Component c = e.getComponent();
                        setComponentSize(c.getWidth(), c.getHeight());
                    }
            });
        }
    }

    /**
     * Default Dialog created with no title in the titlebar and a size of 280 x 220 pixels in the top
     * left of the desktop. Accepts the parent window that it belongs to. Default is not visible and
     * modal.
     *
     * @param window the parent window that this dialog belongs to.
     */
    public XUICustomDialogImpl(XUIWindow window) throws XUIDisplayException
    {
        // make the new mappings table
        childComponentMappings = new Hashtable();


        // now create the XML version of this element
        dialogNode = new XUINodeImpl("Dialog");
        dialogNode.setLevel(2);
        dialogNode.addNamespace("xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
        dialogNode.setXUIComponent(this);

        // add window specific attributes
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "id", "id", "xs:ID",
            IDFactory.getInstance().generateID("customdialog"));
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "x", "x", "xs:unsignedShort",
            "" + dialog.getX());
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "y", "y", "xs:unsignedShort",
            "" + dialog.getY());
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
            "280");
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
            "220");
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "name", "name", "xs:string",
            dialog.getName());
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "visible", "visible", "xs:boolean",
            "" + dialog.isVisible());
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "modal", "modal", "xs:boolean",
            "" + false);


        JFrame frame = (JFrame)window.getPeer();
        dialog = new JDialog(frame, dialogNode.getAttributeValue("name"));
        dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        dialog.setVisible(false);
        dialog.setSize(280, 220);
        dialog.setModal(true);

        dialog.getContentPane().setLayout(new GraphPaperLayout(new Dimension(1, 1), 2, 2));
        dialog.setSize(2, 2);

        // add the layout
        XUINode layout = new XUINodeImpl("GridLayout");
        layout.addNamespace("xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
        Dimension dimension = this.getGrid();
        layout.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
            "" + (int)dimension.getWidth());
        layout.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
            "" + (int)dimension.getHeight());
        layout.setLevel(3);
        dialogNode.addChildNode(layout);

        // keep track of window positioning
        frame.addComponentListener(new ComponentAdapter()
            {
                /**
                 * Update XUI node to reflect the move.
                 */
                public void componentMoved(ComponentEvent e)
                {
                    Component c = e.getComponent();
                    setComponentLocation(c.getX(), c.getY());
                }

                /**
                 * Update XUI node to reflect the resize.
                 */
                public void componentResized(ComponentEvent e)
                {
                    Component c = e.getComponent();
                    setComponentSize(c.getWidth(), c.getHeight());
                }
            });
    }

    /**
     * Constructor for creating a new XUIDialog component. Component will be centered using the width
     * and height given. Requires the parent XUIWindow to be given as well.
     *
     * @param title the title of this XUIDialog.
     * @param width the width of this XUIDialog.
     * @param height the height of this XUIDialog.
     */
    public XUICustomDialogImpl(XUIWindow window, String title, int width, int height) throws XUIDisplayException
    {
        this(window);
        dialog.setTitle(title);
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "name", "name", "xs:string",
            title);
        dialog.setSize(width, height);

        // XML update
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
            "" + width);
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
            "" + height);

        // center by default
        Dimension frameD = new Dimension(width, height);
        Dimension screenD = Toolkit.getDefaultToolkit().getScreenSize();
        setComponentSize(width, height);
        setComponentLocation(((screenD.width - frameD.width)/2), ((screenD.height - frameD.height)/2));
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
        if(component instanceof XUIPanel)
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

            dialog.getContentPane().add(component.getPeer(), rect);
            // for mapping components to the regions they occupy within the grid
            component.getPeer().invalidate();
            dialog.validate();
            childComponentMappings.put(component, rect);


            // add the component's a node
            int level = dialogNode.getLevel();
            node.setLevel(++level);

            if(dialog.getParent() == null && node.getFromRealizer())
                dialogNode.addChildNode(node);
            else if(!(node.getFromRealizer()))
                dialogNode.addChildNode(node);

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
     * does nothing.
     */
    public void removeAllComponents()
    {
        dialog.removeAll();
        childComponentMappings.clear();
        // and same for all the nodes in the tree:
        List list = dialogNode.getAllChildNodes();
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
     * no action is taken.
     *
     * @param component the component to remove.
     */
    public void removeComponent(XUIComponent component)
    {
        // flattened list
        List list = dialogNode.getAllChildNodes();
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
                dialog.remove(swingComponent);
                dialog.repaint();
                dialogNode.removeChildNode(node);
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
        List list = dialogNode.getAllChildNodes();
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
                dialog.remove(swingComponent);
                dialog.repaint();
                // remove the node from the XUI document
                dialogNode.removeChildNode(node);
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
        List list = dialogNode.getAllChildNodes();
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
        List list = dialogNode.getAllChildNodes();
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
     * <p>Sets the grid (rows x columns) of this component. The value allowed for this must fall within the
     * range from 0 to 65,535. Real-time validation is performed and therefor if a wrong value is entered,
     * an exception is generated. If this method is not called or any constructors provided that allow the
     * grid to be set, a default grid size of 1 x 1 will be given.<p>
     *
     * <p>If there are any child components (i.e. atomic, composite or intermediate containers) placed
     * inside of this window, they will be removed and deleted. Application programmers should be aware
     * of this so that when users add things they do not loose their layout without being informed of
     * the consequences.</p>
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#unsignedShort">W3C XML Schema, section 3.3.23</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUIDisplayException if the type does not conform to the W3C XML Schema unsignedshort type.
     * @param rows the number of rows within the grid.
     * @param columns the number of columns within the grid.
     */
    public void setGrid(int rows, int columns) throws XUIDisplayException
    {
        if(rows > 65535 || columns > 65535)
            throw new XUIDisplayException("Grid values must fall within from 0 - 65535");
        else
        {
            dialogNode.deleteChildren();
            XUINode grid = new XUINodeImpl("GridLayout", "xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
            grid.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
                "" + columns);
            grid.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
                "" + rows);
            int level = dialogNode.getLevel();
            grid.setLevel(++level);
            dialog.setLayout(null);
            // now update the GUI with the same
            dialog.getContentPane().setLayout(new GraphPaperLayout(new Dimension(columns, rows), 2, 2));
            // now update the grid as well.
            dialogNode.addChildNode(grid);
        }
    }

    /**
     * Returns the grid height and width.
     *
     * @return the grid height and width as a <code>java.awt.Dimension</code> object.
     */
    public Dimension getGrid()
    {
        GraphPaperLayout gpl = (GraphPaperLayout)dialog.getContentPane().getLayout();
        return gpl.getGridSize();
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
     * <p>Sets the component's XML element's visible attribute. This does not actually make the component visible.
     * In order to see the component visible, call the <code>visualize()</code> method.</p>
     *
     * @param visibility true means this component is visible and false means it is not.
     * @see #visualize()
     */
    public void setVisible(boolean visibility)
    {
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "visible", "visible", "xs:boolean",
            "" + visibility);
        windowVisibility = visibility;
        this.visualize();
    }

    /**
     * If this component has its visible attribute set to true, the component will then be visualized (i.e.
     * displayed on the screen).
     */
    public void visualize()
    {
        dialog.setSize(Integer.parseInt(dialogNode.getAttributeValue("width")),
            Integer.parseInt(dialogNode.getAttributeValue("height")));
        dialog.setLocation(Integer.parseInt(dialogNode.getAttributeValue("x")),
            Integer.parseInt(dialogNode.getAttributeValue("y")));
        dialog.setVisible(Boolean.valueOf(dialogNode.getAttributeValue("visible")).booleanValue());
    }

    /**
     * Returns whether this component is visible or not.
     *
     * @return visible state (true or false).
     */
    public boolean isVisible()
    {
        return windowVisibility;
    }

    /**
     * Returns the XUI id reference of this component.
     *
     * @return the id reference of this component as a string.
     * @see #setIDRef(String)
     */
    public String getIDRef()
    {
        return dialogNode.getAttributeValue("idref");
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
            dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "idref", "idref", "xs:IDREF",
                newIDRef);
        else
            throw new XUITypeFormatException("ID reference must start with alphabetic only: a - z or A - Z.");
    }

    /**
     * Returns the modality state of this dialog.
     *
     * @return the modality state of this component.
     * @see #setModal(boolean)
     */
    public boolean getModal()
    {
        return dialog.isModal();
    }

    /**
     * Sets the modality of this dialog. If the modality value is set to true, then the dialog box must
     * be responded to before the user may interact with the underlying Windows of this application.
     * Otherwise, the user can bypass this dialog and interact with the underlying parent window that
     * called this dialog. The value allowed for this must be a boolean value. The Java boolean type is
     * mapped to the XML Schema boolean type.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#boolean">W3C XML Schema, section 3.2.2</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param modal the modality of this dialog.
     */
    public void setModal(boolean modal)
    {
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "modal", "modal", "xs:boolean",
            "" + modal);
        dialog.setModal(modal);
    }

    /**
     * Returns the XUI id of this component.
     *
     * @return the id of this component as a string.

     */
    public String getID()
    {
        return dialogNode.getAttributeValue("id");
    }

    /**
     * Represents the XUI window component as a XUI node.
     *
     * @return org.purnamaproject.xui.XUINode the XML element as a XUI node.
     */
    public XUINode getNodeRepresentation()
    {
        return dialogNode;
    }

    /**
     * <p>This method returns the peer within the host operating system and user interface environment.
     * For each environment, this will return a different object type and may not be represented as
     * a class called component.</p>
     *
     * <p><b>This implementation of the method returns the content pane of the dialog peer. To return
     * the actual dialog peer, call <code>getDialogPeer</code></b></p>
     *
     * @return Component which is the native representation of the GUI component.

     */
    public Component getPeer()
    {
        return dialog.getContentPane();
    }

    /**
     * Returns the dialog peer. This is required since all other (non dialog) intermediate containers
     * return containers.
     *
     * @return a dialog peer.
     */
    public JDialog getDialogPeer()
    {
        return dialog;
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
     * <p><b>For CustomDialog, this method sets the component on the screen. The top level components
     * have the difference that their x and y coordinates are screen coordinates and not grid coordinates.
     * For this reason, their type for x and y are slightly different than that of an intermediate component,
     * composite component or an atomic component. A top level container has a signed short instead of an
     * unsigned short. Thus the value for the integer can be from -32,768 to 32,767 instead of from
     * 0 to 65,535.</b></p>
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#unsignedShort">W3C XML Schema, section 3.3.23</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUIDisplayException if the component is being placed overtop of an existing component on the
     * layout.
     * @param xCoordinate the y coordinate.
     * @param yCoordinate the x coordinate.
     */
    public void setComponentLocation(int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        if(xCoordinate < -32768 || xCoordinate > 32767)
            throw new XUIDisplayException("x and y values must be within the range of 0 - 65535.");
        if(yCoordinate < -32768 || yCoordinate > 32767)
            throw new XUIDisplayException("x and y values must be within the range of 0 - 65535.");
        else
        {
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            if(xCoordinate > (d.getWidth() + dialog.getWidth()) || xCoordinate < (0 - dialog.getWidth()))
                throw new XUIDisplayException("x and y values must fall within the desktop's resolution.");
            else if(yCoordinate > (d.getHeight() + dialog.getHeight()) || yCoordinate < (0 - dialog.getHeight()))
                throw new XUIDisplayException("x and y values must fall within the desktop's resolution.");
            else
            {
                dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "x", "x", "xs:unsignedShort",
                    String.valueOf(xCoordinate));
                dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "y", "y", "xs:unsignedShort",
                    String.valueOf(yCoordinate));
                dialog.setLocation(xCoordinate, yCoordinate);
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
     * <p><b>For CustomDialog, this method sets the component on the screen. The top level components
     * have the difference that their width and their height are screen values and not grid values.
     * For this reason, their type for width and height are slightly different than that of an intermediate component,
     * composite component or an atomic component. A top level container has a signed short instead of an
     * unsigned short. Thus the value for the integer can be from -32,768 to 32,767 instead of from
     * 0 to 65,535.</b></p>
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
        if(width < -32768 || width > 32767)
            throw new XUIDisplayException("width and height values must be within the range of 0 - 65535.");
        if(height < -32768 || height > 32767)
            throw new XUIDisplayException("width and height values must be within the range of 0 - 65535.");
        else
        {
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            if(width > 32767)
                throw new XUIDisplayException("x and y values must fall within the desktop's resolution.");
            else if(height > 32767)
                throw new XUIDisplayException("x and y values must fall within the desktop's resolution.");
            else
            {
                dialog.setSize(width, height);
                dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
                    String.valueOf(width));
                dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
                    String.valueOf(height));
            }
        }
    }

    // not used
    public void windowActivated(WindowEvent e) { }
    public void windowClosed(WindowEvent e) { }
    public void windowDeactivated(WindowEvent e) { }
    public void windowDeiconified(WindowEvent e) { }
    public void windowIconified(WindowEvent e) { }
    public void windowOpened(WindowEvent e) { }

    public void windowClosing(WindowEvent e)
    {
        if(e.getSource() == dialog)
        {
            int size = windowModelList.size();
            for(int i = 0; i < size; i++)
            {
                WindowModel model = (WindowModel)windowModelList.get(i);
                model.windowAction(this);
            }
        }
    }

    /**
     * Adds a new model to listen to the <code>XUIComponent</code> that also is a <code>XUIEventSource</code>.
     * If the model is not an instance of the WindowModel type, then no binding is done.
     *
     * @param model the model that listens to events based on this type.
     */
    public void addEventListener(XUIModel model)
    {
        if(model instanceof WindowModel)
        {
            // call up the action model.
            windowModelList.add(model);
            dialog.addWindowListener(this);
        }
    }

    /**
     * Delete a model from the <code>XUIComponent</code> that also is a <code>XUIEventSource</code>.
     *
     * @param model the model that is to no longer be a listener.
     */
    public void removeEventListener(XUIModel model)
    {
        windowModelList.remove(model);
        dialog.removeWindowListener(this);
    }

}
