package flat.list;

import flat.component.FlatScrollPane;
import flat.frame.FlatFrame;
import flat.label.FlatLabel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mommoo on 2017-03-12.
 */
public class FlatList<T extends JComponent> extends JComponent implements MouseMotionListener,MouseListener{
    private static final Color DEFAULT_DIVIDER_COLOR = Color.BLACK;
    private static final int BORDER_WIDTH = 1;
    private static final Border DEFAULT_DIVIDER = BorderFactory.createMatteBorder(0,0,BORDER_WIDTH,0,DEFAULT_DIVIDER_COLOR);
    private final DynamicScrollPane SCROLL_PANE = new DynamicScrollPane();
    private Timer autoScrollTimer;
    private final JPanel INNER_PANEL = SCROLL_PANE.INNER_PANEL;
    private final JScrollBar VERTICAL_SCROLL_BAR = SCROLL_PANE.getVerticalScrollBar();
    private final ArrayList<T> ITEM_LIST = new ArrayList<>();
    private Color themeColor = Color.GRAY;
    private boolean isDivider,isFollowTrack;
    private Border divider = DEFAULT_DIVIDER;
    private Color dividerColor = DEFAULT_DIVIDER_COLOR;
    private Color selectedColor = Color.LIGHT_GRAY;
    private Color selectedTextColor = Color.BLACK;
    private OnItemPositionClickEvent onItemPositionClickEvent;
    private GridBagConstraints GBC = new GridBagConstraints();

    public interface OnItemPositionClickEvent<T>{
        public void onItemClick(int position, T object);
    }

    public interface OnItemSelectedEvent<T>{
        public void onSelect(int startPosition, int endPosition, ArrayList<T> list);
    }

    private OnItemSelectedEvent<T> onItemSelectedEvent;

    public FlatList() {
        init();
        initInnerPanel();
        setThemeColor(themeColor);
    }

    public FlatList(List<T> list){
        this();
        for(T t : list) addComponent(t,-1);
    }

    private void init(){
        setLayout(new BorderLayout());
        SCROLL_PANE.setHorizontalScrollBarPolicy(FlatScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(SCROLL_PANE,BorderLayout.CENTER);
        SCROLL_PANE.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.1f)));
    }

    private void initInnerPanel(){
        INNER_PANEL.setLayout(new GridBagLayout());
    }

    private void addComponent(T t, int position){
        JPanel wrapPanel = new JPanel(new BorderLayout());

        if(isDivider) wrapPanel.setBorder(divider);
        t.addMouseListener(this);
        t.addMouseMotionListener(this);

        wrapPanel.add(t,BorderLayout.CENTER);

        /* save child background color & foreground color */
        wrapPanel.setBackground(t.getBackground());
        wrapPanel.setForeground(t.getForeground());

        ITEM_LIST.add(t);

        GBC.gridwidth = 1;
        GBC.gridheight = 1;
        GBC.gridx = 0;
        GBC.gridy = position == -1 ? getItemSize() - 1 : position;
        GBC.weightx = 1.0;
        GBC.fill = GridBagConstraints.BOTH;
        INNER_PANEL.add(wrapPanel,GBC);
    }

    private void drawAllItemDivider(){
        if(isDivider) for(JComponent item : ITEM_LIST) item.setBorder(divider);
        else for(JComponent item : ITEM_LIST) item.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
    }

    private void computeAutoScroll(){
        if (autoScrollTimer == null) autoScrollTimer = new Timer(100, (e)->{
            JScrollBar scrollBar = SCROLL_PANE.getVerticalScrollBar();
            scrollBar.setUnitIncrement(1);
            scrollBar.setValue(scrollBar.getMaximum());
            autoScrollTimer.stop();
        });
        autoScrollTimer.start();
    }

    private void selectComponent(int componentPosition){
        Component targetComponent = ITEM_LIST.get(componentPosition);
        targetComponent.setBackground(selectedColor);
        targetComponent.setForeground(selectedTextColor);
    }

    private void deSelectComponent(int componentPosition){
        Component componentStoredInformation = INNER_PANEL.getComponent(componentPosition);
        Component targetComponent = ITEM_LIST.get(componentPosition);
        targetComponent.setBackground(componentStoredInformation.getBackground());
        targetComponent.setForeground(componentStoredInformation.getForeground());
    }

    public void setThemeColor(Color color){
        this.themeColor = color;
        SCROLL_PANE.setThemeColor(color);
        INNER_PANEL.setBackground(Color.WHITE);
    }

    public void addItem(T t){
        addItem(t,-1);
    }

    public void addItem(T t,int position){
        addComponent(t,position);
        if(isFollowTrack) computeAutoScroll();
        SCROLL_PANE.reDrawScroll();
    }

    public T getItem(int index){
        return ITEM_LIST.get(index);
    }

    public void removeItem(T t){
        ITEM_LIST.remove(t);
        for(Component comp : INNER_PANEL.getComponents()){
            if(((JComponent)comp).getComponent(0).equals(t)){
                INNER_PANEL.remove(comp);
                break;
            }
        }
        SCROLL_PANE.reDrawScroll();
    }

    public void removeItemAtIndex(int index){
        ITEM_LIST.remove(index);
        remove(index);
    }

    public void clear(){
        for(Component comp : INNER_PANEL.getComponents()){
            INNER_PANEL.remove(comp);
            ITEM_LIST.remove(((JComponent)comp).getComponent(0));
        }
        SCROLL_PANE.reDrawScroll();
    }

    public int getItemSize(){
        return ITEM_LIST.size();
    }

    public void setDivider(boolean drawDivider){
        isDivider = drawDivider;
        drawAllItemDivider();
    }

    public boolean isDivider(){
        return isDivider;
    }

    public void setDividerColor(Color color){
        divider = BorderFactory.createMatteBorder(0,0,1,0,color);
        drawAllItemDivider();
    }

    public Color getDividerColor(){
        return dividerColor;
    }

    public void setSelectedColor(Color selectedColor){
        this.selectedColor = selectedColor;
    }

    public Color getSelectedColor(){
        return this.selectedColor;
    }

    public void setSelectedTextColor(Color selectedTextColor){
        this.selectedTextColor = selectedTextColor;
    }

    public Color getSelectedTextColor(){
        return this.selectedTextColor;
    }

    public void setOnItemPositionClickEvent(OnItemPositionClickEvent<T> onItemPositionClickEvent){
        this.onItemPositionClickEvent = onItemPositionClickEvent;
    }

    public void setOnItemSelectedEvent(OnItemSelectedEvent<T> onItemSelectedEvent){
        this.onItemSelectedEvent = onItemSelectedEvent;
    }

    public void setScrollFollowTrack(boolean followTrack){
        isFollowTrack = followTrack;
        if(!isFollowTrack) return;
        JScrollBar scrollBar = SCROLL_PANE.getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getMaximum());
    }

    public void select(int startPosition, int endPosition){
        for(int position = startPosition ; position <= endPosition ; position ++) selectComponent(position);
    }

    public void selectAll(){
        select(0,ITEM_LIST.size()-1);
    }

    public void deSelect(int startPosition, int endPosition){
        for(int position = startPosition ; position <= endPosition ; position ++) deSelectComponent(position);
    }

    public void deSelectAll(){
        deSelect(0,ITEM_LIST.size()-1);
    }

    private class DynamicScrollPane extends FlatScrollPane{
        private final DynamicPanel INNER_PANEL = new DynamicPanel();
        private int viewportWidth = 0;
        private boolean reDraw;

        private DynamicScrollPane(){
            setViewportView(INNER_PANEL);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            viewportWidth = getViewport().getWidth();
            INNER_PANEL.revalidate();
            INNER_PANEL.repaint();
        }

        private void reDrawScroll(){
            reDraw = true;
            repaint();
        }

        private class DynamicPanel extends JPanel{
            @Override
            public void paint(Graphics g) {

                int allComponentsHeight = 0;
                int index = 0 ;
                for(Component comp : getComponents()) {
                    JComponent childComp = ITEM_LIST.get(index++);
                    Dimension parentDimension = comp.getPreferredSize();
                    comp.setSize(parentDimension);
                    childComp.setSize(childComp.getPreferredSize());
                    comp.setLocation(0,allComponentsHeight);
                    allComponentsHeight += parentDimension.height;
                }
                setPreferredSize(new Dimension(viewportWidth,allComponentsHeight));

                /* Scroll view repaint only once */
                if(reDraw){
                    DynamicScrollPane.this.revalidate();
                    DynamicScrollPane.this.repaint();
                    reDraw = false;
                }
                super.paint(g);
            }
        }
    }

    private int enterPosition;
    private int scrollValue;
    private int rootPanelLocationOnScreenY = -1;
    private boolean isAutoScrolling;
    private final UserComponentHandler userComponentHandler = new UserComponentHandler();
    private boolean isLeftMouseButtonPress;

    private void handleNotRecognizedComponent(int componentPosition){

        boolean isStackPointAboveBaseLine = userComponentHandler.isStackPointerAboveBaseLine(componentPosition);
        int userMouseDirection = userComponentHandler.getUserMouseDirectionByPosition(componentPosition);
        int componentStackPointer = userComponentHandler.getComponentStackPointer();

        //System.out.println("마우스 방향 ["+userMouseDirection+"] isStackPointAboveBaseLine : " + isStackPointAboveBaseLine+" , stackCount : " + componentStackPointer + " , position : " + componentPosition);
        if (userMouseDirection == UserComponentHandler.MOUSE_DIRECTION_DOWN){
            if(isStackPointAboveBaseLine){
                for(int position = componentStackPointer ; position < componentPosition ; position++) deSelectComponent(position);
            }else{
                for(int position = componentStackPointer ; position <= componentPosition ; position++) selectComponent(position);
            }
        }

        if (userMouseDirection == UserComponentHandler.MOUSE_DIRECTION_UP){
            if(isStackPointAboveBaseLine){
                for(int position = componentStackPointer ; position >= componentPosition ; position--) selectComponent(position);
            }else{
                for(int position = componentStackPointer ; position > componentPosition ; position--) deSelectComponent(position);
            }
        }
        userComponentHandler.setComponentStackPointer(componentPosition);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        isLeftMouseButtonPress = e.getButton() == MouseEvent.BUTTON1;
        if(!isLeftMouseButtonPress) return;
        enterPosition = ITEM_LIST.indexOf(e.getSource());
        deSelectAll();
        if(onItemPositionClickEvent != null || onItemSelectedEvent != null) selectComponent(ITEM_LIST.indexOf(e.getSource()));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!isLeftMouseButtonPress){
            userComponentHandler.userDragEnd();
            isAutoScrolling = false;
            return;
        }
        Object target = e.getSource();
        int releasedPosition = ITEM_LIST.indexOf(target);
        if(enterPosition  == releasedPosition && this.onItemPositionClickEvent != null)
            this.onItemPositionClickEvent.onItemClick(ITEM_LIST.indexOf(target),(T)target);

        if(userComponentHandler.isUserMouseDrag()){
            final ArrayList<T> ITEM_PART_LIST = new ArrayList<>();

            final int BEGIN_DRAGGED_POSITION = userComponentHandler.getDraggedBeginPosition();
            final int END_DRAGGED_POSITION   = userComponentHandler.getDraggedEndPosition();

            for(int position = BEGIN_DRAGGED_POSITION ; position <= END_DRAGGED_POSITION ; position ++) ITEM_PART_LIST.add(ITEM_LIST.get(position));

            if(this.onItemSelectedEvent != null && this.onItemPositionClickEvent == null || (this.onItemPositionClickEvent != null && BEGIN_DRAGGED_POSITION < END_DRAGGED_POSITION))
                this.onItemSelectedEvent.onSelect(BEGIN_DRAGGED_POSITION,END_DRAGGED_POSITION,ITEM_PART_LIST);
        }
        userComponentHandler.userDragEnd();
        isAutoScrolling = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(!isLeftMouseButtonPress) return;
        enterPosition = ITEM_LIST.indexOf(e.getSource());
        if(userComponentHandler.isUserMouseDrag()) handleNotRecognizedComponent(enterPosition);
    }

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        if(!isLeftMouseButtonPress) return;
        if(rootPanelLocationOnScreenY == -1) rootPanelLocationOnScreenY = FlatList.this.getLocationOnScreen().y + FlatList.this.getHeight();
        int position = ITEM_LIST.indexOf(e.getSource());
        selectComponent(position);
        int currentMouseYonScreen = e.getYOnScreen();
        userComponentHandler.userDragStart(position);
        userComponentHandler.updateMouseDirection();

        scrollValue = (currentMouseYonScreen - rootPanelLocationOnScreenY)/10;
        /*
         *   Scroll-Down :
         *     whether mouse drag y is under the root panel && direction of mouse moving
         *
         *   Scroll-Up :
         *     direction of mouse moving && degree that close to the panel ceiling && whether user is scrolling
         *
         */
        int userMouseDirection = userComponentHandler.getUserMouseDirection();
        boolean isScrollDown = scrollValue>0 && userMouseDirection == UserComponentHandler.MOUSE_DIRECTION_DOWN;
        boolean isScrollUp = userMouseDirection == UserComponentHandler.MOUSE_DIRECTION_UP
                && currentMouseYonScreen - FlatList.this.getLocationOnScreen().y < 100
                && VERTICAL_SCROLL_BAR.getValue() >0;

        if(isScrollDown || isScrollUp) startAutoScroll();
    }

    private void startAutoScroll(){
        if(isAutoScrolling) return;
        isAutoScrolling = true;
        new SwingWorker<Void,Integer>(){

            private final int FREQUENT_DURATION = 70;

            @Override
            protected Void doInBackground() throws Exception {
                while(isAutoScrolling){
                    try{
                        publish(scrollValue);
                        Thread.sleep(FREQUENT_DURATION);
                    }catch(Exception ee){
                        ee.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                super.process(chunks);
                int value = chunks.get(0);
                final int ITEM_SIZE = ITEM_LIST.size();
                int componentStackPointer = userComponentHandler.getComponentStackPointer();
                if(ITEM_SIZE > componentStackPointer && componentStackPointer > -1){
                    JComponent component = ITEM_LIST.get(componentStackPointer);
                    boolean isStackPointerAboveBaseLine = userComponentHandler.isStackPointerAboveBaseLine(componentStackPointer);
                    if(value>0){
                        if(component.getLocationOnScreen().y + component.getHeight() < rootPanelLocationOnScreenY){
                            if(componentStackPointer < ITEM_SIZE - 1) userComponentHandler.increaseComponentStackPointer();
                            if(isStackPointerAboveBaseLine) deSelectComponent(componentStackPointer);
                            else selectComponent(componentStackPointer);
                        }
                    }else{
                        if(INNER_PANEL.getComponent(componentStackPointer).getLocationOnScreen().y - FlatList.this.getLocationOnScreen().y < 0){// || componentStackPointer > -1){
                            selectComponent(componentStackPointer);
                            if(componentStackPointer > 0) userComponentHandler.decreaseComponentStackPointer();
                        }
                    }
                }
                VERTICAL_SCROLL_BAR.setValue(VERTICAL_SCROLL_BAR.getValue()+chunks.get(0));
            }

            @Override
            protected void done() {
                super.done();
            }
        }.execute();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    class UserComponentHandler{
        static final int MOUSE_DIRECTION_DOWN = -1;
        static final int MOUSE_DIRECTION_NONE = +0;
        static final int MOUSE_DIRECTION_UP   = +1;
        private int userMouseDirection = MOUSE_DIRECTION_NONE;

        private int componentStackPointer = -1;
        private int userDragBeginPointer = -1;

        private boolean isUserMouseDrag;

        private int previousUserMouseScreenY;

        void updateMouseDirection(){
            Point currentPoint = MouseInfo.getPointerInfo().getLocation();
            if (currentPoint.y - previousUserMouseScreenY > 0) userMouseDirection = MOUSE_DIRECTION_DOWN;
            if (currentPoint.y == previousUserMouseScreenY) userMouseDirection = MOUSE_DIRECTION_NONE;
            if (currentPoint.y - previousUserMouseScreenY < 0) userMouseDirection = MOUSE_DIRECTION_UP;
            previousUserMouseScreenY = currentPoint.y;
        }

        int getUserMouseDirection(){
            return userMouseDirection;
        }

        int getUserMouseDirectionByPosition(int componentPosition){
            if (componentStackPointer  > componentPosition) userMouseDirection = MOUSE_DIRECTION_UP;
            if (componentStackPointer == componentPosition) userMouseDirection = MOUSE_DIRECTION_NONE;
            if (componentStackPointer  < componentPosition) userMouseDirection = MOUSE_DIRECTION_DOWN;
            return userMouseDirection;
        }

        void setComponentStackPointer(int componentStackPointer){
            this.componentStackPointer = componentStackPointer;
        }

        void increaseComponentStackPointer(){
            this.componentStackPointer++;
        }

        void decreaseComponentStackPointer(){
            this.componentStackPointer--;
        }

        int getComponentStackPointer(){
            return componentStackPointer;
        }

        boolean isStackPointerAboveBaseLine(int componentPosition){
            boolean isStackPointerAboveBaseLine = componentStackPointer < userDragBeginPointer;
            if(componentStackPointer == userDragBeginPointer) isStackPointerAboveBaseLine = getUserMouseDirectionByPosition(componentPosition) == MOUSE_DIRECTION_UP;
            return isStackPointerAboveBaseLine;
        }

        void userDragStart(int userDragStartPointer){
            if (isUserMouseDrag) return;
            this.userDragBeginPointer = userDragStartPointer;
            this.componentStackPointer = userDragStartPointer;
            isUserMouseDrag = true;
            previousUserMouseScreenY =  MouseInfo.getPointerInfo().getLocation().y;
        }

        void userDragEnd(){
            userMouseDirection = MOUSE_DIRECTION_NONE;
            componentStackPointer = -1;
            userDragBeginPointer  = -1;
            isUserMouseDrag = false;
        }

        boolean isUserMouseDrag(){
            return isUserMouseDrag;
        }

        int getDraggedComponentSize(){
            return Math.abs(componentStackPointer - userDragBeginPointer) + 1;
        }

        int getDraggedBeginPosition(){
            return componentStackPointer < userDragBeginPointer ? componentStackPointer : userDragBeginPointer;
        }

        int getDraggedEndPosition(){
            return componentStackPointer > userDragBeginPointer ? componentStackPointer : userDragBeginPointer;
        }
    }

    public static void main(String args[]){
        FlatList<FlatLabel> list = new FlatList<>();
        list.setDivider(true);
        list.setThemeColor(Color.PINK.darker());
        list.setOnItemSelectedEvent((start,end,objects)->{
            for(Object obj : objects){
                System.out.println(((JTextArea)obj).getText());
            }
        });
        for(int i=0; i<13; i++){
            FlatLabel flatLabel = new FlatLabel("position : " + i);
            flatLabel.setBorder(BorderFactory.createEmptyBorder(20,20,20,0));
            list.addItem(flatLabel);
        }
        list.setSelectedColor(Color.PINK);
        list.setOnItemPositionClickEvent((position,target)->{
            System.out.println("[click] position : " + position);
        });

        FlatFrame flatFrame = new FlatFrame();
        flatFrame.setTitle("FlatList Test");
        flatFrame.setSize(400,600);
        flatFrame.getContainer().add(list);
        flatFrame.setLocationOnScreenCenter();
        flatFrame.show();
    }

}


