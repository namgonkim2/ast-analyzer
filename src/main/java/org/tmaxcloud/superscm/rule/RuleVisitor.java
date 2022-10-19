package org.tmaxcloud.superscm.rule;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.tmaxcloud.superscm.kind.Kind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public abstract class RuleVisitor {

    private Set<Integer> nodesToVisit; // Rule 체크를 위해 방문할 노드의 type 을 저장하는 변수

    private List<ASTNode> nodesToCheck; // Rule 체크로 방문할 노드 묶음

    public abstract List<Integer> nodesToVisit();

    public void visitNode(ASTNode astNode) {

    }

    public void scanFile(ASTNode astNode) {
        scanTree(astNode);
    }

    // 전체 ASTNode 를 인자로 갖고 있음
    // nodesToVisit()에서 등록하려는 node의 type을 등록
    // nodesToVisit()에 해당 type을 가지는 node를 등록
    protected void scanTree(ASTNode astNode) {
        if(nodesToVisit == null) {
            List<Integer> kinds = nodesToVisit();
            if(kinds.isEmpty()) {
                nodesToVisit = Set.of(Kind.COMPILATION_UNIT);
            } else {
                nodesToVisit = Set.copyOf(kinds);
            }
        }

        nodesToCheck = new ArrayList<>();
        visit(astNode);
        for(ASTNode node : nodesToCheck) {
            visitNode(node);
        }
        nodesToCheck.clear();
    }

    // 전체 astNode 를 방문하며 nodesToVisit()에 작성된 node의 type일 때,
    private void visit(ASTNode astNode) {
        List<ASTNode> children = getChildren(astNode);
        int nodeType = astNode.getNodeType();
        boolean isSubscribed = isSubscribed(nodeType);

        if(isSubscribed) {
            nodesToCheck.add(astNode);
        }

        if(children.size() > 0) {
            for (ASTNode child : children) {
                visit(child);
            }
        }
    }

    private boolean isSubscribed(int nodeType) {
        return nodesToVisit.contains(nodeType);
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
