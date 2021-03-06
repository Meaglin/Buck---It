package net.minecraft.server;

public class InventoryLargeChest implements IInventory {

    private String a;
    private IInventory b;
    private IInventory c;

    // CraftBukkit start
    public ItemStack[] getContents() {
        ItemStack[] result = new ItemStack[q_()];
        for (int i = 0; i < result.length; i++) {
            result[i] = c_(i);
        }
        return result;
    }
    // CraftBukkit end

    public InventoryLargeChest(String s, IInventory iinventory, IInventory iinventory1) {
        this.a = s;
        this.b = iinventory;
        this.c = iinventory1;
    }

    public int q_() {
        return this.b.q_() + this.c.q_();
    }

    public String c() {
        return this.a;
    }

    public ItemStack c_(int i) {
        return i >= this.b.q_() ? this.c.c_(i - this.b.q_()) : this.b.c_(i);
    }

    public ItemStack a(int i, int j) {
        return i >= this.b.q_() ? this.c.a(i - this.b.q_(), j) : this.b.a(i, j);
    }

    public void a(int i, ItemStack itemstack) {
        if (i >= this.b.q_()) {
            this.c.a(i - this.b.q_(), itemstack);
        } else {
            this.b.a(i, itemstack);
        }
    }

    public int r_() {
        return this.b.r_();
    }

    public void i() {
        this.b.i();
        this.c.i();
    }

    public boolean a_(EntityHuman entityhuman) {
        return this.b.a_(entityhuman) && this.c.a_(entityhuman);
    }
}
