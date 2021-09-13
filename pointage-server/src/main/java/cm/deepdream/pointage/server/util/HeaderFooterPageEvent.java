package cm.deepdream.pointage.server.util;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
public class HeaderFooterPageEvent extends PdfPageEventHelper {

    private PdfTemplate t;
    private Image total;

    public void onOpenDocument(PdfWriter writer, Document document) {
        t = writer.getDirectContent().createTemplate(30, 16);
        try {
            total = Image.getInstance(t);
           total.setRole(PdfName.ARTIFACT);
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        addHeader(writer);
        addFooter(writer);
    }

    private void addHeader(PdfWriter writer){
       
    }

    private void addFooter(PdfWriter writer){
        PdfPTable footer = new PdfPTable(1);
        try {
            // set defaults
            footer.setWidths(new int[]{5});
            footer.setTotalWidth(527);
            footer.setLockedWidth(true);
            footer.getDefaultCell().setFixedHeight(40);
            footer.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            footer.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

            // add copyright
            //footer.addCell(new Phrase("", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));

            // add current page count
            footer.setHorizontalAlignment(Element.ALIGN_CENTER);
            footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            footer.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
            footer.addCell(new Phrase(String.format("Page %d", writer.getPageNumber()), new Font(Font.FontFamily.TIMES_ROMAN, 10)));

            // add placeholder for total page count
            //PdfPCell totalPageCount = new PdfPCell(total);
           // totalPageCount.setBorder(Rectangle.TOP);
           // totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY);
            //footer.addCell(totalPageCount);

            // write page
            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            footer.writeSelectedRows(0, -1, 34, 50, canvas);
            canvas.endMarkedContentSequence();
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    public void onCloseDocument(PdfWriter writer, Document document) {
      //  int totalLength = String.valueOf(writer.getPageNumber()).length();
     //   int totalWidth = totalLength * 5;
      //  ColumnText.showTextAligned(t, Element.ALIGN_RIGHT,
        //        new Phrase(String.valueOf(writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)),
          //      totalWidth, 6, 0);
    }
}