package fr.allienna.google.hashcode.model;

/**
 * Created by aurelienallienne on 12/02/2016.
 */
public enum Commands {

    LOAD("L"),
    UNLOAD("U"),
    DELIVER("D"),
    WAIT("W");

    private String key;
    private Commands(String key) {
        this.key = key;
    }
}
