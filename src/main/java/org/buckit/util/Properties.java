package org.buckit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public final class Properties extends java.util.Properties {
    private static final long serialVersionUID = 1L;

    private static Logger     log              = Logger.getLogger(Properties.class.getName());

    public Properties() {
    }

    public Properties(String name) throws IOException {
        load(new FileInputStream(name));
    }

    public Properties(File file) throws IOException {
        load(new FileInputStream(file));
    }

    public Properties(InputStream inStream) throws IOException {
        load(inStream);
    }

    public Properties(Reader reader) throws IOException {
        load(reader);
    }

    public void load(String name) throws IOException {
        load(new FileInputStream(name));
    }

    public void load(File file) throws IOException {
        load(new FileInputStream(file));
    }

    @Override
    public void load(InputStream inStream) throws IOException {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(inStream, Charset.defaultCharset());
            super.load(reader);
        } finally {
            inStream.close();
            if (reader != null)
                reader.close();
        }
    }

    @Override
    public void load(Reader reader) throws IOException {
        try {
            super.load(reader);
        } finally {
            reader.close();
        }
    }

    @Override
    public String getProperty(String key) {
        String property = super.getProperty(key);

        if (property == null) {
            log.info("Properties: Missing property for key - " + key);

            return null;
        }

        return property.trim();
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        String property = super.getProperty(key, defaultValue);

        if (property == null) {
            log.warning("Properties: Missing defaultValue for key - " + key);

            return null;
        }

        return property.trim();
    }
    
    public boolean getBool(String key,String defaultvalue){
        return Boolean.parseBoolean(getProperty(key,defaultvalue));
    }
    
    public int getInt(String key,String defaultvalue){
        return Integer.parseInt(getProperty(key,defaultvalue));
    }
    
    public int getInt(String key,String defaultvalue,int min,int max){
        int rt = getInt(key,defaultvalue);
        
        if(rt > max)rt = max;
        else if(rt < min)rt = min;
        
        return rt;
    }
}
