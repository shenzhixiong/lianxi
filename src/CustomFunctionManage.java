import java.util.ArrayList;
import java.util.List;

/**
 * 函数管理类
 */
public class CustomFunctionManage {
    private List<CustomFunction> functionList = new ArrayList<CustomFunction>();//存储函数

    public CustomFunctionManage() {
        init();
    }

    public List<CustomFunction> getFunctionList() {
        return functionList;
    }

    public void setFunctionList(List<CustomFunction> functionList) {
        this.functionList = functionList;
    }

    //初始化函数列表
    private void init() {
        CustomFunction customFunction1 = new CustomFunction("iif");
        functionList.add(customFunction1);
        CustomFunction customFunction2 = new CustomFunction("subString");
        functionList.add(customFunction2);
        CustomFunction customFunction3 = new CustomFunction("absolute");
        functionList.add(customFunction3);
        CustomFunction customFunction4 = new CustomFunction("length");
        functionList.add(customFunction4);
        CustomFunction customFunction5 = new CustomFunction("round");
        functionList.add(customFunction5);
        CustomFunction customFunction6 = new CustomFunction("power");
        functionList.add(customFunction6);
        CustomFunction customFunction7 = new CustomFunction("square");
        functionList.add(customFunction7);
        CustomFunction customFunction8 = new CustomFunction("areaOfTriangle");
        functionList.add(customFunction8);
        CustomFunction customFunction9 = new CustomFunction("areaOfCircular");
        functionList.add(customFunction9);
    }

    //对函数进行计算
    public static CustomFunction calcFunction(String express) {
        String functionName = express.substring(0, express.indexOf("("));
        switch (functionName) {
            case "iif":return calcForIIF(express);
            case "subString":return calcForSubString(express);
            case "absolute":return calcForAbsolute(express);
            case "round":return calcForRound(express);
            case "length":return calcForLength(express);
            case "power":return calcForPower(express);
            case "square":return calcForSquare(express);
            case "areaOfTriangle":return calcForAreaOfTriangle(express);
            case "areaOfCircular":return calcForAreaOfCircular(express);
            default:return null;
        }
    }

    //获取函数参数
    private static String[] getParameters(String express) {
        express = express.substring(express.indexOf("(")+1, express.lastIndexOf(")"));
        List<Integer> commaIndexs = new ArrayList<Integer>();
        int leftMinusRight = 0;
        for(int i=0;i<express.length();i++) {
            if(new Element(express.charAt(i)+"").getElementType() == ElementType.LParenthesis) {
                leftMinusRight++;
            } else if(new Element(express.charAt(i)+"").getElementType() == ElementType.RParenthesis){
                leftMinusRight--;
            } else if(new Element(express.charAt(i)+"").getElementType() == ElementType.Comma) {
                if(leftMinusRight == 0) {
                    commaIndexs.add(i);
                }
            }
        }
        String[] strings = new String[commaIndexs.size()+1];
        if(commaIndexs.size()>0) {
            strings[0] = express.substring(0, commaIndexs.get(0));
            for (int i = 1; i < commaIndexs.size(); i++) {
                strings[i] = express.substring(commaIndexs.get(i - 1) + 1, commaIndexs.get(i));
            }
            strings[strings.length - 1] = express.substring(commaIndexs.get(commaIndexs.size() - 1) + 1);
        } else {
            strings[0] = express;
        }
        return strings;
    }

    //计算iif函数
    private static CustomFunction calcForIIF(String express) {
        String[] strings = getParameters(express);
        if(strings.length != 3) {
            return new CustomFunction(false, "iif函数的参数个数不对");
        }
        Response response1 = ExpressUtil.calculate(strings[0]);
        Response response2 = ExpressUtil.calculate(strings[1]);
        Response response3 = ExpressUtil.calculate(strings[2]);
        if(!response2.isFlag()) {
            return new CustomFunction(false, "iif函数的第二个参数错误:"+response2.getErrorMessage());
        }
        if(!response3.isFlag()) {
            return new CustomFunction(false, "iif函数的第三个参数错误:"+response3.getErrorMessage());
        }
        if(response2.getResultType()!=response3.getResultType()) {
            return new CustomFunction(false, "iif函数的第二个参数与第三个参数类型必须一致");
        }
        if(response1.isFlag()) {
            if(response1.getResultType() == ResultType.Boolean) {
                if(response1.getResult().length() == 4) {
                    return new CustomFunction(true, response2.getResult(), response2.getResultType());
                } else {
                    return new CustomFunction(true, response3.getResult(), response3.getResultType());
                }
            } else {
                return new CustomFunction(false, "iif函数的第一个参数必须为布尔值");
            }
        } else {
            return new CustomFunction(false, "iif函数的第一个参数错误:"+response1.getErrorMessage());
        }
    }

    //计算subString函数
    private static CustomFunction calcForSubString(String express) {
        String[] strings = getParameters(express);
        if (strings.length != 3) {
            return new CustomFunction(false, "subString函数的参数个数不对");
        }
        Response response1 = ExpressUtil.calculate(strings[0]);
        Response response2 = ExpressUtil.calculate(strings[1]);
        Response response3 = ExpressUtil.calculate(strings[2]);
        if (response1.isFlag()) {
            if (response1.getResultType() != ResultType.String) {
                return new CustomFunction(false, "subString函数的第一个参数必须为字符串");
            }
        } else {
            return new CustomFunction(false, "subString函数的第一个参数错误:" + response1.getErrorMessage());
        }
        if (response2.isFlag()) {
            if (response2.getResultType() == ResultType.Double) {
                String value = response2.getResult();
                if (!Utils.isPositiveNum(value) && !value.endsWith(".0")) {
                    return new CustomFunction(false, "subString函数的第二个参数必须为非负整数");
                }
                if(value.endsWith(".0")) {
                    value = value.substring(0, value.indexOf(".0"));
                    response2.setResult(value);
                }
            } else {
                return new CustomFunction(false, "subString函数的第二个参数类型错误");
            }
        } else {
            return new CustomFunction(false, "subString函数的第二个参数错误:" + response2.getErrorMessage());
        }
        if (response3.isFlag()) {
            if (response3.getResultType() == ResultType.Double) {
                String value = response3.getResult();
                if (!Utils.isPositiveNum(value) && !value.endsWith(".0")) {
                    return new CustomFunction(false, "subString函数的第三个参数必须为非负整数");
                }
                if(value.endsWith(".0")) {
                    value = value.substring(0, value.indexOf(".0"));
                    response3.setResult(value);
                }
            } else {
                return new CustomFunction(false, "subString函数的第三个参数类型错误");
            }
        } else {
            return new CustomFunction(false, "subString函数的第三个参数错误:"+response3.getErrorMessage());
        }
        int length = response1.getResult().length()-2;
        int value1 = Integer.parseInt(response2.getResult());
        int value2 = Integer.parseInt(response3.getResult());
        if(value1<0 || value1>value2 || value1>length) {
            return new CustomFunction(false, "subString函数的第二个参数必须介于0和第一个参数长度数值之间，且不能大于第三个参数");
        }
        if(value2<0 || value2<value1 || value2>length) {
            return new CustomFunction(false, "subString函数的第三个参数必须介于0和第一个参数长度数值之间，且不能小于第二个参数");
        }
        String result = response1.getResult().substring(Integer.parseInt(response2.getResult())+1, Integer.parseInt(response3.getResult())+1);
        return new CustomFunction(true, "\""+result+"\"", ResultType.String);
    }

    //计算absolute函数
    private static CustomFunction calcForAbsolute(String express) {
        String[] strings = getParameters(express);
        if(strings.length != 1) {
            return new CustomFunction(false, "absolute函数的参数个数不对");
        }
        Response response1 = ExpressUtil.calculate(strings[0]);
        if(response1.isFlag()) {
            if(response1.getResultType() == ResultType.Double) {
                return new CustomFunction(true, Math.abs(Double.parseDouble(response1.getResult()))+"", response1.getResultType());
            } else {
                return new CustomFunction(false, "absolute函数的参数必须为数值型");
            }
        } else {
            return new CustomFunction(false, "absolute函数的参数错误:"+response1.getErrorMessage());
        }
    }

    //计算round函数
    private static CustomFunction calcForRound(String express) {
        String[] strings = getParameters(express);
        if(strings.length != 1) {
            return new CustomFunction(false, "round函数的参数个数不对");
        }
        Response response1 = ExpressUtil.calculate(strings[0]);
        if(response1.isFlag()) {
            if(response1.getResultType() != ResultType.Double) {
                return new CustomFunction(false, "round函数的参数必须为数值型");
            }
        } else {
            return new CustomFunction(false, "round函数的参数错误:"+response1.getErrorMessage());
        }
        Double value1 = Double.parseDouble(response1.getResult());
        return new CustomFunction(true, Math.round(value1)+"", response1.getResultType());
    }

    //计算length函数
    private static CustomFunction calcForLength(String express) {
        String[] strings = getParameters(express);
        if(strings.length != 1) {
            return new CustomFunction(false, "length函数的参数个数不对");
        }
        Response response1 = ExpressUtil.calculate(strings[0]);
        if(response1.isFlag()) {
            if(response1.getResultType() != ResultType.String) {
                return new CustomFunction(false, "length函数的参数必须为字符串");
            }
        } else {
            return new CustomFunction(false, "length函数的参数错误:"+response1.getErrorMessage());
        }
        return new CustomFunction(true, response1.getResult().length()-2+"", response1.getResultType());
    }

    //计算power函数
    private static CustomFunction calcForPower(String express) {
        String[] strings = getParameters(express);
        if(strings.length != 2) {
            return new CustomFunction(false, "power函数的参数个数不对");
        }
        Response response1 = ExpressUtil.calculate(strings[0]);
        Response response2 = ExpressUtil.calculate(strings[1]);
        if (response1.isFlag()) {
            if (response1.getResultType() != ResultType.Double) {
                return new CustomFunction(false, "power函数的第一个参数类型错误");
            }
        } else {
            return new CustomFunction(false, "power函数的第一个参数错误:" + response1.getErrorMessage());
        }
        if (response2.isFlag()) {
            if (response2.getResultType() != ResultType.Double) {
                return new CustomFunction(false, "power函数的第二个参数类型错误");
            }
        } else {
            return new CustomFunction(false, "power函数的第二个参数错误:" + response2.getErrorMessage());
        }
        double value1 = Double.parseDouble(response1.getResult());
        double value2 = Double.parseDouble(response2.getResult());
        return new CustomFunction(true, Math.pow(value1, value2)+"", response1.getResultType());
    }

    //计算square函数
    private static CustomFunction calcForSquare(String express) {
        String[] strings = getParameters(express);
        if(strings.length != 1) {
            return new CustomFunction(false, "square函数的参数个数不对");
        }
        Response response1 = ExpressUtil.calculate(strings[0]);
        if (response1.isFlag()) {
            if (response1.getResultType() != ResultType.Double) {
                return new CustomFunction(false, "square函数的参数类型错误");
            }
        } else {
            return new CustomFunction(false, "square函数的参数错误:" + response1.getErrorMessage());
        }
        double value1 = Double.parseDouble(response1.getResult());
        if(value1<0) {
            return new CustomFunction(false, "square函数的参数必须≥0");
        }
        return new CustomFunction(true, Math.sqrt(value1)+"", response1.getResultType());
    }
    
    //计算areaOfTriangle函数
    private static CustomFunction calcForAreaOfTriangle(String express) {
        String[] strings = getParameters(express);
        if (strings.length != 3) {
            return new CustomFunction(false, "areaOfTriangle函数的参数个数不对");
        }
        Response response1 = ExpressUtil.calculate(strings[0]);
        Response response2 = ExpressUtil.calculate(strings[1]);
        Response response3 = ExpressUtil.calculate(strings[2]);
        if (response1.isFlag()) {
            if (response1.getResultType() != ResultType.Double) {
                return new CustomFunction(false, "areaOfTriangle函数的第一个参数必须为数值型");
            } else {
                if(Double.parseDouble(response1.getResult()) <= 0) {
                    return new CustomFunction(false, "areaOfTriangle函数的第一个参数必须大于零");
                }
            }
        } else {
            return new CustomFunction(false, "areaOfTriangle函数的第一个参数错误:" + response1.getErrorMessage());
        }
        if (response2.isFlag()) {
            if (response2.getResultType() != ResultType.Double) {
                return new CustomFunction(false, "areaOfTriangle函数的第二个参数必须为数值型");
            } else {
                if(Double.parseDouble(response2.getResult()) <= 0) {
                    return new CustomFunction(false, "areaOfTriangle函数的第二个参数必须大于零");
                }
            }
        } else {
            return new CustomFunction(false, "areaOfTriangle函数的第二个参数错误:" + response2.getErrorMessage());
        }
        if (response3.isFlag()) {
            if (response3.getResultType() != ResultType.Double) {
                return new CustomFunction(false, "areaOfTriangle函数的第三个参数必须为数值型");
            } else {
                if(Double.parseDouble(response3.getResult()) <= 0) {
                    return new CustomFunction(false, "areaOfTriangle函数的第三个参数必须大于零");
                }
            }
        } else {
            return new CustomFunction(false, "areaOfTriangle函数的第三个参数错误:" + response3.getErrorMessage());
        }
        double value1 = Double.parseDouble(response1.getResult());
        double value2 = Double.parseDouble(response2.getResult());
        double value3 = Double.parseDouble(response3.getResult());
        if(!((value1+value2>value3)&&(value1+value3>value2)&&(value2+value3>value1))) {
            return new CustomFunction(false, "areaOfTriangle函数的三个参数不满足三角形成立的条件");
        }
        double p = (value1+value2+value3)/2;
        double area = Math.sqrt(p*(p-value1)*(p-value2)*(p-value3));
        return new CustomFunction(true, area+"", response1.getResultType());
    }

    //计算areaOfCircular函数
    private static CustomFunction calcForAreaOfCircular(String express) {
        String[] strings = getParameters(express);
        if (strings.length != 1) {
            return new CustomFunction(false, "areaOfCircular函数的参数个数不对");
        }
        Response response1 = ExpressUtil.calculate(strings[0]);
        if (response1.isFlag()) {
            if (response1.getResultType() != ResultType.Double) {
                return new CustomFunction(false, "areaOfCircular函数的参数必须为数值型");
            } else {
                if(Double.parseDouble(response1.getResult()) <= 0) {
                    return new CustomFunction(false, "areaOfCircular函数的参数必须大于零");
                }
            }
        } else {
            return new CustomFunction(false, "areaOfTriangle函数的参数错误:" + response1.getErrorMessage());
        }
        double value1 = Double.parseDouble(response1.getResult());
        double area = Math.PI*value1*value1;
        return new CustomFunction(true, area+"", response1.getResultType());
    }
}
