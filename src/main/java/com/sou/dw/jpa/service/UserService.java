package com.sou.dw.jpa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;
import com.sou.rest.basicauth.User;

public class UserService extends AbstractService<User> {

	public List<User> findUserByName(final Optional<String> name) {
        if (name.isPresent()) {
            return dao.find(entityClass, "User.findByName", ImmutableMap.of("name", name.get()));
        } else {
            return new ArrayList<>(0);
        }
    }
}
