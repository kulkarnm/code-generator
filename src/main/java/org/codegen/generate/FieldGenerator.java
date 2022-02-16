package org.codegen.generate;

import com.helger.jcodemodel.*;
import org.codegen.metadata.FieldMetadata;
import org.codegen.metadata.LiteralMetadata;
import org.codegen.metadata.LogicStatementMetadata;
import org.codegen.metadata.constants.AccessModifier;
import org.codegen.metadata.constants.LiteralType;
import org.codegen.metadata.constants.OtherModifier;
import org.codegen.metadata.constants.STATEMENTTYPE;

import java.util.ArrayList;
import java.util.List;

public class FieldGenerator extends SegmentGenerator<FieldMetadata> {
    private IJClassContainer ijClassContainer;

    public FieldGenerator(IJClassContainer ijClassContainer, JCodeModel cm) {
        super(cm);
        this.ijClassContainer = ijClassContainer;
    }

    public void generateSegment(FieldMetadata fieldMetadata) throws JClassAlreadyExistsException {
        JDefinedClass jDefinedClass = (JDefinedClass) ijClassContainer;
        String name = fieldMetadata.getName();
        String type = fieldMetadata.getCompositeType();

        AccessModifier accessModifier = fieldMetadata.getAccessModifier();
        List<OtherModifier> otherModifiers = fieldMetadata.getOtherModifiers();
        int finalMod = resolveAccessModifier(0, accessModifier);
        if (null != otherModifiers) {
            for (OtherModifier modifier : otherModifiers) {
                finalMod = resolveOtherModifier(finalMod, modifier);
            }
        }
        LogicStatementMetadata assignedMethodCall = fieldMetadata.getAssignedExpression();
        String initializedValue = fieldMetadata.getInitializedValue();
        JFieldVar fieldForInject = null;
        AbstractJClass typeClass = getCm().ref(type);
        if (null == typeClass) {
            typeClass = getCm()._class(type);
        }
        if (null == assignedMethodCall && null == initializedValue) {
            fieldForInject = jDefinedClass.field(finalMod, typeClass, name);
        } else if (null != assignedMethodCall && null == initializedValue) {
            buildMethodCallStatement(assignedMethodCall, jDefinedClass, finalMod, typeClass, name);
        } else if (null == assignedMethodCall && null != initializedValue) {
            IJExpression initialization = JExpr.ref(initializedValue);
            fieldForInject = jDefinedClass.field(finalMod, typeClass, name, initialization);
        }
        buildAnnotations(fieldMetadata, fieldForInject);
    }

    private JFieldVar buildMethodCallStatement(LogicStatementMetadata logicStatementMetadata, JDefinedClass containerClass, int finalMod, AbstractJClass type, String name) throws JClassAlreadyExistsException {
        if (null != logicStatementMetadata) {
            List<LiteralMetadata> operandsAndOperators = logicStatementMetadata.getOrderedLiteralsUsed();
            if (logicStatementMetadata.getStatementType() == STATEMENTTYPE.METHOD_CALL) {
                String caller = null;
                String methodName = null;
                List<LiteralMetadata> arguments = new ArrayList<>();
                List<IJExpression> expressionsAsArguments = new ArrayList<>();
                String returnType = null;
                String returnVarName = null;
                for (LiteralMetadata literal : operandsAndOperators) {
                    if (literal.getLiteralType() == LiteralType.CALLER_NAME) {
                        caller = literal.getVarName();
                    } else if (literal.getLiteralType() == LiteralType.METHOD_NAME) {
                        methodName = literal.getVarName();
                    } else if (literal.getLiteralType() == LiteralType.ARGUMENT_NAME) {
                        arguments.add(literal);
                    }
                }
                AbstractJClass variableClass = getCm().ref(caller);
                if (null == variableClass) {
                    variableClass = getCm()._class(caller);
                }
                JInvocation invocation = variableClass.staticInvoke(methodName);
                for (int i = 0; i < arguments.size(); i++) {
                    AbstractJClass argumentClass = getCm().ref(arguments.get(i).getUserDefinedType());
                    if (null == argumentClass) {
                        argumentClass = getCm()._class(arguments.get(i).getUserDefinedType());
                    }
                    invocation.arg(argumentClass.dotclass());
                }
                JFieldVar fieldForInject = containerClass.field(finalMod, type, name, invocation);
                return fieldForInject;
            }
        }
        return null;
    }
}