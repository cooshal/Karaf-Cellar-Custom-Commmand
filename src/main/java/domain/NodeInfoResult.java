/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.HashMap;
import java.util.Map;
import org.apache.karaf.cellar.core.command.Result;

/**
 *
 * @author Kushal
 */
public class NodeInfoResult extends Result {

    private Map<String, Object> result;
    
    public NodeInfoResult(String id) {
        super(id);
        
        this.result = new HashMap<>();
    }
    
    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
    
    public Map<String, Object> getResult() {
        return this.result;
    }
    
    @Override
    public String toString() {
        return result.toString();
    }
}
