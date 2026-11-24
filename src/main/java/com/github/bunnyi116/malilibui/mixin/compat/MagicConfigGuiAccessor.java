package com.github.bunnyi116.malilibui.mixin.compat;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import fi.dy.masa.malilib.gui.widgets.WidgetBase;

/** 暴露 MagicLib 配置界面里单独渲染的筛选/排序控件，便于统一布局。 */
@Pseudo
@Mixin(targets = "top.hendrixshen.magiclib.impl.malilib.config.gui.MagicConfigGui", remap = false)
public interface MagicConfigGuiAccessor {
    @Accessor("hoveringWidgets")
    List<WidgetBase> malilibui$getHoveringWidgets();
}
