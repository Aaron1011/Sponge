package org.spongepowered.mod.test.integration;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.mctester.api.junit.MinecraftRunner;
import org.spongepowered.mctester.internal.BaseTest;
import org.spongepowered.mctester.junit.TestUtils;

@RunWith(MinecraftRunner.class)
public class MineBlockTest extends BaseTest {

    public MineBlockTest(TestUtils testUtils) {
        super(testUtils);
    }

    @Test
    public void testMineAllowedBlock() throws Throwable {
        this.testUtils.getThePlayer().getInventory().clear();
        this.testUtils.waitForInventoryPropagation();
        this.testUtils.getThePlayer().getInventory().offer(ItemStack.of(ItemTypes.DIRT, 1));
        this.testUtils.getThePlayer().getInventory().offer(ItemStack.of(ItemTypes.DIAMOND_SHOVEL, 1));
        this.testUtils.waitForInventoryPropagation();

        this.testUtils.getClient().selectHotbarSlot(0);
        Vector3i blockPosition =
                this.testUtils.runOnMainThread(() -> this.testUtils.getThePlayer().getPosition().add(new Vector3d(-2, -1, 0)).toInt());
        this.testUtils.getClient().lookAtBlock(blockPosition);
        this.testUtils.getClient().rightClick();
        this.testUtils.sleepTicks(5);

        this.testUtils.getClient().selectHotbarSlot(1);
        try {
            this.testUtils.runOnMainThread(() -> {
                Assert.assertEquals(BlockTypes.DIRT, testUtils.getThePlayer().getWorld().getBlockType(blockPosition.toInt().add(0, 1, 0)));
                testUtils.getThePlayer().offer(Keys.GAME_MODE, GameModes.SURVIVAL);
            });
            this.testUtils.waitForAll();
            this.testUtils.getClient().holdLeftClick(true);
            this.testUtils.getClient().sleepTicksClient(10);
            this.testUtils.getClient().holdLeftClick(false);
            this.testUtils.runOnMainThread(() -> {
                Assert.assertNotEquals(BlockTypes.DIRT, testUtils.getThePlayer().getWorld().getBlockType(blockPosition.toInt().add(0, 1, 0)));
            });
        } catch (Throwable e) {
            throw new AssertionError(e);
        }
    }
}
