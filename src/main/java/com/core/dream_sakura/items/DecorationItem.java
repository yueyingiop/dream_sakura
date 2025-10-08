package com.core.dream_sakura.items;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.core.dream_sakura.enums.DamageType;
import com.core.dream_sakura.items.client.DecorationRenderer;
import com.core.dream_sakura.items.client.IGlowingItem;
import com.core.dream_sakura.listener.IDamageImmunity;
import com.core.dream_sakura.network.PacketHandler;
import com.core.dream_sakura.network.PlayMusicPacket;
import com.core.dream_sakura.skill.SkillBinding;
import com.core.dream_sakura.skill.SkillRegistry;
import com.core.dream_sakura.tooltip.TooltipHelper;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtils;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class DecorationItem extends Item implements ICurioItem, GeoItem, IDamageImmunity, IGlowingItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final String itemId;
    private final BiConsumer<SlotContext, ItemStack> curioEquipCallback;
    private Function<ItemStack, Set<DamageType>> immunityProvider;
    private final float[] glowColor; // 发光颜色
    private final List<Integer> tooltipColor; // 工具提示颜色列表
    private final SkillBinding skillBinding; // 技能绑定
    private final ResourceLocation musicResource; // 音乐资源


    /**
     * 构造函数
     * @param itemIDString - 物品ID字符串
     * @param properties - 物品属性
     * @param curioTickCallback - 装备时的tick回调函数
     * @param immunityProvider - 伤害免疫提供者
     * @param glowColor - 发光颜色，RGB数组
     * @param tooltipColor - 工具提示颜色，RGB数组
     * @param skillBinding - 技能绑定
     * @param musicResource - 音乐资源
    */ 
    public DecorationItem(
        String itemIDString ,
        Properties properties, 
        BiConsumer<SlotContext, ItemStack> curioTickCallback,
        Function<ItemStack, Set<DamageType>> immunityProvider,
        float[] glowColor,
        List<Integer> tooltipColor,
        SkillBinding skillBinding,
        ResourceLocation musicResource
        )
    {
        super(properties.stacksTo(1));
        this.itemId = itemIDString; // 设置物品ID
        this.curioEquipCallback = curioTickCallback; // 设置装备时的tick回调函数
        this.immunityProvider = immunityProvider; // 设置伤害免疫提供者
        this.glowColor = glowColor != null ? glowColor : new float[]{}; // 默认空列表
        this.tooltipColor = tooltipColor != null ? tooltipColor : Collections.emptyList(); // 默认空颜色列表
        this.skillBinding = skillBinding;
        this.musicResource = musicResource;

        SkillRegistry.registerBinding(this.skillBinding);  // 注册技能绑定
    }

    /**
     * 重载构造函数
     * - 用于只提供回调函数
     * @param itemIDString - 物品ID字符串
     * @param properties - 物品属性
     * @param curioTickCallback - 装备时的tick回调函数
    */ 
    public DecorationItem(String itemIDString, Properties properties, 
                         BiConsumer<SlotContext, ItemStack> curioTickCallback) {
        this(
            itemIDString, properties, curioTickCallback, 
            stack -> Collections.emptySet(), 
            new float[]{1.0f, 0.84f, 0.0f}, 
            null, null, null
        );
    }

    /**
     * 重载构造函数
     * - 用于只提供回调函数和发光颜色
     * @param itemIDString - 物品ID字符串
     * @param properties - 物品属性
     * @param curioTickCallback - 装备时的tick回调函数
     * @param glowColor - 发光颜色，RGB数组
    */
    public DecorationItem(String itemIDString, Properties properties, 
                         BiConsumer<SlotContext, ItemStack> curioTickCallback,
                         float[] glowColor) {
        this(
            itemIDString, properties, curioTickCallback, 
            stack -> Collections.emptySet(), 
            glowColor, null, null, null
        );
    }

    /**
     * 重载构造函数
     * - 用于只提供回调函数和工具提示颜色
     * @param itemIDString
     * @param properties
     * @param curioTickCallback
     * @param colorList
    */
    public DecorationItem(String itemIDString, Properties properties, 
                         BiConsumer<SlotContext, ItemStack> curioTickCallback,
                         List<Integer> colorList){
        this(
            itemIDString, properties, curioTickCallback,
            stack -> Collections.emptySet(), 
            new float[]{1.0f, 0.84f, 0.0f}, 
            colorList, 
            null, null
        );
    }

    /**
     * 重载构造函数
     * - 用于只提供回调函数和技能绑定
     * @param itemIDString
     * @param properties
     * @param curioTickCallback
     * @param skillBinding
    */
    public DecorationItem(String itemIDString, Properties properties, 
                         BiConsumer<SlotContext, ItemStack> curioTickCallback,
                         SkillBinding skillBinding) {
        this(
            itemIDString, properties, curioTickCallback, 
            stack -> Collections.emptySet(), 
            new float[]{1.0f, 0.84f, 0.0f}, 
            null,
            skillBinding, null
        );
    }



    // 实现IGlowingItem接口
    @Override
    public float[] getGlowColor() {
        return glowColor;
    }

    @Override
    public float getGlowIntensity() {
        return 1.0f;
    }



    /**
     * 核心动画逻辑：定义动画状态机
     * - 来自于geckolib
     * @param controllers - 动画控制器注册器 
    */ 
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::handleAnimations));
    }

    // 创建一个RawAnimation对象，用于定义物品的闲置动画
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation");

    /**
     * 处理动画状态
     * - 来自于geckolib
     * @param state - 动画状态
     * @return - 播放状态
    */
    private <T extends GeoItem> PlayState handleAnimations(AnimationState<T> state) {
        if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
            state.getController().setAnimation(IDLE);
        }
        return PlayState.CONTINUE;
    }
    
    @Override
    /**
     * 获取动画实例缓存
     * - 来自于geckolib
     * @return - 动画实例缓存
    */
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    } 

    /**
     * 获取当前tick
     * - 来自于geckolib
     * @param ItemStack - 物品栈
     * @return - 当前tick
    */ 
    @Override
    public double getTick(Object ItemStack) {
        return RenderUtils.getCurrentTick();
    }
    
    /**
     * 初始化客户端扩展
     * - 来自于geckolib
     * @param consumer - 客户端扩展消费者
     * @return - 无返回值
    */
    @Override
    public void initializeClient(@Nonnull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private DecorationRenderer renderer;
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new DecorationRenderer();
                }
                return this.renderer;
            }

        });
    }



    /**
     * 判断物品是否可以装备
     * - 来自于Curios API
     * @param slotContext - 槽位上下文
     * @param stack - 物品栈
     * @return - 是否可以装备
     */
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack){
        return "halo".equals(slotContext.identifier());
    }

    /**
     * 饰品装备时触发
     * - 来自于Curios API
     * @param slotContext - 槽位上下文
     * @param prevStack - 先前的装饰品栈
     * @param stack - 装饰品堆栈
    */
    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ICurioItem.super.onEquip(slotContext, prevStack, stack);

        // 音乐播放
        if (!slotContext.entity().level().isClientSide() && isPlayingMusic()) {
            PacketHandler.sendToClientsTrackingEntity(
                new PlayMusicPacket(this.musicResource, true),
                slotContext.entity()
            );
        }
    }

    /**
     * 饰品取消装备时触发
     * - 来自于Curios API
     * @param slotContext - 槽位上下文
     * @param newStack - 新的装饰品堆栈
     * @param stack - 装饰品堆栈
    */
    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);
        
        // 当卸下饰品时停止音乐
        if (!slotContext.entity().level().isClientSide() && isPlayingMusic()) {
            PacketHandler.sendToClientsTrackingEntity(
                new PlayMusicPacket(this.musicResource, false),
                slotContext.entity()
            );
        }

        disableMayfly("dream_finale", slotContext);
    }

    /**
     * 禁用能力
     * - 用于onUnequip函数
     * @param itemID - 禁用物品ID
     * @param slotContext - 槽位上下文
    */
    public void disableMayfly(String itemID, SlotContext slotContext){
        if (itemID.equals(this.itemId)) {
            LivingEntity entity = slotContext.entity();
            if (entity instanceof Player player && !player.level().isClientSide()) {
                Abilities abilities = player.getAbilities();
                if (!player.isCreative()) {
                    abilities.mayfly = false;
                    abilities.flying = false;
                    abilities.invulnerable = false;
                    if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                        serverPlayer.onUpdateAbilities();
                    }
                }
            }
        }
    }

    /**
     * 是否允许右键直接装备 
     * - 来自于Curios API
     * @param slotContext - 物品上下文
     * @param stack - 物品栈
     * @return - 返回true表示允许右键直接装备
    */ 
    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    /**
     * tick事件
     * - 来自Curios API
     * @param slotContext - 物品上下文
     * @param stack - 物品栈
     */
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        // 执行callback
        if (curioEquipCallback != null) {
            curioEquipCallback.accept(slotContext, stack);
        }
    }



    /**
     * 添加文本提示
     * - 来自minecraft
     * @param stack - 物品栈
     * @param level - 游戏世界
     * @param tooltip - 提示文本列表
     * @param flag - 提示标志
    */
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag){
        super.appendHoverText(stack, level, tooltip, flag);
        CompoundTag stackTag = stack.getOrCreateTag();
        if (!stackTag.contains("level")) {
            stackTag.putInt("level", 1);
        }
        if (!stackTag.contains("xp")) {
            stackTag.putInt("xp", 0);
            stackTag.putInt("maxXp", 1024);
        }
        int itemLevel = stackTag.getInt("level");
        int itemXp = stackTag.getInt("xp");
        int itemMaxXp = stackTag.getInt("maxXp");
        tooltip.add(
            Component.translatable(
                "tooltip.dream_sakura.level",
                itemLevel
            ).withStyle(Style.EMPTY.withColor(0xFF00FF))
        );
        tooltip.add(
            Component.translatable(
                "tooltip.dream_sakura.xp",
                itemXp,
                itemMaxXp
            ).withStyle(Style.EMPTY.withColor(0xADD8E6))
        );

        TooltipHelper.addTooltip(itemId, stack, level, tooltip, flag, tooltipColor);
        if (this.skillBinding != null) {
            TooltipHelper.addSkillsDescription(
                tooltip,
                this.skillBinding.getDescription(),
                this.skillBinding.getkeyMapping().get().getKey().getName()
            );
        }
    }

    // 允许重命名
    @Override
    public int getEnchantmentValue() {
        return 1;
    }



    /**
     * 获取物品ID
     * - 自定义
     * @return - 物品ID字符串
    */
    public String getItemId(){
        return itemId;
    }

    /**
     * 是否存在播放资源
     * - 自定义
     * @return - 布尔值
    */
    public boolean isPlayingMusic() {
        return this.musicResource != null;
    }

    /**
     * 获取物品播放的音乐资源
     * - 自定义
     * @return - 音乐资源
    */
    public ResourceLocation getMusicResource() {
        return this.musicResource;
    }

    /**
     * 获取物品的伤害免疫类型
     * - 来自自定义IDamageImmunity接口
     * @param stack - 物品栈
     * @return - 伤害类型集合
    */
    @Override
    public Set<DamageType> getImmunities(ItemStack stack) {
        return immunityProvider.apply(stack);
    }

    public void setImmunityProvider(Function<ItemStack, Set<DamageType>> immunityProvider) {
        this.immunityProvider = immunityProvider;
    }
}
