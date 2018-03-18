package AssignSubstitutes;

import AssignSubstitutes.InputOutput.xmlParser;
import AssignSubstitutes.classes.Assignment;
import AssignSubstitutes.classes.OnStaffTeacher;
import AssignSubstitutes.classes.Period;
import AssignSubstitutes.classes.Teacher;
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
        ArrayList<OnStaffTeacher> osTeachers=new ArrayList<>();
        Period[] periods=new Period[4];
        periods[0]=new Period(null, null, 1, 100, false);
        periods[1]=new Period(null, null, 2, 100, false);
        periods[2]=new Period(null, null, 4, 100, false);
        periods[3]=new Period(null, null, 5, 100, false);
        OnStaffTeacher teacher = new OnStaffTeacher("Dawn MacIsaac", periods, null);
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementTally();
            teacher.incrementMonthlyTally();
        }
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementMonthlyTally();
        }
        osTeachers.add(teacher);
        periods=new Period[4];
        periods[0]=new Period(null, null, 1, 100, false);
        periods[1]=new Period(null, null, 2, 100, false);
        periods[2]=new Period(null, null, 4, 100, false);
        periods[3]=new Period(null, null, 5, 100, false);
        teacher = new OnStaffTeacher("Jimmy Mac", periods, null);
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementTally();
            teacher.incrementMonthlyTally();
        }
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementMonthlyTally();
        }
        osTeachers.add(teacher);
        periods=new Period[4];
        periods[0]=new Period(null, null, 1, 100, true);
        periods[1]=new Period(null, null, 2, 100, true);
        periods[2]=new Period(null, null, 4, 100, true);
        periods[3]=new Period(null, null, 5, 100, true);
        teacher = new OnStaffTeacher("Johnny Mnemonic", periods, null);
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementTally();
            teacher.incrementMonthlyTally();
        }
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementMonthlyTally();
        }
        osTeachers.add(teacher);
        periods=new Period[4];
        periods[0]=new Period(null, null, 2, 100, false);
        periods[1]=new Period(null, null, 3, 100, false);
        periods[2]=new Period(null, null, 4, 100, false);
        periods[3]=new Period(null, null, 5, 100, false);
        teacher = new OnStaffTeacher("Jane Doe", periods, null);
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementTally();
            teacher.incrementMonthlyTally();
        }
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementMonthlyTally();
        }
        osTeachers.add(teacher);
        periods=new Period[4];
        periods[0]=new Period(null, null, 2, 100, false);
        periods[1]=new Period(null, null, 3, 100, false);
        periods[2]=new Period(null, null, 4, 100, false);
        periods[3]=new Period(null, null, 5, 100, false);
        teacher = new OnStaffTeacher("John Doe", periods, null);
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementTally();
            teacher.incrementMonthlyTally();
        }
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementMonthlyTally();
        }
        osTeachers.add(teacher);
        periods=new Period[4];
        periods[0]=new Period(null, null, 2, 100, false);
        periods[1]=new Period(null, null, 3, 100, false);
        periods[2]=new Period(null, null, 4, 100, false);
        periods[3]=new Period(null, null, 5, 100, false);
        teacher = new OnStaffTeacher("Stephen Cormier", periods, null);
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementTally();
            teacher.incrementMonthlyTally();
        }
        for(int i=0; i < random.nextInt(3); i++){
            teacher.incrementMonthlyTally();
        }
        osTeachers.add(teacher);
        periods=new Period[4];
        periods[0]=new Period(null, null, 2, 100, true);
        periods[1]=new Period(null, null, 3, 100, true);
        periods[2]=new Period(null, null, 4, 100, true);
        periods[3]=new Period(null, null, 5, 100, true);
        teacher = new OnStaffTeacher("Trish Knockwood", periods, null);
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
    public static ArrayList<Teacher> getAbsences(ArrayList<OnStaffTeacher> osTeachers){
        ArrayList<Teacher> absences = new ArrayList<Teacher>();
        for(int i = 0; i < 4; i++) {
            double rand = Math.random() * 10;
            int r = (int) rand;
            if(r >= 7){
                i--;
                continue;
            }
            Teacher t = osTeachers.get(r);
            absences.add(t);
        }
        return absences;
    }

    public static ArrayList<Teacher> getSupplies(){
        ArrayList<Teacher> supplies = new ArrayList<Teacher>();
        Period p[] = new Period[4];
        supplies.add(new Teacher("Jane Foster", p, "Math"));
        p = new Period[4];
        supplies.add(new Teacher("Jane Fondant", p, "Eng"));
        p = new Period[4];
        supplies.add(new Teacher("Mark Zuckerberg", p, "Sci"));
        return supplies;
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
        Period[] schedule = {new Period(null, null, periodNumber, 0, false), new Period
                (null, null, periodNumber, 0, false), new Period(null, null, periodNumber, 0, false), new
                Period(null, null, periodNumber, 0, false)};
        Teacher nullTeacher=new Teacher(null, null, null);
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
    public static ObservableList<ArrayList<String>> getAvailabilityByPeriod(Collection<OnStaffTeacher> fullTeacherList){
        ObservableList<ArrayList<String>> periods= FXCollections.observableArrayList();
        xmlParser settings = new xmlParser("./config");
        int maxMonthly = settings.getTempMonthlyMax();
        int maxWeekly = settings.getTempWeeklyMax();
        for(int i = 0; i<5; i++) {
            ArrayList<String> period = new ArrayList();
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
            }
            List<OnStaffTeacher> noneThisPeriod = fullTeacherList.stream().filter(
                    teacher -> Arrays.stream(teacher.getSchedule()).noneMatch(p -> p.getPeriodNumber()
                            == periodNumber)
            ).collect(Collectors.toList());

            period.add(Long.toString(noneThisPeriod.stream().filter(t-> t.getWeeklyTally() < maxWeekly).count()));
            period.add(Long.toString(noneThisPeriod.stream().filter(t-> t.getWeeklyTally() < maxMonthly).count()));
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