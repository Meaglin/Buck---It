package org.buckit.datasource;

import org.bukkit.entity.Player;

public interface Request {
    public boolean accept(Player acceptingPlayer);
}
