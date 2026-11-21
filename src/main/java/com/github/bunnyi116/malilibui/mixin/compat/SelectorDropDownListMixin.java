package com.github.bunnyi116.malilibui.mixin.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * MasaGadget 的快速切换器使用 MagicLib 的 SelectorDropDownList。
 * 构造时把可见条目数和最大高度调大，让一页能显示更多模组。
 */
@Pseudo
@Mixin(targets = "top.hendrixshen.magiclib.impl.malilib.config.gui.SelectorDropDownList", remap = false)
public class SelectorDropDownListMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, ordinal = 4)
    private static int malilibui$expandMaxHeight(int maxHeight) {
        return Math.max(maxHeight, 400);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, ordinal = 5)
    private static int malilibui$expandVisibleEntries(int visibleEntries) {
        return Math.max(visibleEntries, 12);
    }
}
