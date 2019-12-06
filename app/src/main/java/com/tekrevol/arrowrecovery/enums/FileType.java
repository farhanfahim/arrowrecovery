package com.tekrevol.arrowrecovery.enums;

public enum FileType {
    IMAGE,
    VIDEO,
    DOCUMENT;


    public String canonicalForm() {
        return this.name().toLowerCase();
    }

    public static FileType fromCanonicalForm(String canonical) {
        return valueOf(FileType.class, canonical.toUpperCase());
    }
}