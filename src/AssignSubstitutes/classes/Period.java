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
	public void toggleAbsence(boolean toggle){
		absent=toggle;
	}
	public String getCourse(){return course;}
	public String getTeachable(){return teachable;}
	public int getRoomNumber(){return roomNumber;}
	public int getPeriodNumber(){return periodNumber;}
	public boolean Absent(){return absent;}
	public String getPeriodString(){
		//returns empty string if error
		String periodNum = "";
		switch(periodNumber){
			case 1:	periodNum += 1;
				break;
			case 2: periodNum += 2;
				break;
			case 3: periodNum += "3a";
				break;
			case 4:	periodNum += "3b";
				break;
			case 5: periodNum += "4";
			break;
		}
		return periodNum;
	}
	public String toString(){
		String strReturn = new String();
        if (course.isEmpty()){
			strReturn += "Empty";
		} else {
			strReturn += course;
		}
		strReturn += " Period # "+periodNumber;
		return strReturn;
	}
}
