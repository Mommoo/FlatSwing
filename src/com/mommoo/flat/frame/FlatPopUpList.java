package com.mommoo.flat.frame;

import com.mommoo.flat.frame.popup.OnItemClickListener;
import com.mommoo.flat.list.FlatListView;
import com.mommoo.flat.text.label.FlatLabel;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FlatPopUpList {
    private static final ScreenManager screenManager =ScreenManager.getInstance();
    private static final int PADDING = screenManager.dip2px(10);

    private final CommonJFrame FRAME = new CommonJFrame();
    private OnItemClickListener onItemClickListener = (index, message)->{};
    private FlatListView<FlatLabel> listView = new FlatListView<>();

    public FlatPopUpList(){
        initFrame();
        initContentPane();
        initListView();
        getContentPane().add(listView.getComponent());
    }

    public static void main(String[] args){
        FlatPopUpList popUpList = new FlatPopUpList();
        for (int i = 1; i < 6 ; i++){
            popUpList.addMenu("Item " + i);
        }
        popUpList.setFocusLostDispose();
        popUpList.setOnItemClickListener((position, message) -> {
            System.out.println(message);
        });

        popUpList.show(400,400);
    }

    private void initFrame(){
        FRAME.setType(Window.Type.UTILITY);
        FRAME.setShadowWidth(3);
        FRAME.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void initContentPane(){
        JPanel contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setOpaque(true);
    }

    private void initListView(){
        listView.setTrace(true);
        listView.setSingleSelectionMode(true);
        listView.setOnSelectionListener(((beginIndex, endIndex, selectionList) -> onItemClickListener.onItemClick(beginIndex, selectionList.get(0).getText())));
    }

    private JPanel getContentPane(){
        return FRAME.getCustomizablePanel();
    }

    private FlatLabel createLabel(String text){
        FlatLabel flatLabel = new FlatLabel(text);
        flatLabel.setBorder(BorderFactory.createEmptyBorder(PADDING/2,PADDING,PADDING/2,PADDING));
        return flatLabel;
    }

    public void addMenu(String menuTxt){
        listView.addItem(createLabel(menuTxt));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void show(int x, int y){
        int width = 0;
        int height = 0;
        for (Component comp : listView.getItems()){
            width = Math.max(comp.getPreferredSize().width, width);
            height += comp.getPreferredSize().height;
        }
        listView.getComponent().setPreferredSize(new Dimension(width, height));
        FRAME.pack();
        FRAME.setLocation(x,y);
        FRAME.setVisible(true);

    }

    public void setFocusLostDispose(){
        FRAME.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                dispose();
            }
        });
    }

    public void dispose(){
        FRAME.dispose();
    }
}
