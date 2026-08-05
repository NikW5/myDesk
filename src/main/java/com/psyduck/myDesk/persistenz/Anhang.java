package com.psyduck.myDesk.persistenz;

public class Anhang {

    private final String dateiname;
    private final byte[] inhalt;

    public Anhang(String dateiname, byte[] inhalt) {
        this.dateiname = dateiname;
        this.inhalt = inhalt;
    }

    public String getDateiname() {
        return dateiname;
    }

    public byte[] getInhalt() {
        return inhalt;
    }
}
