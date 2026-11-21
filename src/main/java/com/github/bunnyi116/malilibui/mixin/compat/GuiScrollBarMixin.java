package com.github.bunnyi116.malilibui.mixin.compat;

import fi.dy.masa.malilib.gui.GuiScrollBar;
import fi.dy.masa.malilib.render.GuiContext;
import fi.dy.masa.malilib.render.RenderUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScrollBar.class)
public abstract class GuiScrollBarMixin {
    @Shadow protected int currentValue;
    @Shadow protected int maxValue;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void malilibui$renderSodium(GuiContext ctx, int x, int y, float zLevel,
                                        int width, int height, int contentHeight,
                                        int barWidth, int barHeight, CallbackInfo ci) {
        int trackH = Math.max(1, height);
        RenderUtils.drawRect(ctx, x, y, width, trackH, 0x44000000);
        RenderUtils.drawOutline(ctx, x, y, width, trackH, 0x88FFFFFF);

        int innerH = Math.max(1, trackH - 2);
        float ratio = Math.min(1.0F, innerH / (float) Math.max(1, barHeight));
        int thumbH = Math.max(24, (int) (innerH * ratio));
        int travel = Math.max(0, innerH - thumbH);
        int thumbY = y + 1 + (maxValue > 0 ? currentValue * travel / maxValue : 0);

        RenderUtils.drawRect(ctx, x + 1, thumbY, Math.max(2, width - 2), thumbH, 0xFFFFFFFF);
        ci.cancel();
    }
}
