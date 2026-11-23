package com.github.bunnyi116.malilibui.mixin.accessor;

import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * EditBox caches the text draw position in textX/textY and only refreshes it through
 * its own setX/setY (or setValue). GuiTextFieldGeneric overrides setX/setY without
 * calling that refresh, so moving the widget leaves the text at the old position.
 */
@Mixin(EditBox.class)
public interface EditBoxAccessor {
    @Invoker("updateTextPosition")
    void malilibui$refreshTextPosition();
}
