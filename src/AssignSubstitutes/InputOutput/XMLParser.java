package AssignSubstitutes.InputOutput;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class XMLParser {

    private String masterSchedulePath;
    private String absenceInputPath;
    private String supplyTeacherPath;
    private Integer maxWeeklyTally;
    private Integer maxMonthlyTally;
    private Integer tempWeeklyMax;
    private Integer tempMonthlyMax;

    private String filepath;

    public XMLParser(String filepath) {
        Document doc = null;

        try
        { doc = getDocument(filepath); }
        catch (TransformerException | SAXException | ParserConfigurationException | IOException e)
        { e.printStackTrace(); }

        NodeList config = doc.getElementsByTagName("config");
        Element settings = (Element)config.item(0);

        this.filepath = filepath;

        this.maxWeeklyTally = Integer.valueOf(settings.getElementsByTagName("weeklyMax").item(0).getTextContent());
        this.maxMonthlyTally = Integer.valueOf(settings.getElementsByTagName("monthlyMax").item(0).getTextContent());
        this.tempWeeklyMax = Integer.valueOf(settings.getElementsByTagName("tempWeeklyMax").item(0).getTextContent());
        this.tempMonthlyMax = Integer.valueOf(settings.getElementsByTagName("tempMonthlyMax").item(0).getTextContent());
        this.masterSchedulePath = settings.getElementsByTagName("masterSchedule").item(0).getTextContent();
        this.absenceInputPath = settings.getElementsByTagName("absences").item(0).getTextContent();
        this.supplyTeacherPath = settings.getElementsByTagName("supply").item(0).getTextContent();
        /*this.masterSchedulePath = "";
        this.absenceInputPath = "";
        this.supplyTeacherPath = "";*/

    } // constructor

    public String getMasterSchedulePath() {
        return masterSchedulePath;
    }

    public String getAbsenceInputPath() {
        return absenceInputPath;
    }

    public String getSupplyTeacherPath() {
        return supplyTeacherPath;
    }

    public Integer getMaxWeeklyTally() {
        return maxWeeklyTally;
    }

    public Integer getMaxMonthlyTally() {
        return maxMonthlyTally;
    }

    public Integer getTempWeeklyMax() {
        return tempWeeklyMax;
    }

    public Integer getTempMonthlyMax() {
        return tempMonthlyMax;
    }

    public void setMasterSchedulePath(String masterSchedulePath) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        writeXML(masterSchedulePath, "masterSchedule");
        this.masterSchedulePath = masterSchedulePath;
    }

    public void setAbsenceInputPath(String absenceInputPath) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        writeXML(absenceInputPath, "absences");
        this.absenceInputPath = absenceInputPath;
    }

    public void setSupplyTeacherPath(String supplyTeacherPath) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        writeXML(supplyTeacherPath, "supply");
        this.supplyTeacherPath = supplyTeacherPath;
    }

    public void setMaxWeeklyTally(Integer maxWeeklyTally) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        writeXML(maxWeeklyTally.toString(), "weeklyMax");
        this.maxWeeklyTally = maxWeeklyTally;
    }

    public void setMaxMonthlyTally(Integer maxMonthlyTally) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        writeXML(maxMonthlyTally.toString(),"monthlyMax");
        this.maxMonthlyTally = maxMonthlyTally;
    }

    public void setTempWeeklyMax(Integer tempWeeklyMax) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        writeXML(tempWeeklyMax.toString(), "tempWeeklyMax");
        this.tempWeeklyMax = tempWeeklyMax;
    }

    public void setTempMonthlyMax(Integer tempMonthlyMax) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        writeXML(tempMonthlyMax.toString(), "tempMonthlyMax");
        this.tempMonthlyMax = tempMonthlyMax;
    }

    private static Document getDocument(String filePath) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = docBF.newDocumentBuilder();
        Document doc = db.parse(filePath);
        doc.getDocumentElement().normalize();
        return doc;
    }

    private void writeXML(String input, String childElement) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        Document doc = getDocument(filepath);
        Node out = doc.getElementsByTagName(childElement).item(0);
        out.setTextContent(input);


        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filepath));
        transformer.transform(source, result);

    }
} // class
