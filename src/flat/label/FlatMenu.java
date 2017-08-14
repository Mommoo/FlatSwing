package flat.label;

import flat.component.OnClickListener;
import flat.frame.FlatFrame;
import util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * Created by mommoo on 2017-03-17.
 */
public class FlatMenu extends FlatLabel implements MouseListener {
    private static final int PADDING = ScreenManager.getInstance().dip2px(2);
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
    private boolean isMouseExited;
    private Color mouseEnterMenuColor = Color.lightGray;
    private Color mouseEnterMenuTextColor = Color.BLACK;
    private Color menuColor = Color.white;
    private Color menuTextColor = Color.BLACK;
    private OnClickListener onClickListener;

    public FlatMenu(String menuText){
        setText(menuText);
        setColumns(22);
        setDragEnabled(false);
        setBorder(BorderFactory.createEmptyBorder(PADDING*2,PADDING*2,PADDING*2,PADDING*2));
        setCursor(HAND_CURSOR);
        addMouseListener(this);
        setBackground(menuColor);
    }

    public void setMenuColor(Color menuColor){
        this.menuColor = menuColor;
        setBackground(menuColor);
    }

    public void setMouseEnterMenuColor(Color color){
        this.mouseEnterMenuColor = color;
    }

    public void setMouseEnterMenuTextColor(Color color){
        this.mouseEnterMenuTextColor = color;
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!isMouseExited && this.onClickListener != null){
            this.onClickListener.onClick(this);
            setBackground(menuColor);
            setForeground(menuTextColor);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        isMouseExited = false;
        setBackground(mouseEnterMenuColor);
        setForeground(mouseEnterMenuTextColor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        isMouseExited = true;
        setBackground(menuColor);
        setForeground(menuTextColor);
    }

    public static void main(String[] args){
        FlatMenu flatMenu = new FlatMenu("Menu");

        FlatFrame flatFrame = new FlatFrame();
        flatFrame.setTitle("FlatMenu Test");
        flatFrame.setSize(400,300);
        flatFrame.setLocationOnScreenCenter();
        flatFrame.getContainer().add(flatMenu);
        flatFrame.show();
    }
}
