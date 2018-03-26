package AssignSubstitutes.classes;

import AssignSubstitutes.InformationHandle;

public class OnStaffTeacher extends Teacher{
	private int replacementsThisWeek=0;
	private int replacementsThisMonth=0;
	private int totalTally=0;
	private String name;
	private Period[] schedule;
	private String teachable;
	public OnStaffTeacher(String nameIn, Period[] scheduleIn, String teachableIn){
		super();
		name=nameIn;
		schedule=scheduleIn;
		teachable=teachableIn;
	}
	public void incrementTally(){
		replacementsThisWeek++;
		totalTally++;
		replacementsThisMonth++;
	}
	public void resetMonthlyTally(){
		replacementsThisMonth=0;
	}
	public void resetWeeklyTally(){
		replacementsThisWeek=0;
	}
	public String getName(){return name;}
	public String getTeachable(){return teachable;}
	public Period[] getSchedule(){return schedule;}
	public String toString(){return name;}
	public int getWeeklyTally(){return replacementsThisWeek;}
	public int getMonthlyTally(){return replacementsThisMonth;}
	public int getTotalTally(){return totalTally;}
	public boolean checkForSpare(Period p){
		boolean isFree = false;
		if(p.getPeriodNumber() == schedule[p.getPeriodNumber()-1].getPeriodNumber() && schedule[p.getPeriodNumber()-1].getCourse() == null){
			isFree = true;
		}
		return isFree;
	}
}