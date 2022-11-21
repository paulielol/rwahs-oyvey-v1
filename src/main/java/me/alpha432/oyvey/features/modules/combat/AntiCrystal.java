package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.combat.HoleFiller;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.item.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;

public class AntiCrystal extends Module {

    int currentDelay;
    EntityEnderCrystal crystal;
    int oldSlot;


    public AntiCrystal() {
        super("AntiCrystal", "godly module", Category.MISC, true, false, false);
        this.currentDelay = 0;
        this.crystal = null;
        this.oldSlot = 0;
    }

    public Setting<Double> range = this.register(new Setting<Double>("Range", 4.0, 1.0, 10.0));
    public Setting<Boolean> button = this.register(new Setting<Boolean>("Button", false));
    public Setting<Boolean> web = this.register(new Setting<Boolean>("Web", true));
    public Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 1, 1, 20));
    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    public Setting<Boolean> ghostSwitch = this.register(new Setting<Boolean>("SilentSwitch", true));


    public void onEnable() {
        super.onEnable();
        this.oldSlot = this.mc.player.inventory.currentItem;
    }

    public void onDisable() {
        super.onDisable();
        if (ghostSwitch.getValue()) {
            InventoryUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
            InventoryUtil.mc.playerController.updateController();
            InventoryUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
            InventoryUtil.mc.player.inventory.currentItem = oldSlot;
            InventoryUtil.mc.playerController.updateController();
        }
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.currentDelay % (int) this.delay.getValue() == 0) {
            this.crystal = this.getCrystal();
            if (this.crystal != null) {
                this.handleSwitch();
                BlockUtil.placeBlock(this.crystal.getPosition(), EnumHand.MAIN_HAND, rotate.getValue(), false, false);
                int originalSlot = HoleFiller.mc.player.inventory.currentItem;
                InventoryUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(originalSlot));
                InventoryUtil.mc.player.inventory.currentItem = originalSlot;
                InventoryUtil.mc.playerController.updateController();
            }
        }
        ++this.currentDelay;
    }

    public EntityEnderCrystal getCrystal() {
        EntityEnderCrystal crystal = null;
        for (final Entity e : this.mc.world.loadedEntityList) {
            if (e instanceof EntityEnderCrystal && this.mc.player.getDistance(e) <= (double) this.range.getValue() && this.mc.world.getBlockState(e.getPosition()).getBlock() == Blocks.AIR) {
                crystal = (EntityEnderCrystal) e;
            }
        }
        return crystal;
    }

    public void handleSwitch() {
        if (hasString()) {
            if (ghostSwitch.getValue() && button.getValue()) {
                int buttonSlot = InventoryUtil.findHotbarBlock(BlockButton.class);
                InventoryUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(buttonSlot));
                InventoryUtil.mc.playerController.updateController();
                if (buttonSlot == -1) {
                    Command.sendMessage("No Buttons in hotbar... disabling");
                    return;
                }
            } else {
                if (button.getValue()) {
                    int buttonSlot = InventoryUtil.findHotbarBlock(BlockButton.class);
                    InventoryUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(buttonSlot));
                    InventoryUtil.mc.player.inventory.currentItem = buttonSlot;
                    InventoryUtil.mc.playerController.updateController();
                    if (buttonSlot == -1) {
                        Command.sendMessage("No Buttons in hotbar... disabling");
                        return;
                    }
                }
            }
        }
        if (ghostSwitch.getValue() && web.getValue()) {
            int webSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
            InventoryUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(webSlot));
            InventoryUtil.mc.playerController.updateController();
            if (webSlot == -1) {
                Command.sendMessage("No Webs in hotbar... disabling");
                this.toggle();
            }
        } else {
            if (web.getValue()) {
                int webSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
                InventoryUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(webSlot));
                InventoryUtil.mc.player.inventory.currentItem = webSlot;
                InventoryUtil.mc.playerController.updateController();
                if (webSlot == -1) {
                    Command.sendMessage("No Webs in hotbar... disabling");
                    this.toggle();
                }
            }
        }
    }


    public boolean hasString() {
        boolean hasStr = false;
        for (int i = 0; i < 9; ++i) {
            if (this.mc.player.inventory.getStackInSlot(i).getItem() == Items.STRING) {
                hasStr = true;
            }
        }
        return hasStr;
    }
}
