package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BurrowUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;


public class SelfFill extends Module  {

    public Setting<Boolean> rotate = register(new Setting<>("Rotate", Boolean.valueOf(false)));
    public Setting<Float> offset = register(new Setting("Offset", Float.valueOf(7.0F), Float.valueOf(-20.0F), Float.valueOf(20.0F)));
    private BlockPos originalPos;
    private int oldSlot = -1;

    public SelfFill() {
        super("Burrow", "Blocks yourself", Module.Category.COMBAT, true, false, false);
    }


    @Override
    public void onEnable() {
        super.onEnable();

        // Save our original pos
        originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

        // If we can't place in our actual post then toggle and return
        if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) ||
                intersectsWithEntity(this.originalPos)) {
            toggle();
            return;
        }

        // Save our item slot
        oldSlot = mc.player.inventory.currentItem;
    }
    @Override
    public void onUpdate() {
        // If we don't have obsidian in hotbar toggle and return
        if (BurrowUtil.findHotbarBlock(BlockObsidian.class) == -1 && (BurrowUtil.findHotbarBlock(BlockEnderChest.class) == -1)) {
            Command.sendMessage("Can't find Obsidian or EChest in hotbar!");
            this.disable();
            return;
        }

        // Change to obsidian slot
        if (BurrowUtil.findHotbarBlock(BlockObsidian.class) == -1) { BurrowUtil.switchToSlot(BurrowUtil.findHotbarBlock(BlockEnderChest.class)); }
        if (BurrowUtil.findHotbarBlock(BlockEnderChest.class) == -1) { BurrowUtil.switchToSlot(BurrowUtil.findHotbarBlock(BlockObsidian.class)); }
        if (BurrowUtil.findHotbarBlock(BlockEnderChest.class) != -1 && (BurrowUtil.findHotbarBlock(BlockObsidian.class) != -1)) { BurrowUtil.switchToSlot(BurrowUtil.findHotbarBlock(BlockEnderChest.class)); }


        // Fake jump
        final float[] currentYawPitch = {SelfFill.mc.player.cameraYaw, SelfFill.mc.player.cameraPitch};
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805211997D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821D, mc.player.posZ, true));


        // Place block
        BurrowUtil.placeBlock(originalPos, EnumHand.MAIN_HAND, rotate.getValue(), true, false);


        // Rubberband
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset.getValue(), mc.player.posZ, false));
        SelfFill.mc.player.connection.sendPacket((Packet) new CPacketPlayer.Rotation(currentYawPitch[0], currentYawPitch[1], true));
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        mc.player.setSneaking(false);

        // SwitchBack
        BurrowUtil.switchToSlot(this.oldSlot);

        // AutoDisable
        this.disable();
        return;
    }

    private boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity.equals(mc.player)) continue;
            if (entity instanceof EntityItem) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
        }
        return false;
    }
}