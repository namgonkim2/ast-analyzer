package org.tmaxcloud.superscm.rule.security;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.tmaxcloud.superscm.kind.Kind;
import org.tmaxcloud.superscm.rule.RuleVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class HardCodedSecretCheck extends RuleVisitor {

    private static final String CREDENTIAL_WORDS = "password,passwd,pwd,passphrase,java.naming.security.credentials,api[_.-]?key,auth,credential,secret,token";
    @Override
    public List<Integer> nodesToVisit() {
        return Arrays.asList(Kind.STRING_LITERAL, Kind.VARIABLE_DECLARATION_FRAGMENT);
    }

    @Override
    public void visitNode(ASTNode astNode) {
        if(astNode.getNodeType() == Kind.VARIABLE_DECLARATION_FRAGMENT) {
            VariableDeclarationFragment node = (VariableDeclarationFragment) astNode;
            SimpleName variableName = node.getName();
            System.out.println(variableName);
            if(isCredentialVariableName(variableName)) {
                System.out.println("'" + variableName.toString() + "' detected in this expression, review this potentially hard-coded secret.");
            }
        }
        if(astNode.getNodeType() == Kind.STRING_LITERAL) {
            StringLiteral node = (StringLiteral) astNode;
            System.out.println(node.getLiteralValue());
        }
    }

    boolean isCredentialVariableName(SimpleName variableName) {
        List<String> credential_words = Arrays.asList(CREDENTIAL_WORDS.split(","));
        for(String finder : credential_words) {
            if(variableName.toString().matches(finder)) {
                return true;
            }
        }
        return false;
    }
}
