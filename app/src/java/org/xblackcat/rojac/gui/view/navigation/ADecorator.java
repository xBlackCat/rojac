package org.xblackcat.rojac.gui.view.navigation;

/**
 * @author xBlackCat Date: 22.07.11
 */
abstract class ADecorator {
    protected final IModelControl modelControl;

    ADecorator(IModelControl modelControl) {
        this.modelControl = modelControl;
    }

    abstract AnItem[] getItemsList();
}
