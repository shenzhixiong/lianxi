import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * 测试类
 */

public class Test extends JFrame implements ActionListener{
    JPanel panel1 = new JPanel();
    JLabel label1 = new JLabel("表达式:");
    JTextField textField1 = new JTextField(52);
    JButton button1 = new JButton("计算");
    JPanel panel2 = new JPanel();
    JLabel label2 = new JLabel();
    public Test() {
        this.setLayout(new FlowLayout());
        button1.addActionListener(this);
        panel1.add(label1);
        panel1.add(textField1);
        panel1.add(button1);
        this.add(panel1);
        StringBuilder sb = new StringBuilder("<html><body><p>说明：</p>");
        sb.append("<p>1.表达式支持的字符串运算包含“+”(拼接)、“=”(是否相等)、“!=”(是否不等)。</p>");
        sb.append("<p>2.表达式支持的四则运算符包含“+”、“-”、“*”、“/”、“%”，其中“+”和“-”也可以表示数值的正负。</p>");
        sb.append("<p>3.表达式支持的关系运算符包含“>”、“&lt;”、“=”、“>=”、“&lt;=”、“!=”。</p>");
        sb.append("<p>4.表达式支持的逻辑运算符包含“&&”(逻辑与)、“||”(逻辑或)、“!”(逻辑非)。</p>");
        sb.append("<p>5.表达式支持的函数包含iif、subString、absolute、length、round、power、square、areaOfTriangle、areaOfCircular。</p>");
        sb.append("<p>6.表达式支持以上五种情形进行混合使用，如：iif(length(\"12345\")>square(4),areaOfTriangle(3,4,5),areaOfCircular(1))。</p>");
        sb.append("</body></html>");
        label2.setText(sb.toString());
        panel2.add(label2);
        this.add(panel2);
        this.setTitle("复杂表达式计算");
        this.setBounds(400, 150,720, 220);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String express = textField1.getText().trim();
        Response response = ExpressUtil.calculate(express);
        JFrame frame = new JFrame("计算结果");
        StringBuilder sb = new StringBuilder("<html><body>");
        sb.append("<p>计算结果："+response.getResult()+"</p>");
        sb.append("<p>结果类型："+response.getResultTypeInfo()+"</p>");
        sb.append("<p>错误信息："+response.getErrorMessage()+"</p>");
        sb.append("</body></html>");
        JLabel label = new JLabel(sb.toString(),JLabel.CENTER);
        frame.add(label);
        frame.setBounds(450,200,300,120);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Test();
    }
}