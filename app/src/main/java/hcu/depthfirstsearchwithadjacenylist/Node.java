package hcu.depthfirstsearchwithadjacenylist;

/**
 * Created by stava on 25.03.2017.
 */

public class Node {
    private String NodeColor=null;
    private int Node_DTime=0,Node_FTime=0,AdjacentCount=0;
    private Node[] adjacent=null;
    private int adjacentCount=0;
    private int NodeDegree=0;
    private String nodeName=null;

    public Node(int adjacentCount) {
        NodeColor = "White";
        Node_DTime = 0;
        Node_FTime = 0;
        this.adjacentCount = adjacentCount;
        adjacent = new Node[adjacentCount];
    }

    public int getNode_DTime() {
        return Node_DTime;
    }

    public void setNode_DTime(int node_DTime) {
        Node_DTime = node_DTime;
    }

    public int getNode_FTime() {
        return Node_FTime;
    }

    public void setNode_FTime(int node_FTime) {
        Node_FTime = node_FTime;
    }

    public String getNodeName() {
        return nodeName;
    }

    public int getAdjacentCount() {
        return adjacentCount;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setAdjacent(Node[] adjacent, String[] NodeParts) {
        int index=0;
        for(String part : NodeParts){
            if(part.equals("1")){
                this.adjacent[index] = adjacent[index];
                this.NodeDegree++;
            }
            else{
                this.adjacent[index] = null;
            }
            index++;
        }
    }

    public Node[] getAdjacent() {
        return adjacent;
    }

    public int getNodeDegree(){
        return this.NodeDegree;
    }

    public void setNodeColor(String nodeColor) {
        NodeColor = nodeColor;
    }

    public String getNodeColor() {
        return NodeColor;
    }
}
