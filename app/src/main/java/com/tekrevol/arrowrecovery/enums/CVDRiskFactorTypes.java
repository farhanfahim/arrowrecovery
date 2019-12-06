package com.tekrevol.arrowrecovery.enums;

public enum CVDRiskFactorTypes {
    AGE,
    HDL,
    TOTALCHOL,
    SBPTREATED,
    SBPNOTTREATED,
    SMOKER,
    DIABETIC;

    public String canonicalForm() {
        return this.name();
    }

    public static CVDRiskFactorTypes fromCanonicalForm(String canonical) {
        return valueOf(CVDRiskFactorTypes.class, canonical);
    }
}