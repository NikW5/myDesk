package com.psyduck.myDesk.persistenz;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "todo")
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String text;
    private boolean erledigt;
    private LocalDate faelligAm;

    protected ToDo() {
    }

    public ToDo(String text) {
        this.text = text;
        this.erledigt = false;
        this.faelligAm = null;
    }

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }
    
    public void setText(String text) {
    	this.text = text;
    }

    public boolean isErledigt() {
        return erledigt;
    }

    public void setErledigt(boolean erledigt) {
        this.erledigt = erledigt;
    }
    
    public LocalDate getFaelligAm() {
    	return faelligAm;
    }
    
    public void setFaelligAm(LocalDate faelligAm) {
    	this.faelligAm = faelligAm;
    }
}
