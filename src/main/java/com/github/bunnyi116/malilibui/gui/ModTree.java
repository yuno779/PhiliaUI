package com.github.bunnyi116.malilibui.gui;

import fi.dy.masa.malilib.registry.Registry;
import fi.dy.masa.malilib.util.data.ModInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 2.0 左侧模组树的只读快照。
 * 模组列表来自 MaLiLib 的配置界面注册表；每个模组已访问过的 TAB 名称会被缓存，
 * 这样未打开的模组也能在树里展开显示分组。
 */
public final class ModTree {
    private static final Map<String, List<String>> TAB_CACHE = new LinkedHashMap<>();

    private ModTree() {
    }

    public static void cacheTabs(String modId, List<String> tabs) {
        if (modId == null || modId.isEmpty() || tabs == null || tabs.isEmpty()) {
            return;
        }

        TAB_CACHE.put(modId, List.copyOf(tabs));
    }

    public static List<String> tabsFor(String modId) {
        return TAB_CACHE.getOrDefault(modId, List.of());
    }

    public static List<ModEntry> snapshot() {
        var registry = Registry.CONFIG_SCREEN;
        List<ModEntry> entries = new ArrayList<>();

        if (registry == null) {
            return entries;
        }

        for (ModInfo info : registry.getAllModsWithConfigScreens()) {
            if (info == null || info == ModInfo.NO_MOD) {
                continue;
            }

            entries.add(new ModEntry(info, tabsFor(info.modId())));
        }

        entries.sort(Comparator.comparing(entry -> entry.info().modName(), String.CASE_INSENSITIVE_ORDER));
        return entries;
    }

    public record ModEntry(ModInfo info, List<String> tabs) {
    }
}
