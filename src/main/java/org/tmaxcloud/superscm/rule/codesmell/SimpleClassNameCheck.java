package org.tmaxcloud.superscm.rule.codesmell;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.tmaxcloud.superscm.kind.Kind;
import org.tmaxcloud.superscm.rule.RuleVisitor;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SimpleClassNameCheck extends RuleVisitor {

    private static final String MESSAGE = "Replace this fully qualified name with \"%s\"";

    private static final Predicate<ASTNode> NOT_EMPTY_STATEMENT = astNode -> !(astNode.getNodeType() == Kind.EMPTY_STATEMENT);
    @Override
    public List<Integer> nodesToVisit() {
        return Collections.singletonList(Kind.COMPILATION_UNIT);
    }

    @Override
    public void visitNode(ASTNode astNode) {
        CompilationUnit cut = (CompilationUnit) astNode;
        // Object importFilter = cut.imports().stream().filter(NOT_EMPTY_STATEMENT).map(ImportDeclaration.class::cast).collect(Collectors.toList());
        List<ImportDeclaration> imports = (List<ImportDeclaration>) cut.imports().stream().filter(NOT_EMPTY_STATEMENT).map(ImportDeclaration.class::cast).collect(Collectors.toList());
        // Symbol이라는 별도의 소나큐브 전용 객체가 있는데 이놈을 만들어야함.... 어렵다...
    }

    private void checkImports(List<ImportDeclaration> imports) {

    }

}
