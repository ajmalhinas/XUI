package org.purnamaproject.xui.impl;

/**
 * @(#)XUIMenuItemImpl.java 0.5 18/08/2003
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import org.purnamaproject.xui.helpers.IDFactory;
import org.purnamaproject.xui.binding.XUIModel;
import org.purnamaproject.xui.binding.ActionModel;
import org.purnamaproject.xui.component.menu.XUIMenuItem;
import org.purnamaproject.xui.component.atomic.XUIImage;
import org.purnamaproject.xui.component.atomic.XUIRadioButton;
import org.purnamaproject.xui.impl.XUIImageImpl;
import org.purnamaproject.xui.impl.XUINodeImpl;
import org.purnamaproject.xui.XUIDisplayException;
import org.purnamaproject.xui.XUINode;
import org.purnamaproject.xui.XUITypeFormatException;
import org.purnamaproject.xui.peer.JImagePanel;

/**
 * The menu item represents a GUI menu item that sits on top of a Menu. A menu item can
 * have sub menus hanging off of it. Menu items can also have check marks and images. Menu
 * items can be disabled.
 *
 * @version 0.5 18/08/2003
 * @author  Arron Ferguson
 */
public class XUIMenuItemImpl implements XUIMenuItem, ActionListener
{

    /**
     * The menu representation of this component.
     */
    private JMenuItem menuItem;

    /**
     * The node representation of this component.
     */
    private XUINode menuItemNode;

    /**
     * The list of model objects that are ready to be listeners to any events generated by this
     * component.
     */
    private List actionModelList = new java.util.LinkedList();

    /**
     * <p>Constructs a new XUIMenuItem implementation and passing it its XML node rather than requiring
     * the XUIMenuItem implementation to build it from scratch. This method is called by the Realizer
     * when it reads the XML document from the file system.</p>
     *
     * <p><b><u>Note:</u></b> This constructor is not normally something that should be called up.
     * This constructor is required when creating a XUI Menu Item where the node has already been built
     * by the realizer or a subclass thereof.</p>
     *
     * <p><b><u>Note:</u></b> Schema checking is assumed at this point. Any node passed to this method
     * will be assumed to have passed through the schema validator.</p>
     *
     * @throws org.purnamaproject.xui.XUIDisplayException if the node is null.
     * @param node the new node representing this GUI component.
     */
    public XUIMenuItemImpl(XUINode node) throws XUIDisplayException
    {

        if(!(node.getName().equals("MenuItem")))
            throw new XUIDisplayException("Node for XUIMenuItem must be named 'MenuItem' and conform to the XUI schema.");
        else
        {
            // gui component
            if(node.getAttributeValue("checked").equals("true"))
            {
                JCheckBoxMenuItem m = new JCheckBoxMenuItem();
                m.setEnabled(Boolean.valueOf(node.getAttributeValue("enabled")).booleanValue());
                m.setText(node.getAttributeValue("label"));
                menuItem = m;

            } else
            {
                JMenuItem m = new JMenuItem();
                m.setEnabled(Boolean.valueOf(node.getAttributeValue("enabled")).booleanValue());
                m.setText(node.getAttributeValue("label"));
                menuItem = (JMenuItem)m;
            }
            menuItemNode = node;
            // get image
            List images = node.getChildNodesByName("Image");
            // default image
            if(images.size() == 1)
            {
                XUINode imageNode = (XUINode)images.get(0);
                XUIImage xuiimage = new XUIImageImpl(imageNode);
                addImage(xuiimage);
            }

            // get shortcut commands
            List shortCut = node.getChildNodesByName("Shortcut");
            if(shortCut != null)
            {
                if(shortCut.size() > 0)
                {
                    XUINode s = (XUINode)shortCut.get(0);
                    if(s.getAttributeValue("keyModifier2") == null)
                        setShortCut(s.getAttributeValue("keyCode"), s.getAttributeValue("keyModifier1"));
                    else
                        setShortCut(s.getAttributeValue("keyCode"), s.getAttributeValue("keyModifier1"),
                            s.getAttributeValue("keyModifier2"));
                }
            }
            menuItemNode.setXUIComponent(this);
        }
    }

    /**
     * Creates a menu item with the default textual label 'Menu'.
     */
    public XUIMenuItemImpl()
    {
        // gui component
        menuItem = new JMenuItem("MenuItem");

        // create the XML node representing this component.
        menuItemNode = new XUINodeImpl("MenuItem");
        menuItemNode.setLevel(4);
        menuItemNode.addNamespace("xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
        menuItemNode.setXUIComponent(this);

        // add specific attributes
        menuItemNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "checked", "checked",
            "xs:boolean", "false");
        menuItemNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "id", "id", "xs:ID",
            IDFactory.getInstance().generateID("menuItem"));
        menuItemNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "label", "label", "xs:token",
            "");
        menuItemNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "enabled", "enabled", "xs:boolean",
            "true");

    }

    /**
     * Creates a menu item with passed textual label added to it.
     *
     * @param newLabel the label to add to the new menu
     * @throws org.purnamaproject.xui.XUIDisplayException if the component is being placed overtop of an existing component on the
     * layout.
     */
    public XUIMenuItemImpl(String newLabel)
    {
        this();
        if(newLabel == null || newLabel.matches("^\n") || newLabel.matches("^\t"))
            throw new XUIDisplayException("label for menu cannot be null and must match xs:token.");
        else
        {
            menuItemNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "label", "label", "xs:token",
                newLabel);
            menuItem = new JMenuItem(newLabel);

        }
    }

    /**
     * Creates a menu item with an image.
     *
     * @throws org.purnamaproject.xui.XUIDisplayException if the image URL is null.
     * @param image the image to add to this component. If this is null an exception is generated.
     */
    public XUIMenuItemImpl(XUIImage image)
    {
        this();
        // also add an image if one exists
        if(image != null)
        {
            addImage(image);

        } else
            throw new XUIDisplayException("Image URL must not be null.");
    }

    /**
     * Creates a menu item with passed textual label added to it.
     *
     * @param newLabel the label to add to the new menu
     * @param hasCheckBox determines if this menu item will have a checkbox on it or not
     * @throws org.purnamaproject.xui.XUIDisplayException if the component is being placed overtop of an existing component on the
     * layout.
     */
    public XUIMenuItemImpl(String newLabel, boolean hasCheckBox)
    {
        this(newLabel);
        if(newLabel == null || newLabel.matches("^\n") || newLabel.matches("^\t"))
            throw new XUIDisplayException("label for menu cannot be null and must match xs:token.");
        else
        {
            if(hasCheckBox)
            {
                JCheckBoxMenuItem cbmi = new JCheckBoxMenuItem(newLabel);
                menuItemNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "checked", "checked",
                    "xs:boolean", "true");

            } else
                ; // else this is already built for us from the previous constructor so don't do anything
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
        menuItem.setEnabled(enabled);
        menuItemNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "enabled", "enabled", "xs:boolean",
            "" + menuItem.isEnabled());
    }

    /**
     * Returns the label of this component.
     *
     * @return the label of this component as a string.
     * @see #setLabel(String)
     */
    public String getLabel()
    {
        return menuItem.getText();
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
            throw new XUITypeFormatException("Label for XUIMenuItem cannot be null.");
        else
        {
            menuItem.setText(newLabel);
            menuItemNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "label", "label", "xs:string",
                menuItem.getText());
        }
    }

    /**
     * Returns the whether or not this menu item has a check mark on it (similar to a check box.
     *
     * @return true if checked, false otherwise.
     * @see #setIsChecked(boolean)
     */
    public boolean getIsChecked()
    {
        if(menuItem instanceof JCheckBoxMenuItem)
            return (((JCheckBoxMenuItem) menuItem).getState());  // <-- :-o
        return false;
    }

    /**
     * Sets whether or not this menu item is checked. If true, then this menu item is checked. Otherwise, false.
     * The Java boolean type is mapped to the XML Schema boolean type.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#boolean">W3C XML Schema, section 3.2.2</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param isChecked if true then this menu item is checked. False means it is not.
     */
    public void setIsChecked(boolean isChecked)
    {
        if(menuItem instanceof JCheckBoxMenuItem)
        {
            ((JCheckBoxMenuItem)menuItem).setState(isChecked);
            menuItemNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "checked", "checked", "xs:boolean",
                "" + isChecked);
        }
    }

    /**
     * Adds a new image to this button. The XUIButton class will handle the encoding and decoding
     * of binary format data to Base64 data that will be later stored in the XML file.
     *
     * @param newImage the new image icon (which contains the binary image data).
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#base64Binary">W3C XML Schema, section 3.2.16</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema ID
     * type, or if the image is null, an exception occurs.
     */
    public void addImage(XUIImage newImage) throws XUITypeFormatException
    {
        if(newImage != null)
        {
            // get the image data from the image and place it into the button.
            ImageIcon image = ((JImagePanel)newImage.getPeer()).getImage();

            // first attempt to remove the old default image node ...
            menuItemNode.removeChildNodeOnLooseIDMatch("*");

            XUINode i = newImage.getNodeRepresentation();

            menuItemNode.addChildNode(i);
            menuItem.setIcon(image);
        } else
            throw new XUIDisplayException("XUIImage cannot be null.");

    }

    /**
     * Removes the image that is situated in this component. If no image exists, no action is taken.
     *
     * @see #addImage(XUIImage)
     */
    public void removeImage()
    {
        menuItemNode.removeChildNodeOnLooseIDMatch("default*");
        // ugly :/
        menuItem.setIcon(null);

    }

    /**
     * <p>Sets the keyboard shortcut to this particular menu component. This allows for advanced users to
     * use the keyboard to quickly call up menu commands rather than use the mouse pointer to select each
     * menu component. This overload allows two modifiers (e.g. "B CONTROL ALT")</p>
     *
     * <p>The key value can be any upper case letter from A through Z, numbers are 0 through 9, punctuation
     * and other symbols found on a PC, Macintosh or Unix computer keyboard. For the modifier, the string
     * value must be either "CTRL", "ALT" or "SHIFT". For details on which keys and their respective string
     * literal representations, use the XUI schema as well as the XUI API Guide.</p>
     *
     * <p><b>Note: if the XUIMenu is a popup menu, the shortcut has not effect on it.</b></p>
     *
     * @param keyCode the key (0 - 9, A - Z) from the keyboard.
     * @param modifier1 the first modifier key (CTRL, ALT, SHIFT)
     * @param modifier2 the second modifier key (CTRL, ALT, SHIFT)
     * @throws org.purnamaproject.xui.XUITypeFormatException if the keycode and the modifier do not match the literals found
     * in the XUI Schema or are null.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @see <a href="http://xml.bcit.ca/PurnamaProject/html/xuiapiguide.pdf">Purnama Project XUI API Guide</a>
     */
    public void setShortCut(String keyCode, String modifier1, String modifier2) throws XUITypeFormatException
    {
        if(keyCode == null || modifier1 == null || modifier2 == null)
            throw new XUITypeFormatException("keyCode and modifier must not be null.");
        else if(!(modifier1.equals("CTRL")) && !(modifier1.equals("ALT")) && !(modifier1.equals("SHIFT")))
            throw new XUITypeFormatException("modifier must be a value found in the XUI Schema.");
        else if(!(modifier2.equals("CTRL")) && !(modifier2.equals("ALT")) && !(modifier2.equals("SHIFT")))
            throw new XUITypeFormatException("modifier must be a value found in the XUI Schema.");
        else if(!(keyCode.equals("A")) && !(keyCode.equals("B")) && !(keyCode.equals("C")) &&
            !(keyCode.equals("D")) && !(keyCode.equals("E")) && !(keyCode.equals("F")) &&
            !(keyCode.equals("G")) && !(keyCode.equals("H")) && !(keyCode.equals("I")) &&
            !(keyCode.equals("J")) && !(keyCode.equals("K")) && !(keyCode.equals("L")) &&
            !(keyCode.equals("M")) && !(keyCode.equals("N")) && !(keyCode.equals("O")) &&
            !(keyCode.equals("P")) && !(keyCode.equals("Q")) && !(keyCode.equals("R")) &&
            !(keyCode.equals("S")) && !(keyCode.equals("T")) && !(keyCode.equals("U")) &&
            !(keyCode.equals("V")) && !(keyCode.equals("W")) && !(keyCode.equals("X")) &&
            !(keyCode.equals("Y")) && !(keyCode.equals("Z")) && !(keyCode.equals("0")) &&
            !(keyCode.equals("1")) && !(keyCode.equals("2")) && !(keyCode.equals("3")) &&
            !(keyCode.equals("4")) && !(keyCode.equals("5")) && !(keyCode.equals("6")) &&
            !(keyCode.equals("7")) && !(keyCode.equals("8")) && !(keyCode.equals("9")) &&
            !(keyCode.equals("ADD")) && !(keyCode.equals("ALT")) && !(keyCode.equals("AMPERSAND")) &&
            !(keyCode.equals("BACK_SLASH")) && !(keyCode.equals("BACK_SPACE")) && !(keyCode.equals("BRACE_LEFT")) &&
            !(keyCode.equals("BRACE_RIGHT")) && !(keyCode.equals("CAPS_LOCK")) && !(keyCode.equals("CLEAR")) &&
            !(keyCode.equals("CLOSE_BRACKET")) && !(keyCode.equals("COMMA")) && !(keyCode.equals("CONTROL")) &&
            !(keyCode.equals("DELETE")) && !(keyCode.equals("DIVIDE")) && !(keyCode.equals("DOLLAR")) &&
            !(keyCode.equals("DOWN")) && !(keyCode.equals("END")) && !(keyCode.equals("ENTER")) &&
            !(keyCode.equals("EQUALS")) && !(keyCode.equals("ESCAPE")) && !(keyCode.equals("EXCLAMATION_MARK")) &&
            !(keyCode.equals("F1")) && !(keyCode.equals("F2")) && !(keyCode.equals("F3")) &&
            !(keyCode.equals("F4")) && !(keyCode.equals("F5")) && !(keyCode.equals("F6")) &&
            !(keyCode.equals("F7")) && !(keyCode.equals("F8")) && !(keyCode.equals("F9")) &&
            !(keyCode.equals("F10")) && !(keyCode.equals("F11")) && !(keyCode.equals("F12")) &&
            !(keyCode.equals("GREATER")) && !(keyCode.equals("HOME")) && !(keyCode.equals("INSERT")) &&
            !(keyCode.equals("LEFT")) && !(keyCode.equals("LESS")) && !(keyCode.equals("MINUS")) &&
            !(keyCode.equals("MULTIPLY")) && !(keyCode.equals("NUM_LOCK")) && !(keyCode.equals("NUMBER_SIGN")) &&
            !(keyCode.equals("NUMPAD0")) && !(keyCode.equals("NUMPAD1")) && !(keyCode.equals("NUMPAD2")) &&
            !(keyCode.equals("NUMPAD3")) && !(keyCode.equals("NUMPAD4")) && !(keyCode.equals("NUMPAD5")) &&
            !(keyCode.equals("NUMPAD6")) && !(keyCode.equals("NUMPAD7")) && !(keyCode.equals("NUMPAD8")) &&
            !(keyCode.equals("NUMPAD9")) && !(keyCode.equals("OPEN_BRACKET")) && !(keyCode.equals("PAGE_DOWN")) &&
            !(keyCode.equals("PAGE_UP")) && !(keyCode.equals("PAUSE")) && !(keyCode.equals("PERIOD")) &&
            !(keyCode.equals("PLUS")) && !(keyCode.equals("PRINTSCREEN")) && !(keyCode.equals("QUOTE")) &&
            !(keyCode.equals("QUOTEDBL")) && !(keyCode.equals("RIGHT")) && !(keyCode.equals("SCROLL_LOCK")) &&
            !(keyCode.equals("SEMICOLON")) && !(keyCode.equals("")) && !(keyCode.equals("")) &&
            !(keyCode.equals("SPACE")) && !(keyCode.equals("SHIFT")) && !(keyCode.equals("SLASH")) &&
            !(keyCode.equals("SUBTRACT")) && !(keyCode.equals("TAB")) && !(keyCode.equals("UNDEFINED")) &&
            !(keyCode.equals("UNDERSCORE")))
                throw new XUITypeFormatException("keycode must be a value found in the XUI Schema.");
        else
            menuItem.setAccelerator(KeyStroke.getKeyStroke(modifier1.toLowerCase() + " "
                + modifier2.toLowerCase() + " " + keyCode));
            XUINode shortCutNode = new XUINodeImpl("Shortcut");
            shortCutNode.setLevel(menuItemNode.getLevel() + 1);
            shortCutNode.addNamespace("xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
            shortCutNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "keyCode", "keyCode", "xs:token",
                keyCode);
            shortCutNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "keyModifier1", "keyModifier1",
                "xs:token", modifier1);
            shortCutNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "keyModifier2", "keyModifier2",
                "xs:token", modifier2);
            shortCutNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "id", "id", "xs:ID",
            IDFactory.getInstance().generateID("shortcut"));
            menuItemNode.addChildNode(shortCutNode);
    }

    /**
     * <p>Sets the keyboard shortcut to this particular menu component. This allows for advanced users to
     * use the keyboard to quickly call up menu commands rather than use the mouse pointer to select each
     * menu component.</p>
     *
     * <p>The key value can be any upper case letter from A through Z, numbers are 0 through 9, punctuation
     * and other symbols found on a PC, Macintosh or Unix computer keyboard. For the modifier, the string
     * value must be either "CTRL", "ALT" or "SHIFT". For details on which keys and their respective string
     * literal representations, use the XUI schema as well as the XUI API Guide.</p>
     *
     * <p><b>Note: if the XUIMenu is a popup menu, the shortcut has not effect on it.</b></p>
     *
     * @param keyCode the key (0 - 9, A - Z) from the keyboard.
     * @param modifier the modifier key (CTRL, ALT, SHIFT)
     * @throws org.purnamaproject.xui.XUITypeFormatException if the keycode and the modifier do not match the literals found
     * in the XUI Schema or are null.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @see #setShortCut(String, String, String)
     */
    public void setShortCut(String keyCode, String modifier) throws XUITypeFormatException
    {
        if(keyCode == null || modifier == null)
            throw new XUITypeFormatException("keyCode and modifier must not be null.");
        else if(!(modifier.equals("CTRL")) && !(modifier.equals("ALT")) && !(modifier.equals("SHIFT")))
            throw new XUITypeFormatException("modifier must be a value found in the XUI Schema.");
        else if(!(keyCode.equals("A")) && !(keyCode.equals("B")) && !(keyCode.equals("C")) &&
            !(keyCode.equals("D")) && !(keyCode.equals("E")) && !(keyCode.equals("F")) &&
            !(keyCode.equals("G")) && !(keyCode.equals("H")) && !(keyCode.equals("I")) &&
            !(keyCode.equals("J")) && !(keyCode.equals("K")) && !(keyCode.equals("L")) &&
            !(keyCode.equals("M")) && !(keyCode.equals("N")) && !(keyCode.equals("O")) &&
            !(keyCode.equals("P")) && !(keyCode.equals("Q")) && !(keyCode.equals("R")) &&
            !(keyCode.equals("S")) && !(keyCode.equals("T")) && !(keyCode.equals("U")) &&
            !(keyCode.equals("V")) && !(keyCode.equals("W")) && !(keyCode.equals("X")) &&
            !(keyCode.equals("Y")) && !(keyCode.equals("Z")) && !(keyCode.equals("0")) &&
            !(keyCode.equals("1")) && !(keyCode.equals("2")) && !(keyCode.equals("3")) &&
            !(keyCode.equals("4")) && !(keyCode.equals("5")) && !(keyCode.equals("6")) &&
            !(keyCode.equals("7")) && !(keyCode.equals("8")) && !(keyCode.equals("9")) &&
            !(keyCode.equals("ADD")) && !(keyCode.equals("ALT")) && !(keyCode.equals("AMPERSAND")) &&
            !(keyCode.equals("BACK_SLASH")) && !(keyCode.equals("BACK_SPACE")) && !(keyCode.equals("BRACE_LEFT")) &&
            !(keyCode.equals("BRACE_RIGHT")) && !(keyCode.equals("CAPS_LOCK")) && !(keyCode.equals("CLEAR")) &&
            !(keyCode.equals("CLOSE_BRACKET")) && !(keyCode.equals("COMMA")) && !(keyCode.equals("CONTROL")) &&
            !(keyCode.equals("DELETE")) && !(keyCode.equals("DIVIDE")) && !(keyCode.equals("DOLLAR")) &&
            !(keyCode.equals("DOWN")) && !(keyCode.equals("END")) && !(keyCode.equals("ENTER")) &&
            !(keyCode.equals("EQUALS")) && !(keyCode.equals("ESCAPE")) && !(keyCode.equals("EXCLAMATION_MARK")) &&
            !(keyCode.equals("F1")) && !(keyCode.equals("F2")) && !(keyCode.equals("F3")) &&
            !(keyCode.equals("F4")) && !(keyCode.equals("F5")) && !(keyCode.equals("F6")) &&
            !(keyCode.equals("F7")) && !(keyCode.equals("F8")) && !(keyCode.equals("F9")) &&
            !(keyCode.equals("F10")) && !(keyCode.equals("F11")) && !(keyCode.equals("F12")) &&
            !(keyCode.equals("GREATER")) && !(keyCode.equals("HOME")) && !(keyCode.equals("INSERT")) &&
            !(keyCode.equals("LEFT")) && !(keyCode.equals("LESS")) && !(keyCode.equals("MINUS")) &&
            !(keyCode.equals("MULTIPLY")) && !(keyCode.equals("NUM_LOCK")) && !(keyCode.equals("NUMBER_SIGN")) &&
            !(keyCode.equals("NUMPAD0")) && !(keyCode.equals("NUMPAD1")) && !(keyCode.equals("NUMPAD2")) &&
            !(keyCode.equals("NUMPAD3")) && !(keyCode.equals("NUMPAD4")) && !(keyCode.equals("NUMPAD5")) &&
            !(keyCode.equals("NUMPAD6")) && !(keyCode.equals("NUMPAD7")) && !(keyCode.equals("NUMPAD8")) &&
            !(keyCode.equals("NUMPAD9")) && !(keyCode.equals("OPEN_BRACKET")) && !(keyCode.equals("PAGE_DOWN")) &&
            !(keyCode.equals("PAGE_UP")) && !(keyCode.equals("PAUSE")) && !(keyCode.equals("PERIOD")) &&
            !(keyCode.equals("PLUS")) && !(keyCode.equals("PRINTSCREEN")) && !(keyCode.equals("QUOTE")) &&
            !(keyCode.equals("QUOTEDBL")) && !(keyCode.equals("RIGHT")) && !(keyCode.equals("SCROLL_LOCK")) &&
            !(keyCode.equals("SEMICOLON")) && !(keyCode.equals("")) && !(keyCode.equals("")) &&
            !(keyCode.equals("SPACE")) && !(keyCode.equals("SHIFT")) && !(keyCode.equals("SLASH")) &&
            !(keyCode.equals("SUBTRACT")) && !(keyCode.equals("TAB")) && !(keyCode.equals("UNDEFINED")) &&
            !(keyCode.equals("UNDERSCORE")))
                throw new XUITypeFormatException("keycode must be a value found in the XUI Schema.");
        else
        {
            menuItem.setAccelerator(KeyStroke.getKeyStroke(modifier.toLowerCase() + " " + keyCode));
            XUINode shortCutNode = new XUINodeImpl("Shortcut");

            shortCutNode.setLevel(menuItemNode.getLevel() + 1);
            shortCutNode.addNamespace("xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
            shortCutNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "keyCode", "keyCode", "xs:token",
                keyCode);
            shortCutNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "keyModifier1", "keyModifier1",
                "xs:token", modifier);
            shortCutNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "id", "id", "xs:ID",
            IDFactory.getInstance().generateID("shortcut"));
            menuItemNode.addChildNode(shortCutNode);
        }
    }

    /**
     * Returns the shortcut from this menu component. If no shortcut exists, then nothing is returned.
     *
     * The returned string contains the keycode and any modifiers that are with it.
     */
    public String getShortcut()
    {
        String shortcut = "";
        List shortcuts = menuItemNode.getChildNodesByName("Shortcut");
        if(shortcuts != null)
            if(shortcuts.size() > 0)
            {
                XUINode node = (XUINode)shortcuts.get(0);
                String key = node.getAttributeValue("keyCode");
                String mod1 = node.getAttributeValue("keyModifier1");
                String mod2 = node.getAttributeValue("keyModifier2");
                if(key == null)
                    return "";
                else if(mod2 == null)
                {
                    shortcut = (mod1.toUpperCase() + " " + key);
                } else
                {
                    shortcut = (mod1.toUpperCase() + " " + mod2.toUpperCase() + " " + key);
                }
            }
        return shortcut;
    }

    /**
     * Returns the XUI id reference of this component.
     *
     * @return the id reference of this component as a string.
     * @see #setIDRef(String)
     */
    public String getIDRef()
    {
        return menuItemNode.getAttributeValue("idref");
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
            menuItemNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "idref", "idref", "xs:IDREF",
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
        return menuItemNode.getAttributeID();
    }

    /**
     * Represents the XUI window component as a XUI node.
     *
     * @return org.purnamaproject.xui.XUINode the XML element as a XUI node.
     */
    public XUINode getNodeRepresentation()
    {
        return menuItemNode;
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
        return (Component)menuItem;
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
     * Handles an action event from the underlying GUI peer.
     *
     * @param e the <code>ActionEvent</code>.
     */
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == menuItem)
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
            menuItem.addActionListener(this);
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
        menuItem.removeActionListener(this);
    }


}
