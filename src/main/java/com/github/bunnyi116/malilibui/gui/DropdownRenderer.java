package com.github.bunnyi116.malilibui.gui;

import java.util.List;

import com.github.bunnyi116.malilibui.mixin.accessor.WidgetDropDownListAccessor;
import fi.dy.masa.malilib.gui.GuiScrollBar;
import fi.dy.masa.malilib.gui.widgets.WidgetDropDownList;
import fi.dy.masa.malilib.render.GuiContext;
import fi.dy.masa.malilib.util.StringUtils;

/**
 * 把 MaLiLib 的下拉框（包括 MasaGadget 那一个）从原生黑框换成当前 UI 的深色主题。
 * 只负责绘制，命中判定和滚动仍走控件本身。
 */
public final class DropdownRenderer {
    private DropdownRenderer() {
    }

    public static void render(WidgetDropDownList<?> dropdown, GuiContext ctx,
                              int mouseX, int mouseY) {
        WidgetDropDownListAccessor access = (WidgetDropDownListAccessor) dropdown;
        int x = dropdown.getX();
        int y = dropdown.getY();
        int width = dropdown.getWidth();
        int height = dropdown.getHeight();
        boolean hovered = mouseX >= x && mouseX <= x + width
                && mouseY >= y && mouseY <= y + height;
        String selected = dropdown.getSelectedEntry() == null
                ? "" : access.malilibui$getDisplayString(dropdown.getSelectedEntry());

        // 收起状态：不透明主题底，盖住 MasaGadget 底层的原生黑框。
        UiTheme.drawDropdownField(ctx, x, y, width, Math.max(1, height), hovered);
        UiTheme.text(ctx, UiTheme.trim(ctx.fontRenderer(), selected, width - 22),
                x + 4, y + (height - 9) / 2 + 1, UiTheme.NAV_TEXT_ACTIVE, false);
        UiTheme.drawCenteredGlyph(ctx, access.malilibui$isOpen() ? "▲" : "▼",
                x + width - 18, y, 14, height, UiTheme.ACCENT_PINK, false);

        if (!access.malilibui$isOpen()) {
            return;
        }

        List<?> entries = access.malilibui$getFilteredEntries();
        int rowHeight = height;
        int visible = Math.min(access.malilibui$getMaxVisibleEntries(), entries.size());
        int listY = y + height;
        int barWidth = Math.max(1, access.malilibui$getScrollbarWidth());

        // 内部搜索框有内容时，画一个与主题一致的输入框，不再用原生黑底。
        String filter = access.malilibui$getSearchBar().textField().getValue();
        int filterH = (filter == null || filter.isEmpty())
                ? 0 : Math.max(16, StringUtils.getFontHeight() + 6);
        int listHeight = filterH + visible * rowHeight + 1;
        int firstRowY = listY + 1 + filterH;

        // 下拉列表本体：与搜索框一致的半透明黑底。
        UiTheme.fill(ctx, x, listY, width, listHeight, 0xCC000000);

        if (filterH > 0) {
            UiTheme.fill(ctx, x + 2, listY + 1, width - barWidth - 4, filterH, 0x99000000);
            UiTheme.text(ctx, UiTheme.trim(ctx.fontRenderer(), filter, width - barWidth - 14),
                    x + 6, listY + 4, UiTheme.TEXT_TITLE, false);
        }

        GuiScrollBar bar = access.malilibui$getScrollBar();
        int start = Math.max(0, bar.getValue());
        int end = Math.min(start + access.malilibui$getMaxVisibleEntries(), entries.size());
        int rowY = firstRowY;

        for (int i = start; i < end; ++i) {
            boolean rowHovered = mouseX >= x && mouseX < x + width - barWidth
                    && mouseY >= rowY && mouseY < rowY + rowHeight;
            int bg = (i & 1) == 0 ? 0x18000000 : 0x24000000;

            if (rowHovered) {
                bg = 0x44F2A7D8;
            }

            UiTheme.fill(ctx, x, rowY, width - barWidth, rowHeight, bg);
            UiTheme.text(ctx, UiTheme.trim(ctx.fontRenderer(), access.malilibui$getDisplayString(entries.get(i)),
                            width - barWidth - 10), x + 4, rowY + (rowHeight - 9) / 2 + 1,
                    rowHovered ? UiTheme.ACCENT_PINK : UiTheme.NAV_TEXT, false);
            rowY += rowHeight;
        }

        int contentHeight = Math.max(1, listHeight - 2);
        int totalHeight = Math.max(contentHeight, filterH + entries.size() * rowHeight);
        bar.render(ctx, mouseX, mouseY, 0.0F,
                x + width - barWidth - 1, listY + 1, barWidth, contentHeight, totalHeight);
    }
}
