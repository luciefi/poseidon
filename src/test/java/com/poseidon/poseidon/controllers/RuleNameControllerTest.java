package com.poseidon.poseidon.controllers;

import com.poseidon.poseidon.domain.RuleName;
import com.poseidon.poseidon.exceptions.RuleNameNotFoundException;
import com.poseidon.poseidon.services.IRuleNameService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RuleNameControllerTest {

    final int RULE_NAME_ID = 1;
    final String RULE_NAME_NAME = "name";
    final String RULE_NAME_DESCRIPTION = "description";
    final String RULE_NAME_JSON = "json";
    final String RULE_NAME_TEMPLATE = "template";
    final String RULE_NAME_SQL_STR = "sql str";
    final String RULE_NAME_SQL_PART = "sql part";


    @MockBean
    IRuleNameService service;

    @Autowired
    public MockMvc mockMvc;


    @Test
    void home() throws Exception {
        mockMvc.perform(get("/ruleName/list")).andDo(print()).andExpect(status().isOk()).andExpect(view().name("ruleName/list")).andExpect(content().string(containsString("Rule List")));
        verify(service, Mockito.times(1)).findAll();
    }

    @Test
    void addRuleNameForm() throws Exception {
        mockMvc.perform(get("/ruleName/add")).andDo(print()).andExpect(status().isOk()).andExpect(view().name("ruleName/add")).andExpect(content().string(containsString("Add New Rule")));
    }

    @Test
    void validate() throws Exception {
        String content = "name=" + RULE_NAME_NAME + "&description=" + RULE_NAME_DESCRIPTION + "&json=" + RULE_NAME_JSON + "&template=" + RULE_NAME_TEMPLATE + "&sqlStr=" + RULE_NAME_SQL_STR + "&sqlPart=" + RULE_NAME_SQL_PART;
        mockMvc.perform(post("/ruleName/validate").contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isFound()).andExpect(view().name("redirect:/ruleName/list"));
        ArgumentCaptor<RuleName> ruleName = ArgumentCaptor.forClass(RuleName.class);
        verify(service, Mockito.times(1)).save(ruleName.capture());
        Assertions.assertEquals(RULE_NAME_NAME, ruleName.getValue().getName());
        Assertions.assertEquals(RULE_NAME_DESCRIPTION, ruleName.getValue().getDescription());
        Assertions.assertEquals(RULE_NAME_JSON, ruleName.getValue().getJson());
        Assertions.assertEquals(RULE_NAME_TEMPLATE, ruleName.getValue().getTemplate());
        Assertions.assertEquals(RULE_NAME_SQL_STR, ruleName.getValue().getSqlStr());
        Assertions.assertEquals(RULE_NAME_SQL_PART, ruleName.getValue().getSqlPart());
    }

    @Test
    public void testValidateFormError() throws Exception {
        String content = "name=" + RULE_NAME_NAME + "&description=" + RULE_NAME_DESCRIPTION + "&json=" + RULE_NAME_JSON + "&template=" + RULE_NAME_TEMPLATE + "&sqlStr=" + RULE_NAME_SQL_STR + "&sqlPart=";
        mockMvc.perform(post("/ruleName/validate").contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("ruleName/add"));
        verify(service, Mockito.never()).save(any(RuleName.class));
    }

    @Test
    public void testShowUpdateForm() throws Exception {
        when(service.findById(RULE_NAME_ID)).thenReturn(new RuleName());
        mockMvc.perform(get("/ruleName/update/" + RULE_NAME_ID)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("ruleName/update")).andExpect(content().string(containsString("Update Rule")));
        verify(service, Mockito.times(1)).findById(RULE_NAME_ID);
    }

    @Test
    public void updateRuleName() throws Exception {
        String content = "name=" + RULE_NAME_NAME + "&description=" + RULE_NAME_DESCRIPTION + "&json=" + RULE_NAME_JSON + "&template=" + RULE_NAME_TEMPLATE + "&sqlStr=" + RULE_NAME_SQL_STR + "&sqlPart=" + RULE_NAME_SQL_PART;
        mockMvc.perform(post("/ruleName/update/" + RULE_NAME_ID).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isFound()).andExpect(view().name("redirect:/ruleName/list"));
        ArgumentCaptor<RuleName> ruleName = ArgumentCaptor.forClass(RuleName.class);
        ArgumentCaptor<Integer> id = ArgumentCaptor.forClass(Integer.class);
        verify(service, Mockito.times(1)).update(ruleName.capture(), id.capture());
        Assertions.assertEquals(RULE_NAME_ID, id.getValue(), 0);
        Assertions.assertEquals(RULE_NAME_NAME, ruleName.getValue().getName());
        Assertions.assertEquals(RULE_NAME_DESCRIPTION, ruleName.getValue().getDescription());
        Assertions.assertEquals(RULE_NAME_JSON, ruleName.getValue().getJson());
        Assertions.assertEquals(RULE_NAME_TEMPLATE, ruleName.getValue().getTemplate());
        Assertions.assertEquals(RULE_NAME_SQL_STR, ruleName.getValue().getSqlStr());
        Assertions.assertEquals(RULE_NAME_SQL_PART, ruleName.getValue().getSqlPart());
    }

    @Test
    public void updateRuleNameFormError() throws Exception {
        String content = "name=" + RULE_NAME_NAME + "&description=" + RULE_NAME_DESCRIPTION + "&json=" + RULE_NAME_JSON + "&template=" + RULE_NAME_TEMPLATE + "&sqlStr=" + RULE_NAME_SQL_STR + "&sqlPart=";
        mockMvc.perform(post("/ruleName/update/" + RULE_NAME_ID).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("ruleName/update"));
        verify(service, Mockito.never()).update(any(RuleName.class), anyInt());
    }

    @Test
    public void updateRuleNameNotFound() throws Exception {
        String content = "name=" + RULE_NAME_NAME + "&description=" + RULE_NAME_DESCRIPTION + "&json=" + RULE_NAME_JSON + "&template=" + RULE_NAME_TEMPLATE + "&sqlStr=" + RULE_NAME_SQL_STR + "&sqlPart=" + RULE_NAME_SQL_PART;
        doThrow(new RuleNameNotFoundException()).when(service).update(any(RuleName.class), anyInt());
        mockMvc.perform(post("/ruleName/update/" + RULE_NAME_ID).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("ruleName/update"));
        ArgumentCaptor<RuleName> ruleName = ArgumentCaptor.forClass(RuleName.class);
        ArgumentCaptor<Integer> id = ArgumentCaptor.forClass(Integer.class);
        verify(service, Mockito.times(1)).update(ruleName.capture(), id.capture());
        Assertions.assertEquals(RULE_NAME_ID, id.getValue(), 0);
        Assertions.assertEquals(RULE_NAME_NAME, ruleName.getValue().getName());
        Assertions.assertEquals(RULE_NAME_DESCRIPTION, ruleName.getValue().getDescription());
        Assertions.assertEquals(RULE_NAME_JSON, ruleName.getValue().getJson());
        Assertions.assertEquals(RULE_NAME_TEMPLATE, ruleName.getValue().getTemplate());
        Assertions.assertEquals(RULE_NAME_SQL_STR, ruleName.getValue().getSqlStr());
        Assertions.assertEquals(RULE_NAME_SQL_PART, ruleName.getValue().getSqlPart());
    }

    @Test
    void deleteRuleName() throws Exception {
        mockMvc.perform(get("/ruleName/delete/" + RULE_NAME_ID)).andDo(print()).andExpect(status().isFound()).andExpect(view().name("redirect:/ruleName/list"));
        verify(service, Mockito.times(1)).delete(RULE_NAME_ID);
    }

}
