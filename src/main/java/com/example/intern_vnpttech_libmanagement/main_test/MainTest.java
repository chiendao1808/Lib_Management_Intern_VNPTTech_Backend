package com.example.intern_vnpttech_libmanagement.main_test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainTest {

    public static void main(String[] args) {
        try{
            File dataFile = new File("C:\\Users\\leope\\Desktop\\data_file_book_QLTV.xlsx");
            FileInputStream fis = new FileInputStream(dataFile);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iter = sheet.iterator();
            Map<String,Integer> columnNames = new HashMap<>();
            Row firstRow = sheet.getRow(0); // get first row
            for(Cell cell: firstRow)
            {
                if(cell.getCellType() == CellType.STRING)
                    columnNames.put(cell.getStringCellValue(),cell.getColumnIndex());
            }
            while (iter.hasNext()){
                Row row =iter.next();
                Iterator<Cell> cellIterator = row.iterator();
                while (cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()){
                        case STRING: {
                            System.out.print(cell.getStringCellValue() +"\t\t\t");
                            break;
                        }
                        case NUMERIC: {
                            System.out.print(cell.getNumericCellValue()+"\t\t\t");
                            break;
                        }
                        default:{
                        }
                    }
                }
            System.out.println("");
            }
            System.out.println(columnNames);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
