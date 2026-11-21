package com.github.bunnyi116.malilibui.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListBase;

/** Per-screen mutable state for the restyled config GUI. */
public final class ConfigScreenState {
    public final GuiBase screen;
    public ConfigLayout layout;
    public final List<ButtonBase> navButtons = new ArrayList<>();
    public WidgetListBase<?, ?> listWidget;
    public final Set<Object> positionedControls = Collections.newSetFromMap(new IdentityHashMap<>());
    public boolean ready;

    /** Left tree rows, drawn directly and hit-tested by coordinates. */
    public final List<TreeRow> treeRows = new ArrayList<>();

    public int treeScroll;
    public int treeContentH;
    public boolean autoExpanded;

    /** Scroll to the current mod when a new screen is opened. */
    public boolean scrollToCurrent = true;

    /** Current tab index, used for the left highlight. */
    public int selectedTabIndex;

    /** 2.0 top-bar search field and current query. */
    public GuiTextFieldGeneric searchField;
    public GuiTextFieldGeneric nativeSearchBox;
    public String searchQuery = "";
    public String lastNativeSearch = "";
    public boolean searchActive;

    public record TreeRow(int x, int y, int w, int h, String name, String version,
                          boolean selected, boolean tab, String modId, int tabIndex) {
        public boolean contains(double mouseX, double mouseY) {
            return mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h;
        }
    }

    public ConfigScreenState(GuiBase screen) {
        this.screen = screen;
        this.treeScroll = 0;
    }
}
