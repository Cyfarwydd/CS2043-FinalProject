package AssignSubstitutes;


import AssignSubstitutes.classes.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class IOTest {
    //formats on caller file names based on user config
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

        //remove illegal filename characters
        //retVal = retVal.replaceAll("(\\\\|/|\\?|%|\\.|\\||\"|<|>|\\.| |:)", "");
        retVal = retVal.replaceAll("(\\\\|/|\\?|%|\\.|\\||\"|<|>|\\.| |:)", "");

        System.out.println(retVal);
    }

    //creates OnStaffTeachers and period objects
    public static ArrayList<OnStaffTeacher> getTeachers(){
        Random random = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        int np = 5;
        ArrayList<OnStaffTeacher> osTeachers=new ArrayList<>();
        Period[] periods=new Period[5];
        periods[0]=new Period("class", "Math", 1, "100", false);
        periods[1]=new Period("class", "Math", 2, "100", false);
        periods[2]=new Period(null, null, 3, "100", false);
        periods[3]=new Period("lunch", null, 4, "100", false);
        periods[4]=new Period("class", "Math", 5, "100", false);
        OnStaffTeacher teacher = new OnStaffTeacher("Dawn MacIsaac", periods, "Math");
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementTally();
            teacher.incrementMonthlyTally();
        }
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementMonthlyTally();
        }
        osTeachers.add(teacher);
        periods=new Period[np];
        periods[0]=new Period(null, null, 1, "100", false);
        periods[1]=new Period("class", "Science", 2, "100", false);
        periods[2]=new Period("lunch", null, 3, "100", false);
        periods[3]=new Period("class", "Science", 4, "100", false);
        periods[4]=new Period("class", "Science", 5, "100", false);
        teacher = new OnStaffTeacher("Jimmy Mac", periods, "Science");
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementTally();
            teacher.incrementMonthlyTally();
        }
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementMonthlyTally();
        }
        osTeachers.add(teacher);
        periods=new Period[np];
        periods[0]=new Period("class", "English", 1, "100", true);
        periods[1]=new Period(null, null, 2, "100", true);
        periods[2]=new Period("class", "English", 3, "100", true);
        periods[3]=new Period("lunch", null, 4, "100", true);
        periods[4]=new Period("class", "English", 5, "100", true);
        teacher = new OnStaffTeacher("Johnny Mnemonic", periods, "English");
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementTally();
            teacher.incrementMonthlyTally();
        }
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementMonthlyTally();
        }
        osTeachers.add(teacher);
        periods=new Period[np];
        periods[0]=new Period("class", "Math", 1, "100", false);
        periods[1]=new Period("class", "Math", 2, "100", false);
        periods[2]=new Period(null, null, 3, "100", false);
        periods[3]=new Period("lunch", null, 4, "100", false);
        periods[4]=new Period("class", "Math", 5, "100", false);
        teacher = new OnStaffTeacher("Jane Doe", periods, "Math");
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementTally();
            teacher.incrementMonthlyTally();
        }
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementMonthlyTally();
        }
        osTeachers.add(teacher);
        periods=new Period[np];
        periods[0]=new Period("class", "Science", 1, "100", false);
        periods[1]=new Period("class", "Science", 2, "100", false);
        periods[2]=new Period("lunch", null, 3, "100", false);
        periods[3]=new Period(null, null, 4, "100", false);
        periods[4]=new Period("class", "Science", 5, "100", false);
        teacher = new OnStaffTeacher("John Doe", periods, "Science");
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementTally();
            teacher.incrementMonthlyTally();
        }
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementMonthlyTally();
        }
        osTeachers.add(teacher);
        periods=new Period[np];
        periods[0]=new Period("class", "English", 1, "100", false);
        periods[1]=new Period("class", "English", 2, "100", false);
        periods[2]=new Period("class", "English", 3, "100", false);
        periods[3]=new Period("lunch", null, 4, "100", false);
        periods[4]=new Period(null, null, 5, "100", false);
        teacher = new OnStaffTeacher("Stephen Cormier", periods, "English");
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementTally();
            teacher.incrementMonthlyTally();
        }
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementMonthlyTally();
        }
        osTeachers.add(teacher);
        periods=new Period[np];
        periods[0]=new Period(null, null, 1, "100", true);
        periods[1]=new Period("class", "Math", 2, "100", true);
        periods[2]=new Period("lunch", null, 3, "100", true);
        periods[3]=new Period("class", "Math", 4, "100", true);
        periods[4]=new Period("class", "Math", 5, "100", true);
        teacher = new OnStaffTeacher("Trish Knockwood", periods, "Math");
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementTally();
            teacher.incrementMonthlyTally();
        }
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementMonthlyTally();
        }
        osTeachers.add(teacher);
        return osTeachers;
    }

    //selects teachers that are absent and returns them
    public static ArrayList<OnStaffTeacher> getAbsences(ArrayList<OnStaffTeacher> osTeachers){
        ArrayList<OnStaffTeacher> absences = new ArrayList<OnStaffTeacher>();
        for(int i = 0; i < osTeachers.size(); i++){
            for(int j = 0; j < osTeachers.get(i).getSchedule().length; j++){
                if (osTeachers.get(i).getSchedule()[j].Absent()){
                    absences.add(osTeachers.get(i));
                    break;
                }
            }
        }
        return absences;
    }

    //creates supply teachers and period objects
    public static ArrayList<Teacher> getSupplies(){
        ArrayList<Teacher> supplies = new ArrayList<Teacher>();
        Period p[];
        //p = getSupplyPeriod();
        //supplies.add(new Teacher("Jane Foster", p, "Math"));
        p = getSupplyPeriod();

        supplies.add(new SupplyTeacher("Jane Fondant", p, "Eng"));
        p = getSupplyPeriod();
        supplies.add(new SupplyTeacher("Mark Zuckerberg", p, "Sci"));

        return supplies;
    }
    public static Period[] getSupplyPeriod(){
        Period p[] = new Period[5];
        int rand = (int)(Math.random() * 10);
        for(int i = 0; i < p.length; i++) {
            if((rand%2==0 && i == 3)||(rand%2 ==1 && i==2)){
                Period q = new Period("Lunch", null, i, "100", false);
                p[i] = q;
            }else{
                Period q = new Period(null, null, i, "100", false);
                p[i] = q;
            }
        }
        return p;
    }

    //simulates getting an assignment to display based on the date (to be read in from a file)
    public static ArrayList<Assignment> getAssignmentByDate(LocalDate date, ArrayList<OnStaffTeacher> osTeachers){
        ArrayList<Assignment> retVal = new ArrayList<>();
        if(date.equals(LocalDate.of(2018, 3, 6))){
            retVal.add(new Assignment(osTeachers.get(0), osTeachers.get(3), osTeachers.get(3).getSchedule()[0]));
            retVal.add(new Assignment(osTeachers.get(1), osTeachers.get(4), osTeachers.get(4).getSchedule()[0]));
            retVal.add(new Assignment(osTeachers.get(2), osTeachers.get(5), osTeachers.get(5).getSchedule()[0]));
        }
        return retVal;
    }
}
