package com.mommoo.flat.component;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mommoo on 2017-07-14.
 */
public class FlatPanel extends JPanel {
    public boolean isComponentContained(Component component){
        for (Component comp : getComponents()){
            if (comp == component) return true;
        }
        return false;
    }
}
