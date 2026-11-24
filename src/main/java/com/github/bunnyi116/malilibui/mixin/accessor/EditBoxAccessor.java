package com.github.bunnyi116.malilibui.mixin.accessor;

import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Minecraft 1.21.11+ 的 EditBox 把文字绘制坐标缓存在 textX/textY 中，
 * 只有通过 EditBox 自己的 setX/setY（或 setValue）才会重新计算。
 * MaLiLib 的 GuiTextFieldGeneric 覆盖了 setX/setY，直接改它自己的 x/y 字段，
 * 导致我们平移输入框后边框移动了、数字却留在旧位置。
 * 这里暴露原生刷新方法，让布局代码在移动后单独刷新文字缓存。
 */
@Mixin(EditBox.class)
public interface EditBoxAccessor {
    @Invoker("updateTextPosition")
    void malilibui$refreshTextPosition();
}
