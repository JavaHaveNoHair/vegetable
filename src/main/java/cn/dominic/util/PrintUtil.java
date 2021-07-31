package cn.dominic.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description :
 * @Reference :
 * @Author : dominic
 * @CreateDate : 2021/7/27 22:42
 * @Modify:
 **/
public class PrintUtil {

    public static void printPdf(String printerName, String pdfPath){
        try {
            File file = new File(pdfPath);
            PDDocument document = PDDocument.load(file);
            PrinterJob job = PrinterJob.getPrinterJob();
            for (PrintService ps : PrinterJob.lookupPrintServices()) {
                String psName = ps.toString();
                if (psName.contains(printerName)) {
                    job.setPrintService(ps);
                    break;
                }
            }
            job.setPageable(new PDFPageable(document));

            Paper paper = new Paper();
            paper.setSize(598,842); // 1/72 inch
            paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight()); // no margins
            PageFormat pageFormat = new PageFormat();
            pageFormat.setPaper(paper);
            Book book = new Book();
            book.append(new PDFPrintable(document, Scaling.ACTUAL_SIZE), pageFormat, 1);
            job.setPageable(book);
            job.print();
        } catch (Exception e) {
            throw new RuntimeException("Print err:" + e.getMessage());
        }
    }

    private static void test() throws PrinterException {
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.PNG;
//        DocFlavor flavor = DocFlavor.STRING.TEXT_HTML;

        //可用的打印机列表(字符串数组)
        PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);

        Map<String, PrintService> printMap = new HashMap<>();

        for (int i = 0; i < printService.length; i++) {
            String serviceName = printService[i].getName();
            printMap.put(serviceName, printService[i]);
            System.out.println("serviceName=" + serviceName);
            DocFlavor[] docs = printService[i].getSupportedDocFlavors();
            if(docs!=null) {
                for(DocFlavor doc : docs) {
                    System.out.println(serviceName+"支持类型:" + doc);
                }
            }
        }
        System.out.println("printMap = "+printMap);
        //当前默认打印机
        PrintService PS = PrintServiceLookup.lookupDefaultPrintService();
        String defaaultName = PS.getName();
        //默认打印机名称
        System.out.println("defaaultName=" + defaaultName);
//        PrintService printService1 = printMap.get("58mm Series Printer");
        PrintService printService1 = printMap.get("NPIA7349C (HP LaserJet Pro MFP M226dn)");
        System.out.println("printService1=" + printService1);
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintService(printService1);
    }
}
