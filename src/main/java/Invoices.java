import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Invoices {

    String seller;
    String buyer;
    ArrayList<Car> cars;

    String description;

    public Invoices (String seller, String buyer, ArrayList<Car> cars, String description) {
        this.seller = seller;
        this.buyer = buyer;
        this.cars = cars;
        this.description = description;
    }

    public String generateInvoice() throws FileNotFoundException, DocumentException {
        Document document = new Document();
        Invoice invoice = new Invoice(this.seller, this.buyer, this.cars);
        System.out.println(invoice.getTitle());
        String path = "invoices/"+ invoice.getTitle() + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(path));
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Paragraph p1 = new Paragraph("FAKTURA: " + invoice.getTitle(), font);
        document.add(p1);
        Paragraph p2 = new Paragraph("Sprzedawca: " + invoice.getSeller(),font);
        document.add(p2);
        Paragraph p3 = new Paragraph("Nabywca: " + invoice.getBuyer(),font);
        document.add(p3);
        Font redFont = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.RED);
        Paragraph p4 = new Paragraph(description, redFont);
        document.add(p4);
        Paragraph emptyLine = new Paragraph(" ",font);
        document.add(emptyLine);
        PdfPTable table = new PdfPTable(4);
        double sum = 0;
        for(int i=0; i< invoice.getList().size();i++){
            PdfPCell c1 = new PdfPCell(new Phrase(Integer.toString(i+1), font));
            table.addCell(c1);
            int price = invoice.getList().get(i).getPrice();
            PdfPCell c2 = new PdfPCell(new Phrase(Integer.toString(price), font));
            table.addCell(c2);
            int vat = invoice.getList().get(i).getVat();
            PdfPCell c3 = new PdfPCell(new Phrase(Integer.toString(vat), font));
            table.addCell(c3);
            double val = (1+(vat*0.01))*price;
            sum += val;
            PdfPCell c4 = new PdfPCell(new Phrase(String.format("%.2f", val), font));
            table.addCell(c4);
            this.cars.get(i).setInvoice(true);
        }
        document.add(table);
        Paragraph p5 = new Paragraph("DO ZAPLATY " + String.format("%.2f", sum) + " PLN", font);
        document.add(p5);
        document.close();
        return "invoices/"+ invoice.getTitle() + ".pdf";
    }
}
