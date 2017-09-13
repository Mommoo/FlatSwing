package com.mommoo.flat.list;

import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.flat.list.listener.FlatScrollListener;
import com.mommoo.flat.list.listener.OnDragListener;
import com.mommoo.flat.list.listener.OnSelectionListener;
import com.mommoo.util.ColorManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;

class FlatViewPort<T extends Component> extends FlatPanel implements Scrollable {
    private static final long eventMask = AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.COMPONENT_EVENT_MASK;
    private static final int scrollableUnit = 30;

    private final CompIndexList compIndexList = new CompIndexList();
    private final Rectangle selectionRect = new Rectangle();
    private final int[] selectionFromToIndex = {-1, -1};
    private FlatScrollListener scrollListener = scrollSensitivity -> {};
    private OnSelectionListener<T> onSelectionListener = (beginIndex, endIndex, selectionList) -> {};
    private OnDragListener<T> onDragListener = (beginIndex, endIndex, selectionList) -> {};
    private List<MouseListener> mouseListenerList = new ArrayList<>();
    private List<MouseMotionListener> mouseMotionListenerList = new ArrayList<>();
    private Color selectionColor = ColorManager.getColorAccent();
    private boolean isSingleSelectionMode;
    private boolean isMultiSelectionMode = !isSingleSelectionMode;
    private int dividerThick;


    FlatViewPort(){
        getToolkit().addAWTEventListener(new ViewPortAWTEventListener(), eventMask);
        setLayout(new LinearLayout(Orientation.VERTICAL,0));
    }

    void setScrollListener(FlatScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    @Override
    protected boolean isPaintingOrigin() {
        return true;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics2D = (Graphics2D)g;
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        graphics2D.setColor(selectionColor);
        graphics2D.fill(selectionRect);
    }

    void addComponent(Component component){
        LinearConstraints constraints = new LinearConstraints().setLinearSpace(LinearSpace.MATCH_PARENT);
        add(component, constraints);
        compIndexList.addComp(component);
    }

    void removeComponent(int index){
        remove(index);
        compIndexList.removeComp(index);
    }

    void setDivider(Color color, int thick){
        setOpaque(true);
        setBackground(color);
        ((LinearLayout)getLayout()).setGap(thick);
        this.dividerThick = thick;
    }

    Color getDividerColor(){
        return getBackground();
    }

    int getDividerThick(){
        return this.dividerThick;
    }

    void removeDivider(){
        setOpaque(false);
        ((LinearLayout)getLayout()).setGap(0);
    }

    Color getSelectionColor(){
        return this.selectionColor;
    }

    void setSelectionColor(Color color){
        this.selectionColor = color;
    }

    boolean isSingleSelectionMode(){
        return this.isSingleSelectionMode;
    }

    void setSingleSelectionMode(boolean singleSelectionMode){
        this.isSingleSelectionMode = singleSelectionMode;
        setMultiSelectionMode(!singleSelectionMode);
    }

    boolean isMultiSelectionMode(){
        return this.isMultiSelectionMode;
    }

    void setMultiSelectionMode(boolean multiSelectionMode){
        this.isMultiSelectionMode = multiSelectionMode;
        setSingleSelectionMode(!multiSelectionMode);
    }

    void setOnSelectionListener(OnSelectionListener<T> onSelectionListener){
        this.onSelectionListener = onSelectionListener;
    }

    void setOnDragListener(OnDragListener<T> onDragListener){
        this.onDragListener = onDragListener;
    }

    void select(int beginIndex, int endIndex){
        setSelectionFromToIndex(beginIndex, endIndex);
        paintSelection();
    }

    @Override
    public void addMouseListener(MouseListener mouseListener){
        mouseListenerList.add(mouseListener);
    }

    @Override
    public synchronized void removeMouseListener(MouseListener mouseListener) {
        mouseListenerList.remove(mouseListener);
    }

    @Override
    public void addMouseMotionListener(MouseMotionListener mouseMotionListener){
        mouseMotionListenerList.add(mouseMotionListener);
    }

    @Override
    public synchronized void removeMouseMotionListener(MouseMotionListener mouseMotionListener) {
        mouseMotionListenerList.remove(mouseMotionListener);
    }

    private void setSelectionFromToIndex(int beginIndex, int endIndex){
        selectionFromToIndex[0] = Math.min(beginIndex, endIndex);
        selectionFromToIndex[1] = Math.max(beginIndex, endIndex);
    }

    private void paintSelection(){
        Component beginComp = compIndexList.peek(selectionFromToIndex[0]);
        Component endComp   = compIndexList.peek(selectionFromToIndex[1]);

        int beginY = beginComp.getLocation().y;
        int endY = endComp.getLocation().y + endComp.getPreferredSize().height;

        selectionRect.setBounds(0,beginY, getWidth(), endY - beginY);
        repaint();
    }

    private boolean isSelected(){
        return selectionFromToIndex[0] != -1 && selectionFromToIndex[1] != -1;
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return null;
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return scrollableUnit;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return scrollableUnit;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }


    private enum MouseDirection{
        UP_WARD,
        NONE,
        DOWN_WARD;
    }

    private class ViewPortAWTEventListener implements AWTEventListener{
        private final MouseEventHandler mouseEventHandler = new MouseEventHandler();

        private ViewPortAWTEventListener(){
            mouseEventHandler.setOnDraggedListener((currentIndex, event) -> {
                scrollListener.onDrag(mouseEventHandler.getScrollSensitivity());

                if (isMultiSelectionMode){
                    select(mouseEventHandler.getDragBeginIndex(), currentIndex);
                    onDragListener.onDrag(selectionFromToIndex[0], selectionFromToIndex[1], getFromToList());
                }

            });

            mouseEventHandler.setOnPressedListener((currentIndex, event) -> {});

            mouseEventHandler.setOnReleasedListener((currentIndex, event) -> {
                scrollListener.onDrag(0);
                onSelectionListener.onSelection(selectionFromToIndex[0], selectionFromToIndex[1], getFromToList());
            });

            mouseEventHandler.setOnClickListener((currentIndex, event) -> {
                if (event instanceof MouseEvent && ((MouseEvent)event).getButton() == MouseEvent.BUTTON1){
                    select(currentIndex, currentIndex);
                }
            });
        }

        private java.util.List<T> getFromToList(){
            java.util.List<T> compList = new ArrayList<>();

            for (int index = selectionFromToIndex[0]; index <= selectionFromToIndex[1] ; index++){
                compList.add((T)compIndexList.peek(index));
            }

            return compList;
        }

        @Override
        public void eventDispatched(AWTEvent event) {
            mouseEventHandler.setAWTEvent(event);

            if (event.getID() == ComponentEvent.COMPONENT_RESIZED && isSelected()){
                paintSelection();
            }
        }
    }

    private class MouseEventHandler {
        private static final int NONE = - 1;
        private final BiConsumer<Integer, AWTEvent> defaultListener = (index, event) -> {};
        private DirectionHandler directionHandler = new DirectionHandler();
        private int dragBeginIndex = NONE;
        private int currentIndex   = NONE;
        private int pressedIndex   = NONE;
        private int releasedIndex  = NONE;
        private BiConsumer<Integer, AWTEvent> onDraggedListener  = defaultListener;
        private BiConsumer<Integer, AWTEvent> onPressedListener  = defaultListener;
        private BiConsumer<Integer, AWTEvent> onReleasedListener = defaultListener;
        private BiConsumer<Integer, AWTEvent> onClickListener    = defaultListener;

        private boolean isViewPortMouseEnter;

        private boolean isViewPortEvent(AWTEvent event){
            int eventID = event.getID();

            Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
            Point panelLocation = getParent().getLocationOnScreen();

            if (eventID == MouseEvent.MOUSE_MOVED){

                isViewPortMouseEnter = mouseLocation.x >= panelLocation.x &&
                        mouseLocation.x <= panelLocation.x + getWidth() &&
                        mouseLocation.y >= panelLocation.y &&
                        mouseLocation.y <= panelLocation.y + getHeight();

            }

            return isViewPortMouseEnter;
        }

        private int getScrollSensitivity(){
            int parentComponentY = getParent().getLocationOnScreen().y;
            int parentHeight = getParent().getHeight();
            int absoluteY = MouseInfo.getPointerInfo().getLocation().y;

            if (parentComponentY + parentHeight < absoluteY){
                return absoluteY - (parentComponentY + parentHeight);
            } else if (parentComponentY > absoluteY){
                return absoluteY - parentComponentY;
            }

            return 0;
        }

        private int findCompIndex(){
            int parentPanelLocationY = getParent().getLocationOnScreen().y;
            int mouseY = MouseInfo.getPointerInfo().getLocation().y;

            if (directionHandler.getDirection(mouseY) == MouseDirection.DOWN_WARD){

                for (int index = Math.max(currentIndex,0), size = compIndexList.getSize() ; index < size ; index++){
                    if (isComponentAt(compIndexList.peek(index), mouseY)){
                        return index;
                    }
                }

            } else if (directionHandler.getDirection(mouseY) == MouseDirection.UP_WARD){

                for (int index = Math.max(currentIndex,0); index >= 0 ; index--){
                    if (isComponentAt(compIndexList.peek(index), mouseY)){
                        return index;
                    }
                }

            }

            if (mouseY <= parentPanelLocationY) return 0;

            return compIndexList.getSize() - 1;
        }

        private boolean isComponentAt(Component comp, int currentMouseY){
            Point compLocation = comp.getLocationOnScreen();

            return (compLocation.y <= currentMouseY) && (compLocation.y + comp.getSize().height + dividerThick >= currentMouseY);
        }

        private void setAWTEvent(AWTEvent event){
            if (!isShowing() || !isViewPortEvent(event)) return;

            this.currentIndex = findCompIndex();

            switch(event.getID()){
                case MouseEvent.MOUSE_DRAGGED:

                    if (isDragging()) {
                        onDraggedListener.accept(currentIndex, event);
                        return;
                    }

                    dragBeginIndex = currentIndex;

                    mouseMotionListenerList.forEach(action -> action.mouseDragged((MouseEvent)event));

                    break;

                case MouseEvent.MOUSE_MOVED:

                    mouseMotionListenerList.forEach(action -> action.mouseMoved((MouseEvent)event));

                    break;

                case MouseEvent.MOUSE_CLICKED:

                    mouseListenerList.forEach(action -> action.mouseClicked((MouseEvent)event));

                    break;

                case MouseEvent.MOUSE_PRESSED:

                    pressedIndex = currentIndex;

                    onPressedListener.accept(pressedIndex, event);

                    mouseListenerList.forEach(action -> action.mousePressed((MouseEvent)event));

                    break;

                case MouseEvent.MOUSE_RELEASED:

                    releasedIndex = currentIndex;

                    if (isClicked()){
                        onClickListener.accept(currentIndex, event);
                    }

                    onReleasedListener.accept(releasedIndex, event);

                    reset();

                    mouseListenerList.forEach(action -> action.mouseReleased((MouseEvent)event));

                    break;

                case MouseEvent.MOUSE_ENTERED :

                    mouseListenerList.forEach(action -> action.mouseEntered((MouseEvent)event));

                    break;

                case MouseEvent.MOUSE_EXITED :

                    mouseListenerList.forEach(action -> action.mouseExited((MouseEvent)event));

                    break;
                default:
                    break;
            }

        }

        private void setOnDraggedListener(BiConsumer<Integer, AWTEvent> onDraggedListener){
            this.onDraggedListener = onDraggedListener;
        }

        private void setOnPressedListener(BiConsumer<Integer, AWTEvent> onPressedListener){
            this.onPressedListener = onPressedListener;
        }

        private void setOnReleasedListener(BiConsumer<Integer, AWTEvent> onReleasedListener){
            this.onReleasedListener = onReleasedListener;
        }

        private void setOnClickListener(BiConsumer<Integer, AWTEvent> onClickListener){
            this.onClickListener = onClickListener;
        }

        private int getDragBeginIndex(){
            return dragBeginIndex;
        }

        private int getCurrentIndex(){
            return currentIndex;
        }

        private boolean isDragging(){
            return dragBeginIndex != NONE;
        }

        private boolean isClicked(){
            return pressedIndex == releasedIndex;
        }

        private void reset(){
            dragBeginIndex = currentIndex = pressedIndex = releasedIndex = NONE;
            isViewPortMouseEnter = false;
        }
    }

    private class DirectionHandler {
        private int previousMouseY = 0;
        private MouseDirection direction = MouseDirection.NONE;

        private MouseDirection getDirection(int currentMouseY){

            if (currentMouseY - previousMouseY > 0){
                direction = MouseDirection.DOWN_WARD;
            }

            else if (currentMouseY - previousMouseY < 0){
                direction = MouseDirection.UP_WARD;
            }

            previousMouseY = currentMouseY;

            return direction;
        }
    }
}

