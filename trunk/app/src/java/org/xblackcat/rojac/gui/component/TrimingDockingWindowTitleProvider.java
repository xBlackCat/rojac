package org.xblackcat.rojac.gui.component;

import net.infonode.docking.AbstractTabWindow;
import net.infonode.docking.DockingWindow;
import net.infonode.docking.View;
import net.infonode.docking.title.DockingWindowTitleProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TrimingDockingWindowTitleProvider implements DockingWindowTitleProvider, Serializable {
    private static final long serialVersionUID = 1;

    private int maxLength;

    /**
     * Constructor.
     *
     * @param maxLength if the title exceeds this length no more view titles are added to it
     */
    public TrimingDockingWindowTitleProvider(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getTitle(DockingWindow window) {
        List<String> viewTitles = new ArrayList<>();
        List<Boolean> viewPrimary = new ArrayList<>();
        getViews(window, viewTitles, viewPrimary, true);

        int length = 0;

        for (int i = 0; i < viewTitles.size(); i++) {
            if (viewPrimary.get(i)) {
                length += viewTitles.get(i).length();
            }
        }

        StringBuilder title = new StringBuilder(40);
        int count = 0;

        int i = 0;
        while (i < viewTitles.size() && title.length() < maxLength) {
            boolean primary = viewPrimary.get(i);

            if (primary || length < maxLength) {
                if (title.length() > 0) {
                    title.append(", ");
                }

                title.append(viewTitles.get(i));
                count++;

                if (!primary) {
                    length += viewTitles.get(i).length();
                }
            }
            i++;
        }

        if (title.length() > maxLength) {
            title.setLength(maxLength - 3);
            title.append("...");
        }

        if (count < viewTitles.size()) {
            title.append(", ...");
        }

        return title.toString();
    }

    private void getViews(DockingWindow window, List<String> viewTitles, List<Boolean> viewPrimary, boolean primary) {
        if (window != null) {
            if (window instanceof View) {
                viewTitles.add(((View) window).getViewProperties().getTitle());
                viewPrimary.add(primary);
            } else if (window instanceof AbstractTabWindow) {
                DockingWindow selected = ((AbstractTabWindow) window).getSelectedWindow();

                for (int i = 0; i < window.getChildWindowCount(); i++) {
                    getViews(window.getChildWindow(i), viewTitles, viewPrimary, selected == window.getChildWindow(i) && primary);
                }
            } else {
                for (int i = 0; i < window.getChildWindowCount(); i++) {
                    getViews(window.getChildWindow(i), viewTitles, viewPrimary, primary);
                }
            }
        }
    }

}
