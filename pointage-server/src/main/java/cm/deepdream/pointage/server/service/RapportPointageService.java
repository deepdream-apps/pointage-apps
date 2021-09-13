package cm.deepdream.pointage.server.service;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import cm.deepdream.pointage.enums.JourSemaine;
import cm.deepdream.pointage.model.Employe;
import cm.deepdream.pointage.model.Pointage;
import cm.deepdream.pointage.model.UniteAdministrative;
import cm.deepdream.pointage.server.dao.EmployeDAO;
import cm.deepdream.pointage.server.dao.PointageDAO;
import cm.deepdream.pointage.server.util.HeaderFooterPageEvent;
import cm.deepdream.pointage.server.util.Report;
@Transactional
@Service
public class RapportPointageService {
	private Logger logger = Logger.getLogger(RapportPointageService.class.getName()) ;
	@Autowired
	private Report report ;
	@Autowired
	private EmployeDAO employeDAO ;
	@Autowired
	private PointageDAO pointageDAO ;
	@Autowired
	private JourNonOuvreService jourNonOuvreService ;
	
	public File genererRapportDetaille (LocalDate dateDebut, LocalDate dateFin) throws Exception {
		try {
			String fileName = "Rapport_pointage_detaille_"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy_HHmm"))+".pdf" ;
			Document document = new Document(PageSize.A4.rotate()) ;
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
			HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            writer.setPageEvent(event);
			document.open() ;			
			document.add(report.getEntete());
			
			PdfPTable titleTable = new PdfPTable(1);
			titleTable.setHorizontalAlignment(Element.ALIGN_CENTER) ;
			titleTable.setSpacingBefore (10.0f) ;
			Phrase titlePhrase = new Phrase("RAPPORT DE POINTAGE",
					new Font(FontFamily.TIMES_ROMAN, 14.0f, Font.BOLD));
			PdfPCell titleCell = new PdfPCell(titlePhrase);
			titleCell.setBorder(0);
			titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			titleTable.addCell(titleCell);
			
			PdfPCell subTitleCell = new PdfPCell(new Phrase(dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+
					" - "+dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 
					new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.BOLD)));
			subTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
			subTitleCell.setBorder(0);
			titleTable.addCell(subTitleCell) ;
			
			PdfPTable bodyTable = new PdfPTable (8) ; 
			bodyTable.setSpacingBefore (10.0f) ;
			bodyTable.setHorizontalAlignment(Element.ALIGN_CENTER) ;
			bodyTable.setWidthPercentage(90.f) ;
			bodyTable.setWidths(new int[]{5,15,20,15,25,15,15,15});
			
			document.add(titleTable) ;
			
			String[] headerCellLabs = new String[] {"N°", "Matricule", "Nom & Prénom", "Unité", "Jour", "Arrivée", "Départ", "Volume horaire"} ;
			
			for (String headerCellLab : headerCellLabs) {
				PdfPCell cell = new PdfPCell(new Phrase(headerCellLab, new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.BOLD)));
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY) ;
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
				bodyTable.addCell(cell);
			}
			
			int index = 1 ;
						
			for (LocalDate date = dateDebut ;  ! date.isAfter(dateFin) ; date = date.plusDays(1L)) {
				if(jourNonOuvreService.jourNonOuvre(date)) continue ;
				List<Pointage> listePointages = pointageDAO.findByDatePointage(date) ;
				List<Employe> listeEmployes = listePointages.stream().map(Pointage::getEmploye).distinct().
						sorted((emp1, emp2)->emp1.getNom().compareTo(emp2.getNom())).
						collect(Collectors.toList()) ;
				for (Employe employe : listeEmployes) {		
					List<Pointage> pointages = listePointages.stream().filter(p->p.getEmploye().equals(employe)).collect(Collectors.toList()) ;
					
					PdfPCell _numCell = new PdfPCell(new Phrase(Integer.toString(index), new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
					_numCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					_numCell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
					_numCell.setRowspan(pointages.size() == 0 ? 1: pointages.size());
					bodyTable.addCell(_numCell);
					
					PdfPCell _matell = new PdfPCell(new Phrase(employe.getMatricule(), 
							new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
					_matell.setHorizontalAlignment(Element.ALIGN_LEFT) ;
					_matell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					_matell.setRowspan(pointages.size() == 0 ? 1: pointages.size());
					bodyTable.addCell(_matell) ;
					
					PdfPCell _nomCell = new PdfPCell(new Phrase(employe.getNom()+" "+employe.getPrenom(), 
							new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
					_nomCell.setHorizontalAlignment(Element.ALIGN_LEFT) ;
					_nomCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					_nomCell.setRowspan(pointages.size() == 0 ? 1: pointages.size());
					bodyTable.addCell(_nomCell) ;
					
					PdfPCell _unitCell = new PdfPCell(new Phrase(employe.getUnite() == null ? "-":employe.getUnite().getAbreviation(), 
							new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
					_unitCell.setHorizontalAlignment(Element.ALIGN_LEFT) ;
					_unitCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					_unitCell.setRowspan(pointages.size() == 0 ? 1: pointages.size());
					bodyTable.addCell(_unitCell) ;
				
					PdfPCell _dateCell = new PdfPCell(new Phrase(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 
						new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
					_dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					_dateCell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
					_dateCell.setRowspan(pointages.size() == 0 ? 1: pointages.size());
					bodyTable.addCell(_dateCell);
				
					if(pointages.size() == 0) {
							PdfPCell _arrCell = new PdfPCell(new Phrase("-", new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
							_arrCell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
							bodyTable.addCell(_arrCell);
				
							PdfPCell _depCell = new PdfPCell(new Phrase("-", new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
							_depCell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
							bodyTable.addCell(_depCell) ;
					}else for(Pointage pointage : pointages) {
							PdfPCell _arrCell = new PdfPCell(new Phrase(pointage.getHeure() == null ? "-":pointage.getHeure().format(DateTimeFormatter.ofPattern("HH:mm")), 
								new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
							_arrCell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
							bodyTable.addCell(_arrCell);
			
							PdfPCell _depCell = new PdfPCell(new Phrase(pointage.getHeure() == null ? "-":pointage.getHeure().format(DateTimeFormatter.ofPattern("HH:mm")), 
								new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
							_depCell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
							bodyTable.addCell(_depCell) ;
						}	
						LocalTime volume = LocalTime.of(0, 0)  ;
						PdfPCell _volCell = new PdfPCell(new Phrase(volume.format(DateTimeFormatter.ofPattern("HH:mm")), 
								new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
						_volCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						_volCell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
						_volCell.setRowspan(pointages.size() == 0 ? 1: pointages.size());
						bodyTable.addCell(_volCell);
						index = index + 1 ;
					}					
				}
				document.add(bodyTable) ;
				document.close() ;
				return new File(fileName) ;
		}catch(Exception ex) {
			throw ex ;
		}
	}
	
	public File genererRapport (Employe employe, LocalDate dateDebut, LocalDate dateFin) throws Exception {
		try {
			String fileName = "Rapport_pointage_employe"+System.currentTimeMillis()+".pdf" ;
			Document document = new Document(PageSize.A4) ;
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
			HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            writer.setPageEvent(event);
			document.open() ;			
			//document.add(report.getEntete());
			
			PdfPTable titleTable = new PdfPTable(1);
			titleTable.setHorizontalAlignment(Element.ALIGN_CENTER) ;
			titleTable.setSpacingBefore (10.0f) ;
			Phrase titlePhrase = new Phrase("RAPPORT DE POINTAGE",
					new Font(FontFamily.TIMES_ROMAN, 14.0f, Font.BOLD));
			PdfPCell titleCell = new PdfPCell(titlePhrase);
			titleCell.setBorder(0);
			titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			titleTable.addCell(titleCell);
			
			PdfPCell subTitleCell = new PdfPCell(new Phrase(dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+
					" - "+dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 
					new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.BOLD)));
			subTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
			subTitleCell.setBorder(0);
			titleTable.addCell(subTitleCell) ;
			
			PdfPTable bodyTable = new PdfPTable(5) ; 
			bodyTable.setSpacingBefore (10.0f) ;
			bodyTable.setHorizontalAlignment(Element.ALIGN_CENTER) ;
			bodyTable.setWidthPercentage(90.f) ;
			bodyTable.setWidths(new int[]{5,20,25,20,20});
			
			document.add(titleTable) ;
			
			String[] headerCellLabs = new String[] {"N°", "Date", "Jour", "Arrivée", "Départ"} ;
			
			for (String headerCellLab : headerCellLabs) {
				PdfPCell cell = new PdfPCell(new Phrase(headerCellLab, new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.BOLD)));
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY) ;
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
				bodyTable.addCell(cell);
			}
			
			int index = 1 ;
			
			for (LocalDate date = dateDebut ;  ! date.isAfter(dateFin) ; date = date.plusDays(1L)) {
				if(jourNonOuvreService.jourNonOuvre(date)) continue ;
				
				List<Pointage> listePointages = pointageDAO.findByEmployeAndDatePointage(employe, date) ;
				
				PdfPCell _numCell = new PdfPCell(new Phrase(Integer.toString(index), new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
				_numCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				_numCell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
				_numCell.setRowspan(listePointages.size() == 0 ? 1: listePointages.size());
				bodyTable.addCell(_numCell);
				
				PdfPCell _dateCell = new PdfPCell(new Phrase(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 
						new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
				_dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				_dateCell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
				_dateCell.setRowspan(listePointages.size() == 0 ? 1: listePointages.size());
				bodyTable.addCell(_dateCell);
			
				PdfPCell _jourCell = new PdfPCell(new Phrase(JourSemaine.getLibelle(date.getDayOfWeek().ordinal()), new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
				_jourCell.setHorizontalAlignment(Element.ALIGN_LEFT) ;
				_jourCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				_jourCell.setRowspan(listePointages.size() == 0 ? 1: listePointages.size());
				bodyTable.addCell(_jourCell) ;
				
				if(listePointages.size() == 0) {
					PdfPCell _arrCell = new PdfPCell(new Phrase("-", new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
						_arrCell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
						bodyTable.addCell(_arrCell);
				
						PdfPCell _depCell = new PdfPCell(new Phrase("-", new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
						_depCell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
						bodyTable.addCell(_depCell) ;
				}else for(Pointage pointage : listePointages) {
						PdfPCell _arrCell = new PdfPCell(new Phrase(pointage.getHeure() == null ? "-":pointage.getHeure().format(DateTimeFormatter.ofPattern("HH:mm")), 
								new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
						_arrCell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
						bodyTable.addCell(_arrCell);
			
						PdfPCell _depCell = new PdfPCell(new Phrase(pointage.getHeure() == null ? "-":pointage.getHeure().format(DateTimeFormatter.ofPattern("HH:mm")), 
								new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
						_depCell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
						bodyTable.addCell(_depCell) ;
					}
				index = index + 1 ;
			}
			document.add(bodyTable) ;
			document.close() ;
			return new File(fileName) ;
		}catch(Exception ex) {
			throw ex ;
		}
	}
	
	public String genererRapport (UniteAdministrative unite, LocalDate dateDebut, LocalDate dateFin, String groupBy) throws Exception {
		return null ;
	}
	
	public String genererRapport (LocalDate dateDebut, LocalDate dateFin, String groupBy) throws Exception {
		return null ;
	}
	
	public String genererHistogramme (UniteAdministrative unite, LocalDate dateDebut, LocalDate dateFin) throws Exception {
		return null ;
	}
}
