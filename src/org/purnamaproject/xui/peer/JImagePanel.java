package org.purnamaproject.xui.peer;

/**
 * @(#)XUINode.java    0.5 18/08/2003
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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * Image panel representing an image within the XUI framework. This class does not
 * need to be directly used. It is a container for ImageIcon objects.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public class JImagePanel extends JPanel
{
    /**
     * The image inside of this object.
     */
    private Image image;

    /**
     * The Icon object of the image. Used for sending to the container.
     */
    private ImageIcon i;

    /**
     * The width of the image.
     */
    private int width;

    /**
     * The height of the image.
     */
    private int height;

    /**
     * Default.
     */
    public JImagePanel()
    {
        super();
        this.setEnabled(true);
        this.setVisible(true);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
    }

    /**
     * Give it an image icon to display inside of it.
     */
    public JImagePanel(ImageIcon icon)
    {
        this();
        i = icon;
        image = icon.getImage();
        width = icon.getIconWidth();
        height = icon.getIconHeight();
        this.setSize(width, height);
        this.setOpaque(true);

    }

    /**
     * Returns the image icon of this panel.
     *
     * @return an image icon.
     */
    public ImageIcon getImage()
    {
        return i;
    }

    /**
     * Sets a new image for this component. Looks after the dimensions.
     */
    public void setImage(ImageIcon icon)
    {
        i = icon;
        image = icon.getImage();
        width = icon.getIconWidth();
        height = icon.getIconHeight();
        this.setSize(width, height);
        this.setOpaque(true);


    }

    /**
     * Paint the image inside of this component.
     */
    public void paintComponent(Graphics g)
    {
        int xPosition = 0;
        int yPosition = 0;
        if(image.getWidth(this) < this.getWidth())
            xPosition = (this.getWidth() - image.getWidth(this))/2;
        if(image.getHeight(this) < this.getHeight())
            yPosition = (this.getHeight() - image.getHeight(this))/2;

        g.drawImage(image, xPosition, yPosition, null);
    }
}
