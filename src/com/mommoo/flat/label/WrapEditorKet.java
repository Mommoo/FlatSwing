package com.mommoo.flat.label;

import javax.swing.text.*;

public class WrapEditorKet extends StyledEditorKit{
    private ViewFactory defaultFactory = new WrapColumnFactory();

    @Override
    public ViewFactory getViewFactory() {
        return defaultFactory;
    }

    private class WrapColumnFactory implements ViewFactory{

        @Override
        public View create(Element elem) {
            String kind = elem.getName();

            if (kind != null){
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new WrapLabelView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new ParagraphView(elem);
                } else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return new BoxView(elem, View.Y_AXIS);
                } else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }

            return new LabelView(elem);
        }
    }

    private class WrapLabelView extends LabelView {
        private WrapLabelView(Element elem) {
            super(elem);
        }

        @Override
        public float getMinimumSpan(int axis) {
            switch (axis) {
                case View.X_AXIS:
                    return 0;
                case View.Y_AXIS:
                    return super.getMinimumSpan(axis);
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }

    }
}
