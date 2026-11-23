package com.github.bunnyi116.malilibui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.Minecraft;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.GuiScrollBar;
import fi.dy.masa.malilib.render.GuiContext;
import fi.dy.masa.malilib.util.GuiUtils;
import com.github.bunnyi116.malilibui.gui.ConfigScreenManager;
import com.github.bunnyi116.malilibui.gui.UiTheme;
import com.github.bunnyi116.malilibui.config.UiConfigs;
import com.github.bunnyi116.malilibui.mixin.accessor.GuiScrollBarAccessor;

/** 为配置界面的滚动条重新配色，以匹配新主题。 */
@Mixin(GuiScrollBar.class)
public abstract class GuiScrollBarMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void malilibui$themeScrollbar(GuiContext ctx, int mouseX, int mouseY, float partialTicks,
                                          int xPosition, int yPosition, int width, int height,
                                          int totalHeight, CallbackInfo ci) {
        if (!UiConfigs.General.ENABLED.getBooleanValue()) {
            return;
        }

        if (GuiUtils.getCurrentScreen() instanceof GuiConfigsBase
                || (GuiUtils.getCurrentScreen() instanceof GuiBase guiBase
                    && ConfigScreenManager.isMiniHudShapeManager(guiBase))) {
            GuiScrollBarAccessor access = (GuiScrollBarAccessor) (Object) this;
            boolean hovered = mouseX > xPosition && mouseX < xPosition + width &&
                    mouseY > yPosition && mouseY < yPosition + height;

            access.malilibui$setBackgroundColor(UiTheme.SCROLL_TRACK);
            access.malilibui$setForegroundColor(hovered ? UiTheme.SCROLL_THUMB_HOVER : UiTheme.SCROLL_THUMB);
        }
    }
}
