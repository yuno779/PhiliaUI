package com.github.bunnyi116.malilibui.mixin.accessor;

import java.util.List;

import fi.dy.masa.malilib.gui.GuiScrollBar;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.widgets.WidgetDropDownList;
import fi.dy.masa.malilib.gui.wrappers.TextFieldWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WidgetDropDownList.class)
public interface WidgetDropDownListAccessor {
    @Accessor("isOpen")
    boolean malilibui$isOpen();

    @Accessor("maxHeight")
    int malilibui$getMaxHeight();

    @Accessor("scrollbarWidth")
    int malilibui$getScrollbarWidth();

    @Accessor("filteredEntries")
    List<?> malilibui$getFilteredEntries();

    @Accessor("maxVisibleEntries")
    int malilibui$getMaxVisibleEntries();

    @Accessor("scrollBar")
    GuiScrollBar malilibui$getScrollBar();

    @Accessor("searchBar")
    TextFieldWrapper<GuiTextFieldGeneric> malilibui$getSearchBar();

    @Invoker("getDisplayString")
    String malilibui$getDisplayString(Object entry);
}
