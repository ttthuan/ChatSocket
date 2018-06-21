/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLReader {

    private static DocumentBuilderFactory dbf;
    private static DocumentBuilder db;
    private static Document doc;
    private static ArrayList<Account> listOfAccount = null;

    public static ArrayList<Account> readXMLDocument(File file) {
        createXMLDocument(file);
        ArrayList<Account> xmlDocument = parserXMLDocument();
        return xmlDocument;
    }

    private static void createXMLDocument(File file) {
        try {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Lỗi kết nối tới database");
        }
    }

    private static ArrayList parserXMLDocument() {
        listOfAccount = new ArrayList<>();
        NodeList nodeList = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                String username = elem.getElementsByTagName("username")
                        .item(0).getChildNodes().item(0).getNodeValue();
                String password = elem.getElementsByTagName("password")
                        .item(0).getChildNodes().item(0).getNodeValue();
                String fullname = elem.getElementsByTagName("fullname")
                        .item(0).getChildNodes().item(0).getNodeValue();
                //String image = elem.getElementsByTagName("image")
                //        .item(0).getChildNodes().item(0).getNodeValue();
                Account account = new Account(username, password, fullname);
                listOfAccount.add(account);
            }
        }
        return listOfAccount;
    }

//    public static void main(String[] args) {
//        String path = "../data/data.xml";
//        File file = new File(XMLReader.class.getResource(path).getPath());
//        readXMLDocument(file);
//
//        for (int i = 0; i < listOfAccount.size(); i++) {
//            System.out.println(listOfAccount.get(i).getUserName());
//            System.out.println(listOfAccount.get(i).getPassword());
//            System.out.println(listOfAccount.get(i).getFullName());
//            System.out.println(listOfAccount.get(i).getImage());
//        }
//
//        //addAccount(new Account("thang", "123", "Trần Văn Thắng", ""), file);
//    }

    public static void addAccount(Account account, File file) {
        try {
            createXMLDocument(file);
            Node data = doc.getFirstChild();
            Element elmAccount = doc.createElement("user");

            Element elmUsername = doc.createElement("username");
            elmUsername.appendChild(doc.createTextNode(account.getUserName()));
            Element elmPassword = doc.createElement("password");
            elmPassword.appendChild(doc.createTextNode(account.getPassword()));
            Element elmFullname = doc.createElement("fullname");
            elmFullname.appendChild(doc.createTextNode(account.getFullName()));
            Element elmImage = doc.createElement("image");
            elmImage.appendChild(doc.createTextNode(account.getImage()));

            elmAccount.appendChild(elmUsername);
            elmAccount.appendChild(elmPassword);
            elmAccount.appendChild(elmFullname);
            elmAccount.appendChild(elmImage);

            data.appendChild(elmAccount);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(file.getPath()));
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void modiferAccount(Account account, File file) {
        try {
            createXMLDocument(file);
            NodeList users = doc.getElementsByTagName("user");
            boolean isUserSelected = false;
            
            int i;
            for (i = 0; i < users.getLength(); i++) {
                Node node = users.item(i);
                NodeList nodelist = node.getChildNodes();
                int j;
                for (j = 0; j < nodelist.getLength(); j++) {
                    if (nodelist.item(j).getNodeName().equals("username")) {
                        String username = nodelist.item(j).getTextContent();
                        if (username.equals(account.getUserName())) {
                            isUserSelected = true;
                            j++;
                        }
                    }
                    if(isUserSelected){
                        if (nodelist.item(j).getNodeName().equals("password")) {
                            nodelist.item(j).setTextContent(account.getPassword());
                            break;
                        }
                    }
                }
                if(isUserSelected){
                    break;
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(file.getPath()));
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void deleteNode(String userName, File file){
        try {
            createXMLDocument(file);
            Element data = doc.getDocumentElement();
            NodeList users = doc.getElementsByTagName("user");
            boolean isUserSelected = false;
            
            int i;
            for (i = 0; i < users.getLength(); i++) {
                Node node = users.item(i);
                NodeList nodelist = node.getChildNodes();
                int j;
                for (j = 0; j < nodelist.getLength(); j++) {
                    if (nodelist.item(j).getNodeName().equals("username")) {
                        String username = nodelist.item(j).getTextContent();
                        if (username.equals(userName)) {
                            isUserSelected = true;
                            
                            data.removeChild(node);
                            break;
                        }
                    }
                }
                if(isUserSelected){
                    break;
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(file.getPath()));
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
