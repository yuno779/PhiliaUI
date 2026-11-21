package com.github.bunnyi116.malilibui.gui;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.github.bunnyi116.malilibui.MalilibUiMod;
import com.github.bunnyi116.malilibui.config.UiConfigs;
import com.mojang.blaze3d.platform.NativeImage;

import fi.dy.masa.malilib.render.GuiContext;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.FileUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

/**
 * 绘制左上角徽章（图标 + 文字）。图标按路径懒加载，文件变化后自动重新注册。
 */
public final class BadgeRenderer {
    private static final int HEIGHT = 18;
    private static final int ICON = 14;
    private static final int ICON_TEX = 256;
    private static final int PAD = 5;

    private static final Map<String, IconTexture> ICONS = new HashMap<>();

    private BadgeRenderer() {
    }

    public static void draw(GuiContext ctx, int x, int y, int maxWidth) {
        String text = UiConfigs.Layout.BADGE_TEXT.getStringValue();

        if (text == null || text.isBlank()) {
            text = "MaLiLib UI";
        }

        IconTexture icon = loadIcon(UiConfigs.Layout.BADGE_ICON.getStringValue());
        Font font = ctx.fontRenderer();
        // 侧栏太窄时优先保证文字完整，隐藏图标。
        boolean showIcon = maxWidth >= 100;
        float scale = 1.0f;
        int iconPart = showIcon ? ICON + 5 : 0;
        int textW = Math.round(font.width(text) * scale);
        int width = Math.min(maxWidth, PAD * 2 + iconPart + textW + 2);
        int textX = x + PAD + iconPart;
        int textWidth = Math.max(10, width - PAD * 2 - iconPart);
        String label = UiTheme.trim(font, text, Math.max(8, Math.round(textWidth / scale)));

        if (showIcon) {
            if (icon != null) {
                drawIcon(ctx, icon, x + PAD, y + (HEIGHT - ICON) / 2);
            } else {
                drawFallbackIcon(ctx, x + PAD, y + (HEIGHT - ICON) / 2);
            }
        }

        // 不放外框/底色，仅图标 + 文字，贴近 DSH 左上角；文字略放大。
        int textY = y + (HEIGHT - 8) / 2;
        ctx.pose().pushMatrix();
        ctx.pose().scale(scale, scale);
        UiTheme.text(ctx, label, Math.round(textX / scale), Math.round(textY / scale),
                UiTheme.TEXT_TITLE, true);
        ctx.pose().popMatrix();
    }

    private static void drawIcon(GuiContext ctx, IconTexture icon, int x, int y) {
        // 纹理保持固定 256x256，绘制时按 ICON/ICON_TEX 缩放，避免大图撑爆界面。
        float scale = ICON / (float) ICON_TEX;
        ctx.pose().pushMatrix();
        ctx.pose().translate(x, y);
        ctx.pose().scale(scale, scale);
        RenderUtils.drawTexturedRect(ctx, icon.id, 0, 0, 0, 0, ICON_TEX, ICON_TEX);
        ctx.pose().popMatrix();
    }

    private static void drawFallbackIcon(GuiContext ctx, int x, int y) {
        UiTheme.fill(ctx, x, y, ICON, ICON, 0xFF24304F);
        UiTheme.outline(ctx, x, y, ICON, ICON, 0xFF4A5578);
        UiTheme.fill(ctx, x + 3, y + 3, 4, 4, UiTheme.TEXT_ACCENT);
        UiTheme.fill(ctx, x + 9, y + 3, 4, 4, 0xFF9BB4FF);
        UiTheme.fill(ctx, x + 3, y + 9, 4, 4, 0xFF9BB4FF);
        UiTheme.fill(ctx, x + 9, y + 9, 4, 4, UiTheme.TEXT_ACCENT);
    }

    private static IconTexture loadIcon(String configured) {
        if (configured == null || configured.isBlank()) {
            return null;
        }

        Path path = resolve(configured.trim());

        if (path == null || !Files.isRegularFile(path)) {
            return null;
        }

        try {
            long modified = Files.getLastModifiedTime(path).toMillis();
            String key = path.toAbsolutePath().toString().toLowerCase(Locale.ROOT);
            IconTexture cached = ICONS.get(key);

            if (cached != null && cached.modified == modified) {
                return cached;
            }

            Identifier id = Identifier.fromNamespaceAndPath(MalilibUiMod.MOD_ID,
                    "badge/" + Integer.toHexString(key.hashCode()) + "_" + modified);

            int width;
            int height;
            NativeImage target = new NativeImage(ICON_TEX, ICON_TEX, false);
            try (InputStream in = Files.newInputStream(path);
                 NativeImage source = NativeImage.read(in)) {
                int side = Math.min(source.getWidth(), source.getHeight());
                int sx = (source.getWidth() - side) / 2;
                int sy = (source.getHeight() - side) / 2;
                source.resizeSubRectTo(sx, sy, side, side, target);
                width = source.getWidth();
                height = source.getHeight();
            }

            DynamicTexture texture = new DynamicTexture(id::toString, target);
            Minecraft.getInstance().getTextureManager().register(id, texture);
            texture.upload();

            IconTexture created = new IconTexture(id, modified, width, height);
            ICONS.put(key, created);
            return created;
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static Path resolve(String configured) {
        try {
            Path path = Path.of(configured);

            if (!path.isAbsolute()) {
                path = FileUtils.getConfigDirectory().resolve(configured);
            }

            return path.normalize();
        } catch (Throwable ignored) {
            return null;
        }
    }

    private record IconTexture(Identifier id, long modified, int width, int height) {
    }
}
