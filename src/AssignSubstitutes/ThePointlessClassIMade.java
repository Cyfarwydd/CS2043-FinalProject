package AssignSubstitutes;


import AssignSubstitutes.InputOutput.XMLParser;

import AssignSubstitutes.classes.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

        //remove illegal filename characters
        //retVal = retVal.replaceAll("(\\\\|/|\\?|%|\\.|\\||\"|<|>|\\.| |:)", "");
        retVal = retVal.replaceAll("(\\\\|/|\\?|%|\\.|\\||\"|<|>|\\.| |:)", "");

        System.out.println(retVal);
    }

    //For test use until IO is available
    //creates teachers and period objects
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

    //For test use until InformationHandle is available
    //creates teachers and period objects
    public static ArrayList<Assignment> getAssignmentsFacsimile(ArrayList<OnStaffTeacher> osTeachers){
        ArrayList<Assignment> assignments = new ArrayList<>();
        assignments.add(new Assignment(osTeachers.get(3), osTeachers.get(0), osTeachers.get(3).getSchedule()[1]));
        assignments.add(new Assignment(osTeachers.get(6), osTeachers.get(1), osTeachers.get(6).getSchedule()[1]));
        return assignments;
    }

    //For test use until InformationHandle is available
    //gets a list of available teachers for a given period
    public static List<Teacher> getAssignableTeacherList(Collection<OnStaffTeacher> fullTeacherList,
                                                         ObservableList<Assignment>
                                                                  assignments, int periodNumber, Assignment
                                                             currentAssignment){
        System.out.println("Period " + periodNumber);
        System.out.println();
        System.out.println("Current Assignment "+currentAssignment.getAbsentee()+"\t"+currentAssignment.getSubstitute
                ()+"\t"+currentAssignment.getPeriod().getPeriodNumber());
        System.out.println("On-staff teachers");
        System.out.println("/////////////////");
        for(Teacher t: fullTeacherList) {
            System.out.print(t+" ");
            for(Period p : t.getSchedule()){
                System.out.print(p.getPeriodNumber()+" ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Assignments");
        System.out.println("/////////////////");
        for(Assignment a : assignments){
            System.out.println(a.getAbsentee()+"\t"+a.getSubstitute()+"\t"+a.getPeriod().getPeriodNumber());
            System.out.println();
        }
        System.out.println();

        System.out.println("All teachers without a class this period");
        System.out.println("/////////////////");
        List<Teacher> noneThisPeriod = fullTeacherList.stream().filter(
                teacher -> Arrays.stream(teacher.getSchedule()).noneMatch(period->period.getPeriodNumber()
                            ==periodNumber)
        ).collect(Collectors.toList());
        for(Teacher t: noneThisPeriod) {
            System.out.print(t+" ");
            for(Period p : t.getSchedule()){
                System.out.print(p.getPeriodNumber()+" ");
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("Teachers without a class this period and not already assigned");
        System.out.println("/////////////////");
        List<Teacher> notAssigned = noneThisPeriod.stream().filter(
                teacher -> (assignments.stream().noneMatch(
                        assignment -> assignment.getSubstitute().equals(teacher) &&
                                        assignment.getPeriod().getPeriodNumber() == periodNumber &&
                                        !assignment.equals( currentAssignment )
                        )
        )).collect(Collectors.toList());
        for(Teacher t: notAssigned) {
            System.out.print(t+" ");
            for(Period p : t.getSchedule()){
                System.out.print(p.getPeriodNumber()+" ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("Teachers without a class this period, not already assigned and not absent");
        System.out.println("/////////////////");
        List<Teacher> notAbsent = notAssigned.stream().filter(
                teacher -> (
                        assignments.stream().noneMatch(
                                assignment -> assignment.getAbsentee().equals(teacher) &&
                                        assignment.getPeriod().getPeriodNumber()!=periodNumber

                        )
                )
        ).collect(Collectors.toList());
        Period[] schedule = {new Period(null, null, periodNumber, "0", false), new Period
                (null, null, periodNumber, "0", false), new Period(null, null, periodNumber, "0", false), new
                Period(null, null, periodNumber, "0", false)};
        Teacher nullTeacher=new OnStaffTeacher(null, null, null);
        notAbsent.add(0, nullTeacher);
        for(Teacher t: notAbsent) {
            System.out.print(t + " ");
            /*for (Period p : t.getSchedule()) {
                System.out.print(p.getPeriodNumber() + " ");
            }*/
            System.out.println();
        }
        return notAbsent;
    }
    //For test use until InformationHandle is available
    //gets a list of available teachers for a given period
    public static ObservableList<ArrayList<Object>> getAvailabilityStats(Collection<OnStaffTeacher> fullTeacherList) throws Exception{
        ObservableList<ArrayList<Object>> periods= FXCollections.observableArrayList();
        XMLParser settings = new XMLParser("./config");
        int maxMonthly = settings.getTempMonthlyMax();
        int maxWeekly = settings.getTempWeeklyMax();
        for(int i = 0; i<5; i++) {
            ArrayList<Object> period = new ArrayList();
            int periodNumber = i+1;
            String periodStr = "Period ";
            switch(i){
                case 0:
                case 1:
                    period.add(new String(periodStr+periodNumber));
                    break;
                case 2:
                    period.add(new String(periodStr+periodNumber+"A"));
                    break;
                case 3:
                    period.add(new String(periodStr+i+"B"));
                    break;
                case 4:
                    period.add(new String(periodStr+i));
                    break;
                default:
                    throw new Exception("Period out of bounds");
            }

            List<OnStaffTeacher> noneThisPeriod = fullTeacherList.stream().filter(
                    teacher -> Arrays.stream(teacher.getSchedule()).noneMatch(p -> p.getPeriodNumber()
                            == periodNumber)
            ).collect(Collectors.toList());

            List<OnStaffTeacher> weekTeachers = noneThisPeriod.stream().filter(t-> t.getWeeklyTally() < maxWeekly).collect(Collectors.toList());
            List<OnStaffTeacher> monthTeachers = weekTeachers.stream().filter(t-> t.getWeeklyTally() < maxMonthly).collect(Collectors.toList());

            int size = weekTeachers.size();
            OnStaffTeacher weekAvailTeacher = new OnStaffTeacher(size + " teachers",null,null);
            weekTeachers.add(0, weekAvailTeacher);
            period.add(weekTeachers);

            size = monthTeachers.size();
            OnStaffTeacher monthAvailTeacher = new OnStaffTeacher(size + " teachers",null,null);
            monthTeachers.add(0, monthAvailTeacher);
            period.add(monthTeachers);

            periods.add(period);
        }
        return periods;
    }
    //For test use until IO is available
    //creates teachers and period objects
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