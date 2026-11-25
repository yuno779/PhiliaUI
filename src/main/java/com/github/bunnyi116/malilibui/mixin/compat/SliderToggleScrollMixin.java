package com.github.bunnyi116.malilibui.mixin.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fi.dy.masa.malilib.gui.button.ButtonBase;
import com.github.bunnyi116.malilibui.gui.ConfigScreenManager;

/**
 * malilib 在数值配置项旁边提供“文本输入 / 拖动条”切换按钮，
 * 其监听器直接调用 {@code GuiBase.initGui()} 重建整个配置界面，
 * 列表滚动条会归零。这里在切换前后保存并恢复滚动位置。
 */
@Mixin(targets = "fi.dy.masa.malilib.gui.widgets.WidgetConfigOption$ListenerSliderToggle", remap = false)
public abstract class SliderToggleScrollMixin {
    @Inject(method = "actionPerformedWithButton", at = @At("HEAD"), remap = false)
    private void malilibui$saveScrollBeforeToggle(ButtonBase button, int mouseButton, CallbackInfo ci) {
        ConfigScreenManager.saveScrollBeforeRebuild();
    }
}
