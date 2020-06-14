package org.purnamaproject.xui.impl;

/**
 * @(#)XUIBasicDialogImpl.java  0.5 18/08/2003
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
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import org.purnamaproject.xui.component.container.toplevel.XUIBasicDialog;
import org.purnamaproject.xui.component.container.toplevel.XUIWindow;
import org.purnamaproject.xui.impl.XUINodeImpl;
import org.purnamaproject.xui.helpers.IDFactory;
import org.purnamaproject.xui.helpers.XUIUtils;
import org.purnamaproject.xui.XUIDisplayException;
import org.purnamaproject.xui.component.XUIComponent;
import org.purnamaproject.xui.XUINode;
import org.purnamaproject.xui.XUITypeFormatException;


/**
 * A Simple dialog type component that is modal. This type of component does not have many
 * different features other than for displaying a message. If a customizable dialog is required,
 * use a XUICustomDialog.
 *
 * @version 0.5 18/08/2003
 * @author  Arron Ferguson
 */
public class XUIBasicDialogImpl implements XUIBasicDialog
{
    /**
     * List of supported components that can be placed inside of this one.
     */
    private static LinkedList supportedComponents = null;

    static
    {
        supportedComponents = new LinkedList();
        supportedComponents.add("No supported types for BasicDialog.");
    }

    /**
     * The type of the dialog.
     */
    private byte dialogType = 0;

    /**
     * Value to determine whether or not this component should be displayed when visualize is called.
     */
    private boolean windowVisibility = false;

    /**
     * Node associated with this component.
     */
    private XUINode dialogNode = null;

    /**
     * The parent window that owns this dialog.
     */
    private XUIWindow window;

    /**
     * <p>Constructs a new XUIBasicDialog implementation and passing it its XML node rather
     * than requiring the XUIBasicDialog implementation to build it from scratch. This
     * method is called by the Realizer when it reads the XML document from the file
     * system.</p>
     *
     * <p><b><u>Note:</u></b> This constructor is not normally something that should be called up.
     * This constructor is required when creating a XUI Window where the node has already been built
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
    public XUIBasicDialogImpl(XUINode node) throws XUIDisplayException
    {

            dialogNode = node;

            // need to instantiate the peer first
            windowVisibility = Boolean.valueOf(node.getAttributeValue("visible")).booleanValue();
            window = (XUIWindow)dialogNode.getParent().getXUIComponent();
            setType(Byte.parseByte(node.getAttributeValue("type")));

    }

    /**
     * Default constructor that creates the component with an empty message of type question.
     */
    public XUIBasicDialogImpl()
    {

        // now create the XML version of this element
        dialogNode.setLevel(2);
        dialogNode.addNamespace("xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
        dialogNode.setXUIComponent(this);

        // add window specific attributes
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "id", "id", "xs:ID",
            IDFactory.getInstance().generateID("basicdialog"));
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "idref", "idref", "xs:IDREF",
            "x");
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "x", "x", "xs:unsignedShort",
            "1");
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "y", "y", "xs:unsignedShort",
            "1");
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
            "1");
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
            "1");
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "name", "name", "xs:string",
            "XUI Message.");
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "visible", "visible", "xs:boolean",
            "false");
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "type", "type", "xs:byte",
            "0");
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "message", "message", "xs:string",
            "");
    }

    /**
     * Creates a basic dialog component that accepts the parent, message, name (title in the dialog) and
     * the type based on the types listed above.
     *
     * @param parent the parent window that this dialog belongs to.
     * @param message the message to place in this dialog.
     * @param name the name to appear in this dialog's title bar.
     */
    public XUIBasicDialogImpl(XUIWindow parent, String message, String name, byte type) throws XUIDisplayException
    {
        this();
        if(message == null)
            throw new XUIDisplayException("message must not be null.");
        if(parent == null)
            throw new XUIDisplayException("parent must not be null.");

        window = parent;

        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "name", "name", "xs:string",
        name);
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "message", "message", "xs:string",
        message);
        dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "type", "type", "xs:byte",
        type + "");

    }

    /**
     * <p>Returns the x coordinate of this component. For a top level container, this coordinate is the actual
     * x coordinate value (in pixels) that appears on the desktop. For an intermediate component, this x
     * coordinate is the position within the layout grid that is assigned to the top level container.</p>
     *
     * <p><b>For XUIBasicDialog this value is always 0 as the component cannot be placed or sized.</b></p>
     *
     * @return the x coordinate of this component.
     */
    public int getX()
    {
        return 0;
    }

    /**
     * <p>Returns the y coordinate of this component. For a top level container, this coordinate is the actual
     * y coordinate value (in pixels) that appears on the desktop. For an intermediate component, this y
     * coordinate is the position within the layout grid that is assigned to the top level container.</p>
     *
     * <p><b>For XUIBasicDialog this value is always 0 as the component cannot be placed or sized.</b></p>
     *
     * @return the y coordinate of this component.
     */
    public int getY()
    {
        return 0;
    }

    /**
     * <p>Returns the width of this component. For a top level container, this value is the actual width
     * (in pixels) that appears on the desktop. For an intermediate component, this width value is the
     * position within the layout grid that is assigned to the top level container (e.g. 2 would be two
     * cells wide.</p>
     *
     * <p><b>For XUIBasicDialog this value is always 0 as the component cannot be placed or sized.</b></p>
     *
     * @return the width of this component.
     */
    public int getWidth()
    {
        return 0;
    }

    /**
     * <p>Returns the height of this component. For a top level container, this value is the actual height
     * (in pixels) that appears on the desktop. For an intermediate component, this height value is the
     * position within the layout grid that is assigned to the top level container (e.g. 2 would be two
     * cells high.</p>
     *
     * <p><b>For XUIBasicDialog this value is always 0 as the component cannot be placed or sized.</b></p>
     *
     * @return the height of this component.
     */
    public int getHeight()
    {
        return 0;
    }

    /**
     * <p>Adds a new component to this container. The type of component must adhere to the XUI specification.
     * Real-time validation is performed and therefor if the wrong type of component is added, an exception
     * is generated. For each type of container (each concrete class), there will be a listed set of
     * components supported by that particular container.</p>
     *
     * <p><b>For XUIBasicDialog this method does not perform any task. It is required because this component
     * is an intermediate container.</b></p>
     *
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @see #getSupportedComponents()
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the
     * <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>.
     * @param component the component to add.    */
    public void addComponent(XUIComponent component) throws XUITypeFormatException
    {
        ; // nada
    }

    /**
     * <p>Removes all of the components from this container. If no components found then this method
     * does nothing.</p>
     *
     * <p><b>For XUIBasicDialog this method does not perform any task. It is required because this component
     * is an intermediate container.</b></p>
     */
    public void removeAllComponents()
    {
        ; // nada
    }

    /**
     * <p>Removes the component based on its object reference. If no component found by that reference,
     * no action is taken.</p>
     *
     * <p><b>For XUIBasicDialog this method does not perform any task. It is required because this component
     * is an intermediate container.</b></p>
     *
     * @param component the component to remove.
     */
    public void removeComponent(XUIComponent component)
    {
        ; // nada
    }

    /**
     * <p>Removes the component based on its id. If no component found by that id, no action is taken.</p>
     *
     * <p><b>For XUIBasicDialog this method does not perform any task. It is required because this component
     * is an intermediate container.</b></p>
     *
     * @param id the id of the component to remove.
     */
    public void removeComponent(String id)
    {
        ; // nada
    }

    /**
     * Returns the XUI name of this component as a string.
     *
     * @return the name of this component.
     * @see #setName(String)
     */
    public String getName()
    {
        return dialogNode.getAttributeValue("name");
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
            dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "name", "name", "xs:string",
            newName);
        }
    }

    /**
     * <p>Gets the component based on its id. If no component found by that id, null is returned.</p>
     *
     * <p><b>For XUIBasicDialog this method does not perform any task. It is required because this component
     * is an intermediate container.</b></p>
     *
     * @param id the id of the component to get. Component remains.
     */
    public XUIComponent getComponent(String id)
    {
        return null;
    }

    /**
     * <p>Gets all the components in this container. If no components found, null is returned.</p>
     *
     * <p><b>For XUIBasicDialog this method does not perform any task. It is required because this component
     * is an intermediate container.</b></p>
     *
     * @return a list of the components that are currently children of this container.
     */
    public List getChildrenComponents()
    {
        return null;
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
     * grid to be set, a default grid size of 5 x 5 will be given.</p>
     *
     * <p><b>For XUIBasicDialog this method does not perform any task. It is required because this component
     * is an intermediate container.</b></p>
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#unsignedShort">W3C XML Schema, section 3.3.23</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUIDisplayException if the type does not conform to the W3C XML Schema unsignedshort type.
     * @param rows the number of rows within the grid.
     * @param columns the number of columns within the grid.
     */
    public void setGrid(int rows, int columns) throws XUIDisplayException
    {
        ; // nada
    }

    /**
     * <p>Returns the grid height and width.</p>
     *
     * <p><b>For XUIBasicDialog this method does not perform any task. It is required because this component
     * is an intermediate container.</b></p>
     *
     * @return the grid height and width as a <code>java.awt.Dimension</code> object.
     */
    public Dimension getGrid()
    {
        return null;
    }

    /**
     * <p>Returns a hashtable which contains XUIComponents as keys and rectangles as the regions
     * occupied on the grid by child components. Used for components to determine if they are
     * attempting to take up space on the container where another component already exists.</p>
     *
     * <p><b>For XUIBasicDialog this method does not perform any task. It is required because this component
     * is an intermediate container.</b></p>
     *
     * @return the hashtable of the components and their mapped rectangle regions.
     */
    public Hashtable getGridMapping()
    {
        return null;
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
     * Returns the type of this dialog.
     *
     * @return the type of this component.
     * @see #setType(byte)
     */
    public byte getType()
    {
        return dialogType;
    }

    /**
     * Sets the type of this dialog. The following different types are allowed:
     * <ul>
     *   <li>question - (showing a question Image - value XUIUtils.QUESTION_TYPE)</li>
     *   <li>information - (showing an information Image - value XUIUtils.INFORMATION_TYPE)</li>
     *   <li>warning - (showing a warning Image - value XUIUtils.WARNING_TYPE)</li>
     *   <li>error - (showing an error Image - value XUIUtils.ERROR_TYPE)</li>
     * </ul>
     * <p>The value allowed for this must fall within the range from 0 to 255. Real-time validation is
     * performed and therefor if a wrong value is entered, an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#byte">W3C XML Schema, section 3.3.19</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema byte type.
     * @param type the type this dialog should be. Setting it to a value out of the range of known values (0 - 3)
     * will cause no image to be displayed.
     */
    public void setType(byte type) throws XUITypeFormatException
    {
        if(type == XUIUtils.QUESTION_TYPE)
        {
            dialogType = JOptionPane.QUESTION_MESSAGE;

        } else if(type == XUIUtils.INFORMATION_TYPE)
        {
            dialogType = JOptionPane.PLAIN_MESSAGE;

        }  else if(type == XUIUtils.WARNING_TYPE)
        {
            dialogType = JOptionPane.WARNING_MESSAGE;

        }  else if(type == XUIUtils.ERROR_TYPE)
        {
            dialogType = JOptionPane.ERROR_MESSAGE;

        } else
            throw new XUITypeFormatException("type falls outside of the boundaries of the XUI types allowed.");
    }

    /**
     * Returns the message of this dialog.
     *
     * @return the message of this dialog as a string.
     * @see #setMessage(String)
     */
    public String getMessage()
    {
        return dialogNode.getAttributeValue("message");
    }

    /**
     * Sets the message of this dialog box. The string values allowed can contain both alphabetic and
     * numeric characters. Real-time validation is performed and therefor if a wrong value is entered,
     * an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#string">W3C XML Schema, section 3.2.1</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema ID
     * type, or if the string is null or larger than 128 characters, an exception occurs.
     * @param newMessage the new message to add to this dialog.
     */
    public void setMessage(String newMessage) throws XUITypeFormatException
    {
        if(newMessage.length() > 128)
            throw new XUITypeFormatException("dialog message must be 128 characters or less.");
        else if(newMessage == null)
            throw new XUITypeFormatException("dialog message cannot be null.");
        else
        {
            dialogNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "message", "message",
                "xs:unsignedShort", newMessage);

        }
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
        return null;
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
     * <p><b>For BasicDialog, this method method does not work. This is because dialogs are automatically
     * displayed in the center of the component that they are the child of.</b></p>
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
        ; // nada
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
     * <p><b>For BasicDialog, this method method does not work. This is because dialogs are automatically
     * displayed in the center of the component that they are the child of.</b></p>
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
        ; // nada
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
    }

    /**
     * If this component has its visible attribute set to true, the component will then be visualized (i.e.
     * displayed on the screen).
     */
    public void visualize()
    {
        if(windowVisibility)
            JOptionPane.showMessageDialog(window.getPeer(),
                dialogNode.getAttributeValue("message"), dialogNode.getAttributeValue("name"), dialogType);
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

}
