package com.sou.dw.jpa.service;

import com.google.common.collect.ImmutableMap;
import com.sou.dw.jpa.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerService extends AbstractService<Player> {

    public List<Player> findPlayerByName(final Optional<String> name) {
        if (name.isPresent()) {
            return dao.find(entityClass, "Player.findByName", ImmutableMap.of("name", name.get()));
        } else {
            return new ArrayList<>(0);
        }
    }
}
