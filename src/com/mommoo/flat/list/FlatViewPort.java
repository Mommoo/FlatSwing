package com.mommoo.flat.list;

import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.flat.list.listener.FlatScrollListener;
import com.mommoo.flat.list.listener.OnDragListener;
import com.mommoo.flat.list.listener.OnSelectionListener;
import com.mommoo.flat.list.model.SelectionModel;
import com.mommoo.util.ColorManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

class FlatViewPort<T extends Component> extends FlatPanel implements Scrollable , SelectionModel {
    private static final long eventMask = AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK | AWTEvent.COMPONENT_EVENT_MASK;
    private static final int scrollableUnit = 30;

    private final CompIndexList<T> compIndexList = new CompIndexList<>();
    private final SelectionRange SELECTION_RANGE = new SelectionRange(this);

    private final Rectangle selectionRect = new Rectangle();

    private FlatScrollListener scrollListener = scrollSensitivity -> {};
    private OnSelectionListener<T> onSelectionListener = (beginIndex, endIndex, selectionList) -> {};
    private OnDragListener<T> onDragListener = (beginIndex, endIndex, selectionList) -> {};

    private List<MouseListener> mouseListenerList = new ArrayList<>();
    private List<MouseMotionListener> mouseMotionListenerList = new ArrayList<>();
    private Color selectionColor = ColorManager.getColorAccent();

    private boolean isSingleSelectionMode;
    private boolean isMultiSelectionMode = !isSingleSelectionMode;
    private boolean trace;

    private int dividerThick;
    private Color dividerColor;

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

    private void fillRectForDivider(Graphics g){
        int beginY = getComponent(0).getY();
        Component component = getComponent(getItemSize() - 1);
        int height = component.getY() + component.getHeight() + dividerThick - beginY ;
        g.setColor(dividerColor);
        g.fillRect(0,beginY, getWidth(), height);
    }

    @Override
    public void paint(Graphics g) {
        fillRectForDivider(g);
        super.paint(g);

        Graphics2D graphics2D = (Graphics2D)g;
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        graphics2D.setColor(selectionColor);
        graphics2D.fill(selectionRect);
    }

    void addComponent(T component){
        SwingUtilities.invokeLater(()->{
            add(component, new LinearConstraints().setLinearSpace(LinearSpace.MATCH_PARENT));
            compIndexList.addComp(component);
            doLayout();
            revalidate();
            repaint();
        });
    }

    void addComponent(T component, int index){
        SwingUtilities.invokeLater(()->{
            add(component, new LinearConstraints().setLinearSpace(LinearSpace.MATCH_PARENT), index);
            compIndexList.addComp(component, index);
            doLayout();
            revalidate();
            repaint();
        });

    }

    void clear(){
        SELECTION_RANGE.inspectValidSelection(0);
        removeAll();
        compIndexList.removeAll();
        paintSelection();
    }

    void removeComponent(int index){
        SELECTION_RANGE.inspectValidSelection(index);
        remove(index);
        compIndexList.removeComp(index);
        paintSelection();
    }

    void setDivider(Color color, int thick){
        this.dividerColor = color;
        this.dividerThick = thick;
        ((LinearLayout)getLayout()).setGap(thick);

    }

    Color getDividerColor(){
        return this.dividerColor;
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

    @Override
    public int getItemSize() {
        return compIndexList.getSize();
    }

    @Override
    public boolean isSingleSelectionMode(){
        return this.isSingleSelectionMode;
    }

    void setSingleSelectionMode(boolean singleSelectionMode){
        this.isSingleSelectionMode = singleSelectionMode;
        this.isMultiSelectionMode = !singleSelectionMode;
    }

    boolean isMultiSelectionMode(){
        return this.isMultiSelectionMode;
    }

    void setMultiSelectionMode(boolean multiSelectionMode){
        this.isMultiSelectionMode = multiSelectionMode;
        this.isSingleSelectionMode = !multiSelectionMode;
    }

    void setOnSelectionListener(OnSelectionListener<T> onSelectionListener){
        this.onSelectionListener = onSelectionListener;
    }

    void setOnDragListener(OnDragListener<T> onDragListener){
        this.onDragListener = onDragListener;
    }

    void select(int beginIndex, int endIndex){
        SELECTION_RANGE.saveRange(beginIndex, endIndex);
        paintSelection();
    }

    void deSelect(){
        SELECTION_RANGE.setInValidRange();
        paintSelection();
    }

    void setTrace(boolean trace){
        this.trace = trace;
    }

    List<T> getSelectedList(){

        return compIndexList
                .getList()
                .subList(SELECTION_RANGE.getBeginIndex(), SELECTION_RANGE.getEndIndex() + 1);
    }

    int[] getSelectionFromToIndex(){
        return SELECTION_RANGE.getRange();
    }

    boolean isSelected(){
        return SELECTION_RANGE.isValid();
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

    private void paintSelection(){
        if (!SELECTION_RANGE.isValid()){
            removeRect();
            return;
        }

        paintRect();
    }

    private void removeRect(){
        selectionRect.setBounds(0,0,0,0);
        repaint();
    }

    private void paintRect(){
        Component beginComp = compIndexList.peek(SELECTION_RANGE.getBeginIndex());
        Component endComp   = compIndexList.peek(SELECTION_RANGE.getEndIndex());

        int beginY = beginComp.getLocation().y;
        int endY = endComp.getLocation().y + endComp.getPreferredSize().height;

        selectionRect.setBounds(0,beginY, getWidth(), endY - beginY);
        repaint();
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


    private enum BinaryDirection {
        TARGET,
        UP_WARD,
        DOWN_WARD,
        NONE
    }

    private class ViewPortAWTEventListener implements AWTEventListener{
        private final MouseEventHandler mouseEventHandler = new MouseEventHandler();
        private boolean valid;
        private boolean isViewPortMouseEntered;
        private boolean lock;

        private ViewPortAWTEventListener(){
            mouseEventHandler.setOnWheelListener((currentIndex, event)->{
                if(mouseEventHandler.isDragging()){
                    select(mouseEventHandler.getDragBeginIndex(), currentIndex);
                }
            });

            mouseEventHandler.setOnDraggedListener((currentIndex, event) -> {
                scrollListener.onDrag(mouseEventHandler.getScrollSensitivity());

                if (isMultiSelectionMode){
                    select(mouseEventHandler.getDragBeginIndex(), currentIndex);
                    onDragListener.onDrag(SELECTION_RANGE.getBeginIndex(), SELECTION_RANGE.getEndIndex(), getSelectedList());
                }

            });

            mouseEventHandler.setOnMovedListener((currentIndex, event) ->{
                if (trace){
                    select(currentIndex, currentIndex);
                }
            });

            mouseEventHandler.setOnPressedListener((currentIndex, event)->{
                if (event.getButton() == MouseEvent.BUTTON1){
                    select(currentIndex, currentIndex);
                }
            });

            mouseEventHandler.setOnReleasedListener((currentIndex, event) -> {
                scrollListener.onDrag(0);

                if (isSelected()){
                    onSelectionListener.onSelection(SELECTION_RANGE.getBeginIndex(), SELECTION_RANGE.getEndIndex(), getSelectedList());
                }
            });

            mouseEventHandler.setOnClickListener((currentIndex, event) -> { });
        }

        private boolean isMouseLocationInViewPort(){
            Container parent = getParent();
            Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
            Point panelLocation = parent.getLocationOnScreen();

            return mouseLocation.x >= panelLocation.x &&
                    mouseLocation.x <= panelLocation.x + getWidth() &&
                    mouseLocation.y >= panelLocation.y &&
                    mouseLocation.y <= panelLocation.y + parent.getHeight();
        }

        private boolean isConfirmOwnPanelEvent(Component component){
            return SwingUtilities.getWindowAncestor(FlatViewPort.this) == SwingUtilities.getWindowAncestor(component);
        }

        @Override
        public void eventDispatched(AWTEvent event) {
            if (!isShowing() || !isConfirmOwnPanelEvent((Component)event.getSource())) return;

            if (event.getID() == ComponentEvent.COMPONENT_RESIZED && isSelected()){
                paintSelection();
            }

            if (!(event instanceof MouseEvent)) return;

            MouseEvent mouseEvent = null;

            boolean isMouseLocationInViewPort = isMouseLocationInViewPort();

            if (event.getID() == MouseEvent.MOUSE_MOVED){
                if (isViewPortMouseEntered && !isMouseLocationInViewPort){
                    isViewPortMouseEntered = false;
                    valid = true;
                    mouseEvent = MouseEventFactory.createMouseEvent(FlatViewPort.this, MouseEvent.MOUSE_EXITED, event);
                }

                else if (!isViewPortMouseEntered && isMouseLocationInViewPort){
                    isViewPortMouseEntered = true;
                    valid = true;
                    mouseEvent = MouseEventFactory.createMouseEvent(FlatViewPort.this, MouseEvent.MOUSE_ENTERED, event);
                }
            }

            else if (event.getID() == MouseEvent.MOUSE_PRESSED){
                lock = true;
                valid = isMouseLocationInViewPort;
            }

            else if (event.getID() == MouseEvent.MOUSE_RELEASED){

                lock = false;

            }

            else {

                if (!lock) valid = isMouseLocationInViewPort;

            }

            if (mouseEvent == null ){
                mouseEvent = MouseEventFactory.createMouseEvent(FlatViewPort.this, event.getID(), event);
            }

            if (!valid) return;

            mouseEventHandler.setMouseEvent(mouseEvent);
        }
    }

    private class MouseEventHandler {
        private static final int NONE = - 1;
        private final BiConsumer<Integer, MouseEvent> defaultListener = (index, event) -> {};

        private int dragBeginIndex = NONE;
        private int currentIndex   = NONE;
        private int pressedIndex   = NONE;
        private int releasedIndex  = NONE;
        private BiConsumer<Integer, MouseEvent> onWheelListener    = defaultListener;
        private BiConsumer<Integer, MouseEvent> onDraggedListener  = defaultListener;
        private BiConsumer<Integer, MouseEvent> onMovedListener    = defaultListener;
        private BiConsumer<Integer, MouseEvent> onEnteredListener  = defaultListener;
        private BiConsumer<Integer, MouseEvent> onPressedListener  = defaultListener;
        private BiConsumer<Integer, MouseEvent> onReleasedListener = defaultListener;
        private BiConsumer<Integer, MouseEvent> onClickListener    = defaultListener;

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

            int beginIndex = 0;
            int endIndex = compIndexList.getSize() - 1;
            int targetIndex = (beginIndex + endIndex) /2;

            while(beginIndex <= endIndex){
                BinaryDirection direction = findDirection(targetIndex);

                switch(direction){
                    case NONE : return -1;

                    case TARGET: return targetIndex;

                    case UP_WARD: endIndex = targetIndex -1;
                        break;

                    case DOWN_WARD: beginIndex = targetIndex + 1;
                        break;
                }

                targetIndex = (beginIndex + endIndex) /2;
            }

            if (mouseY <= parentPanelLocationY) return 0;

            return compIndexList.getSize() - 1;
        }

        private BinaryDirection findDirection(int index){
            int currentMouseY = MouseInfo.getPointerInfo().getLocation().y;

            Component comp = compIndexList.peek(index);
            Point compLocation = comp.getLocationOnScreen();

            int compBottomY = compLocation.y + comp.getSize().height;

            if ((compLocation.y <= currentMouseY) && (compBottomY >= currentMouseY)){
                return BinaryDirection.TARGET;
            }

            else if ((compBottomY < currentMouseY) && (compBottomY + dividerThick >= currentMouseY)) {
                return BinaryDirection.NONE;
            }

            else if (compBottomY + dividerThick < currentMouseY){
                return BinaryDirection.DOWN_WARD;
            }

            else {
                return BinaryDirection.UP_WARD;
            }
        }

        private void setMouseEvent(MouseEvent mouseEvent){
            this.currentIndex = findCompIndex();

            if (this.currentIndex == -1) return;

            switch(mouseEvent.getID()){
                case MouseEvent.MOUSE_WHEEL :

                    onWheelListener.accept(currentIndex, mouseEvent);

                    break;

                case MouseEvent.MOUSE_DRAGGED:

                    if (isDragging()) {
                        onDraggedListener.accept(currentIndex, mouseEvent);
                        return;
                    }

                    dragBeginIndex = pressedIndex;

                    mouseMotionListenerList.forEach(action -> action.mouseDragged(mouseEvent));

                    break;

                case MouseEvent.MOUSE_MOVED:

                    onMovedListener.accept(currentIndex, mouseEvent);

                    mouseMotionListenerList.forEach(action -> action.mouseMoved(mouseEvent));

                    break;

                case MouseEvent.MOUSE_CLICKED:

                    mouseListenerList.forEach(action -> action.mouseClicked(mouseEvent));

                    break;

                case MouseEvent.MOUSE_PRESSED:

                    pressedIndex = currentIndex;

                    onPressedListener.accept(pressedIndex, mouseEvent);

                    mouseListenerList.forEach(action -> action.mousePressed(mouseEvent));

                    break;

                case MouseEvent.MOUSE_RELEASED:

                    releasedIndex = currentIndex;

                    if (isClicked()){
                        onClickListener.accept(currentIndex, mouseEvent);
                    }

                    onReleasedListener.accept(releasedIndex, mouseEvent);

                    reset();

                    mouseListenerList.forEach(action -> action.mouseReleased(mouseEvent));

                    break;

                case MouseEvent.MOUSE_ENTERED :

                    onEnteredListener.accept(currentIndex, mouseEvent);

                    mouseListenerList.forEach(action -> action.mouseEntered(mouseEvent));

                    break;

                case MouseEvent.MOUSE_EXITED :

                    mouseListenerList.forEach(action -> action.mouseExited(mouseEvent));

                    break;

                default:
                    break;
            }

        }

        private void setOnWheelListener(BiConsumer<Integer, MouseEvent> onWheelListener){
            this.onWheelListener = onWheelListener;
        }

        private void setOnDraggedListener(BiConsumer<Integer, MouseEvent> onDraggedListener){
            this.onDraggedListener = onDraggedListener;
        }

        private void setOnMovedListener(BiConsumer<Integer, MouseEvent> onMovedListener){
            this.onMovedListener = onMovedListener;
        }

        private void setOnEnteredListener(BiConsumer<Integer, MouseEvent> onEnteredListener){
            this.onEnteredListener = onEnteredListener;
        }

        private void setOnPressedListener(BiConsumer<Integer, MouseEvent> onPressedListener){
            this.onPressedListener = onPressedListener;
        }

        private void setOnReleasedListener(BiConsumer<Integer, MouseEvent> onReleasedListener){
            this.onReleasedListener = onReleasedListener;
        }

        private void setOnClickListener(BiConsumer<Integer, MouseEvent> onClickListener){
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
        }
    }
}

