package com.github.bunnyi116.malilibui.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListBase;

/**
 * 重制后的配置界面 / 渲染层界面的每屏幕可变状态。
 *
 * <p>所有字段仅在渲染线程访问。</p>
 */
public final class ConfigScreenState {
    public final GuiBase screen;

    /**
     * 当前布局几何；首次应用前为 null。
     */
    public ConfigLayout layout;

    /**
     * masa 模组添加到屏幕顶部的原始 TAB 按钮。
     * 保留同一批按钮对象（确保点击回调仍然有效），仅将其移动到左侧导航栏。
     */
    public final List<ButtonBase> navButtons = new ArrayList<>();

    /**
     * 当前安装在界面中的配置选项列表控件。
     */
    public WidgetListBase<?, ?> listWidget;

    /**
     * 已经迁移到内容区的非 TAB 控件（基于对象身份记录，以便 {@code initGui()} 重建的
     * 新控件会再次处理，而已迁移的控件不会被重复平移）。
     */
    public final Set<Object> positionedControls = Collections.newSetFromMap(new IdentityHashMap<>());

    /**
     * 布局首次成功应用后置为 true。
     */
    public boolean ready;

    /** 2.0 左侧树是否已经在当前界面实例上创建过按钮。 */
    public boolean treeBuilt;

    /** 左侧树的模组行按钮，用于判断界面重建后是否需要重加。 */
    public final List<ButtonBase> treeButtons = new ArrayList<>();

    /** 树行按钮对应的绘制数据。 */
    public final Map<ButtonBase, TreeRow> treeRows = new IdentityHashMap<>();

    public record TreeRow(String name, String version, boolean selected, boolean tab) {
    }

    public ConfigScreenState(GuiBase screen) {
        this.screen = screen;
    }
}
