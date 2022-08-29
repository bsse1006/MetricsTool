package othersPackage;

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.HashSet;
import java.util.Set;

public class GraphNode {

    public ASTNode node;
    public Set<GraphNode> parents;
    public Set<GraphNode> children;

    public GraphNode() {
        parents = new HashSet<GraphNode>();
        children = new HashSet<GraphNode>();
    }

    public GraphNode(ASTNode node) {
        this.node = node;
        parents = new HashSet<GraphNode>();
        children = new HashSet<GraphNode>();
    }

    public ASTNode getNode() {
        return node;
    }

    public void setNode(ASTNode node) {
        this.node = node;
    }

    public Set<GraphNode> getParents() {
        return parents;
    }

    public void setParents(Set<GraphNode> parents) {
        this.parents = parents;
    }

    public Set<GraphNode> getChildren() {
        return children;
    }

    public void setChildren(Set<GraphNode> children) {
        this.children = children;
    }
}
