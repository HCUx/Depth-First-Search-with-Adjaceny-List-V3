package hcu.depthfirstsearchwithadjacenylist;

/**
 * Created by stava on 28.03.2017.
 */

public class Adjacent {
    private Node nodeReference=null;
    private Adjacent next=null;

    public Node getNodeReference() {
        return nodeReference;
    }

    public void setNodeReference(Node nodeReference) {
        this.nodeReference = nodeReference;
    }

    public Adjacent getNext() {
        return next;
    }

    public void setNext(Adjacent next) {
        this.next = next;
    }
}
