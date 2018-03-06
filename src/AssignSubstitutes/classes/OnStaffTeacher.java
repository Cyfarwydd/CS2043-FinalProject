package AssignSubstitutes.classes;

public class OnStaffTeacher extends Teacher{
	int replacementsThisWeek=0;
	int replacementsThisMonth=0;
	int totalTally=0;
	public OnStaffTeacher(String name, Period[] schedule, String teachable){
		super(name, schedule, teachable);
	}
	public void incrementTally(){
		replacementsThisWeek++;
		totalTally++;
	}
	public void incrementMonthlyTally(){
		replacementsThisMonth++;
		totalTally++;
	}
	public int getWeeklyTally(){return replacementsThisWeek;}
	public int getMonthlyTally(){return replacementsThisMonth;}
	public int getTotalTally(){return totalTally;}
}