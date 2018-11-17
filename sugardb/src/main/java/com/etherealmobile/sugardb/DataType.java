package com.etherealmobile.sugardb;

public class DataType {
    private String type = "";

    // Data Types
    private String TEXT = " TEXT ";
    private String INTEGER = " INTEGER ";

    // constrains
    private String asUnique = " UNIQUE ";
    private String notNull = " NOT NULL ";

    // Data Type setters
    public DataType _text_() {
        type += TEXT;
        return this;
    }

    public DataType _int_() {
        type += INTEGER;
        return this;
    }


    //
    public DataType unique() {
        type += asUnique;
        return this;
    }

    public DataType notNull() {
        type += notNull;
        return this;
    }

    //
    public String done() {
        return type;
    }
}