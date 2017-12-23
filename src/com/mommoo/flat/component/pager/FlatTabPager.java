package com.mommoo.flat.component.pager;

import com.mommoo.flat.border.FlatEmptyBorder;
import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.component.OnPaintListener;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.image.FlatImagePanel;
import com.mommoo.flat.image.ImageOption;
import com.mommoo.flat.layout.linear.Alignment;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.flat.text.label.FlatLabel;
import com.mommoo.flat.text.textarea.alignment.FlatHorizontalAlignment;
import com.mommoo.flat.text.textarea.alignment.FlatVerticalAlignment;
import com.mommoo.util.ColorManager;
import com.mommoo.util.FontManager;
import com.mommoo.util.ImageManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FlatTabPager {
    private static final ScreenManager SCREEN = ScreenManager.getInstance();
    private static final Font TITLE_FONT = FontManager.getNanumGothicBoldFont(SCREEN.dip2px(12));

    private FlatPanel CONTENT_PANE = new FlatTabPagerView();
    private List<FlatPage> pageList = new ArrayList<>();
    private FlatPageColor flatPageColor = new FlatPageColor(ColorManager.getColorAccent(), Color.BLACK);

    private int offset = 0;
    private int animationDuration = 150;
    private boolean isAnimationOn = true;

    public FlatTabPager() {
        CONTENT_PANE.add(createTitleLabel(), new LinearConstraints(LinearSpace.MATCH_PARENT), "titleLabel");
        CONTENT_PANE.add(new FlatTabPanel(), new LinearConstraints(LinearSpace.MATCH_PARENT), "flatTabPanel");
        CONTENT_PANE.add(new FlatTabIndicator(), new LinearConstraints(LinearSpace.MATCH_PARENT), "indicator");
        CONTENT_PANE.add(new FlatPageSlider(), new LinearConstraints(1, LinearSpace.MATCH_PARENT), "pageSlider");
        setOffset(0);
        getPageSlider().addOnPageSelectedListener(pageIndex->{});
        getPageSlider().addOnPageSelectedListener(pageIndex-> pageList.forEach(page-> page.getPageEventList().forEach(OnPageEventListener::onSelected)));
    }

    public static void main(String[] args) {
        FlatFrame frame = new FlatFrame();
        frame.setTitle("Tab Pager");
        frame.setLocationOnScreenCenter();
        frame.setSize(500,500);
        frame.setResizable(true);
        FlatTabPager pager = new FlatTabPager();
        frame.getContainer().setLayout(new LinearLayout());
        frame.getContainer().add(pager.getView(), new LinearConstraints(1, LinearSpace.MATCH_PARENT));

        JPanel firstContentView = new JPanel();
        firstContentView.setBackground(Color.YELLOW);
        firstContentView.add(new JButton("first page Button"));

        JPanel secondContentView = new JPanel();
        secondContentView.add(new JLabel("second page label"));

        JPanel thirdContentView = new JPanel();
        thirdContentView.setLayout(new LinearLayout(Orientation.VERTICAL, 0, Alignment.CENTER));
        thirdContentView.add(new JButton("third page button"), new LinearConstraints(LinearSpace.WRAP_CENTER_CONTENT));

        JPanel forthContentView = new JPanel();
        forthContentView.setLayout(new GridLayout(2,2, 20,20));
        forthContentView.setBorder(BorderFactory.createEmptyBorder(20,50,20,50));
        forthContentView.add(new FlatImagePanel(ImageManager.LION, ImageOption.MATCH_PARENT));
        forthContentView.add(new FlatImagePanel(ImageManager.PIG, ImageOption.MATCH_PARENT));
        forthContentView.add(new FlatImagePanel(ImageManager.SHEEP, ImageOption.MATCH_PARENT));
        forthContentView.add(new FlatImagePanel(ImageManager.TIGER, ImageOption.MATCH_PARENT));

        pager
                .setTitle("Pager Test")
                .setAnimationOn(true)
                .setAnimationDuration(300)
                .setTabAlignment(FlatTabAlignment.CENTER)
                .setFlatPageColor(Color.PINK, Color.BLACK)
                .setOffset(2)
                .setAnimationOn(true)
                .setScreenOffPageLoad(3)
                .addPage("Image", new FlatPage(firstContentView))
                .addPage("MP3", new FlatPage(secondContentView))
                .addPage("Calendar", new FlatPage(thirdContentView))
                .addPage("Person Name", new FlatPage(forthContentView));

        pager.setOnPageSelectedListener(pageIndex -> {
            System.out.println("selected pageIndex : " + pageIndex);
            System.out.println("selected Tab       : " + pager.getTabText(pageIndex));

            if (pageIndex == 3){
                pager.setFlatPageColor(Color.RED, Color.BLACK);
            }
        });

        pager.getPage("Calendar")
                .setDisableGuideText("NO ACTION")
                .setEnable(false);

//            frame.pack();
        frame.show();
    }

    private Component createTitleLabel() {
        FlatLabel titleLabel = new FlatLabel();
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setOpaque(false);
        titleLabel.setHorizontalAlignment(FlatHorizontalAlignment.CENTER);
        titleLabel.setVerticalAlignment(FlatVerticalAlignment.CENTER);
        titleLabel.setBorder(new FlatEmptyBorder(SCREEN.dip2px(6)));
        titleLabel.setForeground(Color.BLACK);
        return titleLabel;
    }

    private FlatLabel getTitleLabel() {
        return (FlatLabel) CONTENT_PANE.getComponent("titleLabel");
    }

    private FlatTabPanel getTabPanel() {
        return (FlatTabPanel) CONTENT_PANE.getComponent("flatTabPanel");
    }

    private FlatTabIndicator getIndicator() {
        return (FlatTabIndicator) CONTENT_PANE.getComponent("indicator");
    }

    private FlatPageSlider getPageSlider() {
        return (FlatPageSlider) CONTENT_PANE.getComponent("pageSlider");
    }

    private void pageSlide(int index, boolean isAnimation) {
        Rectangle tabBounds = getTabPanel().getComponent(index).getBounds();
        getTabPanel().focusIn(index);
        getIndicator().indicate(tabBounds, isAnimation);
        getPageSlider().slide(index, isAnimation);
    }

    public String getTitle() {
        return getTitleLabel().getText();
    }

    public FlatTabPager setTitle(String title) {
        getTitleLabel().setText(title);
        return this;
    }

    public Font getTitleFont() {
        return getTitleLabel().getFont();
    }

    public FlatTabPager setTitleFont(Font font) {
        getTitleLabel().setFont(font);
        return this;
    }

    public Color getTitleColor() {
        return getTitleLabel().getForeground();
    }

    public FlatTabPager setTitleColor(Color titleColor) {
        getTitleLabel().setForeground(titleColor);
        return this;
    }

    public FlatPageColor getFlatPageColor() {
        return flatPageColor;
    }

    public FlatTabPager setFlatPageColor(Color focusInColor, Color focusOutColor) {
        this.flatPageColor = new FlatPageColor(focusInColor, focusOutColor);
        getTabPanel().setFlatPageColor(flatPageColor);
        getIndicator().setFlatPageColor(flatPageColor);
        return this;
    }

    public Component getView() {
        return CONTENT_PANE;
    }

    public FlatTabPager addPage(String tabText, FlatPage flatPage) {
        pageList.add(flatPage);
        getTabPanel().addTab(tabText, index -> pageSlide(index, isAnimationOn));
        getPageSlider().addPage(flatPage.getViewport());
        return this;
    }

    public FlatTabPager setTabAlignment(FlatTabAlignment tabAlignment){
        getTabPanel().setTabAlignment(tabAlignment);
        return this;
    }

    public String getTabText(int index){
        if (getTabPanel().getComponentCount() <= index || index < 0) return "";

        return getTabPanel().getTabText(index);
    }

    public FlatTabPager removePage(String tabText) {
        removePage(getTabPanel().getTabIndex(tabText));
        return this;
    }

    public FlatTabPager removePage(int pageIndex) {
        getTabPanel().removeTab(pageIndex);
        getPageSlider().removePage(pageIndex);
        return this;
    }

    public FlatPage getPage(String tabText) {
        return getPage(getTabPanel().getTabIndex(tabText));
    }

    public FlatPage getPage(int index) {
        if (pageList.size() < index || index < 0) {
            return null;
        }
        return pageList.get(index);
    }

    public FlatPage[] getPages(){
        return new FlatPage[pageList.size()];
    }

    public List<FlatPage> getPageList(){
        return new ArrayList<>(pageList);
    }

    public int getOffset() {
        return this.offset;
    }

    public FlatTabPager setOffset(int offset) {
        this.offset = offset;
        getTabPanel().setOffset(offset);
        getTabPanel().setOnPaintListener(graphics2D -> {
            getIndicator().setOffsetBounds(getTabPanel().getComponent(offset).getBounds());
            getTabPanel().removeOnPaintListener();
        });
        getPageSlider().setOffset(offset);
        return this;
    }

    public FlatTabPager setScreenOffPageLoad(int pageIndex){
        getPageSlider().setScreenOffLoadIndex(pageIndex);
        return this;
    }

    public OnPageSelectedListener getOnPageSelectedListener() {
        return getPageSlider().getOnPageSelectedListener(0);
    }

    public FlatTabPager setOnPageSelectedListener(OnPageSelectedListener onPageSelectedListener){
        getPageSlider().setOnPageSelectedListener(0, onPageSelectedListener);
        return this;
    }

    public boolean isAnimationOn() {
        return this.isAnimationOn;
    }

    public FlatTabPager setAnimationOn(boolean animationOn) {
        this.isAnimationOn = animationOn;
        return this;
    }

    public int getAnimationDuration() {
        return this.animationDuration;
    }

    public FlatTabPager setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
        getIndicator()
                .getAnimator()
                .setDuration(animationDuration);

        getPageSlider()
                .getAnimator()
                .setDuration(animationDuration);

        return this;
    }

    private class FlatTabPagerView extends FlatPanel {
        private FlatTabPagerView() {
            setLayout(new LinearLayout(Orientation.VERTICAL, 0));
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension dimension = super.getPreferredSize();
            dimension.height += dimension.width;
            return dimension;
        }
    }
}
