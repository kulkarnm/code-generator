package org.codegen.metadata;

import org.codegen.metadata.constants.LOOPTYPE;
import org.codegen.metadata.constants.LiteralType;
import org.codegen.metadata.constants.STATEMENTTYPE;

import java.util.ArrayList;
import java.util.List;

public class LogicStatementMetadata {
    private int sequence;
    private STATEMENTTYPE statementType ;
    private LOOPTYPE loopType;
    private List<LiteralMetadata> orderedLiteralsUsed ;
    private List<LogicStatementMetadata> innerBlockStatements ;
    private List<LogicStatementMetadata> chainedCallStatements ;

    public LogicStatementMetadata(int sequence) {
        this.sequence = sequence;
        this.orderedLiteralsUsed = new ArrayList<>();
        this.innerBlockStatements = new ArrayList<>();
        this.chainedCallStatements = new ArrayList<>();
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public STATEMENTTYPE getStatementType() {
        return statementType;
    }

    public void setStatementType(STATEMENTTYPE statementType) {
        this.statementType = statementType;
    }

    public LOOPTYPE getLoopType() {
        return loopType;
    }

    public void setLoopType(LOOPTYPE loopType) {
        this.loopType = loopType;
    }

    public List<LiteralMetadata> getOrderedLiteralsUsed() {
        return orderedLiteralsUsed;
    }

    public void setOrderedLiteralsUsed(List<LiteralMetadata> orderedLiteralsUsed) {
        this.orderedLiteralsUsed = orderedLiteralsUsed;
    }

    public List<LogicStatementMetadata> getInnerBlockStatements() {
        return innerBlockStatements;
    }

    public void setInnerBlockStatements(List<LogicStatementMetadata> innerBlockStatements) {
        this.innerBlockStatements = innerBlockStatements;
    }

    public List<LogicStatementMetadata> getChainedCallStatements() {
        return chainedCallStatements;
    }

    public void setChainedCallStatements(List<LogicStatementMetadata> chainedCallStatements) {
        this.chainedCallStatements = chainedCallStatements;
    }
     public void addToLiteralMetadata ( LiteralMetadata literalMetadata) {
        this.orderedLiteralsUsed.add(literalMetadata);
     }

     public void reuseLiteralMetadata(LiteralMetadata literalMetadata, LiteralType literalType){
        LiteralMetadata newLiteral = new LiteralMetadata(literalMetadata);
        newLiteral.setLiteralType(literalType);
        this.orderedLiteralsUsed.add(newLiteral);
     }
}
