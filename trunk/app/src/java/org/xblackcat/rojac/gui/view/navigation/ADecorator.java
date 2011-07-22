package org.xblackcat.rojac.gui.view.navigation;

/**
 * @author xBlackCat Date: 22.07.11
 */
abstract class ADecorator {
    protected final AModelControl modelControl;

    ADecorator(AModelControl modelControl) {
        this.modelControl = modelControl;
    }

    abstract AnItem[] getItemsList();
}
