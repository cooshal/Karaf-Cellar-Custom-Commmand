package mbean.infrastructure;

import domain.NodeInfoCommand;
import domain.NodeInfoCommandType;
import domain.NodeInfoResult;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import mbean.services.NodeInfoMBean;
import org.apache.karaf.cellar.core.ClusterManager;
import org.apache.karaf.cellar.core.Node;
import org.apache.karaf.cellar.core.command.ExecutionContext;
import org.slf4j.LoggerFactory;

public class EAINodeInfoMBean implements NodeInfoMBean {

    private final ClusterManager clusterManager;
    private final ExecutionContext executionContext;
    
    protected org.slf4j.Logger log = LoggerFactory.getLogger(EAINodeInfoMBean.class);

    public EAINodeInfoMBean(ClusterManager clusterManager, ExecutionContext executionContext) {
        this.clusterManager = clusterManager;
        this.executionContext = executionContext;
    }
    
    @Override
    public Map<String, Object> getNodeConfigProperties(String nodeId, String configFileName) {
        Node node = clusterManager.findNodeByIdOrAlias(nodeId);
        if (node == null) {
            throw new IllegalArgumentException("Cluster group " + nodeId + " doesn't exist");
        }

        NodeInfoCommand nodeInfoCommand = new NodeInfoCommand(clusterManager.generateId());
        nodeInfoCommand.setConfigFileName(configFileName);
        nodeInfoCommand.setCommandType(NodeInfoCommandType.GET_CONFIG);
        
        nodeInfoCommand.setDestination(new HashSet<>(Arrays.asList(node)));
        Map<Node, NodeInfoResult> result;
        
        try {
            result = executionContext.execute(nodeInfoCommand);
            
            if(result != null) {
                NodeInfoResult r = result.get(node);
                return r.getResult();
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        
        return null;
    }

    @Override
    public void updateNodeAlias(String nodeId, String configFile, String configField) {
        
        Node node = clusterManager.findNodeByIdOrAlias(nodeId);
        
        if (node == null) {
            return;
        }

        NodeInfoCommand nodeInfoCommand = new NodeInfoCommand(clusterManager.generateId());
        nodeInfoCommand.setConfigFileName(configField);
        nodeInfoCommand.setCommandType(NodeInfoCommandType.UPDATE_NODE_ALIAS);
        
        // name of the field to extract the value of Node Alias
        nodeInfoCommand.setAliasConfigField(configField);

        nodeInfoCommand.setDestination(new HashSet<>(Arrays.asList(node)));

        try {
            executionContext.execute(nodeInfoCommand);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
    
}
