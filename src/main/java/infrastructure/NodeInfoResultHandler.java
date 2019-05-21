package infrastructure;


import domain.NodeInfoResult;
import org.apache.karaf.cellar.core.command.ResultHandler;

public class NodeInfoResultHandler extends ResultHandler<NodeInfoResult> {
    
    @Override
    public Class<NodeInfoResult> getType() {
        return NodeInfoResult.class;
    }
    
}
