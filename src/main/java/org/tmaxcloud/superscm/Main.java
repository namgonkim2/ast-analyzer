package org.tmaxcloud.superscm;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.tmaxcloud.superscm.parse.ASTGen;
import org.tmaxcloud.superscm.rule.RuleSet;
import org.tmaxcloud.superscm.rule.RuleVisitor;
import org.tmaxcloud.superscm.visit.ASTVisitor;

public class Main {

    public final static ASTVisitor astVisitor = new ASTVisitor();

    public static void main(String[] args) throws Exception {
        ASTGen astGen = new ASTGen();

        String filePath ="src/main/resources/demo.java";
        String source = astGen.getSourceCode(filePath);
        CompilationUnit compilationUnit = astGen.getCompilationUnit(source);

        ASTNode root = compilationUnit.getRoot();
        astVisitor.visitTree(root);

        scan(root);
    }

    public static void scan(ASTNode astnode) {
        RuleSet ruleSet = new RuleSet();
        for(RuleVisitor rule : ruleSet.getRuleSetList()) {
            System.out.println(rule.getClass().getSimpleName());
            rule.scanFile(astnode);
        }
    }
}
