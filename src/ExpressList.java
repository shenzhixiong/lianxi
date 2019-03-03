import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ExpressList {
    private boolean flag;//表达式是否正确
    private List<Element> expressList = new ArrayList<Element>();//存放表达式元素的列表
    private String errorMessage;//表达式错误信息
    public static ExpressList own;//存放自身信息

    public ExpressList(boolean flag, List<Element> expressList) {
        this.flag = flag;
        this.expressList = expressList;
    }

    public ExpressList(boolean flag, String errorMessage) {
        this.flag = flag;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    //获取表达式的元素列表
    public static ExpressList getExpressList(String express) {
        if(express == null || express == "" || express.length()==0) {
            return new ExpressList(false, "表达式为空");
        }
        Express currentExpress = Express.getExpress(express);
        if(!currentExpress.isFlag()) {
            return new ExpressList(false, currentExpress.getExpressOrError());
        }
        express = currentExpress.getExpressOrError();
        List<Element> expressList = new ArrayList<Element>();
        int start = 0;
        int end = 0;
        int bracketFlag = 0;//括号标记
        int rightBracketCount = 0;//右括号数量
        for(int i=0; i<express.length(); i++) {
            Element currentElement = new Element(express.charAt(i) + "");
            if(Utils.isOperate(currentElement.getValue())) {
                Element afterElement = null;
                if(i + 1 < express.length()) {
                    afterElement = new Element(express.charAt(i + 1) + "");
                }
                if((currentElement.getElementType() == ElementType.Minus || currentElement.getElementType() == ElementType.Add) && (i == 0)) {
                    expressList.add(new Element("0"));
                }
                if(i > 0) {
                    Element beforeElement = new Element(express.charAt(i - 1) + "");
                    if((currentElement.getElementType() == ElementType.Minus || currentElement.getElementType() == ElementType.Add) && (!Utils.isNum(beforeElement.getValue()) && beforeElement.getElementType() != ElementType.RParenthesis && beforeElement.getElementType() != ElementType.String && beforeElement.getElementType() != ElementType.Letter)) {
                        expressList.add(new Element("0"));
                    }
                }
                if((i + 1) < express.length()) {
                    if(Utils.isNotLogicOperate(currentElement.getValue()) && Utils.isNotLogicOperate(afterElement.getValue()) && afterElement.getElementType() != ElementType.EQ) {
                        return new ExpressList(false, "运算符\"" + currentElement.getValue() + "\"左右两边不全为数值");
                    }
                }
                if(currentElement.getElementType() == ElementType.LParenthesis) {
                    bracketFlag++;
                }
                if(currentElement.getElementType() == ElementType.RParenthesis) {
                    rightBracketCount++;
                    bracketFlag--;
                    if(bracketFlag < 0) {
                        return new ExpressList(false, "第" + rightBracketCount + "个右括号错误");
                    }
                }
                end = i;
                if(end > start) {
                    expressList.add(new Element(express.substring(start, end)));
                }
                if((i + 1) < express.length()) {
                    if (currentElement.getElementType() == ElementType.GT && afterElement.getElementType() == ElementType.EQ) {
                        currentElement.setElementType(ElementType.GE);
                        i++;
                    }
                    if (currentElement.getElementType() == ElementType.LT && afterElement.getElementType() == ElementType.EQ) {
                        currentElement.setElementType(ElementType.LE);
                        i++;
                    }
                    if (currentElement.getElementType() == ElementType.Not) {
                        if (afterElement.getElementType() == ElementType.EQ) {
                            currentElement.setElementType(ElementType.NE);
                            i++;
                        } else {
                            currentElement.setElementType(ElementType.Not);
                        }
                    }
                    if (currentElement.getElementType() == ElementType.BitwiseAnd) {
                        if(afterElement.getElementType() == ElementType.BitwiseAnd) {
                            currentElement.setElementType(ElementType.And);
                            i++;
                        } else {
                            return new ExpressList(false, "错误字符\"" + currentElement.getValue() + "\"");
                        }
                    }
                    if (currentElement.getElementType() == ElementType.BitwiseOr) {
                        if(afterElement.getElementType() == ElementType.BitwiseOr) {
                            currentElement.setElementType(ElementType.Or);
                            i++;
                        } else {
                            return new ExpressList(false, "错误字符\"" + currentElement.getValue() + "\"");
                        }
                    }
                }
                expressList.add(currentElement);
                start = i+1;
            } else if(!Utils.isNum(currentElement.getValue())){
                if(currentElement.getElementType() == ElementType.Letter) {
                    Element tempElement = currentElement;
                    start = i;
                    while(tempElement.getElementType() == ElementType.Letter) {
                        i++;
                        if(i < express.length()) {
                            tempElement = new Element(express.charAt(i) + "");
                        } else {
                            break;
                        }
                    }
                    String tempStr = express.substring(start, i);
                    if(Utils.isBoolean(tempStr)) {
                        expressList.add(new Element(tempStr));
                        i -= 1;
                        start = i + 1;
                    } else if(Utils.isFunction(tempStr)) {
                        currentElement = new Element(express.charAt(i) + "");
                        int leftMinusRight = 0;
                        if(currentElement.getElementType() == ElementType.LParenthesis) {
                            leftMinusRight++;
                        } else {
                            return new ExpressList(false, "函数名后面缺少左括号");
                        }
                        while(currentElement.getElementType()!=ElementType.RParenthesis||leftMinusRight!=0) {
                            i++;
                            currentElement = new Element(express.charAt(i)+"");
                            if(currentElement.getElementType()==ElementType.LParenthesis) {
                                leftMinusRight++;
                            }
                            if(currentElement.getElementType()==ElementType.RParenthesis) {
                                leftMinusRight--;
                            }
                            if(i==express.length()) {
                                return new ExpressList(false, "函数没有结尾右括号");
                            }
                        }
                        String oldString = express.substring(start, i+1);
                        CustomFunction customFunction = CustomFunctionManage.calcFunction(oldString);
                        if(customFunction.isFlag()) {
                            currentElement = new Element(customFunction.getResult());
                            expressList.add(currentElement);
                            start = i+1;
                        } else {
                            return new ExpressList(false, customFunction.getErrorMessage());
                        }
                    } else {
                        return new ExpressList(false, "错误字符\"" + tempStr + "\"");
                    }
                } else if(currentElement.getElementType() == ElementType.Point) {
                    if(i + 1 < express.length()) {
                        if(!Utils.isNum(express.charAt(i + 1) + "")) {
                            return new ExpressList(false, "字符\"" + currentElement.getValue() + "\"后面必须为数字");
                        }
                    } else {
                        return new ExpressList(false, "字符\"" + currentElement.getValue() + "\"后面必须为数字");
                    }
                } else if(currentElement.getElementType() == ElementType.String) {
                    if(i>0) {
                        Element beforeElement = new Element(express.charAt(i-1)+"");
                        if(!Utils.isOperate(beforeElement.getValue())) {
                            return new ExpressList(false, "双引号位置错误");
                        }
                    }
                    if(i+1 < express.length()) {
                        Element afterElement = new Element(express.charAt(i+1)+"");
                        while(afterElement.getElementType()!=ElementType.String&&i<express.length()) {
                            i++;
                            afterElement = new Element(express.charAt(i)+"");
                        }
                        expressList.add(new Element(express.substring(start, i+1)));
                        start=i+1;
                    } else {
                        return new ExpressList(false, "双引号数量错误");
                    }
                } else {
                    return new ExpressList(false, "错误字符\"" + currentElement.getValue() + "\"");
                }
            }
        }
        if(start < express.length()) {
            expressList.add(new Element(express.substring(start, express.length())));
        }
        if(bracketFlag != 0) {
            return new ExpressList(false, "左右括号数量不相等");
        }
        return new ExpressList(true, expressList);
    }

    //获取后缀表达式
    public static Stack getSuffix(String express) {
        own = getExpressList(express);
        if(own.flag == false) {
            return null;
        }
        List<Element> list = own.expressList;
        Stack<Element> aStack = new Stack<Element>();
        Stack<Element> bStack = new Stack<Element>();
        for(int i=0; i<list.size(); i++) {
            Element currentElement = list.get(i);
            if(currentElement.getElementType() == ElementType.Digital || currentElement.getElementType() == ElementType.Boolean || currentElement.getElementType() == ElementType.String) {
                bStack.push(currentElement);
            } else if(currentElement.getElementType() == ElementType.LParenthesis) {
                if(i>0) {
                    Element beforeElement = list.get(i - 1);
                    if (!(Utils.isOperate(beforeElement.getValue()) || beforeElement.getElementType() == ElementType.LParenthesis)) {
                        own = new ExpressList(false, "表达式缺少运算符错误");
                        return null;
                    }
                }
                aStack.push(currentElement);
            }
            else if(currentElement.getElementType() == ElementType.RParenthesis) {
                if(i<list.size()-1) {
                    Element afterElement = list.get(i+1);
                    if(!(Utils.isOperate(afterElement.getValue())||afterElement.getElementType()==ElementType.RParenthesis)) {
                        own = new ExpressList(false, "表达式缺少运算符错误");
                        return null;
                    }
                }
                while (!aStack.empty() && aStack.peek().getElementType() != ElementType.LParenthesis) {
                    bStack.push(aStack.pop());
                }
                if(!aStack.empty()) {
                    aStack.pop();
                }
            } else {
                while (!aStack.empty() && aStack.peek().getPriority() >= currentElement.getPriority() && aStack.peek().getElementType() != ElementType.LParenthesis) {
                    bStack.push(aStack.pop());
                }
                aStack.push(currentElement);
            }
        }
        while (!aStack.empty()) {
            bStack.push(aStack.pop());
        }
        return bStack;
    }
}
