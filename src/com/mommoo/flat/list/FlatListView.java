package com.mommoo.flat.list;

import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.list.listener.OnDragListener;
import com.mommoo.flat.list.listener.OnSelectionListener;
import com.mommoo.flat.text.label.FlatLabel;
import com.mommoo.flat.text.textarea.FlatTextArea;
import com.mommoo.util.FontManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlatListView<T extends Component> {
    private FlatVerticalScrollPane<T> SCROLL_PANE = new FlatVerticalScrollPane<>();
    private ArrayList<T> compList = new ArrayList<>();

    public FlatListView() { }

    public FlatListView(List<T> itemList) {
        addItems(itemList);
    }

    public FlatListView(T... items){
        addItems(items);
    }

    private FlatViewPort<T> getViewPortView(){
        return (FlatViewPort<T>)SCROLL_PANE.getViewport().getView();
    }

    public Component getComponent() {
        return SCROLL_PANE;
    }

    public void addItem(T item) {
        getViewPortView().addComponent(item);
        compList.add(item);
    }

    public void addItems(T... items) {
        for (T t : items) {
            addItem(t);
        }
    }

    public void addItem(T item, int index){
        compList.add(index, item);
        getViewPortView().addComponent(item, index);
    }

    public void addItems(List<T> items){
        items.forEach(this::addItem);
    }

    public T getItem(int index) {
        return compList.get(index);
    }

    public void removeItem(int index) {
        getViewPortView().removeComponent(index);
        compList.remove(index);
    }

    public void removeItems(int beginIndex, int endIndex){
        if (beginIndex > endIndex) throw new IllegalArgumentException("beginIndex isn't bigger than endIndex");

        for (int i = endIndex ; i >= beginIndex ; i--){
            compList.remove(i);
        }
    }

    public void setDivider(Color color, int thick) {
        if (thick < 1) thick = 1;
        getViewPortView().setDivider(color, thick);
    }

    public void removeDivider(){
        getViewPortView().removeDivider();
    }

    public Color getDividerColor() {
        return getViewPortView().getDividerColor();
    }

    public int getDividerThick() {
        return getViewPortView().getDividerThick();
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

        getViewPortView().select(startIndex, endIndex);
    }

    public void deSelect(){
        getViewPortView().deSelect();
    }

    public boolean isMultiSelectionMode(){
        return getViewPortView().isMultiSelectionMode();
    }

    public void setMultiSelectionMode(boolean multiSelectionMode) {
        getViewPortView().setMultiSelectionMode(multiSelectionMode);
    }

    public boolean isSingleSelectionMode(){
        return getViewPortView().isSingleSelectionMode();
    }

    public void setSingleSelectionMode(boolean singleSelectionMode){
        getViewPortView().setSingleSelectionMode(singleSelectionMode);
    }

    public void setOnSelectionListener(OnSelectionListener<T> onSelectionListener){
        getViewPortView().setOnSelectionListener(onSelectionListener);
    }

    public List<T> getSelectedList(){
        return getViewPortView().getSelectedList();
    }

    public int[] getSelectionFromToIndex(){
        return getViewPortView().getSelectionFromToIndex();
    }

    public boolean isSelected(){
        return getViewPortView().isSelected();
    }

    public void setOnDragListener(OnDragListener<T> onDragListener){
        getViewPortView().setOnDragListener(onDragListener);
    }

    public Color getSelectionColor(){
        return getViewPortView().getSelectionColor();
    }

    public void setSelectionColor(Color color){
        getViewPortView().setSelectionColor(color);
    }

    public void addMouseListener(MouseListener mouseListener){
        getViewPortView().addMouseListener(mouseListener);
    }

    public void removeMouseListener(MouseListener mouseListener){
        getViewPortView().removeMouseListener(mouseListener);
    }

    public void addMouseMotionListener(MouseMotionListener mouseMotionListener){
        getViewPortView().addMouseMotionListener(mouseMotionListener);
    }

    public void removeMouseMotionListener(MouseMotionListener mouseMotionListener){
        getViewPortView().removeMouseMotionListener(mouseMotionListener);
    }

    public void setTrace(boolean trace){
        getViewPortView().setTrace(trace);
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
//            System.out.println("drag index from : " + beginIndex +", to : " + endIndex);
        });

        frame.getContainer().add(list2.getComponent());
        list2.setDivider(Color.BLACK, 1);

        frame.setLocationOnScreenCenter();
        frame.setResizable(true);
        frame.show();
    }

}
