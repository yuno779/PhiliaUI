package com.github.bunnyi116.malilibui.mixin.accessor;

import fi.dy.masa.malilib.gui.widgets.WidgetDropDownList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WidgetDropDownList.class)
public interface WidgetDropDownListAccessor {
    @Accessor("isOpen")
    boolean malilibui$isOpen();

    @Accessor("maxHeight")
    int malilibui$getMaxHeight();

    @Accessor("scrollbarWidth")
    int malilibui$getScrollbarWidth();
}
