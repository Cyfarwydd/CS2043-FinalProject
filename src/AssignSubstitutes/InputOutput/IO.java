/**
 * @author Josh Watson
 **/
package AssignSubstitutes.InputOutput;



import AssignSubstitutes.classes.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class IO {
        private static ArrayList<OnStaffTeacher> readStaff = new ArrayList<>();

        public static ArrayList<OnStaffTeacher> readTeachers(String file, String courseFile) throws IOException {

            ArrayList<OnStaffTeacher> osTeachers = new ArrayList<>();

            Sheet sheet = newSheet(file);

            DataFormatter df = new DataFormatter();

            String tName;
            String tSkills;
            String[] tp1, tp2, tp3a, tp3b, tp4;
            Period[] tSchedule;
            for ( Row row : sheet) {
                // Iterator keeps going even if cells are empty have to force a break
                if(row.getCell(0) == null || row.getCell(0).getStringCellValue().isEmpty()) break;

                // skipping labels
                if(row.equals(sheet.getRow(0)))  continue;

                tName = df.formatCellValue(row.getCell(0));
                tSkills = df.formatCellValue(row.getCell(1)).split(",")[0];
                tSchedule = new Period[5];
                tp1 = df.formatCellValue(row.getCell(2)).split(",");
                tp2 = df.formatCellValue(row.getCell(3)).split(",");
                tp3a = df.formatCellValue(row.getCell(4)).split(",");
                tp3b = df.formatCellValue(row.getCell(5)).split(",");
                tp4 = df.formatCellValue(row.getCell(6)).split(",");
                if(tp1.length==1){
                    String[] temp={tp1[0], "0"};
                    tp1=temp;
                }
                if(tp2.length==1){
                    String[] temp={tp2[0], "0"};
                    tp2=temp;
                }
                if(tp3a.length==1){
                    String[] temp={tp3a[0], "0"};
                    tp3a=temp;
                }
                if(tp3b.length==1){
                    String[] temp={tp3b[0], "0"};
                    tp3b=temp;
                }
                if(tp4.length==1){
                    String[] temp={tp4[0], "0"};
                    tp4=temp;
                }

                //TODO: Wrap this in another a try catch to differentiate if MasterSchedule file can't be found or CourseCodes file can't be found
                tSchedule[0] = new Period(tp1[0], getTeachable(tp1[0], courseFile), 1, tp1[1], false);
                tSchedule[1] = new Period(tp2[0], getTeachable(tp2[0], courseFile), 2, tp2[1], false);
                tSchedule[2] = new Period(tp3a[0], getTeachable(tp3a[0], courseFile), 3, tp3a[1], false);
                tSchedule[3] = new Period(tp3b[0], getTeachable(tp3b[0], courseFile), 4, tp3b[1], false);
                tSchedule[4] = new Period(tp4[0], getTeachable(tp4[0], courseFile), 5, tp4[1], false);

                osTeachers.add(new OnStaffTeacher(tName, tSchedule, tSkills));
            }
            readStaff = osTeachers;
            return osTeachers;
        } // readTeachers(String)

        // correlate absences to teachers and change their isAbsent state.
        public static ArrayList<OnStaffTeacher> readAbsences(String file, ArrayList<OnStaffTeacher> roster, LocalDate l) throws IOException {

            ArrayList<OnStaffTeacher> osTeachers = new ArrayList<>();

            int d = l.getDayOfWeek().getValue() -1;
            if(d > 4) return null;
            Sheet sheet = newSheet(file);
            DataFormatter df = new DataFormatter();


            String tName;

            for (Row row : sheet) {
                Period[] tSchedule = new Period[5];
                // skips labels and empty rows
                if (row == sheet.getRow(0)) continue;
                if (row == sheet.getRow(1)) continue;
                if (row.getCell(0) == null || row.getCell(0).getStringCellValue().isEmpty()) break;

                tName = df.formatCellValue(row.getCell(0));

                // Goes through the teacher's five periods for each day through Monday to Friday
               /* for (int j = 0; j < 5; j++) {
                    for (int i = j * 5; i < (j * 5) + 5; i++) {
                        tSchedule[i - (j * 5)] = new Period(df.formatCellValue(row.getCell(i + 1)), null, i - (j * 5) + 1, "-1", true);
                    }
                    weeklySchedule[j] = tSchedule;
                }*/
               for(int i = d * 5; i < (d * 5) + 5; i++){
                   if(df.formatCellValue(row.getCell(i + 1)).toLowerCase().equals("x")){
                       tSchedule[i - (d * 5)] = new Period(df.formatCellValue(row.getCell(i + 1)), null, i - (d * 5) + 1, "-1", true);
                   }else{
                       tSchedule[i - (d * 5)] = new Period(df.formatCellValue(row.getCell(i + 1)), null, i - (d * 5) + 1, "-1", false);
                   }
               }
                osTeachers.add(new OnStaffTeacher(tName, tSchedule, null));
            }
            ArrayList<OnStaffTeacher> absent = crossReferenceAbsences(osTeachers, roster);

            return absent;
        } //readAbsences()
    private static ArrayList<OnStaffTeacher> crossReferenceAbsences(ArrayList<OnStaffTeacher> absent, ArrayList<OnStaffTeacher> roster){
            ArrayList<OnStaffTeacher> trueAbsences = new ArrayList<>();
            for(OnStaffTeacher t : absent){
                Period[] sch = new Period[5];
                for(OnStaffTeacher u : roster){
                    if(t.getName().equals(u.getName())) {
                        for (Period p : t.getSchedule()) {
                            for (Period q : u.getSchedule()) {
                                if (p.getPeriodNumber() == q.getPeriodNumber() && p.Absent()) {
                                    sch[q.getPeriodNumber() - 1] = q;
                                    sch[q.getPeriodNumber() - 1].toggleAbsence(true);
                                } else {
                                    sch[q.getPeriodNumber() - 1] = q;
                                }
                            }
                        }
                    }
                }
                for(Period p: sch){
                    if(p.Absent()){
                        trueAbsences.add(new OnStaffTeacher(t.getName(), sch, null));
                        break;
                    }
                }
            }
            return trueAbsences;
    }

        public static ArrayList<SupplyTeacher> readSupplies(String file) throws IOException{

            Sheet sheet = newSheet(file);
            DataFormatter df = new DataFormatter();

            ArrayList<SupplyTeacher> supplies = new ArrayList<>();

            for (Row row : sheet){
                // skips labels and empty rows
                if (row.getCell(0) == null || row.getCell(0).getStringCellValue().isEmpty()) break;
                if (row == sheet.getRow(0)) continue;

                supplies.add(new SupplyTeacher(df.formatCellValue(row.getCell(0)), null));
            }

            return supplies;
        }

        public static void writeOnCallerForms(Map<LocalDate, ArrayList<Assignment>> assignments) throws IOException {
            // iterate through the absent teachers for the day generating an on caller form for each one

            System.err.println(assignments.toString());

    /*       ArraList absentList = assignments.toString()

            for (int i = 0 ; i < assignments.size() ; i++){
                System.err.println(assignments.get(assignments).get(i).getAbsentee().getName());
                saveWorkbook((createWorkbook()), "./On Caller Forms/", assignments.get(i).getAbsentee().getName(), LocalDate.now());
                assignments.get(i).getAbsentee();
            }
    */
        } // TO DO

        //public void saveAssignments(){} // TO DO
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


        private static String getTeachable(String courseName, String courseFile) throws IOException{

            String teachable = "None";
            DataFormatter df = new DataFormatter();

            Sheet sheet = newSheet(courseFile);

            for (Row row : sheet){
                if(row.getCell(0) == null || row.getCell(0).getStringCellValue().isEmpty()) break;

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


       private static Workbook createWorkbook() throws IOException{
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet();

            Font bold = wb.createFont();
            bold.setBold(true);

            CellStyle headerStyle = wb.createCellStyle();
            headerStyle.setFont(bold);

            Row header = sheet.createRow(1);
            header.setRowStyle(headerStyle);

            sheet.createRow(0);
            sheet.getRow(0).setRowStyle(headerStyle);

            sheet.getRow(0).createCell(0).setCellValue("Name: ");
            sheet.getRow(0).createCell(3).setCellValue("Date: ");



            String[] initHeader = {"Period", "Covered By", "Course", "Room #", "Instructions"};

            for(int i = 0 ; i < initHeader.length ; i++){
                header.createCell(i);
                header.getCell(i).setCellValue(initHeader[i]);
            }

            sheet.setColumnWidth(0, 2000);
            sheet.setColumnWidth(1, 4000);
            sheet.setColumnWidth(2, 3000);
            sheet.setColumnWidth(3, 2000);
            sheet.setColumnWidth(4, 6000);

            return wb;
        }



        // TO DO: Remove fileDir and add to settings
        private static void saveWorkbook(Workbook wb, String fileDir, String teacherName, LocalDate date) throws IOException{

            if( fileDir.equals("") ) fileDir = "./Auto Generated Output.xlsx";
            if( date.equals("") ) date = LocalDate.now();

            FileOutputStream output = new FileOutputStream(fileDir+"/"+date.toString()+" "+teacherName);
            wb.write(output);
            output.close();

            // Closing the workbook
            wb.close();

        }


} // class
