package com.tekrevol.arrowrecovery.enums;

public enum BaseURLTypes {
    BASE_URL,
    XML_URL,
    PRICE_BASE_URL;



    public String canonicalForm() {
        return this.name().toLowerCase();
    }

    public static BaseURLTypes fromCanonicalForm(String canonical) {
        return (BaseURLTypes) valueOf(BaseURLTypes.class, canonical.toUpperCase());
    }
}