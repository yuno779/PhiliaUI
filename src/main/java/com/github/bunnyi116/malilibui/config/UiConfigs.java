package com.github.bunnyi116.malilibui.config;

import com.github.bunnyi116.malilibui.MalilibUiMod;
import com.github.bunnyi116.malilibui.api.ConfigFactory;
import com.github.bunnyi116.malilibui.api.config.*;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.util.FileUtils;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 本模组自身的配置：所有配置项都在分组上创建（只填 key），通过链式构建器设置默认值 / 范围 / 可见性，
 * 工厂自动收集全部配置项（{@link #OPTIONS}），界面条目由分组提供。
 */
public class UiConfigs implements IConfigHandler {
    public static final UiConfigs INSTANCE = new UiConfigs();

    private static final String CONFIG_FILE_NAME = MalilibUiMod.MOD_ID + ".json";

    /**
     * 配置工厂：传入 modId（翻译前缀 {@code malilib-ui.config}）。
     */
    public static final ConfigFactory FACTORY = ConfigFactory.of(MalilibUiMod.MOD_ID);

    /**
     * 通用设置。
     */
    public static class General {
        public static final ConfigGroup GROUP = FACTORY.group("general");

        public static final UiConfigBoolean ENABLED = GROUP.bool("enabled")
                        .defaultValue(true)
                        .build();

        public static final UiConfigBoolean SEMI_TRANSPARENT = GROUP.bool("semiTransparent")
                        .defaultValue(true)
                        .build();

        public static final UiConfigInteger BACKGROUND_OPACITY = GROUP.integer("backgroundOpacity")
                        .defaultValue(45)
                        .range(0, 100)
                        // 关闭半透明背景后，背景不透明度不再有意义
                        .visibleWhen(SEMI_TRANSPARENT::getBooleanValue)
                        .build();
    }

    /**
     * 布局设置。
     */
    public static class Layout {
        public static final ConfigGroup GROUP = FACTORY.group("layout");

        public static final UiConfigInteger SIDEBAR_WIDTH =
                GROUP.integer("sidebarWidth")
                        .defaultValue(130)
                        .range(90, 260)
                        .build();

        /** 左上角徽章文字；留空则回退到默认文字。 */
        public static final UiConfigString BADGE_TEXT =
                GROUP.string("badgeText")
                        .defaultValue("Philia UI")
                        .build();

        /** 左上角徽章图标路径；支持绝对路径，或相对 malilib 配置目录。 */
        public static final UiConfigString BADGE_ICON =
                GROUP.string("badgeIcon")
                        .defaultValue("")
                        .build();
    }

    /**
     * 测试内容：多级依赖、条件可见性、构建器示例、控件展示。
     *
     * <p>这些配置项只用于界面演示（可见性联动 / 控件样式），本身不参与任何业务逻辑；
     * 它们的值由用户在界面中修改并保存到配置文件。</p>
     */
    public static class Test {
        // ---- 多级依赖 ----
        public static final ConfigGroup MULTI_GROUP = FACTORY.group("test.multi");

        public static final UiConfigBoolean MASTER = MULTI_GROUP.bool("testMaster")
                .defaultValue(true)
                .build();

        public static final UiConfigBoolean CHILD_A = MULTI_GROUP.bool("testChildA")
                .defaultValue(true)
                .visibleWhen(MASTER::getBooleanValue)
                .build();

        public static final UiConfigInteger CHILD_B = MULTI_GROUP.integer("testChildB")
                .defaultValue(50)
                .range(0, 100)
                .visibleWhen(MASTER::getBooleanValue)
                .build();

        public static final UiConfigBoolean GRANDCHILD = MULTI_GROUP.bool("testGrandchild")
                .defaultValue(true)
                // 多级依赖：总开关 && 子开关 && 子数量 > 0
                .visibleWhen(() -> MASTER.getBooleanValue()
                        && CHILD_A.getBooleanValue()
                        && CHILD_B.getIntegerValue() > 0)
                .build();

        // ---- 条件可见性 ----
        public static final ConfigGroup CONDITIONAL_GROUP = FACTORY.group("test.conditional");

        public static final UiConfigBoolean MODE_A =
                CONDITIONAL_GROUP.bool("testModeA")
                        .defaultValue(false)
                        .visibleWhen(MASTER::getBooleanValue)
                        .build();

        public static final UiConfigInteger MODE_A_OPTION =
                CONDITIONAL_GROUP.integer("testModeAOption")
                        .defaultValue(10)
                        .range(0, 20)
                        .visibleWhen(() -> MASTER.getBooleanValue() && MODE_A.getBooleanValue())
                        .build();

        public static final UiConfigBoolean MODE_B =
                CONDITIONAL_GROUP.bool("testModeB")
                        .defaultValue(false)
                        .visibleWhen(MASTER::getBooleanValue)
                        .build();

        public static final UiConfigString MODE_B_TEXT =
                CONDITIONAL_GROUP.string("testModeBText")
                        .defaultValue("hello")
                        .visibleWhen(() -> MASTER.getBooleanValue() && MODE_B.getBooleanValue())
                        .build();

        // ---- 构建器示例 ----
        public static final ConfigGroup BUILDER_GROUP = FACTORY.group("test.builder");

        public static final UiConfigBoolean BUILDER_MASTER =
                BUILDER_GROUP.bool("builderMaster").defaultValue(true).build();

        public static final UiConfigBoolean BUILDER_CHILD =
                BUILDER_GROUP.bool("builderChild")
                        .defaultValue(true)
                        .visibleWhen(BUILDER_MASTER::getBooleanValue)
                        .build();

        public static final UiConfigInteger BUILDER_VALUE =
                BUILDER_GROUP.integer("builderValue")
                        .defaultValue(5)
                        .range(0, 10)
                        .visibleWhen(BUILDER_MASTER::getBooleanValue)
                        .build();

        // ---- 控件展示（malilib 各类配置控件） ----
        public static final ConfigGroup WIDGETS_GROUP = FACTORY.group("test.widgets");

        public static final UiConfigBoolean WIDGET_BOOLEAN = WIDGETS_GROUP.bool("widgetBoolean")
                .defaultValue(true)
                .build();

        public static final UiConfigInteger WIDGET_INTEGER = WIDGETS_GROUP.integer("widgetInteger")
                .defaultValue(25)
                .range(0, 100)
                .build();

        public static final UiConfigDouble WIDGET_DOUBLE = WIDGETS_GROUP.doubleConfig("widgetDouble")
                .defaultValue(0.5)
                .range(0.0, 1.0)
                .build();

        public static final UiConfigFloat WIDGET_FLOAT = WIDGETS_GROUP.floatConfig("widgetFloat")
                .defaultValue(1.5f)
                .range(0.0f, 10.0f)
                .build();

        public static final UiConfigString WIDGET_STRING = WIDGETS_GROUP.string("widgetString")
                .defaultValue("hello")
                .build();

        public static final UiConfigColor WIDGET_COLOR = WIDGETS_GROUP.color("widgetColor")
                .defaultValue("#FF8800")
                .build();

        public static final UiConfigHotkey WIDGET_HOTKEY = WIDGETS_GROUP.hotkey("widgetHotkey")
                .defaultValue("G")
                .build();

        public static final UiConfigStringList WIDGET_STRING_LIST = WIDGETS_GROUP.stringList("widgetStringList")
                .defaultValue(ImmutableList.of("alpha", "beta", "gamma"))
                .build();

        public static final UiConfigOptionList WIDGET_OPTION_LIST = WIDGETS_GROUP.optionList("widgetOptionList")
                .defaultValue(TestOptionMode.OPTION_B)
                .build();

        public static final UiConfigBooleanHotkeyed WIDGET_BOOLEAN_HOTKEY = WIDGETS_GROUP.booleanHotkey("widgetBooleanHotkey")
                .defaultValue(false)
                .build();

    }

    /**
     * 无分组页面：配置项直接由工厂创建、界面平铺展示（没有分组标题）。
     */
    public static class Flat {
        public static final UiConfigBoolean ENABLED = FACTORY.bool("flatEnabled")
                .defaultValue(true)
                .build();

        public static final UiConfigInteger COUNT = FACTORY.integer("flatCount")
                .defaultValue(5)
                .range(0, 100)
                .build();

        public static final UiConfigBoolean CHILD = FACTORY.bool("flatChild")
                .defaultValue(false)
                .visibleWhen(ENABLED::getBooleanValue)
                .build();

        public static final UiConfigString TEXT = FACTORY.string("flatText")
                .defaultValue("hello")
                .build();

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(ENABLED, COUNT, CHILD, TEXT);
    }

    /**
     * 用于保存 / 加载的完整配置项列表（分组 + 无分组页面）。
     */
    public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.<IConfigBase>builder()
            .addAll(ConfigGroup.allConfigs(
                    General.GROUP,
                    Layout.GROUP,
                    Test.MULTI_GROUP,
                    Test.CONDITIONAL_GROUP,
                    Test.BUILDER_GROUP,
                    Test.WIDGETS_GROUP)
            )
            .addAll(Flat.OPTIONS)
            .build();

    @Override
    public void load() {
        Path configFile = FileUtils.getConfigDirectory().resolve(CONFIG_FILE_NAME);

        if (!Files.exists(configFile) || !Files.isRegularFile(configFile)) {
            return;
        }

        try (Reader reader = Files.newBufferedReader(configFile)) {
            JsonElement element = JsonParser.parseReader(reader);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();
                ConfigUtils.readConfigBase(root, "Generic", OPTIONS);
            }
        } catch (Exception e) {
            MaLiLib.LOGGER.error("Failed to load malilib-ui configs from '{}'", configFile, e);
        }
    }

    @Override
    public void save() {
        Path dir = FileUtils.getConfigDirectory();

        if (!Files.exists(dir)) {
            FileUtils.createDirectoriesIfMissing(dir);
        }

        if (Files.isDirectory(dir)) {
            JsonObject root = new JsonObject();
            ConfigUtils.writeConfigBase(root, "Generic", OPTIONS);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (Writer writer = Files.newBufferedWriter(dir.resolve(CONFIG_FILE_NAME))) {
                gson.toJson(root, writer);
            } catch (Exception e) {
                MaLiLib.LOGGER.error("Failed to save malilib-ui configs to '{}'", dir, e);
            }
        }
    }
}
