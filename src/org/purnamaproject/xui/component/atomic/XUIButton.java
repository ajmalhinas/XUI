package org.purnamaproject.xui.component.atomic;

/**
 * @(#)XUIButton.java   0.5 18/08/2003
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

import javax.swing.ImageIcon;
import org.purnamaproject.xui.XUITypeFormatException;
import org.purnamaproject.xui.binding.XUIEventSource;
import org.purnamaproject.xui.component.atomic.XUIImage;
import org.purnamaproject.xui.XUIDisplayException;

/**
 * This atomic component represents a simple pushbutton. Any listeners registered with
 * this component will be informed when the user presses this button.
 *
 * @version 0.5 18/08/2003
 * @author  Arron Ferguson
 */
public interface XUIButton extends XUIAtomic, XUIEventSource
{
    /**
     * Returns the label of this component.
     *
     * @return the label of this component as a string.
     * @see #setLabel(String)
     */
    public String getLabel();

    /**
     * Sets the label of this component. The string values allowed can contain
     * both alphabetic and numeric characters. Real-time validation is performed and
     * therefore if a wrong value is entered, an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#string">W3C XML Schema, section 3.2.1</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUIDisplayException if the type does not conform to the W3C XML Schema string
     * type, or if the string is null, an exception occurs.
     * @param newLabel the new label to assign to this component.
     */
    public void setLabel(String newLabel) throws XUIDisplayException;

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
    public void setOrientation(int orientation);

    /**
     * <p>Adds a new image to this button. The XUIButton class will handle the encoding and decoding
     * of binary format data to Base64 data that will be later stored in the XML file.</p>
     *
     * <p>This image is the default image that will be shown in the button when no interaction
     * is taking place.</p>
     *
     * @param newImage the new image icon (which contains the binary image data). This is the default
     * image for this component.
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#base64Binary">W3C XML Schema, section 3.2.16</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema ID
     * type, or if the image is null, an exception occurs.
     */
    public void addDefaultImage(XUIImage newImage) throws XUITypeFormatException;

    /**
     * Removes the default image that is situated in this component. If no image exists, no action is taken.
     *
     * @see #addDefaultImage(XUIImage)
     */
    public void removeDefaultImage();

    /**
     * <p>Adds a all the images to this button. The XUIButton class will handle the encoding and decoding
     * of binary format data to Base64 data that will be later stored in the XML file.</p>
     *
     * <p>The three images for this component are:</p>
     * <ul>
     *   <li>Default image - the image that is used as a default image</li>
     *   <li>Rollover image - the image that is used when the mouse cursor rolls over the button</li>
     *   <li>Pressed image - the image that is shown when this component is selected</li>
     * </ul>
     *
     * @param defaultImage the new image icon (which contains the binary image data).
     * @param rolloverImage the image displayed during a mouse "roll over" of the component.
     * @param pressedImage the image displayed when the component is selected (i.e. pressed).
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#base64Binary">W3C XML Schema, section 3.2.16</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUIDisplayException if the type does not conform to the W3C XML Schema ID
     * type, or if the image is null, an exception occurs.
     */
    public void addImages(XUIImage defaultImage, XUIImage rolloverImage, XUIImage pressedImage)
        throws XUIDisplayException;

    /**
     * Removes any or all of the images that are placed inside this component. If no images exists no action
     * is taken.
     *
     * @see #addDefaultImage(XUIImage)
     */
    public void removeImages();

}
