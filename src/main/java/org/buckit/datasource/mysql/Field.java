package org.buckit.datasource.mysql;

public class Field {
    enum Type {
        STRING,
        BOOLEAN,
        BYTE,
        INTEGER,
        SHORT,
        LONG
    }
    public Type type;
    public String name;
    public Object value;
    public Field(Type type,String name,Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }
    
    public Field(String name,Type type) {
        this.type = type;
        this.name = name;
    }
    
    public Field(String name,boolean value) {
        this.type = Type.BOOLEAN;
        this.name = name;
        this.value = value;
    }
    
    public Field(String name,byte value) {
        this.type = Type.BYTE;
        this.name = name;
        this.value = value;
    }
    
    public Field(String name,short value) {
        this.type = Type.SHORT;
        this.name = name;
        this.value = value;
    }

    public Field(String name,int value) {
        this.type = Type.INTEGER;
        this.name = name;
        this.value = value;
    } 
    
    public Field(String name,long value) {
        this.type = Type.LONG;
        this.name = name;
        this.value = value;
    }
    
    public Field(String name,String value) {
        this.type = Type.STRING;
        this.name = name;
        this.value = value;
    }
    
    public Type getType() {
        return type;
    }
    
    public String getName() {
        return name;
    }
    
    public Object getValue() {
        return value;
    }
    
    public int getInt(){
        if(type == Type.INTEGER) {
            return ((Integer)value).intValue();
        } else {
            return -1;
        }
    }
    
    public long getLong(){
        if(type == Type.LONG) {
            return ((Long)value).longValue();
        } else {
            return -1;
        }
    }
    
    public byte getByte(){
        if(type == Type.BYTE) {
            return ((Byte)value).byteValue();
        } else {
            return -1;
        }
    }
    
    public short getShort(){
        if(type == Type.SHORT) {
            return ((Short)value).shortValue();
        } else {
            return -1;
        }
    }
    
    public String getString(){
        if(type == Type.STRING) {
            return ((String)value);
        } else {
            return "";
        }
    }
    
    public boolean getBool(){
        if(type == Type.BOOLEAN) {
            return ((Boolean)value).booleanValue();
        } else {
            return false;
        }
    }
}
