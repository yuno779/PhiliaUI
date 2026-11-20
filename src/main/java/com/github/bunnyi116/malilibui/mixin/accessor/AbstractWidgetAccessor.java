package com.github.bunnyi116.malilibui.mixin.accessor;

import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractWidget.class)
public interface AbstractWidgetAccessor {
    @Accessor("x")
    void malilibui$setWidgetX(int x);

    @Accessor("y")
    void malilibui$setWidgetY(int y);
}
