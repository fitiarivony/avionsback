package com.example.flotte.assurance;

import dao.GenericDAO;
import frame.AnnotMap;
import frame.Attribut;
import utils.Connect;

import java.util.ArrayList;
import java.util.Date;

@AnnotMap(nomTable="assurance_avion_detail")
public class Assurance_avion_detail {
	@Attribut(attr="idavion")
	String idavion;
	@Attribut(attr="numeromatricule")
	String numeromatricule;
	@Attribut(attr="puissance")
	Double puissance;
	@Attribut(attr="prix_neuf")
	Double prix_neuf;
	@Attribut(attr="dateutilisation")
	Date dateutilisation;
	@Attribut(attr="datedebut")
	Date datedebut;
	@Attribut(attr="datefin")
	Date datefin;
	@Attribut(attr="prix")
	Double prix;
	@Attribut(attr="idtypeassurance")
	String idtypeassurance;
	@Attribut(attr="nomassurance")
	String nomassurance;
	public String getIdavion() {
		return idavion;
	}
	public void setIdavion(String idavion) {
		this.idavion = idavion;
	}
	public String getNumeromatricule() {
		return numeromatricule;
	}
	public void setNumeromatricule(String numeromatricule) {
		this.numeromatricule = numeromatricule;
	}
	public Double getPuissance() {
		return puissance;
	}
	public void setPuissance(Double puissance) {
		this.puissance = puissance;
	}
	public Double getPrix_neuf() {
		return prix_neuf;
	}
	public void setPrix_neuf(Double prix_neuf) {
		this.prix_neuf = prix_neuf;
	}
	public Date getDateutilisation() {
		return dateutilisation;
	}
	public void setDateutilisation(Date dateutilisation) {
		this.dateutilisation = dateutilisation;
	}
	public Date getDatedebut() {
		return datedebut;
	}
	public void setDatedebut(Date datedebut) {
		this.datedebut = datedebut;
	}
	public Date getDatefin() {
		return datefin;
	}
	public void setDatefin(Date datefin) {
		this.datefin = datefin;
	}
	public Double getPrix() {
		return prix;
	}
	public void setPrix(Double prix) {
		this.prix = prix;
	}
	public String getIdtypeassurance() {
		return idtypeassurance;
	}
	public void setIdtypeassurance(String idtypeassurance) {
		this.idtypeassurance = idtypeassurance;
	}
	public String getNomassurance() {
		return nomassurance;
	}
	public void setNomassurance(String nomassurance) {
		this.nomassurance = nomassurance;
	}
	public ArrayList<Object> getExpiration(int mois,Connect conn) throws Exception{
		 ArrayList<Object> liste=GenericDAO.executeQuery("  SELECT *  from assurance_avion_detail where "
		 		+ "EXTRACT(YEAR FROM AGE( datefin,  current_timestamp)) * 12 "
		 		+ "+ EXTRACT(MONTH FROM AGE( datefin,  current_timestamp))="+mois
		 		, conn, new Assurance_avion_detail());
		return liste;
	}
	
}
