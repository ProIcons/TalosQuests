package gr.devian.talosquests.backend.Controllers;

import gr.devian.talosquests.backend.AbstractUserControllerTest;
import gr.devian.talosquests.backend.Models.AuthRegisterModel;
import gr.devian.talosquests.backend.Models.User;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by Nikolas on 5/12/2016.
 */
@Transactional
public class UserControllerTests extends AbstractUserControllerTest {

    @Test
    public void GetUnauthorizedOnNoTokenSpecified() throws Exception {

        mockMvc.perform(get("/User"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void GetUnauthorizedOnWrongTokenSpecified() throws Exception {

        mockMvc.perform(get("/User")
                .param("token","invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void GetAuthorizedOnCorrectTokenSpecified() throws Exception {

        mockMvc.perform(get("/User")
                .param("token",session.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void DeleteUnauthorizedOnNoTokenAndNoPasswordSpecified() throws Exception {

        mockMvc.perform(delete("/User"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void DeleteUnauthorizedOnNoPasswordSpecified() throws Exception {

        mockMvc.perform(delete("/User")
                .param("token","test"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }
    @Test
    public void DeleteUnAuthorizedOnNoTokenSpecified() throws Exception {

        mockMvc.perform(delete("/User")
                .param("password","test"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }
    @Test
    public void DeleteUnauthorizedOnIncorrectTokenSpecified() throws Exception {

        mockMvc.perform(delete("/User")
                .param("token","test")
                .param("password","test"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }
    @Test
    public void DeleteUnauthorizedOnIncorrectPasswordSpecified() throws Exception {

        mockMvc.perform(delete("/User")
                .param("token",session.getToken())
                .param("password","bla"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void DeleteOkOnCorrectTokenAndPasswordSpecified() throws Exception {

        mockMvc.perform(delete("/User")
                .param("password","Test1*2^3%1#2@3!")
                .param("token",session.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        User _user = userService.getUserByUsername(user.getUserName());
        assertNull("Failure - Expected Null", _user);

    }


    @Test
    public void PutUnsupportedMediaTypeOnNoTokenAndNoPasswordAndNoModelSpecified() throws Exception {

        mockMvc.perform(put("/User"))
                .andExpect(status().isUnsupportedMediaType())
                .andReturn();

    }

    @Test
    public void PutUnauthorizedOnNoPasswordSpecified() throws Exception {

        mockMvc.perform(put("/User")
                .param("token","test")
                .content(mapToJson(new AuthRegisterModel()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }
    @Test
    public void PutUnauthorizedOnNoTokenSpecified() throws Exception {

        mockMvc.perform(put("/User")
                .param("password","Test1*2^3%1#2@3!")
                .content(mapToJson(new AuthRegisterModel()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }
    @Test
    public void PutUnsupportedMediaTypeOnNoModelSpecified() throws Exception {

        mockMvc.perform(put("/User")
                .param("password","Test1*2^3%1#2@3!")
                .param("token","test"))
                .andExpect(status().isUnsupportedMediaType())
                .andReturn();

    }

    @Test
    public void PutUnauthorizedOnIncorrectTokenSpecified() throws Exception {

        mockMvc.perform(put("/User")
                .param("token","test")
                .param("password","Test1*2^3%1#2@3!")
                .content(mapToJson(new AuthRegisterModel()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }
    @Test
    public void PutUnauthorizedOnIncorrectPasswordSpecified() throws Exception {

        mockMvc.perform(put("/User")
                .param("token",session.getToken())
                .param("password","bla")
                .content(mapToJson(new AuthRegisterModel()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

    }

    @Test
    public void PutOkOnCorrectTokenAndPasswordSpecified() throws Exception {

        mockMvc.perform(put("/User")
                .param("password","Test1*2^3%1#2@3!")
                .param("token",session.getToken())
                .content(mapToJson(nonExistentUserModel))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        User _user = userService.getUserByUsername(user.getUserName());

    }
}