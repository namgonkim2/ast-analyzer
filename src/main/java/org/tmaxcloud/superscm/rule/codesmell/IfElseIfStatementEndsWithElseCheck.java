package org.tmaxcloud.superscm.rule.codesmell;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.tmaxcloud.superscm.kind.Kind;
import org.tmaxcloud.superscm.rule.RuleVisitor;

import java.util.Collections;
import java.util.List;

public class IfElseIfStatementEndsWithElseCheck extends RuleVisitor {
    @Override
    public List<Integer> nodesToVisit() {
        return Collections.singletonList(Kind.IF_STATEMENT);
    }

    @Override
    public void visitNode(ASTNode astNode) {
        IfStatement treeIfStmt = (IfStatement) astNode;
        Statement elseStmt = treeIfStmt.getElseStatement();

        if (elseStmt != null && elseStmt.getNodeType() == Kind.IF_STATEMENT) {
            IfStatement finderIfStmt = (IfStatement) elseStmt;
            if(finderIfStmt.getElseStatement() == null) {
                System.out.println("if(" + treeIfStmt.getExpression() + ")");
                System.out.println("else if(" + finderIfStmt.getExpression() + ")");
                System.out.println("\"if ... else if\" constructs should end with \"else\" clauses.");
            }
        }
    }
}
