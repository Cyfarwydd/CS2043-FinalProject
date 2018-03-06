package AssignSubstitutes.classes;

public class Period{
	String course;
	String teachable;
	int roomNumber;
	int periodNumber;
	boolean absent=false;
	public Period(boolean isAbsent){
		absent=true;
	}
	public String getCoure(){return course;}
	public String getTeachable(){return teachable;}
	public int getRoomNumber(){return roomNumber;}
	public int getPeriodNumber(){return periodNumber;}
	public boolean Absent(){return absent;}
}