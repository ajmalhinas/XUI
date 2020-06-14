package org.purnamaproject.xui.impl;

/**
 * @(#)XUIProgressBarImpl.java  0.5 18/08/2003
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
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.JProgressBar;
import org.purnamaproject.xui.helpers.IDFactory;
import org.purnamaproject.xui.binding.XUIModel;
import org.purnamaproject.xui.component.XUIComponent;
import org.purnamaproject.xui.component.atomic.XUIProgressBar;
import org.purnamaproject.xui.component.container.intermediate.XUIPanel;
import org.purnamaproject.xui.impl.XUINodeImpl;
import org.purnamaproject.xui.XUIDisplayException;
import org.purnamaproject.xui.XUINode;
import org.purnamaproject.xui.XUITypeFormatException;

/**
 * The implementation of the progress bar.
 *
 * @version 0.5 18/08/2003
 * @author  Arron Ferguson
 */
public class XUIProgressBarImpl implements XUIProgressBar
{
    /**
     * The button representation of this component.
     */
    private JProgressBar bar;

    /**
     * The node representation of this component.
     */
    private XUINode barNode;

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
     * <p>Constructs a new XUIProgressBar implementation and passing it its XML node rather than requiring
     * the XUIProgressBar implementation to build it from scratch. This method is called by the Realizer
     * when it reads the XML document from the file system.</p>
     *
     * <p><b><u>Note:</u></b> This constructor is not normally something that should be called up.
     * This constructor is required when creating a XUI Progress Bar where the node has already been built
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
    public XUIProgressBarImpl(XUINode node) throws XUIDisplayException
    {
        gridX = Integer.parseInt(node.getAttributeValue("x"));
        gridY = Integer.parseInt(node.getAttributeValue("y"));
        gridWidth = Integer.parseInt(node.getAttributeValue("width"));
        gridHeight = Integer.parseInt(node.getAttributeValue("height"));

        if(!(node.getName().equals("ProgressBar")))
            throw new XUIDisplayException("Node for XUIProgressBar must be named 'ProgressBar' and conform to the XUI schema.");
        else
        {
            // gui component
            bar = new JProgressBar(0, 100);
            bar.getModel().setValue(Integer.parseInt(node.getAttributeValue("statusValue")));

            bar.setStringPainted(true);
            bar.setString(node.getAttributeValue("label"));
            bar.setEnabled(Boolean.valueOf(node.getAttributeValue("enabled")).booleanValue());

            barNode = node;
            barNode.setXUIComponent(this);
        }
    }

    /**
     * Creates a progress bar. Default size is 1 x 1 cells in the top left corner with a status value of 0.
     */
    public XUIProgressBarImpl()
    {
        // gui component
        bar = new JProgressBar(0, 100);
        bar.setStringPainted(true);

        // create the XML node representing this component.
        barNode = new XUINodeImpl("ProgressBar");
        barNode.setLevel(4);
        barNode.addNamespace("xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
        barNode.setXUIComponent(this);

        // add specific attributes
        barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "id", "id", "xs:ID",
            IDFactory.getInstance().generateID("progressbar"));
        barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "x", "x", "xs:unsignedShort",
            "0");
        barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "y", "y", "xs:unsignedShort",
            "0");
        barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
            "1");
        barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
            "1");
        barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "statusValue", "statusValue",
            "xs:unsignedShort", "0");
        barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "label", "label",
            "xs:token", "Progress");

    }

    /**
     * Creates a progress bar and gives it x and y coordinates. If the progress bar has a parent,
     * then the progress bar will be placed at that position. The x and y values must be within the XUI
     * schema specification as well, be placed on the panel and not overtop of any other component.
     * If so, an exception is generated.
     *
     * @see #setComponentLocation(int, int)
     * @throws org.purnamaproject.xui.XUIDisplayException if the component is being placed overtop of an existing component on the
     * layout.
     * @param xCoordinate the x coordinate used to place the button on the grid.
     * @param yCoordinate the y coordinate used to place the button on the grid.
     */
    public XUIProgressBarImpl(int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        this();

        if(xCoordinate < 0 || xCoordinate > 65535)
            throw new XUIDisplayException("x and y values must be within the range of 0 - 65535.");
        if(yCoordinate < 0 || yCoordinate > 65535)
            throw new XUIDisplayException("x and y values must be within the range of 0 - 65535.");
        else
        {
            gridX = xCoordinate;
            gridY = yCoordinate;
            barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "x", "x", "xs:unsignedShort",
                String.valueOf(xCoordinate));
            barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "y", "y", "xs:unsignedShort",
                String.valueOf(yCoordinate));
        }
    }

    /**
     * Creates a progress bar and gives it x and y coordinates with text. If the progress bar has a parent,
     * then the progress bar will be placed at that position. The x and y values must be within the XUI
     * schema specification as well, be placed on the panel and not overtop of any other component.
     * If so, an exception is generated.
     *
     * @see #setComponentLocation(int, int)
     * @throws org.purnamaproject.xui.XUIDisplayException if the component is being placed overtop of an existing component on the
     * layout.
     * @param xCoordinate the x coordinate used to place the button on the grid.
     * @param yCoordinate the y coordinate used to place the button on the grid.
     * @param text the text that goes into this label. If the string is null, an exception is thrown.
     */
    public XUIProgressBarImpl(int xCoordinate, int yCoordinate, String text) throws XUIDisplayException
    {
        this(xCoordinate, yCoordinate);

        if(text == null)
            throw new XUIDisplayException("text cannot be a null string.");
        if(xCoordinate < 0 || xCoordinate > 65535)
            throw new XUIDisplayException("x and y values must be within the range of 0 - 65535.");
        if(yCoordinate < 0 || yCoordinate > 65535)
            throw new XUIDisplayException("x and y values must be within the range of 0 - 65535.");
        else
        {
            barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "label", "label", "xs:token",
                text);
            bar.setString(text);
        }
    }

    /**
     * Creates a progress bar and gives it x and y coordinates with a status value. If the progress bar has a parent,
     * then the progress bar will be placed at that position. The x and y values must be within the XUI
     * schema specification as well, be placed on the panel and not overtop of any other component.
     * If so, an exception is generated.
     *
     * @see #setComponentLocation(int, int)
     * @throws org.purnamaproject.xui.XUIDisplayException if the component is being placed overtop of an existing component on the
     * layout.
     * @param xCoordinate the x coordinate used to place the button on the grid.
     * @param yCoordinate the y coordinate used to place the button on the grid.
     * @param statusValue the status of the progress. Given as a number between 0 and 100. If the int is beyond
     * or below that range, an exception is thrown.
     */
    public XUIProgressBarImpl(int xCoordinate, int yCoordinate, int statusValue) throws XUIDisplayException
    {
        this(xCoordinate, yCoordinate);

        if(statusValue > 100 || statusValue < 0)
            throw new XUIDisplayException("Position for XUIStatusBar must be between 0 and 100.");
        if(xCoordinate < 0 || xCoordinate > 65535)
            throw new XUIDisplayException("x and y values must be within the range of 0 - 65535.");
        if(yCoordinate < 0 || yCoordinate > 65535)
            throw new XUIDisplayException("x and y values must be within the range of 0 - 65535.");
        else
        {
            barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "label", "label", "xs:unsignedShort",
                statusValue + "");
            bar.getModel().setValue(statusValue);
        }
    }

    /**
     * Creates a progress bar and gives it x and y coordinates with a status value and a label. If the progress
     * bar has a parent, then the progress bar will be placed at that position. The x and y values must be
     * within the XUI schema specification as well, be placed on the panel and not overtop of any other component.
     * If so, an exception is generated.
     *
     * @see #setComponentLocation(int, int)
     * @throws org.purnamaproject.xui.XUIDisplayException if the component is being placed overtop of an existing component on the
     * layout.
     * @param xCoordinate the x coordinate used to place the button on the grid.
     * @param yCoordinate the y coordinate used to place the button on the grid.
     * @param statusValue the status of the progress. Given as a number between 0 and 100. If the int is beyond
     * or below that range, an exception is thrown.
     * @param text the text that goes into this label. If the string is null, an exception is thrown.
     */
    public XUIProgressBarImpl(int xCoordinate, int yCoordinate, int statusValue, String text)
        throws XUIDisplayException
    {
        this(xCoordinate, yCoordinate);

        if(text == null)
            throw new XUIDisplayException("Text for XUIStatusBar cannot be null.");
        if(statusValue > 100 || statusValue < 0)
            throw new XUIDisplayException("Position for XUIStatusBar must be between 0 and 100.");
        if(xCoordinate < 0 || xCoordinate > 65535)
            throw new XUIDisplayException("x and y values must be within the range of 0 - 65535.");
        if(yCoordinate < 0 || yCoordinate > 65535)
            throw new XUIDisplayException("x and y values must be within the range of 0 - 65535.");
        else
        {
            barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "label", "label", "xs:unsignedShort",
                statusValue + "");
            barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "label", "label", "xs:token",
                text);
            bar.getModel().setValue(statusValue);
            bar.setString(text);
        }
    }

    /**
     * <p>Passes the new coordinates and width and height for the dimensions.</p>
     *
     * <p><b>Note:</b> <i>This method call should never be called by any class other than
     * the <code>org.purnamaproject.xui.helpers.ComponentFactory</code> as it is assumed that
     * the XUI document will be created via the API calls rather than through unmarshalling
     * of the XUI document. These two pipelines are treated separately in their handling of
     * Only implementors of the ComponentFactory class should call this method.</i><p>
     *
     * @param x the x coordinate of this component and where to place it within the container
     * that it sits within.
     * @param y the y coordinate of this component and where to place it within the container
     * that it sits within.
     * @param width the width dimension of this component and the size to make it within the container
     * that it sits within.
     * @param height the height dimension of this component and the size to make it within the container
     * that it sits within.
     */
    public void factoryCoordinates(int x, int y, int width, int height)
    {
        gridX = x;
        gridY = y;
        gridWidth = width;
        gridHeight = height;
        barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "x", "x", "xs:unsignedShort",
            "" + x);
        barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "y", "y", "xs:unsignedShort",
            "" + y);
        barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
            "" + width);
        barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
            "" + height);

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
     * Sets this component to being enabled. By being enabled, it will respond to user operations.
     *
     * @param enabled true means this component is enabled and will respond to user operations and false
     * means that it will not.
     */
    public void setEnabled(boolean enabled)
    {
        bar.setEnabled(enabled);
        barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "enabled", "enabled", "xs:boolean",
            "" + bar.isEnabled());
    }

    /**
     * Returns the XUI id of this component.
     *
     * @return the id of this component as a string.

     */
    public String getID()
    {
        return barNode.getAttributeID();
    }

    /**
     * Represents the XUI window component as a XUI node.
     *
     * @return org.purnamaproject.xui.XUINode the XML element as a XUI node.
     */
    public XUINode getNodeRepresentation()
    {
        return barNode;
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
        return bar;
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
            XUIPanel parent = null;

            XUINode parentNode = barNode.getParent();
            if(parentNode != null)
                parent = (XUIPanel)parentNode.getXUIComponent();

            // proposed space to occupy
            Rectangle spaceToOccupy = new Rectangle(yCoordinate, xCoordinate, gridWidth, gridHeight);


            if(parent != null)
            {

                // collision detection
                Hashtable grid = parent.getGridMapping();

                Enumeration keys = grid.keys();

                container = (Container)parent.getPeer();

                // need to check if the space is occupied AND if that space if occupied by
                // someone OTHER than the current proposed component
                while(keys.hasMoreElements())
                {
                    XUIComponent someComponent = (XUIComponent)keys.nextElement();
                    Rectangle region = (Rectangle)grid.get(someComponent);
                    if(region.intersects(spaceToOccupy) && someComponent != this)
                        throw new XUIDisplayException("Component:\n" + someComponent + "\nis already in the area x: "
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
                    container.remove(bar);
                    container.add(bar, new Rectangle(gridY, gridX, gridWidth, gridHeight));
                    bar.invalidate();
                    container.validate();

                    barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "x", "x", "xs:unsignedShort",
                        String.valueOf(xCoordinate));
                    barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "y", "y", "xs:unsignedShort",
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
            XUIPanel parent = null;

            XUINode parentNode = barNode.getParent();
            if(parentNode != null)
                parent = (XUIPanel)parentNode.getXUIComponent();

            // proposed space to occupy
            Rectangle spaceToOccupy = new Rectangle(gridY, gridX, width, height);


            if(parent != null)
            {

                // collision detection
                Hashtable grid = parent.getGridMapping();

                Enumeration keys = grid.keys();

                container = (Container)parent.getPeer();

                // need to check if the space is occupied AND if that space if occupied by
                // someone OTHER than the current proposed component
                while(keys.hasMoreElements())
                {
                    XUIComponent someComponent = (XUIComponent)keys.nextElement();
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
                    container.remove(bar);
                    container.add(bar, new Rectangle(gridY, gridX, gridWidth, gridHeight));
                    bar.invalidate();
                    container.validate();

                    barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
                        String.valueOf(width));
                    barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
                        String.valueOf(height));
                }
            }

        }
    }

    /**
     * Returns the status value indicating the progress of the task.
     *
     * @return the percent of completion of a particular task.
     * @see #setStatusValue(int)
     */
    public int getStatusValue()
    {
        return bar.getModel().getValue();
    }

    /**
     * Sets the percentage of completion of a particular task. The value allowed for this must fall within the
     * range from 0 to 100. Real-time validation is performed and therefor if a wrong value is entered,
     * an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#unsignedShort">W3C XML Schema, section 3.3.23</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema unsignedshort type.
     * @param statusValue the position of the knob on the slider bar.
     */
    public void setStatusValue(int statusValue) throws XUITypeFormatException
    {
        if(statusValue > 100 || statusValue < 0)
            throw new XUITypeFormatException("Position for XUIStatusBar must be between 0 and 100.");
        bar.getModel().setValue(statusValue);
        barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
            String.valueOf(statusValue));

    }

    /**
     * Returns the label of this component.
     *
     * @return the label of this component as a string.
     * @see #setLabel(String)
     */
    public String getLabel()
    {
        return bar.getString();
    }

    /**
     * Sets the label of this component. The string values allowed can contain
     * both alphabetic and numeric characters. Real-time validation is performed and
     * therefore if a wrong value is entered, an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#string">W3C XML Schema, section 3.2.1</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema string
     * type, or if the string is null, an exception occurs.
     * @param newLabel the new label to assign to this component.
     */
    public void setLabel(String newLabel) throws XUITypeFormatException
    {
        if(newLabel == null)
            throw new XUITypeFormatException("Label for XUIProgressBar cannot be null.");
        else
        {
            bar.setString(newLabel);
            barNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "label", "label", "xs:string",
                bar.getString());
        }
    }

}
