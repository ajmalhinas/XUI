package org.purnamaproject.xui.impl;

/**
 * @(#)XUIMenu.java 0.5 18/08/2003
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
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import org.purnamaproject.xui.helpers.IDFactory;
import org.purnamaproject.xui.binding.XUIModel;
import org.purnamaproject.xui.component.XUIComponent;
import org.purnamaproject.xui.component.menu.XUIMenu;
import org.purnamaproject.xui.component.menu.XUIMenuItem;
import org.purnamaproject.xui.impl.XUINodeImpl;
import org.purnamaproject.xui.XUIDisplayException;
import org.purnamaproject.xui.XUINode;
import org.purnamaproject.xui.XUITypeFormatException;
import org.purnamaproject.xui.peer.JImagePanel;
import org.purnamaproject.xui.peer.PopupListener;
import org.purnamaproject.xui.binding.ActionModel;

/**
 * The menu represents a GUI component sits atop of a menu bar. Menus can have menu items. Menus
 * can also be popup menus as well as sub menus. If a menu is a popup menu, then it is only shown
 * when the user right clicks.
 *
 * @version 0.5 18/08/2003
 * @author  Arron Ferguson
 */
public class XUIMenuImpl implements XUIMenu
{
    /**
     * The menu representation of this component.
     */
    private MenuElement menu;

    /**
     * Popup listener for the menu if it's a popup menu.
     */
    private MouseListener popupListener = null;

    /**
     * The node representation of this component.
     */
    private XUINode menuNode;

    /**
     * <p>Constructs a new XUIMenu implementation and passing it its XML node rather than requiring
     * the XUIMenu implementation to build it from scratch. This method is called by the Realizer
     * when it reads the XML document from the file system.</p>
     *
     * <p><b><u>Note:</u></b> This constructor is not normally something that should be called up.
     * This constructor is required when creating a XUI Menu where the node has already been built
     * by the realizer or a subclass thereof.</p>
     *
     * <p><b><u>Note:</u></b> Schema checking is assumed at this point. Any node passed to this method
     * will be assumed to have passed through the schema validator.</p>
     *
     * @throws org.purnamaproject.xui.XUIDisplayException if the node is null.
     * @param node the new node representing this GUI component.
     */
    public XUIMenuImpl(XUINode node) throws XUIDisplayException
    {

        if(!(node.getName().equals("Menu")))
            throw new XUIDisplayException("Node for XUIMenu must be named 'Menu' and conform to the XUI schema.");
        else
        {
            // gui component
            if(node.getAttributeValue("isPopupMenu").equals("true"))
            {
                JPopupMenu m = new JPopupMenu();
                m.setEnabled(Boolean.valueOf(node.getAttributeValue("enabled")).booleanValue());
                m.setLabel(node.getAttributeValue("label"));
                menu = m;

            }
            else
            {
                JMenu m = new JMenu();
                m.setEnabled(Boolean.valueOf(node.getAttributeValue("enabled")).booleanValue());
                m.setText(node.getAttributeValue("label"));
                menu = m;
            }

            menuNode = node;
            menuNode.setXUIComponent(this);
        }
    }

    /**
     * Creates a menu with the default textual label 'Menu'.
     */
    public XUIMenuImpl()
    {
        // gui component
        menu = new JMenu("Menu");

        // create the XML node representing this component.
        menuNode = new XUINodeImpl("Menu");
        menuNode.setLevel(3);
        menuNode.addNamespace("xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
        menuNode.setXUIComponent(this);

        // add specific attributes
        menuNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "isSubMenu", "isSubMenu",
            "xs:boolean", "false");
        menuNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "isPopupMenu", "isPopupMenu",
            "xs:boolean", "false");
        menuNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "id", "id", "xs:ID",
            IDFactory.getInstance().generateID("menu"));
        menuNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "label", "label", "xs:token",
            "");
        menuNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "enabled", "enabled", "xs:boolean",
            "true");

    }

    /**
     * Creates a menu with passed textual label added to it.
     *
     * @param newLabel the label to add to the new menu
     * @throws org.purnamaproject.xui.XUIDisplayException if the component is being placed overtop of an existing component on the
     * layout.
     */
    public XUIMenuImpl(String newLabel)
    {
        this();
        if(newLabel == null || newLabel.matches("^\n") || newLabel.matches("^\t"))
            throw new XUIDisplayException("label for menu cannot be null and must match xs:token.");
        else
        {
            menuNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "label", "label", "xs:token",
                newLabel);
            if(menu instanceof JMenu)
            {
                JMenu mc = (JMenu)menu;
                mc.setText(newLabel);

            } else if(menu instanceof JPopupMenu)
            {
                JPopupMenu mc = (JPopupMenu)menu;
                mc.setLabel(newLabel);

            }
        }
    }

    /**
     * Sets this component to being enabled. By being enabled, it will respond to user operations.
     *
     * @param enabled true means this component is enabled and will respond to user operations and false
     * means that it will not.
     */
    public void setEnabled(boolean enabled)
    {
        Component menuComponent = (Component)menu;

        menuComponent.setEnabled(enabled);
        menuNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "enabled", "enabled", "xs:boolean",
            "" + menuComponent.isEnabled());
    }

    /**
     * Returns the label of this component.
     *
     * @return the label of this component as a string.
     * @see #setLabel(String)
     */
    public String getLabel()
    {
        if(menu instanceof JMenu)
        {
            JMenu menuComponent = (JMenu)menu;
            menuComponent.getText();

        }
        else if(menu instanceof JPopupMenu)
        {
            JPopupMenu menuComponent = (JPopupMenu)menu;
            menuComponent.getLabel();
        }

        return null;
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
            throw new XUITypeFormatException("Label for XUIMenu cannot be null.");
        else
        {
            if(menu instanceof JMenu)
            {
                JMenu menuComponent = (JMenu)menu;
                menuComponent.setText(newLabel);

            }
            else if(menu instanceof JPopupMenu)
            {
                JPopupMenu menuComponent = (JPopupMenu)menu;
                menuComponent.setLabel(newLabel);

            }

            menuNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "label", "label", "xs:string",
                newLabel);
        }
    }

    /**
     * Returns the whether or not this menu is a pop-up menu.
     *
     * @return true if popup menu, false otherwise.
     * @see #setIsPopupMenu(boolean)
     */
    public boolean getIsPopupMenu()
    {
        if(menu instanceof JMenu)
            return false;
        else if(menu instanceof JPopupMenu)
            return true;
        return false;
    }

    /**
     * Sets whether or not this menu is popup. If true, then this menu is popup. Otherwise, false. The Java
     * boolean type is mapped to the XML Schema boolean type.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#boolean">W3C XML Schema, section 3.2.2</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param makePopup if true then this menu is a popup menu. False means it is not.
     */
    public void setIsPopupMenu(boolean makePopup)
    {
        // little bit of extra work since the type is being switched between the type
        // different classes.
        MenuElement[] elements = menu.getSubElements();
        if(makePopup)
        {
            menu = new JPopupMenu(menuNode.getAttributeValue("label"));
            popupListener = new PopupListener((JPopupMenu)menu);
            for(int i = 0; i < elements.length; i++)
            {
                JMenu menuComponent = (JMenu)menu;
                menuComponent.add((Component)elements[i]);
            }

        } else
        {
            menu = new JMenu(menuNode.getAttributeValue("label"));
            for(int i = 0; i < elements.length; i++)
            {
                JPopupMenu menuComponent = (JPopupMenu)menu;
                menuComponent.add((Component)elements[i]);
            }

        }

        menuNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "isPopupMenu", "isPopupMenu",
            "xs:boolean", "" + makePopup);
    }

    /**
     * Returns the whether or not this menu is a sub-menu. If this menu is a popup menu, then this will
     * always return false.
     *
     * @return true if sub-menu, false otherwise.

     */
    public boolean getIsSubMenu()
    {
        if(menu instanceof JMenu)
        {
            JMenu menuComponent = (JMenu)menu;
            return !(menuComponent.isTopLevelMenu());

        }
        else if(menu instanceof JPopupMenu)
            return false;
        return false;
    }

    /**
     * Adds a new XUI menu item to this menu. If the menu item is null, an exception is thrown.
     * This version of this add menu method should only be called up by the realizer as it does
     * not add any child nodes to the XUI DOM since it's assumed that the child nodes already
     * exist.
     *
     * @throws org.purnamaproject.xui.XUITypeFormatException if the menu item is null, an exception occurs.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param menuItem the menu item to add to this menu.
     */
    public void addMenuItemForRealizer(XUIMenuItem menuItem) throws XUITypeFormatException
    {
        if(menuItem == null)
            throw new XUITypeFormatException("XUIMenuItem cannot be null.");
        Component peer = (Component)menuItem.getPeer();
        if(peer == null)
            throw new XUITypeFormatException("XUIMenuItem's peer cannot be null.");
        else
        {
            if(menu instanceof JMenu)
            {
                JMenu menuComponent = (JMenu)menu;
                menuComponent.add(menuItem.getPeer());

            }
            else if(menu instanceof JPopupMenu)
            {
                JPopupMenu menuComponent = (JPopupMenu)menu;
                menuComponent.add(menuItem.getPeer());

            }
        }
    }

    /**
     * Adds a new XUI menu item to this menu. If the menu item is null, an exception is thrown.
     *
     * @throws org.purnamaproject.xui.XUITypeFormatException if the menu item is null, an exception occurs.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param menuItem the menu item to add to this menu.
     */
    public void addMenuItem(XUIMenuItem menuItem) throws XUITypeFormatException
    {
        if(menuItem == null)
            throw new XUITypeFormatException("XUIMenuItem cannot be null.");
        Component peer = (Component)menuItem.getPeer();
        if(peer == null)
            throw new XUITypeFormatException("XUIMenuItem's peer cannot be null.");
        else
        {
            if(menu instanceof JMenu)
            {
                JMenu menuComponent = (JMenu)menu;
                menuComponent.add(menuItem.getPeer());
                menuNode.addChildNode(menuItem.getNodeRepresentation());

            }
            else if(menu instanceof JPopupMenu)
            {
                JPopupMenu menuComponent = (JPopupMenu)menu;
                menuComponent.add(menuItem.getPeer());
                menuNode.addChildNode(menuItem.getNodeRepresentation());

            }
        }
    }

    /**
     * Removes the menu item from this menu. Note: if this menu item contains a sub menu on it, the entire
     * sub-structure will be deleted as well. (e.g. like how deleting a directory will delete its
     * sub-directories).
     *
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema string
     * type, or if the string is null, an exception occurs.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param id the id of the menu item.
     */
    public void removeMenuItem(String id) throws XUITypeFormatException
    {
        if(id == null)
            throw new XUITypeFormatException("id cannot be null.");
        else
        {
            // search based on id
            List children = menuNode.getChildNodesByName("MenuItem");
            for(int i = 0; i < children.size(); i++)
            {
                XUINode node = (XUINode)children.get(i);
                if(node.getAttributeValue("id").equals(id))
                {
                    XUIComponent xuiComponent = node.getXUIComponent();
                    Component peer = xuiComponent.getPeer();
                    XUINode parentNode = node.getParent();

                    XUIComponent xuiComponentParent = parentNode.getXUIComponent();
                    Container parentPeer = (Container)xuiComponentParent.getPeer();
                    parentPeer.remove(peer);
                    parentNode.removeChildNode(node);
                    return;
                }
            }

        }
    }

    /**
     * Adds a new XUI menu to this menu. If the menu is null, an exception is thrown. This method should
     * only be called by the <code>Realizer</code> class since it already assumes a DOM node structure and
     * does not build any child nodes.
     *
     * @throws org.purnamaproject.xui.XUITypeFormatException if the menu is null, an exception occurs.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param newMenu the menu to add to this menu item.
     */
    public void addMenuForRealizer(XUIMenu newMenu) throws XUITypeFormatException
    {
        if(newMenu == null)
            throw new XUITypeFormatException("XUIMenu cannot be null.");

        Container menuComponent = (Container)menu;
        menuComponent.add(newMenu.getPeer());


    }

    /**
     * Adds a menu to this menu bar using the index given. If the index is < 0, no action is taken.
     * If the index is 2 or more greater than the current number of menus, the menu will simply be added
     * to the end of the menubar (last place). If the menu is null, an exception is thrown. As well, if the
     * node already exists within a XUI document hierarchy, then an exception is thrown. Normally in
     * GUI programming this is not a problem. However with XUI, each node has a unique ID and thus
     * each XUI component must be unique since the XML schema requires each element carrying attributes
     * whose type is xs:ID must be unique.
     *
     * @throws org.purnamaproject.xui.XUITypeFormatException if the menu is null, an exception occurs.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @see #addMenu(XUIMenu)
     * @param newMenu the menu to add to this menu item.
     * @param index the position on the menu bar where this menu should go.
     */
    public void addMenu(XUIMenu newMenu, int index) throws XUITypeFormatException
    {
        if(newMenu == null)
            throw new XUITypeFormatException("XUIMenu cannot be null.");

        List children = menuNode.getAllChildNodes();
        List menuNodes = menuNode.getDirectChildren();
        int numberOfMenus = menuNodes.size();

        for(int i = 0; i < children.size(); i++)
        {
            XUINode child = (XUINode)children.get(i);
            if(child.getAttributeID().equals(newMenu.getNodeRepresentation().getAttributeID()))
                throw new XUITypeFormatException("No 2 menu components can have the same ID.");
        }
        Container menuComponent = (Container)menu;
        if(index > (numberOfMenus + 1))
            menuComponent.add(newMenu.getPeer(), (numberOfMenus + 1));
        else if(index < 0)
            index = 0;
        else
            menuComponent.add(newMenu.getPeer(), index);

        menuNode.addChildNode(newMenu.getNodeRepresentation());

    }

    /**
     * Adds a new XUI menu to this menu. If the menu is null, an exception is thrown. As well, if the
     * node already exists within a XUI document hierarchy, then an exception is thrown. Normally in
     * GUI programming this is not a problem. However with XUI, each node has a unique ID and thus
     * each XUI component must be unique since the XML schema requires each element carrying attributes
     * whose type is xs:ID must be unique.
     *
     * @throws org.purnamaproject.xui.XUITypeFormatException if the menu is null, an exception occurs.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param newMenu the menu to add to this menu item.
     */
    public void addMenu(XUIMenu newMenu) throws XUITypeFormatException
    {
        if(newMenu == null)
            throw new XUITypeFormatException("XUIMenu cannot be null.");

        List children = menuNode.getAllChildNodes();
        for(int i = 0; i < children.size(); i++)
        {
            XUINode child = (XUINode)children.get(i);
            if(child.getAttributeID().equals(newMenu.getNodeRepresentation().getAttributeID()))
                throw new XUITypeFormatException("No 2 menu components can have the same ID.");
        }
        Container menuComponent = (Container)menu;
        menuComponent.add(newMenu.getPeer());
        menuNode.addChildNode(newMenu.getNodeRepresentation());

    }

    /**
     * Removes the menu from this menu. Note: if this menu contains a menu items on it, the entire
     * sub-structure will be deleted as well. (e.g. like how deleting a directory will delete its
     * sub-directories).
     *
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema string
     * type, or if the string is null, an exception occurs.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param id the id of the menu.
     */
    public void removeMenu(String id) throws XUITypeFormatException
    {
        if(id == null)
            throw new XUITypeFormatException("id cannot be null.");
        else
        {
            // search based on id
            List children = menuNode.getChildNodesByName("Menu");
            for(int i = 0; i < children.size(); i++)
            {
                XUINode node = (XUINode)children.get(i);
                if(node.getAttributeValue("id").equals(id))
                {
                    XUIComponent xuiComponent = node.getXUIComponent();
                    Component peer = xuiComponent.getPeer();
                    XUINode parentNode = node.getParent();

                    // remove popup listeners if any
                    if(peer instanceof JPopupMenu)
                    {
                        JPopupMenu popup = (JPopupMenu)peer;
                        MouseListener[] listeners = popup.getMouseListeners();
                        for(int x = 0; x < listeners.length; x++)
                        {
                            popup.removeMouseListener(listeners[x]);
                        }

                    }

                    XUIComponent xuiComponentParent = parentNode.getXUIComponent();
                    Container parentPeer = (Container)xuiComponentParent.getPeer();
                    parentPeer.remove(peer);
                    parentNode.removeChildNode(node);
                    return;
                }
            }

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
        return menuNode.getAttributeValue("idref");
    }

    /**
     * Sets the XUI id reference of this component. This value is a reference to parent container that
     * this component is a child of. The value of the reference must be the value of an existing key
     * within the XUI document.  The string values allowed can contain both alphabetic and numeric
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
        if(newIDRef.startsWith("[a-zA-Z]"))
            menuNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "idref", "idref", "xs:IDREF",
                newIDRef);
        else
            throw new XUITypeFormatException("ID reference must start with alphabetic only: a - z or A - Z.");
    }

    /**
     * Returns the XUI id of this component.
     *
     * @return the id of this component as a string.

     */
    public String getID()
    {
        return menuNode.getAttributeID();
    }

    /**
     * Represents the XUI window component as a XUI node.
     *
     * @return org.purnamaproject.xui.XUINode the XML element as a XUI node.
     */
    public XUINode getNodeRepresentation()
    {
        return menuNode;
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
        return (Component)menu;
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
     * <b>For menu components this method does nothing.</b>
     *
     * <p>If the component is a top-level container, then the x and y coordinate values refer to the screen
     * coordinates on the screen. The same rules apply to adhering to the integer range constraint. If the
     * top-level container is placed outside of the screen resolution of the host system, then a
     * <code>XUIDisplayException</code> is thrown and no change is made.</p>
     *
     * <b>For menu components this method does nothing.</b>
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
     * <b>For menu components this method does nothing.</b>
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
     * Removes the menu item from this menu. Note: if this menu item contains a sub menu on it, the entire
     * sub-structure will be deleted as well. (e.g. like how deleting a directory will delete its
     * sub-directories). If the menu item is not found or if it is null, no action is taken
     *
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param menuItem the menu item to remove.
     */
    public void removeMenuItem(XUIMenuItem menuItem)
    {
        if(menuItem == null)
            return;

        List children = menuNode.getChildNodesByName("MenuItem");
        for(int i = 0; i < children.size(); i++)
        {
            XUINode node = (XUINode)children.get(i);
            XUIMenuItem item = (XUIMenuItem)node.getXUIComponent();
            if(item == menuItem)
            {
                children.remove(node);
                ((JMenu)menu).remove(item.getPeer());
            }
        }
    }

    /**
     * Removes the menu from this menu component. Note: if this menu contains a sub menu on it, the entire
     * sub-structure will be deleted as well. (e.g. like how deleting a directory will delete its
     * sub-directories). If the menu is not found or if it is null, no action is taken
     *
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param menu the menu to remove.
     */
    public void removeMenu(XUIMenu menu)
    {
        if(menu == null)
            return;

        List children = menuNode.getChildNodesByName("Menu");
        for(int i = 0; i < children.size(); i++)
        {
            XUINode node = (XUINode)children.get(i);
            XUIMenu m = (XUIMenu)node.getXUIComponent();
            if(m == menu)
            {
                children.remove(node);
                ((JMenu)menu).remove(m.getPeer());
            }
        }
    }

}
