package org.purnamaproject.xui.binding;

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
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.StringTokenizer;
import org.purnamaproject.xui.XUINode;
import org.purnamaproject.xui.XUI;

import org.purnamaproject.xui.helpers.XUIResources;

/**
 * <p>BindingFactory looks after binding the UI application shell to the actual logic that runs it.
 * This can be from two perspectives:</p>
 *
 * <ul>
 *  <li>A XUI document being read in</li>
 *  <li>Building the user interface using API calls</li>
 * </ul>
 *
 * <p>Either situation will allow for program code to be retrieved from a source - either local or remote.
 * For this version, the resource must be based on the same API/platform as the API code for XUI. In the
 * future, this may change.</p>
 *
 * @version    0.1 18/08/2003
 * @author     Arron Ferguson
 */
public class BindingFactory
{

    private static BindingFactory bindingfactory = null;

    /**
     * Default constructor.
     */
    protected BindingFactory()
    {
        ;
    }

    /**
     * Returns the instance of the factory.
     *
     * @return BindingFactory.
     */
    public static BindingFactory getInstance()
    {
        if(bindingfactory == null)
            bindingfactory = new BindingFactory();
        return bindingfactory;
    }

    /**
     * Accepts the node that is contains the resource.
     *
     * @param resource the XUINode that contains the resource information (uri, type).
     * @param xui the reference to the XUI document. This is required for linking to the event handling.
     * @throws XUIBindingException if the binding could not take place. (i.e. the resource does not conform to
     * the API requirement of having a class that answers to the name of 'Model').
     * @throws MalformedURLException if the URL is malformed.
     * @throws IOException if there is a network error, the file is not found or there is a security restriction
     * placed on the access to the file.
     */
    public void doBinding(XUINode resource, XUI xui) throws XUIBindingException, MalformedURLException, IOException
    {
        if(resource.getAttributeValue("type").equals("java"))
        {
            String className = resource.getAttributeValue("class");
            String aURLString = resource.getAttributeValue("uri");
            URL url = null;
            // get the url ... if it's not a valid URL, then try and grab
            // it as a relative URL (i.e. java.io.File). If that fails
            // re-throw the exception, it's toast
            try
            {
                url = new URL("jar:" + aURLString + "!/");

            } catch (MalformedURLException mue)
            {
                // total schmozzle but it works
                String s = "jar:file://" + new File(aURLString)
                    .getAbsolutePath().replace("\\", "/") + "!/";
                url = new URL(s);

                if(url == null)
                {
                    // it really was malformed after all
                    throw new
                        MalformedURLException("Couldn't bind to: "
                        + aURLString);
                }
            }

            // get a jar connection
            JarURLConnection jarConnection = (JarURLConnection)url.openConnection();
            // get the jar file
            JarFile jarFile = jarConnection.getJarFile();
            // jar files have entries. Cycle through the entries until we find the class that we are looking for.
            Enumeration entries = jarFile.entries();

            // the class that will be the entry point into the model
            JarEntry modelClassEntry = null;
            Class modelClass = null;

            XUIClassLoader xuiLoader = new XUIClassLoader(this.getClass().getClassLoader());

            while(entries.hasMoreElements())
            {
                JarEntry remoteClass = (JarEntry)entries.nextElement();

                // load the classes
                if(remoteClass.getName().endsWith(".class"))
                {
                    // have to get the second last word between period marks. This is
                    // because the convention allows for org.purnamaproject.xui.XUI
                    // that is, the periods can represent packages.

                    StringTokenizer st = new StringTokenizer(remoteClass.getName(), ".");
                    String previousToken = st.nextToken();
                    String currentToken = "";
                    String nameOfClassToLoad = previousToken;

                    while(st.hasMoreTokens())
                    {
                        currentToken = st.nextToken();
                        if(currentToken.equals("class"))
                            nameOfClassToLoad = previousToken;
                        else
                        {
                            nameOfClassToLoad += currentToken;
                        }
                    }

                    // get an ouput stream (byte based) attach it to the inputstream
                    // from the jar file based on the jar entry.
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    InputStream is = jarFile.getInputStream(remoteClass);
                    final byte[] bytes = new byte[1024];
                    int read = 0;
                    while ((read = is.read(bytes)) >= 0)
                    {
                        baos.write(bytes, 0, read);
                    }

                    Class c = xuiLoader.getXUIClass(nameOfClassToLoad, baos);

                    // check for the class that has the init method.

                    if(remoteClass.getName().equals(className + ".class"))
                    {

                        modelClassEntry = remoteClass;
                        modelClass = c;
                    }

                } else
                {

                    String imageNameLowerCase = remoteClass.getName().toLowerCase();
                    if(imageNameLowerCase.endsWith(".jpeg") || imageNameLowerCase.endsWith(".jpg") ||
                        imageNameLowerCase.endsWith(".gif") || imageNameLowerCase.endsWith(".png"))
                    {

                        // add resources (images)
                        XUIResources.getInstance().addResource(remoteClass, jarFile);
                    }
                }

            }

            // now instantiate the model.
            try
            {
                // create a new instance of this class
                Object o = modelClass.newInstance();
                // get the method called 'init'. Again this is part of the API requirement
                Method m = modelClass.getMethod("init", new Class[] {XUI.class});
                // at last, call the method up.
                m.invoke(o, new Object[] {xui});

            } catch(InstantiationException ie)
            {

                ie.printStackTrace();

            } catch(IllegalAccessException iae)
            {

                iae.printStackTrace();

            } catch(NoSuchMethodException nsm)
            {

                nsm.printStackTrace();

            } catch(InvocationTargetException ite)
            {
                System.out.println(ite.getTargetException());
                ite.printStackTrace();
            }

        } else
        {
            throw new XUIBindingException("This platform/API requires Java libraries.");
        }
    }


}
