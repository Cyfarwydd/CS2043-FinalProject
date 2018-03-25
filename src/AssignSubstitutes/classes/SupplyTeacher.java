package AssignSubstitutes.classes;

public class SupplyTeacher extends Teacher{
    private String name;
    private Period[] schedule;
    private String teachable;
    public SupplyTeacher(String nameIn,Period[] scheduleIn, String teachableIn){
        super();
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