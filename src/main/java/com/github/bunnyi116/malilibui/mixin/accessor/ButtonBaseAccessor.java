package com.github.bunnyi116.malilibui.mixin.accessor;

import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ButtonBase.class)
public interface ButtonBaseAccessor {
    @Accessor("displayString")
    String malilibui$getDisplayString();

    @Accessor("enabled")
    boolean malilibui$isEnabled();

    @Accessor("actionListener")
    IButtonActionListener malilibui$getActionListener();

    @Accessor("visible")
    void malilibui$setVisible(boolean visible);

    @Accessor("visible")
    boolean malilibui$isVisible();
}
