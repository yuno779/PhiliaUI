package com.github.bunnyi116.malilibui.mixin;

import com.github.bunnyi116.malilibui.gui.ConfigScreenManager;
import fi.dy.masa.malilib.gui.widgets.WidgetSearchBar;
import fi.dy.masa.malilib.render.GuiContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** 1.21.11 的列表搜索框已由 2.0 顶栏取代，不再渲染。 */
@Mixin(WidgetSearchBar.class)
public class WidgetSearchBarMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void malilibui$hideLegacySearchBar(GuiContext ctx, int mouseX, int mouseY,
                                               boolean selected, CallbackInfo ci) {
        if (ConfigScreenManager.shouldHideNativeSearchBar()) {
            ci.cancel();
        }
    }
}
