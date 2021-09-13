package cm.deepdream.pointage.server.util;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
@Component
public class Report {
	private Logger logger = Logger.getLogger(Report.class.getName()) ;
	@Autowired
	private Environment env ;

	public  PdfPTable getEntete() throws Exception{
		PdfPTable table = null ;
        try {
        	table = new PdfPTable(new float[]{3.5f, 3.0f, 3.5f}) ;
        	table.setWidthPercentage(90.f);
            
        	Phrase phrase11 = new Phrase("REPUBLIQUE DU CAMEROUN", new Font(FontFamily.TIMES_ROMAN, 9.0f, Font.BOLD)) ;
            Phrase phrase12 = new Phrase("Paix - Travail - Patrie", new Font(FontFamily.TIMES_ROMAN, 9.0f, Font.BOLD)) ;
            Phrase phrase13 = new Phrase("------------", new Font(FontFamily.TIMES_ROMAN, 7.0f, Font.BOLD)) ;
            Phrase phrase14 = new Phrase("AGENCE NATIONALE DES TECHNOLOGIES DE", new Font(FontFamily.TIMES_ROMAN, 7.0f, Font.BOLD)) ;
            Phrase phrase15 = new Phrase("L'INFORMATION ET DE LA COMMUNICATION", new Font(FontFamily.TIMES_ROMAN, 7.0f, Font.BOLD)) ;
            Phrase phrase16 = new Phrase("------------", new Font(FontFamily.TIMES_ROMAN, 7.0f, Font.BOLD)) ;
            
            PdfPCell cell11 = new PdfPCell(phrase11) ;
            cell11.setBorder(0);
            cell11.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell12 = new PdfPCell(phrase12);
            cell12.setBorder(0);
            cell12.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell13 = new PdfPCell(phrase13);
            cell13.setBorder(0);
            cell13.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell14 = new PdfPCell(phrase14);
            cell14.setBorder(0);
            cell14.setHorizontalAlignment(Element.ALIGN_CENTER) ;

            PdfPCell cell15 = new PdfPCell(phrase15) ;
            cell15.setBorder(0);
            cell15.setHorizontalAlignment(Element.ALIGN_CENTER) ;
            
            PdfPCell cell16 = new PdfPCell(phrase16) ;
            cell16.setBorder(0);
            cell16.setHorizontalAlignment(Element.ALIGN_CENTER) ;
            
           PdfPCell cimage = null ;
            try {
            	Image image = Image.getInstance(env.getRequiredProperty("app.logo.filename")) ;
                cimage = new PdfPCell(image) ;
                cimage.setHorizontalAlignment(Element.ALIGN_CENTER) ;
                cimage.setVerticalAlignment(Element.ALIGN_MIDDLE) ;
                cimage.setRowspan(9) ;
                cimage.setBorder(0) ;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
      
            Phrase phrase21 = new Phrase("REPUBLIC OF CAMEROON", new Font(FontFamily.TIMES_ROMAN, 9.0f, Font.BOLD));
            Phrase phrase22 = new Phrase("Peace - Work - Fatherland", new Font(FontFamily.TIMES_ROMAN, 9.0f, Font.BOLD));
            Phrase phrase23 = new Phrase("------------", new Font(FontFamily.TIMES_ROMAN, 7.0f, Font.BOLD));
            Phrase phrase24 = new Phrase("NATIONAL AGENCY FOR INFORMATION AND", new Font(FontFamily.TIMES_ROMAN, 7.0f, Font.BOLD));
            Phrase phrase25 = new Phrase("COMMUNICATION TECHNOLOGIES", new Font(FontFamily.TIMES_ROMAN, 7.0f, Font.BOLD));

            PdfPCell cell21 = new PdfPCell(phrase21);
            cell21.setBorder(0);
            cell21.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell22 = new PdfPCell(phrase22);
            cell22.setBorder(0);
            cell22.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell23 = new PdfPCell(phrase23);
            cell23.setBorder(0);
            cell23.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell24 = new PdfPCell(phrase24);
            cell24.setBorder(0);
            cell24.setHorizontalAlignment(Element.ALIGN_CENTER) ;

            PdfPCell cell25 = new PdfPCell(phrase25) ;
            cell25.setBorder(0);
            cell25.setHorizontalAlignment(Element.ALIGN_CENTER) ;

            table.addCell(cell11) ;
            table.addCell(cimage);
            table.addCell(cell21) ;
            
            table.addCell(cell12) ;
            table.addCell(cell22) ;
            
            table.addCell(cell13) ;
            table.addCell(cell23) ;
            
            table.addCell(cell14) ;
            table.addCell(cell24) ;
            
            table.addCell(cell15) ;
            table.addCell(cell25) ;
            return table;
        } catch (Exception ex) {
            throw ex ;
        }
	}
	
}
