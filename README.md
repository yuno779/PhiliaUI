> [!NOTE]
> 当前版本已实现核心功能（左侧 TAB + 右侧选项的重制配置界面），支持 Minecraft 26.1 / 26.2 / 26.3 与 1.21.11，欢迎反馈问题与建议。

Philia UI
==========

**MaLiLib UI** 是一套面向 masa 系列模组（MaLiLib 生态）配置界面的重制项目。

它**不 fork MaLiLib**，而是在运行时通过 Fabric Mixin 注入到 MaLiLib 的 GUI 类中，把原本横向排列、风格陈旧的配置界面重新排布为：

- **左侧垂直 TAB 导航**：每个配置分类一个 TAB，选中项高亮并带左侧强调条；
- **右侧配置选项列表**：完整复用 MaLiLib 原有的配置控件（布尔、滑条、文本、快捷键、颜色、列表等），搜索框与滚动条重新定位；
- **现代化但符合 Minecraft 风格的视觉**：深色渐变面板、柔和阴影、主题按钮、行悬停高亮、搜索栏与图标，全部使用原版字体与像素对齐绘制，不引入自定义贴图依赖。

由于注入发生在 MaLiLib 的公共配置界面基类上，所有使用 `GuiConfigsBase` 的 masa 模组（Litematica、Tweakeroo、MiniHUD、Item
Scroller 等）都会自动获得新的界面布局。

支持的 Minecraft 版本
----------

| Minecraft | MaLiLib | 状态                  |
|-----------|---------|-----------------------|
| 1.21.11   | 0.27.20 | 已支持 / 1.21.x 验证版本 |
| 26.1      | 0.28.9  | 已支持                |
| 26.2      | 0.29.3  | 已支持                |
| 26.3      | 0.30.1  | 已支持 / 主力验证版本 |

> 1.21–1.21.10 暂不支持。这些版本的 MaLiLib 仍使用 `DrawContext`，且 1.21.1–1.21.8 的
> `RenderUtils` / `GuiBase.drawButtons` 签名各不相同；本模组的绘制层基于 1.21.11 引入的
> `GuiContext`，回补到旧版本需要重写整个绘制层并为每个版本拆分分支。
> 26.x 与 1.21.11 的 GUI 类字段/方法签名集合一致，仅在渲染方法入口上做一处版本分支。

前置模组
----------

必需：

- [Fabric Loader](https://fabricmc.net/)（`loader_version` 见 `gradle.properties`）
- [MaLiLib](https://modrinth.com/mod/malilib)（对应上表版本）

建议同时安装任意 masa
系模组（如 [Litematica](https://modrinth.com/mod/litematica)、[Tweakeroo](https://modrinth.com/mod/tweakeroo)），以便查看配置界面效果。

界面预览
----------

| 通用配置 TAB                                 | 视效配置 TAB                                 |
|----------------------------------------------|----------------------------------------------|
| ![通用配置](docs/preview/config-general.png) | ![视效配置](docs/preview/config-visuals.png) |

| mod 切换下拉（展开，置顶显示）                | 本模组配置界面                                  |
|-----------------------------------------------|-------------------------------------------------|
| ![mod切换](docs/preview/mod-dropdown.png)     | ![本模组配置](docs/preview/malilibui-config.png) |

| 快捷键高级设置对话框（标题显示翻译名）          | StringList 编辑对话框（标题显示翻译名）           |
|-------------------------------------------------|---------------------------------------------------|
| ![快捷键对话框](docs/preview/keybind-dialog.png) | ![StringList](docs/preview/stringlist-dialog.png) |

| 搜索栏展开                                      | 配置行悬停                            |
|-------------------------------------------------|---------------------------------------|
| ![搜索栏展开](docs/preview/search-expanded.png) | ![行悬停](docs/preview/row-hover.png) |

| 修改配置后该行「重置」按钮亮起（与禁用状态一眼可辨） |
|------------------------------------------------------|
| ![重置按钮状态](docs/preview/reset-states.png)       |

| 「全部」TAB（快捷键筛选搜索栏）             | 普通 TAB（普通搜索栏）                                  |
|---------------------------------------------|---------------------------------------------------------|
| ![全部TAB](docs/preview/config-all-tab.png) | ![普通搜索栏](docs/preview/config-plain-search-tab.png) |

| 渲染层编辑界面（与配置界面统一风格）      |
|-------------------------------------------|
| ![渲染层](docs/preview/render-layers.png) |

> 两种搜索栏变体（带快捷键筛选的 20px 高变体与 14px 高的普通变体）现在会被统一绘制为同尺寸、同位置的搜索框，切换 TAB 时不再跳动。

功能特性
----------

- [x] 左侧垂直 TAB 导航（从各 masa 模组原有的顶部 TAB 按钮迁移而来，点击行为完全保留）
- [x] 右侧配置选项列表 + 主题化滚动条
- [x] 搜索栏重定位与背景绘制、整条点击展开、搜索提示文案；两种搜索栏变体统一尺寸、图标与文本框垂直居中
- [x] 深色渐变主面板、侧栏分隔、面板阴影与描边、内容区内嵌列表面板
- [x] 背景面板半透明（可透出游戏画面，可在本模组配置中关闭）
- [x] 配置行内的按钮 / 控件保持原版 masa 风格（原版浅灰纹理，禁用态区分明显）
- [x] 配置行交替背景、整行悬停高亮（行首强调条位于列表左侧留白，与配置名称保持间距，不与文字重叠）、分组标题样式
- [x] 渲染层编辑界面（`GuiRenderLayerEditBase`，如 Litematica 的「渲染层」）同样套用侧栏 TAB + 内容区布局
- [x] mod 切换下拉迁移到内容区标题行右侧，展开时置顶显示（不被配置列表遮挡）
- [x] 本模组自身的配置界面已注册到 mod 切换下拉框（启用界面重制 / 半透明背景 / 侧栏宽度）
- [x] 配置相关对话框标题显示翻译后的配置名（快捷键高级设置、StringList / LockedList / ColorList / Table 编辑等）
- [x] 窗口缩放自适应、小窗口回退原版布局
- [x] 多版本整合包构建（fabricWrapper）
- [x] 开发期自动截图验证脚本（`scripts/verify-gui.ps1`）

实现原理（简述）
----------

所有注入都限定在 `GuiConfigsBase`（masa 配置界面基类）或 `GuiRenderLayerEditBase`（渲染层编辑界面）实例的上下文中：

| 注入点                                                   | 作用                                                                  |
|----------------------------------------------------------|-----------------------------------------------------------------------|
| `GuiBase.initGui` TAIL / `drawScreenBackground` HEAD       | 确保布局已应用（TAB 切换直接调用 `initGui()`，不经过 `init()`） |
| `GuiBase.drawHoveredWidget` TAIL                          | 内容绘制完之后重绘展开的 mod 切换下拉框，避免被配置列表遮挡 |
| `GuiBase.drawScreenBackground` HEAD（cancel）            | 绘制新面板背景、侧栏、内容区、标题、装饰线                            |
| `GuiBase.drawTitle` HEAD（cancel）                       | 由新布局自行绘制标题                                                  |
| `GuiBase.drawButtons` Redirect                           | 把原 TAB 按钮绘制为左侧导航项（按钮对象与点击回调保持原样）           |
| `GuiConfigsBase.getBrowserWidth/getBrowserHeight` RETURN | 让重建的列表与搜索栏使用右侧内容区尺寸                                |
| `WidgetConfigOption.render` HEAD                         | 配置行背景 / 悬停高亮 / 分组标题                                      |
| `WidgetSearchBar.render` HEAD / 点击                     | 统一绘制搜索栏背景、整条点击展开、图标与文本框垂直居中                |
| `GuiScrollBar.render` HEAD                               | 主题化滚动条颜色                                                      |
| 各配置对话框构造器 RETURN                                | 配置对话框标题从原始配置名改为翻译显示名                              |
| `GuiRenderLayerEditBase` 布局管理                        | TAB 移入侧栏、其余控件整体平移到内容区（按控件对象身份只平移一次）    |

详细调研报告见 [docs/research/malilib-gui.md](docs/research/malilib-gui.md)。

开发者 API
----------

本模组提供公开 API，供其他模组扩展重制后的配置界面（动态可见性、分组折叠等），
第三方模组可以在 `fabric.mod.json` 中通过 `"malilib-ui"` 依赖本模组后直接调用：

- 完整文档：[docs/api.md](docs/api.md)
- API 入口：`com.github.bunnyi116.malilibui.api.MalilibUiApi`
- API 版本：`MalilibUiApi.API_VERSION`（当前为 `1`）

```java
// 动态可见性：master 关闭时隐藏 dependent
MalilibUiApi.registerVisibility(dependentConfig, () -> masterConfig.getBooleanValue());

// 分组折叠控制（分组标题为列表中的 LABEL 文本）
MalilibUiApi.setGroupCollapsed("litematica", "通用", true);
```

安装
----------

1. 安装对应 Minecraft 版本的 Fabric Loader 与 MaLiLib；
2. 将 `malilib-ui` 的 jar 放入 `mods/` 目录：
    - `malilib-ui-<version>-versionpack.jar`：多版本整合包（内部按 Minecraft 版本选择对应模块，推荐）；
    - 或 `versions/<mcver>/` 下构建出的单版本 jar。
3. 启动游戏，打开任意 masa 模组的配置界面查看效果。

构建
----------

项目使用 Gradle（wrapper 已包含），运行 Gradle 需要 JDK 25（Fabric Loom 1.19 的要求）；
编译目标由 `mod-plugin` 按 Minecraft 版本自动选择，1.21.11 为 Java 21，26.x 为 Java 25。
多版本源码由 preprocessor 从根目录 `src/main/` 分发到 `versions/<mcver>/`。

```bash
# 构建单个版本（产物：versions/26.3/build/libs/）
gradlew :26.3:build

# 构建全部版本 + 多版本整合包（产物：fabricWrapper/build/libs/）
gradlew build

# 启动开发客户端（以 26.3 为例）
gradlew :26.3:runClient
```

多版本整合包位于 `fabricWrapper/build/libs/`，单版本 jar 位于 `fabricWrapper/build/libs/jars/`。

开发与验证
----------

- 源码根目录：`src/main/java/com/github/bunnyi116/malilibui/`
- 资源根目录：`src/main/resources/`（`fabric.mod.json`、`malilib-ui.mixins.json`、`assets/malilib-ui/`）
- Mixin 包名：`com.github.bunnyi116.malilibui.mixin`
- 版本工程（`versions/26.x`）只保留各自的 `gradle.properties`，源码与资源由 preprocessor 自动分发
- `fabricWrapper` 为多版本整合包工程：收集各版本 jar 并生成带 `jars` 列表的 `fabric.mod.json`
- 开发期视觉验证：设置 `MALILIB_UI_AUTOTEST=litematica` 启动 `runClient`，客户端到标题屏后会自动打开 litematica 配置界面；
  `scripts/verify-gui.ps1` 可一键完成构建、启动、截图与清理，详见 [scripts/README.md](scripts/README.md)

致谢
----------

- [masa](https://github.com/maruohon/malilib)：MaLiLib 及全部 masa 系模组；
- [sakura-ryoko](https://github.com/sakura-ryoko/malilib)：适配新版 Minecraft 的 MaLiLib 分支；
- [aleksilassila/litematica-printer](https://github.com/aleksilassila/litematica-printer)：本项目的构建骨架来源于该项目的分支；
- [bunnyi116](https://github.com/bunnyi116)：项目维护。

许可证
----------

[AGPL-3.0](LICENSE.md)
