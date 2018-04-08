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

public class Settings {
    private final static Integer DEFAULT_MAX_WEEKLY_TALLY=2;
    private final static Integer DEFAULT_MAX_MONTHLY_TALLY=4;

    private final static Integer DEFAULT_WEEKS_TO_REMINDER=22;

    private final static String DEFAULT_ON_CALLER_FORM_PATH = "/OnCallerForms/";
    private final static String DEFAULT_ON_CALLER_FORM_NAME_FORMAT = "[NAME]-[MMM][DD]-[YY]";

    private Integer maxWeeklyTally;
    private Integer maxMonthlyTally;
    private Integer tempWeeklyMax;
    private Integer tempMonthlyMax;
    //TODO: make all class variables static
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

    private static String filepath;

    public Settings() throws TransformerException, SAXException, ParserConfigurationException, IOException
    {
        Document doc;
        NodeList config;
        Element settings;
        this.filepath = "config";

        try {
            doc = getDocument(filepath);
            config = doc.getElementsByTagName("config");
            settings = (Element) config.item(0);
        } catch (NullPointerException | ParserConfigurationException | SAXException | IOException e)
        {
            doc=replaceMissingDocument();
            config = doc.getElementsByTagName("config");
            settings = (Element) config.item(0);
        }

        //TODO: add date temps were changed and a check to reverse them
        Element parentElement;
        try {
            parentElement = (Element) settings.getElementsByTagName("onCalls").item(0);

            try {
                this.maxWeeklyTally = verifyInts(settings.getElementsByTagName("weeklyMax").item(0).getTextContent(),
                        DEFAULT_MAX_WEEKLY_TALLY, "weeklyMax");
            }catch (Exception e){
                replaceMissingElement(doc, parentElement, "weeklyMax");
                setMaxWeeklyTally(DEFAULT_MAX_WEEKLY_TALLY);
            }

            try {
                this.maxMonthlyTally = verifyInts(settings.getElementsByTagName("monthlyMax").item(0).getTextContent(),
                        DEFAULT_MAX_MONTHLY_TALLY, "monthlyMax");
            }catch (Exception e){
                replaceMissingElement(doc, parentElement,"monthlyMax");
                setMaxMonthlyTally(DEFAULT_MAX_WEEKLY_TALLY);
            }

            try {
                this.tempWeeklyMax = verifyInts(settings.getElementsByTagName("tempWeeklyMax").item(0).getTextContent(),
                        maxWeeklyTally, "tempWeeklyMax");
            }catch (Exception e){
                replaceMissingElement(doc, parentElement,"tempWeeklyMax");
                setTempWeeklyMax(maxWeeklyTally);
            }

            try {
                this.tempMonthlyMax = verifyInts(settings.getElementsByTagName("tempMonthlyMax").item(0).getTextContent(),
                        maxMonthlyTally, "tempMonthlyMax");
            }catch (Exception e){
                replaceMissingElement(doc, parentElement, "tempMonthlyMax");
                setTempMonthlyMax(maxMonthlyTally);
            }

        }catch (Exception e){
            replaceMissingOnCallsElement(doc, settings);
        }

        try {
            parentElement = (Element) settings.getElementsByTagName("startEndDates").item(0);

            try {
                this.startDate = verifyDate(settings.getElementsByTagName("startDate").item(0).getTextContent(),
                        setLocalDate(), "startDate");

            } catch (Exception e) {
                replaceMissingElement(doc, parentElement, "startDate");
                setStartDate(setLocalDate());
            }

            try {
                this.weeksToReminder = verifyInts(settings.getElementsByTagName("weeksUntilEnd").item(0).getTextContent(), DEFAULT_WEEKS_TO_REMINDER, "weeksUntilEnd");
            } catch (Exception e) {
                replaceMissingElement(doc, parentElement, "weeksUntilEnd");
                setWeeksToReminder(DEFAULT_WEEKS_TO_REMINDER);
            }

        }catch (Exception e){
            replaceMissingStartEndDatesElement(doc, settings);
        }

        try{
            parentElement = (Element) settings.getElementsByTagName("noReminders").item(0);

            try {
                this.noNagOverwriteAssignmentChanges = Boolean.parseBoolean(settings.getElementsByTagName("overwriteAssignmentChanges").item(0).getTextContent());
            } catch (Exception e) {
                replaceMissingElement(doc, parentElement, "overwriteAssignmentChanges");
            }

            try {
                this.noNagSaveWithEmptyAssignments = Boolean.parseBoolean(settings.getElementsByTagName("saveWithEmptyAssignments").item(0).getTextContent());
            }catch(Exception e){
                replaceMissingElement(doc, parentElement, "saveWithEmptyAssignments");
            }

            try {
                this.noNagOverwriteSave = Boolean.parseBoolean(settings.getElementsByTagName("overwriteSave").item(0).getTextContent());
            }catch(Exception e){
                replaceMissingElement(doc, parentElement, "overwriteSave");
            }

        }catch (Exception e){
            replaceMissingNoRemindersElement(doc, settings);
        }

        try {
            parentElement = (Element) settings.getElementsByTagName("inputFilePaths").item(0);

            try {
                this.masterSchedulePath = settings.getElementsByTagName("masterSchedule").item(0).getTextContent();
            }catch(Exception e){
                replaceMissingElement(doc, parentElement, "masterSchedule");
            }

            try {
                this.absenceInputPath = settings.getElementsByTagName("absences").item(0).getTextContent();
            }catch(Exception e){
                replaceMissingElement(doc, parentElement, "absences");
            }

            try {
                this.supplyTeacherPath = settings.getElementsByTagName("supply").item(0).getTextContent();
            }catch (Exception e){
                replaceMissingElement(doc, parentElement, "supply");
            }

            try {
                this.courseCodesPath = settings.getElementsByTagName("courses").item(0).getTextContent();
            }catch(Exception e){
                replaceMissingElement(doc, parentElement, "courses");
            }
        }catch(Exception e){
            replaceMissingInputFilePathsElement(doc, settings);
        }

        try {
            parentElement = (Element) settings.getElementsByTagName("output").item(0);

            try {
                this.onCallerFormPath = settings.getElementsByTagName("onCallerFormsDir").item(0).getTextContent();
            }catch (Exception e){
                replaceMissingElement(doc, parentElement, "onCallerFormsDir");
                setOnCallerFormPath(DEFAULT_ON_CALLER_FORM_PATH);
            }
            try {
                this.onCallerFormNameFormat = settings.getElementsByTagName("onCallerNameFormat").item(0).getTextContent();
            }catch (Exception e){
                replaceMissingElement(doc, parentElement, "onCallerNameFormat");
                setOnCallerFormNameFormat(DEFAULT_ON_CALLER_FORM_NAME_FORMAT);
            }
        }catch (Exception e){
            replaceMissingOutputElement(doc, settings);
        }
        System.out.println("does it get this far?");
    } // constructor



    private Integer verifyInts(String integer, int defaultVal, String key) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        Integer retVal;
        try{
            int num = Integer.parseInt(integer);
            retVal = num;
        }
        catch (Exception e){
            retVal = defaultVal;
            writeXML(retVal.toString(), key);
        }
        return retVal;
    }

    private LocalDate verifyDate(String date, LocalDate defaultVal, String key) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        LocalDate retVal;
        try{
            LocalDate d = LocalDate.parse(date);
            retVal = d;
        }
        catch (Exception e){
            retVal = defaultVal;
            writeXML(retVal.toString(), key);
        }
        return retVal;
    }

    private LocalDate setLocalDate(){
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        LocalDate date;
        if(month > 1 && month < 7){
            //start month is February
            date = LocalDate.of(now.getYear(), 2, 1);
        }else{
            //start month is September
            int year;
            if(month==1){
                year = now.getYear()-1;
            }else{
                year = now.getYear();
            }
            date = LocalDate.of(year, 9, 1);
        }
        int dayOfTheWeek = date.getDayOfWeek().getValue();
        //start on a Monday
        if(dayOfTheWeek!=1){
            date.plusDays(8-dayOfTheWeek);
        }
        return date;
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

    public String getMasterSchedulePath() { return masterSchedulePath; }

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

    private Document getDocument(String filePath) throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = docBF.newDocumentBuilder();
        Document doc;
        doc = db.parse(filePath);
        doc.getDocumentElement().normalize();
        return doc;
    }

    private Document replaceMissingDocument() throws IOException, SAXException, ParserConfigurationException, TransformerException
    {
        DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = docBF.newDocumentBuilder();
        Document doc = db.newDocument();
        Element rootElement = doc.createElement("config");
        doc.appendChild(rootElement);
        replaceMissingOnCallsElement(doc, rootElement);
        replaceMissingStartEndDatesElement(doc, rootElement);
        replaceMissingNoRemindersElement(doc, rootElement);
        replaceMissingInputFilePathsElement(doc, rootElement);
        replaceMissingOutputElement(doc, rootElement);
        return doc;
    }

    private void replaceMissingOnCallsElement(Document doc, Element parent) throws IOException, SAXException, ParserConfigurationException, TransformerException
    {
        Element ele = doc.createElement("onCalls");
        parent.appendChild(ele);
        parent = ele;
        ele = doc.createElement("weeklyMax");
        parent.appendChild(ele);
        ele = doc.createElement("monthlyMax");
        parent.appendChild(ele);
        ele = doc.createElement("tempWeeklyMax");
        parent.appendChild(ele);
        ele = doc.createElement("tempMonthlyMax");
        parent.appendChild(ele);
        writeXML(doc);
        setMaxWeeklyTally(DEFAULT_MAX_WEEKLY_TALLY);
        setMaxMonthlyTally(DEFAULT_MAX_WEEKLY_TALLY);
        setTempWeeklyMax(maxWeeklyTally);
        setTempMonthlyMax(maxMonthlyTally);
    }

    private void replaceMissingStartEndDatesElement(Document doc, Element parent) throws IOException, SAXException, ParserConfigurationException, TransformerException
    {
        Element ele = doc.createElement("startEndDates");
        parent.appendChild(ele);
        parent = ele;
        ele = doc.createElement("startDate");
        parent.appendChild(ele);
        ele = doc.createElement("weeksUntilEnd");
        parent.appendChild(ele);
        writeXML(doc);

        setStartDate(setLocalDate());
        setWeeksToReminder(DEFAULT_WEEKS_TO_REMINDER);
    }

    private void replaceMissingNoRemindersElement(Document doc, Element parent) throws TransformerException
    {
        Element ele = doc.createElement("noReminders");
        parent.appendChild(ele);
        parent = ele;
        ele = doc.createElement("overwriteAssignmentChanges");
        parent.appendChild(ele);
        ele = doc.createElement("saveWithEmptyAssignments");
        parent.appendChild(ele);
        ele = doc.createElement("overwriteSave");
        parent.appendChild(ele);
        writeXML(doc);
    }

    private void replaceMissingInputFilePathsElement(Document doc, Element parent) throws TransformerException
    {
        Element ele = doc.createElement("inputFilePaths");
        parent.appendChild(ele);
        parent = ele;
        ele = doc.createElement("masterSchedule");
        parent.appendChild(ele);
        ele = doc.createElement("absences");
        parent.appendChild(ele);
        ele = doc.createElement("supply");
        parent.appendChild(ele);
        ele = doc.createElement("courses");
        parent.appendChild(ele);
        writeXML(doc);
    }

    private void replaceMissingOutputElement(Document doc, Element parent) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        Element ele = doc.createElement("output");
        parent.appendChild(ele);
        parent = ele;
        ele = doc.createElement("onCallerFormsDir");
        parent.appendChild(ele);
        ele = doc.createElement("onCallerNameFormat");
        parent.appendChild(ele);
        writeXML(doc);

        setOnCallerFormPath(DEFAULT_ON_CALLER_FORM_PATH);
        setOnCallerFormNameFormat(DEFAULT_ON_CALLER_FORM_NAME_FORMAT);
    }

    private void replaceMissingElement(Document doc, Element parentElement, String key) throws TransformerException
    {
        Element ele = doc.createElement(key);
        parentElement.appendChild(ele);
        writeXML(doc);
    }

    private void writeXML(String input, String childElement) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        Document doc = getDocument(filepath);
        Node out = doc.getElementsByTagName(childElement).item(0);
        out.setTextContent(input);

        writeXML(doc);
    }

    private void writeXML(Document doc) throws TransformerException
    {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filepath));
        transformer.transform(source, result);
    }
} // class
