package com.github.bunnyi116.malilibui.mixin.compat;

import com.github.bunnyi116.malilibui.mixin.accessor.WidgetDropDownListAccessor;
import fi.dy.masa.malilib.gui.widgets.WidgetDropDownList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 展开后的下拉列表会绘制到控件原始高度之外，但点击命中仍只按
 * 关闭状态的 16px 高度判断，导致列表底部几项点不到。
 * 展开时把命中区域向下扩展到绘制区域。
 */
@Mixin(WidgetDropDownList.class)
public abstract class WidgetDropDownListMixin {
    @Inject(method = "isMouseOver", at = @At("HEAD"), cancellable = true)
    private void malilibui$expandClickArea(int mouseX, int mouseY, CallbackInfo ci) {
        WidgetDropDownList<?> self = (WidgetDropDownList<?>) (Object) this;

        if (!((WidgetDropDownListAccessor) self).malilibui$isOpen()) {
            return;
        }

        int x = self.getX();
        int y = self.getY();
        int width = self.getWidth();
        int height = Math.max(self.getHeight(), 400);

        if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
            ci.cancel();
        }
    }
}
