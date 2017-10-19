package de.holisticon.annotationprocessortoolkit.templating.expressions.operands;

import de.holisticon.annotationprocessortoolkit.templating.expressions.Expression;
import de.holisticon.annotationprocessortoolkit.templating.expressions.Operand;
import de.holisticon.annotationprocessortoolkit.templating.expressions.OperandType;

/**
 * Expression Based Operand.
 */
public class ExpressionOperand extends Operand <Object>{

    private final Expression expression;

    public ExpressionOperand(OperandType operandType, String expressionString, boolean negate,  Expression expression) {
        super(operandType, expressionString, negate);

        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Class<Object> getOperandsJavaType() {
        return null;
    }

    @Override
    public Object value() {
        return null;
    }


}