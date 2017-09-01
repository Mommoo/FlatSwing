package com.mommoo.flat.frame;

import com.mommoo.flat.button.FlatButton;
import com.mommoo.util.FontManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static com.mommoo.flat.frame.FlatColorPicker.ColorUtils.convertHSBtoRGB;
import static com.mommoo.flat.frame.FlatColorPicker.ColorUtils.convertHueToRGB;

public class FlatColorPicker {

    public interface ColorChangeListener{
        public void colorChanged(int hue, int saturation, int brightness, Color color);
    }

    public interface OnClickListener {
        public void onClick(Object o, Color color);
    }

    public interface Observer{
        public void update();
    }

    private static final ScreenManager SCREEN_MANAGER = ScreenManager.getInstance();
    private static final int FLAT_COLOR_PICKER_WIDTH = SCREEN_MANAGER.getScreenWidth()/3;
    private static final int FLAT_COLOR_PICKER_HEIGHT = 2*FLAT_COLOR_PICKER_WIDTH/3;
    private static final Color THEME_COLOR = new Color(230,230,230);
    private static final Color BORDER_COLOR = Color.GRAY;
    private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

    private final ArrayList<Observer> OBSERVER_LIST = new ArrayList<>();

    private final FlatFrame PICKER_VIEW = new FlatFrame();

    private Color selectedColor = Color.WHITE;
    private int hue, saturation, brightness = 100;

    private ColorChangeListener colorChangeListener;
    private OnClickListener onClickListener;

    public FlatColorPicker(){
        init();
        JPanel container = PICKER_VIEW.getContainer();
        container.setLayout(null);

        final HSB_RectView HSB_RECT_VIEW = new HSB_RectView();
        container.add(HSB_RECT_VIEW).setBounds(0,0,4*FLAT_COLOR_PICKER_HEIGHT/5,4*FLAT_COLOR_PICKER_HEIGHT/5);
        OBSERVER_LIST.add(HSB_RECT_VIEW);

        final VerticalHueView VERTICAL_HUE_VIEW = new VerticalHueView();
        int spaceWidth = FLAT_COLOR_PICKER_WIDTH - 4*FLAT_COLOR_PICKER_HEIGHT/5;
        container.add(VERTICAL_HUE_VIEW).setBounds(4*FLAT_COLOR_PICKER_HEIGHT/5,0,spaceWidth/3,4*FLAT_COLOR_PICKER_HEIGHT/5);
        OBSERVER_LIST.add(VERTICAL_HUE_VIEW);

        final OutputView OUT_PUT_VIEW = new OutputView();
        container.add(OUT_PUT_VIEW).setBounds(4*FLAT_COLOR_PICKER_HEIGHT/5 + spaceWidth/3,0,2*spaceWidth/3,4*FLAT_COLOR_PICKER_HEIGHT/5);
        OBSERVER_LIST.add(OUT_PUT_VIEW);

        final ManipulationView MANIPULATION_VIEW = new ManipulationView();
        container.add(MANIPULATION_VIEW).setBounds(0,4*FLAT_COLOR_PICKER_HEIGHT/5,FLAT_COLOR_PICKER_WIDTH,FLAT_COLOR_PICKER_HEIGHT/5);
    }

    private void init(){
        PICKER_VIEW.setTitle("ColorPicker");
        PICKER_VIEW.setThemeColor(THEME_COLOR);
        PICKER_VIEW.setSize(FLAT_COLOR_PICKER_WIDTH,FLAT_COLOR_PICKER_HEIGHT+FlatFrame.getTitleBarHeight());
    }

    private void notifyColorChanged(Observer hostObserver){
        for(Observer obs : OBSERVER_LIST){
            if(hostObserver != obs)obs.update();
        }
        if (colorChangeListener != null) colorChangeListener.colorChanged(hue,saturation,brightness,selectedColor);
    }

    private int getPaddingTopAndBottom(Component comp){
        return comp.getHeight()/20;
    }

    public void setTitle(String title){
        PICKER_VIEW.setTitle(title);
    }

    public String getTitle(){
        return PICKER_VIEW.getTitle();
    }

    public void setColorChangedListener(ColorChangeListener colorChangedListener){
        this.colorChangeListener = colorChangedListener;
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public Color getSelectedColor(){
        return selectedColor;
    }

    public void setSelectedColor(Color color){
        this.selectedColor = color;
        final float[] HSB = new float[3];
        Color.RGBtoHSB(color.getRed(),color.getGreen(),color.getBlue(),HSB);
        this.hue = Math.round(HSB[0] * 360);
        this.saturation = Math.round(HSB[1] * 100);
        this.brightness = Math.round(HSB[2] * 100);
        notifyColorChanged(null);
    }

    public void setSelectedHSB(int hue, int saturation, int brightness){
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
        this.selectedColor = convertHSBtoRGB(hue,saturation,brightness);
        notifyColorChanged(null);
    }

    public int[] getSelectedHSB(){
        return new int[]{this.hue,this.saturation,this.brightness};
    }

    public void setLocation(Point p){
        PICKER_VIEW.setLocation(p.x,p.y);
    }

    public void setLocationOnScreenCenter(){
        PICKER_VIEW.setLocationOnScreenCenter();
    }

    public void show(){
        PICKER_VIEW.show();
    }

    public static int getWidth(){
        return FLAT_COLOR_PICKER_WIDTH;
    }

    public static int getHeight(){
        return FLAT_COLOR_PICKER_HEIGHT;
    }

    class VerticalHueView extends JPanel implements Observer,MouseListener,MouseMotionListener{

        private BufferedImage verticalHue,guide;
        private boolean mousePressed;
        private int mouseY;
        private float hueViewWidth,hueViewHeight;
        private boolean isCallBack;

        VerticalHueView(){
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            final int PADDING_TOP_BOTTOM = getPaddingTopAndBottom(this);

            hueViewWidth = getWidth()/3.0f;
            hueViewHeight = getHeight() - PADDING_TOP_BOTTOM*2;

            float colorRectWidth = 9*hueViewWidth/10;
            float colorRectHeight = hueViewHeight/360.0f;
            final float paddingLeft = (hueViewWidth - colorRectWidth)/2;
            final float SIZE = PADDING_TOP_BOTTOM/2;

            if(guide == null){
                guide = new BufferedImage((int)(hueViewWidth+(SIZE*2)),(int)SIZE,BufferedImage.TYPE_INT_ARGB);
                final Graphics2D G_2D = guide.createGraphics();
                G_2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                final Color BORDER_COLOR = Color.BLACK;
                final Color SHAPE_FILL_COLOR = new Color(230,230,230);

                final Path2D LEFT_SHAPE = createGuideShape(new float[]{0,0},SIZE,1);
                G_2D.setColor(BORDER_COLOR);
                G_2D.fill(LEFT_SHAPE);
                G_2D.setColor(SHAPE_FILL_COLOR);
                final Path2D SMALL_LEFT_SHAPE = createGuideShape(new float[]{SIZE/10,SIZE/10},SIZE - SIZE/5,1);
                G_2D.fill(SMALL_LEFT_SHAPE);

                final Path2D RIGHT_SHAPE = createGuideShape(new float[]{hueViewWidth + SIZE*2,0},SIZE,-1);
                G_2D.setColor(BORDER_COLOR);
                G_2D.fill(RIGHT_SHAPE);
                G_2D.setColor(SHAPE_FILL_COLOR);
                final Path2D SMALL_RIGHT_SHAPE = createGuideShape(new float[]{hueViewWidth - SIZE/10 + SIZE*2,SIZE/10},SIZE - SIZE/5,-1);
                G_2D.fill(SMALL_RIGHT_SHAPE);
            }

            if(verticalHue == null){
                verticalHue = new BufferedImage((int)hueViewWidth,getHeight(),BufferedImage.TYPE_INT_ARGB);
                final Graphics2D G_2D = verticalHue.createGraphics();
                final Rectangle2D.Float RECT = new Rectangle2D.Float();
                G_2D.setStroke(new BasicStroke(colorRectHeight));
                for(int i=360; i>=0; i--){
                    G_2D.setColor(convertHueToRGB(i));
                    G_2D.drawLine((int)paddingLeft +1,(int)(colorRectHeight*(360-i)),(int)hueViewWidth-3,(int)(colorRectHeight*(360-i)));
                    G_2D.fill(RECT);
                }
            }

            final int START_X = (int)hueViewWidth;
            final int START_Y = PADDING_TOP_BOTTOM;
            final int MAX_Y = START_Y;
            final int MIN_Y = MAX_Y + (int)hueViewHeight;
            final float ONE_DEGREE = hueViewHeight/360.0f;

            int standardY = mouseY;
            if(mouseY >= MIN_Y) standardY = MIN_Y;
            else if(mouseY <= MAX_Y) standardY = MAX_Y;

            boolean isUserMouseAction = mousePressed;
            if(!isUserMouseAction){
                standardY = MIN_Y - Math.round(hue * ONE_DEGREE);
            }

            g.setColor(BORDER_COLOR);
            ((Graphics2D)g).fill(new Rectangle2D.Float(START_X,((float)START_Y - paddingLeft),hueViewWidth,(hueViewHeight + paddingLeft*3)));

            g.drawImage(verticalHue,START_X,START_Y,this);
            g.drawImage(guide,START_X-(int)SIZE,standardY - (int)SIZE/2,this);
            g.setColor(Color.BLACK);
            g.drawLine(START_X,standardY,START_X + (int)hueViewWidth,standardY);

            hue = Math.round((MIN_Y - standardY)/ONE_DEGREE);
            selectedColor = convertHSBtoRGB(hue,saturation,brightness);
            callBack();
        }

        /**
         * @param START_POINT : Guide shape start points
         * @param RECT_SIZE : Standard of guide shape size
         * @param direction : if direction +1 : right direction and -1 : left direction
         */
        private Path2D.Float createGuideShape(final float[] START_POINT,final float RECT_SIZE,int direction){
            final Path2D.Float PATH = new Path2D.Float();
            final float TRIANGLE_HEIGHT = (float)Math.sqrt(3)*RECT_SIZE/2;
            PATH.moveTo(START_POINT[0],START_POINT[1]);
            PATH.lineTo(START_POINT[0],START_POINT[1] + RECT_SIZE);
            PATH.lineTo(START_POINT[0] + direction*(RECT_SIZE - TRIANGLE_HEIGHT),START_POINT[1] +RECT_SIZE);
            PATH.lineTo(START_POINT[0] + direction*(RECT_SIZE),START_POINT[1] + RECT_SIZE/2);
            PATH.lineTo(START_POINT[0] + direction*(RECT_SIZE - TRIANGLE_HEIGHT),START_POINT[1]);
            PATH.closePath();
            return PATH;
        }

        private boolean isHueViewEntered(MouseEvent e){
            final int PARENT_WIDTH = getWidth();
            final int PARENT_HEIGHT = getHeight();
            final int PADDING_LEFT = (int)(PARENT_WIDTH - hueViewWidth)/2;
            final int PADDING_TOP = (int)(PARENT_HEIGHT - hueViewHeight)/2;
            final int clickedX = e.getX();
            final int clickedY = e.getY();
            return clickedX >= PADDING_LEFT && clickedX <= hueViewWidth + PADDING_LEFT && clickedY >= PADDING_TOP && clickedY <= hueViewHeight + PADDING_TOP;
        }

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {
            if (isHueViewEntered(e)){
                mousePressed = true;
                mouseY = e.getY();
                isCallBack = true;
                repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mousePressed = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseDragged(MouseEvent e) {
            if(mousePressed){
                mouseY = e.getY();
                isCallBack = true;
                repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if(isHueViewEntered(e)) setCursor(HAND_CURSOR);
            else setCursor(DEFAULT_CURSOR);
        }

        private void callBack(){
            if(isCallBack) notifyColorChanged(this);
            isCallBack = false;
        }

        @Override
        public void update() {
            repaint();
        }

        @Override
        public String toString(){
            return "VerticalHueView";
        }
    }

    class HSB_RectView extends JPanel implements Observer,MouseMotionListener{
        private final Rectangle2D.Float RECT = new Rectangle2D.Float();
        private BufferedImage hsbRectView;
        private final int MAX_RECT_SIZE = 100;
        private float mouseX,mouseY;
        private final BasicStroke STROKE = new BasicStroke(1.5f);
        private boolean mousePressed;
        private int tempHue;
        private final Color[] MOUSE_POINTER_COLOR_ARRAY = new Color[]{Color.BLACK,Color.WHITE};
        private boolean isCallBack;

        HSB_RectView(){
            tempHue = hue;
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(isHsbRect(e)){
                        mouseX = e.getX();
                        mouseY = e.getY();
                        mousePressed = true;
                        isCallBack = true;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    mousePressed = false;
                }
            });
            addMouseMotionListener(this);
        }

        private void callBack(){
            if(isCallBack) notifyColorChanged(HSB_RectView.this);
            isCallBack = false;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            final Graphics2D G_2D = (Graphics2D)g;
            final float PADDING = getPaddingTopAndBottom(this);
            final float HSB_RECT_WIDTH = (getWidth() - PADDING*2);
            final float HSB_RECT_HEIGHT = (getHeight() - PADDING*2);
            final float colorRectWidth = HSB_RECT_WIDTH/MAX_RECT_SIZE;
            final float colorRectHeight = HSB_RECT_HEIGHT/MAX_RECT_SIZE;
            final int RADIUS = (int)colorRectWidth*2;

            /* Draw background rect for border effect */
            G_2D.translate(PADDING-1,PADDING-1);
            drawBackgroundRect(G_2D,HSB_RECT_WIDTH,HSB_RECT_HEIGHT);

            /* Draw HSB rect */
            G_2D.translate(1,1);
            drawHsbRect(G_2D,HSB_RECT_WIDTH,HSB_RECT_HEIGHT);

            G_2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            G_2D.setStroke(STROKE);

            float mouseX = this.mouseX - PADDING;
            float mouseY = this.mouseY - PADDING;
            boolean isUserMouseAction = mousePressed;

            if(mouseX <= RADIUS) mouseX = 0;
            if(mouseX >= HSB_RECT_WIDTH + RADIUS) mouseX = HSB_RECT_WIDTH;
            if(mouseY <= RADIUS) mouseY = 0;
            if(mouseY >= HSB_RECT_HEIGHT + RADIUS) mouseY = HSB_RECT_HEIGHT;

            if(!isUserMouseAction){
                mouseX = saturation * colorRectWidth;
                mouseY = (100 - brightness) * colorRectHeight;
            }

            /* Decide mouse circle pointer color */
            G_2D.setColor(MOUSE_POINTER_COLOR_ARRAY[brightness<=40?1:0]);

            /* Draw mouse area and Draw mouse point using circle*/
            G_2D.clipRect(0,0,(int)HSB_RECT_WIDTH,(int)HSB_RECT_HEIGHT);
            G_2D.drawOval(Math.round(mouseX-RADIUS),Math.round(mouseY-RADIUS),RADIUS*2,RADIUS*2);


            if (!isUserMouseAction) return;

            /* Data changed by user manipulation are setting*/
            FlatColorPicker.this.saturation = Math.min((int)(mouseX/colorRectWidth),100);
            FlatColorPicker.this.brightness = Math.max(100-(int)(mouseY/colorRectHeight),0);
            FlatColorPicker.this.selectedColor = convertHSBtoRGB(FlatColorPicker.this.hue,FlatColorPicker.this.saturation,FlatColorPicker.this.brightness);
            callBack();
        }

        private void drawBackgroundRect(Graphics2D G_2D,float hsbRectWidth, float hsbRectHeight){
            RECT.setRect(0,0,hsbRectWidth + 2.0f,hsbRectHeight + 2.0f);
            G_2D.setColor(BORDER_COLOR);
            G_2D.fill(RECT);
        }

        private void drawHsbRect(Graphics2D G_2D,float hsbRectWidth, float hsbRectHeight){
            if (hsbRectView == null || tempHue != hue){
                tempHue = hue;
                if(hsbRectView == null) hsbRectView = new BufferedImage((int)hsbRectWidth,(int)hsbRectHeight,BufferedImage.TYPE_INT_ARGB);
                float colorRectWidth = hsbRectWidth/MAX_RECT_SIZE;
                float colorRectHeight = hsbRectHeight/MAX_RECT_SIZE;
                final Graphics2D hsbRectViewGraphics = hsbRectView.createGraphics();
                hsbRectViewGraphics.clearRect(0,0,(int)hsbRectWidth,(int)hsbRectHeight);
                for(int row = 0 ; row < MAX_RECT_SIZE ; row++){
                    for(int col = 0 ; col < MAX_RECT_SIZE ; col ++){
                        RECT.setRect(col * colorRectWidth,row * colorRectHeight,colorRectWidth,colorRectHeight);
                        hsbRectViewGraphics.setColor(convertHSBtoRGB(FlatColorPicker.this.hue,col,100-row));
                        hsbRectViewGraphics.fill(RECT);
                    }
                }
            }
            G_2D.drawImage(hsbRectView,0,0,this);
        }

        private boolean isHsbRect(MouseEvent e){
            int padding = getPaddingTopAndBottom(HSB_RectView.this);
            return e.getX()>=padding && e.getX() <=getWidth() - padding && e.getY() >= padding && e.getY() <= getHeight() - padding;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if(mousePressed){
                mouseX = e.getX();
                mouseY = e.getY();
                isCallBack =true;
                repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if(isHsbRect(e)) setCursor(HAND_CURSOR);
            else setCursor(DEFAULT_CURSOR);
        }

        @Override
        public void update() {
            repaint();
        }

        @Override
        public String toString(){
            return "HSB_RectView";
        }
    }

    class OutputView extends JPanel implements Observer{
        private final JPanel COLOR_GUIDE_LINE = new JPanel();
        private final JPanel COLOR_PANEL = new JPanel();
        private final LineView HUE_GUIDE_LINE = new LineView("H :","0","˚");
        private final LineView SATURATION_GUIDE_LINE = new LineView("S :","0","%");
        private final LineView BRIGHTNESS_GUIDE_LINE = new LineView("B :","0","%");
        private final LineView RED_GUIDE_LINE = new LineView("R :","0","");
        private final LineView GREEN_GUIDE_LINE = new LineView("G :","0","");
        private final LineView BLUE_GUIDE_LINE = new LineView("B :","0","");
        private final LineView HEX_COLOR_TEXT = new LineView("#","ffffff","");

        OutputView(){
            setLayout(new GridLayout(-1,1));
            initColorGuideView();
            add(COLOR_GUIDE_LINE);
            add(HUE_GUIDE_LINE);
            add(SATURATION_GUIDE_LINE);
            add(BRIGHTNESS_GUIDE_LINE);
            add(RED_GUIDE_LINE);
            add(GREEN_GUIDE_LINE);
            add(BLUE_GUIDE_LINE);
            HEX_COLOR_TEXT.GUIDE_VALUE.setColumns(6);
            add(HEX_COLOR_TEXT);
        }

        private void initColorGuideView(){
            COLOR_GUIDE_LINE.setLayout(new FlowLayout(FlowLayout.LEFT));
            final JLabel COLOR_GUIDE_TEXT = new JLabel("선택 색상 :  ");
            COLOR_GUIDE_TEXT.setFont(FontManager.getNanumGothicFont(Font.BOLD,ScreenManager.getInstance().dip2px(10)));
            COLOR_GUIDE_LINE.add(COLOR_GUIDE_TEXT);
            COLOR_GUIDE_LINE.add(COLOR_PANEL);
            Dimension d = COLOR_GUIDE_LINE.getPreferredSize();
            COLOR_PANEL.setPreferredSize(new Dimension(d.height,d.height));
            COLOR_PANEL.setBackground(selectedColor);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            final int LINE_HEIGHT = COLOR_GUIDE_LINE.getSize().height;
            COLOR_PANEL.setSize(new Dimension(LINE_HEIGHT,LINE_HEIGHT));
        }

        class LineView extends JPanel{
            private final ScreenManager SCREEN_MANAGER = ScreenManager.getInstance();
            private final JLabel GUIDE_TEXT = new JLabel();
            private final JTextField GUIDE_VALUE = new JTextField(3);
            private final JLabel FIGURE_TEXT = new JLabel();

            private LineView(String guide, String value, String figure){
                initComponent();
                GUIDE_TEXT.setText(guide);
                GUIDE_VALUE.setText(value);
                FIGURE_TEXT.setText(figure);
                setLayout(new FlowLayout(FlowLayout.LEFT));
                add(GUIDE_TEXT);
                add(GUIDE_VALUE);
                add(FIGURE_TEXT);
            }

            private void initComponent(){
                final int PADDING = SCREEN_MANAGER.dip2px(5);
                final int FONT_SIZE = SCREEN_MANAGER.dip2px(10);
                final Font TEXT_FONT = FontManager.getNanumGothicFont(Font.BOLD,FONT_SIZE);
                final Font FIGURE_FONT = FontManager.getNanumGothicFont(Font.PLAIN,FONT_SIZE);

                GUIDE_TEXT.setFont(TEXT_FONT);
                GUIDE_VALUE.setBorder(BorderFactory.createEmptyBorder(PADDING,PADDING,PADDING,PADDING));
                GUIDE_VALUE.setBackground(Color.WHITE);
                GUIDE_VALUE.setHorizontalAlignment(JTextField.CENTER);
                GUIDE_VALUE.setEditable(false);
                FIGURE_TEXT.setFont(FIGURE_FONT);
            }

            private void setValue(String value){
                GUIDE_VALUE.setText(value);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Dimension guideDimen = new Dimension(50 , GUIDE_TEXT.getHeight());
                GUIDE_TEXT.setSize(guideDimen);
                GUIDE_TEXT.setPreferredSize(guideDimen);
                GUIDE_TEXT.setMinimumSize(guideDimen);
            }
        }

        @Override
        public void update() {
            final int RED = selectedColor.getRed();
            final int GREEN = selectedColor.getGreen();
            final int BLUE = selectedColor.getBlue();
            RED_GUIDE_LINE.setValue("" + RED);
            GREEN_GUIDE_LINE.setValue("" + GREEN);
            BLUE_GUIDE_LINE.setValue("" + BLUE);
            HUE_GUIDE_LINE.setValue(""+hue);
            SATURATION_GUIDE_LINE.setValue(""+saturation);
            BRIGHTNESS_GUIDE_LINE.setValue(""+brightness);
            HEX_COLOR_TEXT.setValue(String.format("%02x%02x%02x", RED, GREEN, BLUE));
            COLOR_PANEL.setBackground(selectedColor);
        }

        @Override
        public String toString(){
            return "OutputView";
        }
    }

    class ManipulationView extends JPanel{
        private final ScreenManager SCREEN_MANAGER = ScreenManager.getInstance();
        private final Font font = FontManager.getNanumGothicFont(Font.BOLD,SCREEN_MANAGER.dip2px(11));
        private final FlatButton CONFIRM_BUTTON = new FlatButton("확인");
        private final FlatButton CANCEL_BUTTON = new FlatButton("취소");
        private final JPanel MANIPULATE_AREA = new JPanel();
        private final JLabel SHOP = new JLabel("#");
        private final JTextField HEX_COLOR_VALUE = new JTextField(6);
        private final FlatButton APPLY_BUTTON = new FlatButton("적용");

        ManipulationView(){
            setLayout(new FlowLayout(FlowLayout.CENTER,SCREEN_MANAGER.dip2px(16),0));
            initButtons(CONFIRM_BUTTON,CANCEL_BUTTON,APPLY_BUTTON);
            initManipulationArea();
            addComponent(MANIPULATE_AREA,CONFIRM_BUTTON,CANCEL_BUTTON);
        }

        private void initButtons(FlatButton... flatButtons){

            int padding = SCREEN_MANAGER.dip2px(6);
            for(FlatButton btn : flatButtons){
                btn.setThemeColor(THEME_COLOR);
                btn.setTextColor(Color.BLACK);
                btn.setFont(font);
                btn.setOutLine(true);
                btn.setBorder(BorderFactory.createEmptyBorder(padding,padding*2,padding,padding*2));
            }
            CONFIRM_BUTTON.setOnClickListener(o->{
                if(FlatColorPicker.this.onClickListener != null) onClickListener.onClick(o,selectedColor);
                FlatColorPicker.this.PICKER_VIEW.getJFrame().dispose();
            });

            CANCEL_BUTTON.setOnClickListener(o-> FlatColorPicker.this.PICKER_VIEW.getJFrame().dispose());
        }

        private void initManipulationArea(){
            MANIPULATE_AREA.setLayout(new FlowLayout());
            MANIPULATE_AREA.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f)));
            SHOP.setFont(font);
            HEX_COLOR_VALUE.setFont(font.deriveFont(Font.PLAIN));
            HEX_COLOR_VALUE.setHorizontalAlignment(JTextField.CENTER);
            MANIPULATE_AREA.add(SHOP);
            MANIPULATE_AREA.add(HEX_COLOR_VALUE);
            MANIPULATE_AREA.add(APPLY_BUTTON);
            APPLY_BUTTON.setOnClickListener(o->{
                if(!isHexColorString(HEX_COLOR_VALUE.getText())){
                    new FlatDialog.Builder()
                            .setTitle("에러")
                            .setContent("잘못된 색상 형식입니다.")
                            .setButtonBackgroundColor(THEME_COLOR)
                            .setLocationCenterTo(FlatColorPicker.this.PICKER_VIEW.getJFrame())
                            .setOnClickListener((obj)->{HEX_COLOR_VALUE.setText("");})
                            .build().show();
                }else setSelectedColor(Color.decode("#"+HEX_COLOR_VALUE.getText()));
            });
        }

        private void addComponent(JComponent... comps){
            for(JComponent comp : comps){
                add(comp);
            }
        }

        private boolean isHexColorString(String str){
            if(str.length() != 6) return false;
            str = str.toUpperCase();
            for(char c : str.toCharArray()){
                if(c>70) return false;
            }
            return true;
        }
    }
    static class ColorUtils{
        static Color convertHueToRGB(int hue){

            if(hue <0 ) return null;

            float r = 0.0f;
            float g = 0.0f;
            float b = 0.0f;
            float sub = 4.250f;

            float ratio = hue - 60*(hue/60);
            if(ratio % 60 ==0) ratio += 60;
            if (hue == 0){
                r = 255.0f;
            }else if (hue>0 && hue<=60){
                r = 255.0f;
                g = sub*ratio;
                b = 0.0f;
            }else if (hue>60 && hue<=120){
                r = 255.0f - sub*ratio;
                g = 255.0f;
                b = 0.0f;
            }else if (hue>120 && hue<=180){
                r = 0f;
                g = 255.0f;
                b = sub*ratio;
            }else if (hue>180 && hue <= 240){
                r = 0f;
                g = 255.0f - sub*ratio;
                b = 255.0f;
            }else if (hue>240 && hue <= 300){
                r = sub*ratio;
                g = 0.0f;
                b = 255.0f;
            }else if (hue>300 && hue<=360){
                r = 255.0f;
                g = 0.0f;
                b = 255.0f - sub*ratio;
            }

            return new Color((int)r,(int)g,(int)b);
        }


        /**
         * @param h
         *            0-360
         * @param s
         *            0-100
         * @param b
         *            0-100
         * @return color in hex string
         */
        public static Color convertHSBtoRGB(float h, float s, float b) {

            float R, G, B;

            h /= 360f;
            s /= 100f;
            b /= 100f;

            if (s == 0)
            {
                R = b * 255;
                G = b * 255;
                B = b * 255;
            } else {
                float var_h = h * 6;
                if (var_h == 6)
                    var_h = 0; // H must be < 1
                int var_i = (int) Math.floor((double) var_h); // Or ... var_i =
                // floor( var_h )
                float var_1 = b * (1 - s);
                float var_2 = b * (1 - s * (var_h - var_i));
                float var_3 = b * (1 - s * (1 - (var_h - var_i)));

                float var_r;
                float var_g;
                float var_b;
                if (var_i == 0) {
                    var_r = b;
                    var_g = var_3;
                    var_b = var_1;
                } else if (var_i == 1) {
                    var_r = var_2;
                    var_g = b;
                    var_b = var_1;
                } else if (var_i == 2) {
                    var_r = var_1;
                    var_g = b;
                    var_b = var_3;
                } else if (var_i == 3) {
                    var_r = var_1;
                    var_g = var_2;
                    var_b = b;
                } else if (var_i == 4) {
                    var_r = var_3;
                    var_g = var_1;
                    var_b = b;
                } else {
                    var_r = b;
                    var_g = var_1;
                    var_b = var_2;
                }

                R = var_r * 255; // RGB results from 0 to 255
                G = var_g * 255;
                B = var_b * 255;
            }
                return new Color(Math.round(R),Math.round(G),Math.round(B));
        }
    }

    public static void main(String[] args){
        FlatColorPicker flatColorPicker = new FlatColorPicker();
        flatColorPicker.setLocationOnScreenCenter();
        flatColorPicker.setColorChangedListener(
                (hue, saturation, brightness, color)-> System.out.println("Changed Color : " + color));
        flatColorPicker.setOnClickListener((o, color) -> System.out.println("Selected Color : " + color));
        flatColorPicker.show();
    }
}
