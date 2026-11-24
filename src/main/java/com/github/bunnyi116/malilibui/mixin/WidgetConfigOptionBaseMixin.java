package com.github.bunnyi116.malilibui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;

import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase.ConfigOptionWrapper;
import fi.dy.masa.malilib.gui.widgets.WidgetConfigOptionBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.util.GuiUtils;

import com.github.bunnyi116.malilibui.gui.ConfigEntryFilter;
import com.github.bunnyi116.malilibui.gui.ConfigScreenManager;
import com.github.bunnyi116.malilibui.mixin.accessor.WidgetListEntryBaseAccessor;

/**
 * 让配置列表里的分组标题（{@code LABEL} 行）可以点击折叠 / 展开。
 */
@Mixin(WidgetConfigOptionBase.class)
public abstract class WidgetConfigOptionBaseMixin {
    @Inject(method = "onMouseClickedImpl", at = @At("HEAD"), cancellable = true)
    private void malilibui$onLabelClicked(MouseButtonEvent click, boolean doubleClick,
                                          CallbackInfoReturnable<Boolean> cir) {
        Object entry = ((WidgetListEntryBaseAccessor) (WidgetListEntryBase<?>) (Object) this).malilibui$getEntry();

        if (!(entry instanceof ConfigOptionWrapper wrapper) ||
                wrapper.getType() != ConfigOptionWrapper.Type.LABEL) {
            return;
        }

        String label = wrapper.getLabel();
        Screen current = GuiUtils.getCurrentScreen();

        if (label == null || !(current instanceof GuiConfigsBase configs) ||
                !ConfigScreenManager.hasLayout(configs)) {
            return;
        }

        ConfigEntryFilter.toggle(configs.getModId(), label);
        ConfigScreenManager.refreshCurrentConfigScreen();
        cir.setReturnValue(true);
    }
}
