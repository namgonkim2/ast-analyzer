package org.tmaxcloud.superscm.visit;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ASTVisitor {
    public void visitTree(ASTNode root) {
        visitNode(root);
    }

    public void visitNode(ASTNode node) {
        ArrayList<ASTNode> children = getChildren(node);
        System.out.println(node.getClass().toString());
        String nodeType = ASTNode.nodeClassForType(node.getNodeType()).getSimpleName();

        System.out.println("##########");
        System.out.println(nodeType);
        if (children.size() > 0) {
            for (ASTNode child : children) {
                visitNode(child);
            }
            System.out.println();
        } else {
            System.out.println(node.toString());
        }
    }

    private ArrayList<ASTNode> getChildren(ASTNode node) {
        ArrayList<ASTNode> children = new ArrayList<>();
        List<Object> list = node.structuralPropertiesForType();

        for (int i = 0; i < list.size(); i++) {
            StructuralPropertyDescriptor curr = (StructuralPropertyDescriptor) list.get(i);
            Object child = node.getStructuralProperty(curr);
            if (child instanceof List) {
                children.addAll((Collection<? extends ASTNode>) child);
            } else if (child instanceof ASTNode) {
                children.add((ASTNode) child);
            } else{
            }
        }
        return children;
    }
}
