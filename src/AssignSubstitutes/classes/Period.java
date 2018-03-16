package AssignSubstitutes.classes;

public class Period{
	private String course;
	private String teachable;
	private int roomNumber;
	private int periodNumber;
	private boolean absent;
	public Period(String courseIn, String teachableIn, int periodNumberIn, int roomNumberIn, boolean isAbsent){
		course=courseIn;
		teachable=teachableIn;
		periodNumber=periodNumberIn;
		roomNumber=roomNumberIn;
		absent=isAbsent;
	}
	public String getCoure(){return course;}
	public String getTeachable(){return teachable;}
	public int getRoomNumber(){return roomNumber;}
	public int getPeriodNumber(){return periodNumber;}
	public boolean Absent(){return absent;}
}