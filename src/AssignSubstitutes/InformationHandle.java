package AssignSubstitutes;


import AssignSubstitutes.InputOutput.XMLParser;
import AssignSubstitutes.classes.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class InformationHandle{
    //TODO: replace NUM_PERIODS, M_TAL, W_TAL with actual variables
	public static final int NUM_PERIODS = 5;
	private static final int M_TAL = 4;
	private static final int W_TAL = 2;
	public static ArrayList<Assignment> generateAssignments(ArrayList<OnStaffTeacher> roster, ArrayList<SupplyTeacher> supply, ArrayList<OnStaffTeacher> absent){
	    ArrayList<Assignment> assignments = new ArrayList<Assignment>();

		for(int i = 0; i < absent.size(); i++){
		    for(int j = 0; j < absent.get(i).getSchedule().length; j++){
		        if((absent.get(i).getSchedule()[j].Absent()
                        && (absent.get(i).getSchedule()[j].getCourse() != null)
                        && !absent.get(i).getSchedule()[j].getCourse().equals("lunch"))){
		            Assignment a = new Assignment(absent.get(i), absent.get(i).getSchedule()[j]);
		            assignments.add(a);
                }
            }
        }

        for(int i = 0; i < assignments.size(); i++){
		    //grab the assignment, see if a supply is available for this period. If there is, assign them. Otherwise,
            //grab the next best os and assign them.
            Assignment a = assignments.get(i);
            if(supplyFree(supply, a, assignments)){
                assignSupply(a, supply, assignments);
            }else{
                assignOS(a, roster, assignments, absent);
            }
        }
		return assignments;
	}
	private static boolean supplyFree(ArrayList<SupplyTeacher> supply, Assignment a, ArrayList<Assignment> assignments){
	    boolean freeTeacher = false;
	    for(int i = 0; i < supply.size(); i++){
	        if(supply.get(i).checkForSpare(a.getPeriod())){
	            freeTeacher = true;
            }
        }
        return freeTeacher;
    }
    private static void assignOS(Assignment a, ArrayList<OnStaffTeacher> roster, ArrayList<Assignment> assignments, ArrayList<OnStaffTeacher> abs){
        roster = sortSubs(roster);
        int wt = roster.get(0).getWeeklyTally();
        OnStaffTeacher best = roster.get(0);
        for(int j = 1; j < roster.size(); j++) {
            if (checkIfAssigned(a, roster.get(j), assignments)
                    && checkIfAbsent(roster.get(j), abs)
                    && roster.get(j).checkForSpare(a.getPeriod())) {
                best = roster.get(j);
            }
        }
        a.setSubstitute(best);	
    }
    private static void assignSupply(Assignment a, ArrayList<SupplyTeacher> supply, ArrayList<Assignment> assignments){
        if(supply.size()>0) {
            for (int i = 0; i < supply.size(); i++) {
                Teacher newTeach = supply.get(i);

                for (int j = 0; j < newTeach.getSchedule().length; j++) {
                    if (newTeach.checkForSpare(a.getPeriod())
                            && checkIfAssigned(a, newTeach, assignments)) {
                        a.setSubstitute(newTeach);
                        return;
                    }
                }
            }
            if (a.getSubstitute() == null) {
                System.out.println("ERR: Teacher not assigned\n");
            }
        }
    }
    private static boolean checkIfAssigned(Assignment a, Teacher t, ArrayList<Assignment> assignments){
	    boolean subsFree = true;
	    for(int i = 0; i < assignments.size(); i++){
	        Assignment curr = assignments.get(i);
	        if(a.getPeriod().getPeriodNumber() == curr.getPeriod().getPeriodNumber()){
	            if(curr.getSubstitute() != null){
	                if(curr.getSubstitute().getName().equals(t.getName())
                            && t.checkForSpare(a.getPeriod())){
	                    subsFree = false;
                    }
                }
            }
	    }
	    return subsFree;
    }
	public static ArrayList<OnStaffTeacher> getAvailability(int period, ArrayList<OnStaffTeacher> roster ){
		ArrayList<OnStaffTeacher> result = new ArrayList<OnStaffTeacher>();
		for(int i = 0; i < roster.size(); i++){
			OnStaffTeacher t = roster.get(i);
			try{
				if(	t.getWeeklyTally() < W_TAL &&
					t.getMonthlyTally() < M_TAL)
                {
				    for(int j = 0; j < t.getSchedule().length; j++) {
                        if (t.getSchedule()[j].getPeriodNumber() > period) {
                            break;
                        } else if (t.getSchedule()[j].getPeriodNumber() < period) {
                            continue;
                        } else {
                        	if(t.getSchedule()[j].getCourse() == null) {
                        		result.add(t);
                        	}
                        }
                    }
				}
			}catch(Exception e){
				System.err.println(e);
				continue;
			}
		}
		return result;
	}
	public static ArrayList<Teacher> getAvailability(int period, ArrayList<OnStaffTeacher> roster, ArrayList<SupplyTeacher> supplies){
		ArrayList<Teacher> available = new ArrayList<Teacher>();
		for(SupplyTeacher t : supplies) {
			available.add(t);
		}
		for(OnStaffTeacher t: roster) {
			try {
				if(t.getWeeklyTally() < W_TAL && t.getMonthlyTally() < M_TAL) {
					for(int j = 0; j < t.getSchedule().length; j++) {
						if(t.getSchedule()[j].getPeriodNumber() > period) {
							break;
						}
						else if(t.getSchedule()[j].getPeriodNumber() < period) {
							continue;
						}else {
							if(t.getSchedule()[j].getCourse() == null) {
								available.add(t);
							}
						}
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return available;
	}
	private static boolean checkIfAbsent(OnStaffTeacher t, ArrayList<OnStaffTeacher> absences){
		for(int i = 0; i < absences.size(); i++){
			if(t.getName().equals(absences.get(i).getName())){
				return false;
			}
		}
		return true;
	}
	private static boolean checkSpareAvailable(OnStaffTeacher t, Assignment a){
	    boolean available = false;
	    if(a.getPeriod().getPeriodNumber() == t.getSchedule()[(a.getPeriod().getPeriodNumber()-1)].getPeriodNumber()
                && t.getSchedule()[(a.getPeriod().getPeriodNumber()-1)].getCourse() == null
        ){
	        available = true;
        }
        return available;
    }
	private static ArrayList<OnStaffTeacher> sortSubs(ArrayList<OnStaffTeacher> oldSubs) {
        ArrayList<OnStaffTeacher> newSubs = new ArrayList<OnStaffTeacher>();
        for(int i = 0; i < oldSubs.size(); i++){
            OnStaffTeacher min = oldSubs.get(i);
            System.out.println("i: " + oldSubs.get(i).getName());
            for(int j = i+1; j < oldSubs.size();j++){
                if(compareTeacher(min, oldSubs.get(j)) == oldSubs.get(j)){
                    //the one before is less.
                    min = oldSubs.get(j);
                    swap(oldSubs, i, j);
                }
            }
            System.out.println("Min Teacher #" + i + ": " + min.getName() +
                "\n\tWeekly Tally: " + min.getWeeklyTally() +
                "\n\tMonthly Tally: " + min.getMonthlyTally() +
                "\n\tTotal Tally: " + min.getTotalTally());
            newSubs.add(min);
        }
        return newSubs;
    }
	private static OnStaffTeacher compareTeacher(OnStaffTeacher t, OnStaffTeacher u){
	    //returns the lesser of the two
	    OnStaffTeacher result = t;
	    if(result.getWeeklyTally() > u.getWeeklyTally()) {
            result = u;
        }
	    else if(result.getWeeklyTally() == u.getWeeklyTally()){
	        if(result.getMonthlyTally() > u.getMonthlyTally()){
	            result = u;
            }else if(result.getMonthlyTally() == u.getMonthlyTally()){
	            if(result.getTotalTally() > u.getTotalTally()){
	                result = u;
                }else if(result.getTotalTally() == u.getTotalTally()){
                    if(result.getName().compareTo(u.getName()) > 0){
                        result = u;
                    }
                }
            }
        }
     /*   System.out.println(t.getName() + " vs " + u.getName() + "\n"
            + result.getName() + " is less:\n" +
            t.getWeeklyTally() + " vs " + u.getWeeklyTally() + "\n"
            + t.getMonthlyTally() + " vs " + u.getMonthlyTally() + "\n"
            + t.getTotalTally() + " vs " + u.getTotalTally());*/
        return result;
    }
    private static void swap(ArrayList<OnStaffTeacher> oldSubs, int a, int b){
	    if(a >= oldSubs.size() || b >= oldSubs.size()){
	        return;
        }
	    OnStaffTeacher t = oldSubs.get(a);
	    OnStaffTeacher u = oldSubs.get(b);
   	    oldSubs.set(a, u);
	    oldSubs.set(b, t);
    }
    /*public static ArrayList<ArrayList<ArrayList<OnStaffTeacher>>> generateAvailabilityStats(ArrayList<OnStaffTeacher> roster){
		//info for help:
		//stats.get() returns the stats for a specific period.
		//stats.get(1).get(0) returns list of teachers available this week for period 1
		//stats.get(1).get(1) returns list of teachers availabe this month for period 1
		ArrayList<ArrayList<ArrayList<OnStaffTeacher>>> stats = new ArrayList<ArrayList<ArrayList<OnStaffTeacher>>>();

		for(int i = 0; i < NUM_PERIODS; i++){
			ArrayList<OnStaffTeacher> x =getAvailability(i, roster);
			stats.get(i).add(x);
		}

		return stats;
	}*/
	/*public static ArrayList<String> generateCoverageStats(ArrayList<OnStaffTeacher> roster){
		int thisTally;
		int prevTally = -1;
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<OnStaffTeacher> t = new ArrayList<OnStaffTeacher>();
		for(int i = 0; i < roster.size(); i++){
			thisTally = roster.get(i).getTotalTally();
			if(prevTally==-1){
				prevTally = thisTally;
				t.add(roster.get(i));
			}else if(prevTally > thisTally){
				t.add(roster.get(i));
				prevTally = thisTally;
			}else{
				int x = i-1;
				while(x >= 0 && thisTally < t.get(x).getTotalTally()){
					OnStaffTeacher u = t.get(x-1);
					t.add(u);
					x--;
				}
				t.set(x, roster.get(i));
			}
		}
		for(int i = 0; i < t.size(); i++){
			OnStaffTeacher te = t.get(i);
			result.add(te.getName());
			result.add("" + te.getWeeklyTally());
			result.add("" + te.getMonthlyTally());
			result.add("" + te.getTotalTally());
		}
		return result;
	}*/
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
    public static List<Teacher> getAssignableTeacherList(Collection<OnStaffTeacher> fullTeacherList, Collection<SupplyTeacher> supplyList,
            ObservableList<Assignment>
                     assignments, int periodNumber, Assignment
                currentAssignment){
    	System.out.println("Period " + periodNumber);
    	System.out.println();
    	System.out.println("Current Assignment "+currentAssignment.getAbsentee()+"\t"+currentAssignment.getSubstitute
    			()+"\t"+currentAssignment.getPeriod().getPeriodNumber());
    	System.out.println("Supply teachers");
    	System.out.println("/////////////////");
    	for(Teacher t: supplyList) {
    		System.out.print(t+" ");
    		for(Period p: t.getSchedule()) {
    			System.out.print(p.getPeriodNumber() + " ");
    		}
    		System.out.println();
    	}
    	System.out.println();
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
    	
    	List<Teacher> allTeachers = new List<Teacher>();
    	for(Teacher t : supplyList) {
    		allTeachers.add(t);
    	}
    	for(Teacher t: fullTeacherList) {
    		allTeachers.add(t);
    	}
    	
    	System.out.println("All teachers without a class this period");
    	System.out.println("/////////////////");
    	List<Teacher> noneThisPeriod = allTeachers.stream().filter(
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
    public static List<Teacher> getAssignableTeacherList(Collection<OnStaffTeacher> fullTeacherList, Collection<SupplyTeacher> supplyList,
            ObservableList<Assignment>
                     assignments, int periodNumber, Assignment
                currentAssignment){
    	System.out.println("Period " + periodNumber);
    	System.out.println();
    	System.out.println("Current Assignment "+currentAssignment.getAbsentee()+"\t"+currentAssignment.getSubstitute
    			()+"\t"+currentAssignment.getPeriod().getPeriodNumber());
    	System.out.println("Supply teachers");
    	System.out.println("/////////////////");
    	for(Teacher t: supplyList) {
    		System.out.print(t+" ");
    		for(Period p: t.getSchedule()) {
    			System.out.print(p.getPeriodNumber() + " ");
    		}
    		System.out.println();
    	}
    	System.out.println();
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
    	
    	List<Teacher> allTeachers = new List<Teacher>();
    	for(Teacher t : supplyList) {
    		allTeachers.add(t);
    	}
    	for(Teacher t: fullTeacherList) {
    		allTeachers.add(t);
    	}
    	
    	System.out.println("All teachers without a class this period");
    	System.out.println("/////////////////");
    	List<Teacher> noneThisPeriod = allTeachers.stream().filter(
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
}
