package utils;

import Model.Class.Bill_Item;
import Model.Class.Employee;
import Model.Class.Item;
import Model.Class.Store;
import Model.DAO.StoreDAO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.prefs.Preferences;

public class PrinterPDF {

    private Preferences preferences;
    private Store store;
    private BaseFont baseFont;
    private Font boldFont;
    private Font normalFont;

    private final float SIZE_NORMAL_FONT = 12;
    private final float SIZE_BOLD_FONT = 14;

    public PrinterPDF() throws IOException, DocumentException {
        preferences = Preferences.userNodeForPackage(Employee.class);
        StoreDAO storeDAO = new StoreDAO();
        store = storeDAO.getStoreById(preferences.getInt("defaultStore", -1));

        String font_path = getClass().getResource("/fonts/times.ttf").getPath();
        baseFont = BaseFont.createFont(font_path, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        boldFont =  new Font(baseFont, 12, Font.BOLD, new BaseColor(0,0,0));
        boldFont.setStyle(Font.BOLD);
        boldFont.setSize(SIZE_BOLD_FONT);
        boldFont.setColor(0,0,0);

        normalFont =  new Font(baseFont,12,Font.NORMAL ,new BaseColor(0,0,0));
        normalFont.setSize(SIZE_NORMAL_FONT);
        normalFont.setColor(0,0,0);
    }
    public void ItemsReport(List<Item> list) throws IOException, DocumentException, URISyntaxException {

        // Name file
        LocalDateTime now = LocalDateTime.now();
        String name = nameFile(now) + "-Items.pdf";

        Document document = new Document();
        PdfWriter pdfWriter = null;

        // Output
        String path = "docs/items/"+ name;
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(path));

        document.open();

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(90f);
        insertCell(table, "Tên sản phẩm", Element.ALIGN_CENTER, 1, boldFont);
        insertCell(table, "Giá gốc", Element.ALIGN_CENTER, 1, boldFont);
        insertCell(table, "Giá bán", Element.ALIGN_CENTER, 1, boldFont);
        insertCell(table, "Số lượng", Element.ALIGN_CENTER, 1, boldFont);
        insertCell(table, "Lợi nhuận", Element.ALIGN_CENTER, 1, boldFont);
        table.setHeaderRows(1);

        float profit, cost, price, sumProfit = 0;
        int quantity;
        for (int i = 0; i < list.size(); i++) {

            cost = list.get(i).getCostItem();
            price = list.get(i).getPriceItem();
            quantity = list.get(i).getQuantityItem();
            profit = (price - cost)*quantity;
            sumProfit += profit;
            insertCell(table, list.get(i).getNameItem(), Element.ALIGN_CENTER, 1, normalFont);
            insertCell(table, String.valueOf(cost), Element.ALIGN_CENTER, 1, normalFont);
            insertCell(table, String.valueOf(price), Element.ALIGN_CENTER, 1, normalFont);
            insertCell(table, String.valueOf(quantity), Element.ALIGN_CENTER, 1, normalFont);
            insertCell(table, String.valueOf(profit), Element.ALIGN_CENTER, 1, normalFont);
        };

        String infoStore = "Tên cửa hàng: "+ this.store.getNameStore()
                        + "\nĐịa chỉ: " + this.store.getAddressStore()
                        + "\nTên công ty: " + this.store.getCompany().getNameCompany()
                + "\n\n\nTổng lợi nhuận: " + sumProfit
                + "\nNgày: " + DateUtil.formatDate(now);

        String separateArea = "\n\n\n";

        Paragraph paragraph = new Paragraph();
        paragraph.setFont(boldFont);
        paragraph.add("Thống kê hàng hóa");
        paragraph.setFont(normalFont);
        paragraph.add(infoStore);
        paragraph.add(separateArea);
        paragraph.add(table);
        document.add(paragraph);

        document.close();
        pdfWriter.close();
    }
    public void EmployeesReport(List<Employee> list) throws IOException, DocumentException, URISyntaxException {

        // Name file
        LocalDateTime now = LocalDateTime.now();
        String name = nameFile(now) + "-Employee.pdf";

        Document document = new Document();
        PdfWriter pdfWriter = null;

        // Output
        String path = "docs/employees/"+ name;
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(path));

        document.open();

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(90f);
        insertCell(table, "Tên nhân viên", Element.ALIGN_CENTER, 1, boldFont);
        insertCell(table, "Mức lương", Element.ALIGN_CENTER, 1, boldFont);
        insertCell(table, "SĐT", Element.ALIGN_CENTER, 1, boldFont);
        insertCell(table, "Chức vụ", Element.ALIGN_CENTER, 1, boldFont);
        insertCell(table, "Ngày làm việc", Element.ALIGN_CENTER, 1, boldFont);
        table.setHeaderRows(1);

        for (int i = 0; i < list.size(); i++) {
            insertCell(table, list.get(i).getName(), Element.ALIGN_CENTER, 1, normalFont);
            insertCell(table, String.valueOf(list.get(i).getSalary()), Element.ALIGN_CENTER, 1, normalFont);
            insertCell(table, String.valueOf(list.get(i).getPhone()), Element.ALIGN_CENTER, 1, normalFont);
            if(list.get(i).getPosition() == 0)
                insertCell(table, "Nhân viên", Element.ALIGN_CENTER, 1, normalFont);
            else insertCell(table, "Quản lý", Element.ALIGN_CENTER, 1, normalFont);
            insertCell(table, DateUtil.formatDate(list.get(i).getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()), Element.ALIGN_CENTER, 1, normalFont);
        };

        String infoStore = "Tên cửa hàng: "+ this.store.getNameStore()
                + "\nĐịa chỉ: " + this.store.getAddressStore()
                + "\nTên công ty: " + this.store.getCompany().getNameCompany()
                + "\nNgày: " + DateUtil.formatDate(now) + "\n\n\n";

        String separateArea = "\n\n\n";
        Paragraph paragraph = new Paragraph();
        paragraph.setFont(normalFont);
        paragraph.add(infoStore);
        paragraph.add(separateArea);
        paragraph.add(table);
        document.add(paragraph);

        document.close();
        pdfWriter.close();
    }
    public void payment(List<Bill_Item> list, String nameCustomer) throws IOException, DocumentException, URISyntaxException {
        // Name file
        LocalDateTime now = LocalDateTime.now();
        String name = nameFile(now) + "-Bill.pdf";

        Document document = new Document(PageSize.A6, 10,10,10,10);
        PdfWriter pdfWriter = null;

        // Output
        String path = "docs/bills/"+ name;
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(path));

        document.open();

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(90f);
        insertCell(table, "Tên sản phẩm", Element.ALIGN_CENTER, 1, boldFont);
        insertCell(table, "Giá", Element.ALIGN_CENTER, 1, boldFont);
        insertCell(table, "Số lượng", Element.ALIGN_CENTER, 1, boldFont);
        insertCell(table, "Tổng", Element.ALIGN_CENTER, 1, boldFont);
        table.setHeaderRows(1);

        float sum = 0;
        float discount = 0;
        for (int i = 0; i < list.size(); i++) {
            insertCell(table, list.get(i).getItem().getNameItem(), Element.ALIGN_CENTER, 1, normalFont);
            insertCell(table, String.valueOf(list.get(i).getTempPrice()), Element.ALIGN_CENTER, 1, normalFont);
            insertCell(table, String.valueOf(list.get(i).getCount()), Element.ALIGN_CENTER, 1, normalFont);
            insertCell(table, String.valueOf(list.get(i).getTotal()), Element.ALIGN_CENTER, 1, normalFont);
            sum+= list.get(i).getTotal(); // Thành tiền 1 món hàng
            discount += list.get(i).getDiscount(); // Discount 1 món hàng
        };

        String infoStore =
                  "Tên cửa hàng: "+ this.store.getNameStore()
                + "\nĐịa chỉ: " + this.store.getAddressStore()
                + "\nTên công ty: " + this.store.getCompany().getNameCompany();

        String separateArea = "\n\n\n";

        String content = "Hóa đơn tính tiền \n"
                        + "Khách hàng: " + nameCustomer+"\n";

        String summary = "Tổng số tiền: " + sum // Thành tiền chưa tính discount
                + "\nTổng tiền giảm: " + discount
                + "\nTổng cộng: " + sum; // Thành tiền sau khi tính discount


        Paragraph paragraph = new Paragraph();
        paragraph.setFont(normalFont);

        paragraph.add(infoStore);
        paragraph.add(separateArea);
        paragraph.add(content);
        paragraph.add(table);
        paragraph.add(separateArea);
        paragraph.add(summary);
        document.add(paragraph);

        document.close();
        pdfWriter.close();
    }


    public void insertCell(PdfPTable table, String text, int align, int colspan, Font font){
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        if(text.trim().equals(""))
            cell.setMinimumHeight(10f);
        table.addCell(cell);
    }

    public String nameFile(LocalDateTime now){
        String[] dateString = DateUtil.formatDate(now).split(" ");
        String[] time = dateString[1].split(":");
        String name = dateString[0]+"-"+time[0]+time[1]+time[2];
        return name;
    }
}
