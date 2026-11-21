package com.github.bunnyi116.malilibui.mixin.compat;

import com.github.bunnyi116.malilibui.gui.ConfigScreenManager;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 顶栏搜索框改用自绘的半透明背景，隐藏原版黑色输入框底板。
 */
@Mixin(GuiTextFieldGeneric.class)
public class GuiTextFieldGenericMixin {
    @Inject(method = "renderWidget", at = @At("HEAD"), cancellable = true)
    private void malilibui$hideSearchBackground(@Coerce Object drawContext, int mouseX, int mouseY,
                                                float partialTicks, CallbackInfo ci) {
        if (ConfigScreenManager.isSearchField((GuiTextFieldGeneric) (Object) this)) {
            ci.cancel();
        }
    }
}
