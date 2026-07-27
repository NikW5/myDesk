package com.psyduck.myDesk.persistenz;

import java.time.LocalDateTime;

public class Nachricht {

    private String benutzer;
	private String titel;
    private String inhalt;
    private LocalDateTime empfangenAm;
    private String vorschau;

    public Nachricht() {
    	
    }
    
    public String getBenutzer() {
    	return benutzer;
    }
    
    public void setBenutzer(String benutzer) {
    	this.benutzer = benutzer;
    }
    
    public String getTitel() {
    	return titel;
    }
    
    public void setTitel(String titel) {
    	this.titel = titel;
    }
    
    public String getInhalt() {
    	return inhalt;
    }
    
    public void setInhalt(String inhalt) {
    	this.inhalt = inhalt;
    }
    
    public LocalDateTime getEmpfangenAm() {
    	return empfangenAm;
    }
    
    public void setEmpfangenAm(LocalDateTime empfangenAm) {
    	this.empfangenAm = empfangenAm;
    }
    
    public String getVorschau() {
    	return vorschau;
    }
    
    public void setVorschau(String vorschau) {
    	this.vorschau = vorschau;
    }

}
