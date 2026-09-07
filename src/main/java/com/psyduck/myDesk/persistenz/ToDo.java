package com.psyduck.myDesk.persistenz;

public class ToDo {

    private String text;
    private boolean erledigt;

    public ToDo(String text) {
        this.text = text;
        this.erledigt = false;
    }

    public String getText() {
        return text;
    }

    public boolean isErledigt() {
        return erledigt;
    }

    public void setErledigt(boolean erledigt) {
        this.erledigt = erledigt;
    }
}
