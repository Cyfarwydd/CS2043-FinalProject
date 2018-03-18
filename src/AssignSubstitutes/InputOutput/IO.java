/**
 * @author Josh Watson
 **/
package AssignSubstitutes.InputOutput;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class IO {

    public ArrayList readTeachers(String file) throws IOException {
        return new ArrayList(parseWorksheet(file));
    }


    public ArrayList readAbsences(String file) throws IOException {
        return new ArrayList(parseWorksheet(file));
    }


    // Iterates through the excel file at the given path
    private ArrayList parseWorksheet(String file) throws IOException {

        FileInputStream xlFile = new FileInputStream(new File(file));
        Workbook wb = new XSSFWorkbook(xlFile);
        Sheet datatypeSheet = wb.getSheetAt(0);
        Iterator<Row> rowIterator = datatypeSheet.iterator();

        ArrayList output = new ArrayList();

        while (rowIterator.hasNext()) {

            Row currentRow = rowIterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();

            while (cellIterator.hasNext()) {
                Cell currentCell = cellIterator.next();

                // I know mixing ArrayList datatypes is generally not a good idea, but I think it would be best for this so as to keep everything as close to the Excel file as possible
                if (currentCell.getCellTypeEnum() == CellType.STRING)
                    output.add(currentCell.getStringCellValue());
                else if (currentCell.getCellTypeEnum() == CellType.NUMERIC)
                    output.add(currentCell.getNumericCellValue());
            } // while iterator.hasNext()
            xlFile.close();
        } // while next Row
        return output;
    }


    // Used if we need to check for empty rows as there's no method for this in Apache and it will iterate over all the empty rows
    private boolean isRowEmpty(Row row) {
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);

            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
                return false;
        }
        return true;
    }


} // class


