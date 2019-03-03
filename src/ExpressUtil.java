import java.math.BigDecimal;
import java.util.Stack;

/**
 * 表达式工具类，提供计算表达式的方法
 */
public class ExpressUtil {

    //计算表达式的值
    public static Response calculate(String express) {
        Stack<Element> stack = ExpressList.getSuffix(express);
        if(stack == null) {
            return new Response(false, ExpressList.own.getErrorMessage());
        }
        String result = null;
        ResultType resultType = ResultType.Undefined;
        Stack<Element> temp = new Stack<Element>();
        while (!stack.empty()) {
            temp.push(stack.pop());
        }
        while (!temp.empty()) {
            if(Utils.isNum(temp.peek().getValue())) {
                resultType = ResultType.Double;
                stack.push(temp.pop());
            } else if(Utils.isBoolean(temp.peek().getValue())) {
                resultType = ResultType.Boolean;
                stack.push(temp.pop());
            } else if(Utils.hasQuotation(temp.peek().getValue())) {
                resultType = ResultType.String;
                stack.push(temp.pop());
            } else if(temp.peek().getElementType() == ElementType.Not) {
                Element num = stack.pop();
                Element notOperate = temp.pop();
                if(Utils.isNum(num.getValue())) {
                    return new Response(false, "数值型不能进行取反操作");
                } else if(Utils.hasQuotation(num.getValue())) {
                    return new Response(false, "字符串不能进行取反操作");
                } else {
                    stack.push(new Element(!Boolean.parseBoolean(num.getValue()) + ""));
                }
            } else {
                Element operate = temp.pop();
                String operateName = operate.getValue();
                Element num2 = null;
                Element num1 = null;
                if(!stack.empty()) {
                    if(!stack.empty()) {
                        num2 = stack.pop();
                    }
                    if(!stack.empty()) {
                        num1 = stack.pop();
                    }
                    if(num2 != null) {
                        if(Utils.isZero(num2.getValue())) {
                            if(operate.getElementType() == ElementType.Divide) {
                                return new Response(false, "被零除");
                            }
                            if(operate.getElementType() == ElementType.Mod) {
                                return new Response(false, "被零模");
                            }
                        }
                    } else {
                        if(Utils.isLogicOperate(operate.getValue())) {
                            operateName += operate.getValue();
                        }
                        return new Response(false, "运算符\"" + operateName + "\"左右两边均不能为空");
                    }
                    if(num1 != null) {
                        if(Utils.isLogicOperate(operate.getValue())) {
                            operateName += operate.getValue();
                            if(Utils.isNum(num1.getValue())) {
                                return new Response(false, "运算符\"" + operateName + "\"左边不能为数值型");
                            }
                            if(Utils.isNum(num2.getValue())) {
                                return new Response(false, "运算符\"" + operateName + "\"右边不能为数值型");
                            }
                            if(Utils.hasQuotation(num1.getValue())) {
                                return new Response(false, "运算符\"" + operateName + "\"左边不能为字符串");
                            }
                            if(Utils.hasQuotation(num2.getValue())) {
                                return new Response(false, "运算符\"" + operateName + "\"右边不能为字符串");
                            }
                            resultType = ResultType.Boolean;
                        } else if(operate.getElementType() == ElementType.Add || operate.getElementType() == ElementType.EQ || operate.getElementType() == ElementType.NE){
                            if(Utils.isBoolean(num1.getValue())) {
                                return new Response(false, "运算符\"" + operateName + "\"左边不能为布尔型");
                            }
                            if(Utils.isBoolean(num2.getValue())) {
                                return new Response(false, "运算符\"" + operateName + "\"右边不能为布尔型");
                            }
                            if(num1.getElementType() != num2.getElementType()) {
                                return new Response(false, "运算符\"" + operateName + "\"左右两边类型必须一致");
                            }
                            if(Utils.isRelationOperate(operate.getElementType())) {
                                resultType = ResultType.Boolean;
                            } else if(Utils.hasQuotation(num1.getValue())){
                                resultType = ResultType.String;
                            } else {
                                resultType = ResultType.Double;
                            }
                        } else {
                            if(!Utils.isNum(num1.getValue())) {
                                return new Response(false, "运算符\"" + operateName + "\"左边不能为非数值型");
                            }
                            if(!Utils.isNum(num2.getValue())) {
                                return new Response(false, "运算符\"" + operateName + "\"右边不能为非数值型");
                            }
                            if(Utils.isRelationOperate(operate.getElementType())) {
                                resultType = ResultType.Boolean;
                            } else {
                                resultType = ResultType.Double;
                            }
                        }
                    } else {
                        if(Utils.isLogicOperate(operate.getValue())) {
                            operateName += operate.getValue();
                        }
                        return new Response(false, "运算符\"" + operateName + "\"左右两边均不能为空");
                    }
                    result = calcValue(num1, num2, operate);
                    stack.push(new Element(result));
                } else {
                    if(Utils.isLogicOperate(operate.getValue())) {
                        operateName += operate.getValue();
                    }
                    return new Response(false, "运算符\"" + operateName + "\"左右两边均不能为空");
                }
            }
        }
        return new Response(true, stack.pop().getValue(), resultType);
    }

    //表达式计算的具体细节
    private static String calcValue(Element num1, Element num2, Element operate) {
        boolean boolNum1 = false;
        boolean boolNum2 = false;
        double doubleNum1 = 0;
        double doubleNum2 = 0;
        String stringNum1 = null;
        String stringNum2 = null;
        if(Utils.isLogicOperate(operate.getValue())) {
            boolNum1 = Boolean.parseBoolean(num1.getValue());
            boolNum2 = Boolean.parseBoolean(num2.getValue());
        } else {
            if (Utils.hasQuotation(num1.getValue())) {
                stringNum1 = num1.getValue();
            } else {
                doubleNum1 = Double.parseDouble(num1.getValue());
            }
            if (Utils.hasQuotation(num2.getValue())) {
                stringNum2 = num2.getValue();
            } else {
                doubleNum2 = Double.parseDouble(num2.getValue());
            }
        }
        switch (operate.getElementType()) {
            case Add:
                if(stringNum1==null) {
                    return BigDecimal.valueOf(doubleNum1).add(BigDecimal.valueOf(doubleNum2)).doubleValue() + "";
                } else {
                    return stringNum1.substring(0,stringNum1.length()-1) + stringNum2.substring(1);
                }
            case Minus:return BigDecimal.valueOf(doubleNum1).subtract(BigDecimal.valueOf(doubleNum2)).doubleValue() + "";
            case Multiply:return BigDecimal.valueOf(doubleNum1).multiply(BigDecimal.valueOf(doubleNum2)).doubleValue() + "";
            case Divide:return BigDecimal.valueOf(doubleNum1).divide(BigDecimal.valueOf(doubleNum2), Constant.PRECISION, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
            case Mod:return BigDecimal.valueOf(doubleNum1).divideAndRemainder(BigDecimal.valueOf(doubleNum2))[1].doubleValue() + "";
            case GT:return (doubleNum1 > doubleNum2) + "";
            case GE:return (doubleNum1 >= doubleNum2) + "";
            case LT:return (doubleNum1 < doubleNum2) + "";
            case LE:return (doubleNum1 <= doubleNum2) + "";
            case EQ:
                if(stringNum1 == null) {
                    return (doubleNum1 == doubleNum2) + "";
                } else {
                    return stringNum1.equals(stringNum2) + "";
                }
            case NE:
                if(stringNum1 == null) {
                    return (doubleNum1 != doubleNum2) + "";
                } else {
                    return !stringNum1.equals(stringNum2) + "";
                }
            case And:return (boolNum1 && boolNum2) + "";
            case Or:return (boolNum1 || boolNum2) + "";
            default:return null;
        }
    }
}
