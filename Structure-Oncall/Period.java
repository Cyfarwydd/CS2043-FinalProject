public class period{
	String course;
	String teachable;
	int roomNumber;
	int periodNumber;
	boolean absent=false;
	public period(boolean isAbsent){
		absent=true;
	}
	public period(){}
	public String getCoure(){return course;}
	public String getTeachable(){return teachable;}
	public int getRoomNumber(){return roomNumber;}
	public int getPeriodNumber(){return periodNumber;}
	public boolean Absent(){return isAbsent;}
}
