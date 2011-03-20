package org.buckit.datasource;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class RequestDataSource implements DataSource {

    private DataSourceManager manager;
    private HashMap<Integer, Request> RequestById;
    private HashMap<String, Request> RequestByName;
    
    public RequestDataSource(DataSourceManager manager) {
        this.manager = manager;
    }
    
    public DataSourceManager getDataSourceManager(){
        return manager;
    }
    
    public void registerRequest(Player player,Request request) {
        registerRequest(player.getPlayerId(),player.getName(),request);
    }
    
    public void registerRequest(int id, String name, Request request) {
        RequestById.put(id, request);
        RequestByName.put(name, request);
    }
    
    public void removeRequest(Player player) {
        removeRequest(player.getPlayerId(),player.getName());
    }
    
    public void removeRequest(int id, String name) {
        RequestById.remove(id);
        RequestByName.remove(name);
    }
    
    public Request getRequest(Player p) {
        return getRequest(p.getPlayerId(), p.getName());
    }
    
    public Request getRequest(int id) { return getRequest(id,null); }
    public Request getRequest(String name) { return getRequest(-1,null); }
    
    public Request getRequest(int id,String name) {
        if(RequestById.containsKey(id)) {
            return RequestById.get(id);
        } else if(RequestByName.containsKey(name)) {
            return RequestByName.get(name);
        } else
            return null;
    }
    
    @Override
    public boolean load() {
        RequestById = new HashMap<Integer, Request>();
        RequestByName = new HashMap<String, Request>();
        return true;
    }

}
