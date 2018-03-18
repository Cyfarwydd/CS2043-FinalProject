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

public class xmlParser {

    private String masterResetDate;
    private Integer maxWeeklyTally;
    private Integer maxMonthlyTally;
    private Integer tempWeeklyMax;
    private Integer tempMonthlyMax;
    private Integer defaultCoverageView;
    private String filepath;

    public xmlParser(String filepath) {
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
        this.masterResetDate = "Not Set";

    } // constructor

    public String getMasterResetDate() {
        return masterResetDate;
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

    public Integer getDefaultCoverageView() {
        return defaultCoverageView;
    }

    public void setMasterResetDate(String masterResetDate) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        writeXML(masterResetDate, "masterResetDate");
        this.masterResetDate = masterResetDate;
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

    public void setDefaultCoverageView(Integer defaultCoverageView) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        writeXML(defaultCoverageView.toString(), "defaultCoverageView");
        this.defaultCoverageView = defaultCoverageView;
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
