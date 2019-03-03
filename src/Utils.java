import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 */
public class Utils {

    //判断字符串是否为数值
    public static boolean isNum(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //判断字符串是否为非负整数
    public static boolean isPositiveNum(String value) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(value);
        return isNum.matches();
    }

    //判断字符串是否为布尔值
    public static boolean isBoolean(String value) {
        if("true".equals(value) || "false".equals(value)) {
            return true;
        }
        return false;
    }

    //判断字符串是否为零
    public static boolean isZero(String value) {
        boolean bool = true;
        for (int i=0; i<value.length(); i++) {
            if(value.charAt(i) == '.' || value.charAt(i) == '0') {
                continue;
            }
            bool = false;
        }
        return bool;
    }

    //判断字符串是否为运算符
    public static boolean isOperate(String value) {
        Element element = new Element(value);
        if(element.getElementType() == ElementType.Add || element.getElementType() == ElementType.Minus || element.getElementType() == ElementType.Multiply || element.getElementType() == ElementType.Divide || element.getElementType() == ElementType.Mod) {
            return true;
        }
        if(element.getElementType() == ElementType.LParenthesis || element.getElementType() == ElementType.RParenthesis || element.getElementType() == ElementType.GT || element.getElementType() == ElementType.LT || element.getElementType() == ElementType.EQ) {
            return true;
        }
        if(element.getElementType() == ElementType.Not || element.getElementType() == ElementType.And || element.getElementType() == ElementType.Or || element.getElementType() == ElementType.BitwiseAnd || element.getElementType() == ElementType.BitwiseOr) {
            return true;
        }
        return false;
    }

    //判断运算符是否为逻辑与或逻辑或
    public static boolean isLogicOperate(String operate) {
        if(operate.equals("&") || operate.equals("|")) {
            return true;
        }
        return false;
    }

    //判断运算符是否为逻辑非或不等于（即是否为“!”）
    public static boolean isNotOperate(String operate) {
        if("!".equals(operate)) {
            return true;
        }
        return false;
    }

    //判断运算符是否为加、减、乘、除、模、大于、小于、等于
    public static boolean isNotLogicOperate(String operate) {
        if("+".equals(operate) || "-".equals(operate) || "*".equals(operate) || "/".equals(operate) || "%".equals(operate) || ">".equals(operate) || "<".equals(operate) || "=".equals(operate)) {
            return true;
        }
        return false;
    }

    //判断表达式是否含有双引号
    public static boolean hasQuotation(String express) {
        if(express.contains("\"")) {
            return true;
        }
        return false;
    }

    //判断表达式是否含有逗号
    public static boolean hasComma(String express) {
        if(express.contains(",")) {
            return true;
        }
        return false;
    }

    //判断字符串是否为英文字符
    public static boolean isLetter(String value) {
        if(value.length() == 1) {
            char ch = value.charAt(0);
            if((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
                return true;
            }
        }
        return false;
    }

    //判断字符串是否为小数点
    public static boolean isPoint(String value) {
        if(".".equals(value)) {
            return true;
        }
        return false;
    }

    //判断字符串对应的函数是否存在
    public static boolean isFunction(String value) {
        CustomFunctionManage functionManage = new CustomFunctionManage();
        List<CustomFunction> functionList = functionManage.getFunctionList();
        for(int i=0; i<functionList.size(); i++) {
            if(value.equals(functionList.get(i).getFunctionName())) {
                return true;
            }
        }
        return false;
    }

    //判断表达式中是否含有函数
    public static boolean hasFunction(String express) {
        CustomFunctionManage functionManage = new CustomFunctionManage();
        List<CustomFunction> functionList = functionManage.getFunctionList();
        for(int i=0; i<functionList.size(); i++) {
            if(express.contains(functionList.get(i).getFunctionName())) {
                return true;
            }
        }
        return false;
    }

    //判断运算符是否为关系运算符
    public static boolean isRelationOperate(ElementType elementType) {
        if(elementType == ElementType.GT || elementType == ElementType.LT || elementType == ElementType.EQ || elementType == ElementType.GE || elementType == ElementType.LE || elementType == ElementType.NE) {
            return true;
        }
        return false;
    }
}
