package com.mommoo.flat.list;

import com.mommoo.example.ExampleFactory;
import com.mommoo.flat.list.listener.OnDragListener;
import com.mommoo.flat.list.listener.OnSelectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlatListView<T extends Component> {
    private final FlatVerticalScrollPane<T> SCROLL_PANE = new FlatVerticalScrollPane<>();
    private final ArrayList<T> compList = new ArrayList<>();
    private final Scroller SCROLLER = new Scroller();

    public FlatListView() { }

    public FlatListView(List<T> itemList) {
        addItems(itemList);
    }

    @SafeVarargs
    public FlatListView(T... items){
        addItems(items);
    }

    private FlatViewPort<T> getViewPortView(){
        return (FlatViewPort<T>)SCROLL_PANE.getViewport().getView();
    }

    public Component getComponent() {
        return SCROLL_PANE;
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

    private void repaint(){
        getViewPortView().revalidate();
        getViewPortView().repaint();
    }

    private void removeItem(int index, boolean repaint){
        getViewPortView().removeComponent(index);
        if(repaint) repaint();
        compList.remove(index);
    }

    public void removeItem(int index) {
        removeItem(index, true);
    }

    public void removeItems(int beginIndex, int endIndex){
        if (beginIndex > endIndex) throw new IllegalArgumentException("beginIndex isn't bigger than endIndex");

        for (int index = endIndex ; index >= beginIndex ; index--){
            removeItem(index, false);
        }

        repaint();
    }

    public void clear(){
        for (int index = getItemSize() - 1 ; index >= 0 ; index--){
            removeItem(index, false);
        }

        repaint();
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

    public Color getBackground(){
        return this.SCROLL_PANE.getViewport().getBackground();
    }

    public void setBackground(Color backgroundColor){
        this.SCROLL_PANE.getViewport().setOpaque(true);
        this.SCROLL_PANE.getViewport().setBackground(backgroundColor);
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
            endIndex = startIndex;
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

    public void addItem(T item) {
        getViewPortView().addComponent(item);
        compList.add(item);

    }

    public Scroller getScroller(){
        return SCROLLER;
    }

    public class Scroller{
        private Scroller(){}

        public void scrollByValue(int value){
            SCROLL_PANE.scrollByValue(value);
        }

        public void scrollByPosition(int position){
            SCROLL_PANE.scrollByPosition(position);
        }

        public void smoothScrollByValue(boolean relative, int value){
            SCROLL_PANE.smoothScrollByValue(relative, value);
        }

        public void smoothScrollByPosition(boolean relative, int position){
            SCROLL_PANE.smoothScrollByPosition(relative, position);
        }

        public int getScrollAnimationDuration(){
            return SCROLL_PANE.getScrollAnimationDuration();
        }

        public void setScrollAnimationDuration(int scrollAnimationDuration){
            SCROLL_PANE.setScrollAnimationDuration(scrollAnimationDuration);
        }

        public Color getScrollBarColor() {
            return SCROLL_PANE.getThemeColor();
        }

        public void setScrollBarColor(Color color) {
            SCROLL_PANE.setThemeColor(color);
        }

        public Color getScrollTrackColor(){
            return SCROLL_PANE.getVerticalScrollTrackColor();
        }

        public void setScrollTrackColor(Color trackColor){
            SCROLL_PANE.setVerticalScrollTrackColor(trackColor);
        }
    }

    public static void main(String[] args){
        ExampleFactory.FlatListViewExample.example3();
    }

}
