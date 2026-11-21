package com.github.bunnyi116.malilibui.gui;

import fi.dy.masa.malilib.render.GuiContext;
import fi.dy.masa.malilib.render.RenderUtils;
import net.minecraft.client.gui.Font;

import com.github.bunnyi116.malilibui.config.UiConfigs;

/**
 * 重制后的 masa 配置界面所使用的集中配色与底层绘制工具。
 *
 * <p>这里只使用纯色矩形、渐变与线条；面板几何保持像素对齐并使用原版字体，
 * 使整体风格仍然符合 Minecraft，同时比原版 masa 界面更干净整洁。</p>
 */
public final class UiTheme {
    private UiTheme() {
    }

    /* ------------------------------------------------------------------ *
     *  调色板（ARGB）
     * ------------------------------------------------------------------ */

    // 面板后方的全屏压暗遮罩
    public static final int OVERLAY = 0x8C121017;

    // 主面板
    public static final int PANEL_TOP = 0xB81E212C;
    public static final int PANEL_BOTTOM = 0xAC161821;
    public static final int PANEL_BORDER = 0xFF0A0B10;
    public static final int PANEL_INNER_GLOW = 0xFF3B415A;

    // 侧栏
    public static final int SIDEBAR_TOP = 0xBC161823;
    public static final int SIDEBAR_BOTTOM = 0xB00E0F16;
    public static final int SIDEBAR_DIVIDER = 0xFF2A2F42;

    // 内容区
    public static final int CONTENT_HEADER_LINE = 0xFF2A2F42;
    public static final int LIST_BG = 0x30000000;
    public static final int CONTENT_WELL_BG = 0x40000000;
    public static final int CONTENT_WELL_BORDER = 0xFF232838;

    // 搜索框 / 文本输入区域
    public static final int FIELD_BG = 0xD8141722;
    public static final int FIELD_BG_HOVER = 0xE0191D2B;
    public static final int FIELD_BORDER = 0xFF2C3242;
    public static final int FIELD_BORDER_HOVER = 0xFF4A5578;
    public static final int FIELD_HIGHLIGHT = 0x0FFFFFFF;

    // 导航 TAB
    public static final int NAV_TEXT = 0xFFA9B1C9;
    public static final int NAV_TEXT_HOVER = 0xFFFFFFFF;
    public static final int NAV_TEXT_ACTIVE = 0xFFFFFFFF;
    public static final int NAV_ACTIVE_TOP = 0xFF2E3A64;
    public static final int NAV_ACTIVE_BOTTOM = 0xFF212A47;
    /** 昔涟粉：替换原先所有蓝色竖线/强调线。 */
    public static final int ACCENT_PINK = 0xFFF2A7D8;
    public static final int NAV_ACTIVE_EDGE = ACCENT_PINK;
    public static final int NAV_ACTIVE_GLOW = 0x336C8CFF;
    public static final int NAV_HOVER_TOP = 0x1AFFFFFF;
    public static final int NAV_HOVER_BOTTOM = 0x0DFFFFFF;

    // 文字
    public static final int TEXT_TITLE = 0xFFFFFFFF;
    public static final int TEXT_SUB = 0xFF8B93AD;
    public static final int TEXT_ACCENT = ACCENT_PINK;
    /**
     * 禁用状态动作按钮的文字颜色（刻意降低对比度）。
     */
    public static final int TEXT_DISABLED = 0xFF69718A;

    // 滚动条（供主题化列表使用）
    public static final int SCROLL_TRACK = 0x40000000;
    /** 常态灰粉，悬停亮粉。 */
    public static final int SCROLL_THUMB = 0xFF9A8492;
    public static final int SCROLL_THUMB_HOVER = ACCENT_PINK;

    /* ------------------------------------------------------------------ *
     *  随配置变化的颜色（半透明背景开关）
     * ------------------------------------------------------------------ */

    public static boolean isSemiTransparent() {
        return UiConfigs.General.SEMI_TRANSPARENT.getBooleanValue();
    }

    /**
     * 背景不透明度（30-100，越大越不透明）。
     */
    private static int opacityPercent() {
        return UiConfigs.General.BACKGROUND_OPACITY.getIntegerValue();
    }

    private static int alphaOf(int percent) {
        int clamped = Math.max(0, Math.min(100, percent));

        return clamped * 255 / 100;
    }

    private static int withAlpha(int color, int alpha) {
        return (alpha << 24) | (color & 0xFFFFFF);
    }

    public static int overlayColor() {
        if (!isSemiTransparent()) {
            return 0xE6121017;
        }

        return withAlpha(OVERLAY, alphaOf(opacityPercent() - 20));
    }

    public static int panelTopColor() {
        if (!isSemiTransparent()) {
            return 0xF21E212C;
        }

        return withAlpha(PANEL_TOP, alphaOf(opacityPercent()));
    }

    public static int panelBottomColor() {
        if (!isSemiTransparent()) {
            return 0xF0161821;
        }

        return withAlpha(PANEL_BOTTOM, alphaOf(opacityPercent() - 5));
    }

    public static int sidebarTopColor() {
        if (!isSemiTransparent()) {
            return 0xF0161823;
        }

        return withAlpha(SIDEBAR_TOP, alphaOf(opacityPercent() + 2));
    }

    public static int sidebarBottomColor() {
        if (!isSemiTransparent()) {
            return 0xF00E0F16;
        }

        return withAlpha(SIDEBAR_BOTTOM, alphaOf(opacityPercent() - 3));
    }

    /* ------------------------------------------------------------------ *
     *  基础绘制
     * ------------------------------------------------------------------ */

    public static void fill(GuiContext ctx, int x, int y, int width, int height, int color) {
        if (width <= 0 || height <= 0) {
            return;
        }

        RenderUtils.drawRect(ctx, x, y, width, height, color);
    }

    public static void gradient(GuiContext ctx, int x, int y, int width, int height, int topColor, int bottomColor) {
        if (width <= 0 || height <= 0) {
            return;
        }

        RenderUtils.drawGradientRectBatched(ctx, x, y, x + width, y + height, topColor, bottomColor);
    }

    public static void outline(GuiContext ctx, int x, int y, int width, int height, int color) {
        if (width <= 0 || height <= 0) {
            return;
        }

        RenderUtils.drawOutline(ctx, x, y, width, height, color);
    }

    public static void hline(GuiContext ctx, int x, int y, int width, int color) {
        fill(ctx, x, y, width, 1, color);
    }

    public static void vline(GuiContext ctx, int x, int y, int height, int color) {
        fill(ctx, x, y, 1, height, color);
    }

    /**
     * 柔和投影：若干层向外扩散的半透明矩形。
     */
    public static void shadow(GuiContext ctx, int x, int y, int width, int height) {
        for (int i = 4; i >= 1; --i) {
            int alpha = 0x10 + (4 - i) * 0x0A;
            fill(ctx, x - i, y - i + 2, width + i * 2, height + i * 2, (alpha << 24));
        }
    }

    public static void text(GuiContext ctx, String text, int x, int y, int color, boolean shadow) {
        if (text == null || text.isEmpty()) {
            return;
        }

        if (shadow) {
            ctx.drawString(ctx.fontRenderer(), text, x, y, color, true);
        } else {
            ctx.drawString(ctx.fontRenderer(), text, x, y, color, false);
        }
    }

    public static void textCentered(GuiContext ctx, String text, int centerX, int y, int color, boolean shadow) {
        if (text == null || text.isEmpty()) {
            return;
        }

        Font font = ctx.fontRenderer();
        int x = centerX - font.width(text) / 2;

        if (shadow) {
            ctx.drawString(font, text, x, y, color, true);
        } else {
            ctx.drawString(font, text, x, y, color, false);
        }
    }

    /**
     * 按最大宽度裁剪标签，超出时追加省略号。
     */
    public static String trim(Font font, String text, int maxWidth) {
        if (text == null) {
            return "";
        }

        if (maxWidth <= 0 || font.width(text) <= maxWidth) {
            return text;
        }

        String ellipsis = "...";
        int ellipsisWidth = font.width(ellipsis);
        int i = text.length();

        while (i > 0 && font.width(text.substring(0, i)) + ellipsisWidth > maxWidth) {
            --i;
        }

        return text.substring(0, Math.max(0, i)) + ellipsis;
    }

    /* ------------------------------------------------------------------ *
     *  组合绘制
     * ------------------------------------------------------------------ */

    /**
     * 绘制左侧导航栏中的一个 TAB。
     *
     * @param active  当前选中的 TAB（以高亮样式绘制）
     * @param hovered 鼠标是否悬停在该 TAB 上
     */
    public static void drawNavTab(GuiContext ctx, int x, int y, int width, int height,
                                  String label, boolean active, boolean hovered) {
        if (width <= 0 || height <= 0) {
            return;
        }

        if (active) {
            // 选中项背景跟随背景不透明度，避免高透明背景下出现突兀的实心色块。
            int navAlpha = isSemiTransparent() ? Math.min(255, alphaOf(opacityPercent()) + 20) : 0xFF;
            gradient(ctx, x, y, width, height,
                    withAlpha(NAV_ACTIVE_TOP, navAlpha),
                    withAlpha(NAV_ACTIVE_BOTTOM, Math.max(0, navAlpha - 14)));
            // 左侧强调条
            fill(ctx, x, y + 4, 3, height - 8, NAV_ACTIVE_EDGE);
            // 右侧外发光，使选中项从导航栏中浮起
            vline(ctx, x + width - 1, y + 2, height - 4, NAV_ACTIVE_GLOW);
        } else if (hovered) {
            gradient(ctx, x, y, width, height, NAV_HOVER_TOP, NAV_HOVER_BOTTOM);
            fill(ctx, x, y + height / 2 - 6, 2, 12, 0x66A9B1C9);
        }

        int color = active ? NAV_TEXT_ACTIVE : (hovered ? NAV_TEXT_HOVER : NAV_TEXT);
        String text = trim(ctx.fontRenderer(), label, width - 22);
        int textY = y + (height - 8) / 2;

        text(ctx, text, x + 12, textY, color, true);
    }

    /**
     * 绘制可折叠分组标题右侧的折叠指示三角（带阴影，便于辨认）。
     *
     * @param centerX   三角的水平中心
     * @param centerY   三角的垂直中心
     * @param size      三角的基准尺寸（像素）
     * @param collapsed true 表示当前已折叠（绘制 ▶），false 表示展开（绘制 ▼）
     */
    public static void drawFoldArrow(GuiContext ctx, int centerX, int centerY, int size,
                                     boolean collapsed, int color) {
        // 深色阴影，提升在透明背景上的可读性。
        drawFoldArrowShape(ctx, centerX + 1, centerY + 1, size, collapsed, 0x66000000);
        drawFoldArrowShape(ctx, centerX, centerY, size, collapsed, color);
    }

    private static void drawFoldArrowShape(GuiContext ctx, int centerX, int centerY, int size,
                                           boolean collapsed, int color) {
        int half = Math.max(1, size / 2);

        if (collapsed) {
            // ▶：half 列，列高从 size 递减（垂直居中）
            int startX = centerX - half / 2;

            for (int i = 0; i < half; i++) {
                int h = Math.max(1, size - i * 2);
                fill(ctx, startX + i, centerY - h / 2, 1, h, color);
            }
        } else {
            // ▼：half 行，行宽从 size 递减（水平居中）
            int startY = centerY - half / 2;

            for (int i = 0; i < half; i++) {
                int w = Math.max(1, size - i * 2);
                fill(ctx, centerX - w / 2, startY + i, w, 1, color);
            }
        }
    }

    /**
     * 绘制侧栏内部的小号“分组标题”。
     */
    public static void drawSectionCaption(GuiContext ctx, int x, int y, int width, String caption) {
        text(ctx, trim(ctx.fontRenderer(), caption, width), x, y, TEXT_SUB, false);
    }

    /**
     * 绘制配置列表搜索栏使用的深色搜索框背景。
     */
    public static void drawSearchField(GuiContext ctx, int x, int y, int width, int height,
                                       boolean hovered) {
        if (width <= 0 || height <= 0) {
            return;
        }

        fill(ctx, x, y, width, height, hovered ? FIELD_BG_HOVER : FIELD_BG);
        outline(ctx, x, y, width, height, hovered ? FIELD_BORDER_HOVER : FIELD_BORDER);
    }

    /**
     * 绘制承载选项列表 / 渲染层控件的内嵌凹槽面板。
     */
    public static void drawContentWell(GuiContext ctx, int x, int y, int width, int height) {
        if (width <= 0 || height <= 0) {
            return;
        }

        fill(ctx, x, y, width, height, 0x1A000000);
        outline(ctx, x, y, width, height, 0x33FFFFFF);
    }

    /**
     * 原版浅灰按钮纹理的深色主题替代绘制。
     */
    public static void drawActionButton(GuiContext ctx, int x, int y, int width, int height,
                                        boolean hovered, boolean enabled) {
        if (width <= 0 || height <= 0) {
            return;
        }

        int top;
        int bottom;
        int border;
        int highlight;

        if (!enabled) {
            // 禁用态：平面、极暗、低对比，明确表示“当前不可用”。
            top = 0xFF14161D;
            bottom = 0xFF111318;
            border = 0xFF1D212B;
            highlight = 0x06FFFFFF;
        } else if (hovered) {
            top = 0xFF46527A;
            bottom = 0xFF333C58;
            border = 0xFF93ABFF;
            highlight = 0x3DFFFFFF;
        } else {
            // 可用态：明显比禁用态更亮，并带有凸起质感。
            top = 0xFF2F3750;
            bottom = 0xFF232A3C;
            border = 0xFF4A5578;
            highlight = 0x1AFFFFFF;
        }

        gradient(ctx, x, y, width, height, top, bottom);
        outline(ctx, x, y, width, height, border);
        hline(ctx, x + 1, y + 1, width - 2, highlight);
    }

    /**
     * 文本框 / 下拉框的深色主题背景。
     */
    public static void drawFieldBackground(GuiContext ctx, int x, int y, int width, int height,
                                           boolean focused) {
        if (width <= 0 || height <= 0) {
            return;
        }

        fill(ctx, x, y, width, height, focused ? 0xFF14161E : 0xFF1A1D27);
        outline(ctx, x, y, width, height, focused ? TEXT_ACCENT : 0xFF2E3444);
    }
}
