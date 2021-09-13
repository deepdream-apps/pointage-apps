package cm.deepdream.pointage.server.service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import cm.deepdream.pointage.enums.EtatContrat;
import cm.deepdream.pointage.model.Employe;
import cm.deepdream.pointage.server.dao.EmployeDAO;
import cm.deepdream.pointage.server.dao.SequenceDAO;
import cm.deepdream.pointage.server.util.CryptoSystem;
import cm.deepdream.pointage.server.util.EmailSender;
import cm.deepdream.pointage.server.util.QRCodeGenerator;
import cm.deepdream.pointage.server.util.WhatsAppSender;
@Transactional
@Service
public class EmployeService {
	private Logger logger = Logger.getLogger(EmployeService.class.getName()) ;
	@Autowired
	private EmployeDAO employeDAO ;
	@Autowired
	private SequenceDAO sequenceDAO ;
	@Autowired
	private EmailSender emailSender ;
	@Autowired
	private WhatsAppSender whatsAppSender ;
	@Autowired
	private QRCodeGenerator qRCodeGenerator ;
	@Autowired
	private CryptoSystem cryptoSystem ;

	public Employe creer(Employe employe) throws Exception {
		try{
			logger.info("Lancement de la creation d'un employe") ;	
			employe.setId(sequenceDAO.nextId(Employe.class.getName())) ;
			
			employe.setLibelleEtat(EtatContrat.getLibelle(employe.getEtat())) ;
			employe.setUnite(employe.getPoste()==null?null:employe.getPoste().getUniteAdministrative());
			employe.setDateCreation(LocalDateTime.now());
			employe.setDateDernMaj(LocalDateTime.now());
			employe.setTexte(getTexte(employe));
			employeDAO.save(employe) ;
			String cipherText = cryptoSystem.encrypt(employe.getTexte()) ;
			String qrCodePath = qRCodeGenerator.generateQRCodeImage(cipherText, 640, 480) ;
			Map<String, Object> params = new HashMap<String, Object>() ;
			params.put("civilite", employe.getCivilite()) ;
			params.put("nom", employe.getNom()) ;
			try {
				emailSender.sendMessage(employe.getEmail(), "Enregistrement au pointeur biométrique", "qr-code-sending-email.html", 
					qrCodePath, params) ;
			}catch(Exception ex) {}
			try {
				//whatsAppSender.sendMessage(employe.getTelephoneWhatsapp(), "qr-code-sending-whatsapp.html", qrCodePath, params) ;
			}catch(Exception ex) {}
			
			return employe ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public void supprimer(Employe employe) throws Exception {
		try{
			logger.info("Lancement de la suppression d'un employe");
			employeDAO.delete(employe) ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public Employe rechercher(long id) throws Exception {
		try{
			logger.info("Lancement de la recherche d'un employe");
			Employe employe = employeDAO.findById(id).orElseThrow(Exception::new) ;
			return employe ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}

	
	public List<Employe> rechercher(Employe employe) throws Exception {
		try{
			logger.info("Lancement de la recherche des employes");
			Iterable<Employe> employes = employeDAO.findAll() ;
			List<Employe> listeEmployes = new ArrayList<Employe>() ;
			employes.forEach(listeEmployes::add) ;
			return listeEmployes ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}


	public Employe modifier(Employe employe) throws Exception {
		try{
			logger.info("Lancement de la modification d'un employe");			
			employe.setTexte(getTexte(employe));
			employe.setLibelleEtat(EtatContrat.getLibelle(employe.getEtat())) ;
			employe.setDateDernMaj(LocalDateTime.now());
			employeDAO.save(employe) ;
			return employe ;
		}catch (Exception ex){
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex ;
		}
	}
	
	private String getTexte(Employe employe) {
		logger.info("Unite : "+employe.getUnite()) ;
		String employeTexte = employe.getId()+"|"+
				employe.getCivilite()+"|"+
				employe.getMatricule()+"|"+
				employe.getNom()+"|"+
				employe.getPrenom()+"|"+
				employe.getSexe()+"|"+
				(employe.getUnite()==null ?"":employe.getUnite().getId())+"|"+
				(employe.getUnite()==null?"":employe.getUnite().getLibelle())+"|"+				
				(employe.getPoste()==null?"":employe.getPoste().getId())+"|"+
				(employe.getPoste()==null ?"":employe.getPoste().getLibelle())+"|"+
				(employe.getStatut()==null?"":employe.getStatut().getId())+"|"+
				(employe.getStatut()==null?"":employe.getStatut().getLibelle())+"|"+
				employe.getEmail()+"|"+employe.getTelephone()+"|"+employe.getTelephoneWhatsapp()+"|"+employe.getEtat()+"|"+
				employe.getLangue()+"|"+employe.getDateDernMaj().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))		;
		return employeTexte ;
	}

}
