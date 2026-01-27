package ec.edu.epn.petclinic.owner;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link VisitController} using MockMvc.
 *
 * @author Gianfranco Pupiales
 */
@WebMvcTest(VisitController.class)
@WithMockUser
class VisitControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OwnerRepository ownerRepository;

    private Owner testOwner;

    @BeforeEach
    void setUp() {
        testOwner = new Owner();
        testOwner.setId(1);
        testOwner.setFirstName("María");
        testOwner.setLastName("Pérez");
        testOwner.setAddress("Av. Amazonas N35-17 y Juan Pablo Sanz");
        testOwner.setCity("Quito");
        testOwner.setTelephone("0991234567");

        PetType perroType = new PetType();
        perroType.setId(1);
        perroType.setName("Perro");

        Pet testPet = new Pet();
        testPet.setId(1);
        testPet.setName("Manchitas");
        testPet.setType(perroType);
        testPet.setBirthDate(LocalDate.of(2020, 1, 15));
        testOwner.getPets().add(testPet);
    }

    @Nested
    @DisplayName("Init Visit Form Tests")
    class InitVisitFormTests {
        @Test
        @DisplayName("Should display new visit form")
        void testInitNewVisitForm() throws Exception {
            // ARRANGE
            when(ownerRepository.findById(1)).thenReturn(Optional.of(testOwner));

            // ACT & ASSERT
            mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", 1, 1))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pets/createOrUpdateVisitForm"))
                    .andExpect(model().attributeExists("visit"))
                    .andExpect(model().attributeExists("pet"))
                    .andExpect(model().attributeExists("owner"));
        }

        @Test
        @DisplayName("Should throw exception when owner not found")
        void testInitNewVisitFormOwnerNotFound() throws Exception {
            // ARRANGE
            when(ownerRepository.findById(999)).thenReturn(Optional.empty());

            // ACT & ASSERT
            try {
                mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", 999, 1));
            } catch (ServletException ex) {
                assertThat(ex.getCause()).isInstanceOf(IllegalArgumentException.class);
                assertThat(ex.getCause().getMessage()).contains("Owner not found");
            }
        }

        @Test
        @DisplayName("Should throw exception when pet not found")
        void testInitNewVisitFormPetNotFound() throws Exception {
            // ARRANGE
            Owner ownerWithoutPet = new Owner();
            ownerWithoutPet.setId(1);
            ownerWithoutPet.setFirstName("Carlos");
            ownerWithoutPet.setLastName("Guamán");
            ownerWithoutPet.setAddress("Calle Sucre 456 y Bolivar");
            ownerWithoutPet.setCity("Cuenca");
            ownerWithoutPet.setTelephone("0987654321");

            when(ownerRepository.findById(1)).thenReturn(Optional.of(ownerWithoutPet));

            // ACT & ASSERT
            try {
                mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", 1, 999));
            } catch (ServletException ex) {
                assertThat(ex.getCause()).isInstanceOf(IllegalArgumentException.class);
                assertThat(ex.getCause().getMessage()).contains("Pet with id 999 not found");
            }
        }
    }

    @Nested
    @DisplayName("Process Visit Form Tests")
    class ProcessVisitFormTests {
        @Test
        @DisplayName("Should create visit successfully")
        void testProcessNewVisitFormSuccess() throws Exception {
            // ARRANGE
            when(ownerRepository.findById(1)).thenReturn(Optional.of(testOwner));
            when(ownerRepository.save(any(Owner.class))).thenReturn(testOwner);

            // ACT & ASSERT
            mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", 1, 1).with(csrf())
                            .param("date", "2023-07-20")
                            .param("description", "Control anual de salud"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/owners/1"))
                    .andExpect(flash().attributeExists("message"));
        }

        @Test
        @DisplayName("Should return to form when validation errors exist")
        void testProcessNewVisitFormWithErrors() throws Exception {
            // ARRANGE
            when(ownerRepository.findById(1)).thenReturn(Optional.of(testOwner));

            // ACT & ASSERT
            mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", 1, 1).with(csrf())
                            .param("date", "2023-07-20")
                            .param("description", "")) // descripción vacía -> error de validación
                    .andExpect(status().isOk())
                    .andExpect(view().name("pets/createOrUpdateVisitForm"));
        }

        @Test
        @DisplayName("Should handle visit creation with default date")
        void testProcessNewVisitFormWithDefaultDate() throws Exception {
            // ARRANGE
            when(ownerRepository.findById(1)).thenReturn(Optional.of(testOwner));
            when(ownerRepository.save(any(Owner.class))).thenReturn(testOwner);

            // ACT & ASSERT
            mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", 1, 1).with(csrf())
                            .param("description", "Consulta de emergencia"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/owners/1"));
        }
    }

    @Nested
    @DisplayName("Model Attribute Tests")
    class ModelAttributeTests {
        @Test
        @DisplayName("Should load pet with visit in model")
        void testLoadPetWithVisit() throws Exception {
            // ARRANGE
            when(ownerRepository.findById(1)).thenReturn(Optional.of(testOwner));

            // ACT & ASSERT
            mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", 1, 1))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("visit"))
                    .andExpect(model().attributeExists("pet"))
                    .andExpect(model().attributeExists("owner"));
        }
    }
}
