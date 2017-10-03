package com.mommoo.flat.text.textarea;

import javax.swing.text.*;

class FlatWrapEditorKit extends StyledEditorKit {
    private ViewFactory defaultFactory = new WrapColumnFactory();
    boolean isNeedToCentered;
    private FlatAutoResizeListener flatAutoResizeListener;

    public ViewFactory getViewFactory() {
        return defaultFactory;
    }

    FlatWrapEditorKit(FlatAutoResizeListener flatAutoResizeListener){
        this.flatAutoResizeListener = flatAutoResizeListener;
    }

    private class WrapColumnFactory implements ViewFactory {
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new WrapLabelView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new ParagraphView(elem);
                } else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return isNeedToCentered ? new CenteredBoxView(elem, View.Y_AXIS) : new BoxView(elem, View.Y_AXIS);
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

        public View breakView(int axis, int p0, float pos, float len) {
            if (axis == View.X_AXIS) {
                checkPainter();
                int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos, len);

                if (p0 == getStartOffset() && p1 == getEndOffset()) {
                    return this;
                }

                return createFragment(p0, p1);
            }
            return this;
        }

        public int getBreakWeight(int axis, float pos, float len) {
            if (axis == View.X_AXIS) {
                checkPainter();
                int p0 = getStartOffset();
                int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos, len);


                if (p1 == p0) {
                    return View.BadBreakWeight;
                }
                return View.GoodBreakWeight;
            }
            return View.GoodBreakWeight;
        }

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

    private class CenteredBoxView extends BoxView {
        private  CenteredBoxView(Element elem, int axis) {
            super(elem,axis);
        }
        protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {

            super.layoutMajorAxis(targetSpan, axis, offsets, spans);

            int textBlockHeight = flatAutoResizeListener.getFittedHeight(flatAutoResizeListener.getLineCount());
            int offset = (getContainer().getHeight() - textBlockHeight) / 2;

            for (int i = 0; i < offsets.length; i++) {
                offsets[i] += offset;
            }

        }
    }
}


