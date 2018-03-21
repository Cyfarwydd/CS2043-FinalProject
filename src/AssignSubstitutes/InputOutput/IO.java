/**
 * @author Josh Watson
 **/
package AssignSubstitutes.InputOutput;


import AssignSubstitutes.classes.OnStaffTeacher;
import AssignSubstitutes.classes.Period;
import AssignSubstitutes.classes.Settings;
import AssignSubstitutes.classes.Teacher;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class IO {


        public ArrayList<Teacher> readTeachers(String file) throws IOException {
            ArrayList<Teacher> osTeachers = new ArrayList<>();

            Sheet sheet = newSheet(file);
            DataFormatter df = new DataFormatter();


            String tName;
            String tSkills;
            String tp1, tp2, tp3a, tp3b, tp4;
            Period[] tSchedule = new Period[5];


            for ( Row row : sheet) {
                // Iterator keeps going even if cells are empty have to force a break
                if(row.getCell(0) == null) break;
                // skipping labels
                if(row == sheet.getRow(0))  continue;

                tName = df.formatCellValue(row.getCell(0));
                // !*! Will need to handle csv parsing !*! \\
                tSkills = df.formatCellValue(row.getCell(1));
                tp1 = df.formatCellValue(row.getCell(2));
                tp2 = df.formatCellValue(row.getCell(3));
                tp3a = df.formatCellValue(row.getCell(4));
                tp3b = df.formatCellValue(row.getCell(5));
                tp4 = df.formatCellValue(row.getCell(6));

                tSchedule[0] = new Period(tp1, null, 1, -1, false);
                tSchedule[1] = new Period(tp2, null, 2, -1, false);
                tSchedule[2] = new Period(tp3a, null, 3, -1, false);
                tSchedule[3] = new Period(tp3b, null, 4, -1, false);
                tSchedule[4] = new Period(tp4, null, 5, -1, false);

                osTeachers.add(new Teacher(tName, tSchedule, tSkills));
            }

            return osTeachers;
        } // readTeachers(String)




        public ArrayList<OnStaffTeacher> readAbsences(String file) throws IOException {
            ArrayList<OnStaffTeacher> osTeachers = new ArrayList<>();

            Sheet sheet = newSheet(file);
            DataFormatter df = new DataFormatter();

            String tName;
            Period[] tSchedule = new Period[5];

            Period[][] weeklySchedule = new Period[5][];

            for (Row row : sheet) {
                // skips labels and empty rows
                if (row.getCell(0) == null) break;
                if (row == sheet.getRow(0)) continue;
                if (row == sheet.getRow(1)) continue;

                tName = df.formatCellValue(row.getCell(0));

                // Goes through the teacher's five periods for each day through Monday to Friday
                for (int j = 0; j < 5; j++) {
                    for (int i = j * 5; i < (j * 5) + 5; i++) {
                        tSchedule[i - (j * 5)] = new Period(df.formatCellValue(row.getCell(i + 1)), null, i - (j * 5) + 1, -1, true);
                    }
                    weeklySchedule[j] = tSchedule;
                }

                osTeachers.add(new OnStaffTeacher(tName, weeklySchedule[0], null));
            }

            return osTeachers;
        } //readAbsences()





        /////////////\\\\\\\\\\\\\\
        // ### Helper Methods ###  \\
        // // // // ///\\\ \\ \\ \\ \\\


        private Sheet newSheet(String file) throws IOException {
            FileInputStream xlFile = new FileInputStream(new File(file));
            Workbook wb = new XSSFWorkbook(xlFile);
            Sheet sheet = wb.getSheetAt(0);
            return sheet;
        }


    } // class





} // class
