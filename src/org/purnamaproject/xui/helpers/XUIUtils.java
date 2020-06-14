package org.purnamaproject.xui.helpers;

/**
 * @(#)XUIUtils.java    0.5 18/08/2003
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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import net.jmge.gif.Gif89Encoder;
import org.apache.commons.codec.binary.Base64;
import org.purnamaproject.xui.XUITypeFormatException;

/**
 * Utility class that contains many common constants required for some of the classes as well
 * as provides translation from image binary data to base 64 encoding.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public class XUIUtils
{
    /**
     * Dialog type question.
     */
    public final static int    QUESTION_TYPE = 0;

    /**
     * Dialog type question.
     */
    public final static int    INFORMATION_TYPE = 1;

    /**
     * Dialog type question.
     */
    public final static int    WARNING_TYPE = 2;

    /**
     * Dialog type question.
     */
    public final static int    ERROR_TYPE = 3;

    /**
     * Justify the text left.
     */
    public final static int LEFT_JUSTIFIED = 0;

    /**
     * Justify the text right.
     */
    public final static int RIGHT_JUSTIFIED = 1;

    /**
     * Justify the text center.
     */
    public final static int CENTER_JUSTIFIED = 2;

    /**
     * Horizontally display image and text (assuming there is both). This will place the
     * image and text side by side. Image is always first.
     */
    public final static int ORIENT_HORIZONTAL = 0;

    /**
     * Vertically display image and text (assuming there is both). This will place the
     * image overtop of the text.
     */
    public final static int ORIENT_VERTICAL = 1;

    /**
     * For setting the scroll panel's horizontal scroll bar.
     */
    public final static int HORIZONTAL_SCROLLING = 0;

    /**
     * For setting the scroll panel's vertical scroll bar.
     */
    public final static int VERTICAL_SCROLLING = 1;

    /**
     * For setting no scroll bars.
     */
    public final static int NO_SCROLLING = 2;

    /**
     * For setting both the vertical and the horizontal scroll bars.
     */
    public final static int BOTH_SCROLLING = 3;

    /**
     * Requests placement of a component be put in the left side or the top depending
     * on the layout set.
     */
    public final static int PLACEMENT_LEFT_OR_TOP = 0;

    /**
     * Requests placement of a component be put in the right side or the bottom depending
     * on the layout set.
     */
    public final static int PLACEMENT_RIGHT_OR_BOTTOM = 1;

    /**
     * Receives the Base 64 data (based on RFC 2045 Base64 as defined by RFC 2045, N. Freed
     * and N. Borenstein) and turns it into an image icon object in the Java application. The
     * image will be converted, provided the image is one of the following formats:
     * <ul>
     *   <li>GIF (extension gif)</li>
     *   <li>JPEG (extension jpg, jpeg)</li>
     *   <li>PNG (extension png)</li>
     * </ul>
     *
     * If the image is a format not listed above, the return value of this method is null.
     *
     * @param data the base 64 data as a string.
     * @param type the type of image (e.g. jpeg, jpg, gif, png).
     * @return an image icon that has been decoded based on the base 64 data.
     * @throws org.purnamaproject.xui.XUITypeFormatException if the format of the base64 is not correct.
     *
     */
    public static ImageIcon base64ToImage(String data, String type) throws XUITypeFormatException
    {
        if(type.equalsIgnoreCase("jpg") || type.equalsIgnoreCase("jpeg")
            || type.equalsIgnoreCase("png") || type.equalsIgnoreCase("gif"))
        {
            byte[] decodedData = Base64.decodeBase64(data.getBytes());
            ImageIcon image = new ImageIcon(decodedData);
            return image;
        } else
            return null;

    }

    /**
     * Encodes the Java ImageIcon object into base 64 data (based on RFC 2045 Base64 as defined
     * by RFC 2045, N. Freed and N. Borenstein) and returns a byte array. The image will be
     * converted, provided the image is one of the following formats:
     * <ul>
     *   <li>GIF (extension gif)</li>
     *   <li>JPEG (extension jpg, jpeg)</li>
     *   <li>PNG (extension png)</li>
     * </ul>
     *
     * If the image is a format not listed above, the return value of this method is null.
     *
     * @param i the image icon object
     * @param imageName the full name of the image (including the extension)
     * @return the byte array of base 64 data. Null if image format not recognized.
     */
    public static byte[] imageToBase64(ImageIcon i, String imageName) throws XUITypeFormatException,
        IOException
    {
        String name = imageName;
        String type = "";
        StringTokenizer st = new StringTokenizer(name, ".");
        // do this in case there are more than 1 "." in a file name
        while (st.hasMoreTokens())
        {
            type = st.nextToken();
        }
        // check for null
        if(type == null)
            type = "";
        // use the support found in the standard JDK
        if(type.equalsIgnoreCase("jpg") || type.equalsIgnoreCase("jpeg") || type.equalsIgnoreCase("png"))
        {
            BufferedImage bi = new BufferedImage(i.getIconWidth(), i.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bi.createGraphics();
            g2.drawImage(i.getImage(), 0, 0, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, type, baos);
            String stringData = new String(Base64.encodeBase64(baos.toByteArray()));
            byte[] base64Data = stringData.getBytes();
            return base64Data;
        } else if(type.equalsIgnoreCase("gif"))
        {
            // use gif encoding library for GIF
            Gif89Encoder gif = new Gif89Encoder(i.getImage());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            gif.encode(baos);
            String stringData = new String(Base64.encodeBase64(baos.toByteArray()));
            byte[] base64Data = stringData.getBytes();
            return base64Data;
        } else
            return null;
    }

    /**
     * Encodes the Java ImageIcon object into base 64 data (based on RFC 2045 Base64 as defined
     * by RFC 2045, N. Freed and N. Borenstein) and returns a byte array. The image will be
     * converted, provided the image is one of the following formats:
     * <ul>
     *   <li>GIF (extension gif)</li>
     *   <li>JPEG (extension jpg, jpeg)</li>
     *   <li>PNG (extension png)</li>
     * </ul>
     *
     * If the image is a format not listed above, the return value of this method is null.
     *
     * @param i the image icon object
     * @return the byte array of base 64 data. Null if image format not recognized.
     */
    public static byte[] imageToBase64(ImageIcon i) throws XUITypeFormatException,
        IOException
    {
        String name = i.getDescription();
        String type = "";
        StringTokenizer st = new StringTokenizer(name, ".");
        // do this in case there are more than 1 "." in a file name
        while (st.hasMoreTokens())
        {
            type = st.nextToken();
        }
        // check for null
        if(type == null)
            type = "";
        // use the support found in the standard JDK
        if(type.equalsIgnoreCase("jpg") || type.equalsIgnoreCase("jpeg") || type.equalsIgnoreCase("png"))
        {
            BufferedImage bi = new BufferedImage(i.getIconWidth(), i.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bi.createGraphics();
            g2.drawImage(i.getImage(), 0, 0, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, type, baos);
            String stringData = new String(Base64.encodeBase64(baos.toByteArray()));
            byte[] base64Data = stringData.getBytes();
            return base64Data;
        } else if(type.equalsIgnoreCase("gif"))
        {
            // use gif encoding library for GIF
            Gif89Encoder gif = new Gif89Encoder(i.getImage());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            gif.encode(baos);
            String stringData = new String(Base64.encodeBase64(baos.toByteArray()));
            byte[] base64Data = stringData.getBytes();
            return base64Data;
        } else
            return null;
    }

}
