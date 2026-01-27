package ec.edu.epn.petclinic.system;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Unit tests for {@link CrashController} using MockMvc.
 *
 * @author Gianfranco Pupiales
 */
@WebMvcTest(CrashController.class)
@WithMockUser
class CrashControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should trigger exception when accessing /oups")
    void testTriggerException() throws Exception {
        // ACT & ASSERT
        try {
            mockMvc.perform(get("/oups"));
        } catch (ServletException ex) {
            assertThat(ex.getCause()).isInstanceOf(RuntimeException.class);
        }
    }

    @Test
    @DisplayName("Should throw RuntimeException with expected message")
    void testExceptionMessage() throws Exception {
        // ACT & ASSERT
        try {
            mockMvc.perform(get("/oups"));
        } catch (ServletException ex) {
            assertThat(ex.getCause()).isInstanceOf(RuntimeException.class);
            assertThat(ex.getCause().getMessage()).contains("Expected");
        }
    }
}
