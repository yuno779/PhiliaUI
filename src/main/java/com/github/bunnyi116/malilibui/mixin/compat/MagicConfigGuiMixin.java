package com.github.bunnyi116.malilibui.mixin.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fi.dy.masa.malilib.render.GuiContext;
import com.github.bunnyi116.malilibui.gui.ConfigScreenManager;

/**
 * MagicLib 的 MagicConfigGui 在所有 widget 绘制完成后，会对 hoveringWidgets
 * 再调用一遍 WidgetBase.render(...) 重画下拉框（原生灰边黑底）。
 * 这会盖掉我们的主题样式，产生白色边框。在我们接管的界面里取消这次重画，
 * 下拉框的正常绘制已由 WidgetDropDownListMixin 接管。
 */
@Pseudo
@Mixin(targets = "top.hendrixshen.magiclib.impl.malilib.config.gui.MagicConfigGui", remap = false)
public class MagicConfigGuiMixin {
    @Inject(method = "renderDropDownList", at = @At("HEAD"), cancellable = true)
    private void malilibui$skipNativeDropdownRedraw(GuiContext ctx, int mouseX, int mouseY,
                                                     CallbackInfo ci) {
        if (ConfigScreenManager.shouldStyleDropdown()) {
            ci.cancel();
        }
    }
}
