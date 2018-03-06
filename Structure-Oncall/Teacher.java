public class Teacher{
	String name;
	Period[] schedule;
	String teachable;
	public teacher(String nameIn,Period[] scheduleIn, String teachableIn){
		name=nameIn;
		schedule=scheduleIn;
		teachable=teachableIn;
	}
	public String getName(){return name;}
	public String getTeachable(){return teachable;}
	public Period[] getSchedule(){return schedule;}
}