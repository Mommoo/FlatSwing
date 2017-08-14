package flat.frame.titlebar;

import util.FontManager;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mommoo on 2017-07-13.
 */
public class TitleLabel extends JLabel {
    private static final int TITLE_TEXT_LEFT_PADDING = 10;

    public TitleLabel(int fontSize){
        setHorizontalAlignment(JLabel.LEFT);
        setOpaque(false);
        setFont(FontManager.getNanumGothicFont(Font.PLAIN, fontSize));
        setBorder(BorderFactory.createEmptyBorder(0,TITLE_TEXT_LEFT_PADDING,0,0));
    }
}
