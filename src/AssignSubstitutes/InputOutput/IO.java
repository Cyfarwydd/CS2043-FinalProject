/**
 * @author Josh Watson
 **/
package AssignSubstitutes.InputOutput;



import AssignSubstitutes.classes.OnStaffTeacher;
import AssignSubstitutes.classes.Period;
import AssignSubstitutes.classes.SupplyTeacher;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class IO {



        public static ArrayList<OnStaffTeacher> readTeachers(String file) throws IOException {
            System.out.println(file);

            ArrayList<OnStaffTeacher> osTeachers = new ArrayList<>();

            Sheet sheet = newSheet(file);
            DataFormatter df = new DataFormatter();

            String tName;
            String tSkills;
            String[] tp1, tp2, tp3a, tp3b, tp4;
            Period[] tSchedule = new Period[5];

            for ( Row row : sheet) {
                // Iterator keeps going even if cells are empty have to force a break

                if(row.getCell(0) == null || row.getCell(0).getStringCellValue().isEmpty()) break;

                // skipping labels
                if(row == sheet.getRow(0))  continue;

                tName = df.formatCellValue(row.getCell(0));
                tSkills = df.formatCellValue(row.getCell(1)).split(",")[0];
                tp1 = df.formatCellValue(row.getCell(2)).split(",");
                tp2 = df.formatCellValue(row.getCell(3)).split(",");
                tp3a = df.formatCellValue(row.getCell(4)).split(",");
                tp3b = df.formatCellValue(row.getCell(5)).split(",");
                tp4 = df.formatCellValue(row.getCell(6)).split(",");


                /*tSchedule[0] = new Period(tp1[0], getTeachable(tp1[0]), 1, Integer.parseInt(tp1[1]), false);
                tSchedule[1] = new Period(tp2[0], getTeachable(tp2[0]), 2, Integer.parseInt(tp2[1]), false);
                tSchedule[2] = new Period(tp3a[0], getTeachable(tp3a[0]), 3, Integer.parseInt(tp3a[1]), false);
                tSchedule[3] = new Period(tp3b[0], getTeachable(tp3b[0]), 4, Integer.parseInt(tp3b[1]), false);
                tSchedule[4] = new Period(tp4[0], getTeachable(tp4[0]), 5, Integer.parseInt(tp4[1]), false);*/
                //for testing while teachable code is in progress
                tSchedule[0] = new Period(tp1[0], null, 1, Integer.parseInt(tp1[1]), false);
                tSchedule[1] = new Period(tp2[0], null, 2, Integer.parseInt(tp2[1]), false);
                tSchedule[2] = new Period(tp3a[0], null, 3, Integer.parseInt(tp3a[1]), false);
                tSchedule[3] = new Period(tp3b[0], null, 4, Integer.parseInt(tp3b[1]), false);
                tSchedule[4] = new Period(tp4[0], null, 5, Integer.parseInt(tp4[1]), false);

                osTeachers.add(new OnStaffTeacher(tName, tSchedule, tSkills));
            }

            return osTeachers;
        } // readTeachers(String)




        // correlate absences to teachers and change their isAbsent state.
        public static ArrayList<OnStaffTeacher> readAbsences(String file) throws IOException {
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





        public static ArrayList<SupplyTeacher> readSupplies(String file) throws IOException{

            Sheet sheet = newSheet(file);
            DataFormatter df = new DataFormatter();

            ArrayList<SupplyTeacher> supplies = new ArrayList<>();

            for (Row row : sheet){
                // skips labels and empty rows
                if (row.getCell(0) == null) break;
                if (row == sheet.getRow(0)) continue;

                supplies.add(new SupplyTeacher(df.formatCellValue(row.getCell(0)), null, null));
            }

            return supplies;
        }



        //private String checkTeachables(){} //





        public void readMasterSchedule(){} //TO DO





        //public void saveAssignments(){} // TO DO
        //public void writeOnCallerForm(){} // TO DO
        //public Settings readSettings() {} // TO DO
        //public String getMasterResetDate() {} // TO DO
        //public int getMaxWeeklyTally(){} // to do
        //public int getMaxMonthlyTally(){} // to do
        //public int getTempMaxWeeklyTally(){} // to do
        //public int getTempMaxMonthlyTally(){} // to do
        //public int getDefaultCoverageView(){} // to do








         /////////////\\\\\\\\\\\\\\

        // ### Helper Methods ###  \\
        // // // // ///\\\ \\ \\ \\ \\\


        private static String getTeachable(String courseName) throws IOException{

            String teachable = "None";
            DataFormatter df = new DataFormatter();

            Sheet sheet = newSheet("./in/Course Code Input");

            for (Row row : sheet){
                if (row.getCell(0) == null) break;
                if (row == sheet.getRow(0)) continue;

                for(Cell cell : row){
                    if (df.formatCellValue(cell).equals(courseName))
                        teachable = df.formatCellValue(row.getCell(0));
                }
            }

            return teachable;
        }


        private static Sheet newSheet(String file) throws IOException {

            FileInputStream xlFile = new FileInputStream(new File(file));
            Workbook wb = new XSSFWorkbook(xlFile);
            Sheet sheet = wb.getSheetAt(0);
            return sheet;
        }

} // class
