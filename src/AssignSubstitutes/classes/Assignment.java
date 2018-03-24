package AssignSubstitutes.classes;

public class Assignment{
	private OnStaffTeacher absentee;
	private Teacher substitute;
	private Period period;
	public Assignment(OnStaffTeacher onStaffTeacherIn, Teacher substituteIn, Period periodIn){
		absentee=onStaffTeacherIn;
		substitute=substituteIn;
		period=periodIn;
	}
	public Assignment(){

	}
	public OnStaffTeacher getAbsentee(){return absentee;}
	public Teacher getSubstitute(){return substitute;}
	public Period getPeriod(){return period;}
	public void setSubstitute(Teacher subIn){substitute=subIn;}
}