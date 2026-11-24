package com.github.bunnyi116.malilibui.mixin.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.bunnyi116.malilibui.gui.ConfigScreenManager;
import fi.dy.masa.malilib.gui.GuiBase;

/**
 * Hana 版打印机（beta29+ / dev447+ 的新 UI）原生界面使用墨绿色点缀；这里只把颜色换成 Philia UI 的粉色，
 * 不移动任何控件，也不改变控件的尺寸与绘制方式。
 *
 * <p>仅在新 UI 的 Hana 打印机存在时生效，旧版 Hana、dev446 及更早、bunnyi 等其它版本保持原样。</p>
 */
@Pseudo
@Mixin(targets = "me.aleksilassila.litematica.printer.gui.ConfigUi", remap = false)
public abstract class HanaPrinterColorsMixin {
    /** 全屏底色与侧栏底色改成与 Philia UI 一致的半透明深色。 */
    @ModifyConstant(method = "drawScreenBackground",
            constant = @Constant(intValue = -266855392), remap = false)
    private int malilibui$translucentPage(int original) {
        return ConfigScreenManager.isHanaPrinterInstalled() ? 0x8C121017 : original;
    }

    @ModifyConstant(method = "drawScreenBackground",
            constant = @Constant(intValue = -15460068), remap = false)
    private int malilibui$translucentSidebar(int original) {
        return ConfigScreenManager.isHanaPrinterInstalled() ? 0xBC161823 : original;
    }

    @ModifyConstant(method = "drawScreenBackground",
            constant = @Constant(intValue = -13611965), remap = false)
    private int malilibui$pinkNavActive(int original) {
        return ConfigScreenManager.isHanaPrinterInstalled() ? 0xFF4A2E46 : original;
    }

    @ModifyConstant(method = "drawScreenBackground",
            constant = @Constant(intValue = -5449526), remap = false)
    private int malilibui$pinkNavEdge(int original) {
        return ConfigScreenManager.isHanaPrinterInstalled() ? 0xFFF2A7D8 : original;
    }

    /** 内容区顶部的页面标题（如“工作台”）也换成粉色。 */
    @ModifyConstant(method = "drawContents",
            constant = @Constant(intValue = -4661037), remap = false)
    private int malilibui$pinkPageTitle(int original) {
        return ConfigScreenManager.isHanaPrinterInstalled() ? 0xFFF2A7D8 : original;
    }

    /**
     * Hana 在切换左侧内置页面时会重建控件并覆盖模组切换框坐标，
     * 每帧绘制背景前把它重新对齐到 Philia 适配界面的位置。
     */
    @Inject(method = "drawScreenBackground", at = @At("HEAD"), remap = false)
    private void malilibui$realignSwitcher(CallbackInfo ci) {
        if (ConfigScreenManager.isHanaPrinterInstalled()) {
            ConfigScreenManager.positionSwitcherVanilla((GuiBase) (Object) this);
        }
    }
}
