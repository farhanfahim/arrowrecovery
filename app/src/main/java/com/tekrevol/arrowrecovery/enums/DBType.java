package com.tekrevol.arrowrecovery.enums;

public enum DBType {
    PLATINUM,
    RODIUM,
    PALLADIUM;

    public String canonicalForm() {
        return this.name();
    }

    public static DBType fromCanonicalForm(String canonical) {
        return valueOf(DBType.class, canonical);
    }
}