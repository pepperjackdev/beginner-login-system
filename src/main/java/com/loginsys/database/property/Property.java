package com.loginsys.database.property;

public enum Property {

    ID(
            "id",
            true,
            false
    ),

    EMAIL(
            "email",
            true,
            true
    ),

    USERNAME(
            "username",
            true,
            true
    ),

    PASSWORD(
            "password",
            false,
            true
    );

    private Property(String value, boolean searchable, boolean editable) {
        this.value = value;
        this.searchable = searchable;
        this.editable = editable;
    }

    @Override
    public String toString() {
        return value;
    }

    public final String value;
    public final boolean searchable;
    public final boolean editable;
}
