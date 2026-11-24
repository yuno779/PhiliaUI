package com.github.bunnyi116.malilibui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.widgets.WidgetDropDownList;
import fi.dy.masa.malilib.render.GuiContext;
import fi.dy.masa.malilib.util.GuiUtils;

import com.github.bunnyi116.malilibui.gui.ConfigScreenManager;
import com.github.bunnyi116.malilibui.gui.DropdownRenderer;
import com.github.bunnyi116.malilibui.mixin.accessor.WidgetDropDownListAccessor;
import com.github.bunnyi116.malilibui.config.UiConfigs;

/**
 * 下拉框展开时，把滚轮事件标记为已消费，
 * 避免在滚动下拉列表的同时也滚动下方的配置列表（事件穿透）。
 */
@Mixin(WidgetDropDownList.class)
public abstract class WidgetDropDownListMixin {
    /** 收起和展开状态都换成当前 UI 的深色样式，MasaGadget 的下拉框同样继承此类。 */
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void malilibui$renderThemed(GuiContext ctx, int mouseX, int mouseY, boolean selected,
                                        org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        WidgetDropDownList<?> dropdown = (WidgetDropDownList<?>) (Object) this;

        if (!ConfigScreenManager.shouldStyleDropdown(dropdown)) {
            return;
        }

        DropdownRenderer.render(dropdown, ctx, mouseX, mouseY);
        ci.cancel();
    }

    @Inject(method = "onMouseScrolledImpl", at = @At("RETURN"), cancellable = true)
    private void malilibui$consumeScrollWheel(double mouseX, double mouseY, double horizontalAmount,
                                              double verticalAmount, CallbackInfoReturnable<Boolean> cir) {
        if (!UiConfigs.General.ENABLED.getBooleanValue()) {
            return;
        }

        if (!(GuiUtils.getCurrentScreen() instanceof GuiConfigsBase)) {
            return;
        }

        if (((WidgetDropDownListAccessor) this).malilibui$isOpen()) {
            cir.setReturnValue(true);
        }
    }
}
