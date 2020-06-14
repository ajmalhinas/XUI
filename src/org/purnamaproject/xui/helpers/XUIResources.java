package org.purnamaproject.xui.helpers;

/**
 * @(#)BindingFactory.java    0.1 18/08/2003
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.StringTokenizer;
import javax.swing.ImageIcon;
import org.purnamaproject.xui.component.atomic.XUIImage;
import org.purnamaproject.xui.impl.XUIImageImpl;
import org.purnamaproject.xui.impl.XUINodeImpl;
import org.purnamaproject.xui.XUINode;
import org.purnamaproject.xui.XUIDisplayException;
import org.purnamaproject.xui.peer.JImagePanel;

/**
 * <p>Responsible for maintaining a collection of images that are loaded from a jar.
 * This adds convenience for the end user in not having to make calls such as:</p>
 *
 * <code>XUIImage ant = XUIComponentFactory.makeImage(AnimalModel.class.getResource("antcol-1.thm.jpeg"), 3, 3, 2, 1);</code>
 *
 * <p>Which can cause classloader problems anyways. In the above, we would assume that within the jar file
 * being loaded, that there was an image called 'antcol-1.thm.jpeg' found inside it. Instead, make the
 * following call:</p>
 *
 * <code>XUIImage ant = XUIResources.getInstance().getImage("antcol-1.thm.jpeg", 3, 3, 2, 1);</code>
 *
 * <p><b>Note: If you are simply referring to images or other resources found outside of the
 * jar, you may use URL objects as usual. This class only applies to jar files with resources
 * in them.</b></p>
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public class XUIResources
{
    /**
     * table of resources.
     */
    private static Hashtable resourceList;

    /**
     * The instance of self.
     */
    private static XUIResources resources = null;

    /**
     * Default constructor not viewed nor should it be called.
     */
    private XUIResources()
    {
        resourceList = new Hashtable();
    }

    /**
     * Return an instance of the XUIResources resources class.
     *
     * @return XUIResources
     */
    public static XUIResources getInstance()
    {
        if(resources == null)
            resources = new XUIResources();
        return resources;
    }

    /**
     * Adds a resource based on the <code>JarEntry</code> which is one item found
     * in a <code>JarFile</code>. This method should only be called up by the
     * <code>BindingFactory</code>
     *
     * @param resource the actual file being found in the <code>JarFile</code>.
     * @param jarFile the actual file that the resource is found in.
     * @throws IOException if the resource cannot be found.
     */
    public void addResource(JarEntry resource, JarFile jarFile) throws IOException
    {
        // get an ouput stream (byte based) attach it to the inputstream
        // from the jar file based on the jar entry.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = jarFile.getInputStream(resource);
        final byte[] bytes = new byte[1024];
        int read = 0;
        while ((read = is.read(bytes)) >= 0)
        {
            baos.write(bytes, 0, read);
        }

        String imageName = resource.getName();

        // the rest of this gets a little ugly since we need to dip into some of
        // the underlying method calls that normally should never be exposed.

        // create an XUI image. Default means no image nor image data. We'll solve
        // that next.
        XUIImage image = new XUIImageImpl();

        // the image icon
        ImageIcon i = new ImageIcon(baos.toByteArray());
        ((JImagePanel)image.getPeer()).setImage(i);


        // get the image's file type
        String type = "";

        StringTokenizer st = new StringTokenizer(imageName, ".");

        while (st.hasMoreTokens())
        {
            type = st.nextToken();
        }


        // now change the node
        XUINode imageNode = image.getNodeRepresentation();
        imageNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "id", "id", "xs:ID",
            IDFactory.getInstance().generateID("image"));
        imageNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "x", "x", "xs:unsignedShort",
            "0");
        imageNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "y", "y", "xs:unsignedShort",
            "0");
        imageNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "width", "width", "xs:unsignedShort",
            "" + + i.getIconWidth());
        imageNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "height", "height", "xs:unsignedShort",
            "" + + i.getIconWidth());
        imageNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "type", "type", "xs:token",
            type);
        imageNode.addAttribute("http://xml.bcit.ca/PurnamaProject/2003/xui", "name", "name", "xs:token",
            imageName);
        XUINode data = new XUINodeImpl("ImageData");
        data.setLevel(5);
        data.addNamespace("xui", "http://xml.bcit.ca/PurnamaProject/2003/xui");
        try
        {
            data.setCDATA(new String(XUIUtils.imageToBase64(i, resource.getName())));
        } catch (IOException ioe)
        {
            throw new XUIDisplayException("Forwarded IOException: " + ioe.getMessage());
        }
        imageNode.addChildNode(data);


        // and finally store it.
        resourceList.put(imageName, image);

    }

    /**
     * Returns an image from the resource pool based on its name. Currently this version
     * does not allow images of the same name.
     *
     * @param imageName the name of the image to load.
     * @param x the x coordinate of this component and where to place it within the container
     * that it sits within.
     * @param y the y coordinate of this component and where to place it within the container
     * that it sits within.
     * @param width the width dimension of this component and the size to make it within the container
     * that it sits within.
     * @param height the height dimension of this component and the size to make it within the container
     * that it sits within.
     */
    public XUIImage getImage(String imageName, int width, int height, int x, int y)
    {
        XUIImage image = null;
        if(imageName != null)
        {
            image = (XUIImage)resourceList.get(imageName);
            image.factoryCoordinates(x, y, width, height);
        }
        return image;
    }
}

