package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.RuleName;
import com.poseidon.poseidon.exceptions.RuleNameNotFoundException;
import com.poseidon.poseidon.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleNameService implements IRuleNameService {

    @Autowired
    RuleNameRepository repository;

    @Override
    public List<RuleName> findAll() {
        return repository.findAll();
    }

    @Override
    public void save(RuleName ruleName) {
        repository.save(ruleName);
    }

    @Override
    public void update(RuleName updatedRuleName, int id) {
        RuleName oldRuleName = findById(id);
        repository.save(updateFields(oldRuleName, updatedRuleName));
    }

    @Override
    public RuleName findById(Integer id) {
        return repository.findById(id).orElseThrow(RuleNameNotFoundException::new);
    }

    @Override
    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

    private RuleName updateFields(RuleName ruleName, RuleName updatedRuleName) {
        ruleName.setName(updatedRuleName.getName());
        ruleName.setDescription(updatedRuleName.getDescription());
        ruleName.setJson(updatedRuleName.getJson());
        ruleName.setTemplate(updatedRuleName.getTemplate());
        ruleName.setSqlPart(updatedRuleName.getSqlPart());
        ruleName.setSqlStr(updatedRuleName.getSqlStr());
        return ruleName;
    }
}
