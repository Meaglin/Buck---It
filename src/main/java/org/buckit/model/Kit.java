package org.buckit.model;

import org.bukkit.inventory.ItemStack;

public class Kit {
    private int     id, minaccesslevel, delay;
    private String  name;
    private int[][] items;

    /**
     * items array is a 2 dimensional array consisting of array( array( itemId,
     * amount, damage ) )
     * 
     * @param name
     * @param items
     * @param delay
     */
    public Kit(int id, String name, int[][] items, int minaccesslevel, int delay) {
        this.name = name;
        this.items = items;
        this.delay = delay;
    }

    public int[][] getItemsArray() {
        return items;
    }

    public ItemStack[] getItems() {
        ItemStack[] rt = new ItemStack[items.length];
        for (int i = 0; i < items.length; i++) {
            rt[i] = new ItemStack(items[i][0], items[i][1], (short) items[i][2]);
        }
        return rt;
    }

    public String getName() {
        return name;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the minaccesslevel
     */
    public int getMinaccesslevel() {
        return minaccesslevel;
    }

    /**
     * @return the delay
     */
    public int getDelay() {
        return delay;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Kit))
            return false;

        if (getId() == ((Kit) object).getId())
            return true;

        return false;
    }
}
