package com.mommoo.flat.component;

import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * Created by mommoo on 2017-07-14.
 */
public class FlatPanel extends JPanel {

    private MouseClickAdapter mouseClickAdapter;
    private OnClickListener onClickListener = comp -> {};
    private OnPaintListener onPaintListener = g2d -> {};
    private OnLayoutListener onLayoutListener = (width, height) -> {};

    private float alpha = 1.0f;

    private final Map<String, Component> COMPONENT_MAP = new HashMap<>();

    public FlatPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        init();
    }

    public FlatPanel(LayoutManager layout) {
        super(layout);
        init();
    }

    public FlatPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        init();
    }

    public FlatPanel() {
        super();
        init();
    }

    private void init(){
        setOpaque(false);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics2D = (Graphics2D)g;

        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        Insets insets = getInsets();
        int availableWidth = getWidth() - insets.left - insets.right;
        int availableHeight = getHeight() - insets.top - insets.bottom;

        preDraw(graphics2D, availableWidth, availableHeight);

        super.paint(g);

        onPaintListener.onPaint(graphics2D);
        onLayoutListener.onLayout(availableWidth, availableHeight);
        postDraw(graphics2D, availableWidth, availableHeight);
        draw(graphics2D, availableWidth, availableHeight);
    }

    protected void preDraw(Graphics2D graphics2D, int availableWidth, int availableHeight){

    }

    protected void postDraw(Graphics2D graphics2D, int availableWidth, int availableHeight){

    }

    @Deprecated
    protected void draw(Graphics2D graphics2D, int availableWidth, int availableHeight){

    }

    public boolean isComponentContained(Component component){
        for (Component comp : getComponents()){
            if (comp == component) return true;
        }
        return false;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener){
        if (onClickListener == null) return;
        this.onClickListener = onClickListener;
        this.mouseClickAdapter = new MouseClickAdapter(onClickListener);
        addMouseListener(new MouseClickAdapter(onClickListener));
    }

    public void removeOnClickListener(){
        removeMouseListener(this.mouseClickAdapter);
        this.onClickListener = component -> {};
    }

    public OnPaintListener getOnPaintListener() {
        return onPaintListener;
    }

    public void setOnPaintListener(OnPaintListener onPaintListener){
        if (onPaintListener == null) return;
        this.onPaintListener = onPaintListener;
    }

    public void removeOnPaintListener(){
        onPaintListener = g -> {};
    }

    public OnLayoutListener getOnLayoutListener() {
        return onLayoutListener;
    }

    public void setOnLayoutListener(OnLayoutListener onLayoutListener){
        if (onLayoutListener == null) return;
        this.onLayoutListener = onLayoutListener;
    }

    public void removeOnLayoutListener(){
        onLayoutListener = (width, height) -> {};
    }

    public float getAlpha(){
        return this.alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public Component getComponent(String key){
        if (COMPONENT_MAP == null) return null;
        return COMPONENT_MAP.get(key);
    }

    public Component add(Component comp, String componentKey) {
        COMPONENT_MAP.put(componentKey, comp);
        return super.add(comp);
    }

    public Component add(String name, Component comp, String componentKey) {
        COMPONENT_MAP.put(componentKey, comp);
        return super.add(name, comp);
    }

    public Component add(Component comp, int index, String componentKey) {
        COMPONENT_MAP.put(componentKey, comp);
        return super.add(comp, index);
    }

    public void add(Component comp, Object constraints, String componentKey) {
        COMPONENT_MAP.put(componentKey, comp);
        super.add(comp, constraints);
    }

    public void add(Component comp, Object constraints, int index, String componentKey) {
        COMPONENT_MAP.put(componentKey, comp);
        super.add(comp, constraints, index);
    }

    public void remove(String componentKey){
        if (COMPONENT_MAP.get(componentKey) != null){
            super.remove(COMPONENT_MAP.get(componentKey));
            COMPONENT_MAP.remove(componentKey);
        }
    }

    @Override
    public void remove(int index) {
        String key = getKey(index);
        if (key != null) COMPONENT_MAP.remove(key);
        super.remove(index);
    }

    public String getKey(Component component){

        Optional<Map.Entry<String, Component>> optional =
                COMPONENT_MAP
                        .entrySet()
                        .stream()
                        .filter( entry -> entry.getValue() == component)
                        .findFirst();

        if (optional.isPresent()){
            return optional.get().getKey();
        }

        return null;
    }

    public String getKey(int index){
        return getKey(getComponent(index));
    }

    @Override
    public void remove(Component comp) {
        String key = getKey(comp);
        if (key != null) COMPONENT_MAP.remove(key);
        super.remove(comp);
    }

    @Override
    public void removeAll() {
        COMPONENT_MAP.clear();
        super.removeAll();
    }
    private static JPanel createColorPanel(Color color) {
        JPanel colorPanel = new JPanel();
        colorPanel.setOpaque(true);
        colorPanel.setBackground(color);
        return colorPanel;
    }
    public static void main(String[] args){
        FlatFrame frame = new FlatFrame();
        frame.setSize(500,500);
        frame.setLocationOnScreenCenter();
        frame.getContainer().setLayout(new BorderLayout());
        JPanel panel = new JPanel(new LinearLayout(0));
        panel.add(new JButton("버튼"), new LinearConstraints(1, LinearSpace.MATCH_PARENT));
        panel.add(createColorPanel(Color.GREEN), new LinearConstraints(1, LinearSpace.MATCH_PARENT));
        frame.getContainer().add(panel);
        panel.addMouseListener(new MouseClickAdapter(comp->System.out.println("onClick!!")));
        frame.show();
    }
}
