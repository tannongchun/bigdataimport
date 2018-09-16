/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package com.sunducation.waterflow.utils;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.sunducation.waterflow.dto.DataDTO;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.crypto.Data;


public class ParseXMLUtil {

    private  final BlockingQueue<DataDTO> results ;

    public ParseXMLUtil(BlockingQueue<DataDTO> results) {
        this.results = results;
    }

    public void processFirstSheet(String filename) throws Exception {

        OPCPackage pkg = OPCPackage.open(filename, PackageAccess.READ);
        try {
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();
            XMLReader parser = fetchSheetParser(sst);
            // process the first sheet
            InputStream sheet2 = r.getSheetsData().next();
            InputSource sheetSource = new InputSource(sheet2);
            parser.parse(sheetSource);
            sheet2.close();
        } finally {
            pkg.close();
        }
    }

    public void processAllSheets(String filename) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename, PackageAccess.READ);
        try {
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();
            XMLReader parser = fetchSheetParser(sst);
            Iterator<InputStream> sheets = r.getSheetsData();
            while (sheets.hasNext()) {
                System.out.println("Processing new sheet:\n");
                InputStream sheet = sheets.next();
                InputSource sheetSource = new InputSource(sheet);
                parser.parse(sheetSource);
                sheet.close();
            }
        } finally {
            pkg.close();
        }
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader();
        ContentHandler handler = new SheetHandler(sst,this.results);
        parser.setContentHandler(handler);
        return parser;
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private static class SheetHandler extends DefaultHandler {
        private final SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private boolean inlineStr;
        private String row ="";
        private final  BlockingQueue<DataDTO> results;
        private  DataDTO dto  = new DataDTO();
        private final LruCache<Integer,String> lruCache = new LruCache<Integer,String>(50);

        private static class LruCache<A,B> extends LinkedHashMap<A, B> {
            private final int maxEntries;

            public LruCache(final int maxEntries) {
                super(maxEntries + 1, 1.0f, true);
                this.maxEntries = maxEntries;
            }

            @Override
            protected boolean removeEldestEntry(final Map.Entry<A, B> eldest) {
                return super.size() > maxEntries;
            }
        }
        
        private SheetHandler(SharedStringsTable sst,final BlockingQueue<DataDTO> results) {
            this.sst = sst;
            this.results = results;
        }

        @Override
        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            // c => cell
            if(name.equals("c")) {
                // Print the cell reference
                row = attributes.getValue("r") ;
                String cellType = attributes.getValue("t");
                nextIsString = cellType != null && cellType.equals("s");
                inlineStr = cellType != null && cellType.equals("inlineStr");
            }
            // Clear contents cache
            lastContents = "";
        }

        @Override
        public void endElement(String uri, String localName, String name)
                throws SAXException {
            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            if(nextIsString) {
                Integer idx = Integer.valueOf(lastContents);
                lastContents = lruCache.get(idx);
                if (lastContents == null && !lruCache.containsKey(idx)) {
                    lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                    lruCache.put(idx, lastContents);
                }
                nextIsString = false;
            }

            // v => contents of a cell
            // Output after we've seen the string contents
            if(name.equals("v") || (inlineStr && name.equals("c"))) {
                char rowC=row.substring(0,1).charAt(0);
                int  rowNum=Integer.parseInt(row.substring(1));
                if(rowNum <=1){
                    return;
                }
                switch (rowC){
                    case 65:
                        dto.setNo(Integer.parseInt(lastContents.toString()));
                        break;
                    case 66:
                        dto.setGrade((String) lastContents);
                        break;
                    case 67:
                        dto.setName((String) lastContents);
                        break;
                    case 68:
                        dto.setAge(Integer.parseInt(lastContents.toString()));
                        break;
                }
            }
            if(name.equals("row")){
            int  rowNum=Integer.parseInt(row.substring(1));
            if(rowNum <=1){
                return;
            }
              results.offer(dto);
              dto = new DataDTO();

            }

        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException { // NOSONAR
            lastContents += new String(ch, start, length);
        }
    }

    public static void main(String[] args) throws Exception {
        long startMini = System.currentTimeMillis();
        BlockingQueue<DataDTO> results = new ArrayBlockingQueue<DataDTO>(16);
        ParseXMLUtil xmlUtil = new ParseXMLUtil(results);
        xmlUtil.processFirstSheet("C:/Users/92039/Desktop/print/demo2.xlsx");
        System.out.println(results.size());
        long endMini = System.currentTimeMillis();
        System.out.println(" take time is " + (endMini - startMini)/1000.0);
    }
}
