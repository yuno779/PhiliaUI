package com.github.bunnyi116.malilibui.mixin.compat;

import com.github.bunnyi116.malilibui.gui.ConfigScreenManager;
import fi.dy.masa.malilib.gui.GuiBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * LPCTools 兼容接入：它自己的 initGui() 在 super 之后才创建标签按钮，
 * 因此在它的 TAIL 再套用一次通用布局即可。
 */
@Pseudo
@Mixin(targets = "lpctools.lpcfymasaapi.LPCConfigPage$ConfigPageInstance", remap = false)
public abstract class LpctoolsConfigPageMixin {
    @Inject(method = "initGui", at = @At("TAIL"))
    private void malilibui$afterLpctoolsInitGui(CallbackInfo ci) {
        ConfigScreenManager.ensureLayout((GuiBase) (Object) this);
    }
}
