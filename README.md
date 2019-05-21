# Karaf-Cellar-Custom-Commmand

## Overview

Custom Command for Karaf cellar for node-to-node communication

## Pre-requisites

1. Apache Karaf (obviously)
2. Apache Karaf Cellar Installed
3. Jolokia or Hawtio installed

Note: this bundle should be installed on all the cellar nodes.

## Usage

Assuming there are two nodes, install this bundle (by copying in the deploy folder of Karaf installation) on both of the nodes. 

After the bundle is installed, it will expose an mbean "com.example.mbean:type=management,name=nodeInfo"

It can be invoked as:

```json
{
    "type": "exec",
    "mbean": "com.example.mbean:type=management,name=nodeInfo",
    "operation": "getNodeConfigProperties(java.lang.String,java.lang.String)",
    "arguments": ["192.168.99.1:5702", "org.ops4j.pax.web"]
}
```

Send an HTTP Post Request to the jolokia endpoint and the following result should be obtained:

```json
{
    "request": {
        "mbean": "com.example.mbean:name=nodeInfo,type=management",
        "arguments": [
            "192.168.99.1:5702",
            "org.ops4j.pax.web"
        ],
        "type": "exec",
        "operation": "getNodeConfigProperties(java.lang.String,java.lang.String)"
    },
    "value": {
        "javax.servlet.context.tempdir": "D:\\temp\\nodes\\worker-1\\data/pax-web-jsp",
        "felix.fileinstall.filename": "file:/D:/temp/nodes/worker-1/etc/org.ops4j.pax.web.cfg",
        "org.ops4j.pax.web.config.file": "D:\\temp\\nodes\\worker-1\\etc/jetty.xml",
        "org.osgi.service.http.port": "8182",
        "service.pid": "org.ops4j.pax.web"
    },
    "timestamp": 1558452525,
    "status": 200
}
```

This command can be invoked from java code as follows (see `EAINodeInfoMBean.java`):

```java
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
```

## Implementation

The implementation details can be found in the file `NodeInfoCommandHandler.java`.

## Issues

Updating the bundle does not reflect changes found in the newer bundle. A full restart of the node does the job though.