package com.github.bunnyi116.malilibui.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.GuiRenderLayerEditBase;
import fi.dy.masa.malilib.gui.GuiScrollBar;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import fi.dy.masa.malilib.gui.widgets.WidgetDropDownList;
import fi.dy.masa.malilib.gui.widgets.WidgetListBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptionsBase;
import fi.dy.masa.malilib.gui.wrappers.TextFieldWrapper;
import fi.dy.masa.malilib.render.GuiContext;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.data.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import com.github.bunnyi116.malilibui.MalilibUiMod;
import com.github.bunnyi116.malilibui.config.UiConfigs;
import com.github.bunnyi116.malilibui.mixin.accessor.ButtonBaseAccessor;
import com.github.bunnyi116.malilibui.mixin.accessor.GuiBaseAccessor;
import com.github.bunnyi116.malilibui.mixin.accessor.GuiConfigsBaseAccessor;
import com.github.bunnyi116.malilibui.mixin.accessor.GuiListBaseAccessor;
import com.github.bunnyi116.malilibui.mixin.accessor.WidgetListBaseAccessor;
import com.github.bunnyi116.malilibui.mixin.accessor.WidgetListConfigOptionsBaseAccessor;
import net.fabricmc.loader.api.FabricLoader;

/**
 * 闂佸搫鐗滈崜姘冲暞缂傚倷绀佺€氼喖鈻撻幋锕€鍐€缂佸娉曟俊鍥煥濞戞瑧顣查悽顖涙尦閺?malilib 闂佹眹鍔岀€氼剛鏁锝嗗弿閻庯綆鍓氬▍鏇㈡⒒閸偅绶涚紒杈ㄧ箞閺屽苯顓奸崱妯煎帎闂佸湱鍎ょ敮鎺旂博閻戣姤鏅?
 * 闂佺偨鍎茬划灞剧▕韫囨稒鏅?TAB 闁诲簼绲绘竟鍫ュ春?+ 闂佸憡鐟ラ崢鏍疾閸洖绀冮柛娑卞弾閸熷洭鏌涢弽顑锯偓鈧柍褜鍓氱换鎰濠靛宓侀柛鎾茶兌鐢盯鏌涢幒鏇炵厫闁哄苯锕幆鍐礋椤撶喐鐝梻鍌氱墑閸ㄥジ藟婵犲喚娈伴悽顖ｅ枟鐎?
 *
 * <p>婵炴潙鍚嬬喊宥囨暜鐎靛摜涓嶉柍銉ㄥ蔼閳峰牏绱掗銉ュ惞闁哄拋鍋婂Λ鍐綖椤斿墽鐛?/p>
 * <ul>
 *   <li>{@link GuiConfigsBase}闂佹寧绋掗懝楣冩偉閸洘鏅?masa 闂備焦婢樼粔鍫曟偪閸℃稒鍋╃€光偓閸曨剚銆冮梺闈涙閸婂潡鍩€椤掆偓椤︾敻濡村澶婄婵°倕瀚ㄩ埀顒€鍟撮獮鎺曨槺婵炲棎鍨虹€电厧顫濋幇浣硅晧缂備礁顦抽褎鎱?
 *       闂佹寧绋戦悧鍡涙嚐閻旂厧鎹堕柕濞炬櫆娴犳﹢鎮烽弴姘卞妽婵＄偛鍊块弻灞筋吋閸涱垳澶勯梺鎸庣☉椤︻垶宕哄畝鍕闁告侗鍙庨崯鍥煕閺嶎偄鐨戠紒杈ㄧ箞楠炴牕顭ㄩ崨顓炰憾濠碘剝顨呭Λ鏃傜箔瀹€鍕厐鐎广儱娲ㄩ弸鍌炴偠濞戞鐒告繛鍫涘灪缁嬪﹦鈧綆鍠栧▍銏ゆ煛閸屾稒绶茬紒鏃堫棑娴狅箓鍩€椤掑嫭鏅?/li>
 *   <li>{@link GuiRenderLayerEditBase}闂佹寧绋掗崺鍐焵椤掍胶鐭嬮悷鏇炴瀵骞樺畷鍥ㄥ嬀闂佺偨鍎茬换鍕椤忓懏缍囬柟瀛樼箖閻濄倝鏌ｉ敐鍛劯婵為棿鍗抽弫宥夊醇濠婂懍绱ｉ梺?litematica 闂?
 *       {@code GuiRenderLayer}闂佹寧绋戦ˇ顓㈠焵椤戣法绠揂B 闂佸湱顭堥ˇ鐢稿箰瀹曞洦鍋栨い鎰╀紗閳哄懎绀嗛柡澶娿仒缁狀垰銆掑鈧崘鎯ф闂佺厧澹婃禍婵嬫偉椤曗偓閺佸秶浠﹂挊澶婄翻婵炶揪绲鹃悷锔炬暜閼哥數顩烽柤绋跨仛濞堣霉閿濆棙灏梺瑙ｆ櫇缁梻鎷犻幓鎺斿帓闂佸憡鍔曢幊搴敊閹版澘绀岄柣姘摠鐎?/li>
 * </ul>
 *
 * <p>闂佸憡顭囬崰搴綖?TAB 闂佸湱顭堥ˇ鐢稿箰瀹曞洠鍋撻悽娈挎敯闁芥牕瀚粚鍗炩攽閸パ呮Х闁诲孩绋掕摫婵＄偛娼￠弫宥囦沪閼测晝鐓傜紓浣割槼椤曆勬叏閳哄倹濯寸€广儱娲ㄩ弸鍌炴煥濞戞瑧鈽夐柛娆撶畺閹瑩鎮烽弶鎸庣槚闂佹悶鍎抽崑鐘绘儍閻斿摜顩风€广儱妫楅埛鏃堟偠濞戞瀚伴悽顖ｅ亰瀵爼濡疯閻ｉ亶鏌涢幒鎴炲鐎规洘鐓￠弻鍛村及韫囨洖绔奸梺?
 * 闂佹眹鍨硅ぐ澶岃姳?masa 濠碘槅鍨界槐鏇犲垝瀹ュ绀嗛柛銉ｅ妼鎼?TAB 闂佸搫鍟﹢鍦娴兼潙绀冪€广儱妫崑褔鎮圭€ｎ亜鏆熼柡?{@code initGui()}闂?
 * 闁汇埄鍨伴崯顐︽儑椤掍礁顕辨慨姗嗗墯闊剚鎱ㄩ敐鍛闁硅埖鑹鹃妴鎺楀矗婢跺苯甯梺鍛婃尭缁夌數鈧灚绮撳Λ渚€鍩€椤掑嫭鐓傜€广儱妫欓悡鈧柟鐓庣摠濮婂寮妶澶嬫櫢?/p>
 */
public final class ConfigScreenManager {
    /**
     * 闂佸搫顑勭粈渚€鎯冮鍌滅幓婵°倐鍋撶憸鐗堢洴閹啴宕熼鍡欘啍闂佺缈伴崐婵單ｉ崶顒€纾圭痪顓㈩棑缁愭霉閿濆懐鍑圭紒顭戝墮椤垽濡堕崨顔筋潊闁诲酣娼ч幉鈥愁渻閸岀偛绠ョ€广儱妫欓弶鍦偓娈垮枛閸熸挳宕抽幖浣哥閻犻缚娅ｉ鎾倶閻愨晛浜鹃梺鎸庣☉閻楀﹪顢楀鍛殰闁告縿鍎崇槐姘舵煛閳ь剟宕ｉ妷褏鎲规繛鏉戝悑娣囬缚顤傞梺鍛婄懄閸╁﹦妲愬鑸垫櫢?
     */
    private static final int MIN_SCREEN_W = 240;
    private static final int MIN_SCREEN_H = 160;

    /**
     * 婵炶揪绲界粔宕囪姳椤掑嫬鍌ㄩ柣鏃傤儠閳ь剙锕弻鍫ュΩ閿斿€熸嫬婵炴垶鎼╅崢褰掑焵椤戣法绐旈柣婵愬枟閹棃鏁傜捄銊ф喒闁烩剝甯掗幊鎾舵崲閸愵亝瀚氶柕澶堝劗閸嬫捇鎳栭埡鍌滄殸闂佸湱顭堥ˇ鐢稿箰瀹曞洦鍋栨い鎰╁労濞兼帡鏌?TAB 闂佸湱顭堥ˇ鐢稿箰閹惰姤鏅?
     */
    private static final int TAB_STRIP_MAX_Y = 46;
    private static final int TAB_MAX_HEIGHT = 22;

    /**
     * 濠电偞鎸稿鍫曟偂鐎ｎ兛娌柛灞剧☉娴犳ê霉閻樼儤纭炬繝鈧鍫濆偍闁绘柨鎼。濂告煟閿濆懐鐒告繛鑲╁缁嬪顢橀悩宕囨殸闂佸憡甯楃换鍌烇綖閹邦厽濯寸€广儱娲ㄩ弸鍌炴煥?
     */
    private static final int RENDER_LAYER_CONTROLS_X = 10;
    private static final int RENDER_LAYER_CONTROLS_Y = 60;

    /**
     * 闂佸憡鍔曢幊搴敊閹版澘鐏虫繝濠傚暙濮ｅ牊淇婇懠缂庢帡鍩€椤掍胶绠炴繛闂村嵆瀵爼宕归鍊熷惈闂佸憡鐟﹂〃鍫ヮ敇閹间礁绠崇憸灞解枎閵忊剝缍囬柛锔诲幗濞呮洖顭块懜鐢靛濠⒀冩健閹啴宕熼銏犲缂備浇浜慨鐢稿汲閻斿吋鏅?
     */
    private static final int WELL_MARGIN = 6;

    private static final Map<Screen, ConfigScreenState> STATES = new WeakHashMap<>();

    /** 闁荤姳鐒﹀妯肩礊瀹ュ懌鈧帡宕ｆ径灞藉脯闁诲繒鍋涢崐鐣屾椤撱垹绀傞柕澶涘瘜閺€閬嶆煟閵娿儱顏╅悽顖ｅ亝閹便劎鈧綆浜濈紞蹇涙煛瀹ュ懏绁紒杈ㄧ箓琚欓煫鍥ㄦ閸嬔囨偨椤栨艾鏆欓柣顏嶅墴閺屽牓鎳為妷銉ф▎闂佸憡顭囬崰搴綖閹版澘閿ら柟閭﹀幘閸ㄥジ姊洪幓鎺斝ｉ柡灞斤躬瀹曟垼绠涢弴鍛櫊閺佸秴鐣濋崟顏嗙礆闂佺绻愮粔鐢垫閹惧墎鐭撻悹浣哥－閻熴垽鏌￠崒婊冾暭閻㈩垵鍩栫粋鎺楁嚋閻㈢數顢呯紓浣割槷缁鳖噣骞?*/
    private static final Map<GuiTextFieldGeneric, int[]> STANDALONE_FIELD_ORIGINS = new WeakHashMap<>();

    /** 闂佸搫瀚烽崹浼村箚娓氣偓瀵灚寰勭€ｎ剚瀚ч梺绉嗗嫷娈曟い鎺撶矒瀹曠兘濡搁…鎴炶晧闂佸綊鏀遍悧鏇熸叏閳哄懏鏅柛顐ゅ枎閻濇盯鏌涢弬璇插婵犫偓閿熺姵鈷掗柡澶嬪灥閺嬫瑧绱撴担瑙勫碍缂佹棃顥撴禒锕傚焵椤掑嫭鐓傜€广儱鎳愮槐锕傛煥濞戞鐒稿ù灏栨櫊瀹曟顓奸崱妤冨綄闂佺懓鍢查埀顒傚櫏濞艰泛鈽夐幙鍐ㄥ箺闁哄苯娲弫宥嗗緞濞戞瑥顏?*/
    private static boolean sliderDragging;

    /** 闂佸搫鐗冮崑鎾诲级閳哄倸鐏ｇ紒鏃€娼欓埢搴ㄥ焺閸愵亝瀚ч梺鍝勵槴閸撴繄鎲伴崱娑樿摕闁规崘鍩栭悾閬嶆煛閸愩劎鍩ｆ俊顐㈡健楠炲绮甸崷顓狀槱闂佹椿娼块崝瀣姳椤掑嫬绀嗛柕鍫濇閻掍粙鏌熼柨瀣閻庢艾娼″浼搭敍濮橆厼鍓ㄦ繛瀵稿Т缁夋潙锕㈤鍛氦婵炴垶绮犻弨浠嬫煥濞戞﹩妾ч柟?*/
    private static long lastSliderRenderTime;

    /** 闁烩剝甯掗幊鎾舵崲閸愵亝瀚氶柕澶涢檮椤ρ囨⒒閸屾氨鎽犻柣銉ユ嚇瀵灚寰勭€ｎ剚瀚ч梺鍝勵槴閸撴繄鎲伴崱娑樿摕闁瑰灝瀚粈澶愭倶韫囨氨绨挎い鎾剁帛缁嬪鎮滃Ο铏瑰綄闂佸湱鏌夊Λ鍕礄閿涘嫮纾奸煫鍥ㄦ礈濞夈垽鏌℃径灞芥珢缂佽鲸鐟╅弻鍡涘垂椤旂厧璧嬮梺鍛婂姌瀹曠數鍒掗妸鈺佺闁斥晛鍠氶崝銉╂煥濞戞﹩妾ч柟?*/
    private static final long SLIDER_DRAG_TIMEOUT_MS = 200L;

    private ConfigScreenManager() {
    }

    public static void setSliderDragging(boolean dragging) {
        sliderDragging = dragging;

        if (dragging) {
            lastSliderRenderTime = System.currentTimeMillis();
        }
    }

    public static void markSliderRendering() {
        lastSliderRenderTime = System.currentTimeMillis();
    }

    public static boolean isSliderDragging() {
        if (sliderDragging && System.currentTimeMillis() - lastSliderRenderTime > SLIDER_DRAG_TIMEOUT_MS) {
            // 濠电姴锕ラ崹璺侯焽椤栨凹鍟呴棅顐幘閻熸繈鏌涢幇顒傂ｉ悷鏇炴瀵骞欓崟顓狀槱婵炴挻鑹鹃鍛淬€呰閹儳鐣濋崟顒併€冮梻浣瑰絻缁夊绱撻幘鍨涘亾娴ｅ啫顥嶉柛銇卞洤绠崇憸灞解枎閵忋倖鐓ラ柍褜鍓欒濞撴艾锕︾粈鍡涙煥濞戞鐒锋い顐㈩儐缁嬪鎮滃Ο铏瑰綄闂佸綊顥撻弲顐ゅ垝閵娾晛绾ч柣鏇炲€荤粈?
            // 闂備緡鍓欓悘婵嬪储閵堝洦鏆滈柛鎰╁妿濠€浼存煕閹邦垰鍚圭紒顔哄姂瀹曪繝鍩勯崘鎯уГ闁诲簼绲绘竟鍫ュ吹瑜旈幃鎯х暆閸曨剚銆冩繛鎴炴尭缁夋挳宕埀顒勬煛閸パ呮憼闁哄苯锕弫?
            sliderDragging = false;
        }

        return sliderDragging;
    }

    /* ------------------------------------------------------------------ *
     *  闁汇埄鍨伴崯顐︽儑椤掍礁绶為柛鏇ㄥ幗閸婄偤鏌ㄥ☉妯煎闁伙讣绱曢弫顔款槺閻犳劗鍠栧畷锝呂旈崘鎯ф倎闂佷紮绲介惌鍌氼焽閹殿喗瀚柛鎰典簼閺嗗繘鏌?
     * ------------------------------------------------------------------ */

    public static void ensureLayout(GuiBase screen) {
        if (isManageDisabled(screen)) {
            // 缂備礁鍊烽懗鍫曞极閵堝鐓傜€广儱鎳庨悡?/ 缂備焦鍔栭〃鍛般亹濞戙垹鍑犲ù锝呮憸濮ｅ牓鏌￠崘鈺傛瀯缂佹顦伴幆鏃堝籍閸屾稒娈㈤柣銏╁灠閸燁偊鎯囬鍕櫖婵炴垶锚閺傃囨煟濡も偓閻楀﹥绂掗钘夌窞鐎广儱鎳忛煬顒勬倵楠炲灝鐏柛鈺傜洴閺?tick 婵炴垶鎼╅崢濂杆囬埡鍛仩闁糕槅鍘界€?
            return;
        }

        if (isSliderDragging() && STATES.containsKey(screen)) {
            // 闂佸綊鏀遍悧鏇熸叏閳轰緡鐓ラ柟瀵稿仦閽傚鏌ㄥ☉妯煎缂佽翰鍎叉穱濠囧磼濞嗘劖鐝烽梺鍝勭Т缁绘劙顢欓弮鈧幆鏃堟晜閼测晝顦梺鍝勭墢閸犳牕螞閸ф绀冮柤濮愬€楀▔銏ゆ偨椤栨艾鏆欓柣顏嶅墯閹棃寮崒娑欐闂佹寧绋戦惌鍌涘閳哄懎绀傜€广儱鎳庨悘娆撴偠濞戞牕濮傞柛锝囶焾椤曟瑩鎮滃Ο琛″徍闂佸搫鍋嗛崢鐣屸偓姘儔楠炲繘寮妶鍥ц€?
            // 闂佸搫顦遍崑鎾存櫠濠婂牆瑙﹂幖杈剧稻閻ｅ崬鈽夐幘鎰佸剮缂佹柨顕弫顔款槺缂佸彉鍗抽幊娑㈩敂閸曨倣妤呭箹鐎涙ɑ鈷掗柡浣靛€濆顒勫级鐠侯煈浼岄柣蹇曞亹閸嬫捇鏌?
            return;
        }

        if (screen instanceof GuiConfigsBase configs) {
            ensureConfigsLayout(configs);
        } else if (screen instanceof GuiRenderLayerEditBase) {
            ensureRenderLayerLayout(screen);
        }
    }

    /**
     * 闁诲骸绠嶉崹娲春濞戙垺鏅?tick闂佹寧绋掗懝楣兯囬埡鍛仩闁糕剝鐟ú锝夋煟椤剙濮傞柛锝嗘倐閺?/ 闂佸搫顑勭粈渚€鎯冮鍌滅幓婵°倐鍋撶憸鐗堢洴瀵噣宕滄担鍦殸闂佸憡顭囬崰鎾存櫠濡ゅ懎绠掗柕蹇曞濡插鏌?
     *
     * <p>闂佽鍘归崹褰捤囬弻銉︻梿闁逞屽墰閹茬増绗熼埀顒勫闯缁嬫鍤堥柧蹇氼潐濞呮洟姊婚崼顐ｇ窙缂佽鲸顎侤code initGui()}闂佹寧绋戦¨鈧紒杈ㄧ箚缁犳盯宕ㄥǎ顑藉亾韫囨稒鏅?tick 闂傚倸鍟抽崺鏍敊瀹€鍕鐟滄垿銆侀幋锔藉殌閻忕偠鍋愰悷婵嬫煛閸曢潧鐏￠悷鏇炴瀵骞樼紒妯锋瀱濠电偛鐗忛。顔炬?
     * 闂佸憡鐔粻鎴﹀垂椤栫偞鍋╃€光偓閸曨剚銆冮梺鍛婄懐閸ㄧ敻宕楀鈧畷鎴濐煥閸涱喗鐣梺闈╄礋閸斿秴螞椤愩倖鏆滈柛鎰╁妿濠€浼存煥濞戞澧旂紒妤€鐭傚畷姘跺炊閵娿儱绨?TAB 闂佹眹鍔岀€氼垶鎯岄幆褜鍤楅柛鏇ㄤ簽缁犱粙鏌熼梹鎰樂缂佹梹鎸抽幊娑欐綇閸撗咁槴闂?/p>
     */
    public static void onClientTick() {
        Screen current = GuiUtils.getCurrentScreen();

        if (!(current instanceof GuiBase screen) || !STATES.containsKey(screen)) {
            return;
        }

        if (isManageDisabled(screen)) {
            restoreVanillaLayout(screen);
            return;
        }

        // 闂佸憡鏌ｉ崝宥夊焵椤戣法顦︾憸鎷屽皺閹插瓨鎷呴崗澶婁壕鐟滃秷銇愭径鎰闁哄秶鏁哥粈鍕煛鐏炴儳濮冮梺鍙夌矒閺屽懏寰勯幇鈹惧亾瀹ュ绀嗛柛銉ｅ妼鎼村﹪鏌涘顒€鍤辩紒杈ㄧ箖缁楃喎鈹戞繝鍕垫船闁诲海鎳撻崯鏉库枔閹达附鐒诲鑸靛姂閳ь剙绉瑰Λ渚€鍩€椤掑倹鍟哄ù锝堫潐閳绘棃鏌?/ 闂傚倸鎳忛崝妯何涘畝鍕櫖濠㈣泛妫楅弲?闂備焦褰冪粔瀵哥磽閹捐绀嗘俊銈呭閳ь剙鍟撮弫?
        if (current instanceof GuiConfigsBase && ConfigEntryFilter.hasVisibilityChanged()) {
            refreshCurrentConfigScreen();
        }
    }

    /**
     * 闂備焦褰冪粔瀵哥磽閹惧箍浜归柟鎯у暱椤ゅ懘姊洪弶璺ㄐら柣銈呮閹儳鐣濋崟顒併€冮梺鎸庣☉閻楀棗鈻庨姀鈩冧氦闁绘柨鍢查悡鍌氣槈閹炬剚鍎庣紒?tick闂佹寧绋戦惌鍌涘閳哄懎绀傜€广儱鎳忛煬顒勬煟閹邦喗鍤€闁?/ 濠电偞鎸稿鍫曟偂鐎ｎ€㈣鎷呯悰鈩冪厾婵炴垶鎼╅崣鍐闯缁嬫鍤堥柧蹇氼潐濞呮洟姊婚崼顐ｇ窙缂佽鲸宀搁弫?
     *
     * <p>闂備焦褰冪粔瀵哥磽閹捐绀堢€广儱鐗滈崬鍓佹喐閻楀牊绀€闁割煈浜為幃浼村Ω瑜庨悾杈ㄧ節婵犲啳澹樺┑顔规櫆閹峰懎顓奸崶鈺傜€梺鎸庣☉閻倿宕崇粙娆惧殘闁告繂瀚崐鐢告煙椤撴粌鐏╂い锔界叀閺佸秴鐣濋崟顏嗙礆闂佺绻愮粔鍨叏瀹€鍕櫢?/ 闂佸搫瀚晶浠嬪Φ濮樿埖鈷曢柟閭﹀灡椤ユ垿鏌涘顒勵€楅柛顭戜簽閹即濡搁妸褍鍔滈梺鎼炲劤閸嬨倝濡存繝鍥ㄧ劸妞ゎ厽甯楃€?/p>
     */
    public static void refreshCurrentConfigScreen() {
        Screen current = GuiUtils.getCurrentScreen();

        if (!(current instanceof GuiBase screen)) {
            return;
        }

        Minecraft.getInstance().execute(() -> {
            int scrolled = getScrollValue(screen);
            screen.initGui();
            setScrollValue(screen, scrolled);
        });
    }

    private static int getScrollValue(GuiBase screen) {
        if (screen instanceof GuiListBase<?, ?, ?> list) {
            WidgetListBase<?, ?> widget = ((GuiListBaseAccessor) list).malilibui$getListWidget();

            if (widget != null) {
                return ((WidgetListBaseAccessor) widget).malilibui$getScrollBar().getValue();
            }
        }

        return 0;
    }

    private static void setScrollValue(GuiBase screen, int value) {
        if (screen instanceof GuiListBase<?, ?, ?> list) {
            WidgetListBase<?, ?> widget = ((GuiListBaseAccessor) list).malilibui$getListWidget();

            if (widget != null) {
                GuiScrollBar bar = ((WidgetListBaseAccessor) widget).malilibui$getScrollBar();
                bar.setValue(Math.max(0, Math.min(value, bar.getMaxValue())));
            }
        }
    }

    /**
     * 闂侀潧妫楅懟顖炲箚鎼淬劍鍋ㄩ柕濞у嫭鐝梻鍌氱墑閸ㄤ粙宕抽幖浣哥闂婎剚绁撮崑鎾愁吋婢跺﹤璧嬬紓鍌氬枤閸ｏ綁濡村澶嬪剭闁告洦鍋€閸嬫捇鎮℃惔锛勭М闂佸憡鐗楅悧鏇灻洪弽顐ｅ闁告劘娉曠粈鍕煕閿斿搫濡介悗鍨矒閺岋箓顢欓幆褌鍖栭梺鍛婂灱椤曆囁囬埡鍛仩闁糕剝绋掗埢澶嬬箾閸儲娑ч柟顔芥尭椤垽濡烽妸褎顔嶉梺姹囧焺閻撳妲愬鑸垫櫢?
     *
     * <p>闂佺绻戞繛濠偽涢幘顔肩睄闁告挷鑳惰ぐ宀勬煕濡ゅ嫭鐝ù鐙€鍠楀鍕吋閸涱厽鏋鹃梺缁橆殔閻楀棛绮╂搴濇勃闁逞屽墴閻涱噣宕奸弴鐐搭仧閻庣偣鍊涘▍锝夊疾椤愶附顥堟い顐枤缁€鍕槈閹炬娊顎楅柛銊ョ箻閺?TAB 闂佺儵鏅濋…鍫ュ箖閹剧粯鍎嶉柛鏇ㄥ枤閻斿懐鈧灚婢樼€氬摜妲愬鑸垫櫢?
     * 闁哄鏅滈悷锕傛偋鏉堛劎鈻旂€广儱顦版禒姗€鎮烽弴姘冲厡闁烩剝鍨剁粙澶屸偓锝傛櫇椤忛亶鏌?闂佸憡甯掑ú顓㈠Υ婢舵劖鏅繛鎴欏灩濞呫垽鏌￠崒娑欑凡缂佽鲸鍨垮畷銉╊敍濞戞瑯妲繛鎴炴尭椤戝嫮绮╅幘顔芥櫢?ensure 婵炴潙鍚嬫穱娲吹濠婂牆绀夐柕濞炬櫅濞呫垽鏌￠崒娑欑凡缂併劍鐓￠幃浠嬪Ω閵壯屼紝闁诲繒鍋愰崑鎾绘煥?/p>
     */
    public static void onRestyleEnabledChanged(boolean enabled) {
        Screen current = GuiUtils.getCurrentScreen();

        if (enabled || !(current instanceof GuiBase screen) || !STATES.containsKey(screen)) {
            return;
        }

        restoreVanillaLayout(screen);
    }

    /**
     * 闁哄鏅滈弻銊ッ洪弽顓炶摕闁归偊浜崵瀣煟閿濆懐鐒告繛鍏煎閵囨劙骞橀崘宸瀫闂佹眹鍨婚崰鎰板疾閵夆晜鍎嶉柛鏇ㄥ亞椤忔挳鎮橀悙鈺佷壕闂佹寧绋掔粙鎺戯耿椤撱垹绠抽柕澹苯鎮?/ 閻庣懓鎲¤ぐ鍐矗瑜斿濠氼敇閵忕姵顏熼梺鍛婂笩閸╂牕顪冮崒娑欎氦闁哄倹瀵х粈鈧?null闂?
     *
     * <p>GuiListBase 闂?mixin 闂佹椿娼块崝宀勬偩閻樿绾ч柕澶嗘櫅濞呫垽鎮楃憴鍕闁诡喗绮撳畷姘旈崟鈹惧亾閸愵喖绠崇憸灞解枎閵忋倖鍎嶉柛鏇ㄥ亜閻忥紕鈧偣鍊曢幖顐㈩焽濡ゅ懎鍐€闁搞儵顥撶粈?
     * 闁哄鏅滈悷锕傛偋闁秴绠ｉ柟瀛樼矋缁箑鈽夐幘宕囆ф繛鎾冲閹茬増鎷呴柨瀣伎闂佽　鍋撻柣鐔告緲閺傃囨煟濡も偓閻楀繐鈻撻幋锕€绀嗘俊銈呭閳ь剙鍟村畷鎼佸箛椤撶姴鐏遍柣搴㈢⊕椤ㄥ棝顢欏畝鍕櫢?/p>
     */
    public static ConfigLayout layoutFor(GuiBase screen) {
        ConfigScreenState state = STATES.get(screen);

        if (state == null || !state.ready || state.layout == null) {
            return null;
        }

        return state.layout;
    }

    /**
     * 闂佸搫瀚烽崹浼村箚娴ｈ櫣鐭夊ù锝囧劋閺嗗繐霉濠婂啯顥㈤柛锝嗘倐瀹曟氨鎹勯搹璇′紝闁诲繒鍋愰崑鎾绘煥濞戞澧﹂柛妯稿€楃槐鏃堫敊閻撳骸缍橀梻鍌氬亞閸樹粙宕归妸褏鐜绘俊銈傚亾鐟滅増鐩鍛婃媴閸濄儲鐦撻梺鎸庣☉椤р偓闁?
     */
    private static boolean isManageDisabled(GuiBase screen) {
        if (!UiConfigs.General.ENABLED.getBooleanValue()) {
            return true;
        }

        return screen.getScreenWidth() < MIN_SCREEN_W || screen.getScreenHeight() < MIN_SCREEN_H;
    }

    private static ConfigScreenState stateFor(GuiBase screen) {
        ConfigScreenState state = STATES.get(screen);

        if (state == null) {
            state = new ConfigScreenState(screen);
            STATES.put(screen, state);
        }

        return state;
    }

    private static boolean isTooSmallToManage(GuiBase screen) {
        if (screen.getScreenWidth() < MIN_SCREEN_W || screen.getScreenHeight() < MIN_SCREEN_H) {
            // 闁诲繐绻愰幖顐︻敋椤撶喐浜ら柛銉戝懏鐦撻梺鎸庣⊕绾板秵绂掗钘夌窞鐎广儱鎳庨弬褔鏌ｅΔ鈧悧鍡欑博妞嬪簼娌柍褜鍓氱粙澶嬫償閳ヨ櫕鏋炬繝娈垮枛椤戝懘宕归鍡樺仒闁靛鍊楃粔瀵哥磽閸愭儳娅欓柟?
            restoreVanillaLayout(screen);
            return true;
        }

        return false;
    }

    private static boolean isDisabledByConfig(GuiBase screen) {
        if (UiConfigs.General.ENABLED.getBooleanValue()) {
            return false;
        }

        // 闂佹椿娼块崝宥夊春濞戞氨鐭夊ù锝囧劋閺嗗繘姊洪幓鎺斝㈤柛鈺傤殜瀵噣鎳滈崗绋垮灊婵犮垼娉涚粔瀵告暜椤愶附鍋嬮柛顐ｇ箘椤忔挳鎮橀悙鈺佷壕闂佹寧绋戦悧鍡欑不閹惧墎椹冲鑸电〒缁愭鎱ㄩ敐鍛闁硅埖宀稿浠嬫晸閻橀潧鐭楅梺鐑╂櫆濞兼瑩宕哄畝鈧埀顒傛嚀閼活垶宕ｈ箛娑樼畳闁靛繒濯Σ濠氭煥濞戞﹩妾ч柟?
        restoreVanillaLayout(screen);
        return true;
    }

    /**
     * 闂佽鍘归崹褰捤囬弻銉ュ偍闁绘柨鎼。濂告偨椤栨艾鏆欓柣顏嶅墴閺佸秹宕煎┑鍫㈡殼缂備焦绋戦¨鈧紒杈ㄥ哺閺佸秴顫濆畷鍥╊啂闂佸憡顭囬崰鏇綖閿曞倸绠ｉ柟瀛樼矋缁箑菐閸ヨ泛鏋熼柡浣稿悑濞煎宕堕埡鍌滄殸闂佸憡甯楅〃澶愬Υ閸愵亖鍋撻悷鑸粧閻庨潧鐭傚畷鎼佸箛椤撶姴鐏遍梺?
     * 濡ょ姷鍋犲▔娑橈耿椤忓牊顥嗛柍褜鍓涢幉鐗堟媴鐟欏嫷妲梻浣瑰絻缁夊绱撻幘瀵糕枖闁逞屽墮閳诲酣鏁嶉崟顒佺彧闂傚倸鐗滃浣烘濠靛牊濯奸柍杞拌兌閹藉秶绱掗銈囧笡閻庡灚绮撳畷銏ゆ偄閸濆嫷鏆繛杈剧到缁夊爼鎮块崱娑欑厒鐎广儱鎳愮槐?TAB 闂佸湱顭堥ˇ鐢稿箰閾忣偆鈻旈幖绮光偓宕囦粴闁荤偞绋忛崝宥囨暜閼哥數顩烽悹浣筋潐鐎?
     *
     * <p>婵烇絽娲︾换鍕汲閳ь剟鏌熼幁鎺戝姢妞ゆ挻鎮傞幃鈺呮嚋绾版ê浜惧ù锝埿掗崑鎾朵沪閼测晝鎲归梺鍝勫閸ㄥ啿煤閸ф绠抽柕澶堝€栨稉銏⑩偓娈垮枛閸犳稓妲愬┑鍥╊浄闁靛鍊楅埞鎺楁⒑閹绘帞校闁哄苯锕畷銉╊敍濠婂嫭娈㈤梻浣瑰絻缁夋挳宕哄Δ鍛睄闁诡垱婢橀崢瀵哥磼閺傛鍎忛柛鈹垮灲閺屽苯顓奸崱妯煎帎闁圭厧鐡ㄥ濠氬极閵堝洦鏆滈柛鎰╁妿濠€浼存煥?/p>
     */
    private static void restoreVanillaLayout(GuiBase screen) {
        ConfigScreenState state = STATES.get(screen);

        if (state == null) {
            return;
        }

        boolean needsRebuild = state.ready || state.layout != null;

        state.ready = false;
        // 濠电偞鎸搁幊鎰板煘閺嶎偅鏆滈柛鎰╁妿濠€浼存煥濞戞瑧顣插ù鐙€鍠楀鍕吋閸涱厽閿梻鍌氭礌閸嬫捇鏌熺粭娑樻－閺€钘夆槈閹绢垰浜惧┑鐐叉濡插嫮妲愬┑瀣Е闁挎洍鍋撻柛顭戝灠琚欓煫鍥ㄦ尰婵酣姊洪钘夌亰缂佸彉鍗抽弻灞筋吋閸滀焦些闂備焦褰冪粔瀵哥磽閹剧粯鍋╃€光偓閸曨剚銆冮梺鎸庣☉閻楀﹤螞閳哄嫮鐤€婵°倐鍋撻柛鈺傜〒娴狅箒绠涚€Ｑ冧壕濞达絿鍎ゅ▍鏇㈡⒒閸稑鐏╃€殿喛濮ゅ鍕吋婢跺﹥顏熺紓鍌氬枤閸ｎ垳妲愬鑸垫櫢?
        state.layout = null;

        if (needsRebuild) {
            MalilibUiMod.LOGGER.info("[{}] Restoring vanilla layout for {}",
                    MalilibUiMod.MOD_NAME, screen.getClass().getSimpleName());

            // GuiListBase 闂?initGui() 闂佸憡鐟禍婊冿耿椤忓牆绀嗘俊銈呭閳ь剙鍟撮獮鎺曨槺婵炲棎鍨介弫?null 闂佸搫鍟抽崺鏍ㄦ櫠閻樼粯鐓傜€广儱鎳愮槐锕傛倵閻熸澘鏋︾紒?
            // 闂佸湱顣介崑鎾趁归悩顔煎姎闁告ɑ绋戦妴鎺楀川椤栨鏅為梺鍛婂笚椤ㄥ濡撮崘顔肩鐟滃苯鈻庨姀銈嗘櫖鐎光偓閳ь剟鍨惧Ο鑽も攳婵犻潧娲ら惁濠氭煕濡厧鏋斿褎顨婂畷鎼佸箛椤撶姴鐏遍梻浣瑰絻缁夌敻寮绘繝鍥х婵炴垶顭囩槐锕傛煥?
            if (screen instanceof GuiListBase<?, ?, ?>) {
                ((GuiListBaseAccessor) screen).malilibui$setListWidgetField(null);
            }

            // 闂佺儵鏅涢悺銊ф暜閹绢喗鐓傜€广儱鎳愮槐锕傛煟閿濆懐鐒告繛闂村嵆楠炴帟顦辨繛鍡愬灲閺佸秹宕煎鍛喒闁荤姍鍕棎闁逞屽墯缁矂宕ｈ閺?闂?闂備焦褰冪粔瀵告閹达箑鐏虫繝闈涙川閵堬妇绱掔€ｎ亶鍎滅紒杈ㄧ箞閺屽棝宕归鐓庤祴婵崿鍛ｉ柣鏍电秮瀹曟寰勭€ｎ剙鐏遍柣鐐村嚬閸嬪﹪宕抽幘顔肩哗闁诡垎鍡楃彲闁荤姴鎼崯鍧楀春瀹€鈧禒锕佺疀閹鹃鍟傛繛鎴炴惄閸欏啫螞閸ф鏅鑸电⊕鐎?
            screen.initGui();
        }
    }

    private static void ensureConfigsLayout(GuiConfigsBase configs) {
        if (isDisabledByConfig(configs) || isTooSmallToManage(configs)) {
            return;
        }

        ConfigScreenState state = stateFor(configs);
        applyNavLayout(configs, state);

        // 2.0闂佹寧绋掗悺鏈匓 閻庣懓鎲¤ぐ鍐耿椤忓嫀鐔兼晬閸曨厾鐓侀梺鍝勭У閸ㄦ娊鎳欓幋鐘电＜婵☆垰鎼悡鎴︽煥濞戞瀚伴悽顖ｅ亰閹?TAB 闂佸湱顭堥ˇ鐢稿箰閸愭畫鎺曠疀閹炬潙顫撻梻渚囧枟閸ㄦ繄鎹㈠璺虹濞撴埃鍋撴繛鍫秮閹稿﹨绠涢妷褏顦梻渚囧墮閻忔繈宕㈤妶鍡欌枖閹兼番鍨婚崺鎰版⒑閹绘帞孝鐟滅増妫冩俊?
        if (!is26x()) {
            for (ButtonBase nav : state.navButtons) {
                nav.setPosition(-1000, -1000);
            }
        }

        ensureModTree(configs, state);
        ensureList(configs, state);
        ensureModSwitcher(configs, state);
        positionExtraDropdowns(configs, state);
        state.ready = true;
    }

    private static final java.util.Set<String> EXPANDED_MODS = new java.util.LinkedHashSet<>();

    /**
     * 2.0 閻庡綊娼婚梽鍕偉椤斿槈鐔兼晬閸曨厾鐓侀梺鍝勭У閸╁﹦妲愭导鏉戠婵°倐鍋撻柛銈庡弮楠炲秹鍩€椤掑嫬瀚夊璺猴工閸ゆ帗绻涙径鍫濆闁哥偠妫勮灒闁挎稑瀚惌瀣煥濞戞瀚扮紓宥咁儔瀹曟粌顓奸崰锝勮兌缁辨帡宕熼锝囥偧闁诲氦顫夐懝鎯э耿椤忓懐鈻旈悗锝庡亝閻撴瑩鏌￠崟顐⑩挃闁?TAB闂?
     * 闂佺绻戝﹢鍦垝椤掆偓铻ｉ柨娑樺閻鏌涘▎妯虹仩闁活偄绉撮锝夊焵椤掑嫬鍙婇柛鎾椾椒绮甸悗瑙勭摃鐏忔瑧妲愰敂閿亾濞戞瑯鐒芥繛?TAB 闂佸憡鑹剧粔鎯扳叿闂?
     */
    private static void ensureModTree(GuiBase screen, ConfigScreenState state) {
        if (is26x()) {
            return;
        }

        ConfigLayout layout = state.layout;
        List<ButtonBase> buttons = ((GuiBaseAccessor) screen).malilibui$getButtons();

        // 婵絽绻愰幎姘跺箰婢跺浠橀柡鍌涙緲缁旈浠﹂埀顒勬煂瀹ュ懐绱︾€归潻缂氶弲鍫曞冀閹搭垳绀夐梺顒€鐏濋崢銈夊籍瑜忓▓鎴﹀箰婢舵劖灏﹂柛褎鍔栭悥?閻忕偞娲栫槐鎴︽偐閼哥鍋撴担鍦殭闁伙絾鐟ｉ埀?
        buttons.removeAll(state.treeButtons);
        state.treeButtons.clear();
        state.treeRows.clear();
        String currentModId = getModId(screen);

        if (!state.autoExpanded) {
            EXPANDED_MODS.add(currentModId);
            state.autoExpanded = true;
        }

        int y = layout.navY - state.treeScroll;
        int bottom = layout.sidebarY + layout.sidebarH - 10;

        if (!state.treeScrollAdjusted && y > bottom) {
            state.treeScroll += y - bottom;
            state.treeScrollAdjusted = true;
        }
        final int modRowH = 28;
        final int tabRowH = 16;

        for (ModTree.ModEntry entry : ModTree.snapshot()) {
            if (y + modRowH > bottom) {
                break;
            }

            String modId = entry.info().modId();
            boolean current = modId.equals(currentModId);
            boolean expanded = EXPANDED_MODS.contains(modId);
            String name = UiTheme.trim(screen.getFont(), entry.info().modName(), layout.navW - 20);
            String version = FabricLoader.getInstance().getModContainer(modId)
                    .map(c -> c.getMetadata().getVersion().getFriendlyString()).orElse("");

            ButtonGeneric modButton = new ButtonGeneric(layout.navX, y, layout.navW, modRowH, "");
            modButton.setRenderDefaultBackground(false);
            state.treeRows.put(modButton, new ConfigScreenState.TreeRow(name, version, current, false));
            screen.addButton(modButton, (button, mouseButton) -> {
                if (current) {
                    if (!EXPANDED_MODS.remove(modId)) {
                        EXPANDED_MODS.add(modId);
                    }
                } else {
                    EXPANDED_MODS.add(modId);
                    GuiBase target = entry.info().configScreenSupplier().get();

                    if (target != null) {
                        GuiBase.openGui(target);
                    }
                }

                // 閻庢鍠栭幖顐﹀春濡や胶鈻旈悗锝傛櫇椤忛亶鎮介鍡楁搐濞呫垻鈧偣鍊楅崕銈夋偉閺屻儲鏅悘鐐垫櫕濞兼梻鈧鍠掗崑?闂佽　鍋撻柟顖滃瀹曟娊鏌涘▎妯圭盎濠靛倹鐗犲畷顐㈩吋閸涱剛顔嗘繛鎾寸殰閸愩劎浠氶柣鐐翠緱閻撳妲愬┑鍥┾枖鐎广儱妫楅埅鐢碘偓娈垮枓閸嬫捇鏌ｉ敐鍛劯婵為棿鍗虫俊?
                ((GuiBaseAccessor) screen).malilibui$getButtons().removeAll(state.treeButtons);
                state.treeButtons.clear();
                state.treeRows.clear();
                state.treeBuilt = false;
            });

            state.treeButtons.add(modButton);
            y += modRowH;

            if (expanded) {
                java.util.List<String> tabs = entry.tabs();

                // 閻熸粎澧楅幐鍛婃櫠閻樼粯鍋╃€光偓閸曨剚銆冮梺?TAB 闂佸憡鑹剧粔鎯扳叿婵炲濮伴崕鍐诧耿閿涘嫧鍋撻崷顓炰沪閻庡灚绮撻弻锕傤敊閺勫繒顦伴梺鍛婂灥濡妲愬┑瀣劶闁割煈鍠栫敮宕囩磽閸屾稒灏柣掳鍔戦弻灞界暆閳ь剙鈻撻幋锕€绫?闂佽鎯屾禍婵嬪蓟閸ヮ剙妫橀柛銉ｅ妽閹烽亶鏌?
                if (current && !state.navButtons.isEmpty()) {
                    tabs = new java.util.ArrayList<>();

                    for (ButtonBase nav : state.navButtons) {
                        tabs.add(((ButtonBaseAccessor) nav).malilibui$getDisplayString());
                    }
                }

                for (int tabIndex = 0; tabIndex < tabs.size(); tabIndex++) {
                    String tab = tabs.get(tabIndex);

                    if (y + tabRowH > bottom) {
                        break;
                    }

                    String tabName = UiTheme.trim(screen.getFont(), tab, layout.navW - 42);
                    ButtonGeneric tabButton = new ButtonGeneric(layout.navX + 18, y, layout.navW - 18, tabRowH, "");
                    tabButton.setRenderDefaultBackground(false);
                    state.treeRows.put(tabButton, new ConfigScreenState.TreeRow(tabName, "", false, true));
                    final int targetTab = tabIndex;
                    screen.addButton(tabButton, (button, mouseButton) -> {
                        // 当前模组的 tab：直接调用 malilib 原生 tab 按钮的监听器，保证切换的就是这一页。
                        if (current && targetTab < state.navButtons.size()) {
                            ButtonBase nativeButton = state.navButtons.get(targetTab);
                            var listener = ((ButtonBaseAccessor) nativeButton).malilibui$getActionListener();

                            if (listener != null) {
                                listener.actionPerformedWithButton(nativeButton, 0);
                                return;
                            }
                        }

                        GuiBase target = entry.info().configScreenSupplier().get();

                        if (target != null) {
                            GuiBase.openGui(target);
                        }
                    });

                    state.treeButtons.add(tabButton);
                    y += tabRowH;
                }
            }
        }

        state.treeBuilt = true;
        state.treeContentH = Math.max(0, y + state.treeScroll - layout.navY);

        // 闂佸搫绉甸崹鍧楀闯瀹勯偊鍟呴柤纰卞墰閻ュ懐绱撴担濮戭亪宕哄Δ浣侯洸闁糕剝顨堢粔濂告煕閹惧磭校閼垛晝绱撴担绋款伀婵?TAB闂佹寧绋掔粙鎴犳暜椤愶附鏅?TAB 闂佸湱顭堥ˇ鐢稿箰閹惰姤鈷曢柟閭﹀灡椤ユ垿鏌ㄥ☉妯肩劯濞村皷鏅犲畷妤€顓兼径濠冾仧婵犮垼娉涚粙鍕箯?
        for (ButtonBase nav : state.navButtons) {
            nav.setPosition(-1000, -1000);
        }

        if (System.getProperty("malilib-ui.debugtree") != null) {
            StringBuilder sb = new StringBuilder("[tree] nav=").append(state.navButtons.size())
                    .append(" tree=").append(state.treeButtons.size())
                    .append(" y=").append(y);

            for (ButtonBase nav : state.navButtons) {
                sb.append(" [").append(nav.getX()).append(',').append(nav.getY()).append(']');
            }

            MalilibUiMod.LOGGER.info(sb.toString());
        }
    }

    /**
     * 闂備緡鍠撻崝宀勫垂鎼粹垾鐔兼晬閸曨厾鐓佹繛鏉戝悑閼规儳锕㈤鍫熺厐鐎广儱娲ㄩ弸鍌炴煟閿濆懐鐒告繛闂村嵆閺屽苯鐣濋崟鍨緭婵犮垼鍩栭悧妤呭锤婵犲洤绀夐柣妯哄暱缁楁捇鏌?缂備焦绋掗惄顖炲焵椤掆偓椤︻喚绮崨鏉戠濠㈣泛顑愰弨閬嶆煏閸℃鈧悂鎮鹃悩宕囶浄妞ゆ巻鍋撻悽顖ｅ亰瀵敻顢楁担鍝ユ▎闁诲繒鍋涚换鎰不閻戣棄閿ら柟閭﹀幘閸ㄨ偐鐥褍澧扮紒槌栦邯閹秵绗熸繝鍕槷
     * 闂備焦褰冪粔鎾春濡ゅ啯鏆滈柛鎰╁妿濠€浼存煕濮橆剛鍑圭紒杈ㄥ閹噣顢曢敐鍛瘍闂佸憡甯婇崡鍐差焽娴兼潙绾ч柛顭戝櫘濡棝鏌ㄥ☉娆戭灱缂佺粯鐗犻弻灞界暆閳ь剛鍒掗悜妯尖枖闁逞屽墴楠炴劖寰勬繝鍌涙殽闁诲骸婀遍幊鎾朵焊椤栫偛鐭楅悗鍦Х閻濆墎绱撻崒娑宠€块柛锝嗘倐瀵剟鎮ч崼鐔锋櫩闂佽　鍋撻柟鎯ф噺鐎?
     * 26.x 婵烇絽娲︾换鍐偓鍨瀹曘垽鎮㈠ú缁樻杸婵炴垶鎹佸銊х箔婢舵劕绀夋い顓熷笚鐎?
     */
    private static void positionExtraDropdowns(GuiConfigsBase configs, ConfigScreenState state) {
        if (is26x()) {
            return;
        }

        ConfigLayout layout = state.layout;
        WidgetDropDownList<ModInfo> switcher = ((GuiConfigsBaseAccessor) configs).malilibui$getModSwitchWidget();
        List<WidgetDropDownList<?>> extras = new ArrayList<>();

        for (WidgetBase widget : ((GuiBaseAccessor) configs).malilibui$getWidgets()) {
            if (widget instanceof WidgetDropDownList<?> dropdown && dropdown != switcher) {
                extras.add(dropdown);
            }
        }

        if (extras.isEmpty()) {
            return;
        }

        int right = switcher != null ? switcher.getX() + switcher.getWidth()
                : layout.contentX + layout.contentW - 8;
        int y = switcher != null ? switcher.getY()
                : layout.contentY + (layout.headerH - 18) / 2;

        for (int i = extras.size() - 1; i >= 0; --i) {
            WidgetDropDownList<?> dropdown = extras.get(i);

            String dropdownClass = dropdown.getClass().getName();
            if (dropdownClass.contains("magiclib") || dropdownClass.contains("SelectorDropDownList")) {
                // MasaGadget 闂?MagicLib 闂?SelectorDropDownList 闂佸搫娲ら妵姗€宕鍕偍闁绘柨鎼。濂告煕閹烘垶澶勭€规洘鐓″畷鎶解€﹂幒鎾愁伓?
                // 闁诲海顢婂Λ鍕偓纭呮珪缁屽崬鈹戦崶褏妲锋繛鎴炴尭妤犲摜鏁锔藉亱闁割偅绻傞悗濂告煙楠炲灝鐏╂繛鍛笚缁嬪鍩€椤掑嫭鍤婇弶鍫濆⒔缁€澶愭煕濞嗘ü绨绘い銈呭暣瀹曟繈鎮╂潏鈺婁紘婵＄偑鍊濆褑銇愰弻銉ュ強闁告挆浣风驳闂佹眹鍔岀€氼厼顭囬鐐村剮妞ゆ棁濮ゅ▓鍫曟煥?
                int width = switcher != null ? switcher.getWidth() : 130;
                dropdown.setWidth(width);
                dropdown.setHeight(switcher != null ? switcher.getHeight() : 18);
                dropdown.setPosition(right - width, y);
                expandMasaGadgetVisibleEntries();
                continue;
            }

            int width = dropdown.getWidth();
            right -= width;
            dropdown.setPosition(Math.max(layout.contentX + 8, right), y);
            right -= 4;
        }
    }

    private static boolean masaGadgetEntriesExpanded;

    private static void expandMasaGadgetVisibleEntries() {
        if (masaGadgetEntriesExpanded) {
            return;
        }

        try {
            Class<?> configs = Class.forName("com.plusls.MasaGadget.game.Configs");
            Object option = configs.getField("fastSwitchMasaGadgetConfigGuiVisibleEntries").get(null);
            Object value = option.getClass().getMethod("getIntegerValue").invoke(option);

            if (value instanceof Integer current && current < 12) {
                try {
                    option.getClass().getMethod("setIntegerValue", int.class).invoke(option, 12);
                } catch (NoSuchMethodException e) {
                    option.getClass().getMethod("setValue", Object.class).invoke(option, 12);
                }
            }

            masaGadgetEntriesExpanded = true;
        } catch (Throwable ignored) {
            // MasaGadget/MagicLib 婵炴垶鎸哥粔鎾偤閵娾晛鎹堕柕濠忕畱閻?API 闂佸憡鐟﹂敋閻庡灚鐓″顕€寮甸悽鐢殿啍闂佸綊鏅茬粈浣烘暜椤愶箑鍐€闁告垯鍊栫€?
        }
    }


    private static void ensureRenderLayerLayout(GuiBase screen) {
        if (isDisabledByConfig(screen) || isTooSmallToManage(screen)) {
            return;
        }

        ConfigScreenState state = stateFor(screen);
        applyNavLayout(screen, state);
        positionStandaloneControls(screen, state);
        state.ready = true;
    }

    private static void applyNavLayout(GuiBase screen, ConfigScreenState state) {
        List<ButtonBase> buttons = ((GuiBaseAccessor) screen).malilibui$getButtons();

        if (!state.navButtons.isEmpty()) {
            // 闂佷紮绲介惌鍌氼焽閺夋鍟呴柡灞诲劚濞呫垻鈧偣鍊楃亸銊ф濞嗘挸绀嗛柛銉ｅ妼鎼?TAB闂佹寧绋戦¨鈧紒杈ㄧ箞瀹曘垽鎮㈢粙璺ㄦ▎闂備胶鐡旈崰鏍偪閸曨剛鐟归悗锝庝簻瀵潡姊洪鍝勫闁靛洦鍨垮顐﹀醇椤掍礁顏?
            for (ButtonBase button : state.navButtons) {
                if (!buttons.contains(button)) {
                    state.navButtons.clear();
                    break;
                }
            }
        }

        if (state.navButtons.isEmpty()) {
            for (ButtonBase button : buttons) {
                if (isTabButton(button)) {
                    state.navButtons.add(button);
                }
            }
        }

        ConfigLayout layout = ConfigLayout.compute(screen.getScreenWidth(), screen.getScreenHeight(),
                state.navButtons.size());
        state.layout = layout;

        // 2.0 濠碘槅鍨界槐鏇犲垝瀹ュ鍐€闁硅揪闄勬禒姗€鎮烽弴姘冲厡闁绘搫绻濋弻鍡涘箻鐎靛憡袞婵炴垶鎼╂禍婵婂暞缂傚倷绀佺€氼厼锕㈡笟鈧畷顐︻敂閸愵亝妲€ TAB闂佹寧绋掔粙鏍崲閺嶎厽鐓傜€光偓閳ь剛妲愰敂閿亾濞戞顏嗙礊鐎ｎ喖绀堢€广儱娲﹀▍鏇㈡⒒閸稑鐏╅柛鎴磿缁辨帟绠涢弮鍌楀亾椤栨凹鍤堥柛婵嗗濮ｅ鏌ｉ妸銉ヮ仼闁搞劌娴风槐鎺楀礋椤愶絽鈧磭绱掓径濠傜盎闁?
        List<String> tabLabels = new ArrayList<>();

        for (ButtonBase button : state.navButtons) {
            tabLabels.add(((ButtonBaseAccessor) button).malilibui$getDisplayString());
        }

        ModTree.cacheTabs(getModId(screen), tabLabels);

        // 闂?TAB 闂佸湱顭堥ˇ鐢稿箰瀹曞洨鐭撻悹鍥ㄥ絻琚熼梺鍛婂笚濠㈡ɑ绋婅箛鏃傜懝鐟滃秹顢氶柆宥嗗殝妞ゅ繐鎳愰崺澶愭煥?
        for (int i = 0; i < state.navButtons.size(); i++) {
            ButtonBase button = state.navButtons.get(i);
            int x = layout.navX;
            int y = layout.navItemY(i);

            if (button.getX() != x || button.getY() != y ||
                    button.getWidth() != layout.navW || button.getHeight() != layout.navItemH) {
                button.setPosition(x, y);
                button.setWidth(layout.navW);
                button.setHeight(layout.navItemH);
            }
        }
    }

    private static void ensureModSwitcher(GuiConfigsBase configs, ConfigScreenState state) {
        WidgetDropDownList<ModInfo> switcher = ((GuiConfigsBaseAccessor) configs).malilibui$getModSwitchWidget();

        if (switcher == null) {
            return;
        }

        ConfigLayout layout = state.layout;
        // 闂侀潻璐熼崝宀勫船鐎电硶鍋撻崷顓熷殌閻忓浚鍨跺浠嬪炊椤掑姣岄柣鐐寸☉閼活垶宕幘顔煎瀭闁稿本绮嶇痪顖炴倶閻愬弶鍣搁柤鍨灴閺佸秶浠﹂崐鐔风彲婵炴垶鎸哥€涒晠骞冨畷鍥潟闁靛繆妲呴弨閬嶆煕濞嗗繒鏋冪紒鏃傚枔缁辨挸螣娓氼垰娈告慨鎺旂帛閸斞呮鏉堚晝鐜绘俊銈傚亾鐟滅増绋掑蹇涘礃椤忓棗纾块梺鍝勫暞濠€鍦箔婢舵劕绀傚ù锝囩摂閸熷懘鎮洪幒鎴伐闁搞値鍙冨畷姗€宕ㄩ褍鏅ｉ梺鍛婄墱鐏忋劑骞?
        int x = Math.max(layout.contentX + 8, layout.contentX + layout.contentW - 152);
        int y = layout.contentY + (layout.headerH - 18) / 2;

        if (switcher.getX() != x || switcher.getY() != y) {
            switcher.setPosition(x, y);
        }
    }

    private static boolean hasModSwitcher(ConfigScreenState state) {
        return state.screen instanceof GuiConfigsBase configs &&
                ((GuiConfigsBaseAccessor) configs).malilibui$getModSwitchWidget() != null;
    }

    static boolean is26x() {
        try {
            Class.forName("net.minecraft.client.gui.GuiGraphicsExtractor");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static boolean isTabButton(ButtonBase button) {
        int y = button.getY();

        return y >= 0 && y <= TAB_STRIP_MAX_Y &&
                button.getHeight() > 0 && button.getHeight() <= TAB_MAX_HEIGHT &&
                button.getWidth() > 0;
    }

    private static void ensureList(GuiConfigsBase configs, ConfigScreenState state) {
        ConfigLayout layout = state.layout;
        GuiListBaseAccessor listAccess = (GuiListBaseAccessor) configs;
        WidgetListBase<?, ?> widget = listAccess.malilibui$getListWidget();

        if (widget == null) {
            return;
        }

        WidgetListBaseAccessor widgetAccess = (WidgetListBaseAccessor) widget;
        boolean recreated = false;
        boolean positionOk = widgetAccess.malilibui$getListPosX() == layout.listX &&
                widgetAccess.malilibui$getListPosY() == layout.listY;

        if (!positionOk) {
            // 闂備焦褰冪粔鐢稿蓟婵犲洤绀嗘繛鎴烆焽缁憋箓鏌涢幒鎿冩畽闁靛棗鍟撮獮鎺曨槺婵炲棎鍨介弫宥呪枎缁斺暐ListBase 闂?mixin 婵炴潙鍚嬬喊宥嗕繆閹间礁绀嗘繛鎴烆焽缁憋箓鏌涜閸旀牠鎮ラ敐澶婂嵆闁圭楠告惔濠囨煙鐎涙ê濮堥柡灞斤功閺侇噣宕橀妸褎鎷遍梺姹囧妼鐎氼亞绱為崨顖滅＞妞ゆ洖妫涚粈?
            // 闂佸憡甯楅〃澶愬Υ閸愵喖绠崇憸灞解枎閵忋倖鍎嶉柛鏇ㄥ墰閳ь剦鍨堕弻鍛存偐閹绘帗鐦ｉ梺杞扮劍濠㈡﹢骞冮幘璇茬睄闁哄牏鏁哥粣妤呮煕濮橆厼鐏ラ柣顐㈡閻涱噣寮撮悢鍝勫绩闁荤姴娲㈤崹铏圭矈閼姐倖鍠嗛柛顐ｇ箖閻濄倝鎮楅柅娑氱煓闁绘繍鍣ｉ弫宥夊醇濠靛洤鈧亶鏌″鍥ㄧ効妞ゎ偓绠撻弻灞筋吋閸涱垱姣庨梺鍛婄閸ㄧ敻宕哄畝鍕闁哄娉曢鎾倶閻愨晛浜鹃柣蹇撶箰閹碱偊顢氶銏℃櫖濠㈣埖绋撶粈?
            // 闂佹悶鍎虫慨鐢割敆濠靛绠规繝濠傛噹閸嬪秵淇婂Δ鈧Λ鏃傜箔瀹€鍕骇闁挎稑瀚粣妤呮煕閺嵮勬儓闁绘挻鐟╅弻鍫ュ箣閹烘梻鐛ユ繛瀵稿О閸庢煡顢楀鍛厹妞ゆ梻鍋撻悾閬嶆倶韫囨挻鎯堟い鏇ㄥ弮瀹曟艾鈽夊Ο鑲╁闂?
            listAccess.malilibui$reCreateListWidget();

            widget = listAccess.malilibui$getListWidget();

            if (widget == null) {
                return;
            }

            widgetAccess = (WidgetListBaseAccessor) widget;
            recreated = true;
        }

        boolean needsRebuild = recreated;

        // 闂佸憡甯楅〃澶愬Υ閸愵喖绠崇憸灞解枎閵忋倕鐭楁い鏍ㄧ箓閸樻挳鏌￠崟闈涚仸闁革絾鎮傚畷妯衡枎鎼达絽鏅?getBrowserHeight() 闂佹眹鍔岀€氼剟鎮哄▎鎴犲暗濞村吋鐟х粈鍕€掑顒夊剰妞?GuiModConfigs闂佹寧绋戦¨鈧紒?
        // 闂佸吋鐪归崕鏌ユ倵椤栫偞鐒婚柣妯诲絻濮ｆ劙鏌℃担鍝勵暭婵炴挸澧庣槐鎺楀醇閻旂儤鈷曢梺鍝勭墕椤︾敻宕虫导瀛樺仺闁靛绠戦悘澶娒归悪鈧崜娑樷枔閹寸姳鐒婇柛婵嗗閸ょ喖鏌ㄥ☉妯垮闁割煈鍨堕弻灞筋吋閸℃鍘愰柟鐓庣摠濮婂寮妶鍥ㄦ殰闁告劑鍔庡﹢浼存倶韫囨挻鎯堟い鏇ㄥ弮閺?
        if (widgetAccess.malilibui$getBrowserWidth() != layout.listW ||
                widgetAccess.malilibui$getBrowserHeight() != layout.listH) {
            widget.setSize(layout.listW, layout.listH);
            needsRebuild = true;
        }

        // 闂備焦婢樼粔鍫曟偪閸℃瑦鍋橀悘鐐村劤娴犳ê霉閻樼儤纭炬い鏃€姊归幆鏃堟晝娴ｅ憡顔囬梻渚囧亜閸婂摜鑺遍弻銉︽櫖婵浜畷鏇犵磼閹邦収娈旂憸鐗堢⊕缁嬪鈧綆鍋呴弳顏嗙磽閸屾娅嗛悽顖濆煐缁傛帞鎹勯崫鍕粴闂佹寧绋戦惌鍌涘閳哄懎绀傜€广儱妫欏▓濠氭偠濞戞ɑ婀伴悗娑掓櫊瀹曟瑩宕崟顐ｆ殽闁诲骸婀遍幊鎾朵焊椤栫偞鏅?
        if (widget instanceof WidgetListConfigOptionsBase<?, ?> configList) {
            WidgetListConfigOptionsBaseAccessor configAccess = (WidgetListConfigOptionsBaseAccessor) configList;
            int requested = ((GuiConfigsBaseAccessor) configs).malilibui$getConfigWidth();
            int maxAllowed = Math.max(80, layout.listW - 170);
            int desired = Math.min(requested, maxAllowed);

            if (configAccess.malilibui$getConfigWidth() != desired) {
                configAccess.malilibui$setConfigWidth(desired);
                needsRebuild = true;
            }
        }

        if (needsRebuild) {
            widget.initGui();
        }

        state.listWidget = widget;
    }

    /**
     * 闂佺娉涢敃銉ф啺閸℃稑钃熼柟鎯х－濠€瀵哥磽閸屾稓澧崇紒棰濆亰瀹曟娊濡搁妶鍫濆箑闂?TAB 闂佹眹鍔岀€氼厾鏁懜鐢殿浄閻犵儤妞介幐顒傜磼婢跺寒鍤欓柛鈺佺焸瀹曟﹢宕ㄩ褍鏅ｉ梺鍛婄墱鐏忋劑骞?
     *
     * <p>闁哄鏅滈悷銈囪姳濞差亝鍋╃€光偓閸曨剚銆冮梺姹囧妼鐎氼厾鏁懜鐢殿浄閻犻缚娅ｉ崬銊ッ瑰鍐€楅悽顖ｅ亰閹囧醇濠靛懐顔嗘繛鎴炴尭閿曘劑锝炲Δ鍛敜闁归偊鍘鹃崹濂告煥濞戞ɑ绶癅code 10, 60}闂佹寧绋戦ˇ顖炲垂閸楃儐鍤堥柣姘嚟楠炪垹霉閿濆懐肖娴滄盯姊洪锝勪孩缂佽鍟村畷鐑樻姜閹殿喛澹橀柣搴ｆ暩椤㈠﹪鎸呴崟顖滃彆妞ゆ劏鈧櫕鏆呴梺?
     * {@code positionedControls} 闂傚倸妫楀Λ妤呭箖鎼淬垻鈹嶆繝闈涳功濡插牊鎱ㄩ敐鍛闂佸弶绮撻獮鎺曨槺婵炲棎鍨藉畷锝夘敂閸曨厼鐣ㄩ梺娲绘娇閸斿绮╅弶鎴旀瀻闁宠棄鎳愮粈澶愭煕閵壯冃ｆい鎺撶箞楠炴帟顦辨繛鍡愬灪缁嬪顓奸崟顓犵崶闁荤偞鍑归崑濠囧闯閸涘﹤绶炵€广儱鎳橀幐顒傜磼婢跺涓茬紒?
     * 闂佸憡鑹鹃張顒€顪冮崒鐐茬煑闁割偓绲介崢鎾偣閾忕懓鏋涙繛鍫涘灪缁嬪﹦鈧綆浜濋崐鐢告煟閵娿儱顏柣娑栧劦瀹曪綁鏁愰崶鈺冪闂佽　鍋撻柟鎯ф噺鐎?/p>
     */
    private static void positionStandaloneControls(GuiBase screen, ConfigScreenState state) {
        ConfigLayout layout = state.layout;
        int deltaX = layout.listX - RENDER_LAYER_CONTROLS_X;
        int deltaY = layout.listY - RENDER_LAYER_CONTROLS_Y;

        if (deltaX == 0 && deltaY == 0) {
            return;
        }

        GuiBaseAccessor access = (GuiBaseAccessor) screen;

        for (ButtonBase button : access.malilibui$getButtons()) {
            if (state.navButtons.contains(button) || state.treeButtons.contains(button)
                    || !state.positionedControls.add(button)) {
                continue;
            }

            button.setPosition(button.getX() + deltaX, button.getY() + deltaY);
        }

        for (WidgetBase widget : access.malilibui$getWidgets()) {
            if (!state.positionedControls.add(widget)) {
                continue;
            }

            widget.setPosition(widget.getX() + deltaX, widget.getY() + deltaY);
        }

        for (TextFieldWrapper<? extends GuiTextFieldGeneric> wrapper : access.malilibui$getTextFields()) {
            GuiTextFieldGeneric field = wrapper.textField();

            if (field == null) {
                continue;
            }

            int[] origin = STANDALONE_FIELD_ORIGINS.computeIfAbsent(field,
                    f -> new int[] { f.getX(), f.getY() });
            int nx = origin[0] + deltaX;
            int ny = origin[1] + deltaY;
            // GuiTextFieldGeneric 闂佺厧顨庢禍婊呮崲娴ｅ湱鈹嶆繝闈涙閹界姴霉濠婂啯顥欑紒鏃€鎸抽弫?x/y闂佹寧绋戞總鏃傚緤?EditBox/AbstractWidget 闂?
            // 闂佺粯鐗曟晶搴ｆ偖椤愩倐鍋撳☉娆樻畷妞ゆ柨鏈粋鎺戭吋閸涱剛涓嶉梺浼欑稻閻熴儱锕㈤鍫濈睄鐟滃秴顭囧Δ鍛唨闁搞儵顥撶粈澶愭煛閸屾碍澶勬繝鈧悧鍫濐嚤婵﹩鍘奸惁濠氭煟閺嵮冾暢閻炴凹鍋婂畷鎼佸箛椤撶姴鐏辩紓鍌欑劍閿氶柛鈺傤殜婵℃挳宕掑┑鍫㈩啇闂備焦褰冮懟顖炲箖閹炬剚娼伴柕澶樼仢閳哄懎绀夐柕濞у啰鍘紓渚囧亯椤曆囨偤瑜嶉埢鎾跺枈婢跺顏?
            field.setX(nx);
            field.setY(ny);
            field.setPosition(nx, ny);
            ((com.github.bunnyi116.malilibui.mixin.accessor.AbstractWidgetAccessor) field)
                    .malilibui$setWidgetX(nx);
            ((com.github.bunnyi116.malilibui.mixin.accessor.AbstractWidgetAccessor) field)
                    .malilibui$setWidgetY(ny);

            if (Boolean.getBoolean("malilib-ui.debuglayout")) {
                StringBuilder sb = new StringBuilder();
                sb.append("field ").append(field.getClass().getName())
                  .append(" getX=").append(field.getX())
                  .append(" getY=").append(field.getY())
                  .append(" value=").append(field.getValue())
                  .append('\n');
                for (Class<?> c = field.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
                    for (java.lang.reflect.Field f : c.getDeclaredFields()) {
                        if (f.getType() == int.class && (f.getName().equals("x") || f.getName().equals("y"))) {
                            try {
                                f.setAccessible(true);
                                sb.append("  ").append(c.getSimpleName()).append('.').append(f.getName())
                                  .append('=').append(f.getInt(field)).append('\n');
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
                try {
                    java.nio.file.Files.writeString(java.nio.file.Path.of("C:\\Users\\AsiaA\\Desktop\\rli\\field-debug.log"), sb.toString());
                } catch (Exception ignored) {
                }
            }
        }
    }

    /* ------------------------------------------------------------------ *
     *  闂?mixin 婵炶揪缍€濞夋洟寮妶澶嬪剭闁告洦鍓涢崣鈧柣鐘叉储閸ㄦ椽寮婚悢鍏兼櫢?
     * ------------------------------------------------------------------ */

    public static boolean hasLayout(GuiBase screen) {
        ConfigScreenState state = STATES.get(screen);

        return state != null && state.ready && state.layout != null;
    }

    public static boolean isNavButton(GuiBase screen, ButtonBase button) {
        ConfigScreenState state = STATES.get(screen);

        return state != null && state.ready && state.navButtons.contains(button);
    }

    /* ------------------------------------------------------------------ *
     *  濠电偞鎸稿鍫曟偂?
     * ------------------------------------------------------------------ */

    /**
     * 缂傚倷鐒﹂敋闁糕晜顨婂顐﹀级閹寸偛浠撮梺鍝勫€规竟鍡椻枔閹达附鍋╃€光偓閸曨剚銆冮梺鐓庡暱閺堫剙鈻嶅▎鎾存櫖闁割偆鍠愮粋灞矫归悪鍛埌閻㈩垼鍋婇幃褔宕奸敐鍛闂佸搫鎷嬮崹宕囧垝椤栫偛绀嗛悹浣告贡缁€鍡涙煥?
     */
    public static void drawBackground(GuiBase screen, GuiContext ctx, int mouseX, int mouseY) {
        ConfigScreenState state = STATES.get(screen);

        if (state == null || !state.ready || state.layout == null) {
            return;
        }

        ConfigLayout layout = state.layout;

        // 闂佺绻堥崝宀勬儓閸℃稑鍌ㄩ悗锝庡亝椤斿倿姊洪澶婃灓闁稿秶鏁婚弫?
        UiTheme.fill(ctx, 0, 0, layout.screenW, layout.screenH, UiTheme.overlayColor());

        // 闂傚倸鐗勯崹鍝勵熆濮椻偓楠炲酣寮撮悙宸婵炴垶鎸哥花鑲┾偓闈涙湰閹峰懘骞欓崟顒€顏?
        UiTheme.shadow(ctx, layout.panelX, layout.panelY, layout.panelW, layout.panelH);
        UiTheme.gradient(ctx, layout.panelX, layout.panelY, layout.panelW, layout.panelH,
                UiTheme.panelTopColor(), UiTheme.panelBottomColor());
        UiTheme.outline(ctx, layout.panelX, layout.panelY, layout.panelW, layout.panelH,
                UiTheme.PANEL_BORDER);
        UiTheme.hline(ctx, layout.panelX + 1, layout.panelY + 1, layout.panelW - 2,
                UiTheme.PANEL_INNER_GLOW);

        if (layout.hasSidebar) {
            drawSidebar(ctx, state);
        }

        // 闂佸憡鍔曢幊搴ｇ矈閻㈢绀勯悷娆忓珔椤愶附顥堥柕蹇婂墲缁舵煡鏌ㄥ☉妯肩伇闁轰降鍊栫粋宥嗘償閵忕媭妫￠柡澶屽仒缁瑩鍩€椤掆偓椤︾敻濡村澶婄婵°倕瀚ㄩ埀?/ 濠电偞鎸稿鍫曟偂鐎ｎ兛娌柛灞剧☉娴犳ê霉閻樹警鍟囬柟?
        int wellMargin = layout.compact ? 4 : WELL_MARGIN;
        UiTheme.drawContentWell(ctx, layout.listX - wellMargin, layout.listY - wellMargin,
                layout.listW + wellMargin * 2, layout.listH + wellMargin * 2);

        drawContentHeader(ctx, state);
    }

    private static void drawSidebar(GuiContext ctx, ConfigScreenState state) {
        ConfigLayout layout = state.layout;
        String modName = prettyModName(getModId(state.screen));

        UiTheme.gradient(ctx, layout.sidebarX, layout.sidebarY, layout.sidebarW, layout.sidebarH,
                UiTheme.sidebarTopColor(), UiTheme.sidebarBottomColor());
        UiTheme.vline(ctx, layout.sidebarX + layout.sidebarW, layout.sidebarY, layout.sidebarH,
                UiTheme.SIDEBAR_DIVIDER);

        // 閻庡灚澹嗛弲顐︽偟闁垮鈻旈幖娣灮閸ㄧ厧螞閻楀煫顏嗕焊椤栫偞鏅?.21.11 婵炶揪缍€濞夋洟寮妶澶婄煑妞ゆ牗绻傚▓浼存倵鐟欏嫮鐓紒鐘靛枎椤曘儵鎮惧畝鈧ぐ鍧楁煥?6.x 婵烇絽娲︾换鍐偓鍨⒐缁嬪绻濋崘顏勭厬闂佸憡顭囬崰鎰版偋闁秵鏅?
        if (is26x()) {
            UiTheme.fill(ctx, layout.sidebarX + 12, layout.sidebarY + 16, 3, 14, UiTheme.TEXT_ACCENT);
            UiTheme.text(ctx, UiTheme.trim(ctx.fontRenderer(), modName, layout.sidebarW - 40),
                    layout.sidebarX + 21, layout.sidebarY + 19, UiTheme.TEXT_TITLE, true);
            UiTheme.text(ctx, "MaLiLib UI", layout.sidebarX + 21, layout.sidebarY + 33,
                    UiTheme.TEXT_SUB, false);

            UiTheme.hline(ctx, layout.sidebarX + 12, layout.sidebarY + 48, layout.sidebarW - 24,
                    0x22FFFFFF);
            return;
        }

        BadgeRenderer.draw(ctx, layout.sidebarX + 10, layout.sidebarY + 8,
                Math.max(40, layout.sidebarW - 20));

        UiTheme.hline(ctx, layout.sidebarX + 12, layout.sidebarY + 42, layout.sidebarW - 24,
                0x22FFFFFF);

        drawModTree(ctx, state);
    }

    /** 2.0 閻庡綊娼荤紓姘跺疾鐠轰綍鐔兼晬閸曨厾鐓侀梺鍝勭У閸╁﹦妲愰弶璇剧喖鏁嶉崟顓犵厑闁荤偞绋戞總鏃傛偖鏉堚晜鍋橀悘鐐靛亾閻庮噣鎮楀☉娆樻缂佽鲸鐟╁畷銉ヮ吋閸?闂佺粯顨呴悧濠傦耿娴煎瓨鏅鑸电〒缁€濉桝B 闁荤偞绋戦惉鑲╃磽婢跺浜ゆ繛鎴濈－楠炪垽姊洪銏╂Х闁煎灚鍨归幃鏉跨暆閳ь剙鈻嶈椤?+ 閻庡綊娼荤紓姘跺疾閸撲胶鍗氶柡宥冨妽閽傚鏌?*/
    private static void drawModTree(GuiContext ctx, ConfigScreenState state) {
        ConfigLayout layout = state.layout;

        if (state.treeButtons.isEmpty()) {
            return;
        }

        for (ButtonBase button : state.treeButtons) {
            ConfigScreenState.TreeRow row = state.treeRows.get(button);

            if (row == null || button.getY() < 0) {
                continue;
            }

            int x = button.getX();
            int y = button.getY();
            int w = button.getWidth();
            int h = button.getHeight();

            if (row.selected()) {
                UiTheme.fill(ctx, x, y, w, h, 0x33FFFFFF);
                UiTheme.outline(ctx, x, y, w, h, 0xFFFFFFFF);
            }

            if (row.tab()) {
                // 婵炲瓨绮岄惉鍏碱殽閸ャ劊浜滈梺顐ｇ〒閻熸繂霉閿濆牊纭堕柡浣靛€楃划鈺呭冀椤愩垺鐤囬柡澶嗘櫇閸嬬偟鏁幘顔芥櫖婵炴垶纰嶉弳蹇涙煛閸パ呮憼婵炲牜鍋婇幆鍐礋椤愩倓浜㈤梻鍌氬暞濞兼瑦绺介弽顐ｅ仒闁靛ě浣风驳闁诲海鎳撻崯顐︽儓濡崵顩查幖瀛樼箘閻熸劙鏌￠崒婵愭綈閼垛晝绱撴担绋款仴闁?
                UiTheme.fill(ctx, x + 8, y, w - 8, h, 0x1C000000);

                if (row.selected()) {
                    UiTheme.fill(ctx, layout.sidebarX + 1, y + 1, 3, h - 2, UiTheme.TEXT_ACCENT);
                }

                UiTheme.text(ctx, row.name(), x + 18, y + (h - 8) / 2,
                        row.selected() ? UiTheme.TEXT_TITLE : UiTheme.TEXT_SUB, row.selected());
            } else {
                if (row.selected()) {
                    UiTheme.fill(ctx, layoutAccentX(x), y, 3, h, UiTheme.TEXT_ACCENT);
                }

                // 濠电偛澶囬崜婵嗭耿娓氣偓瀹曪綁顢涘鍕闂佹悶鍎抽崑鎾绘偉閿濆绫嶉柡鍫㈡暩閻熸繄绱撴担濮戭亪宕哄Δ鍛闁绘浜粔鎾煕瑜庨〃鎰濠靛妫橀柛銉戝懏鎲婚梺鐑╂櫅閻°劎鏁€涙顩烽幖娣€ら弨钘壩涢敐鍡欏ⅹ缂佽鲸鍨堕幈銊р偓锝冨妷閸?
                UiTheme.text(ctx, row.name(), x + 8, y + 4,
                        row.selected() ? UiTheme.TEXT_TITLE : UiTheme.TEXT_SUB, row.selected());

                if (!row.version().isEmpty()) {
                    UiTheme.text(ctx, row.version().length() > 18 ? row.version().substring(0, 17) + "\u2026" : row.version(),
                            x + 8, y + 15, 0xFF7A7A7A, false);
                }
            }
        }
        // 闂佸憡鐟ラ崢鏍疾鐠轰警鐓ユ慨姗嗗墮琚熼梺鍝勵槴濡插嫮妲愭导鏉戠闁告侗鍙庨崯鍥旈崒娴㈣偐鑺遍鍌涘枂闁糕剝顨呯紞鎾绘煛閸愵厼鐓愬褏濞€瀵即宕滆娴犳盯鏌?
        int viewTop = layout.sidebarY + 46;
        int viewBottom = layout.sidebarY + layout.sidebarH - 8;
        int viewH = viewBottom - viewTop;

        if (state.treeContentH > viewH && viewH > 0) {
            int thumbH = Math.max(24, viewH * viewH / state.treeContentH);
            int maxScroll = Math.max(1, state.treeContentH - viewH);
            int thumbY = viewTop + (viewH - thumbH) * state.treeScroll / maxScroll;
            UiTheme.fill(ctx, layout.sidebarX + layout.sidebarW - 5, thumbY, 3, thumbH, 0x88FFFFFF);
        }
    }

    /** 婵崿鍛ｉ柣鏍电秮瀹曠兘濡搁妷銈囶唵婵炴挻鐨滈崟顓炵厙闂佸憡鐗曢幖顐︽偂濞嗗浚鐓ユ慨姗嗗墮琚熼梺鍝勫暢閸╂牜鍒掗幘顔肩闁靛濡囬崺鎰版煥濞戞鐒烽柍褜鍓欐總鏃傜箔婢舵劕鍙婃い鏍ㄨ壘閻庡ジ鏌?TAB闂?*/
    public static boolean onTreeScroll(GuiBase screen, double mouseX, double mouseY, double vertical) {
        if (is26x() || !hasLayout(screen)) {
            return false;
        }

        ConfigScreenState state = stateFor(screen);
        ConfigLayout layout = state.layout;

        if (mouseX < layout.sidebarX || mouseX > layout.sidebarX + layout.sidebarW ||
                mouseY < layout.sidebarY || mouseY > layout.sidebarY + layout.sidebarH) {
            return false;
        }

        int viewH = layout.sidebarH - 54;
        int max = Math.max(0, state.treeContentH - viewH);
        state.treeScroll = Math.max(0, Math.min(max,
                state.treeScroll - (int) Math.signum(vertical) * 20));

        ((GuiBaseAccessor) screen).malilibui$getButtons().removeAll(state.treeButtons);
        state.treeButtons.clear();
        state.treeRows.clear();
        state.treeBuilt = false;
        return true;
    }

    private static int layoutAccentX(int rowX) {
        return rowX + 1;
    }

    private static void drawContentHeader(GuiContext ctx, ConfigScreenState state) {
        ConfigLayout layout = state.layout;
        // 婵炴挻鐨滈崟顓炵効闂佸憡鐟禍娆戞崲濮樿埖鍋╂繛鍡楃箳閻涳妇绱掗弮鍌氼€撶紒杈ㄧ箓铻ｉ柨娑樺閻鏌涘顒傂ょ紒顔惧劋缁嬪鍩€椤掑嫬鍙婇柛鎾椾椒绮甸梺闈╄礋閸斿矂宕€电硶鍋撻崷顓熷殌閻忓浚鍨跺浠嬪炊椤掑姣岄梺鍝勭Т妤犲繒妲愬┑瀣劶闁割煈鍠栫敮鎶芥⒑閹绘帞孝妞わ附鐓￠弫?
        String currentTab = prettyModName(getModId(state.screen));

        if (currentTab == null || currentTab.isEmpty()) {
            currentTab = state.screen.getTitleString();
        }

        int titleWidth = layout.contentW - 40;

        if (hasModSwitcher(state)) {
            titleWidth -= 158;
        }

        if (currentTab != null && !currentTab.isEmpty()) {
            int accentY = layout.contentY + (layout.headerH - 12) / 2;
            int textY = layout.contentY + (layout.headerH - 8) / 2;

            UiTheme.fill(ctx, layout.contentX + 12, accentY, 3, 12,
                    UiTheme.TEXT_ACCENT);
            UiTheme.text(ctx, UiTheme.trim(ctx.fontRenderer(), currentTab, titleWidth),
                    layout.contentX + 21, textY, UiTheme.TEXT_TITLE, true);
        }

        // 闁哄鏅滈悷鈺呭闯闁垮鈻旂€广儱鎳庨弲娆撴煟閵忕媭鐓兼い銈呭€瑰鍕冀椤愶絿鏆犻梺鍛婂笒濡繂鈻撹閻ヮ亪宕犻敍鍕崶闂佸搫绉村ú顓€傞懞銉р枖閻庯綆鍋呴悡娆撴煟閵娿儱顏╅柛鐐差嚟閳ь剙婀遍幊鎾诲吹椤掆偓鍗遍柛濠勫枔閻濆爼鏌?
        // 閻庤鐡曠亸娆戝垝閿熺姴绠甸柟閭︿簽鏉╂柨霉濠婂啯顥℃い顐㈩儑閹插瓨寰勬繝鍌溾偓濠氭⒒閸涱喗顥栫紒杈ㄧ懇瀹曟ê顓奸崶銊︽瘞婵炴垶鎸撮崑鎾绘煛婢舵稓纾块柛銈呴叄瀹曪絽顓奸崼姘壕閻忕偠妫勯崜鍏肩箾閹惧啿绾ч柣鎾愁儔閺屻劌鈻庨幒婵嗘闂佹寧绋戦¨鈧柟?
    }

    /**
     * 缂傚倷鐒﹂敋闁糕晜顨嗙粙澶愬焵椤掍胶鈻旀い蹇撳閸ゃ倝鏌?TAB闂佹寧绋掔粙鏍崲閹达附鏅?true 闁荤偞绋忛崝搴ㄥΦ濮樿鲸瀚氶柕澶涚畱閻﹀姊虹粵瀣灈闁活偅蓱缁傚秵鎯旈埥鍡楁闂佺厧澹婃禍婵嬫偉椤旂晫鈻旈柡鍌氱仢閸ゆ帡鏌涢敂鍝勫妞ゆ帗绻冨鍕礋椤撶喎鈧偤鏌ㄥ☉妯煎ⅵ婵炴挸澧庨幑鍕偓鍦Х缁犳牠鏌涘Ο鐓庢灁濠⒀勵殔閵嗘帡宕ｆ径灞藉脯闂佹寧绋戦¨鈧柟?
     */
    public static boolean drawNavButton(GuiBase screen, ButtonBase button, GuiContext ctx,
                                        int mouseX, int mouseY) {
        ConfigScreenState state = STATES.get(screen);

        if (state == null || !state.ready || !state.navButtons.contains(button)) {
            return false;
        }

        ButtonBaseAccessor access = (ButtonBaseAccessor) button;
        String label = access.malilibui$getDisplayString();
        boolean active = !access.malilibui$isEnabled();
        boolean hovered = mouseX >= button.getX() && mouseX < button.getX() + button.getWidth() &&
                mouseY >= button.getY() && mouseY < button.getY() + button.getHeight();

        UiTheme.drawNavTab(ctx, button.getX(), button.getY(), button.getWidth(), button.getHeight(),
                label, active, !active && hovered);

        return true;
    }

    private static String activeTabLabel(ConfigScreenState state) {
        for (ButtonBase button : state.navButtons) {
            ButtonBaseAccessor access = (ButtonBaseAccessor) button;

            if (!access.malilibui$isEnabled()) {
                return access.malilibui$getDisplayString();
            }
        }

        return null;
    }

    private static String getModId(GuiBase screen) {
        if (screen instanceof GuiConfigsBase configs) {
            return configs.getModId();
        }

        // 濠电偞鎸稿鍫曟偂鐎ｎ兛娌柛灞剧矌濡炰粙寮堕崼鐔风仴婵炲懏甯楃粙澶婎吋閸℃?GuiConfigsBase闂佹寧绋掔粙鎰垝閻樿绀岄柛娑卞幗閸婃娊鏌ㄥ☉妯荤稈i.dy.masa.<modId>.*闂?
        // 闂佽浜介崝宥夊蓟閸ヮ灛鐔兼晬閸曨厾鐓?ID闂佹寧绋戦惌渚€宕樼紒妯碱浄闁靛／鍕婵炲瓨绮岀花濂稿疾閸洖鍐€闊洦姊婚崹鐓幬涢悧鍫叕闁?
        String[] parts = screen.getClass().getName().split("\\.");

        for (int i = 1; i < parts.length; i++) {
            if ("masa".equals(parts[i - 1])) {
                return parts[i];
            }
        }

        return "config";
    }

    private static String prettyModName(String modId) {
        if (modId == null || modId.isEmpty()) {
            return "Config";
        }

        String name = modId.replace('_', ' ').replace('-', ' ');
        StringBuilder builder = new StringBuilder(name.length());

        for (String part : name.split(" ")) {
            if (part.isEmpty()) {
                continue;
            }

            if (builder.length() > 0) {
                builder.append(' ');
            }

            builder.append(part.substring(0, 1).toUpperCase(Locale.ROOT))
                    .append(part.substring(1));
        }

        return builder.toString();
    }
}
