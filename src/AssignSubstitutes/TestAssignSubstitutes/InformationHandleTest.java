package AssignSubstitutes;

import AssignSubstitutes.classes.Assignment;
import AssignSubstitutes.classes.OnStaffTeacher;
import AssignSubstitutes.classes.SupplyTeacher;
import AssignSubstitutes.classes.Teacher;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//import org.junit.experimental.theories.internal.Assignments;

class InformationHandleTest {
	ArrayList<OnStaffTeacher> osRoster; //size should end up 7
	ArrayList<SupplyTeacher> supplyRoster; //size should end up 2
	ArrayList<OnStaffTeacher> absences; //size should end up 2
	@BeforeAll
	void buildTeacherStuff() {
		osRoster = AssignSubstitutes.IOTest.getTeachers();
		supplyRoster = AssignSubstitutes.IOTest.getSupplies();
		absences = AssignSubstitutes.IOTest.getAbsences(osRoster);
	}
	@Test
	void testOSRosterSize() {
		assertEquals(7, osRoster.size());
	}
	
	@Test
	void testSupplyRosterSize() {
		assertEquals(2, supplyRoster.size());
	}
	
	@Test
	void testAbsencesSize() {
		assertEquals(2, absences.size());
	}
	
	@Test
	void testAbsencesPeople() {
		Assertions.assertEquals("Johnny Mnemonic", absences.get(0));
		Assertions.assertEquals("Trish Knockwood", absences.get(1));
	}
	
	@Test
	void testGenerateAssignments() {
		ArrayList<Assignment> assignments = AssignSubstitutes.InformationHandle.generateAssignments(osRoster, supplyRoster, absences, 2, 4);
		assertEquals(6, assignments.size());
		for(Assignment a: assignments) {
			assertNotNull(a.getSubstitute());
			assertNotNull(a.getAbsentee());
			assertNotNull(a.getPeriod());
		}
	}

	@Test
	void testGetAvailabilityOSOnly() {
		ArrayList<OnStaffTeacher> r = AssignSubstitutes.InformationHandle.getAvailability(0, osRoster, 2, 4);
		//There should be 2 availabe in Schedule[0];
		assertEquals(2,  r.size());
		Assertions.assertEquals("Jimmy Mac", r.get(0).getName());
		Assertions.assertEquals("Trish Knockwood", r.get(1).getName());
	}

	@Test
	void testGetAvailabilityAllTeachers() {
		ArrayList<Teacher> r = AssignSubstitutes.InformationHandle.getAvailability(0, osRoster, supplyRoster, 2, 4);
		//should be all supply teachers + whatever teachers available from roster. We know roster should be 2. 
		//supplyRoster should have a list of 2. THerefore, r.size() should equal 4
		assertEquals(4, r.size());
	}

	@Test
	void testGetAssignableTeacherListOSOnly() {
		ArrayList<Assignment> a = AssignSubstitutes.InformationHandle.generateAssignments(osRoster, supplyRoster, absences, 2, 4);
		List<Teacher> r = AssignSubstitutes.InformationHandle.getAssignableTeacherList(osRoster, (ObservableList<Assignment>) a, 0, a.get(0));
		//only 1 class needed covering so if 2 are generally available, but supply should cover that 1 class, so r.size should be 1,
		//because one of the roster generally teaching and available to teach at Schedule[0] is absent 
		assertEquals(1, r.size());
	}

	@Test
	void testGetAssignableTeacherListAllTeachers() {
		ArrayList<Assignment> a = AssignSubstitutes.InformationHandle.generateAssignments(osRoster, supplyRoster, absences, 2, 4);
		List<Teacher> r = AssignSubstitutes.InformationHandle.getAssignableTeacherList(osRoster, supplyRoster,  (ObservableList<Assignment>) a, 0, a.get(0));
		//only 1 class needed covering so if 2 os are available and 2 supply are available and 1 supply was taken to cover the class,
		//then r.size should be 3
		assertEquals(3, r.size());
	}

}
