package com.comp3607.io;

import com.comp3607.questions.Question;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
//import javax.swing.text.html.parser.Element; //it brokey
// incase i did something wrong and you need to correct it @reems
// https://www.geeksforgeeks.org/java/read-and-write-xml-files-in-java
// https://www.docs.inductiveautomation.com/docs/8.1/platform/scripting/scripting-examples/parsing-xml-with-the-etree-library
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class XMLReader implements Reader{
    private String filename;

    public XMLReader(String filename) {
        this.filename = filename;
    }

    public List<Question> read() {
        List<Question> questions = new ArrayList<>();
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(filename));
            NodeList nodes = doc.getElementsByTagName("QuestionItem");
            for (int i=0; i<nodes.getLength(); i++){
                Node n = nodes.item(i); //error code tags n as null
                if (n.getNodeType() == Node.ELEMENT_NODE){ //either this or hasAttributes, documentation says that it can tell whether a node is an element or not - confirm if works
                    Element x = (Element) n; //if its an element then recast n to element and then start extracting data
                    String category = x.getElementsByTagName("Category").item(0).getTextContent();
                    int value = Integer.parseInt(x.getElementsByTagName("Value").item(0).getTextContent());
                    String text = x.getElementsByTagName("QuestionText").item(0).getTextContent();
                    Map<Character, String> options = new HashMap<>();
                    options.put('A', x.getElementsByTagName("OptionA").item(0).getTextContent());
                    options.put('B', x.getElementsByTagName("OptionB").item(0).getTextContent());
                    options.put('C', x.getElementsByTagName("OptionC").item(0).getTextContent());
                    options.put('D', x.getElementsByTagName("OptionD").item(0).getTextContent());
                    char answer = (x.getElementsByTagName("CorrectAnswer").item(0).getTextContent()).charAt(0);
                    questions.add(new Question(category, value, text, options, answer));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return questions;
    }
}
