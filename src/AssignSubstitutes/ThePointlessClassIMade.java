package AssignSubstitutes;

import AssignSubstitutes.classes.Assignment;
import AssignSubstitutes.classes.OnStaffTeacher;
import AssignSubstitutes.classes.Period;
import AssignSubstitutes.classes.Teacher;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
        ArrayList<OnStaffTeacher> osTeachers=new ArrayList<>();
        Period[] periods=new Period[4];
        periods[0]=new Period(null, null, 1, 100, false);
        periods[1]=new Period(null, null, 2, 100, false);
        periods[2]=new Period(null, null, 4, 100, false);
        periods[3]=new Period(null, null, 5, 100, false);
        osTeachers.add(new OnStaffTeacher("Dawn MacIsaac", periods, null));
        periods=new Period[4];
        periods[0]=new Period(null, null, 1, 100, false);
        periods[1]=new Period(null, null, 2, 100, false);
        periods[2]=new Period(null, null, 4, 100, false);
        periods[3]=new Period(null, null, 5, 100, false);
        osTeachers.add(new OnStaffTeacher("Jimmy Mac", periods, null));
        periods=new Period[4];
        periods[0]=new Period(null, null, 1, 100, true);
        periods[1]=new Period(null, null, 2, 100, true);
        periods[2]=new Period(null, null, 4, 100, true);
        periods[3]=new Period(null, null, 5, 100, true);
        osTeachers.add(new OnStaffTeacher("Johnny Mnemonic", periods, null));
        periods=new Period[4];
        periods[0]=new Period(null, null, 2, 100, false);
        periods[1]=new Period(null, null, 3, 100, false);
        periods[2]=new Period(null, null, 4, 100, false);
        periods[3]=new Period(null, null, 5, 100, false);
        osTeachers.add(new OnStaffTeacher("Jane Doe", periods, null));
        periods=new Period[4];
        periods[0]=new Period(null, null, 2, 100, false);
        periods[1]=new Period(null, null, 3, 100, false);
        periods[2]=new Period(null, null, 4, 100, false);
        periods[3]=new Period(null, null, 5, 100, false);
        osTeachers.add(new OnStaffTeacher("John Doe", periods, null));
        periods=new Period[4];
        periods[0]=new Period(null, null, 2, 100, false);
        periods[1]=new Period(null, null, 3, 100, false);
        periods[2]=new Period(null, null, 4, 100, false);
        periods[3]=new Period(null, null, 5, 100, false);
        osTeachers.add(new OnStaffTeacher("Stephen Cormier", periods, null));
        periods=new Period[4];
        periods[0]=new Period(null, null, 2, 100, true);
        periods[1]=new Period(null, null, 3, 100, true);
        periods[2]=new Period(null, null, 4, 100, true);
        periods[3]=new Period(null, null, 5, 100, true);
        osTeachers.add(new OnStaffTeacher("Trish Knockwood", periods, null));
        return osTeachers;
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
    public static List<Teacher> getAvailableTeachers(Collection<OnStaffTeacher> fullTeacherList,
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
                                        assignment.getPeriod().getPeriodNumber() == periodNumber /*&&
                                        !assignment.equals( currentAssignment )*/
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
        for(Teacher t: notAbsent) {
            System.out.print(t+" ");
            for(Period p : t.getSchedule()){
                System.out.print(p.getPeriodNumber()+" ");
            }
            System.out.println();
        }
        return notAbsent;
        /*return fullTeacherList.stream().filter(
                teacher -> Arrays.stream(teacher.getSchedule()).noneMatch(period->period.getPeriodNumber()==periodNumber)
                ).filter(
                teacher -> (
                        assignments.stream().noneMatch(
                                assignment -> assignment.getSubstitute().equals(teacher) &&
                                        assignment.getPeriod().getPeriodNumber()==periodNumber
                        )
                )
        ).collect(Collectors.toList());*/
    }
}