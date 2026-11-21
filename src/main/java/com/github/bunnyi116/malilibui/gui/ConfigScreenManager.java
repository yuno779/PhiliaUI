package com.github.bunnyi116.malilibui.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.GuiRenderLayerEditBase;
import fi.dy.masa.malilib.gui.GuiScrollBar;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import fi.dy.masa.malilib.gui.widgets.WidgetDropDownList;
import fi.dy.masa.malilib.gui.widgets.WidgetListBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptionsBase;
import fi.dy.masa.malilib.gui.wrappers.TextFieldWrapper;
import fi.dy.masa.malilib.render.GuiContext;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.data.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import com.github.bunnyi116.malilibui.MalilibUiMod;
import com.github.bunnyi116.malilibui.config.UiConfigs;
import com.github.bunnyi116.malilibui.mixin.accessor.ButtonBaseAccessor;
import com.github.bunnyi116.malilibui.mixin.accessor.GuiBaseAccessor;
import com.github.bunnyi116.malilibui.mixin.accessor.GuiConfigsBaseAccessor;
import com.github.bunnyi116.malilibui.mixin.accessor.GuiListBaseAccessor;
import com.github.bunnyi116.malilibui.mixin.accessor.WidgetListBaseAccessor;
import com.github.bunnyi116.malilibui.mixin.accessor.WidgetListConfigOptionsBaseAccessor;

/**
 * 本模组的核心：接收 malilib 的原始界面，重新排布为
 * “左侧 TAB 导航 + 右侧内容区”，并绘制新的界面装饰。
 *
 * <p>会接管两类界面：</p>
 * <ul>
 *   <li>{@link GuiConfigsBase}：常规 masa 配置界面。选项列表控件会被移动
 *       （并在需要时重建）到内容区，搜索框与配置行随之重新布局。</li>
 *   <li>{@link GuiRenderLayerEditBase}：“渲染层”编辑器界面（例如 litematica 的
 *       {@code GuiRenderLayer}）。TAB 按钮被移到左侧导航栏，其余控件整体平移到内容区。</li>
 * </ul>
 *
 * <p>原始 TAB 按钮对象保持存活，仅移动位置；其点击回调仍执行原来的切换逻辑。
 * 由于 masa 模组切换 TAB 时会再次调用 {@code initGui()}，
 * 布局会在每帧渲染前按需重新应用。</p>
 */
public final class ConfigScreenManager {
    /**
     * 极小窗口的保护阈值：低于此尺寸时才放弃重制布局（正常缩放不会触发）。
     */
    private static final int MIN_SCREEN_W = 240;
    private static final int MIN_SCREEN_H = 160;

    /**
     * 位于原顶部条中、高度不超过该值的按钮被视为 TAB 按钮。
     */
    private static final int TAB_STRIP_MAX_Y = 46;
    private static final int TAB_MAX_HEIGHT = 22;

    /**
     * 渲染层控件在原版界面中的初始位置。
     */
    private static final int RENDER_LAYER_CONTROLS_X = 10;
    private static final int RENDER_LAYER_CONTROLS_Y = 60;

    /**
     * 内容“凹槽”面板在受管控件边界外扩的像素数。
     */
    private static final int WELL_MARGIN = 6;

    private static final Map<Screen, ConfigScreenState> STATES = new WeakHashMap<>();

    /** 记录渲染层输入框的原始坐标，每次布局都按原始坐标重新偏移，避免漂移与新控件漏移。 */
    private static final Map<GuiTextFieldGeneric, int[]> STANDALONE_FIELD_ORIGINS = new WeakHashMap<>();

    /** 是否有滑块正在被拖动（拖动期间冻结布局重建，避免拖拽被中断）。 */
    private static boolean sliderDragging;

    /** 最近一次滑条渲染的时间戳（用于判断拖拽是否仍在进行）。 */
    private static long lastSliderRenderTime;

    /** 超过该时间没有滑条渲染，就认为拖拽已经结束（避免冻结卡死）。 */
    private static final long SLIDER_DRAG_TIMEOUT_MS = 200L;

    private ConfigScreenManager() {
    }

    public static void setSliderDragging(boolean dragging) {
        sliderDragging = dragging;

        if (dragging) {
            lastSliderRenderTime = System.currentTimeMillis();
        }
    }

    public static void markSliderRendering() {
        lastSliderRenderTime = System.currentTimeMillis();
    }

    public static boolean isSliderDragging() {
        if (sliderDragging && System.currentTimeMillis() - lastSliderRenderTime > SLIDER_DRAG_TIMEOUT_MS) {
            // 滑条已不再渲染（例如界面重建导致控件销毁），视为拖拽结束，
            // 避免布局冻结卡死导致界面不再更新。
            sliderDragging = false;
        }

        return sliderDragging;
    }

    /* ------------------------------------------------------------------ *
     *  布局处理（每帧为受管界面调用）
     * ------------------------------------------------------------------ */

    public static void ensureLayout(GuiBase screen) {
        if (isManageDisabled(screen)) {
            // 禁用重制 / 窗口极小时不应用布局；原版恢复在客户端 tick 中处理。
            return;
        }

        if (isSliderDragging() && STATES.containsKey(screen)) {
            // 拖动滑条（例如侧栏宽度）期间冻结布局应用，避免列表重建打断拖拽；
            // 松手后的下一帧会自动应用新布局。
            return;
        }

        if (screen instanceof GuiConfigsBase configs) {
            ensureConfigsLayout(configs);
        } else if (screen instanceof GuiRenderLayerEditBase) {
            ensureRenderLayerLayout(screen);
        }
    }

    /**
     * 客户端 tick：处理禁用重制 / 极小窗口时的原版恢复。
     *
     * <p>恢复需要重建界面（{@code initGui()}），必须在 tick 阶段执行而不是渲染阶段，
     * 否则界面可能停留在旧布局（与切换 TAB 的路径保持一致）。</p>
     */
    public static void onClientTick() {
        Screen current = GuiUtils.getCurrentScreen();

        if (!(current instanceof GuiBase screen) || !STATES.containsKey(screen)) {
            return;
        }

        if (isManageDisabled(screen)) {
            restoreVanillaLayout(screen);
            return;
        }

        // 动态可见性变化（某个选项切换后，依赖它的选项需要显示 / 隐藏）→ 重建列表。
        if (current instanceof GuiConfigsBase && ConfigEntryFilter.hasVisibilityChanged()) {
            refreshCurrentConfigScreen();
        }
    }

    /**
     * 重建当前配置界面（延迟到下一 tick，避免在点击 / 渲染流程中重建界面）。
     *
     * <p>重建前记录列表的滚动位置，重建后恢复，避免折叠 / 显示隐藏后列表跳回顶部。</p>
     */
    public static void refreshCurrentConfigScreen() {
        Screen current = GuiUtils.getCurrentScreen();

        if (!(current instanceof GuiBase screen)) {
            return;
        }

        Minecraft.getInstance().execute(() -> {
            int scrolled = getScrollValue(screen);
            screen.initGui();
            setScrollValue(screen, scrolled);
        });
    }

    private static int getScrollValue(GuiBase screen) {
        if (screen instanceof GuiListBase<?, ?, ?> list) {
            WidgetListBase<?, ?> widget = ((GuiListBaseAccessor) list).malilibui$getListWidget();

            if (widget != null) {
                return ((WidgetListBaseAccessor) widget).malilibui$getScrollBar().getValue();
            }
        }

        return 0;
    }

    private static void setScrollValue(GuiBase screen, int value) {
        if (screen instanceof GuiListBase<?, ?, ?> list) {
            WidgetListBase<?, ?> widget = ((GuiListBaseAccessor) list).malilibui$getListWidget();

            if (widget != null) {
                GuiScrollBar bar = ((WidgetListBaseAccessor) widget).malilibui$getScrollBar();
                bar.setValue(Math.max(0, Math.min(value, bar.getMaxValue())));
            }
        }
    }

    /**
     * 「启用界面重制」配置项的值变化回调（在按钮点击处理阶段同步调用）。
     *
     * <p>关闭时立即恢复原版布局并重建界面（与切换 TAB 相同的路径），
     * 这样不需要等下一帧/切页；重新开启时下一帧 ensure 会自动重新应用布局。</p>
     */
    public static void onRestyleEnabledChanged(boolean enabled) {
        Screen current = GuiUtils.getCurrentScreen();

        if (enabled || !(current instanceof GuiBase screen) || !STATES.containsKey(screen)) {
            return;
        }

        restoreVanillaLayout(screen);
    }

    /**
     * 返回某个界面当前生效的布局；未接管 / 已关闭重制时返回 null。
     *
     * <p>GuiListBase 的 mixin 用它来重定向列表控件的创建坐标，
     * 这样我们不需要修改原版的列表坐标字段。</p>
     */
    public static ConfigLayout layoutFor(GuiBase screen) {
        ConfigScreenState state = STATES.get(screen);

        if (state == null || !state.ready || state.layout == null) {
            return null;
        }

        return state.layout;
    }

    /**
     * 是否禁用了重制布局（配置关闭或窗口极小）。
     */
    private static boolean isManageDisabled(GuiBase screen) {
        if (!UiConfigs.General.ENABLED.getBooleanValue()) {
            return true;
        }

        return screen.getScreenWidth() < MIN_SCREEN_W || screen.getScreenHeight() < MIN_SCREEN_H;
    }

    private static ConfigScreenState stateFor(GuiBase screen) {
        ConfigScreenState state = STATES.get(screen);

        if (state == null) {
            state = new ConfigScreenState(screen);
            STATES.put(screen, state);
        }

        return state;
    }

    private static boolean isTooSmallToManage(GuiBase screen) {
        if (screen.getScreenWidth() < MIN_SCREEN_W || screen.getScreenHeight() < MIN_SCREEN_H) {
            // 尺寸过小：恢复原版布局与原始列表位置。
            restoreVanillaLayout(screen);
            return true;
        }

        return false;
    }

    private static boolean isDisabledByConfig(GuiBase screen) {
        if (UiConfigs.General.ENABLED.getBooleanValue()) {
            return false;
        }

        // 用户禁用重制时恢复原版布局（幂等：每帧校验直到完全恢复）。
        restoreVanillaLayout(screen);
        return true;
    }

    /**
     * 恢复原版布局（幂等）：还原被我们修改过的列表宿主坐标，
     * 并在需要时重建一次界面，让子类按原版位置重建 TAB 按钮与列表控件。
     *
     * <p>保留接管状态而不是直接丢弃，以便重新启用重制时能立刻重新应用布局。</p>
     */
    private static void restoreVanillaLayout(GuiBase screen) {
        ConfigScreenState state = STATES.get(screen);

        if (state == null) {
            return;
        }

        boolean needsRebuild = state.ready || state.layout != null;

        state.ready = false;
        // 清空布局：恢复只需执行一次，否则每帧都会重复重建界面（日志刷屏、界面反复重置）。
        state.layout = null;

        if (needsRebuild) {
            MalilibUiMod.LOGGER.info("[{}] Restoring vanilla layout for {}",
                    MalilibUiMod.MOD_NAME, screen.getClass().getSimpleName());

            // GuiListBase 的 initGui() 只在列表控件为 null 时才重建它，
            // 所以先清空列表控件，确保按原版坐标重新创建。
            if (screen instanceof GuiListBase<?, ?, ?>) {
                ((GuiListBaseAccessor) screen).malilibui$setListWidgetField(null);
            }

            // 直接重建界面控件（不走“关闭 → 重开”流程，避免鼠标光标被释放并跳到屏幕中间）。
            screen.initGui();
        }
    }

    private static void ensureConfigsLayout(GuiConfigsBase configs) {
        if (isDisabledByConfig(configs) || isTooSmallToManage(configs)) {
            return;
        }

        ConfigScreenState state = stateFor(configs);
        applyNavLayout(configs, state);
        ensureList(configs, state);
        ensureModSwitcher(configs, state);
        positionExtraDropdowns(configs, state);
        state.ready = true;
    }

    /**
     * 部分模组会在配置界面里额外添加排序/筛选下拉框。它们原本按屏幕坐标硬编码，
     * 重制布局后会被挤到面板外；这里统一按内容区右边缘重新摆放。
     * 26.x 保持原行为不动。
     */
    private static void positionExtraDropdowns(GuiConfigsBase configs, ConfigScreenState state) {
        if (is26x()) {
            return;
        }

        ConfigLayout layout = state.layout;
        WidgetDropDownList<ModInfo> switcher = ((GuiConfigsBaseAccessor) configs).malilibui$getModSwitchWidget();
        List<WidgetDropDownList<?>> extras = new ArrayList<>();

        for (WidgetBase widget : ((GuiBaseAccessor) configs).malilibui$getWidgets()) {
            if (widget instanceof WidgetDropDownList<?> dropdown && dropdown != switcher) {
                extras.add(dropdown);
            }
        }

        if (extras.isEmpty()) {
            return;
        }

        int right = switcher != null ? switcher.getX() + switcher.getWidth()
                : layout.contentX + layout.contentW - 8;
        int y = switcher != null ? switcher.getY()
                : layout.contentY + (layout.headerH - 18) / 2;

        for (int i = extras.size() - 1; i >= 0; --i) {
            WidgetDropDownList<?> dropdown = extras.get(i);

            String dropdownClass = dropdown.getClass().getName();
            if (dropdownClass.contains("magiclib") || dropdownClass.contains("SelectorDropDownList")) {
                // MasaGadget 用 MagicLib 的 SelectorDropDownList 替代原版切换器。
                // 宽度保持与原版切换器一致，只增加一页可显示的条目数。
                int width = switcher != null ? switcher.getWidth() : 130;
                dropdown.setWidth(width);
                dropdown.setHeight(switcher != null ? switcher.getHeight() : 18);
                dropdown.setPosition(right - width, y);
                expandMasaGadgetVisibleEntries();
                continue;
            }

            int width = dropdown.getWidth();
            right -= width;
            dropdown.setPosition(Math.max(layout.contentX + 8, right), y);
            right -= 4;
        }
    }

    private static boolean masaGadgetEntriesExpanded;

    private static void expandMasaGadgetVisibleEntries() {
        if (masaGadgetEntriesExpanded) {
            return;
        }

        try {
            Class<?> configs = Class.forName("com.plusls.MasaGadget.game.Configs");
            Object option = configs.getField("fastSwitchMasaGadgetConfigGuiVisibleEntries").get(null);
            Object value = option.getClass().getMethod("getIntegerValue").invoke(option);

            if (value instanceof Integer current && current < 12) {
                try {
                    option.getClass().getMethod("setIntegerValue", int.class).invoke(option, 12);
                } catch (NoSuchMethodException e) {
                    option.getClass().getMethod("setValue", Object.class).invoke(option, 12);
                }
            }

            masaGadgetEntriesExpanded = true;
        } catch (Throwable ignored) {
            // MasaGadget/MagicLib 不存在或 API 变化时保持原样。
        }
    }


    private static void ensureRenderLayerLayout(GuiBase screen) {
        if (isDisabledByConfig(screen) || isTooSmallToManage(screen)) {
            return;
        }

        ConfigScreenState state = stateFor(screen);
        applyNavLayout(screen, state);
        positionStandaloneControls(screen, state);
        state.ready = true;
    }

    private static void applyNavLayout(GuiBase screen, ConfigScreenState state) {
        List<ButtonBase> buttons = ((GuiBaseAccessor) screen).malilibui$getButtons();

        if (!state.navButtons.isEmpty()) {
            // 界面已重建（切换 TAB），原按钮实例全部失效。
            for (ButtonBase button : state.navButtons) {
                if (!buttons.contains(button)) {
                    state.navButtons.clear();
                    break;
                }
            }
        }

        if (state.navButtons.isEmpty()) {
            for (ButtonBase button : buttons) {
                if (isTabButton(button)) {
                    state.navButtons.add(button);
                }
            }
        }

        ConfigLayout layout = ConfigLayout.compute(screen.getScreenWidth(), screen.getScreenHeight(),
                state.navButtons.size());
        state.layout = layout;

        // 2.0 模组树需要知道每个模组有哪些 TAB；这里缓存当前界面已经构建出的分组名称。
        List<String> tabLabels = new ArrayList<>();

        for (ButtonBase button : state.navButtons) {
            tabLabels.add(((ButtonBaseAccessor) button).malilibui$getDisplayString());
        }

        ModTree.cacheTabs(getModId(screen), tabLabels);

        // 把 TAB 按钮移动到左侧导航栏。
        for (int i = 0; i < state.navButtons.size(); i++) {
            ButtonBase button = state.navButtons.get(i);
            int x = layout.navX;
            int y = layout.navItemY(i);

            if (button.getX() != x || button.getY() != y ||
                    button.getWidth() != layout.navW || button.getHeight() != layout.navItemH) {
                button.setPosition(x, y);
                button.setWidth(layout.navW);
                button.setHeight(layout.navItemH);
            }
        }
    }

    private static void ensureModSwitcher(GuiConfigsBase configs, ConfigScreenState state) {
        WidgetDropDownList<ModInfo> switcher = ((GuiConfigsBaseAccessor) configs).malilibui$getModSwitchWidget();

        if (switcher == null) {
            return;
        }

        ConfigLayout layout = state.layout;
        // 在内容区标题行内垂直居中，并与搜索框右边缘对齐；窗口较窄时不允许越出内容区。
        int x = Math.max(layout.contentX + 8, layout.contentX + layout.contentW - 152);
        int y = layout.contentY + (layout.headerH - 18) / 2;

        if (switcher.getX() != x || switcher.getY() != y) {
            switcher.setPosition(x, y);
        }
    }

    private static boolean hasModSwitcher(ConfigScreenState state) {
        return state.screen instanceof GuiConfigsBase configs &&
                ((GuiConfigsBaseAccessor) configs).malilibui$getModSwitchWidget() != null;
    }

    static boolean is26x() {
        try {
            Class.forName("net.minecraft.client.gui.GuiGraphicsExtractor");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static boolean isTabButton(ButtonBase button) {
        int y = button.getY();

        return y >= 0 && y <= TAB_STRIP_MAX_Y &&
                button.getHeight() > 0 && button.getHeight() <= TAB_MAX_HEIGHT &&
                button.getWidth() > 0;
    }

    private static void ensureList(GuiConfigsBase configs, ConfigScreenState state) {
        ConfigLayout layout = state.layout;
        GuiListBaseAccessor listAccess = (GuiListBaseAccessor) configs;
        WidgetListBase<?, ?> widget = listAccess.malilibui$getListWidget();

        if (widget == null) {
            return;
        }

        WidgetListBaseAccessor widgetAccess = (WidgetListBaseAccessor) widget;
        boolean recreated = false;
        boolean positionOk = widgetAccess.malilibui$getListPosX() == layout.listX &&
                widgetAccess.malilibui$getListPosY() == layout.listY;

        if (!positionOk) {
            // 重新创建列表控件：GuiListBase 的 mixin 会把创建坐标替换成新布局的位置，
            // 列表控件的构造函数同时会向屏幕查询浏览器宽高（同样被重定向到新布局尺寸），
            // 因此搜索框与条目区域都会以正确的尺寸创建。
            listAccess.malilibui$reCreateListWidget();

            widget = listAccess.malilibui$getListWidget();

            if (widget == null) {
                return;
            }

            widgetAccess = (WidgetListBaseAccessor) widget;
            recreated = true;
        }

        boolean needsRebuild = recreated;

        // 列表控件可能是重写了 getBrowserHeight() 的子类（例如 GuiModConfigs）；
        // 若构造函数最终没有采用我们的尺寸，则重新应用布局尺寸。
        if (widgetAccess.malilibui$getBrowserWidth() != layout.listW ||
                widgetAccess.malilibui$getBrowserHeight() != layout.listH) {
            widget.setSize(layout.listW, layout.listH);
            needsRebuild = true;
        }

        // 配置行控件宽度自适应：窄窗口下收缩控件列，避免整行溢出内容区。
        if (widget instanceof WidgetListConfigOptionsBase<?, ?> configList) {
            WidgetListConfigOptionsBaseAccessor configAccess = (WidgetListConfigOptionsBaseAccessor) configList;
            int requested = ((GuiConfigsBaseAccessor) configs).malilibui$getConfigWidth();
            int maxAllowed = Math.max(80, layout.listW - 170);
            int desired = Math.min(requested, maxAllowed);

            if (configAccess.malilibui$getConfigWidth() != desired) {
                configAccess.malilibui$setConfigWidth(desired);
                needsRebuild = true;
            }
        }

        if (needsRebuild) {
            widget.initGui();
        }

        state.listWidget = widget;
    }

    /**
     * 把渲染层编辑器中非 TAB 的控件平移到内容区。
     *
     * <p>这些界面的控件基于原版左上角坐标（{@code 10, 60}）创建；位移通过基于对象身份的
     * {@code positionedControls} 集合保证每个控件只应用一次，因此控件不会被重复平移，
     * 同时又能跟随之后的窗口缩放。</p>
     */
    private static void positionStandaloneControls(GuiBase screen, ConfigScreenState state) {
        ConfigLayout layout = state.layout;
        int deltaX = layout.listX - RENDER_LAYER_CONTROLS_X;
        int deltaY = layout.listY - RENDER_LAYER_CONTROLS_Y;

        if (deltaX == 0 && deltaY == 0) {
            return;
        }

        GuiBaseAccessor access = (GuiBaseAccessor) screen;

        for (ButtonBase button : access.malilibui$getButtons()) {
            if (state.navButtons.contains(button) || !state.positionedControls.add(button)) {
                continue;
            }

            button.setPosition(button.getX() + deltaX, button.getY() + deltaY);
        }

        for (WidgetBase widget : access.malilibui$getWidgets()) {
            if (!state.positionedControls.add(widget)) {
                continue;
            }

            widget.setPosition(widget.getX() + deltaX, widget.getY() + deltaY);
        }

        for (TextFieldWrapper<? extends GuiTextFieldGeneric> wrapper : access.malilibui$getTextFields()) {
            GuiTextFieldGeneric field = wrapper.textField();

            if (field == null) {
                continue;
            }

            int[] origin = STANDALONE_FIELD_ORIGINS.computeIfAbsent(field,
                    f -> new int[] { f.getX(), f.getY() });
            int nx = origin[0] + deltaX;
            int ny = origin[1] + deltaY;
            // GuiTextFieldGeneric 自己保存了一份 x/y，但 EditBox/AbstractWidget 的
            // 父类字段仍停留在旧坐标，文本会按父类坐标绘制。这里同步移动父类字段。
            field.setX(nx);
            field.setY(ny);
            field.setPosition(nx, ny);
            ((com.github.bunnyi116.malilibui.mixin.accessor.AbstractWidgetAccessor) field)
                    .malilibui$setWidgetX(nx);
            ((com.github.bunnyi116.malilibui.mixin.accessor.AbstractWidgetAccessor) field)
                    .malilibui$setWidgetY(ny);

            if (Boolean.getBoolean("malilib-ui.debuglayout")) {
                StringBuilder sb = new StringBuilder();
                sb.append("field ").append(field.getClass().getName())
                  .append(" getX=").append(field.getX())
                  .append(" getY=").append(field.getY())
                  .append(" value=").append(field.getValue())
                  .append('\n');
                for (Class<?> c = field.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
                    for (java.lang.reflect.Field f : c.getDeclaredFields()) {
                        if (f.getType() == int.class && (f.getName().equals("x") || f.getName().equals("y"))) {
                            try {
                                f.setAccessible(true);
                                sb.append("  ").append(c.getSimpleName()).append('.').append(f.getName())
                                  .append('=').append(f.getInt(field)).append('\n');
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
                try {
                    java.nio.file.Files.writeString(java.nio.file.Path.of("C:\\Users\\AsiaA\\Desktop\\rli\\field-debug.log"), sb.toString());
                } catch (Exception ignored) {
                }
            }
        }
    }

    /* ------------------------------------------------------------------ *
     *  供 mixin 使用的查询方法
     * ------------------------------------------------------------------ */

    public static boolean hasLayout(GuiBase screen) {
        ConfigScreenState state = STATES.get(screen);

        return state != null && state.ready && state.layout != null;
    }

    public static boolean isNavButton(GuiBase screen, ButtonBase button) {
        ConfigScreenState state = STATES.get(screen);

        return state != null && state.ready && state.navButtons.contains(button);
    }

    /* ------------------------------------------------------------------ *
     *  渲染
     * ------------------------------------------------------------------ */

    /**
     * 绘制整块新的界面背景（替代原版背景绘制）。
     */
    public static void drawBackground(GuiBase screen, GuiContext ctx, int mouseX, int mouseY) {
        ConfigScreenState state = STATES.get(screen);

        if (state == null || !state.ready || state.layout == null) {
            return;
        }

        ConfigLayout layout = state.layout;

        // 全屏压暗遮罩。
        UiTheme.fill(ctx, 0, 0, layout.screenW, layout.screenH, UiTheme.overlayColor());

        // 面板投影与主体。
        UiTheme.shadow(ctx, layout.panelX, layout.panelY, layout.panelW, layout.panelH);
        UiTheme.gradient(ctx, layout.panelX, layout.panelY, layout.panelW, layout.panelH,
                UiTheme.panelTopColor(), UiTheme.panelBottomColor());
        UiTheme.outline(ctx, layout.panelX, layout.panelY, layout.panelW, layout.panelH,
                UiTheme.PANEL_BORDER);
        UiTheme.hline(ctx, layout.panelX + 1, layout.panelY + 1, layout.panelW - 2,
                UiTheme.PANEL_INNER_GLOW);

        if (layout.hasSidebar) {
            drawSidebar(ctx, state);
        }

        // 内嵌凹槽面板，用于承载选项列表 / 渲染层控件。
        int wellMargin = layout.compact ? 4 : WELL_MARGIN;
        UiTheme.drawContentWell(ctx, layout.listX - wellMargin, layout.listY - wellMargin,
                layout.listW + wellMargin * 2, layout.listH + wellMargin * 2);

        drawContentHeader(ctx, state);
    }

    private static void drawSidebar(GuiContext ctx, ConfigScreenState state) {
        ConfigLayout layout = state.layout;
        String modName = prettyModName(getModId(state.screen));

        UiTheme.gradient(ctx, layout.sidebarX, layout.sidebarY, layout.sidebarW, layout.sidebarH,
                UiTheme.sidebarTopColor(), UiTheme.sidebarBottomColor());
        UiTheme.vline(ctx, layout.sidebarX + layout.sidebarW, layout.sidebarY, layout.sidebarH,
                UiTheme.SIDEBAR_DIVIDER);

        // 徽章与标题区：1.21.11 使用可自定义徽章；26.x 保持上游原样。
        if (is26x()) {
            UiTheme.fill(ctx, layout.sidebarX + 12, layout.sidebarY + 16, 3, 14, UiTheme.TEXT_ACCENT);
            UiTheme.text(ctx, UiTheme.trim(ctx.fontRenderer(), modName, layout.sidebarW - 40),
                    layout.sidebarX + 21, layout.sidebarY + 19, UiTheme.TEXT_TITLE, true);
            UiTheme.text(ctx, "MaLiLib UI", layout.sidebarX + 21, layout.sidebarY + 33,
                    UiTheme.TEXT_SUB, false);

            UiTheme.hline(ctx, layout.sidebarX + 12, layout.sidebarY + 48, layout.sidebarW - 24,
                    0x22FFFFFF);
            return;
        }

        BadgeRenderer.draw(ctx, layout.sidebarX + 10, layout.sidebarY + 8,
                Math.max(40, layout.sidebarW - 20));

        UiTheme.hline(ctx, layout.sidebarX + 12, layout.sidebarY + 42, layout.sidebarW - 24,
                0x22FFFFFF);
    }

    private static void drawContentHeader(GuiContext ctx, ConfigScreenState state) {
        ConfigLayout layout = state.layout;
        // 侧栏只保留徽章，模组名统一显示在内容区标题栏，避免重复。
        String currentTab = prettyModName(getModId(state.screen));

        if (currentTab == null || currentTab.isEmpty()) {
            currentTab = state.screen.getTitleString();
        }

        int titleWidth = layout.contentW - 40;

        if (hasModSwitcher(state)) {
            titleWidth -= 158;
        }

        if (currentTab != null && !currentTab.isEmpty()) {
            int accentY = layout.contentY + (layout.headerH - 12) / 2;
            int textY = layout.contentY + (layout.headerH - 8) / 2;

            UiTheme.fill(ctx, layout.contentX + 12, accentY, 3, 12,
                    UiTheme.TEXT_ACCENT);
            UiTheme.text(ctx, UiTheme.trim(ctx.fontRenderer(), currentTab, titleWidth),
                    layout.contentX + 21, textY, UiTheme.TEXT_TITLE, true);
        }

        // 这里不再画额外的分隔线：标题下方的内容凹槽边框
        // 已经提供了视觉分隔（再画一条线反而像渲染错误）。
    }

    /**
     * 绘制一个导航 TAB；返回 true 表示该按钮属于导航栏且已在此处理（需跳过原版渲染）。
     */
    public static boolean drawNavButton(GuiBase screen, ButtonBase button, GuiContext ctx,
                                        int mouseX, int mouseY) {
        ConfigScreenState state = STATES.get(screen);

        if (state == null || !state.ready || !state.navButtons.contains(button)) {
            return false;
        }

        ButtonBaseAccessor access = (ButtonBaseAccessor) button;
        String label = access.malilibui$getDisplayString();
        boolean active = !access.malilibui$isEnabled();
        boolean hovered = mouseX >= button.getX() && mouseX < button.getX() + button.getWidth() &&
                mouseY >= button.getY() && mouseY < button.getY() + button.getHeight();

        UiTheme.drawNavTab(ctx, button.getX(), button.getY(), button.getWidth(), button.getHeight(),
                label, active, !active && hovered);

        return true;
    }

    private static String activeTabLabel(ConfigScreenState state) {
        for (ButtonBase button : state.navButtons) {
            ButtonBaseAccessor access = (ButtonBaseAccessor) button;

            if (!access.malilibui$isEnabled()) {
                return access.malilibui$getDisplayString();
            }
        }

        return null;
    }

    private static String getModId(GuiBase screen) {
        if (screen instanceof GuiConfigsBase configs) {
            return configs.getModId();
        }

        // 渲染层编辑器不是 GuiConfigsBase；从包名（fi.dy.masa.<modId>.*）
        // 推断模组 ID，足以用于侧栏标题。
        String[] parts = screen.getClass().getName().split("\\.");

        for (int i = 1; i < parts.length; i++) {
            if ("masa".equals(parts[i - 1])) {
                return parts[i];
            }
        }

        return "config";
    }

    private static String prettyModName(String modId) {
        if (modId == null || modId.isEmpty()) {
            return "Config";
        }

        String name = modId.replace('_', ' ').replace('-', ' ');
        StringBuilder builder = new StringBuilder(name.length());

        for (String part : name.split(" ")) {
            if (part.isEmpty()) {
                continue;
            }

            if (builder.length() > 0) {
                builder.append(' ');
            }

            builder.append(part.substring(0, 1).toUpperCase(Locale.ROOT))
                    .append(part.substring(1));
        }

        return builder.toString();
    }
}
