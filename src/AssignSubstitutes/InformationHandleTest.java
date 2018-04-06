package AssignSubstitutes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//import org.junit.experimental.theories.internal.Assignments;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import AssignSubstitutes.classes.Assignment;
import AssignSubstitutes.classes.Teacher;
import AssignSubstitutes.classes.SupplyTeacher;
import AssignSubstitutes.classes.OnStaffTeacher;
import AssignSubstitutes.classes.Period;

class InformationHandleTest {
	ArrayList<OnStaffTeacher> osRoster; //size should end up 7
	ArrayList<SupplyTeacher> supplyRoster; //size should end up 2
	ArrayList<OnStaffTeacher> absences; //size should end up 2
	@BeforeAll
	void buildTeacherStuff() {
		osRoster = IOTest.getTeachers();
		supplyRoster = IOTest.getSupplies();
		absences = IOTest.getAbsences(osRoster);
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
		assertEquals("Johnny Mnemonic", absences.get(0));
		assertEquals("Trish Knockwood", absences.get(1));
	}
	
	@Test
	void testGenerateAssignments() {
		ArrayList<Assignment> assignments = InformationHandle.generateAssignments(osRoster, supplyRoster, absences);
		assertEquals(6, assignments.size());
		for(Assignment a: assignments) {
			assertNotNull(a.getSubstitute());
			assertNotNull(a.getAbsentee());
			assertNotNull(a.getPeriod());
		}
	}

	@Test
	void testGetAvailabilityOSOnly() {
		ArrayList<OnStaffTeacher> r = InformationHandle.getAvailability(0, osRoster);
		//There should be 2 availabe in Schedule[0];
		assertEquals(2,  r.size());
		assertEquals("Jimmy Mac", r.get(0).getName());
		assertEquals("Trish Knockwood", r.get(1).getName());
	}

	@Test
	void testGetAvailabilityAllTeachers() {
		ArrayList<Teacher> r = InformationHandle.getAvailability(0, osRoster, supplyRoster);
		//should be all supply teachers + whatever teachers available from roster. We know roster should be 2. 
		//supplyRoster should have a list of 2. THerefore, r.size() should equal 4
		assertEquals(4, r.size());
	}

	@Test
	void testGetAssignableTeacherListOSOnly() {
		ArrayList<Assignment> a = InformationHandle.generateAssignments(osRoster, supplyRoster, absences);
		List<Teacher> r = InformationHandle.getAssignableTeacherList(osRoster, (ObservableList<Assignment>) a, 0, a.get(0));
		//only 1 class needed covering so if 2 are generally available, but supply should cover that 1 class, so r.size should be 1,
		//because one of the roster generally teaching and available to teach at Schedule[0] is absent 
		assertEquals(1, r.size());
	}

	@Test
	void testGetAssignableTeacherListAllTeachers() {
		ArrayList<Assignment> a = InformationHandle.generateAssignments(osRoster, supplyRoster, absences);
		List<Teacher> r = InformationHandle.getAssignableTeacherList(osRoster, supplyRoster,  (ObservableList<Assignment>) a, 0, a.get(0));
		//only 1 class needed covering so if 2 os are available and 2 supply are available and 1 supply was taken to cover the class,
		//then r.size should be 3
		assertEquals(3, r.size());
	}

}
