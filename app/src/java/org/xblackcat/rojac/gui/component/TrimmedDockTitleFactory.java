package org.xblackcat.rojac.gui.component;

import bibliothek.gui.dock.themes.basic.BasicDockTitle;
import bibliothek.gui.dock.themes.basic.BasicStationTitle;
import bibliothek.gui.dock.title.DockTitleFactory;
import bibliothek.gui.dock.title.DockTitleRequest;

import java.io.Serializable;

public class TrimmedDockTitleFactory implements DockTitleFactory, Serializable {
    private static final long serialVersionUID = 1;

    private int maxLength;

    /**
     * Constructor.
     *
     * @param maxLength if the title exceeds this length no more view titles are added to it
     */
    public TrimmedDockTitleFactory(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void install(DockTitleRequest request) {
    }

    @Override
    public void request(DockTitleRequest request) {
        if (request.getTarget().asDockStation() == null) {
            request.answer(
                    new BasicDockTitle(request.getTarget(), request.getVersion()) {
                        @Override
                        protected void updateText() {
                            final StringBuilder title = new StringBuilder(getDockable().getTitleText());

                            if (title.length() > maxLength) {
                                title.setLength(maxLength - 3);
                                title.append("...");
                            }

                            setText(title.toString());
                        }
                    }
            );
        } else {
            request.answer(new BasicStationTitle(request.getTarget(), request.getVersion()));
        }
    }

    @Override
    public void uninstall(DockTitleRequest request) {
    }
}
