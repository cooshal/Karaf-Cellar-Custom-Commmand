/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mbean.services;

import java.util.Map;

/**
 *
 * @author Kushal
 */
public interface NodeInfoMBean {
    
    public Map<String, Object> getNodeConfigProperties(String nodeId, String configFileName);
    public void updateNodeAlias(String nodeId, String configFile, String configField);
    
}
