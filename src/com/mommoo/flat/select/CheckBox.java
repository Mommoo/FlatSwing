package com.mommoo.flat.select;

import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.component.MouseClickAdapter;
import com.mommoo.flat.component.OnClickListener;
import com.mommoo.flat.image.FlatImagePanel;
import com.mommoo.flat.image.ImageOption;
import com.mommoo.util.ImageManager;
import com.mommoo.util.ScreenManager;

import javax.swing.*;
import java.awt.*;

class CheckBox extends FlatPanel{
    private static final ScreenManager screenManager = ScreenManager.getInstance();
    private static final Dimension CHECK_BOX_DIMENSION = new Dimension(screenManager.dip2px(15), screenManager.dip2px(15));

    private OnClickListener onClickListener;

    private MouseClickAdapter userMouseClickAdapter;

    CheckBox(){
        initCheckBox();
        Component checkImage = createCheckImage();
        add(checkImage);
        initOnClickListener(checkImage);
        addMouseListener(new HoverColorChangeListener());
        addMouseListener(new MouseClickAdapter(onClickListener));

    }

    private void initCheckBox(){
        setOpaque(true);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        setBackground(Color.WHITE);
    }

    private Component createCheckImage(){
        FlatImagePanel checkImage = new FlatImagePanel();
        checkImage.setImage(ImageManager.CHECK, ImageOption.MATCH_PARENT);
        checkImage.setVisible(false);

        return checkImage;
    }

    private void initOnClickListener(Component checkImage){
        onClickListener = comp-> checkImage.setVisible(!checkImage.isVisible());
    }

    void doClick(){
        this.onClickListener.onClick(this);
        if (this.userMouseClickAdapter != null) this.userMouseClickAdapter.getOnClickListener().onClick(this);
    }

    public void setOnClickListener(OnClickListener onClickListener){
        if (userMouseClickAdapter != null) removeMouseListener(userMouseClickAdapter);
        this.userMouseClickAdapter = new MouseClickAdapter(onClickListener);
        addMouseListener(this.userMouseClickAdapter);
    }

    boolean isChecked(){
        return getComponent(0).isVisible();
    }

    void setChecked(boolean check){
        getComponent(0).setVisible(check);
    }

    @Override
    public Dimension getMaximumSize() {
        return CHECK_BOX_DIMENSION;
    }

    @Override
    public Dimension getPreferredSize() {
        return CHECK_BOX_DIMENSION;
    }

    @Override
    public Dimension getMinimumSize() {
        return CHECK_BOX_DIMENSION;
    }
}

