package com.psyduck.myDesk.persistenz;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "anhang")
public class Anhang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "dateiname", nullable = false)
    private String dateiname;

    @Column(name = "dateityp")
    private String dateityp;

    @Column(name = "daten")
    private byte[] inhalt;

    @ManyToOne
    @JoinColumn(name = "nachrichten_id", nullable = false)
    private Nachricht nachricht;

    protected Anhang() {
    }

    public Anhang(
            String dateiname,
            String dateityp,
            byte[] inhalt) {

        this.dateiname = dateiname;
        this.dateityp = dateityp;
        this.inhalt = inhalt;
    }

    public Integer getId() {
        return id;
    }

    public String getDateiname() {
        return dateiname;
    }

    public String getDateityp() {
        return dateityp;
    }

    public byte[] getInhalt() {
        return inhalt;
    }

    public Nachricht getNachricht() {
        return nachricht;
    }

    public void setNachricht(Nachricht nachricht) {
        this.nachricht = nachricht;
    }
}
