package utils;

import Model.Class.Employee;
import Model.Class.Item;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public class PrinterPDF {
    public void ItemsReport(List<Item> list) throws IOException, DocumentException, URISyntaxException {

        // Name file
        LocalDateTime now = LocalDateTime.now();
        String[] dateString = DateUtil.formatDate(now).split(" ");
        String[] time = dateString[1].split(":");
        String temp = time[0]+"h"+time[1]+"m";
        String name = temp +  "-Date-"+dateString[0] + "ItemsReport.pdf";

        Document document = new Document();
        PdfWriter pdfWriter = null;

        // Output
        String path = "docs/items/"+ name;
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(path));

        Font timesRoman12Bold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0,0,0));
        Font timesRoman12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0,0,0));

        document.open();

        Paragraph paragraph = new Paragraph("Summary Items Report\n");

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(90f);
        insertCell(table, "Name", Element.ALIGN_CENTER, 1, timesRoman12Bold);
        insertCell(table, "Cost", Element.ALIGN_CENTER, 1, timesRoman12Bold);
        insertCell(table, "Price", Element.ALIGN_CENTER, 1, timesRoman12Bold);
        insertCell(table, "Quantity", Element.ALIGN_CENTER, 1, timesRoman12Bold);
        insertCell(table, "Profit", Element.ALIGN_CENTER, 1, timesRoman12Bold);
        table.setHeaderRows(1);

        float profit, cost, price, sumProfit = 0;
        int quantity;
        for (int i = 0; i < list.size(); i++) {

            cost = list.get(i).getCostItem();
            price = list.get(i).getPriceItem();
            quantity = list.get(i).getQuantityItem();
            profit = (price - cost)*quantity;
            sumProfit += profit;
            insertCell(table, list.get(i).getNameItem(), Element.ALIGN_CENTER, 1, timesRoman12);
            insertCell(table, String.valueOf(cost), Element.ALIGN_CENTER, 1, timesRoman12);
            insertCell(table, String.valueOf(price), Element.ALIGN_CENTER, 1, timesRoman12);
            insertCell(table, String.valueOf(quantity), Element.ALIGN_CENTER, 1, timesRoman12);
            insertCell(table, String.valueOf(profit), Element.ALIGN_CENTER, 1, timesRoman12);
        };

        String note = "\nStore: ABC Store"
                + "\nSum profit: " + sumProfit
                + "\nDate: " + DateUtil.formatDate(now) + "\n\n\n";

        paragraph.add(note);
        paragraph.add(table);
        document.add(paragraph);

        document.close();
        pdfWriter.close();
    }
    public void EmployeesReport(List<Employee> list) throws IOException, DocumentException, URISyntaxException {

        // Name file
        LocalDateTime now = LocalDateTime.now();
        String[] dateString = DateUtil.formatDate(now).split(" ");
        String[] time = dateString[1].split(":");
        String temp = time[0]+"h"+time[1]+"m";
        String name = temp +  "-Date-"+dateString[0] + "EmployeesReport.pdf";

        Document document = new Document();
        PdfWriter pdfWriter = null;

        // Output
        String path = "docs/employees/"+ name;
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(path));

        Font timesRoman12Bold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0,0,0));
        Font timesRoman12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0,0,0));

        document.open();

        Paragraph paragraph = new Paragraph("Summary Items Report");

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(90f);
        insertCell(table, "Name", Element.ALIGN_CENTER, 1, timesRoman12Bold);
        insertCell(table, "Salary", Element.ALIGN_CENTER, 1, timesRoman12Bold);
        insertCell(table, "Phone", Element.ALIGN_CENTER, 1, timesRoman12Bold);
        insertCell(table, "Position", Element.ALIGN_CENTER, 1, timesRoman12Bold);
        insertCell(table, "Start Date", Element.ALIGN_CENTER, 1, timesRoman12Bold);
        table.setHeaderRows(1);

        for (int i = 0; i < list.size(); i++) {
            insertCell(table, list.get(i).getName(), Element.ALIGN_CENTER, 1, timesRoman12);
            insertCell(table, String.valueOf(list.get(i).getSalary()), Element.ALIGN_CENTER, 1, timesRoman12);
            insertCell(table, String.valueOf(list.get(i).getPhone()), Element.ALIGN_CENTER, 1, timesRoman12);
            insertCell(table, String.valueOf(list.get(i).getPosition()), Element.ALIGN_CENTER, 1, timesRoman12);
            insertCell(table, String.valueOf(list.get(i).getStartDate()), Element.ALIGN_CENTER, 1, timesRoman12);
        };

        paragraph.add(table);
        document.add(paragraph);

        document.close();
        pdfWriter.close();
    }

    private void addCustomRows(PdfPTable table) throws URISyntaxException, IOException, BadElementException {
        Path path = Paths.get(ClassLoader.getSystemResource("Java_logo.png").toURI());
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        img.scalePercent(10);

        PdfPCell imageCell = new PdfPCell(img);
        table.addCell(imageCell);

        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);

        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(verticalAlignCell);
    }

    private void addRows(PdfPTable table) {
        table.addCell("row 1, col 1");
        table.addCell("row 1, col 2");
        table.addCell("row 1, col 3");
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Name", "Cost", "Price", "Quanitty", "Profit")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }


    public void insertCell(PdfPTable table, String text, int align, int colspan, Font font){
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        if(text.trim().equals(""))
            cell.setMinimumHeight(10f);
        table.addCell(cell);
    }
}
