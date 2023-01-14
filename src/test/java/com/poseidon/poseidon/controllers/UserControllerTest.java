package com.poseidon.poseidon.controllers;

import com.poseidon.poseidon.domain.User;
import com.poseidon.poseidon.domain.UserRole;
import com.poseidon.poseidon.exceptions.NewUserWithEmptyPasswordException;
import com.poseidon.poseidon.exceptions.UserNotFoundException;
import com.poseidon.poseidon.services.IUserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username="admin",roles={"ADMIN"})
public class UserControllerTest {

    final int USER_ID = 1;
    final String USER_FULL_NAME = "full name";
    final String USER_NAME = "user name";
    final String USER_PASSWORD = "Password!0";
    final UserRole USER_ROLE = UserRole.User;

    @MockBean
    IUserService service;

    @Autowired
    public MockMvc mockMvc;


    @Test
    public void testHome() throws Exception {
        mockMvc.perform(get("/user/list")).andDo(print()).andExpect(status().isOk()).andExpect(view().name("user/list")).andExpect(content().string(containsString("User List")));
        verify(service, Mockito.times(1)).findAll();
    }

    @Test
    public void testAddUserForm() throws Exception {
        mockMvc.perform(get("/user/add")).andDo(print()).andExpect(status().isOk()).andExpect(view().name("user/add")).andExpect(content().string(containsString("Add New User")));
    }

    @Test
    public void testValidate() throws Exception {
        String content = "fullname=" + USER_FULL_NAME + "&username=" + USER_NAME + "&password=" + USER_PASSWORD + "&role=" + USER_ROLE;
        mockMvc.perform(post("/user/validate").contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isFound()).andExpect(view().name("redirect:/user/list"));
        ArgumentCaptor<User> user = ArgumentCaptor.forClass(User.class);
        verify(service, Mockito.times(1)).save(user.capture());
        assertEquals(USER_FULL_NAME, user.getValue().getFullname());
        assertEquals(USER_NAME, user.getValue().getUsername());
        assertEquals(USER_PASSWORD, user.getValue().getPassword());
        assertEquals(USER_ROLE, user.getValue().getRole());
    }

    @Test
    public void testValidatePasswordException() throws Exception {
        String content = "fullname=" + USER_FULL_NAME + "&username=" + USER_NAME + "&password=" + USER_PASSWORD + "&role=" + USER_ROLE;
        doThrow(new NewUserWithEmptyPasswordException()).when(service).save(any(User.class));
        mockMvc.perform(post("/user/validate").contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("user/add"));
        verify(service, Mockito.times(1)).save(any(User.class));
    }

    @Test
    public void testValidateFormError() throws Exception {
        String content = "fullname=" + USER_FULL_NAME + "&username=" + USER_NAME + "&password=";
        mockMvc.perform(post("/user/validate").contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("user/add"));
        verify(service, Mockito.never()).save(any(User.class));
    }

    @Test
    public void testShowUpdateForm() throws Exception {
        when(service.getUserById(USER_ID)).thenReturn(new User());
        mockMvc.perform(get("/user/update/" + USER_ID)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("user/update")).andExpect(content().string(containsString("Update User")));
        verify(service, Mockito.times(1)).getUserById(USER_ID);
    }

    @Test
    public void testUpdateUser() throws Exception {
        String content = "fullname=" + USER_FULL_NAME + "&username=" + USER_NAME + "&password=" + USER_PASSWORD + "&role=" + USER_ROLE;
        mockMvc.perform(post("/user/update/" + USER_ID).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isFound()).andExpect(view().name("redirect:/user/list"));
        ArgumentCaptor<User> user = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Integer> id = ArgumentCaptor.forClass(Integer.class);
        verify(service, Mockito.times(1)).update(user.capture(), id.capture());
        assertEquals(USER_ID, id.getValue(), 0);
        assertEquals(USER_FULL_NAME, user.getValue().getFullname());
        assertEquals(USER_NAME, user.getValue().getUsername());
        assertEquals(USER_PASSWORD, user.getValue().getPassword());
        assertEquals(USER_ROLE, user.getValue().getRole());
    }

    @Test
    public void testUpdateUserFormError() throws Exception {
        String content = "fullname=" + USER_FULL_NAME + "&username=" + USER_NAME + "&password=";
        mockMvc.perform(post("/user/update/" + USER_ID).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("user/update"));
        verify(service, Mockito.never()).update(any(User.class), anyInt());
    }

    @Test
    public void testUpdateUserNotFound() throws Exception {
        String content = "fullname=" + USER_FULL_NAME + "&username=" + USER_NAME + "&password=" + USER_PASSWORD + "&role=" + USER_ROLE ;
        doThrow(new UserNotFoundException()).when(service).update(any(User.class), anyInt());
        mockMvc.perform(post("/user/update/" + USER_ID).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("user/update"));
        ArgumentCaptor<User> user = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Integer> id = ArgumentCaptor.forClass(Integer.class);
        verify(service, Mockito.times(1)).update(user.capture(), id.capture());
        assertEquals(USER_ID, id.getValue(), 0);
        assertEquals(USER_FULL_NAME, user.getValue().getFullname());
        assertEquals(USER_NAME, user.getValue().getUsername());
        assertEquals(USER_PASSWORD, user.getValue().getPassword());
        assertEquals(USER_ROLE, user.getValue().getRole());
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(get("/user/delete/" + USER_ID)).andDo(print()).andExpect(status().isFound()).andExpect(view().name("redirect:/user/list"));
        verify(service, Mockito.times(1)).delete(USER_ID);
    }

}
