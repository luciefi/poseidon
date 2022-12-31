package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.RuleName;

import java.util.List;

public interface IRuleNameService {
    List<RuleName> findAll();

    void save(RuleName ruleName);

    void update(RuleName ruleName, int id);

    RuleName findById(Integer id);

    void delete(Integer id);
}
