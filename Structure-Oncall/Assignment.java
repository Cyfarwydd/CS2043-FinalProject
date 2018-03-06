public class Assignment{
	OnStaffTeacher absentee;
	Teacher substitute;
	Period period;
	pubic Assignment(OnStaffTeacher onStaffTeacherIn,Teacher substituteIn,Period periodIn){
		absentee=onStaffTeacherIn;
		substitute=substituteIn;
		period=periodIn;
	}
	public (OnStaffTeacher onStaffTeacherIn,Period periodIn){
		absentee=onStaffTeacherIn;
		period=periodIn;
	}
	public OnStaffTeacher getAbsentee(){return absentee;}
	public Teacher getSubstitute(){return substitute;}
	public Period getPeriod(){return period;}
	public void setSubstitute(Teacher subIn){substitute=subIn;}
}