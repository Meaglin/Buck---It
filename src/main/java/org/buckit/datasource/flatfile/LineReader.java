package org.buckit.datasource.flatfile;

public class LineReader {
    private final String    line;
    private final String[]  entries;
    private int count = 0;
    
    
    public LineReader(String line) {
        this.line = line;
        this.entries = this.line.split(FileHandler.sep1);
    }
    
    public int nextInt() {
        count++;
        return getInt(count-1);
    }

    public String nextStr() {
        count++;
        return getStr(count-1);
    }
    
    public Boolean nextBool() {
        count++;
        return getBool(count-1);
    }
    
    public Double nextDouble() {
        count++;
        return getDouble(count-1);
    }
    
    public Float nextFloat() {
        count++;
        return getFloat(count-1);
    }
    
    
    public int getInt(int i) {
        int rt = 0;
        
        try { 
            rt = Integer.parseInt(entries[i]); 
        } catch (Exception e) {
            
        }
        
        return rt;
    }

    public String getStr(int i) {
        String rt = entries[i];
        
        return rt;
    }
    
    public Boolean getBool(int i) {
        boolean rt = false;
        
        try { 
            rt = Boolean.parseBoolean(entries[i]); 
        } catch (Exception e) { 
            
        }
        
        return rt;
    }
    
    public Double getDouble(int i) {
        Double rt = 0.0;
        
        try { 
            rt = Double.parseDouble(entries[i]); 
        } catch (Exception e) { 
             
        }
        
        return rt;
    }
    
    public Float getFloat(int i) {
        Float rt = (float) 0;
        
        try { 
            rt = Float.parseFloat(entries[i]); 
        } catch (Exception e) { 
             
        }
        
        return rt;
    }
    
    
    
    public void skip() {
        count++;
    }
}
