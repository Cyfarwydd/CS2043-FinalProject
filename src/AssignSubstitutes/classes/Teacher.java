package AssignSubstitutes.classes;


public abstract class Teacher{


	abstract public String getName();
	abstract public String getTeachable();
	abstract public Period[] getSchedule();
	abstract public String toString();

	abstract public boolean checkForSpare(Period p);
	//TODO: combine identical implementations from subclasses here
}