package com.mommoo.flat.component.pager;

import com.mommoo.flat.border.FlatEmptyBorder;
import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.flat.text.label.FlatLabel;
import com.mommoo.flat.text.textarea.alignment.FlatHorizontalAlignment;
import com.mommoo.flat.text.textarea.alignment.FlatVerticalAlignment;
import com.mommoo.util.FontManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;

public class FlatTabPager {
    private static final ScreenManager SCREEN = ScreenManager.getInstance();
    private static final Font TITLE_FONT = FontManager.getNanumGothicBoldFont(SCREEN.dip2px(12));

    private FlatPanel CONTENT_PANE = new FlatTabPagerView();

    public FlatTabPager(){
        CONTENT_PANE.add(createTitleLabel(), new LinearConstraints(LinearSpace.MATCH_PARENT), "titleLabel");
        CONTENT_PANE.add(new FlatTabPanel(), new LinearConstraints(LinearSpace.MATCH_PARENT), "flatTabPanel");
        CONTENT_PANE.add(new FlatTabIndicator(), new LinearConstraints(LinearSpace.MATCH_PARENT), "indicator");
        CONTENT_PANE.add(new FlatPageSlider(), new LinearConstraints(1,LinearSpace.MATCH_PARENT), "pageSlider");

        CONTENT_PANE.setOnLayoutListener((availableWidth, availableHeight) -> {
            if (getTabPanel().getComponentCount() > 0){
                getIndicator().initBounds(getTabPanel().getComponent(0).getBounds());
                CONTENT_PANE.removeOnLayoutListener();
            }
        });
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            FlatFrame frame = new FlatFrame();
            frame.setSize(500,500);
            frame.setLocationOnScreenCenter();
            FlatTabPager pager = new FlatTabPager();
            frame.getContainer().setLayout(new FlowLayout());
            frame.getContainer().add(pager.CONTENT_PANE);
            pager.setTitle("Pager Test");
            pager.addPage("Image", createColorPanel(Color.YELLOW));
            pager.addPage("MP3", createColorPanel(Color.ORANGE));
            pager.addPage("Calendar", createColorPanel(Color.GREEN));
            pager.addPage("Person Name", createColorPanel(Color.MAGENTA));
            frame.show();
        });
    }

    private static JPanel createColorPanel(Color color){
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(color);
        return panel;
    }

    private Component createTitleLabel(){
        FlatLabel titleLabel = new FlatLabel();
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(FlatHorizontalAlignment.CENTER);
        titleLabel.setVerticalAlignment(FlatVerticalAlignment.CENTER);
        titleLabel.setBorder(new FlatEmptyBorder(SCREEN.dip2px(6)));
        titleLabel.setForeground(Color.BLACK);
        return titleLabel;
    }

    private FlatLabel getTitleLabel(){
        return (FlatLabel) CONTENT_PANE.getComponent("titleLabel");
    }

    private FlatTabPanel getTabPanel(){
        return (FlatTabPanel) CONTENT_PANE.getComponent("flatTabPanel");
    }

    private FlatTabIndicator getIndicator(){
        return (FlatTabIndicator) CONTENT_PANE.getComponent("indicator");
    }

    private FlatPageSlider getPageSlider(){
        return (FlatPageSlider) CONTENT_PANE.getComponent("pageSlider");
    }

    public String getTitle(){
        return getTitleLabel().getText();
    }

    public void setTitle(String title){
        getTitleLabel().setText(title);
    }

    public void setFlatPageColor(FlatPageColor flatPageColor){
        getTabPanel().setFlatPageColor(flatPageColor);
        getIndicator().setFlatPageColor(flatPageColor);
    }

    public Component getView(){
        return CONTENT_PANE;
    }

    public void addPage(String tabText, JPanel page){
        getTabPanel().addTab(tabText, (index, comp)-> {
            getIndicator().indicate(comp.getBounds());
            getPageSlider().slide(index);
        });

        getPageSlider().addPage(page);
    }

    private class FlatTabPagerView extends FlatPanel{
        private FlatTabPagerView(){
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
