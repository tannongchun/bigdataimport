
package com.sunducation.waterflow.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import com.sunducation.waterflow.dto.DataDTO;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.extractor.XSSFEventBasedExcelExtractor;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


public class ParseExcelUtil {
    /**
     * Uses the XSSF Event SAX helpers to do most of the work
     *  of parsing the Sheet XML, and outputs the contents
     *  as a (basic) CSV.
     */

    private class SheetToCSV implements SheetContentsHandler {
        private boolean firstCellOfRow = false;
        private int currentRow = -1;
        private int currentCol = -1;
        private int startRowIndex = 0;
        private DataDTO dto =new DataDTO();
       @Override
        public void startRow(int rowNum) {
            if(rowNum <= startRowIndex) {
                return;
            }
            firstCellOfRow = true;
            currentRow = rowNum;
            currentCol = -1;
    }

        @Override
        public void endRow(int rowNum) {
            if(rowNum <= startRowIndex) {
                return;
            }
            if( null!= dto){
                dataList.add(dto);
            }
            //
            dto = new DataDTO();
        }

        @Override
        public void cell(String cellReference, String formattedValue,
                XSSFComment comment) {

            if(currentRow <= startRowIndex) {
                return;
            }
            if (firstCellOfRow) {
                firstCellOfRow = false;
            }
            // gracefully handle missing CellRef here in a similar way as XSSFCell does
            if(cellReference == null) {
                cellReference = new CellAddress(currentRow, currentCol).formatAsString();
            }
            // Did we miss any cells?
            int thisCol = (new CellReference(cellReference)).getCol();
            int missedCols = thisCol - currentCol - 1;
            currentCol = thisCol;
            Object val = null;
            // Number or string?
//            try {
//                //noinspection ResultOfMethodCallIgnored
//                Double.parseDouble(formattedValue);
                val = formattedValue ;
//            } catch (NumberFormatException e) {
//                 val = formattedValue ;
//            }
            char row=cellReference.substring(0,1).charAt(0);
            switch (row){
                case 65:
                      dto.setNo(Integer.parseInt(val.toString()));
                      break;
                case 66:
                    dto.setGrade((String) val);
                    break;
                case 67:
                    dto.setName((String) val);
                    break;
                case 68:
                    dto.setAge(Integer.parseInt(val.toString()));
                    break;
            }

        } // cell

        @Override
        public void headerFooter(String text, boolean isHeader, String tagName) {
            // Skip, no headers or footers in CSV
        }
    }
    private final OPCPackage xlsxPackage;
    /**
     * Number of columns to read starting with leftmost
     */
    private final int minColumns;

    /**
     * Destination for data
     */
    private final List<DataDTO> dataList;

    /**
     * Creates a new XLSX -> CSV converter
     *
     * @param pkg        The XLSX package to process
     * @param dataList     存放数据集合
     * @param minColumns The minimum number of columns to output, or -1 for no minimum
     */
    public ParseExcelUtil(OPCPackage pkg, List<DataDTO> dataList, int minColumns) {
        this.xlsxPackage = pkg;
        this.dataList = dataList;
        this.minColumns = minColumns;
    }

    /**
     * Parses and shows the content of one sheet
     * using the specified styles and shared-strings tables.
     *
     * @param styles The table of styles that may be referenced by cells in the sheet
     * @param strings The table of strings that may be referenced by cells in the sheet
     * @param sheetInputStream The stream to read the sheet-data from.

     * @exception IOException An IO exception from the parser,
     *            possibly from a byte stream or character stream
     *            supplied by the application.
     * @throws SAXException if parsing the XML data fails.
     */
    public void processSheet(
            StylesTable styles,
            ReadOnlySharedStringsTable strings,
            SheetContentsHandler sheetHandler, 
            InputStream sheetInputStream) throws IOException, SAXException {
        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        try {
            XMLReader sheetParser = SAXHelper.newXMLReader();
            ContentHandler handler = new XSSFSheetXMLHandler(
                  styles, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
         } catch(ParserConfigurationException e) {
            throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
         }
    }

    /**
     * Initiates the processing of the XLS workbook file to CSV.
     *
     * @throws IOException If reading the data from the package fails.
     * @throws SAXException if parsing the XML data fails.
     */
    public void process() throws IOException, OpenXML4JException, SAXException {
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int index = 0;
        while (iter.hasNext()) {
            InputStream stream = iter.next();
            String sheetName = iter.getSheetName();
            processSheet(styles, strings, new SheetToCSV(), stream);
            stream.close();
            ++index;
        }
    }

    public static void main(String[] args) throws Exception {
        int minColumns = 1;
        long startMini = System.currentTimeMillis();
        List<DataDTO> results = new ArrayList<DataDTO>(64);
        // The package open is instantaneous, as it should be.
        OPCPackage p = OPCPackage.open("C:/Users/92039/Desktop/print/demo.xlsx", PackageAccess.READ);
        ParseExcelUtil xlsx2csv = new ParseExcelUtil(p, results, minColumns);
        xlsx2csv.process();
        p.close();
        long endMini = System.currentTimeMillis();
        System.out.println(" take time is " + (endMini - startMini)/1000.0);
	}
}
