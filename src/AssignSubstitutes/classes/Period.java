package AssignSubstitutes.classes;

public class Period{
	private String course;
	private String teachable;
	private String roomNumber;
	private int periodNumber;
	private boolean absent;
	public Period(String courseIn, String teachableIn, int periodNumberIn, String roomNumberIn, boolean isAbsent){
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
	public String getRoomNumber(){return roomNumber;}
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
		strReturn += " Period # "+getPeriodString();
		return strReturn;
	}
}
