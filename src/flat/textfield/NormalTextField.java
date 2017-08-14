package flat.textfield;

import javax.swing.*;

/**
 * Created by mommoo on 2017-07-14.
 */
public class NormalTextField extends TextFieldProxy {

    NormalTextField() {
        super(new JTextField());
    }

    @Override
    void setText(String text) {
        getTextField().setForeground(this.originalForegroundColor == null ? getTextField().getForeground() : this.originalForegroundColor);
        getTextField().setText(text);
    }

    @Override
    void setHintText() {
        getTextField().setText(hint);
        this.originalForegroundColor = getTextField().getForeground();
        getTextField().setForeground(hintColor);
    }
}
