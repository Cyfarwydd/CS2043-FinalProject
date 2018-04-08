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
import java.time.temporal.WeekFields;
import java.util.Locale;

public class Settings {
    //TODO: create Element inner class that consists of default data, set data, key, parent and children and make a tree
    // that corresponds to the data and makes it easier to manipulate the xml with a much cleaner init process

    private final static String FILEPATH="config";

    //DEFAULT VALUES
    private final static Integer DEFAULT_MAX_WEEKLY_TALLY=2;
    private final static Integer DEFAULT_MAX_MONTHLY_TALLY=4;
    private final static Integer DEFAULT_APPLICABLE_PERIOD=0;

    private final static Integer DEFAULT_WEEKS_TO_REMINDER=22;

    private final static String DEFAULT_ON_CALLER_FORM_PATH = "/OnCallerForms/";
    private final static String DEFAULT_ON_CALLER_FORM_NAME_FORMAT = "[NAME]-[MMM][DD]-[YY]";

    //STORED VALUES
    private static Integer maxWeeklyTally;
    private static Integer maxMonthlyTally;
    private static Integer tempWeeklyMax;
    private static Integer tempMonthlyMax;
    private static Integer tempWeeklyMaxApplicableWeek;
    private static Integer tempMonthlyMaxApplicableMonth;

    private static LocalDate startDate;
    private static Integer weeksToReminder;

    private static Boolean noNagOverwriteAssignmentChanges;
    private static Boolean noNagSaveWithEmptyAssignments;
    private static Boolean noNagOverwriteSave;

    private static String masterSchedulePath;
    private static String absenceInputPath;
    private static String supplyTeacherPath;
    private static String courseCodesPath;

    private static String onCallerFormPath;
    private static String onCallerFormNameFormat;

    //INIT
    private static boolean isInit = false;
    public static void init() throws TransformerException, SAXException, ParserConfigurationException, IOException
    {
        isInit=true;
        Document doc;
        NodeList config;
        Element settings;

        try {
            doc = getDocument(FILEPATH);
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
                maxWeeklyTally = verifyInts(settings.getElementsByTagName("weeklyMax").item(0).getTextContent(),
                        DEFAULT_MAX_WEEKLY_TALLY, "weeklyMax");
            }catch (Exception e){
                replaceMissingElement(doc, parentElement, "weeklyMax");
                setMaxWeeklyTally(DEFAULT_MAX_WEEKLY_TALLY);
            }

            try {
                maxMonthlyTally = verifyInts(settings.getElementsByTagName("monthlyMax").item(0).getTextContent(),
                        DEFAULT_MAX_MONTHLY_TALLY, "monthlyMax");
            }catch (Exception e){
                replaceMissingElement(doc, parentElement,"monthlyMax");
                setMaxMonthlyTally(DEFAULT_MAX_WEEKLY_TALLY);
            }

            try {
                tempWeeklyMaxApplicableWeek = verifyInts(settings.getElementsByTagName("tempWeeklyMaxApplicableWeek").item(0).getTextContent(),
                        DEFAULT_APPLICABLE_PERIOD, "tempWeeklyMaxApplicableWeek");
            }catch (Exception e){
                replaceMissingElement(doc, parentElement,"tempWeeklyMaxApplicableWeek");
                setTempWeeklyMaxApplicableWeek(DEFAULT_APPLICABLE_PERIOD);
            }

            try {
                if(getWeek()==tempWeeklyMaxApplicableWeek) {
                    tempWeeklyMax = verifyInts(settings.getElementsByTagName("tempWeeklyMax").item(0).getTextContent(),
                            maxWeeklyTally, "tempWeeklyMax");
                }else{
                    setTempWeeklyMax(maxWeeklyTally);
                }
            } catch (Exception e) {
                replaceMissingElement(doc, parentElement, "tempWeeklyMax");
                setTempWeeklyMax(maxWeeklyTally);
            }

            try {
                tempMonthlyMaxApplicableMonth = verifyInts(settings.getElementsByTagName("tempMonthlyMaxApplicableMonth").item(0).getTextContent(),
                        DEFAULT_APPLICABLE_PERIOD, "tempMonthlyMaxApplicableMonth");
            }catch (Exception e){
                replaceMissingElement(doc, parentElement,"tempMonthlyMaxApplicableMonth");
                setTempMonthlyMaxApplicableMonth(DEFAULT_APPLICABLE_PERIOD);
            }

            try {
                if(getMonth()==tempMonthlyMaxApplicableMonth) {
                    tempMonthlyMax = verifyInts(settings.getElementsByTagName("tempMonthlyMax").item(0).getTextContent(),
                            maxMonthlyTally, "tempMonthlyMax");
                }else{
                    setTempMonthlyMax(maxMonthlyTally);
                }
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
                startDate = verifyDate(settings.getElementsByTagName("startDate").item(0).getTextContent(),
                        setLocalDate(), "startDate");

            } catch (Exception e) {
                replaceMissingElement(doc, parentElement, "startDate");
                setStartDate(setLocalDate());
            }

            try {
                weeksToReminder = verifyInts(settings.getElementsByTagName("weeksUntilEnd").item(0).getTextContent(), DEFAULT_WEEKS_TO_REMINDER, "weeksUntilEnd");
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
                noNagOverwriteAssignmentChanges = Boolean.parseBoolean(settings.getElementsByTagName("overwriteAssignmentChanges").item(0).getTextContent());
            } catch (Exception e) {
                replaceMissingElement(doc, parentElement, "overwriteAssignmentChanges");
            }

            try {
                noNagSaveWithEmptyAssignments = Boolean.parseBoolean(settings.getElementsByTagName("saveWithEmptyAssignments").item(0).getTextContent());
            }catch(Exception e){
                replaceMissingElement(doc, parentElement, "saveWithEmptyAssignments");
            }

            try {
                noNagOverwriteSave = Boolean.parseBoolean(settings.getElementsByTagName("overwriteSave").item(0).getTextContent());
            }catch(Exception e){
                replaceMissingElement(doc, parentElement, "overwriteSave");
            }

        }catch (Exception e){
            replaceMissingNoRemindersElement(doc, settings);
        }

        try {
            parentElement = (Element) settings.getElementsByTagName("inputFilePaths").item(0);

            try {
                masterSchedulePath = settings.getElementsByTagName("masterSchedule").item(0).getTextContent();
            }catch(Exception e){
                replaceMissingElement(doc, parentElement, "masterSchedule");
            }

            try {
                absenceInputPath = settings.getElementsByTagName("absences").item(0).getTextContent();
            }catch(Exception e){
                replaceMissingElement(doc, parentElement, "absences");
            }

            try {
                supplyTeacherPath = settings.getElementsByTagName("supply").item(0).getTextContent();
            }catch (Exception e){
                replaceMissingElement(doc, parentElement, "supply");
            }

            try {
                courseCodesPath = settings.getElementsByTagName("courses").item(0).getTextContent();
            }catch(Exception e){
                replaceMissingElement(doc, parentElement, "courses");
            }
        }catch(Exception e){
            replaceMissingInputFilePathsElement(doc, settings);
        }

        try {
            parentElement = (Element) settings.getElementsByTagName("output").item(0);

            try {
                onCallerFormPath = settings.getElementsByTagName("onCallerFormsDir").item(0).getTextContent();
            }catch (Exception e){
                replaceMissingElement(doc, parentElement, "onCallerFormsDir");
                setOnCallerFormPath(DEFAULT_ON_CALLER_FORM_PATH);
            }
            try {
                onCallerFormNameFormat = settings.getElementsByTagName("onCallerNameFormat").item(0).getTextContent();
            }catch (Exception e){
                replaceMissingElement(doc, parentElement, "onCallerNameFormat");
                setOnCallerFormNameFormat(DEFAULT_ON_CALLER_FORM_NAME_FORMAT);
            }
        }catch (Exception e){
            replaceMissingOutputElement(doc, settings);
        }

    }

    //ACCESSORS

    public static Integer getMaxWeeklyTally() throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        return maxWeeklyTally;
    }

    public static Integer getMaxMonthlyTally() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        confirmInit();
        return maxMonthlyTally;
    }

    public static Integer getTempWeeklyMax() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        confirmInit();
        return tempWeeklyMax;
    }

    public static Integer getTempMonthlyMax() throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        return tempMonthlyMax;
    }

    public static LocalDate getStartDate() throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        return startDate;
    }

    public static Integer getWeeksToReminder() throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        return weeksToReminder;
    }

    public static Boolean isNoNagOverwriteAssignmentChanges() throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        return noNagOverwriteAssignmentChanges;
    }

    public static Boolean isNoNagSaveWithEmptyAssignments() throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        return noNagSaveWithEmptyAssignments;
    }

    public static Boolean isNoNagOverwriteSave() throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        return noNagOverwriteSave;
    }

    public static String getMasterSchedulePath() throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        return masterSchedulePath;
    }

    public static String getAbsenceInputPath() throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        return absenceInputPath;
    }

    public static String getSupplyTeacherPath() throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        return supplyTeacherPath;
    }

    public static String getCourseCodesPath() throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        return courseCodesPath;
    }

    public static String getOnCallerFormPath() throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        return onCallerFormPath;
    }

    public static String getOnCallerFormNameFormat() throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        return onCallerFormNameFormat;
    }

    //MUTATORS

    public static void setMaxWeeklyTally(Integer maxWeeklyTallyIn) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        maxWeeklyTally = maxWeeklyTallyIn;
        writeXML(maxWeeklyTally.toString(), "weeklyMax");
    }

    public static void setMaxMonthlyTally(Integer maxMonthlyTallyIn) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        maxMonthlyTally = maxMonthlyTallyIn;
        writeXML(maxMonthlyTally.toString(),"monthlyMax");
    }

    public static void setTempWeeklyMax(Integer tempWeeklyMaxIn) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        tempWeeklyMax = tempWeeklyMaxIn;
        setTempWeeklyMaxApplicableWeek(getWeek());
        writeXML(tempWeeklyMax.toString(), "tempWeeklyMax");
    }

    public static void setTempMonthlyMax(Integer tempMonthlyMaxIn) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        tempMonthlyMax = tempMonthlyMaxIn;
        setTempMonthlyMaxApplicableMonth(getMonth());
        writeXML(tempMonthlyMax.toString(), "tempMonthlyMax");
    }

    private static void setTempWeeklyMaxApplicableWeek(Integer newVal) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        tempWeeklyMaxApplicableWeek = newVal;
        writeXML(tempWeeklyMaxApplicableWeek.toString(), "tempWeeklyMaxApplicableWeek");
    }

    private static void setTempMonthlyMaxApplicableMonth(Integer newVal) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        tempMonthlyMaxApplicableMonth = newVal;
        writeXML(tempMonthlyMaxApplicableMonth.toString(), "tempMonthlyMaxApplicableMonth");
    }

    public static void setStartDate(LocalDate startDateIn) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        startDate = startDateIn;
        writeXML(startDate.toString(), "startDate");
    }

    public static void setWeeksToReminder(Integer weeksToReminderIn) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        weeksToReminder = weeksToReminderIn;
        writeXML(weeksToReminder.toString(), "weeksUntilEnd");
    }

    public static void setNoNagOverwriteAssignmentChanges(Boolean noNagOverwriteAssignmentChangesIn) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        noNagOverwriteAssignmentChanges = noNagOverwriteAssignmentChangesIn;
        writeXML(noNagOverwriteAssignmentChanges.toString(), "overwriteAssignmentChanges");
    }

    public static void setNoNagSaveWithEmptyAssignments(Boolean noNagSaveWithEmptyAssignmentsIn) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        noNagSaveWithEmptyAssignments = noNagSaveWithEmptyAssignmentsIn;
        writeXML(noNagSaveWithEmptyAssignments.toString(), "saveWithEmptyAssignments");
    }

    public static void setNoNagOverwriteSave(Boolean noNagOverwriteSaveIn) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        noNagOverwriteSave = noNagOverwriteSaveIn;
        writeXML(noNagOverwriteSave.toString(), "overwriteSave");
    }

    public static void setMasterSchedulePath(String masterSchedulePathIn) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        masterSchedulePath = masterSchedulePathIn;
        writeXML(masterSchedulePath, "masterSchedule");
    }

    public static void setAbsenceInputPath(String absenceInputPathIn) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        absenceInputPath = absenceInputPathIn;
        writeXML(absenceInputPath, "absences");
    }

    public static void setSupplyTeacherPath(String supplyTeacherPathIn) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        supplyTeacherPath = supplyTeacherPathIn;
        writeXML(supplyTeacherPath, "supply");
    }

    public static void setCourseCodesPath(String courseCodesPathIn) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        courseCodesPath = courseCodesPathIn;
        writeXML(courseCodesPath, "courses");
    }

    public static void setOnCallerFormPath(String onCallerFormPathIn) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        onCallerFormPath = onCallerFormPathIn;
        writeXML(onCallerFormPath, "onCallerFormsDir");
    }

    public static void setOnCallerFormNameFormat(String onCallerFormNameFormatIn) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        confirmInit();
        onCallerFormNameFormat = onCallerFormNameFormatIn;
        writeXML(onCallerFormNameFormat, "onCallerNameFormat");
    }

    //FAULT TOLERANCE

    private static void confirmInit() throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        if(!isInit){
            init();
        }
    }

    private static Integer verifyInts(String integer, int defaultVal, String key) throws ParserConfigurationException, SAXException, IOException, TransformerException
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

    private static LocalDate verifyDate(String date, LocalDate defaultVal, String key) throws ParserConfigurationException, SAXException, IOException, TransformerException
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

    private static LocalDate setLocalDate(){
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

    private static Document replaceMissingDocument() throws IOException, SAXException, ParserConfigurationException, TransformerException
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

    private static void replaceMissingOnCallsElement(Document doc, Element parent) throws IOException, SAXException, ParserConfigurationException, TransformerException
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
        ele = doc.createElement("tempWeeklyMaxApplicableWeek");
        parent.appendChild(ele);
        ele = doc.createElement("tempMonthlyMaxApplicableMonth");
        parent.appendChild(ele);
        writeXML(doc);
        setMaxWeeklyTally(DEFAULT_MAX_WEEKLY_TALLY);
        setMaxMonthlyTally(DEFAULT_MAX_WEEKLY_TALLY);
        setTempWeeklyMax(maxWeeklyTally);
        setTempMonthlyMax(maxMonthlyTally);
        setTempWeeklyMaxApplicableWeek(DEFAULT_APPLICABLE_PERIOD);
        setTempMonthlyMaxApplicableMonth(DEFAULT_APPLICABLE_PERIOD);
    }

    private static void replaceMissingStartEndDatesElement(Document doc, Element parent) throws IOException, SAXException, ParserConfigurationException, TransformerException
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

    private static void replaceMissingNoRemindersElement(Document doc, Element parent) throws TransformerException
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

    private static void replaceMissingInputFilePathsElement(Document doc, Element parent) throws TransformerException
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

    private static void replaceMissingOutputElement(Document doc, Element parent) throws IOException, SAXException, ParserConfigurationException, TransformerException {
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

    private static void replaceMissingElement(Document doc, Element parentElement, String key) throws TransformerException
    {
        Element ele = doc.createElement(key);
        parentElement.appendChild(ele);
        writeXML(doc);
    }

    //OTHER HELPERS

    private static Integer getWeek(){
        WeekFields week = WeekFields.of(Locale.getDefault());
        Integer weekNum = LocalDate.now().get(week.weekOfWeekBasedYear());
        return weekNum;
    }

    private static Integer getMonth(){
        return LocalDate.now().getMonthValue();
    }

    private static Document getDocument(String filePath) throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = docBF.newDocumentBuilder();
        Document doc;
        doc = db.parse(filePath);
        doc.getDocumentElement().normalize();
        return doc;
    }

    private static void writeXML(String input, String childElement) throws ParserConfigurationException, SAXException, IOException, TransformerException
    {
        Document doc = getDocument(FILEPATH);
        Node out = doc.getElementsByTagName(childElement).item(0);
        out.setTextContent(input);

        writeXML(doc);
    }

    private static void writeXML(Document doc) throws TransformerException
    {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(FILEPATH));
        transformer.transform(source, result);
    }
} // class