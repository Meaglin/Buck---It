package net.minecraft.server;

// CraftBukkit start
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockPlaceEvent;
// CraftBukkit end

public class ItemBlock extends Item {

    private int a;

    public ItemBlock(int i) {
        super(i);
        this.a = i + 256;
        this.b(Block.byId[i + 256].a(2));
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit;

        if (world.getTypeId(i, j, k) == Block.SNOW.id) {
            l = 0;
        } else {
            if (l == 0) {
                --j;
            }

            if (l == 1) {
                ++j;
            }

            if (l == 2) {
                --k;
            }

            if (l == 3) {
                ++k;
            }

            if (l == 4) {
                --i;
            }

            if (l == 5) {
                ++i;
            }
        }

        if (itemstack.count == 0) {
            return false;
        } else {
            if (world.a(this.a, i, j, k, false)) {
                Block block = Block.byId[this.a];

                // CraftBukkit start - This executes the placement of the block
                BlockState replacedBlockState = CraftBlockState.getBlockState(world, i, j, k);

                // Special case the silly stepstone :'(
                if (l == 1 && world.getTypeId(i, j - 1, k) == Block.STEP.id && itemstack.id == Block.STEP.id) {
                    replacedBlockState = CraftBlockState.getBlockState(world, i, j - 1, k);
                }
                /**
                 * @see net.minecraft.server.World#b(int i, int j, int k, int l, int i1)
                 * 
                 * This replaces world.b(IIIII), we're doing this because we need to
                 * hook between the 'placement' and the informing to 'world' so we can
                 * sanely undo this.
                 *
                 * Whenever the call to 'world.b' changes we need to figure out again what to
                 * replace this with.
                 */
                if (world.setTypeIdAndData(i, j, k, a, a(itemstack.h()))) { // <-- world.b does this to place the block
                    BlockPlaceEvent event = CraftEventFactory.callBlockPlaceEvent(world, entityhuman, replacedBlockState, clickedX, clickedY, clickedZ, block);

                    if (event.isCancelled() || !event.canBuild()) {
                        // CraftBukkit Undo!

                        if ((this.a == Block.STEP.id) && (world.getTypeId(i, j - 1, k) == Block.DOUBLE_STEP.id) && (world.getTypeId(i, j, k) == 0)) {
                            // Half steps automatically set the block below to a double
                            world.setTypeId(i, j - 1, k, 44);

                        } else {

                            if (this.a == Block.ICE.id) {
                                // Ice will explode if we set straight to 0
                                world.setTypeId(i, j, k, 20);
                            }

                            world.setTypeIdAndData(i, j, k, replacedBlockState.getTypeId(), replacedBlockState.getRawData());
                        }

                    } else {
                        world.f(i, j, k, a); // <-- world.b does this on success (tell the world)

                        Block.byId[this.a].d(world, i, j, k, l);
                        Block.byId[this.a].a(world, i, j, k, (EntityLiving) entityhuman);
                        world.a((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), block.stepSound.c(), (block.stepSound.a() + 1.0F) / 2.0F, block.stepSound.b() * 0.8F);
                        --itemstack.count;
                    }
                    // CraftBukkit end
                }
            }

            return true;
        }
    }

    public String a() {
        return Block.byId[this.a].f();
    }
}
