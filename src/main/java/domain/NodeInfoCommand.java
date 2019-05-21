package domain;

import org.apache.karaf.cellar.core.command.Command;

public class NodeInfoCommand extends Command<NodeInfoResult>  {
    
    private String configFileName;
    private NodeInfoCommandType commandType;
    private String aliasConfigField;
    
    public NodeInfoCommand(String id) {
        super(id);
    }

    public String getConfigFileName() {
        return configFileName;
    }

    public void setConfigFileName(String configFileName) {
        this.configFileName = configFileName;
    }

    public NodeInfoCommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(NodeInfoCommandType commandType) {
        this.commandType = commandType;
    }

    public String getAliasConfigField() {
        return aliasConfigField;
    }

    public void setAliasConfigField(String aliasConfigField) {
        this.aliasConfigField = aliasConfigField;
    }
}
