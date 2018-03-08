package AssignSubstitutes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ThePointlessClassIMade {
    private void StringFormatReplacement(String format, String teacherName){
        DateTimeFormatter dfLongYear = DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter dfShortYear = DateTimeFormatter.ofPattern("yy");
        DateTimeFormatter dfLongMonth = DateTimeFormatter.ofPattern("MMMM");
        DateTimeFormatter dfAbrMonth = DateTimeFormatter.ofPattern("MMM");
        DateTimeFormatter dfNumMonth = DateTimeFormatter.ofPattern("MM");
        DateTimeFormatter dfNumNoPadMonth = DateTimeFormatter.ofPattern("M");
        DateTimeFormatter dfDay = DateTimeFormatter.ofPattern("dd");
        DateTimeFormatter dfNoPadDay = DateTimeFormatter.ofPattern("d");
        LocalDateTime now = LocalDateTime.now();
        String retVal;
        retVal = format.replaceAll("\\[Name\\]", teacherName);
        retVal = retVal.replaceAll("\\[NameUp\\]", teacherName.toUpperCase());
        retVal = retVal.replaceAll("\\[YYYY\\]", dfLongYear.format(now));
        retVal = retVal.replaceAll("\\[YY\\]", dfShortYear.format(now));
        retVal = retVal.replaceAll("\\[MMMMUp\\]", dfLongMonth.format(now).toUpperCase());
        retVal = retVal.replaceAll("\\[MMMUp\\]", dfAbrMonth.format(now).toUpperCase());
        retVal = retVal.replaceAll("\\[MMMM\\]", dfLongMonth.format(now));
        retVal = retVal.replaceAll("\\[MMM\\]", dfAbrMonth.format(now));
        retVal = retVal.replaceAll("\\[MM\\]", dfNumMonth.format(now));
        retVal = retVal.replaceAll("\\[M\\]", dfNumNoPadMonth.format(now));
        retVal = retVal.replaceAll("\\[DD\\]", dfDay.format(now));
        retVal = retVal.replaceAll("\\[D\\]", dfNoPadDay.format(now));
        System.out.println(retVal);
    }
}
