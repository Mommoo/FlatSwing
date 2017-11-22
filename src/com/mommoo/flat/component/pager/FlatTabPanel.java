package com.mommoo.flat.component.pager;

import com.mommoo.flat.border.FlatEmptyBorder;
import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.layout.linear.Alignment;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.mommoo.flat.text.label.FlatLabel;
import com.mommoo.util.FontManager;
import com.mommoo.util.ScreenManager;

import javax.swing.border.Border;
import java.awt.*;

class FlatTabPanel extends FlatPanel {
    private static final ScreenManager SCREEN = ScreenManager.getInstance();
    private Runnable onPaintFinishListener = () -> {};
    private int offset = 0;

    FlatTabPanel(){
        setLayout(new LinearLayout(SCREEN.dip2px(2)));
        setOnLayoutListener(((availableWidth, availableHeight) -> {
            if (getComponentCount() > 0){
                ((FlatTabView)getComponent(offset)).focusIn();
                removeOnLayoutListener();
            }
        }));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        onPaintFinishListener.run();
    }

    void setOnPaintFinishListener(Runnable onPaintFinishListener){
        this.onPaintFinishListener = onPaintFinishListener;
    }

    void setFlatPageColor(FlatPageColor pagerColor){
        for (Component comp : getComponents()){
            ( (FlatTabView) comp ).setFlatPageColor(pagerColor);
        }
    }

    void addTab(String tabText, OnTabClickListener onTabClickListener){
        final int index = getComponentCount();

        FlatTabView tabView = new FlatTabView(tabText);
        add(tabView, new LinearConstraints(LinearSpace.MATCH_PARENT));

        tabView.setOnClickListener(component -> {
            focusOutAll();
            tabView.focusIn();
            onTabClickListener.onTabClick(index);
        });
    }

    int getTabIndex(String tabText){
        for (int i = 0, size = getComponentCount(); i < size ; i ++){
            if (getTabText(i).equals(tabText)){
                return i;
            }
        }

        return -1;
    }

    String getTabText(int index){
        return ((FlatTabView) getComponent(index)).getText();
    }

    void removeTab(int index){
        remove(index);
    }

    void setTabAlignment(FlatTabAlignment alignment){
        LinearLayout linearLayout = (LinearLayout) getLayout();
        switch(alignment){
            case START :
                linearLayout.setAlignment(Alignment.START);
                break;
            case CENTER :
                linearLayout.setAlignment(Alignment.CENTER);
                break;
            case END :
                linearLayout.setAlignment(Alignment.END);
                break;
        }

        doLayout();
    }

    void focusIn(int index){
        focusOutAll();
        ((FlatTabView)getComponent(index)).focusIn();
    }

    void setOffset(int offset){
        this.offset = offset;
    }

    private void focusOutAll(){
        for (Component comp : getComponents()){
            ((FlatTabView) comp).focusOut();
        }
    }

    interface OnTabClickListener{
        public void onTabClick(int position);
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
            setCursor(new Cursor(Cursor.HAND_CURSOR));
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
