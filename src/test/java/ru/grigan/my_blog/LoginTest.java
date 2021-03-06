package ru.grigan.my_blog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.grigan.my_blog.controller.GreetingController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {
    @Autowired
    private GreetingController controller;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void whenQuesGetHelloPageThenPageContainsExpectedStringAndNameEqualsQuest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("This My Simple Blog application!")))
                .andExpect(content().string(containsString("Quest")));
    }

    @Test
    public void whenQuestGetMainPageThenAccessWillDenied() throws Exception {
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void whenUserEnterCorrectLoginThenShowUsernameIntoNavbar() throws Exception {
        this.mockMvc.perform(formLogin().user("user").password("1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void whenUserEnterInCorrectLoginThenStatus403() throws Exception {
        this.mockMvc.perform(post("/login").param("name", "fakeBob"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
