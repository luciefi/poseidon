package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.RuleName;
import com.poseidon.poseidon.exceptions.RuleNameNotFoundException;
import com.poseidon.poseidon.repositories.RuleNameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RuleNameServiceTest {

    final int RULE_NAME_ID = 1;
    final String RULE_NAME_NAME = "name";
    final String RULE_NAME_DESCRIPTION = "description";
    final String RULE_NAME_JSON = "json";
    final String RULE_NAME_TEMPLATE = "template";
    final String RULE_NAME_SQL_STR = "sql str";
    final String RULE_NAME_SQL_PART = "sql part";

    @InjectMocks
    private RuleNameService service;

    @Mock
    private RuleNameRepository repository;


    @Test
    public void testFindAll() {
        // Act
        service.findAll();

        // Assert
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testSave() {
        // Arrange
        RuleName newRuleName = new RuleName();
        newRuleName.setName(RULE_NAME_NAME);
        newRuleName.setDescription(RULE_NAME_DESCRIPTION);
        newRuleName.setJson(RULE_NAME_JSON);
        newRuleName.setTemplate(RULE_NAME_TEMPLATE);
        newRuleName.setSqlStr(RULE_NAME_SQL_STR);
        newRuleName.setSqlPart(RULE_NAME_SQL_PART);

        // Act
        service.save(newRuleName);

        // Assert
        ArgumentCaptor<RuleName> ruleName = ArgumentCaptor.forClass(RuleName.class);
        verify(repository, times(1)).save(ruleName.capture());
        assertEquals(RULE_NAME_NAME, ruleName.getValue().getName());
        assertEquals(RULE_NAME_DESCRIPTION, ruleName.getValue().getDescription());
        assertEquals(RULE_NAME_JSON, ruleName.getValue().getJson());
        assertEquals(RULE_NAME_TEMPLATE, ruleName.getValue().getTemplate());
        assertEquals(RULE_NAME_SQL_STR, ruleName.getValue().getSqlStr());
        assertEquals(RULE_NAME_SQL_PART, ruleName.getValue().getSqlPart());
    }

    @Test
    public void testUpdate() {
        // Arrange
        RuleName RuleNameFromRepo = new RuleName();
        RuleNameFromRepo.setId(RULE_NAME_ID);
        when(repository.findById(RULE_NAME_ID)).thenReturn(Optional.of(RuleNameFromRepo));
        RuleName updatedRuleName = new RuleName();
        updatedRuleName.setName(RULE_NAME_NAME);
        updatedRuleName.setDescription(RULE_NAME_DESCRIPTION);
        updatedRuleName.setJson(RULE_NAME_JSON);
        updatedRuleName.setTemplate(RULE_NAME_TEMPLATE);
        updatedRuleName.setSqlStr(RULE_NAME_SQL_STR);
        updatedRuleName.setSqlPart(RULE_NAME_SQL_PART);

        // Act
        service.update(updatedRuleName, RULE_NAME_ID);

        // Assert
        ArgumentCaptor<RuleName> ruleName = ArgumentCaptor.forClass(RuleName.class);
        verify(repository, times(1)).save(ruleName.capture());
        assertEquals(RULE_NAME_ID, ruleName.getValue().getId(), 0);
        assertEquals(RULE_NAME_NAME, ruleName.getValue().getName());
        assertEquals(RULE_NAME_DESCRIPTION, ruleName.getValue().getDescription());
        assertEquals(RULE_NAME_JSON, ruleName.getValue().getJson());
        assertEquals(RULE_NAME_TEMPLATE, ruleName.getValue().getTemplate());
        assertEquals(RULE_NAME_SQL_STR, ruleName.getValue().getSqlStr());
        assertEquals(RULE_NAME_SQL_PART, ruleName.getValue().getSqlPart());
        verify(repository, times(1)).findById(RULE_NAME_ID);
    }

    @Test
    public void testFindById() {
        // Arrange
        RuleName ruleNameFromRepo = new RuleName();
        ruleNameFromRepo.setId(RULE_NAME_ID);
        when(repository.findById(RULE_NAME_ID)).thenReturn(Optional.of(ruleNameFromRepo));

        // Act
        RuleName ruleName = service.findById(RULE_NAME_ID);

        // Assert
        assertEquals(RULE_NAME_ID, ruleName.getId(), 0);
        verify(repository, times(1)).findById(RULE_NAME_ID);
    }

    @Test
    public void testFindByIdNotFound() {
        // Arrange
        when(repository.findById(RULE_NAME_ID)).thenReturn(Optional.empty());

        // Act
        assertThrows(RuleNameNotFoundException.class, () -> service.findById(RULE_NAME_ID));

        // Assert
        verify(repository, times(1)).findById(RULE_NAME_ID);
    }

    @Test
    public void testDelete() {
        // Act
        service.delete(RULE_NAME_ID);

        // Assert
        verify(repository, times(1)).deleteById(RULE_NAME_ID);
    }
}
