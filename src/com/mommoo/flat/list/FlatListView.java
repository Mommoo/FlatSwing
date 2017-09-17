package com.mommoo.flat.list;

import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.list.listener.OnDragListener;
import com.mommoo.flat.list.listener.OnSelectionListener;
import com.mommoo.flat.text.textarea.FlatTextArea;
import com.mommoo.util.FontManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlatListView<T extends Component> {
    private FlatVerticalScrollPane<T> SCROLL_PANE = new FlatVerticalScrollPane<>();
    private List<T> compList = new ArrayList<>();

    public FlatListView() { }

    public FlatListView(List<T> itemList) {
        this();
        addItems(itemList);
    }

    public FlatListView(T... items){
        this();
        addItems(items);
    }


    public Component getComponent() {
        return SCROLL_PANE;
    }

    public void addItem(T item) {
        SCROLL_PANE.getViewPort().addComponent(item);
        compList.add(item);
    }

    public void addItems(T... items) {
        for (T t : items) {
            addItem(t);
        }
    }

    public void addItems(List<T> items){
        items.forEach(this::addItem);
    }

    public T getItem(int index) {
        return compList.get(index);
    }

    public void removeItem(int index) {
        SCROLL_PANE.getViewPort().removeComponent(index);
        compList.remove(index);
    }

    public void setDivider(Color color, int thick) {
        if (thick < 1) thick = 1;
        SCROLL_PANE.getViewPort().setDivider(color, thick);
    }

    public void removeDivider(){
        SCROLL_PANE.getViewPort().removeDivider();
    }

    public Color getDividerColor() {
        return SCROLL_PANE.getViewPort().getDividerColor();
    }

    public int getDividerThick() {
        return SCROLL_PANE.getViewPort().getDividerThick();
    }

    public void setPadding(int left, int top, int right, int bottom) {
        SCROLL_PANE.setViewportBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }

    public List<T> getItems() {
        return Collections.unmodifiableList(compList);
    }

    public Color getScrollBarColor() {
        return SCROLL_PANE.getThemeColor();
    }

    public void setScrollBarColor(Color color) {
        SCROLL_PANE.setThemeColor(color);
    }

    public int getItemSize() {
        return compList.size();
    }

    public void select(int beginIndex, int endIndex) {
        if (beginIndex < 0 && endIndex < 0){
            throw new IndexOutOfBoundsException("beginIndex or endIndex have to not smaller than zero");
        }

        int startIndex = Math.min(beginIndex, endIndex);
        endIndex = Math.max(beginIndex, endIndex);

        if (isSingleSelectionMode()) {
            endIndex = startIndex+1;
        }

        SCROLL_PANE.getViewPort().select(startIndex, endIndex);
    }

    public static void main(String[] args){
        FlatFrame frame = new FlatFrame();
        frame.setTitle("Beautiful FlatList");
        frame.setSize(500, 500);

        FlatListView<FlatTextArea> list2 = new FlatListView<>();
        Font font = FontManager.getNanumGothicFont(Font.PLAIN, 10);
        for (int i = 0 ; i < 100 ; i ++){
            FlatTextArea area = new FlatTextArea("index : " + i);
            area.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            area.setFont(font);
            list2.addItem(area);
        }

        list2.setOnSelectionListener((beginIndex, endIndex, selectionList) -> {
            System.out.println("selected index from : " + beginIndex +", to : " + endIndex);
        });

        list2.setOnDragListener((beginIndex, endIndex, selectionList) -> {
            System.out.println("drag index from : " + beginIndex +", to : " + endIndex);
        });

        frame.getContainer().add(list2.getComponent());
        list2.setDivider(Color.BLACK, 1);

        frame.setLocationOnScreenCenter();
        frame.setResizable(true);
        frame.show();
    }

    public boolean isMultiSelectionMode(){
        return SCROLL_PANE.getViewPort().isMultiSelectionMode();
    }

    public void setMultiSelectionMode(boolean multiSelectionMode) {
        SCROLL_PANE.getViewPort().setMultiSelectionMode(multiSelectionMode);
    }

    public boolean isSingleSelectionMode(){
        return SCROLL_PANE.getViewPort().isSingleSelectionMode();
    }

    public void setSingleSelectionMode(boolean singleSelectionMode){
        SCROLL_PANE.getViewPort().setSingleSelectionMode(singleSelectionMode);
    }

    public void setOnSelectionListener(OnSelectionListener<T> onSelectionListener){
        SCROLL_PANE.getViewPort().setOnSelectionListener(onSelectionListener);
    }

    public void setOnDragListener(OnDragListener<T> onDragListener){
        SCROLL_PANE.getViewPort().setOnDragListener(onDragListener);
    }

    public Color getSelectionColor(){
        return SCROLL_PANE.getViewPort().getSelectionColor();
    }

    public void setSelectionColor(Color color){
        SCROLL_PANE.getViewPort().setSelectionColor(color);
    }

    public void addMouseListener(MouseListener mouseListener){
        SCROLL_PANE.getViewPort().addMouseListener(mouseListener);
    }

    public void removeMouseListener(MouseListener mouseListener){
        SCROLL_PANE.getViewPort().removeMouseListener(mouseListener);
    }

    public void addMouseMotionListener(MouseMotionListener mouseMotionListener){
        SCROLL_PANE.getViewPort().addMouseMotionListener(mouseMotionListener);
    }

    public void removeMouseMotionListener(MouseMotionListener mouseMotionListener){
        SCROLL_PANE.getViewPort().removeMouseMotionListener(mouseMotionListener);
    }

    public void setTrace(boolean trace){
        SCROLL_PANE.getViewPort().setTrace(trace);
    }

}
