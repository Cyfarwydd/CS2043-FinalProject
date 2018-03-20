package AssignSubstitutes;

import java.util.ArrayList;
import AssignSubstitutes.classes.*;

public class InformationHandle{
    //TODO: replace NUM_PERIODS, M_TAL, W_TAL with actual variables
	public static final int NUM_PERIODS = 5;
	public static final int M_TAL = 4;
	public static final int W_TAL = 2;
	public static ArrayList<Assignment> generateAssignments(ArrayList<OnStaffTeacher> roster, ArrayList<Teacher> supply, ArrayList<OnStaffTeacher> absent){
		//initializing variables to work with
		ArrayList<ArrayList<Period>> absentPeriods = new ArrayList<ArrayList<Period>>();
		ArrayList<ArrayList<Teacher>> supplyList = new ArrayList<ArrayList<Teacher>>();
		ArrayList<ArrayList<OnStaffTeacher>> onCallList = new ArrayList<ArrayList<OnStaffTeacher>>();

		for(int i = 0; i <= NUM_PERIODS; i++){
			supplyList.add(new ArrayList<Teacher>());
			absentPeriods.add(new ArrayList<Period>());
			//absentPerPeriod.add(new ArrayList<OnStaffTeacher>());
		}
		ArrayList<Assignment> assignments = new ArrayList<Assignment>();
		//Assignment a = new Assignment(roster.get(0), supply.get(0), roster.get(0).getSchedule()[2]);
		//assignments.add(a);

        //building list of absent periods to be filled
        for(int i = 0; i < absent.size(); i++){
            OnStaffTeacher t = absent.get(i);
            Period schedule[] = t.getSchedule();
            for(int j = 0; j < schedule.length; j++){
                if(schedule[j].Absent()){
                    Period p = schedule[j];
                    int pNum = p.getPeriodNumber();
                    absentPeriods.get(pNum).add(p);
                   // absentPerPeriod.get(p.getPeriodNumber()).add(t);
                }
            }
        }

		//building list of subs for the day
		for(int i = 0; i < supply.size(); i++){
			Teacher t = supply.get(i);
			for(int j = 0; j < NUM_PERIODS; j++){
				supplyList.get(j).add(t);
			}
		}
		for(int i = 0; i < roster.size(); i++){
		    OnStaffTeacher t = roster.get(i);
		    if(checkIfAbsent(t, absent)){ continue;}
		    for(int j = 0; j < NUM_PERIODS; j++){
		        ArrayList<OnStaffTeacher> thisP = getAvailability(j, roster);
		        onCallList.add(thisP);
            }
        }
        int frontSup = 0;
        int frontOS = 0;
		while(absentPeriods.size() > assignments.size()){
            for(int i = 0; i < absentPeriods.size(); i++){
                for(int j = 0; j < absentPeriods.get(i).size(); j++) {
                    Period p = absentPeriods.get(i).get(j);
                    OnStaffTeacher t = matchProfPeriod(roster, p);
                    if (frontSup < supply.size()){
                        Assignment a = new Assignment(t, supply.get(frontSup), p);
                        frontSup ++;
                        assignments.add(a);
                    }else{
                        //TODO: Fix the case to assign onstaff teachers properly
                        //Assignment a = new Assignment(t, onCallList.get(frontOS), p);
                        //TODO: Fix polymorphism so assignments can take onstaff teachers as well as supply teachers
                        Period pEmpty = new Period(null, null, -1, -1, false);
                        Period schep[] = new Period[4];
                        for(int k = 0; k < 4; k++){
                            schep[k] = pEmpty;
                        }
                        Supply s = new Supply("Jane Eyre", schep, "Eng");
                        Assignment a = new Assignment(t, s, p);
                        frontOS++;
                        assignments.add(a);
                    }
                }
            }
        }
		return assignments;
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

	public static ArrayList<OnStaffTeacher> getAvailability(int period, ArrayList<OnStaffTeacher> roster ){
		ArrayList<OnStaffTeacher> result = new ArrayList<OnStaffTeacher>();
		for(int i = 0; i < roster.size(); i++){
			OnStaffTeacher t = roster.get(i);
			try{
				if(	t.getWeeklyTally() < W_TAL &&
					t.getMonthlyTally() < M_TAL)
                {
				    for(int j = 0; j < t.getSchedule().length; j++) {
                        if (t.getSchedule()[j].getPeriodNumber() == period) {
                            break;
                        } else if (t.getSchedule()[j].getPeriodNumber() < period) {
                            continue;
                        } else {
                            result.add(t);
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
	private static boolean checkIfAbsent(OnStaffTeacher t, ArrayList<OnStaffTeacher> absences){
		for(int i = 0; i < absences.size(); i++){
			if(t.getName().equals(absences.get(i).getName())){
				return true;
			}
		}
		return false;
	}
	private static ArrayList<OnStaffTeacher> sortSubs(ArrayList<OnStaffTeacher> oldSubs){
	    //sorting by monthly tally, then weekly tally if monthly is same. Otherwise, sorted by total tally, then by alphabetical
		ArrayList<OnStaffTeacher> result= new ArrayList<OnStaffTeacher>();

		while(result.size() != oldSubs.size()) {
            OnStaffTeacher minT = oldSubs.get(0);
            for (int i = 0; i < oldSubs.size(); i++) {
                OnStaffTeacher newT = oldSubs.get(i);
                if (minT.getMonthlyTally() > newT.getMonthlyTally()) {
                    minT = newT;
                } else if (minT.getMonthlyTally() == newT.getMonthlyTally()) {
                    if (minT.getWeeklyTally() > newT.getWeeklyTally()) {
                        minT = newT;
                    } else if (minT.getWeeklyTally() == newT.getWeeklyTally()) {
                        if (minT.getTotalTally() > newT.getTotalTally()) {
                            minT = newT;
                        } else if (minT.getTotalTally() == newT.getTotalTally()) {
                            if (minT.getName().compareTo(newT.getName()) > 0) {
                                minT = newT;
                            }
                        }
                    }
                }
            }
            result.add(minT);
        }
		return result;
	}
	private static boolean doesProfTeachThis(OnStaffTeacher t, Period p){
	    boolean teacher = false;
	    Period[] sch = t.getSchedule();
	    for(int i = 0; i < sch.length; i++){
	        if(sch[i].getPeriodNumber() == p.getPeriodNumber()
                    && (sch[i].getCourse().equals(p.getCourse())
                    && (sch[i].getRoomNumber() == p.getRoomNumber())
                    && (sch[i].getTeachable().equals(p.getTeachable()
            )))) {
                teacher = true;
                break;
            }
        }
        return teacher;
    }
    private static OnStaffTeacher matchProfPeriod(ArrayList<OnStaffTeacher> roster, Period p){
	    OnStaffTeacher t = roster.get(0);
	    //TODO: fix why this is giving nullpointerexception
	    try{
	        for(int i = 0; !doesProfTeachThis(t, p) && i < roster.size(); i++) {
                t = roster.get(i);
            }
        }catch(Exception e){

        }
        return t;
    }
}
