package com.mommoo.flat.list;

import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.list.content.OverlapContentView;
import com.mommoo.flat.text.textarea.FlatTextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.List;

public class FlatList2<T extends Component>{
    private CompIndexList<T> compList = new CompIndexList<>();
    private OverlapContentView contentView = new OverlapContentView();
    private DragEventHandler<T> dragEventHandler = new DragEventHandler<>();

    public FlatList2(){
    }

    public FlatList2(List<T> itemList){
        this();
        compList.addAllComp(itemList);
        itemList.forEach(this::addItem);
    }

    public static void main(String[] args){
        FlatFrame frame = new FlatFrame();
        frame.setTitle("title");
        frame.setSize(500, 500);
        FlatList2<FlatTextArea> list2 = new FlatList2<>();
        list2.addItem(new FlatTextArea("index : 1"));
        list2.addItem(new FlatTextArea("index : 2"));
        list2.addItem(new FlatTextArea("index : 3"));
        list2.addItem(new FlatTextArea("index : 4"));
        list2.addItem(new FlatTextArea("index : 5"));
        list2.addItem(new FlatTextArea("index : 6"));
        list2.addItem(new FlatTextArea("index : 7"));
        list2.addItem(new FlatTextArea("index : 8"));
        list2.addItem(new FlatTextArea("index : 9"));
        list2.addItem(new FlatTextArea("index : 10"));
        list2.addItem(new FlatTextArea("index : 11"));
        list2.addItem(new FlatTextArea("index : 12"));
        list2.addItem(new FlatTextArea("index : 13"));
        list2.addItem(new FlatTextArea("index : 14"));
        list2.addItem(new FlatTextArea("index : 15"));
        list2.addItem(new FlatTextArea("index : 16"));
        list2.addItem(new FlatTextArea("index : 17"));
        list2.addItem(new FlatTextArea("index : 18"));
        list2.addItem(new FlatTextArea("index : 19"));
        list2.addItem(new FlatTextArea("index : 20"));
        list2.addItem(new FlatTextArea("index : 21"));
        list2.addItem(new FlatTextArea("index : 22"));
        list2.addItem(new FlatTextArea("index : 23"));

        list2.addItem(new FlatTextArea("index : 24 LONG LONG LONG LOGN LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG"));
        list2.addItem(new FlatTextArea("index : 25 LONG LONG LONG LOGN LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONGLONG LONG LONG LOGN LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG LONG"));
        frame.getContainer().add(list2.getComponent());
        frame.show();


    }

    public Component getComponent(){
        return contentView;
    }

    public void addItem(T item){
        addMouse(item);
        compList.addComp(item);
        contentView.addComponent(item);
    }

    private void addMouse (T item){
        MouseAdapter adapter = dragEventHandler.getMouseDragEventListener();
        item.addMouseListener(adapter);
        item.addMouseMotionListener(adapter);
        //item.addMouseWheelListener(adapter);
    }

    public T getItem(int index) {
        return compList.peek(index);
    }

    public void setDivider(Color color, int thick){
        if (thick <= 0) thick = 1;
        contentView.setDivider(color, thick);
    }

    public void setPadding(int left, int top, int right, int bottom){
        contentView.setViewportBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }

    public List<T> getItems(){
        return compList.getList();
    }

    public int getItemSize(){
        return compList.getSize();
    }
}
