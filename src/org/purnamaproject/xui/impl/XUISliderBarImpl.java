package org.purnamaproject.xui.impl;

/**
 * @(#)XUISliderBarImpl.java    0.5 18/08/2003
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.JSlider;
import org.purnamaproject.xui.helpers.IDFactory;
import org.purnamaproject.xui.helpers.XUIUtils;
import org.purnamaproject.xui.binding.XUIModel;
import org.purnamaproject.xui.binding.ActionModel;
import org.purnamaproject.xui.component.container.XUIContainer;
import org.purnamaproject.xui.component.XUIComponent;
import org.purnamaproject.xui.component.atomic.XUISliderBar;
import org.purnamaproject.xui.component.container.intermediate.XUIPanel;
import org.purnamaproject.xui.impl.XUINodeImpl;
import org.purnamaproject.xui.XUIDisplayException;
import org.purnamaproject.xui.XUINode;
import org.purnamaproject.xui.XUITypeFormatException;

/**
 * The implementation of the slider bar.
 *
 * @version 0.5 18/08/2003
 * @author  Arron Ferguson
 */
public class XUISliderBarImpl implements XUISliderBar, ChangeListener
{
    /**
     * The button representation of this component.
     */
    private JSlider slider;

    /**
     * The node representation of this component.
     */
    private XUINode sliderNode;

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
     * The list of model objects that are ready to be listeners to any events generated by this
     * component.
     */
    private List actionModelList = new java.util.LinkedList();

    /**
     * <p>Constructs a new XUISliderBar implementation and passing it its XML node rather than requiring
     * the XUISliderBar implementation to build it from scratch. This method is called by the Realizer
     * when it reads the XML document from the file system.</p>
     *
     * <p><b><u>Note:</u></b> This constructor is not normally something that should be called up.
     * This constructor is required when creating a XUI Sliderbar where the node has already been built
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
    public XUISliderBarImpl(XUINode node) throws XUIDisplayException
    {
        gridX = Integer.parseInt(node.getAttributeValue("x"));
        gridY = Integer.parseInt(node.getAttributeValue("y"));
        gridWidth = Integer.parseInt(node.getAttributeValue("width"));
        gridHeight = Integer.parseInt(node.getAttributeValue("height"));

        if(!(node.getName().equals("SliderBar")))
            throw new XUIDisplayException("Node for XUISliderBar must be named 'SliderBar' and conform to the XUI schema.");
        else
        {
            // gui component
            slider = new JSlider();
            slider.setPaintLabels(true);
            slider.setPaintTicks(true);

            sliderNode = node;
            sliderNode.setXUIComponent(this);
            slider.setMinimum(0);
            slider.setMaximum(Integer.parseInt(node.getAttributeValue("maxValue")));
            slider.setEnabled(Boolean.valueOf(node.getAttributeValue("enabled")).booleanValue());
            slider.setMajorTickSpacing(Integer.parseInt(node.getAttributeValue("visualMeasureIncrement")));
            slider.setValue(Integer.parseInt(node.getAttributeValue("cursorPosition")));


            if(sliderNode.getAttributeValue("orientation").equals("vertical"))
            {
                slider.setOrientation(JSlider.VERTICAL);

            }
            else if(sliderNode.getAttributeValue("orientation").equals("horizontal"))
            {
                slider.setOrientation(JSlider.HORIZONTAL);

            } else
                throw new XUITypeFormatException("orientation must either be 'XUIUtils.ORIENT_HORIZONTAL' or 'XUIUtils.ORIENT_VERTICAL'.");

        }
    }

    /**
     * Creates a slider with with a default value of 0 and increment value of 5 and horizontal in
     * orientation. Default size is 1 x 1 cells in the top left corner.
     */
    public XUISliderBarImpl()
    {
        // gui component
        slider = new JSlider();
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);

        slider.setMinimum(0);
        slider.setMaximum(100);
        slider.setMajorTickSpacing(5);
        slider.setValue(0);
        slider.setOrientation(JSlider.HORIZONTAL);

        // create the XML node representing this component.
        sliderNode = new XUINodeImpl("Slider");
        sliderNode.setLevel(4);
        sliderNode.setXUIComponent(this);
        sliderNode.addNamespace("xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
        sliderNode.setXUIComponent(this);

        // add specific attributes
        sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "id", "id", "xs:ID",
            IDFactory.getInstance().generateID("slider"));
        sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "x", "x", "xs:unsignedShort",
            "0");
        sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "y", "y", "xs:unsignedShort",
            "0");
        sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
            "1");
        sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
            "1");
        sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "orientation", "orientation", "xs:token",
            "horizontal");
        sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "enabled", "enabled", "xs:boolean",
            "true");
        sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "cursorPosition", "cursorPosition",
            "xs:unsignedShort", "0");
        sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "visualMeasureIncrement",
            "visualMeasureIncrement", "xs:unsignedShort", "5");
        sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "maxValue", "maxValue",
            "xs:unsignedShort", "100");

    }

    /**
     * Creates a slider and gives it x and y coordinates. If the slider has a parent,
     * then the slider will be placed at that position. The x and y values must be within the XUI
     * schema specification as well, be placed on the panel and not overtop of any other component.
     * If so, an exception is generated.
     *
     * @see #setComponentLocation(int, int)
     * @throws org.purnamaproject.xui.XUIDisplayException if the component is being placed overtop of an existing component on the
     * layout.
     * @param xCoordinate the x coordinate used to place the button on the grid.
     * @param yCoordinate the y coordinate used to place the button on the grid.
     */
    public XUISliderBarImpl(int xCoordinate, int yCoordinate) throws XUIDisplayException
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
            sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "x", "x", "xs:unsignedShort",
                String.valueOf(xCoordinate));
            sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "y", "y", "xs:unsignedShort",
                String.valueOf(yCoordinate));
        }

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
        slider.setEnabled(enabled);
        sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "enabled", "enabled", "xs:boolean",
            "" + slider.isEnabled());
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
        sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "x", "x", "xs:unsignedShort",
            "" + x);
        sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "y", "y", "xs:unsignedShort",
            "" + y);
        sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
            "" + width);
        sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
            "" + height);
    }

    /**
     * Returns the XUI id of this component.
     *
     * @return the id of this component as a string.

     */
    public String getID()
    {
        return sliderNode.getAttributeID();
    }

    /**
     * Represents the XUI window component as a XUI node.
     *
     * @return org.purnamaproject.xui.XUINode the XML element as a XUI node.
     */
    public XUINode getNodeRepresentation()
    {
        return sliderNode;
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
        return slider;
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

            XUINode parentNode = sliderNode.getParent();
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
                    container.remove(slider);
                    container.add(slider, new Rectangle(gridY, gridX, gridWidth, gridHeight));
                    slider.invalidate();
                    container.validate();

                    sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "x", "x", "xs:unsignedShort",
                        String.valueOf(xCoordinate));
                    sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "y", "y", "xs:unsignedShort",
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

            XUINode parentNode = sliderNode.getParent();
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
                    container.remove(slider);
                    container.add(slider, new Rectangle(gridY, gridX, gridWidth, gridHeight));
                    slider.invalidate();
                    container.validate();

                    sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
                        String.valueOf(width));
                    sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
                        String.valueOf(height));
                }
            }

        }
    }

    /**
     * Sets the orientation of this button component. Orientation refers to how the image and text are
     * placed. If there is both an image and text, then this value has meaning. If vertical, then the image
     * is overtop of the text. If the orientation is set to horizontal, then the image is on the left and the
     * text is on the right. If a value other than vertical or horizontal are given, no change it made. The default
     * value is horizontal.
     *
     * @param orientation the orientation of image and text.
     * @see org.purnamaproject.xui.helpers.XUIUtils#ORIENT_HORIZONTAL
     * @see org.purnamaproject.xui.helpers.XUIUtils#ORIENT_VERTICAL
     */
    public void setOrientation(int orientation)
    {
        if(orientation == XUIUtils.ORIENT_VERTICAL)
        {
            slider.setOrientation(JSlider.VERTICAL);
            sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "orientation", "orientation", "xs:token",
                "vertical");
        }
        else if(orientation == XUIUtils.ORIENT_HORIZONTAL)
        {
            slider.setOrientation(JSlider.HORIZONTAL);
            sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "orientation", "orientation", "xs:token",
                "horizontal");
        } else
            throw new XUITypeFormatException("orientation must either be 'XUIUtils.ORIENT_HORIZONTAL' or 'XUIUtils.ORIENT_VERTICAL'.");

    }

    /**
     * Returns the position of the knob on this sliderbar.
     *
     * @return the position of the knob on this sliderbar.
     * @see #setCurrentPosition(int)
     */
    public int getCurrentPosition()
    {
        return slider.getOrientation();
    }

    /**
     * Sets the position of the knob on this progress bar. The value allowed for this must fall within the
     * range from 0 to 100. Real-time validation is performed and therefor if a wrong value is entered,
     * an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#unsignedShort">W3C XML Schema, section 3.3.23</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema unsignedshort type.
     * @param position the position of the knob on the slider bar.
     */
    public void setCurrentPosition(int position) throws XUITypeFormatException
    {
        if(position < 0 || position > 100)
            throw new XUITypeFormatException("Position must be between the range 0 and 100.");
        else
        {
            slider.getModel().setValue(position);
            sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "cursorPosition", "cursorPosition",
                "xs:unsignedShort", position + "");
        }
    }

    /**
     * Returns the increment value that will be used to move the component in when the user moves the knob
     * on the slider bar. e.g. 5 units per move.
     *
     * @return the visual measure increment value.
     * @see #setVisualMeasureIncrement(int)
     */
    public int getVisualMeasureIncrement()
    {
        return slider.getMajorTickSpacing();
    }

    /**
     * Sets visual measure increment value on the knob. When the user moves the knob or clicks within the
     * bar itself (not on the arrows), the knob moves in a certain number of units. This allows for faster
     * scrolling. The value must fall within the specified range based on the XUI specification.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#unsignedShort">W3C XML Schema, section 3.3.23</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema unsignedshort type.
     * @param increment the position of the knob on the slider bar.
     */
    public void setVisualMeasureIncrement(int increment) throws XUITypeFormatException
    {
        if(increment < 0 || increment > 65535)
            throw new XUITypeFormatException("Increment must be between the range 0 and 65,535.");
        else
        {
            slider.setMajorTickSpacing(increment);
            sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "visualMeasureIncrement",
                "visualMeasureIncrement", "xs:unsignedShort", increment + "");
        }
    }

    /**
     * Returns the maximum value that is represented on the slider bar. e.g. 100.
     *
     * @return the maximum value.
     * @see #setMaxValue(int)
     */
    public int getMaxValue()
    {
        return slider.getMaximum();
    }

    /**
     * Sets the maximum value on the slider bar. This is the ceiling value that represents the top of the range.
     * The value must fall within the specified range based on the XUI specification.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#unsignedShort">W3C XML Schema, section 3.3.23</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema unsignedshort type.
     * @param maxValue the position of the knob on the slider bar.
     */
    public void setMaxValue(int maxValue) throws XUITypeFormatException
    {
        if(maxValue < 0 || maxValue > 65535)
            throw new XUITypeFormatException("Increment must be between the range 0 and 65,535.");
        else
        {
            slider.setMaximum(maxValue);
            sliderNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "maxValue",
                "maxValue", "xs:unsignedShort", maxValue + "");
        }
    }

    /**
     * Handles a change event from the underlying GUI peer.
     *
     * @param e the <code>ChangeEvent</code>.
     */
    public void stateChanged(ChangeEvent e)
    {
        if(e.getSource() == slider)
        {
            int size = actionModelList.size();
            for(int i = 0; i < size; i++)
            {
                ActionModel model = (ActionModel)actionModelList.get(i);
                model.action(this);
            }
        }
    }

    /**
     * Adds a new model to listen to the <code>XUIComponent</code> that also is a <code>XUIEventSource</code>.
     * If the model is not an instance of the ActionModel type, then no binding is done.
     *
     * @param model the model that listens to events based on this type.
     */
    public void addEventListener(XUIModel model)
    {
        if(model instanceof ActionModel)
        {
            // call up the action model.
            actionModelList.add(model);
            slider.addChangeListener(this);
        }
    }

    /**
     * Delete a model from the <code>XUIComponent</code> that also is a <code>XUIEventSource</code>.
     *
     * @param model the model that is to no longer be a listener.
     */
    public void removeEventListener(XUIModel model)
    {
        actionModelList.remove(model);
        slider.removeChangeListener(this);
    }
}
