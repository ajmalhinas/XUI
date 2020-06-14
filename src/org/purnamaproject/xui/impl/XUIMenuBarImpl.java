package org.purnamaproject.xui.impl;

/**
 * @(#)XUIMenuBarImpl.java  0.5 18/08/2003
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
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.MenuElement;
import org.purnamaproject.xui.helpers.IDFactory;
import org.purnamaproject.xui.binding.XUIModel;
import org.purnamaproject.xui.component.menu.XUIMenuBar;
import org.purnamaproject.xui.component.XUIComponent;
import org.purnamaproject.xui.component.menu.XUIMenu;
import org.purnamaproject.xui.impl.XUINodeImpl;
import org.purnamaproject.xui.XUIDisplayException;
import org.purnamaproject.xui.XUINode;
import org.purnamaproject.xui.XUITypeFormatException;
import org.purnamaproject.xui.peer.JWebBrowser;
import org.purnamaproject.xui.peer.PopupListener;
import org.purnamaproject.xui.peer.JScrollTable;
import org.purnamaproject.xui.peer.JScrollTree;
import org.purnamaproject.xui.component.atomic.XUITreeNode;


/**
 * The menu bar represents a GUI component that sits on top of a window that holds menus.
 *
 * @version 0.5 18/08/2003
 * @author  Arron Ferguson
 */
public class XUIMenuBarImpl implements XUIMenuBar
{
    /**
     * The menubar representation of this component.
     */
    private JMenuBar menuBar;

    /**
     * The node representation of this component.
     */
    private XUINode menuBarNode;

    /**
     * <p>Constructs a new XUIMenuBar implementation and passing it its XML node rather than requiring
     * the XUIMenuBar implementation to build it from scratch. This method is called by the Realizer
     * when it reads the XML document from the file system.</p>
     *
     * <p><b><u>Note:</u></b> This constructor is not normally something that should be called up.
     * This constructor is required when creating a XUI Menu bar where the node has already been built
     * by the realizer or a subclass thereof.</p>
     *
     * <p><b><u>Note:</u></b> Schema checking is assumed at this point. Any node passed to this method
     * will be assumed to have passed through the schema validator.</p>
     *
     * @throws org.purnamaproject.xui.XUIDisplayException if the node is null.
     * @param node the new node representing this GUI component.
     */
    public XUIMenuBarImpl(XUINode node) throws XUIDisplayException
    {

        if(!(node.getName().equals("MenuBar")))
            throw new XUIDisplayException("Node for XUIMenuBar must be named 'MenuBar' and conform to the XUI schema.");
        else
        {
            // gui component
            menuBar = new JMenuBar();
            menuBar.setEnabled(Boolean.valueOf(node.getAttributeValue("enabled")).booleanValue());

            menuBarNode = node;
            menuBarNode.setXUIComponent(this);
        }
    }

    /**
     * Creates a menubar.
     */
    public XUIMenuBarImpl()
    {
        // gui component
        menuBar = new JMenuBar();

        // create the XML node representing this component.
        menuBarNode = new XUINodeImpl("MenuBar");
        menuBarNode.setLevel(2);
        menuBarNode.addNamespace("xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
        menuBarNode.setXUIComponent(this);

        // add specific attributes
        menuBarNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "id", "id", "xs:ID",
            IDFactory.getInstance().generateID("menubar"));

    }

    /**
     * Returns the XUI id reference of this component.
     *
     * @return the id reference of this component as a string.
     * @see #setIDRef(String)
     */
    public String getIDRef()
    {
        return menuBarNode.getAttributeValue("idref");
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
            menuBarNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "idref", "idref", "xs:IDREF",
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
        return menuBarNode.getAttributeID();
    }

    /**
     * Represents the XUI window component as a XUI node.
     *
     * @return org.purnamaproject.xui.XUINode the XML element as a XUI node.
     */
    public XUINode getNodeRepresentation()
    {
        return menuBarNode;
    }

    /**
     * Adds a new XUI menu to this menu bar. If the menu is null, an exception is thrown. This method should
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

        Container menuComponent = (Container)menuBar;
        menuComponent.add(newMenu.getPeer());
        if(newMenu.getIsPopupMenu())
        {
            String IDReference = newMenu.getNodeRepresentation().getAttributeValue("idref");
            XUINode frameNode = menuBarNode.getParent();
            List children = frameNode.getAllChildNodes();
            for(int i = 0; i < children.size(); i++)
            {
                XUINode child = (XUINode)children.get(i);

                // this check is required since some of the nodes do not have id attributes
                // (e.g. the GridLayout element)
                if(child.getAttributeID() != null)
                    if(child.getAttributeID().equals(IDReference))
                    {
                        XUIComponent component = child.getXUIComponent();
                        // this next check is required because the components underneath
                        // will not be created yet - only the nodes will exist. Therefor
                        // a null pointer must be checked for:
                        if(component != null)
                        {
                            Component peer = component.getPeer();

                            /*
                             * Each of these components needs to be checked for because they each have
                             * some very distinct ways of gathering their components and adding event
                             * handling to them.
                             */

                            // doing this so that a hypertext panel will display the popup
                            if(peer instanceof JWebBrowser)
                            {
                                Component pane = ((JWebBrowser)peer).getPane();
                                pane.addMouseListener(new PopupListener((JPopupMenu)newMenu.getPeer()));

                            } else if(peer instanceof JScrollTable)
                            {
                                JScrollTable tableScroller = (JScrollTable)peer;
                                JTable table = (JTable)tableScroller.getTable();
                                table.addMouseListener(new PopupListener((JPopupMenu)newMenu.getPeer()));

                            } else if(peer instanceof JScrollTree)
                            {
                                // get the scroll tree
                                JScrollTree treeScroller = (JScrollTree)peer;
                                XUITreeNode root = treeScroller.getRootNode();
                                JTree tree = treeScroller.getTree();
                                tree.addMouseListener(new PopupListener((JPopupMenu)newMenu.getPeer()));

                            } else if(peer instanceof JScrollPane)
                            {
                                // if inside of a scroll pane it will be assumed that all child components
                                // want to catch focus of the event capabilities and have the popupmenu
                                JScrollPane scroller = (JScrollPane)peer;
                                scroller.getViewport().getView().addMouseListener(
                                    new PopupListener((JPopupMenu)newMenu.getPeer()));

                            } else
                                if(peer != null)
                                    peer.addMouseListener(new PopupListener((JPopupMenu)newMenu.getPeer()));

                        }

                    }
            }

        }

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

        List children = menuBarNode.getAllChildNodes();
        List menuNodes = menuBarNode.getDirectChildren();
        int numberOfMenus = menuNodes.size();
        for(int i = 0; i < children.size(); i++)
        {
            XUINode child = (XUINode)children.get(i);
            if(child.getAttributeID().equals(newMenu.getNodeRepresentation().getAttributeID()))
                throw new XUITypeFormatException("No 2 menu components can have the same ID.");
        }
        Container menuComponent = (Container)menuBar;
        if(index > (numberOfMenus + 1))
            menuComponent.add(newMenu.getPeer(), (numberOfMenus + 1));
        else if(index < 0)
            index = 0;
        else
            menuComponent.add(newMenu.getPeer(), index);

        menuBarNode.addChildNode(newMenu.getNodeRepresentation());

        if(newMenu.getIsPopupMenu())
        {
            String IDReference = newMenu.getNodeRepresentation().getAttributeValue("idref");
            XUINode frameNode = menuBarNode.getParent();
            for(int i = 0; i < children.size(); i++)
            {
                XUINode child = (XUINode)children.get(i);
                if(child.getAttributeID() != null)
                    if(child.getAttributeID().equals(IDReference))
                    {
                        XUIComponent component = child.getXUIComponent();
                        // this next check is required because the components underneath
                        // will not be created yet - only the nodes will exist. Therefor
                        // a null pointer must be checked for:
                        if(component != null)
                        {
                            Component peer = component.getPeer();
                            // doing this so that a hypertext panel will display the popup
                            if(peer instanceof JWebBrowser)
                            {
                                Component pane = ((JWebBrowser)peer).getPane();
                                pane.addMouseListener(new PopupListener((JPopupMenu)newMenu.getPeer()));

                            } else if(peer instanceof JScrollTree)
                            {
                                JScrollTree treeScroller = (JScrollTree)peer;
                                JTree tree = (JTree)treeScroller.getTree();
                                tree.addMouseListener(new PopupListener((JPopupMenu)newMenu.getPeer()));

                            } else if(peer instanceof JScrollTable)
                            {
                                JScrollTable tableScroller = (JScrollTable)peer;
                                JTable table = (JTable)tableScroller.getTable();
                                table.addMouseListener(new PopupListener((JPopupMenu)newMenu.getPeer()));

                            } else if(peer instanceof JScrollPane)
                            {
                                // if inside of a scroll pane it will be assumed that all child components
                                // want to catch focus of the event capabilities and have the popupmenu
                                JScrollPane scroller = (JScrollPane)peer;
                                scroller.getViewport().getView().addMouseListener(
                                    new PopupListener((JPopupMenu)newMenu.getPeer()));

                            } else
                                if(peer != null)
                                    peer.addMouseListener(new PopupListener((JPopupMenu)newMenu.getPeer()));
                        }
                    }
            }

        }

    }

    /**
     * Adds a new XUI menu to this menu bar. If the menu is null, an exception is thrown. As well, if the
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

        List children = menuBarNode.getAllChildNodes();

        Container menuComponent = (Container)menuBar;
        menuComponent.add(newMenu.getPeer());
        menuBarNode.addChildNode(newMenu.getNodeRepresentation());

        if(newMenu.getIsPopupMenu())
        {
            String IDReference = newMenu.getNodeRepresentation().getAttributeValue("idref");
            XUINode frameNode = menuBarNode.getParent();
            for(int i = 0; i < children.size(); i++)
            {
                XUINode child = (XUINode)children.get(i);
                if(child.getAttributeID() != null)
                    if(child.getAttributeID().equals(IDReference))
                    {
                        XUIComponent component = child.getXUIComponent();
                        // this next check is required because the components underneath
                        // will not be created yet - only the nodes will exist. Therefor
                        // a null pointer must be checked for:
                        if(component != null)
                        {
                            Component peer = component.getPeer();
                            // doing this so that a hypertext panel will display the popup
                            if(peer instanceof JWebBrowser)
                            {
                                Component pane = ((JWebBrowser)peer).getPane();
                                pane.addMouseListener(new PopupListener((JPopupMenu)newMenu.getPeer()));

                            } else if(peer instanceof JScrollTree)
                            {
                                JScrollTree treeScroller = (JScrollTree)peer;
                                JTree tree = (JTree)treeScroller.getTree();
                                tree.addMouseListener(new PopupListener((JPopupMenu)newMenu.getPeer()));

                            } else if(peer instanceof JScrollTable)
                            {
                                JScrollTable tableScroller = (JScrollTable)peer;
                                JTable table = (JTable)tableScroller.getTable();
                                table.addMouseListener(new PopupListener((JPopupMenu)newMenu.getPeer()));

                            } else if(peer instanceof JScrollPane)
                            {
                                // if inside of a scroll pane it will be assumed that all child components
                                // want to catch focus of the event capabilities and have the popupmenu
                                JScrollPane scroller = (JScrollPane)peer;
                                scroller.getViewport().getView().addMouseListener(
                                    new PopupListener((JPopupMenu)newMenu.getPeer()));

                            } else
                                if(peer != null)
                                    peer.addMouseListener(new PopupListener((JPopupMenu)newMenu.getPeer()));
                        }
                    }
            }

        }

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
            List children = menuBarNode.getChildNodesByName("Menu");
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
     * This method returns the peer within the host operating system and user interface environment.
     * For each environment, this will return a different object type and may not be represented as
     * a class called component.
     *
     * @return Component which is the native representation of the GUI component.
     */
    public Component getPeer()
    {
        return (Component)menuBar;
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

        List children = menuBarNode.getChildNodesByName("Menu");
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
