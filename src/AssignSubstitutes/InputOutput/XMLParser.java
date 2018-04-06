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
import java.time.LocalDate;

public class XMLParser {
    //TODO: remove Settings class

    private Integer maxWeeklyTally;
    private Integer maxMonthlyTally;
    private Integer tempWeeklyMax;
    private Integer tempMonthlyMax;

    private LocalDate startDate;
    private Integer weeksToReminder;

    private boolean noNagOverwriteAssignmentChanges;
    private boolean noNagSaveWithEmptyAssignments;
    private boolean noNagOverwriteSave;

    private String masterSchedulePath;
    private String absenceInputPath;
    private String supplyTeacherPath;
    private String courseCodesPath;

    private String onCallerFormPath;
    private String onCallerFormNameFormat;

    private String filepath;

    public XMLParser() {
        Document doc = null;

        //TODO: add defaults
        try
        { doc = getDocument("config"); }
        catch (TransformerException | SAXException | ParserConfigurationException | IOException e)
        { e.printStackTrace(); }

        NodeList config = doc.getElementsByTagName("config");
        Element settings = (Element)config.item(0);

        this.filepath = "config";

        //TODO: make not shit the bed whenever something doesn't exist

        this.maxWeeklyTally = Integer.valueOf(settings.getElementsByTagName("weeklyMax").item(0).getTextContent());
        this.maxMonthlyTally = Integer.valueOf(settings.getElementsByTagName("monthlyMax").item(0).getTextContent());
        this.tempWeeklyMax = Integer.valueOf(settings.getElementsByTagName("tempWeeklyMax").item(0).getTextContent());
        this.tempMonthlyMax = Integer.valueOf(settings.getElementsByTagName("tempMonthlyMax").item(0).getTextContent());

        this.startDate = LocalDate.parse(settings.getElementsByTagName("startDate").item(0).getTextContent());
        this.weeksToReminder = Integer.valueOf(settings.getElementsByTagName("weeksUntilEnd").item(0).getTextContent());

        this.noNagOverwriteAssignmentChanges = Boolean.parseBoolean(settings.getElementsByTagName("overwriteAssignmentChanges").item(0).getTextContent());
        this.noNagSaveWithEmptyAssignments = Boolean.parseBoolean(settings.getElementsByTagName("saveWithEmptyAssignments").item(0).getTextContent());
        this.noNagOverwriteSave = Boolean.parseBoolean(settings.getElementsByTagName("overwriteSave").item(0).getTextContent());

        this.masterSchedulePath = settings.getElementsByTagName("masterSchedule").item(0).getTextContent();
        this.absenceInputPath = settings.getElementsByTagName("absences").item(0).getTextContent();
        this.supplyTeacherPath = settings.getElementsByTagName("supply").item(0).getTextContent();
        this.courseCodesPath = settings.getElementsByTagName("courses").item(0).getTextContent();

        this.onCallerFormPath = settings.getElementsByTagName("onCallerFormsDir").item(0).getTextContent();
        this.onCallerFormNameFormat = settings.getElementsByTagName("onCallerNameFormat").item(0).getTextContent();
    } // constructor

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public Integer getWeeksToReminder() {
        return weeksToReminder;
    }

    public boolean isNoNagOverwriteAssignmentChanges() {
        return noNagOverwriteAssignmentChanges;
    }

    public boolean isNoNagSaveWithEmptyAssignments() {
        return noNagSaveWithEmptyAssignments;
    }

    public boolean isNoNagOverwriteSave() {
        return noNagOverwriteSave;
    }

    public String getMasterSchedulePath() {
        return masterSchedulePath;
    }

    public String getAbsenceInputPath() {
        return absenceInputPath;
    }

    public String getSupplyTeacherPath() {
        return supplyTeacherPath;
    }

    public String getCourseCodesPath() {
        return courseCodesPath;
    }

    public String getOnCallerFormPath() {
        return onCallerFormPath;
    }

    public String getOnCallerFormNameFormat() {
        return onCallerFormNameFormat;
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

    public void setStartDate(LocalDate startDate) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        writeXML(startDate.toString(), "startDate");
        this.startDate = startDate;
    }

    public void setWeeksToReminder(Integer weeksToReminder) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        writeXML(weeksToReminder.toString(), "weeksUntilEnd");
        this.weeksToReminder = weeksToReminder;
    }

    public void setNoNagOverwriteAssignmentChanges(Boolean noNagOverwriteAssignmentChanges) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        writeXML(noNagOverwriteAssignmentChanges.toString(), "overwriteAssignmentChanges");
        this.noNagOverwriteAssignmentChanges = noNagOverwriteAssignmentChanges;
    }

    public void setNoNagSaveWithEmptyAssignments(Boolean noNagSaveWithEmptyAssignments) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        writeXML(noNagSaveWithEmptyAssignments.toString(), "saveWithEmptyAssignments");
        this.noNagSaveWithEmptyAssignments = noNagSaveWithEmptyAssignments;
    }

    public void setNoNagOverwriteSave(Boolean noNagOverwriteSave) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        writeXML(noNagOverwriteSave.toString(), "overwriteSave");
        this.noNagOverwriteSave = noNagOverwriteSave;
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

    public void setCourseCodesPath(String courseCodesPath) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        writeXML(courseCodesPath, "courses");
        this.courseCodesPath = courseCodesPath;
    }

    public void setOnCallerFormPath(String onCallerFormPath) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        writeXML(onCallerFormPath, "onCallerFormsDir");
        this.onCallerFormPath = onCallerFormPath;
    }

    public void setOnCallerFormNameFormat(String onCallerFormNameFormat) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        writeXML(onCallerFormNameFormat, "onCallerNameFormat");
        this.onCallerFormNameFormat = onCallerFormNameFormat;
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
