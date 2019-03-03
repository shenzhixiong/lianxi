import java.util.ArrayList;
import java.util.List;

/**
 * 表达式类，用于存放正确的表达式或错误表达式的错误信息
 */
public class Express {
    private boolean flag;//表达式是否正确
    private String expressOrError;//表达式或错误信息
    public static List<Element> elementList = new ArrayList<Element>();//存放元素列表

    public Express(boolean flag, String expressOrError) {
        this.flag = flag;
        this.expressOrError = expressOrError;
    }

    public boolean isFlag() {
        return flag;
    }

    public String getExpressOrError() {
        return expressOrError;
    }

    //对表达式进行空格处理，获取处理后的表达式
    public static Express getExpress(String express) {
        express = express.trim();
        if(Utils.hasQuotation(express)) {
            List<Integer> quotationIndexes = getQuotationInfo(express);
            int count = quotationIndexes.size();
            if(count%2==1) {
                return new Express(false, "表达式中双引号数量错误");
            }
            String[] oldStrings = new String[count/2+1];
            oldStrings[0] = express.substring(0, quotationIndexes.get(0));
            for(int i=1,j=1;i<count-1;i+=2,j++) {
                oldStrings[j] = express.substring(quotationIndexes.get(i)+1, quotationIndexes.get(i+1));
            }
            oldStrings[oldStrings.length-1] = express.substring(quotationIndexes.get(count-1)+1);
            String[] newStrings = new String[oldStrings.length];
            for(int i=0;i<oldStrings.length;i++) {
                if(hasAbnormalBlank(oldStrings[i])) {
                    return new Express(false, "表达式中含有异常空格");
                }
                newStrings[i] = deleteBlank(oldStrings[i]);
                express = express.replace(oldStrings[i], newStrings[i]);
            }
        } else {
            if(hasAbnormalBlank(express)) {
                return new Express(false, "表达式中含有异常空格");
            }
            express = deleteBlank(express);
        }
        return new Express(true, express);
    }

    //检查表达式是否含有异常空格
    private static boolean hasAbnormalBlank(String express) {
        express = express.trim();
        for(int i=0; i<express.length(); i++) {
            Element currentElement = new Element(express.charAt(i) + "");
            if(currentElement.getElementType() == ElementType.Blank) {
                Element beforeElement = new Element(express.charAt(i - 1) + "");
                Element afterElement = new Element(express.charAt(i + 1) + "");
                if(Utils.isNum(beforeElement.getValue()) && Utils.isNum(afterElement.getValue())) {
                    return true;
                }
                if((beforeElement.getElementType() == ElementType.GT || beforeElement.getElementType() == ElementType.LT || beforeElement.getElementType() == ElementType.Not) && (afterElement.getElementType() == ElementType.EQ)) {
                    return true;
                }
                if(Utils.isLetter(beforeElement.getValue()) && Utils.isLetter(afterElement.getValue())) {
                    return true;
                }
                if((Utils.isPoint(beforeElement.getValue())) || (Utils.isPoint(afterElement.getValue()))) {
                    return true;
                }
                if(Utils.isLogicOperate(beforeElement.getValue()) && Utils.isLogicOperate(afterElement.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    //删除表达式中非异常空格
    private static String deleteBlank(String express) {
        express = express.trim();
        char[] temp = new char[express.length()];
        for(int i=0,j=0; j<express.length(); j++) {
            Element element = new Element(express.charAt(j) + "");
            if(element.getElementType() == ElementType.Blank) {
                continue;
            }
            temp[i++] = express.charAt(j);
        }
        express = new String(temp).trim();
        return express;
    }

    //获取表达式中双引号的位置信息
    private static List<Integer> getQuotationInfo(String express) {
        List<Integer> quotationIndexes = new ArrayList<Integer>();
        for(int i=0;i<express.length();i++) {
            if(new Element(express.charAt(i)+"").getElementType() == ElementType.String) {
                quotationIndexes.add(i);
            }
        }
        return quotationIndexes;
    }
}


