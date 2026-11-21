package com.github.bunnyi116.malilibui.compat;

import com.github.bunnyi116.malilibui.gui.MalilibUiConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * 供 MasaGadget 的右上角模组切换下拉框枚举本模组。
 * MasaGadget 通过 ModMenu 的 entrypoint 构建列表，没有这个入口就不会出现 MaLiLib UI。
 */
public class MalilibUiModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new MalilibUiConfigScreen().setParent(parent);
    }
}
