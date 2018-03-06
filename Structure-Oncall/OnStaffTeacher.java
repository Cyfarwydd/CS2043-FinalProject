public class OnStaffTeacher extends Teacher{
	int replacementsThisWeek=0;
	int replacementsThisMonth=0;
	int totalTally=0;
	public OnStaffTeacher(String name, Period[] scheldule,String teachable){
		super(name,scheldule,teachable);
	}
	public void incrementWeeklyTally(){
		replacementsThisWeek++;
		totalTally++;
	}
	public void incrementMonthlyTally(){
		replacementsThisMonth++;
		totalTally++;
	}
	public getWeeklyTally(){return replacementsThisWeek;}
	public getMonthlyTally(){return replacementsThisMonth;}
	public getTotalTally(){return getTotalTally;}
}