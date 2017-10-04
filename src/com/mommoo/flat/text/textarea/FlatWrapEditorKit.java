package com.mommoo.flat.text.textarea;

import javax.swing.text.*;
import java.awt.*;

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
//                    return new LabelView(elem);
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
                    return 0f;
                case View.Y_AXIS:
                    return super.getMinimumSpan(axis);
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }
/////////////////////////////////////////////////////////////////////////////////////
//        public int getNextVisualPositionFrom(int pos, Position.Bias b, Shape a,
//                                             int direction, Position.Bias[] biasRet)
//                throws BadLocationException {
//            if (pos < -1) {
//                // -1 is a reserved value, see the code below
//                throw new BadLocationException("Invalid position", pos);
//            }
//
//            biasRet[0] = Position.Bias.Backward;
//            switch (direction) {
//                case NORTH:
//                case SOUTH:
//                {
//                    if (pos == -1) {
//                        pos = (direction == NORTH) ? Math.max(0, getEndOffset() - 1) :
//                                getStartOffset();
//                        break;
//                    }
//                    JTextComponent target = (JTextComponent) getContainer();
//                    Caret c = (target != null) ? target.getCaret() : null;
//                    // YECK! Ideally, the x location from the magic caret position
//                    // would be passed in.
//                    Point mcp;
//                    if (c != null) {
//                        mcp = c.getMagicCaretPosition();
//                    }
//                    else {
//                        mcp = null;
//                    }
//                    int x;
//                    if (mcp == null) {
//                        Rectangle loc = target.modelToView(pos);
//                        x = (loc == null) ? 0 : loc.x;
//                    }
//                    else {
//                        x = mcp.x;
//                    }
//                    if (direction == NORTH) {
//                        pos = Utilities.getPositionAbove(target, pos, x);
//                    }
//                    else {
//                        pos = Utilities.getPositionBelow(target, pos, x);
//                    }
//                }
//                break;
//                case WEST:
//                    if(pos == -1) {
//                        pos = Math.max(0, getEndOffset() - 1);
//                    }
//                    else {
//                        pos = Math.max(0, pos - 1);
//                    }
//                    break;
//                case EAST:
//                    if(pos == -1) {
//                        pos = getStartOffset();
//                    }
//                    else {
//                        pos = Math.min(pos + 1, getDocument().getLength());
//                    }
//                    break;
//                default:
//                    throw new IllegalArgumentException("Bad direction: " + direction);
//            }
//            return pos;
//        }
//
//        public Shape modelToView(int p0, Position.Bias b0, int p1, Position.Bias b1, Shape a) throws BadLocationException {
//            Shape s0 = modelToView(p0, a, Position.Bias.Backward);
//            Shape s1;
//            if (p1 == getEndOffset()) {
//                try {
//                    s1 = modelToView(p1, a, b1);
//                } catch (BadLocationException ble) {
//                    s1 = null;
//                }
//                if (s1 == null) {
//                    // Assume extends left to right.
//                    Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a :
//                            a.getBounds();
//                    s1 = new Rectangle(alloc.x + alloc.width - 1, alloc.y,
//                            1, alloc.height);
//                }
//            }
//            else {
//                s1 = modelToView(p1, a, b1);
//            }
//            Rectangle r0 = s0.getBounds();
//            Rectangle r1 = (s1 instanceof Rectangle) ? (Rectangle) s1 :
//                    s1.getBounds();
//            if (r0.y != r1.y) {
//                // If it spans lines, force it to be the width of the view.
//                Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a :
//                        a.getBounds();
//                r0.x = alloc.x;
//                r0.width = alloc.width;
//            }
//            r0.add(r1);
////            r0.width += 20;
//            return r0;
//        }
//
//
//
//        public int getViewIndex(float x, float y, Shape allocation) {
//            for (int counter = getViewCount() - 1; counter >= 0; counter--) {
//                Shape childAllocation = getChildAllocation(counter, allocation);
//
//                if (childAllocation != null) {
//                    Rectangle rect = (childAllocation instanceof Rectangle) ?
//                            (Rectangle)childAllocation : childAllocation.getBounds();
//
//                    if (rect.contains(x, y)) {
//                        return counter + 1;
//                    }
//                }
//            }
//            return -1;
//        }
    }

    private class CenteredBoxView extends BoxView {
        private  CenteredBoxView(Element elem, int axis) {
            super(elem,axis);
        }
        protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {

            super.layoutMajorAxis(targetSpan, axis, offsets, spans);

            int offset = (getContainer().getHeight() - flatAutoResizeListener.getPreferredSize().height) / 2;

            for (int i = 0; i < offsets.length; i++) {
                offsets[i] += offset;
            }

        }
    }
}


