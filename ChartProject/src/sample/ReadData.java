package sample;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadData {

    Main main = new Main();
    private final File file = main.getFile();
    private String title;
    private String xlabel;

    public String getTitle() {
        return title;
    }
    public String getXlabel() {
        return xlabel;
    }


    public ReadData(){
        try {
            String fileFormat = main.getFileFormat();
            if (fileFormat.equals("txt"))
                readTextFile();
            else if (fileFormat.equals("xml"))
                readXMLFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //setLineNumbers();
        //printData();
    }



    private void readTextFile() throws FileNotFoundException {
        ArrayList<String>tempDataToString = new ArrayList<>();
        try(Scanner scanner = new Scanner(file)){
            String line;
            while (scanner.hasNext()){
                line = scanner.nextLine();
                tempDataToString.add(line);
            }
        }
        title = tempDataToString.get(0);
        xlabel = tempDataToString.get(1);
    }

    private void readXMLFile(){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try{
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(file);

            doc.getDocumentElement().normalize();

            //System.out.println("Root element: " +doc.getDocumentElement().getNodeName());
            //System.out.println("===========");

            NodeList titleList = doc.getElementsByTagName("data");
            Node titleNode = titleList.item(0);
            if (titleNode.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) titleNode;
                title = element.getElementsByTagName("title").item(0).getTextContent();
            }
            if (titleNode.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) titleNode;
                xlabel = element.getElementsByTagName("xlabel").item(0).getTextContent();
            }
            //System.out.println(xlabel+" "+title);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

}
