package com.github.bunnyi116.malilibui.mixin.accessor;

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.widgets.WidgetSearchBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WidgetSearchBar.class)
public interface WidgetSearchBarAccessor {
    @Accessor("searchBox")
    GuiTextFieldGeneric malilibui$getSearchBox();
}
