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
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Bar implements Comparable<Bar>{
    private String date;
    private String name;
    private String category;
    private int value;

    Main main = new Main();
    File file = main.getFile();

    public Bar(){
        try {
            String fileFormat = main.getFileFormat();
            if (fileFormat.equals("txt")) {
                readTextFileForBar();
                categorizeByDateForText();
            }
            else if (fileFormat.equals("xml")) {
                readXMLFileForBar();
                categorizeByDateForXML();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Bar(String date,String name, String category, int value){
        this.date = date;
        this.name = name;
        this.category = category;
        this.value = value;
    }

    ArrayList<Bar> barArrayList = new ArrayList<>();

    private void readTextFileForBar() throws FileNotFoundException {
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
                Bar bar = new Bar(splittedStr[0],splittedStr[1], splittedStr[4], Integer.parseInt(splittedStr[3]));
                barArrayList.add(bar);
            }
        }
    }

    private void readXMLFileForBar(){
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
                    Bar bar = new Bar(date, name, category, Integer.parseInt(value));
                    barArrayList.add(bar);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private void categorizeByDateForXML(){
        int count = 0;
        while (count++ <= barArrayList.size()){
            ArrayList<Bar>temp = new ArrayList<>();
            String tempDate = barArrayList.get(0).getDate();
            temp.add(barArrayList.get(0));
            ArrayList<Integer> indexOfWillBeDeleted = new ArrayList<>();
            indexOfWillBeDeleted.add(0);
            for (int i = 1; i < barArrayList.size(); i++){
                if (tempDate.equals(barArrayList.get(i).getDate())){
                    temp.add(barArrayList.get(i));
                    indexOfWillBeDeleted.add(i);
                }
            }
            categorizedByDate.add(temp);
            for (int i = indexOfWillBeDeleted.size() - 1; i >= 0; i--){
                int index = indexOfWillBeDeleted.get(i);
                barArrayList.remove(index);
            }
        }
        System.out.println(barArrayList);
        System.out.println(barArrayList.size());
    }

    private final ArrayList<ArrayList<Bar>> categorizedByDate = new ArrayList<>();

    private void categorizeByDateForText(){
        ArrayList<Bar> temp = new ArrayList<>();
        for (int i = 0; i < barArrayList.size() - 1; i++){
            if (barArrayList.get(i).getDate().equals(barArrayList.get(i+1).getDate())){
                temp.add(barArrayList.get(i));
            }
            else{
                temp.add(barArrayList.get(i));
                categorizedByDate.add(temp);
                temp = new ArrayList<>();
            }
        }
        //sonuncunun sonuncusu eklemiyor
        temp.add(barArrayList.get(barArrayList.size()-1));
        categorizedByDate.add(temp);
    }


    private final List<List<Bar>> sublisted = new ArrayList<>();

    public List<List<Bar>> sublistedArray(){
        for (ArrayList<Bar> barArr : categorizedByDate){
            Collections.sort(barArr);
            Collections.reverse(barArr);
            List<Bar> temp = barArr.subList(0, 10);
            sublisted.add(temp);
        }
        return sublisted;
    }

    @Override
    public String toString() {
        return getDate()+" "+getName()+" "+getCategory()+" "+getValue();
    }

    public String getDate() {
        return date;
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


    @Override
    public int compareTo(Bar o) {
        Integer mValue = value;
        Integer oValue = o.value;
        return mValue.compareTo(oValue);
    }

}
