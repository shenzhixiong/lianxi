/**
 * 存放表达式计算结果信息类
 */
public class Response {
    private boolean flag;
    private String result;
    private ResultType resultType = ResultType.Undefined;
    private String errorMessage;

    public Response(boolean flag, String result, ResultType resultType) {
        this.flag = flag;
        this.result = result;
        this.resultType = resultType;
    }

    public Response(boolean flag, String errorMessage) {
        this.flag = flag;
        this.errorMessage = errorMessage;
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

    public String getResultTypeInfo() {
        switch (resultType) {
            case Undefined:return "undefined";
            case String:return "String";
            case Double:return "double";
            case Boolean:return "boolean";
            default:return null;
        }
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

    public String toString() {
        if (flag == true) {
            return result + "";
        }
        return errorMessage;
    }
}
