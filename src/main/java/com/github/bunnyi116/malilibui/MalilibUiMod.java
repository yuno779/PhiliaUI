package com.github.bunnyi116.malilibui;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.registry.Registry;
import fi.dy.masa.malilib.util.data.ModInfo;

import com.github.bunnyi116.malilibui.config.UiConfigs;
import com.github.bunnyi116.malilibui.gui.ConfigScreenManager;
import com.github.bunnyi116.malilibui.hotkey.UiKeybindProvider;
import com.github.bunnyi116.malilibui.gui.MalilibUiConfigScreen;

/**
 * malilib-ui：面向 masa 系列模组（所有使用 malilib {@code GuiConfigsBase} 的模组）
 * 配置界面的现代 Minecraft 风格重制。
 *
 * <p>本模组不 fork malilib，而是在运行时向 malilib 的 GUI 类注入，
 * 把原有配置界面重新排布为：左侧垂直 TAB 导航栏、右侧配置选项列表，
 * 并加入现代但保留原版风格的界面观感。</p>
 */
public class MalilibUiMod implements ClientModInitializer {
    public static final String MOD_ID = "malilib-ui";
    public static final String MOD_NAME = "Philia UI";
    public static final String DISPLAY_NAME = "Philia UI";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitializeClient() {
        // 本模组自身的配置（保存到 config/malilib-ui.json）。
        ConfigManager.getInstance().registerConfigHandler(MOD_ID, UiConfigs.INSTANCE);

        // 关闭「启用界面重制」时立即恢复原版布局（不必等切页或下一帧）。
        UiConfigs.General.ENABLED.setValueChangeCallback(
                config -> ConfigScreenManager.onRestyleEnabledChanged(config.getBooleanValue()));

        // 注册热键：让配置项里的热键（布尔 + 热键等）真正响应按键。
        InputEventHandler.getKeybindManager().registerKeybindProvider(new UiKeybindProvider());

        // 注册本模组自己的配置界面：使其同样出现在
        // 任意 masa 配置界面右上角的模组切换下拉框中。
        Registry.CONFIG_SCREEN.registerConfigScreenFactory(
                new ModInfo(MOD_ID, DISPLAY_NAME, MalilibUiConfigScreen::new));

        LOGGER.info("[{}] Initialized: masa config screen restyling enabled", MOD_NAME);
    }
}
