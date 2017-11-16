package com.mommoo.flat.component;

import com.mommoo.flat.frame.FlatFrame;
import com.mommoo.flat.text.textarea.FlatTextArea;
import com.mommoo.util.ColorManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * Created by mommoo on 2017-03-11.
 */
public class FlatScrollPane extends JScrollPane {
    private Color themeColor = ColorManager.getColorAccent();

    public FlatScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
        init();
    }

    public FlatScrollPane(Component view) {
        super(view);
        init();
    }

    public FlatScrollPane(int vsbPolicy, int hsbPolicy) {
        super(vsbPolicy, hsbPolicy);
        init();
    }

    public FlatScrollPane() {
        super();
        init();
    }

    public static void main(String[] ags) {
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setPreferredSize(new Dimension(400, 400));
//
//        JPanel content = new JPanel();
//        content.setBackground(Color.WHITE);
//        content.setPreferredSize(new Dimension(500, 500));
//        ;
//        content.add(new JTextArea("testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest"));
//        frame.add(new ModernScrollPane(content));
//
//        frame.pack();
//        frame.setVisible(true);
        FlatScrollPane flatScrollPane = new FlatScrollPane();
//        FlatTextArea flatLabel = new FlatTextArea();
        JTextArea flatLabel = new JTextArea("testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest");
//        flatLabel.setLineWrap(false);
//        flatLabel.setText("1\n2\n3\n4\n5\n6\n7\n8\n9\n1\n2\n3\n4\n5\n6\n7\n8\n9\n");
        flatLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        flatScrollPane.setViewportView(flatLabel);

        FlatFrame flatFrame = new FlatFrame();
        flatFrame.setTitle("FlatScrollPane Test");
        flatFrame.setSize(500,300);
        flatFrame.setLocationOnScreenCenter();
        flatFrame.getContainer().add(flatScrollPane);
        flatFrame.show();
    }

    public Color getThemeColor() {
        return this.themeColor;
    }

    public void setThemeColor(Color themeColor) {
        this.themeColor = themeColor;
    }

    public Color getVerticalScrollTrackColor() {
        return getVerticalScrollBar().getBackground();
    }

    public void setVerticalScrollTrackColor(Color trackColor) {
        getVerticalScrollBar().setOpaque(true);
        getVerticalScrollBar().setBackground(trackColor);
    }

    public Color getHorizontalScrollTrackColor() {
        return getHorizontalScrollBar().getBackground();
    }

    public void setHorizontalScrollTrackColor(Color trackColor) {
        getHorizontalScrollBar().setOpaque(true);
        getHorizontalScrollBar().setBackground(trackColor);
    }

    private void init() {
        setBorder(BorderFactory.createEmptyBorder());
        setVerticalScrollBar(new FlatScrollBar());
        setHorizontalScrollBar(new FlatScrollBar());
        setComponentZOrder(getVerticalScrollBar(), 0);
        setComponentZOrder(getViewport(), 1);
        setLayout(new ScrollPaneLayout(){
            @Override
            public void layoutContainer(Container parent) {
                JScrollPane scrollPane = (JScrollPane)parent;

                Rectangle availR = scrollPane.getBounds();
                availR.x = availR.y = 0;

                Insets insets = parent.getInsets();
                availR.x = insets.left;
                availR.y = insets.top;
                availR.width  -= insets.left + insets.right;
                availR.height -= insets.top  + insets.bottom;

                Rectangle vsbR = new Rectangle();
                vsbR.width  = 12;
                vsbR.height = availR.height;
                vsbR.x = availR.x + availR.width - vsbR.width;
                vsbR.y = availR.y;

                if(viewport != null) {
                    viewport.setBounds(availR);
                }
                if(vsb != null) {
                    vsb.setVisible(true);
                    vsb.setBounds(vsbR);
                }
            }
        });
    }

    @Override
    protected boolean isPaintingOrigin() {
        return true;
    }

    public static class ModernScrollPane extends JScrollPane {

        private static final long serialVersionUID = 8607734981506765935L;

        private static final int SCROLL_BAR_ALPHA_ROLLOVER = 100;
        private static final int SCROLL_BAR_ALPHA = 50;
        private static final int THUMB_SIZE = 8;
        private static final int SB_SIZE = 10;
        private static final Color THUMB_COLOR = Color.BLACK;

        public ModernScrollPane(Component view) {
            this(view, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
        }

        public ModernScrollPane(int vsbPolicy, int hsbPolicy) {
            this(null, vsbPolicy, hsbPolicy);
        }

        public ModernScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
            setBorder(null);

            // Set ScrollBar UI
            JScrollBar verticalScrollBar = getVerticalScrollBar();
            verticalScrollBar.setOpaque(false);
            setVerticalScrollBar(new JScrollBar(){
                @Override
                protected boolean isPaintingOrigin() {
                    return true;
                }

                @Override
                public boolean isPaintingTile() {
                    return true;
                }

                @Override
                public void setDoubleBuffered(boolean aFlag) {
                    super.setDoubleBuffered(true);
                }

                @Override
                public boolean isOpaque() {
                    return false;
                }
            });
            verticalScrollBar.setUI(new ModernScrollBarUI(this));

            JScrollBar horizontalScrollBar = getHorizontalScrollBar();
            horizontalScrollBar.setOpaque(false);
//            horizontalScrollBar.setUI(new ModernScrollBarUI(this));

            setLayout(new ScrollPaneLayout() {
                private static final long serialVersionUID = 5740408979909014146L;

                @Override
                public void layoutContainer(Container parent) {
/* Sync the (now obsolete) policy fields with the
         * JScrollPane.
         */
                    JScrollPane scrollPane = (JScrollPane) parent;
                    vsbPolicy = scrollPane.getVerticalScrollBarPolicy();
                    hsbPolicy = scrollPane.getHorizontalScrollBarPolicy();

                    Rectangle availR = scrollPane.getBounds();
                    availR.x = availR.y = 0;

                    Insets insets = parent.getInsets();
                    availR.x = insets.left;
                    availR.y = insets.top;
                    availR.width -= insets.left + insets.right;
                    availR.height -= insets.top + insets.bottom;

        /* Get the scrollPane's orientation.
         */
                    boolean leftToRight = scrollPane.getComponentOrientation().isLeftToRight();

        /* If there's a visible column header remove the space it
         * needs from the top of availR.  The column header is treated
         * as if it were fixed height, arbitrary width.
         */

                    Rectangle colHeadR = new Rectangle(0, availR.y, 0, 0);

                    if ((colHead != null) && (colHead.isVisible())) {
                        int colHeadHeight = Math.min(availR.height,
                                colHead.getPreferredSize().height);
                        colHeadR.height = colHeadHeight;
                        availR.y += colHeadHeight;
                        availR.height -= colHeadHeight;
                    }

        /* If there's a visible row header remove the space it needs
         * from the left or right of availR.  The row header is treated
         * as if it were fixed width, arbitrary height.
         */

                    Rectangle rowHeadR = new Rectangle(0, 0, 0, 0);

                    if ((rowHead != null) && (rowHead.isVisible())) {
                        int rowHeadWidth = Math.min(availR.width,
                                rowHead.getPreferredSize().width);
                        rowHeadR.width = rowHeadWidth;
                        availR.width -= rowHeadWidth;
                        if (leftToRight) {
                            rowHeadR.x = availR.x;
                            availR.x += rowHeadWidth;
                        } else {
                            rowHeadR.x = availR.x + availR.width;
                        }
                    }

        /* If there's a JScrollPane.viewportBorder, remove the
         * space it occupies for availR.
         */

                    Border viewportBorder = scrollPane.getViewportBorder();
                    Insets vpbInsets;
                    if (viewportBorder != null) {
                        vpbInsets = viewportBorder.getBorderInsets(parent);
                        availR.x += vpbInsets.left;
                        availR.y += vpbInsets.top;
                        availR.width -= vpbInsets.left + vpbInsets.right;
                        availR.height -= vpbInsets.top + vpbInsets.bottom;
                    } else {
                        vpbInsets = new Insets(0, 0, 0, 0);
                    }


        /* At this point availR is the space available for the viewport
         * and scrollbars. rowHeadR is correct except for its height and y
         * and colHeadR is correct except for its width and x.  Once we're
         * through computing the dimensions  of these three parts we can
         * go back and set the dimensions of rowHeadR.height, rowHeadR.y,
         * colHeadR.width, colHeadR.x and the bounds for the corners.
         *
         * We'll decide about putting up scrollbars by comparing the
         * viewport views preferred size with the viewports extent
         * size (generally just its size).  Using the preferredSize is
         * reasonable because layout proceeds top down - so we expect
         * the viewport to be laid out next.  And we assume that the
         * viewports layout manager will give the view it's preferred
         * size.  One exception to this is when the view implements
         * Scrollable and Scrollable.getViewTracksViewport{Width,Height}
         * methods return true.  If the view is tracking the viewports
         * width we don't bother with a horizontal scrollbar, similarly
         * if view.getViewTracksViewport(Height) is true we don't bother
         * with a vertical scrollbar.
         */

                    Component view = (viewport != null) ? viewport.getView() : null;
                    Dimension viewPrefSize =
                            (view != null) ? view.getPreferredSize()
                                    : new Dimension(0, 0);

                    Dimension extentSize =
                            (viewport != null) ? viewport.toViewCoordinates(availR.getSize())
                                    : new Dimension(0, 0);

                    boolean viewTracksViewportWidth = false;
                    boolean viewTracksViewportHeight = false;
                    boolean isEmpty = (availR.width < 0 || availR.height < 0);
                    Scrollable sv;
                    // Don't bother checking the Scrollable methods if there is no room
                    // for the viewport, we aren't going to show any scrollbars in this
                    // case anyway.
                    if (!isEmpty && view instanceof Scrollable) {
                        sv = (Scrollable) view;
                        viewTracksViewportWidth = sv.getScrollableTracksViewportWidth();
                        viewTracksViewportHeight = sv.getScrollableTracksViewportHeight();
                    } else {
                        sv = null;
                    }

        /* If there's a vertical scrollbar and we need one, allocate
         * space for it (we'll make it visible later). A vertical
         * scrollbar is considered to be fixed width, arbitrary height.
         */

                    Rectangle vsbR = new Rectangle(0, availR.y - vpbInsets.top, 0, 0);

                    boolean vsbNeeded;
                    if (isEmpty) {
                        vsbNeeded = false;
                    } else if (vsbPolicy == VERTICAL_SCROLLBAR_ALWAYS) {
                        vsbNeeded = true;
                    } else if (vsbPolicy == VERTICAL_SCROLLBAR_NEVER) {
                        vsbNeeded = false;
                    } else {  // vsbPolicy == VERTICAL_SCROLLBAR_AS_NEEDED
                        vsbNeeded = !viewTracksViewportHeight && (viewPrefSize.height > extentSize.height);
                    }


                    if ((vsb != null) && vsbNeeded) {
                        adjustForVSB(true, availR, vsbR, vpbInsets, leftToRight);
                        extentSize = viewport.toViewCoordinates(availR.getSize());
                    }

        /* If there's a horizontal scrollbar and we need one, allocate
         * space for it (we'll make it visible later). A horizontal
         * scrollbar is considered to be fixed height, arbitrary width.
         */

                    Rectangle hsbR = new Rectangle(availR.x - vpbInsets.left, 0, 0, 0);
                    boolean hsbNeeded;
                    if (isEmpty) {
                        hsbNeeded = false;
                    } else if (hsbPolicy == HORIZONTAL_SCROLLBAR_ALWAYS) {
                        hsbNeeded = true;
                    } else if (hsbPolicy == HORIZONTAL_SCROLLBAR_NEVER) {
                        hsbNeeded = false;
                    } else {  // hsbPolicy == HORIZONTAL_SCROLLBAR_AS_NEEDED
                        hsbNeeded = !viewTracksViewportWidth && (viewPrefSize.width > extentSize.width);
                    }

                    if ((hsb != null) && hsbNeeded) {
                        adjustForHSB(true, availR, hsbR, vpbInsets);

            /* If we added the horizontal scrollbar then we've implicitly
             * reduced  the vertical space available to the viewport.
             * As a consequence we may have to add the vertical scrollbar,
             * if that hasn't been done so already.  Of course we
             * don't bother with any of this if the vsbPolicy is NEVER.
             */
                        if ((vsb != null) && !vsbNeeded &&
                                (vsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {

                            extentSize = viewport.toViewCoordinates(availR.getSize());
                            vsbNeeded = viewPrefSize.height > extentSize.height;

                            if (vsbNeeded) {
                                adjustForVSB(true, availR, vsbR, vpbInsets, leftToRight);
                            }
                        }
                    }

        /* Set the size of the viewport first, and then recheck the Scrollable
         * methods. Some components base their return values for the Scrollable
         * methods on the size of the Viewport, so that if we don't
         * ask after resetting the bounds we may have gotten the wrong
         * answer.
         */

                    if (viewport != null) {
                        viewport.setBounds(availR);

                        if (sv != null) {
                            extentSize = viewport.toViewCoordinates(availR.getSize());

                            boolean oldHSBNeeded = hsbNeeded;
                            boolean oldVSBNeeded = vsbNeeded;
                            viewTracksViewportWidth = sv.
                                    getScrollableTracksViewportWidth();
                            viewTracksViewportHeight = sv.
                                    getScrollableTracksViewportHeight();
                            if (vsb != null && vsbPolicy == VERTICAL_SCROLLBAR_AS_NEEDED) {
                                boolean newVSBNeeded = !viewTracksViewportHeight &&
                                        (viewPrefSize.height > extentSize.height);
                                if (newVSBNeeded != vsbNeeded) {
                                    vsbNeeded = newVSBNeeded;
                                    adjustForVSB(vsbNeeded, availR, vsbR, vpbInsets,
                                            leftToRight);
                                    extentSize = viewport.toViewCoordinates
                                            (availR.getSize());
                                }
                            }
                            if (hsb != null && hsbPolicy == HORIZONTAL_SCROLLBAR_AS_NEEDED) {
                                boolean newHSBbNeeded = !viewTracksViewportWidth &&
                                        (viewPrefSize.width > extentSize.width);
                                if (newHSBbNeeded != hsbNeeded) {
                                    hsbNeeded = newHSBbNeeded;
                                    adjustForHSB(hsbNeeded, availR, hsbR, vpbInsets);
                                    if ((vsb != null) && !vsbNeeded &&
                                            (vsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {

                                        extentSize = viewport.toViewCoordinates
                                                (availR.getSize());
                                        vsbNeeded = viewPrefSize.height >
                                                extentSize.height;

                                        if (vsbNeeded) {
                                            adjustForVSB(true, availR, vsbR, vpbInsets,
                                                    leftToRight);
                                        }
                                    }
                                }
                            }
                            if (oldHSBNeeded != hsbNeeded ||
                                    oldVSBNeeded != vsbNeeded) {
                                viewport.setBounds(availR);
                                // You could argue that we should recheck the
                                // Scrollable methods again until they stop changing,
                                // but they might never stop changing, so we stop here
                                // and don't do any additional checks.
                            }
                        }
                    }

        /* We now have the final size of the viewport: availR.
         * Now fixup the header and scrollbar widths/heights.
         */
                    vsbR.height = availR.height + vpbInsets.top + vpbInsets.bottom;
                    hsbR.width = availR.width + vpbInsets.left + vpbInsets.right;
                    rowHeadR.height = availR.height + vpbInsets.top + vpbInsets.bottom;
                    rowHeadR.y = availR.y - vpbInsets.top;
                    colHeadR.width = availR.width + vpbInsets.left + vpbInsets.right;
                    colHeadR.x = availR.x - vpbInsets.left;

        /* Set the bounds of the remaining components.  The scrollbars
         * are made invisible if they're not needed.
         */

                    if (rowHead != null) {
                        rowHead.setBounds(rowHeadR);
                    }

                    if (colHead != null) {
                        colHead.setBounds(colHeadR);
                    }

                    if (vsb != null) {
                        if (vsbNeeded) {
                            if (colHead != null &&
                                    UIManager.getBoolean("ScrollPane.fillUpperCorner")) {
                                if ((leftToRight && upperRight == null) ||
                                        (!leftToRight && upperLeft == null)) {
                                    // This is used primarily for GTK L&F, which needs to
                                    // extend the vertical scrollbar to fill the upper
                                    // corner near the column header.  Note that we skip
                                    // this step (and use the default behavior) if the
                                    // user has set a custom corner component.
                                    vsbR.y = colHeadR.y;
                                    vsbR.height += colHeadR.height;
                                }
                            }
                            vsb.setVisible(true);
                            vsb.setBounds(vsbR);
                            SwingUtilities.getUnwrappedParent(scrollPane).revalidate();
                            SwingUtilities.getUnwrappedParent(scrollPane).repaint();
//                            scrollPane.repaint();
                        } else {
                            vsb.setVisible(false);
                        }
                    }

                    if (hsb != null) {
                        if (hsbNeeded) {
                            if (rowHead != null &&
                                    UIManager.getBoolean("ScrollPane.fillLowerCorner")) {
                                if ((leftToRight && lowerLeft == null) ||
                                        (!leftToRight && lowerRight == null)) {
                                    // This is used primarily for GTK L&F, which needs to
                                    // extend the horizontal scrollbar to fill the lower
                                    // corner near the row header.  Note that we skip
                                    // this step (and use the default behavior) if the
                                    // user has set a custom corner component.
                                    if (leftToRight) {
                                        hsbR.x = rowHeadR.x;
                                    }
                                    hsbR.width += rowHeadR.width;
                                }
                            }
                            hsb.setVisible(true);
                            hsb.setBounds(hsbR);
                        } else {
                            hsb.setVisible(false);
                        }
                    }

                    if (lowerLeft != null) {
                        lowerLeft.setBounds(leftToRight ? rowHeadR.x : vsbR.x,
                                hsbR.y,
                                leftToRight ? rowHeadR.width : vsbR.width,
                                hsbR.height);
                    }

                    if (lowerRight != null) {
                        lowerRight.setBounds(leftToRight ? vsbR.x : rowHeadR.x,
                                hsbR.y,
                                leftToRight ? vsbR.width : rowHeadR.width,
                                hsbR.height);
                    }

                    if (upperLeft != null) {
                        upperLeft.setBounds(leftToRight ? rowHeadR.x : vsbR.x,
                                colHeadR.y,
                                leftToRight ? rowHeadR.width : vsbR.width,
                                colHeadR.height);
                    }

                    if (upperRight != null) {
                        upperRight.setBounds(leftToRight ? vsbR.x : rowHeadR.x,
                                colHeadR.y,
                                leftToRight ? vsbR.width : rowHeadR.width,
                                colHeadR.height);
                    }
                }

                /**
                 * Adjusts the <code>Rectangle</code> <code>available</code> based on if
                 * the vertical scrollbar is needed (<code>wantsVSB</code>).
                 * The location of the vsb is updated in <code>vsbR</code>, and
                 * the viewport border insets (<code>vpbInsets</code>) are used to offset
                 * the vsb. This is only called when <code>wantsVSB</code> has
                 * changed, eg you shouldn't invoke adjustForVSB(true) twice.
                 */
                private void adjustForVSB(boolean wantsVSB, Rectangle available,
                                          Rectangle vsbR, Insets vpbInsets,
                                          boolean leftToRight) {
                    int oldWidth = vsbR.width;
                    if (wantsVSB) {
                        int vsbWidth = Math.max(0, Math.min(vsb.getPreferredSize().width,
                                available.width));

//                        available.width -= vsbWidth;
                        vsbR.width = vsbWidth;

                        if (leftToRight) {
                            vsbR.x = available.x + available.width - vsbWidth + vpbInsets.right;
                        } else {
                            vsbR.x = available.x - vpbInsets.left;
                            available.x += vsbWidth;
                        }
                    } else {
                        available.width += oldWidth;
                    }
                }

                /**
                 * Adjusts the <code>Rectangle</code> <code>available</code> based on if
                 * the horizontal scrollbar is needed (<code>wantsHSB</code>).
                 * The location of the hsb is updated in <code>hsbR</code>, and
                 * the viewport border insets (<code>vpbInsets</code>) are used to offset
                 * the hsb.  This is only called when <code>wantsHSB</code> has
                 * changed, eg you shouldn't invoked adjustForHSB(true) twice.
                 */
                private void adjustForHSB(boolean wantsHSB, Rectangle available,
                                          Rectangle hsbR, Insets vpbInsets) {
                    int oldHeight = hsbR.height;
                    if (wantsHSB) {
                        int hsbHeight = Math.max(0, Math.min(available.height,
                                hsb.getPreferredSize().height));

//                        available.height -= hsbHeight;
                        hsbR.y = available.y + available.height + vpbInsets.bottom;
                        hsbR.height = hsbHeight;
                    } else {
                        available.height += oldHeight;
                    }
                }
            });
            setComponentZOrder(getVerticalScrollBar(), 0);
            setComponentZOrder(getHorizontalScrollBar(), 1);
            setComponentZOrder(getViewport(), 2);

            viewport.setView(view);
        }

                        @Override
                protected boolean isPaintingOrigin() {
                    return true;
                }

        /**
         * Class extending the BasicScrollBarUI and overrides all necessary methods
         */
        private static class ModernScrollBarUI extends BasicScrollBarUI {

            private JScrollPane sp;

            public ModernScrollBarUI(ModernScrollPane sp) {
                this.sp = sp;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new InvisibleScrollBarButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new InvisibleScrollBarButton();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                Color color = null;
                JScrollBar sb = (JScrollBar) c;
                System.out.println(r);
                if (!sb.isEnabled() || r.width > r.height) {
                    return;
                } else if (isDragging) {
                    color = new Color(200, 200, 100, 200);
                } else if (isThumbRollover()) {
                    color = new Color(255, 255, 100, 200);
                } else {
                    color = new Color(220, 220, 200, 200);
                }
                g2.setPaint(color);
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.setPaint(Color.WHITE);
                g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.dispose();
            }

            @Override
            protected void setThumbBounds(int x, int y, int width, int height) {
                super.setThumbBounds(x, y, width, height);
                sp.repaint();
            }

            /**
             * Invisible Buttons, to hide scroll bar buttons
             */
            private static class InvisibleScrollBarButton extends JButton {

                private static final long serialVersionUID = 1552427919226628689L;

                private InvisibleScrollBarButton() {
                    setOpaque(false);
                    setFocusable(false);
                    setFocusPainted(false);
                    setBorderPainted(false);
                    setBorder(BorderFactory.createEmptyBorder());
                }
            }
        }
    }

    private class FlatScrollBar extends JScrollBar {

        FlatScrollBar() {
            setUI(new FlatScrollBarUI());
            setPreferredSize(new Dimension(10, 0));
            setOpaque(false);
        }
    }

    private class FlatScrollBarUI extends BasicScrollBarUI {

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color color;
            if (!c.isEnabled() || thumbBounds.width > thumbBounds.height) {
                return;
            } else if (isDragging) {
                color = themeColor.darker();
            } else if (isThumbRollover()) {
                color = themeColor.brighter();
            } else {
                color = themeColor;
            }

            g2.setPaint(color);
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 5, 5);
            g2.dispose();
        }

        @Override
        protected void setThumbBounds(int x, int y, int width, int height) {
            super.setThumbBounds(x, y, width, height);
            scrollbar.repaint();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            Dimension zeroDim = new Dimension(0, 0);
            button.setPreferredSize(zeroDim);
            button.setMinimumSize(zeroDim);
            button.setMaximumSize(zeroDim);
            return button;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }
//        @Override
//        protected JButton createIncreaseButton(int orientation) {
//            return new ArrowButton(orientation) {
//                @Override
//                protected void paintArrow(Graphics2D graphics2D,Dimension parentDimension, Path2D line) {
//                    double leftX = 0.0d, leftY = 0.0d;
//                    double topX = 0.0d,topY = 0.0d;
//                    double rightX = 0.0d ,rightY = 0.0d;
//                    double bottomX = 0.0d,bottomY = 0.0d;
//                    if(orientation == EAST){
//                        topX = parentDimension.width/4.0d;
//                        topY = parentDimension.height/4.0d;
//                        rightX = 3*parentDimension.width/4.0d;
//                        rightY = parentDimension.height/2.0d;
//                        bottomX = topX;
//                        bottomY = 3*parentDimension.height/4.0d;
//                        line.moveTo(topX,topY);
//                        line.lineTo(rightX,rightY);
//                        line.lineTo(bottomX,bottomY);
//                    }else if(orientation == SOUTH){
//                        leftX = parentDimension.width/4.0d;
//                        leftY = parentDimension.height/4.0d;
//                        bottomX = parentDimension.width/2.0d;
//                        bottomY = 3*parentDimension.height/4.0d;
//                        rightX = parentDimension.width - leftX;
//                        rightY = leftY;
//                        line.moveTo(leftX,leftY);
//                        line.lineTo(bottomX,bottomY);
//                        line.lineTo(rightX,rightY);
//                    }
//
//                    graphics2D.draw(line);
//                }
//            };
//        }
//
//        @Override
//        protected JButton createDecreaseButton(int orientation) {
//            return new ArrowButton(orientation) {
//                @Override
//                protected void paintArrow(Graphics2D graphics2D,Dimension parentDimension, Path2D line) {
//                    double leftX = 0.0d, leftY = 0.0d;
//                    double topX = 0.0d,topY = 0.0d;
//                    double rightX = 0.0d ,rightY = 0.0d;
//                    double bottomX = 0.0d,bottomY = 0.0d;
//                    if(orientation == WEST){
//                        leftX = parentDimension.width/4.0d;
//                        leftY = parentDimension.height/2.0d;
//                        topX = 3*parentDimension.width/4.0d;
//                        topY = parentDimension.height/4.0d;
//                        bottomX = topX;
//                        bottomY = 3*parentDimension.height/4.0d;
//                        line.moveTo(topX,topY);
//                        line.lineTo(leftX,leftY);
//                        line.lineTo(bottomX,bottomY);
//                    }else if(orientation == NORTH){
//                        leftX = parentDimension.width/4.0d;
//                        leftY = 3*parentDimension.height/4.0d;
//                        topX = parentDimension.width/2.0d;
//                        topY = parentDimension.height/4.0d;
//                        rightX = 3*parentDimension.width/4.0d;
//                        rightY = leftY;
//                        line.moveTo(leftX,leftY);
//                        line.lineTo(topX,topY);
//                        line.lineTo(rightX,rightY);
//                    }
//                    graphics2D.draw(line);
//                }
//            };
//        }
//
//        private abstract class ArrowButton extends JButton{
//            private final BasicStroke BASIC_STROKE = new BasicStroke(2.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
//
//            private ArrowButton(int orientation) {
//                setCursor(new Cursor(Cursor.HAND_CURSOR));
//                setBackground(Color.WHITE);
//                setBorderPainted(false);
//                setFocusPainted(false);
//                setModel(new DefaultButtonModel(){
//                    private static final long serialVersionUID = 1L;
//                    @Override
//                    public boolean isPressed() {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean isRollover() {
//                        return false;
//                    }
//
//                    @Override
//                    public void setRollover(boolean b) {
//
//                    }
//                });
//            }
//
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                Dimension d = getSize();
//                final Path2D LINE = new Path2D.Double();
//                Graphics2D graphics2D = (Graphics2D)g;
//                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                graphics2D.setColor(themeColor.darker());
//                graphics2D.setStroke(BASIC_STROKE);
//                paintArrow(graphics2D,d,LINE);
//                LINE.closePath();
//            }
//
//            @Override
//            public Dimension getPreferredLabelSize() {
//                return new Dimension(16, 16);
//            }
//
//            protected abstract void paintArrow(Graphics2D graphics2D, Dimension parentDimension ,Path2D line);
//        }
    }
}
