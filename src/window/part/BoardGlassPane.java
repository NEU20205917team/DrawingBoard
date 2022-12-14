package window.part;

import MyComponent.MyComponent;
import MyComponent.myGraph.JGraphFactory;
import MyComponent.myLine.JDrawLine;
import MyComponent.myLine.MyPoint;
import MyComponent.textarea.JMyTextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class BoardGlassPane extends JPanel implements MouseListener, MouseMotionListener {
    private final Board board;

    private boolean isMake = false;
    private MyPoint mousePressedPoint;

    public BoardGlassPane(Board b){
        board = b;
        setLayout(null);
        setOpaque(false);

        addMouseMotionListener(this);
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount()>=2 )
            board.toolBox.setSelection(selects.Mouse);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressedPoint = new MyPoint(e.getX(),e.getY());
        switch (board.toolBox.getSelection()){
            //鼠标选择
            case Mouse-> System.out.println("点到底部了");
            case Rubber -> isMake = true;
            case Pen->{
                isMake = true;
                board.jDrawLines.add(new JDrawLine(board.toolBox.getDrawLineColor(),board.toolBox.getDrawLineStroke()));
                board.jDrawLines.get(board.jDrawLines.size()-1).drawLine(getGraphics());
            }
            case CreatTextArea,CreatJGraph->{
                isMake = true;
                //创建图形
                MyComponent myComponent;
                if(board.toolBox.getSelection() == selects.CreatTextArea)
                    myComponent = new JMyTextArea(board.toolBox.getTextFont(),board.toolBox.getDrawLineColor());
                else
          /*?*/     myComponent = JGraphFactory.creatJGraph(board.toolBox,board.toolBox.getGraphType());
                //添加组件
                board.add(myComponent,board.maxLayer++);
                //选中当前组件
                board.changeChooseGraph(myComponent);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(isMake){
            isMake = false;
            switch (board.toolBox.getSelection()){
                case Pen-> board.jDrawLines.get(board.jDrawLines.size()-1).drawLine(getGraphics());
                case CreatJGraph,CreatTextArea->{
                    //如果大小为零，则删除（防误触）
                    MyComponent myComponent = board.getChooseGraph();
                    if(myComponent!=null && (myComponent.getHeight()<0 || myComponent.getHeight()<0))
                        board.remove((Component) myComponent);
                    else if(myComponent!=null)
                        myComponent.setEnabled(false);
                }
            }
        }
        board.changeChooseGraph(null);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(isMake){
            switch (board.toolBox.getSelection()){
                case Rubber -> {
                    board.jDrawLines.removeIf(jDrawLine -> {
                        //如果有删除,删除后为空，则删除线段记录
                        if(jDrawLine.deletePoint(new MyPoint(e.getX(), e.getY()),(int)board.toolBox.getDrawLineStroke().getLineWidth()*3))
                            return jDrawLine.isEmpty();
                        return false;
                    });
                    board.repaint();
                }
                case Pen->
                    board.jDrawLines.get(board.jDrawLines.size()-1).addAndDrawPoint(new MyPoint(e.getX(),e.getY()),getGraphics());
                case CreatJGraph,CreatTextArea-> board.getChooseGraph().resize(mousePressedPoint,new MyPoint(e.getX(),e.getY()));
                default -> System.out.println( "创建失败"+board.toolBox.getSelection());
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        board.dispatchEvent(SwingUtilities.convertMouseEvent((BoardGlassPane)e.getSource(),e,board));
    }
}
