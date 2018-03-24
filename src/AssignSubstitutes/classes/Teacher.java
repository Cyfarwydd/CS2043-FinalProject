package AssignSubstitutes.classes;

public class Teacher{
	String name;
	Period[] schedule;
	String teachable;
	public Teacher(String nameIn, Period[] scheduleIn, String teachableIn){
		name=nameIn;
		schedule=scheduleIn;
		teachable=teachableIn;
	}
	public String getName(){return name;}
	public String getTeachable(){return teachable;}
	public Period[] getSchedule(){return schedule;}
	public String toString(){return name;}
	public boolean checkForSpare(Period p){
		boolean r = false;
		if(schedule[(p.getPeriodNumber() -1)].getCourse()==null){
			r = true;
		}
		return r;
	}
}