package com.github.bunnyi116.malilibui.gui;

import com.github.bunnyi116.malilibui.config.UiConfigs;

/**
 * 重制后配置界面的布局几何计算。
 *
 * <p>所有坐标均为 GUI 缩放后的坐标。屏幕被划分为：整体面板、可选的左侧导航栏、
 * 右侧内容区；malilib 的选项列表控件会被重新放置到内容区中。
 * 窗口较小时会自动切换为紧凑模式（减小边距、标题行高度与侧栏宽度），
 * 而不是退回原版布局。</p>
 *
 * <pre>
 * +-------------------------------------------------------------+
 * | 外边距                                                       |
 * |   +----------------+------------------------------------+  |
 * |   | 侧栏            | 内容区标题（标题、搜索框）           |  |
 * |   |  模组标题       |------------------------------------|  |
 * |   |  [ TAB 1 ]     |                                    |  |
 * |   |  [ TAB 2 ]     |      配置选项列表                   |  |
 * |   |  ...           |                                    |  |
 * |   +----------------+------------------------------------+  |
 * |                                                             |
 * +-------------------------------------------------------------+
 * </pre>
 */
public final class ConfigLayout {
    public static final int SIDEBAR_MIN = 90;
    public static final int SIDEBAR_MAX = 260;
    public static final int NAV_ITEM_H = 26;
    public static final int NAV_GAP = 2;

    public int screenW;
    public int screenH;

    /** 窗口较小时使用紧凑布局。 */
    public boolean compact;

    public int margin;
    public int pad;
    public int headerH;

    public int panelX;
    public int panelY;
    public int panelW;
    public int panelH;

    public boolean hasSidebar;
    public int sidebarX;
    public int sidebarY;
    public int sidebarW;
    public int sidebarH;

    public int contentX;
    public int contentY;
    public int contentW;
    public int contentH;

    public int listX;
    public int listY;
    public int listW;
    public int listH;

    public int navX;
    public int navY;
    public int navW;
    public int navItemH;
    public int navGap;

    private ConfigLayout() {
    }

    public static ConfigLayout compute(int screenW, int screenH, int tabCount) {
        ConfigLayout layout = new ConfigLayout();

        layout.screenW = screenW;
        layout.screenH = screenH;
        layout.compact = screenW < 520 || screenH < 340;

        layout.margin = layout.compact ? 6 : 14;
        layout.pad = layout.compact ? 6 : 10;
        layout.headerH = ConfigScreenManager.is26x() ? 30 : 28;

        layout.panelX = layout.margin;
        layout.panelY = layout.margin;
        layout.panelW = screenW - layout.margin * 2;
        layout.panelH = screenH - layout.margin * 2;

        layout.hasSidebar = tabCount > 0;

        int sidebarW = 0;

        if (layout.hasSidebar) {
            int configured = UiConfigs.Layout.SIDEBAR_WIDTH.getIntegerValue();
            // 大窗口下用户配置的宽度完全生效；
            // 只有窗口太窄、内容区会被挤压时才临时限制侧栏宽度。
            int minimum = SIDEBAR_MIN;
            int maxByScreen = Math.max(minimum, screenW - 300);

            sidebarW = Math.max(minimum, Math.min(Math.min(configured, SIDEBAR_MAX), maxByScreen));
        }

        layout.sidebarX = layout.panelX;
        layout.sidebarY = layout.panelY;
        layout.sidebarW = Math.max(70, sidebarW - 10);
        layout.sidebarH = layout.panelH;

        int divider = layout.hasSidebar ? 1 : 0;

        layout.contentX = layout.sidebarX + layout.sidebarW + 17;
        layout.contentY = layout.panelY;
        layout.contentW = layout.panelX + layout.panelW - layout.contentX;
        layout.contentH = layout.panelH;

        layout.listX = layout.contentX + layout.pad;
        layout.listY = layout.contentY + layout.headerH;
        layout.listW = Math.max(80, layout.contentW - layout.pad * 2);
        layout.listH = Math.max(40, layout.contentH - layout.headerH - layout.pad);

        int navTop = ConfigScreenManager.is26x()
                ? (layout.compact ? 46 : 62)
                : Math.max(0, layout.listY - layout.sidebarY - 2);

        layout.navX = layout.sidebarX + (layout.compact ? 8 : 10);
        layout.navY = layout.sidebarY + navTop;
        layout.navW = Math.max(60, layout.sidebarW - (layout.compact ? 16 : 20));
        layout.navGap = NAV_GAP;

        int itemH = NAV_ITEM_H;
        int available = layout.sidebarH - navTop - (layout.compact ? 36 : 54);

        if (tabCount > 0) {
            int needed = tabCount * (itemH + layout.navGap);

            if (needed > available) {
                itemH = Math.max(18, available / tabCount - layout.navGap);
            }
        }

        layout.navItemH = itemH;

        return layout;
    }

    public int navItemY(int index) {
        return this.navY + index * (this.navItemH + this.navGap);
    }

    public int navTotalHeight(int tabCount) {
        return tabCount * this.navItemH + Math.max(0, tabCount - 1) * this.navGap;
    }
}
