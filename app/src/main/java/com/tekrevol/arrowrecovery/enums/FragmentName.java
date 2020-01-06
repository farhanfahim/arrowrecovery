package com.tekrevol.arrowrecovery.enums;

public enum FragmentName {
    RegistrationRequired,
    SimpleLogin;

    public String canonicalForm() {
        return this.name().toLowerCase();
    }

    public static FragmentName fromCanonicalForm(String canonical) {
        return valueOf(FragmentName.class, canonical.toUpperCase());
    }
}