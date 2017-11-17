package com.mommoo.flat.component.pager;

import com.mommoo.flat.border.FlatEmptyBorder;
import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.flat.text.label.FlatLabel;
import com.mommoo.util.FontManager;
import com.mommoo.util.ScreenManager;

import javax.swing.border.Border;
import java.awt.*;

public class FlatTabPanel extends FlatPanel {
    private static final ScreenManager SCREEN = ScreenManager.getInstance();

    public FlatTabPanel(){
        setLayout(new LinearLayout(SCREEN.dip2px(2)));
        setOnLayoutListener(((availableWidth, availableHeight) -> {
            if (getComponentCount() > 0){
                ((FlatTabView)getComponent(0)).focusIn();
                removeOnLayoutListener();
            }
        }));
    }

    public void setFlatPageColor(FlatPageColor pagerColor){
        for (Component comp : getComponents()){
            ( (FlatTabView) comp ).setFlatPageColor(pagerColor);
        }
    }

    public void addTab(String tabText, OnTabClickListener onTabClickListener){
        final int index = getComponentCount();

        FlatTabView tabView = new FlatTabView(tabText);
        add(tabView, new LinearConstraints(LinearSpace.MATCH_PARENT));

        tabView.setOnClickListener(component -> {

            for (Component comp : getComponents()){

                FlatTabView flatTabView = (FlatTabView) comp;

                if (flatTabView != tabView){
                    flatTabView.focusOut();
                }
            }

            tabView.focusIn();

            onTabClickListener.onTabClick(index, component);
        });
    }

    interface OnTabClickListener{
        public void onTabClick(int position, Component comp);
    }

    private static class FlatTabView extends FlatLabel {
        private static final int FONT_SIZE = SCREEN.dip2px(8);
        private static final Font TAB_LABEL_PLAIN_FONT = FontManager.getNanumGothicFont(Font.PLAIN, FONT_SIZE);
        private static final Font TAB_LABEL_BOLD_FONT = FontManager.getNanumGothicFont(Font.BOLD, FONT_SIZE);
        private static final Border PADDING_BORDER = new FlatEmptyBorder(SCREEN.dip2px(6));

        private FlatPageColor flatPageColor = FlatPageColor.getDefaultFlatPagerColor();

        private FlatTabView(String text){
            setText(text);
            setBorder(PADDING_BORDER);
            focusIn();
            fixWidth(getPreferredSize().width);
            focusOut();

        }

        private void setFlatPageColor(FlatPageColor flatPageColor){
            this.flatPageColor = flatPageColor;
        }

        private void focusIn(){
            setFont(TAB_LABEL_BOLD_FONT);
            setForeground(flatPageColor.getFocusInColor());
        }

        private void focusOut(){
            setFont(TAB_LABEL_PLAIN_FONT);
            setForeground(flatPageColor.getFocusOutColor());
        }
    }
}
