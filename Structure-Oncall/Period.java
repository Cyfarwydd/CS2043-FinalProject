public class period{
	String course;
	String teachable;
	int roomNumber;
	int periodNumber;
	boolean absent=false;
	public period(String coureIn, String teachableIn, int roomNum, int periodNum,boolean isAbsent){
		course=courseIn;
		teachable=teachableIn;
		roomNumber=roomNum;
		periodNumber=periodNum;
		absent=true
	}
	public period(String coureIn, String teachableIn, int roomNum, int periodNum){
		course=courseIn;
		teachable=teachableIn;
		roomNumber=roomNum;
		periodNumber=periodNum;
	}
	public String getCoure(){return course;}
	public String getTeachable(){return teachable;}
	public int getRoomNumber(){return roomNumber;}
	public int getPeriodNumber(){return periodNumber;}
	public boolean Absent(){return isAbsent;}
}
