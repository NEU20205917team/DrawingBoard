package window.area.part;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

//小窗口包含白板和按钮
public class Boarder extends JPanel {
    public Board board;
    public Boarder(Board B){
        setLayout(null);

        board = B;
        //透视按钮
        JButton jb = new JButton();
        add(jb);

//        jb.setIconTextGap(0);//将标签中显示的文本和图标之间的间隔量设置为0
//        jb.setBorderPainted(false);//不打印边框
//        jb.setBorder(null);//除去边框
//        jb.setText(null);//除去按钮的默认名称
//        jb.setFocusPainted(false);//除去焦点的框
        jb.setContentAreaFilled(false);//除去默认的背景填充
        jb.setText("点击");

        addComponentListener(new ComponentAdapter() {
            //重设组件大小
            @Override
            public void componentResized(ComponentEvent e) {
                jb.setBounds(getWidth(),getHeight(),getWidth(),getHeight());
                board.setBounds(getWidth(),getHeight(),getWidth(),getHeight());
            }
        });
    }
}