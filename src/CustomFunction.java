/**
 * 函数类（用于对自定义函数进行处理）
 */
public class CustomFunction {
    private boolean flag;//函数是否正确
    private String functionName;//函数名
    private String result;//函数返回结果
    private ResultType resultType;//函数返回结果的类型
    private String errorMessage;//函数错误信息

    public CustomFunction(String functionName) {
        this.functionName = functionName;
    }

    public CustomFunction(boolean flag, String result, ResultType resultType) {
        this.flag = flag;
        this.result = result;
        this.resultType = resultType;
    }

    public CustomFunction(boolean flag, String errorMessage) {
        this.flag = flag;
        this.errorMessage = errorMessage;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
