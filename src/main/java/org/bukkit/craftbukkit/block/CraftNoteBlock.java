package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityNote;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.NoteBlock;
import org.bukkit.craftbukkit.CraftWorld;

/**
 * Represents a note block.
 *
 * @author sk89q
 */
public class CraftNoteBlock extends CraftBlockState implements NoteBlock {
    private final CraftWorld world;
    private final TileEntityNote note;

    public CraftNoteBlock(final Block block) {
        super(block);

        world = (CraftWorld)block.getWorld();
        note = (TileEntityNote)world.getTileEntityAt(getX(), getY(), getZ());
    }

    public byte getNote() {
        return note.a;
    }

    public void setNote(byte n) {
        note.a = n;
    }

    public boolean play() {
        Block block = getBlock();

        synchronized (block) {
            if (block.getType() == Material.NOTE_BLOCK) {
                note.a(world.getHandle(), getX(), getY(), getZ());
                return true;
            } else {
                return false;
            }
        }
    }
}
