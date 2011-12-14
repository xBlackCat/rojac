package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;

/**
 * @author xBlackCat
 */
public enum ThreadToolbarActions {
    NewThread {
        @Override
        JButton makeButton(TreeTableThreadView view) {
            return WindowsUtils.registerImageButton(view, "new_thread", view.new NewThreadAction());
        }
    },
    ToThreadRoot {
        @Override
        JButton makeButton(TreeTableThreadView view) {
            return WindowsUtils.registerImageButton(view, "to_root", view.new ToThreadRootAction());
        }
    },
    PreviousPost {
        @Override
        JButton makeButton(TreeTableThreadView view) {
            return WindowsUtils.registerImageButton(view, "prev", view.new PreviousAction());
        }
    },
    NextPost {
        @Override
        JButton makeButton(TreeTableThreadView view) {
            return WindowsUtils.registerImageButton(view, "next", view.new NextAction());
        }
    },
    PreviousUnread {
        @Override
        JButton makeButton(TreeTableThreadView view) {
            return WindowsUtils.registerImageButton(view, "prev_unread", view.new PreviousUnreadAction());
        }
    },
    NextUnread {
        @Override
        JButton makeButton(TreeTableThreadView view) {
            return WindowsUtils.registerImageButton(view, "next_unread", view.new NextUnreadAction());
        }
    },
    MarkSubTreeRead {
        @Override
        JButton makeButton(TreeTableThreadView view) {
            return WindowsUtils.registerImageButton(view, "mark_read_subtree", view.new MarkSubTreeReadAction());
        }
    },
    MarkThreadRead {
        @Override
        JButton makeButton(TreeTableThreadView view) {
            return WindowsUtils.registerImageButton(view, "mark_read_thread", view.new MarkWholeThreadReadAction());
        }
    },
    IgnoreUnread {
        @Override
        JButton makeButton(TreeTableThreadView view) {
            return WindowsUtils.registerImageButton(view, "ignore_unread", view.new IgnoreUnreadAction());
        }
    };

    abstract JButton makeButton(TreeTableThreadView view);
}
