package ec.edu.epn.petclinic.system;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Unit tests for {@link WelcomeController} using MockMvc.
 *
 * @author Gianfranco Pupiales
 */
@WebMvcTest(WelcomeController.class)
@WithMockUser
class WelcomeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return welcome view")
    void testWelcome() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("welcome"));
    }

    @Test
    @DisplayName("Should return correct view name")
    void testWelcomeViewName() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("welcome"));
    }

    @Test
    @DisplayName("Should handle request without parameters")
    void testWelcomeWithoutParameters() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should handle request with query parameters gracefully")
    void testWelcomeWithQueryParameters() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(get("/").param("someParam", "someValue"))
                .andExpect(status().isOk())
                .andExpect(view().name("welcome"));
    }
}
