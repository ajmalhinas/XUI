package org.purnamaproject.xui.helpers;

/**
 * @(#)XUIFilter.java    0.5 18/08/2003
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

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.filechooser.FileFilter;

/**
 * A helper class that filters file extensions for the XUIFileDialog.
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public class XUIFilter extends FileFilter
{
    /**
     * The list of types accepted by the filter.
     */
    private LinkedList filterList = null;

    /**
     * Initializes the filter
     */
    public XUIFilter()
    {
        filterList = new LinkedList();
    }

    /**
     * Adds a specific type to the list of filtered file extension types. Any types listed
     * in this filter will show up when the XUIFileDialog displays files. All others will be
     * ignored.
     *
     * @param type the type to add. This can be 'jpeg', 'jpg', 'gif', 'tiff', 'tif', etc.
     */
    public void addType(String type)
    {
        filterList.add(type);
    }

    /**
     * Adds a sublist to the already existing list of filters.
     *
     * @param subList the sub-list to add. These can be 'jpeg', 'jpg', 'gif', 'tiff', 'tif', etc.
     */
    public void addTypes(List subList)
    {
        filterList.add(subList);
    }

    /**
     * Deletes a specific type from the list of filtered file extension types. If the type
     * is not found, no action is taken.
     *
     * @param type the file type (i.e. file extension). This can be 'jpeg', 'jpg', 'gif', 'tiff', 'tif', etc.
     */
    public void deleteType(String type)
    {
        filterList.remove(type);
    }

    /**
     * Removes all the types listed by this filter.
     *
     */
    public void removeAllTypes()
    {
        for(int i = 0; i < filterList.size(); i++)
        {
            filterList.remove(i);
        }
    }

    /**
     * Returns the list of file filter types that are accepted.
     *
     * @return the list of types accepted.
     */
    public List getTypes()
    {
        return filterList;
    }

    /**
     * Determines whether or not to the file being passed to
     * it is accepted. A file will be accepted if it is listed as one of the accepted
     * types of files. These is based on file extensions. In order to add a file type
     * (i.e. extension) to the list, call the <code>addType(String type)</code> method.
     *
     * @param file the file that is passed to it.
     * @return true if the file is accepted and false otherwise.
     */
    public boolean accept(File file)
    {
        if(file.isDirectory())
            return true;

        // get the file extension
        String ext = "";
        StringTokenizer st = new StringTokenizer(file.getName(), ".");

        // do this in case there are more than 1 '.' in a file name
        while (st.hasMoreTokens())
        {
            ext = st.nextToken();
        }
        // check for null
        if(ext == null)
            ext = "";

        // go through the list
        for(int i = 0; i < filterList.size(); i++)
        {
            String type = (String)filterList.get(i);
            if(type.equals(ext))
                return true;
        }

        return false;
    }

    /**
     * Returns the description of the filter.
     *
     * @return the string description of this filter.
     */
    public String getDescription()
    {
        StringBuffer sb = new StringBuffer(40);
        int size = filterList.size();
        if(size == 0)
            return "";
        else if(size == 1)
            return (String)filterList.get(0);
        else
        {
            boolean firstTime = true;
            for(int i = 0; i < filterList.size(); i++)
            {
                if(firstTime)
                {
                    sb.append((String)filterList.get(i));
                    firstTime = false;
                } else
                    sb.append(", " + (String)filterList.get(i));
            }
            return sb.toString();
        }
    }

}
