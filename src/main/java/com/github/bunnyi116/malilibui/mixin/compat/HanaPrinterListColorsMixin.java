package com.github.bunnyi116.malilibui.mixin.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.github.bunnyi116.malilibui.gui.ConfigScreenManager;

/** 同上：把打印机配置列表的分组标题与分隔线从墨绿换成粉色。 */
@Pseudo
@Mixin(targets = "me.aleksilassila.litematica.printer.gui.WorkspaceConfigList$OptionRow", remap = false)
public abstract class HanaPrinterListColorsMixin {
    @ModifyConstant(method = "addLabel",
            constant = @Constant(intValue = -5449526), remap = false)
    private int malilibui$pinkSectionLabel(int original) {
        return ConfigScreenManager.isHanaPrinterInstalled() ? 0xFFF2A7D8 : original;
    }

    @ModifyConstant(method = "render",
            constant = @Constant(intValue = -12892090), remap = false)
    private int malilibui$pinkSectionDivider(int original) {
        return ConfigScreenManager.isHanaPrinterInstalled() ? 0xFF4A3848 : original;
    }
}
