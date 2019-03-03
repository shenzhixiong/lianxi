/**
 * 元素类（用于对表达式拆分）
 */
public class Element {
    private String value;//元素值
    private ElementType elementType;//元素类型
    private int priority;//元素优先级

    public Element(String value) {
        this.value = value;
        this.elementType = getElementType(value);
        this.priority = getPriority(elementType);
    }

    public String getValue() {
        return value;
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public int getPriority() {
        return priority;
    }

    //获取对应字符串的类型
    private static ElementType getElementType(String value) {
        switch (value)
        {
            case " ":
                return ElementType.Blank;
            case ".":
                return ElementType.Point;
            case ",":
                return ElementType.Comma;
            case "+":
                return ElementType.Add;
            case "-":
                return ElementType.Minus;
            case "*":
                return ElementType.Multiply;
            case "/":
                return ElementType.Divide;
            case "%":
                return ElementType.Mod;
            case "&":
                return ElementType.BitwiseAnd;
            case "|":
                return ElementType.BitwiseOr;
            case "&&":
                return ElementType.And;
            case "||":
                return ElementType.Or;
            case "!":
                return ElementType.Not;
            case ">":
                return ElementType.GT;
            case "<":
                return ElementType.LT;
            case "=":
                return ElementType.EQ;
            case ">=":
                return ElementType.GE;
            case "<=":
                return ElementType.LE;
            case "!=":
                return ElementType.NE;
            case "(":
                return ElementType.LParenthesis;
            case ")":
                return ElementType.RParenthesis;
        }
        if(Utils.isNum(value)) {
            return ElementType.Digital;
        }
        if(Utils.isLetter(value)) {
            return ElementType.Letter;
        }
        if(Utils.hasQuotation(value)) {
            return ElementType.String;
        }
        if(Utils.isBoolean(value)) {
            return ElementType.Boolean;
        }
        if(Utils.isFunction(value)) {
            return ElementType.Function;
        }
        return ElementType.Undefined;
    }

    //获取对应类型的优先级
    private static int getPriority(ElementType elementType) {
        switch (elementType)
        {
            case LParenthesis:
            case RParenthesis:
                return 7;
            //逻辑非是一元操作符,所以其优先级较高
            case Not:
                return 6;
            case Mod:
                return 5;
            case Multiply:
            case Divide:
                return 4;
            case Add:
            case Minus:
                return 3;
            case GT:
            case GE:
            case LT:
            case LE:
            case EQ:
            case NE:
                return 2;
            case And:
            case Or:
                return 1;
            default:
                return 0;
        }
    }
}
