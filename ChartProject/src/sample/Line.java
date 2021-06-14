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
import java.util.*;

public class Line {
    private String name;
    private String category;
    private int value;
    private String date;

    //ReadData rd = new ReadData();
    Main main = new Main();
    File file = main.getFile();

    public Line(){
        try {
            String fileFormat = main.getFileFormat();
            if (fileFormat.equals("txt"))
                readTextFileforLine();
            else if (fileFormat.equals("xml"))
                readXMLFileForLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //categorizeByName();
        testLister();
    }

    public Line(String date,String name, String category, int value){
        this.name = name;
        this.category = category;
        this.value = value;
        this.date=date;
    }

    ArrayList<Line> lineArrayList = new ArrayList<>();
    //ArrayList<String> readDataArray = rd.getDataToString();

    /*
    private void setLineArrayList(){
        for (String str : readDataArray){
            String[] strSplit = str.split(",");
            Line line = new Line(strSplit[1], strSplit[4], Integer.parseInt(strSplit[3]));
            lineArrayList.add(line);
            System.out.println(line.toString());
        }
    }
     */

    @Override
    public String toString() {
        return getDate()+" "+getName()+" "+getCategory()+" "+getValue();
    }

    public String getName() {
        return name;
    }
    public String getCategory() {
        return category;
    }
    public int getValue() {
        return value;
    }
    public String getDate(){return date;}

    private void readTextFileforLine() throws FileNotFoundException {
        ArrayList<String>tempDataToString = new ArrayList<>();
        try(Scanner scanner = new Scanner(file)){
            String line;
            while (scanner.hasNext()){
                line = scanner.nextLine();
                tempDataToString.add(line);
            }
        }
        //ilk 3 satırda önceden kullanığımız veriler old. için sildik.
        tempDataToString.subList(0, 3).clear();
        for (String str : tempDataToString){
            String[] splittedStr = str.split(",");
            if (splittedStr.length==5){
                /*if(!dates.contains(splittedStr[0])){
                    dates.add(splittedStr[0]);
                }*/
                date=splittedStr[0];
                name=splittedStr[1];
                value=Integer.parseInt(splittedStr[3]);
                category=splittedStr[4];
                Line line = new Line(date,name, category,value);
                lineArrayList.add(line);
            }
        }
        //System.out.println(dates);
        System.out.println(lineArrayList.size());
        System.out.println(lineArrayList.get(0).getName());
    }


    private void readXMLFileForLine(){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try{
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(file);

            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("record");
            for (int temp = 0; temp < list.getLength(); temp++){
                Node node = list.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    //System.out.println(element.getNodeName());//record yazdırır
                    String date = element.getElementsByTagName("field").item(2).getTextContent();
                    String name = element.getElementsByTagName("field").item(0).getTextContent();
                    String value = element.getElementsByTagName("field").item(3).getTextContent();
                    String category = element.getElementsByTagName("field").item(4).getTextContent();
                    Line line = new Line(date, name, category, Integer.parseInt(value));
                    lineArrayList.add(line);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Line> byName=new ArrayList<>();
    public ArrayList<Integer> lineNumber=new ArrayList<>();
    public ArrayList<String> allCategory=new ArrayList<>();
    private final ArrayList<Line> tempList=new ArrayList<>();
    private final ArrayList<String> tempCategory=new ArrayList<>();
    private  ArrayList<Integer> willDelete=new ArrayList<>();
    private void testLister(){
        while(lineArrayList.size()!=0){
            String compareName=lineArrayList.get(0).getDate();
            for (int i=0;i<lineArrayList.size();i++) {
                if (compareName.equals(lineArrayList.get(i).getDate())) {
                    tempList.add(lineArrayList.get(i));
                    tempCategory.add(lineArrayList.get(i).getCategory());
                    willDelete.add(i);
                }
            }
            allCategory.addAll(tempCategory);
            byName.addAll(tempList);
            lineNumber.add(tempList.size());
            tempList.clear();
            tempCategory.clear();
            for (int i=willDelete.size()-1;i>=0;i--){
                int index=willDelete.get((i));
                lineArrayList.remove(index);
            }
            willDelete.clear();

        }
        System.out.println(byName);
        Set<String> s=new HashSet<>(allCategory);
        allCategory.clear();
        allCategory.addAll(s);
        //System.out.println(allCategory);

    }

}
