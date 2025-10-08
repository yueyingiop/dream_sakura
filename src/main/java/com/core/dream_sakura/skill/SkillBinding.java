package com.core.dream_sakura.skill;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import com.core.dream_sakura.dream_sakura;
import com.core.dream_sakura.items.DecorationItem;
import com.core.dream_sakura.network.PacketHandler;
import com.core.dream_sakura.network.SkillTriggerPacket;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

@Mod.EventBusSubscriber(modid = dream_sakura.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SkillBinding {
    private final int KeyCode; // 按键码
    private final String description;  // 描述
    private final BiConsumer<Player, ItemStack> callback; // 回调,技能实现
    private final int cooldown; // 技能冷却时间
    private long lastUsedTime = 0; // 技能最后使用时间
    @OnlyIn(Dist.CLIENT)
    private Lazy<KeyMapping> keyMapping; // 按键映射
    private boolean isActive = false; // 技能是否激活
    private final String itemId; // 技能物品ID

    public SkillBinding(int keyCode, String description, int cooldownTime, String itemId, BiConsumer<Player, ItemStack> skillAction) {
        this.KeyCode = keyCode;
        this.description = description;
        this.cooldown = cooldownTime;
        this.itemId = itemId;
        this.callback = skillAction;
        if (FMLEnvironment.dist == Dist.CLIENT) { // 环境检查
            this.keyMapping = Lazy.of(() -> new KeyMapping(
                "skill." + this.description.replace(' ', '_').toLowerCase(), // 技能描述
                this.KeyCode, // 按键码
                "key.category.dream_sakura.skills" // 技能分类
            ));
        }
        
        
        
    }

    public String getDescription(){
        return description;
    }

    public Lazy<KeyMapping> getkeyMapping(){
        return keyMapping;
    }

    public String getItemId(){
        return itemId;
    }

    public int getCooldown() {
        return cooldown;
    }

    public long getLastUsedTime() {
        return lastUsedTime;
    }

    public void setLastUsedTime(long time) {
        this.lastUsedTime = time;
    }

    // 客户端按键检测
    @Mod.EventBusSubscriber(modid = dream_sakura.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            for (SkillBinding binding : SkillRegistry.getBindings()) {
                event.register(binding.keyMapping.get());
            }
        }

        @Mod.EventBusSubscriber(modid = dream_sakura.MODID, value = Dist.CLIENT)
        public static class ClientForgeEvents {
            @SubscribeEvent
            public static void onClientTick(TickEvent.ClientTickEvent event) {
                if (event.phase == TickEvent.Phase.END) {
                    for (SkillBinding binding : SkillRegistry.getBindings()) {
                        Player player = Minecraft.getInstance().player;
                        if (player != null && binding.isWearingBoundItem(player)) {
                            if (binding.keyMapping.get().isDown()) {
                                PacketHandler.sendToServer(new SkillTriggerPacket(binding.getItemId()));
                                binding.isActive = true;
                            }
                        } else {
                            // 未佩戴饰品时重置激活状态
                            binding.isActive = false;
                        }
                    }
                }
            }
        }
    }



    // 触发技能
    public void triggerSkill(Player player) {
        if (isWearingBoundItem(player)) {
            ItemStack stack = getBoundItemStack(player);
            callback.accept(player, stack);
            // dream_sakura.LOGGER.info("技能按键是:"+keyMapping.get().getKey().getValue() + keyMapping.get().getKey().getName() + "原来按键是:" + this.KeyCode);
        }
    }

    // 检查玩家是否佩戴了绑定的饰品
    public boolean isWearingBoundItem(Player player) {
        Optional<ICuriosItemHandler> curiosHandler = CuriosApi.getCuriosInventory(player).resolve();
        if (curiosHandler.isPresent()) {
            Map<String, ICurioStacksHandler> stacksHandlers = curiosHandler.get().getCurios();
            ICurioStacksHandler haloHandler = stacksHandlers.get("halo");
            if (haloHandler != null) {
                IDynamicStackHandler stacks = haloHandler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    ItemStack stack = stacks.getStackInSlot(i);
                    if (stack.getItem() instanceof DecorationItem) {
                        DecorationItem item = (DecorationItem) stack.getItem();
                        if (itemId.equals(item.getItemId())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // 获取绑定的饰品堆栈
    private ItemStack getBoundItemStack(Player player) {
        Optional<ICuriosItemHandler> curiosHandler = CuriosApi.getCuriosInventory(player).resolve();
        if (curiosHandler.isPresent()) {
            Map<String, ICurioStacksHandler> stacksHandlers = curiosHandler.get().getCurios();
            ICurioStacksHandler haloHandler = stacksHandlers.get("halo");
            if (haloHandler != null) {
                IDynamicStackHandler stacks = haloHandler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    ItemStack stack = stacks.getStackInSlot(i);
                    if (stack.getItem() instanceof DecorationItem) {
                        DecorationItem item = (DecorationItem) stack.getItem();
                        if (itemId.equals(item.getItemId())) {
                            return stack;
                        }
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    // 检查冷却时间
    public boolean isCooledDown() {
        return System.currentTimeMillis() - lastUsedTime >= cooldown;
    }
}

