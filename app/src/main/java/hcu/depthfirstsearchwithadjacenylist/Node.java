package hcu.depthfirstsearchwithadjacenylist;

/**
 * Created by stava on 25.03.2017.
 */

public class Node {
    private String NodeColor=null,NodeName=null;
    private Adjacent adjacent=null;
    private int dtime=0,ftime=0;

    public Node() {
        NodeColor = "White";
    }

    public int getFtime() {
        return ftime;
    }

    public void setFtime(int ftime) {
        this.ftime = ftime;
    }

    public int getDTime() {
        return dtime;
    }

    public void setDTime(int time) {
        this.dtime = time;
    }

    public Adjacent getAdjacent() {
        return adjacent;
    }

    public void setAdjacent(Adjacent adjacent) {
        this.adjacent = adjacent;
    }

    public void setNodeName(String name) {
        this.NodeName = name;
    }

    public String getNodeName() {
        return NodeName;
    }

    public void setNodeColor(String nodeColor) {
        NodeColor = nodeColor;
    }

    public String getNodeColor() {
        return NodeColor;
    }
}
