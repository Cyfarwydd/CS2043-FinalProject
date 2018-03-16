package AssignSubstitutes.classes;

public class Settings {

	private String masterResetDate;
	private int maxWeeklyTally;
	private int maxMonthlyTally;
	private int tempMaxWeeklyTally;
	private int tempMaxMonthlyTally;
	private String absenceInputPath;
	private String masterSchedulePath;
	private String courseCodePath;
	private String supplyTeacherPath;
	private String OnCallerFormDirPath;
	private String OnCallerFormFileNameFormat;
	
	public Settings(String masterResetDateIn,int maxWeeklyTallyIn, int maxMonthlyTallyIn, int tempMaxMonthlyTallyIn,
                    int tempMaxWeeklyTallyIn, String absenceInputPathIn, String masterSchedulePathIn, String
                            courseCodePathIn, String supplyTeacherPathIn, String OnCallerFormDirPathIn,
							String OnCallerFormFileNameFormatIn) {
		
		masterResetDate=masterResetDateIn;
		maxWeeklyTally=maxWeeklyTallyIn;
		maxMonthlyTally=maxMonthlyTallyIn;
		tempMaxMonthlyTally=tempMaxMonthlyTallyIn;
		tempMaxWeeklyTally=tempMaxWeeklyTallyIn;
		absenceInputPath=absenceInputPathIn;
		masterSchedulePath=masterSchedulePathIn;
		courseCodePath=courseCodePathIn;
		supplyTeacherPath=supplyTeacherPathIn;
		OnCallerFormDirPath = OnCallerFormDirPathIn;
		OnCallerFormFileNameFormat = OnCallerFormFileNameFormatIn;
	}
	public String getMasterResetDate() {return masterResetDate;}
	public int getMaxWeeklyTally() {return maxWeeklyTally;}
	public int getMaxMonthlyTally(){return maxMonthlyTally;}
	public int getTempMaxMonthlyTally(){return tempMaxMonthlyTally;}
	public int getTempMaxWeeklyTally(){return tempMaxWeeklyTally;}
	public String getAbsenceInputPath(){return absenceInputPath;}
	public String getMasterSchedulePath(){return masterSchedulePath;}
	public String getCourseCodePath(){return courseCodePath;}
	public String getSupplyTeacherPath(){return supplyTeacherPath;}
	public String getOnCallerFormDirPath(){return OnCallerFormDirPath;}
	public String getOnCallerFormFileNameFormat(){return OnCallerFormFileNameFormat;}
	public void setMasterResetDate(String masterResetDateIn) {masterResetDate = masterResetDateIn;}
	public void setMaxWeeklyTally(int maxWeeklyTallyIn) { maxWeeklyTally = maxWeeklyTallyIn;}
	public void setMaxMonthlyTally(int maxMonthlyTallyIn){maxMonthlyTally=maxMonthlyTallyIn;}
	public void setTempMaxMonthlyTally(int tempMaxMonthlyTallyIn){tempMaxMonthlyTally=tempMaxMonthlyTallyIn;}
	public void setTempMaxWeeklyTally(int tempMaxWeeklyTallyIn){tempMaxWeeklyTally=tempMaxWeeklyTallyIn;}
	public void setAbsenceInputPath(String absenceInputPathIn){absenceInputPath=absenceInputPathIn;}
	public void setMasterSchedulePath(String masterSchedulePathIn){masterSchedulePath=masterSchedulePathIn;}
	public void setCourseCodePath(String courseCodePathIn){courseCodePath=courseCodePathIn;}
	public void setSupplyTeacherPath(String supplyTeacherPathIn){supplyTeacherPath=supplyTeacherPathIn;}
	public void setOnCallerFormDirPath(String OnCallerFormDirPathIn){OnCallerFormDirPath = OnCallerFormDirPathIn;}
	public void setOnCallerFormFileNameFormat(String OnCallerFormFileNameFormatIn){OnCallerFormFileNameFormat = OnCallerFormFileNameFormatIn;}

}
