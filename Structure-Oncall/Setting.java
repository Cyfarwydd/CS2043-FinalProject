public class Settings {

	String masterResetDate;
	int maxWeeklyTally;
	int maxMonthlyTally;
	int tempMaxWeeklyTally;
	int tempMaxMonthlyTally;
	String absenceInputPath;
	String masterSchedulePath;
	String courseCodePath;
	String supplyTeacherPath;
	int DefaultCoverageView;
	
	public Settings(String masterResetDateIn,int maxWeeklyTallyIn, int maxMonthlyTallyIn,int tempMaxMonthlyTallyIn,
	int tempMaxWeeklyTallyIn, String absenceInputPathIn, String masterSchedulePathIn, String courseCodePathIn, String supplyTeacherPathIn
	int DefaultCoverageViewIn) {
		
		masterResetDate=masterResetDateIn;
		maxWeeklyTally=maxWeeklyTallyIn;
		maxMonthlyTally=maxMonthlyTallyIn;
		tempMaxMonthlyTally=tempMaxMonthlyTallyIn;
		tempMaxWeeklyTally=tempMaxWeeklyTallyIn;
		absenceInputPath=absenceInputPathIn;
		masterSchedulePath=masterSchedulePathIn;
		courseCodePath=courseCodePathIn;
		supplyTeacherPath=supplyTeacherPathIn;
		DefaultCoverageView=DefaultCoverageViewIn;
		
		
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
	public int getDefaultCoverageView(){return DefaultCoverageView;}
	
}
