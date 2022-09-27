package org.tmaxcloud.superscm.rule;

import org.tmaxcloud.superscm.rule.codesmell.IfElseIfStatementEndsWithElseCheck;
import org.tmaxcloud.superscm.rule.codesmell.SimpleClassNameCheck;
import org.tmaxcloud.superscm.rule.security.HardCodedSecretCheck;

import java.util.ArrayList;
import java.util.List;

public class RuleSet {

    private List<RuleVisitor> ruleSetList = new ArrayList<>();

    public RuleSet() {
        ruleSetList.add(new HardCodedSecretCheck());
        ruleSetList.add(new SimpleClassNameCheck());
        ruleSetList.add(new IfElseIfStatementEndsWithElseCheck());
    }

    public List<RuleVisitor> getRuleSetList() {
        return ruleSetList;
    }
}
