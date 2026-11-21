package com.github.bunnyi116.malilibui.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListBase;

/**
 * 閲嶅埗鍚庣殑閰嶇疆鐣岄潰 / 娓叉煋灞傜晫闈㈢殑姣忓睆骞曞彲鍙樼姸鎬併€?
 *
 * <p>鎵€鏈夊瓧娈典粎鍦ㄦ覆鏌撶嚎绋嬭闂€?/p>
 */
public final class ConfigScreenState {
    public final GuiBase screen;

    /**
     * 褰撳墠甯冨眬鍑犱綍锛涢娆″簲鐢ㄥ墠涓?null銆?
     */
    public ConfigLayout layout;

    /**
     * masa 妯＄粍娣诲姞鍒板睆骞曢《閮ㄧ殑鍘熷 TAB 鎸夐挳銆?
     * 淇濈暀鍚屼竴鎵规寜閽璞★紙纭繚鐐瑰嚮鍥炶皟浠嶇劧鏈夋晥锛夛紝浠呭皢鍏剁Щ鍔ㄥ埌宸︿晶瀵艰埅鏍忋€?
     */
    public final List<ButtonBase> navButtons = new ArrayList<>();

    /**
     * 褰撳墠瀹夎鍦ㄧ晫闈腑鐨勯厤缃€夐」鍒楄〃鎺т欢銆?
     */
    public WidgetListBase<?, ?> listWidget;

    /**
     * 宸茬粡杩佺Щ鍒板唴瀹瑰尯鐨勯潪 TAB 鎺т欢锛堝熀浜庡璞¤韩浠借褰曪紝浠ヤ究 {@code initGui()} 閲嶅缓鐨?
     * 鏂版帶浠朵細鍐嶆澶勭悊锛岃€屽凡杩佺Щ鐨勬帶浠朵笉浼氳閲嶅骞崇Щ锛夈€?
     */
    public final Set<Object> positionedControls = Collections.newSetFromMap(new IdentityHashMap<>());

    /**
     * 甯冨眬棣栨鎴愬姛搴旂敤鍚庣疆涓?true銆?
     */
    public boolean ready;

    /** 2.0 宸︿晶鏍戞槸鍚﹀凡缁忓湪褰撳墠鐣岄潰瀹炰緥涓婂垱寤鸿繃鎸夐挳銆?*/
    public boolean treeBuilt;

    /** 宸︿晶鏍戠殑妯＄粍琛屾寜閽紝鐢ㄤ簬鍒ゆ柇鐣岄潰閲嶅缓鍚庢槸鍚﹂渶瑕侀噸鍔犮€?*/
    public final List<ButtonBase> treeButtons = new ArrayList<>();

    /** 鏍戣鎸夐挳瀵瑰簲鐨勭粯鍒舵暟鎹€?*/
    public final Map<ButtonBase, TreeRow> treeRows = new IdentityHashMap<>();

    /** 宸︿晶妯＄粍鏍戠殑婊氬姩鍋忕Щ涓庡唴瀹规€婚珮搴︺€?*/
    public int treeScroll;
    public int treeContentH; public boolean treeScrollAdjusted;

    public record TreeRow(String name, String version, boolean selected, boolean tab) {
    }

    public ConfigScreenState(GuiBase screen) {
        this.screen = screen;
    }
}
