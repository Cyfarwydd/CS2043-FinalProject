package InputOutput;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class xmlParser {

    private String masterResetDate;
    private Integer maxWeeklyTally;
    private Integer maxMonthlyTally;
    private Integer tempWeeklyMax;
    private Integer tempMonthlyMax;
    private Integer defaultCoverageView;

    public xmlParser(String filepath) {
        Document doc = null;

        try
        { doc = getDocument(filepath); }
        catch (SAXException | ParserConfigurationException | IOException e)
        { e.printStackTrace(); }

        NodeList config = doc.getElementsByTagName("config");
        Element settings = (Element)config.item(0);

        this.maxWeeklyTally = Integer.valueOf(settings.getElementsByTagName("weeklyMax").item(0).getTextContent());
        this.maxMonthlyTally = Integer.valueOf(settings.getElementsByTagName("monthlyMax").item(0).getTextContent());
        this.tempWeeklyMax = Integer.valueOf(settings.getElementsByTagName("tempWeeklyMax").item(0).getTextContent());
        this.tempMonthlyMax = Integer.valueOf(settings.getElementsByTagName("tempMonthlyMax").item(0).getTextContent());
        this.defaultCoverageView = Integer.valueOf(settings.getElementsByTagName("defaultCoverageView").item(0).getTextContent());
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

    public void setMasterResetDate(String masterResetDate) {
        //write to file
        this.masterResetDate = masterResetDate;
    }

    public void setMaxWeeklyTally(Integer maxWeeklyTally) {
        //write to file
        this.maxWeeklyTally = maxWeeklyTally;
    }

    public void setMaxMonthlyTally(Integer maxMonthlyTally) {
        //write to file
        this.maxMonthlyTally = maxMonthlyTally;
    }

    public void setTempWeeklyMax(Integer tempWeeklyMax) {
        //write to file
        this.tempWeeklyMax = tempWeeklyMax;
    }

    public void setTempMonthlyMax(Integer tempMonthlyMax) {
        //write to file
        this.tempMonthlyMax = tempMonthlyMax;
    }

    public void setDefaultCoverageView(Integer defaultCoverageView) {
        //write to file
        this.defaultCoverageView = defaultCoverageView;
    }

    private static Document getDocument(String filePath) throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = docBF.newDocumentBuilder();
        Document doc = db.parse(filePath);
        doc.getDocumentElement().normalize();
        return doc;
    }
} // class
