/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infrastructure;

import domain.NodeInfoCommand;
import domain.NodeInfoCommandType;
import domain.NodeInfoResult;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.apache.karaf.cellar.core.command.CommandHandler;
import org.apache.karaf.cellar.core.control.BasicSwitch;
import org.apache.karaf.cellar.core.control.Switch;
import org.osgi.service.cm.Configuration;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kushal
 */
public class NodeInfoCommandHandler extends CommandHandler<NodeInfoCommand, NodeInfoResult> {

    public static final String SWITCH_ID = "org.apache.karaf.cellar.command.nodeinfo.switch";

    private final Switch commandSwitch = new BasicSwitch(SWITCH_ID);
    
    protected org.slf4j.Logger log = LoggerFactory.getLogger(NodeInfoCommandHandler.class);

    /**
     * to get the config properties, set the type of command to GET_CONFIG
     * 
     * In order to set the node alias, set the value of command type to 
     * UPDATE_NODE_ALIAS and also set the value of config filename as well as 
     * config field name.
     * 
     * This implementation has to be done here as alias can be set only from 
     * within the node itself.
     * @param nodeInfoCommand
     * @return 
     */
    @Override
    public NodeInfoResult execute(NodeInfoCommand nodeInfoCommand) {

        NodeInfoCommandType commandType = nodeInfoCommand.getCommandType();
        
        String configFileName = nodeInfoCommand.getConfigFileName();
        
        try {
            Map<String, Object> result = this.parseConfigFile(configFileName);
            
            if(commandType.equals(NodeInfoCommandType.UPDATE_NODE_ALIAS)) {
               String aliasConfigField = nodeInfoCommand.getAliasConfigField();
               
               if(aliasConfigField != null && !aliasConfigField.isEmpty() && result != null) {
                   String nodeAliasName = (String) result.get(aliasConfigField);
                   
                   if(nodeAliasName != null) {
                       this.clusterManager.setNodeAlias(nodeAliasName);
                   }
               }
            }
            
            NodeInfoResult nodeInfoResult = new NodeInfoResult(nodeInfoCommand.getId());
            nodeInfoResult.setResult(result);
            
            return nodeInfoResult;
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public Class<NodeInfoCommand> getType() {
        return NodeInfoCommand.class;
    }

    @Override
    public Switch getSwitch() {
        return commandSwitch;
    }

    private Map<String, Object> parseConfigFile(String configFileName) throws IOException {

        Map<String, Object> result = null;
        Configuration config;
        config = configurationAdmin.getConfiguration(configFileName);

        if (config != null && config.getProperties() != null) {

            result = new HashMap<>();
            
            Enumeration propertyNames = config.getProperties().keys();
            while (propertyNames.hasMoreElements()) {
                String key = (String) propertyNames.nextElement();
                String value = (String) config.getProperties().get(key);
                result.put(key, value);
            }
        }
        return result;
    }

}
