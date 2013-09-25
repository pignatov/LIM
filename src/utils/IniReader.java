package utils;

/*************************************************************************
 * LAN Client/Server Instant Messaging
 * Copyright (C) 2002  Plamen Ignatov <plig@mail.bg>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *************************************************************************/

import data.Users;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.*;
import java.util.Hashtable;
import java.util.StringTokenizer;


public final class IniReader {
    private final Hashtable iniSettings = new Hashtable();

    public IniReader() {
    }

    public IniReader(final String filename) throws IOException {
        // Initialize ini filename
        final BufferedReader f = new BufferedReader(new FileReader(filename));
        String input;
        String key;
        String value;
        while ((input = f.readLine()) != null) {
            // line starting with '#' is a comment
            if (!input.startsWith("#")) {
                final StringTokenizer st = new StringTokenizer(input, "=");
                key = st.nextToken();
                value = st.nextToken();
                add(key.toUpperCase(), value.toUpperCase());
            }
        }
    }

    private void add(final String key, final String value) {
        iniSettings.put(key, value);
    }

    public String getValue(final String parameter) {
        return (String) iniSettings.get(parameter.toUpperCase());
    }

    public static void fillData(final Users aUsers) {
        try {
            final DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            final DocumentBuilder domBuilder =
                    factory.newDocumentBuilder();
            Document test;
            try{
               test = domBuilder.parse(new File("users.xml"));
            }catch (Exception ex){
                test = domBuilder.newDocument();
                Element newUsers = test.createElement("users");
                test.appendChild(newUsers);
                TransformerFactory tFactory =
                        TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer();
                DOMSource source = new DOMSource(test);

                StreamResult result = new StreamResult(new FileWriter("users.xml"));
                transformer.transform(source, result);
            }

            final Document xmlDocument = domBuilder.parse(new File("users.xml"));

            final NodeList users =
                    xmlDocument.getElementsByTagName("user");


            for (int i = 0; i < users.getLength(); i++) {
                final NodeList uInfo =
                        ((Element) users.item(i)).getElementsByTagName("*");
                String UIN = new String();
                String color = new String();
                for (int j = 0; j < uInfo.getLength(); j++) {
                    //one user
                    final Node node = uInfo.item(j);
                    if (node.getNodeName().equals("name")) UIN = node.getFirstChild().getNodeValue();
                    if (node.getNodeName().equals("color")) color = node.getFirstChild().getNodeValue();

                }

                final Long lColor = new Long(color);
                final int iColor = lColor.intValue();

                if (aUsers.exist(UIN.toUpperCase())) aUsers.setColor(UIN.toUpperCase(), new Color(iColor));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void changeData(final String User, final int Color) {
        boolean found = false;
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder domBuilder = factory.newDocumentBuilder();
            final Document xmlDocument;
            File users_xml = new File("users.xml");
            if (users_xml.exists()) {
                xmlDocument = domBuilder.parse(new File("users.xml"));
            } else {
                xmlDocument = domBuilder.newDocument();
                Element newUsers = xmlDocument.createElement("users");
                xmlDocument.appendChild(newUsers);
                TransformerFactory tFactory =
                        TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer();
                DOMSource source = new DOMSource(xmlDocument);

                StreamResult result = new StreamResult(new FileWriter("users.xml"));
                transformer.transform(source, result);
            }

            final NodeList users = xmlDocument.getElementsByTagName("user");

            for (int i = 0; i < users.getLength(); i++) {
                final NodeList uInfo =
                        ((Element) users.item(i)).getElementsByTagName("*");
                String UIN = new String();
                String color = new String();
                int node1 = 0;
                int node2 = 0;
                for (int j = 0; j < uInfo.getLength(); j++) {
                    //one user
                    final Node node = uInfo.item(j);
                    if (node.getNodeName().equals("name")) {
                        UIN = node.getFirstChild().getNodeValue();
                        node1 = j;
                    }
                    if (node.getNodeName().equals("color")) {
                        color = node.getFirstChild().getNodeValue();
                        node2 = j;
                    }
                }
                if (UIN.toUpperCase() == User.toUpperCase()) {
                    uInfo.item(node2).getFirstChild().setNodeValue((new Long(Color)).toString());
                    found = true;
                }
            }

            if (!found) { // We need to create a new record
                final Element newUser = xmlDocument.createElement("user");
                final Element newName = xmlDocument.createElement("name");
                newName.appendChild(xmlDocument.createTextNode(User));
                final Element newColor = xmlDocument.createElement("color");
                newColor.appendChild(xmlDocument.createTextNode((new Long(Color)).toString()));
                newUser.appendChild(newName);
                newUser.appendChild(newColor);
                xmlDocument.getFirstChild().insertBefore(newUser, null);
            }

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(xmlDocument);
            StreamResult result = new StreamResult(new FileWriter("users.xml"));
            transformer.transform(source, result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
