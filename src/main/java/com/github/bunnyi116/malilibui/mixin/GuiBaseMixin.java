package com.github.bunnyi116.malilibui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.widgets.WidgetDropDownList;
import fi.dy.masa.malilib.render.GuiContext;
import fi.dy.masa.malilib.util.data.ModInfo;
import com.github.bunnyi116.malilibui.gui.ConfigScreenManager;
import com.github.bunnyi116.malilibui.mixin.accessor.GuiConfigsBaseAccessor;
import com.github.bunnyi116.malilibui.mixin.accessor.WidgetDropDownListAccessor;

/**
 * 重制的入口：挂钩 malilib 的 {@link GuiBase}，
 * 使所有 masa 配置界面（均继承 {@code GuiConfigsBase}）都会被重新排布与绘制。
 */
@Mixin(GuiBase.class)
public abstract class GuiBaseMixin {
    // 1.21.11 与 26.x 的渲染入口方法名不同（render / extractRenderState），
    // 但 initGui 与 drawHoveredWidget 的签名一致，因此把公共逻辑挂在这两个
    // 方法上，避免在根节点的未预处理源码里留下版本分支。
    @Inject(method = "initGui", at = @At("TAIL"))
    private void malilibui$afterInitGui(CallbackInfo ci) {
        ConfigScreenManager.ensureLayout((GuiBase) (Object) this);
    }

    @Inject(method = "drawHoveredWidget", at = @At("TAIL"))
    private void malilibui$afterDrawHoveredWidget(GuiContext ctx, int mouseX, int mouseY, CallbackInfo ci) {
        GuiBase screen = (GuiBase) (Object) this;

        if (!(screen instanceof GuiConfigsBase configs) || !ConfigScreenManager.hasLayout(screen)) {
            return;
        }

        // 模组切换下拉框是普通 widget，会先于选项列表绘制；
        // 展开时其下拉面板会被列表再次覆盖。因此在内容之后重新绘制一次，
        // 使其始终浮在内容之上。
        WidgetDropDownList<ModInfo> switcher = ((GuiConfigsBaseAccessor) configs).malilibui$getModSwitchWidget();

        if (switcher != null && ((WidgetDropDownListAccessor) switcher).malilibui$isOpen()) {
            switcher.postRenderHovered(ctx, mouseX, mouseY, false);
        }
    }

    @Inject(method = "mouseDragged", at = @At("HEAD"), cancellable = true)
    private void malilibui$mouseDragged(net.minecraft.client.input.MouseButtonEvent click, double dragX, double dragY,
                                        org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Boolean> cir) {
        if (ConfigScreenManager.onTreeScrollbarDrag((GuiBase) (Object) this, dragY)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void malilibui$mouseClicked(net.minecraft.client.input.MouseButtonEvent click, boolean doubleClick,
                                        org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Boolean> cir) {
        // 编辑框优先，避免抢走输入焦点。
        if (ConfigScreenManager.onSearchClick((GuiBase) (Object) this, click.x(), click.y())) {
            cir.setReturnValue(true);
            return;
        }

        if (ConfigScreenManager.onTreeScrollbarClick((GuiBase) (Object) this, click.x(), click.y())) {
            cir.setReturnValue(true);
            return;
        }

        if (ConfigScreenManager.onTreeClick((GuiBase) (Object) this, click.x(), click.y(), click.button())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true)
    private void malilibui$mouseScrolled(double mouseX, double mouseY, double horizontal,
                                         double vertical, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Boolean> cir) {
        if (ConfigScreenManager.onTreeScroll((GuiBase) (Object) this, mouseX, mouseY, vertical)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "drawScreenBackground", at = @At("HEAD"), cancellable = true)
    private void malilibui$drawScreenBackground(GuiContext ctx, int mouseX, int mouseY, CallbackInfo ci) {
        GuiBase screen = (GuiBase) (Object) this;
        ConfigScreenManager.ensureLayout(screen);

        if (ConfigScreenManager.hasLayout(screen)) {
            ConfigScreenManager.drawBackground(screen, ctx, mouseX, mouseY);
            ci.cancel();
        }
    }

    @Inject(method = "drawTitle", at = @At("HEAD"), cancellable = true)
    private void malilibui$drawTitle(GuiContext ctx, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (ConfigScreenManager.hasLayout((GuiBase) (Object) this)) {
            ci.cancel();
        }
    }

    @Redirect(method = "drawButtons",
            at = @At(value = "INVOKE",
                    target = "Lfi/dy/masa/malilib/gui/button/ButtonBase;" +
                            "render(Lfi/dy/masa/malilib/render/GuiContext;IIZ)V"))
    private void malilibui$renderButton(ButtonBase button, GuiContext ctx, int mouseX, int mouseY, boolean selected) {
        GuiBase screen = (GuiBase) (Object) this;

        if (ConfigScreenManager.isNavButton(screen, button) &&
                ConfigScreenManager.drawNavButton(screen, button, ctx, mouseX, mouseY)) {
            return;
        }

        button.render(ctx, mouseX, mouseY, selected);
    }
}
