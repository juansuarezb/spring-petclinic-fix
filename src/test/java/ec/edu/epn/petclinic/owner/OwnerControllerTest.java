package ec.edu.epn.petclinic.owner;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link OwnerController} using MockMvc.
 *
 * @author Dome
 */
@WebMvcTest(OwnerController.class)
@WithMockUser
class OwnerControllerTest {

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
	}

	@Nested
	@DisplayName("Create Owner Tests")
	class CreateOwnerTests {

		@Test
		@DisplayName("Should display creation form")
		void testInitCreationForm() throws Exception {
			// ARRANGE - nothing needed

			// ACT & ASSERT
			mockMvc.perform(get("/owners/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("owners/createOrUpdateOwnerForm"))
				.andExpect(model().attributeExists("owner"));
		}

		@Test
		@DisplayName("Should create owner successfully")
		void testProcessCreationFormSuccess() throws Exception {
			// ARRANGE
			when(ownerRepository.save(any(Owner.class))).thenAnswer(invocation -> {
				Owner owner = invocation.getArgument(0);
				owner.setId(1);
				return owner;
			});

			// ACT & ASSERT
			mockMvc.perform(post("/owners/new").with(csrf())
					.param("firstName", "Carlos")
					.param("lastName", "Guamán")
					.param("address", "Calle Sucre 456 y Bolivar")
					.param("city", "Cuenca")
					.param("telephone", "0987654321"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/owners/1"))
				.andExpect(flash().attributeExists("message"));
		}

		@Test
		@DisplayName("Should return to form when validation errors exist")
		void testProcessCreationFormWithErrors() throws Exception {
			// ARRANGE - empty firstName will cause validation error

			// ACT & ASSERT
			mockMvc.perform(post("/owners/new").with(csrf())
					.param("firstName", "")
					.param("lastName", "")
					.param("address", "")
					.param("city", "")
					.param("telephone", "invalid"))
				.andExpect(status().isOk())
				.andExpect(view().name("owners/createOrUpdateOwnerForm"));
		}
	}

	@Nested
	@DisplayName("Find Owner Tests")
	class FindOwnerTests {

		@Test
		@DisplayName("Should display find form")
		void testInitFindForm() throws Exception {
			// ARRANGE - nothing needed

			// ACT & ASSERT
			mockMvc.perform(get("/owners/find"))
				.andExpect(status().isOk())
				.andExpect(view().name("owners/findOwners"));
		}

		@Test
		@DisplayName("Should redirect to owner when only one result found")
		void testProcessFindFormOneResult() throws Exception {
			// ARRANGE
			when(ownerRepository.findByLastNameStartingWith(anyString(), any(Pageable.class)))
				.thenReturn(new PageImpl<>(List.of(testOwner)));

			// ACT & ASSERT
			mockMvc.perform(get("/owners")
					.param("lastName", "Pérez"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/owners/1"));
		}

		@Test
		@DisplayName("Should show list when multiple results found")
		void testProcessFindFormMultipleResults() throws Exception {
			// ARRANGE
			Owner owner2 = new Owner();
			owner2.setId(2);
			owner2.setFirstName("Rosa");
			owner2.setLastName("Pérez");
			owner2.setAddress("Av. 6 de Diciembre N45-89");
			owner2.setCity("Quito");
			owner2.setTelephone("0998765432");

			when(ownerRepository.findByLastNameStartingWith(anyString(), any(Pageable.class)))
				.thenReturn(new PageImpl<>(List.of(testOwner, owner2)));

			// ACT & ASSERT
			mockMvc.perform(get("/owners")
					.param("lastName", "Pérez"))
				.andExpect(status().isOk())
				.andExpect(view().name("owners/ownersList"))
				.andExpect(model().attributeExists("listOwners"));
		}

		@Test
		@DisplayName("Should return to find form when no results found")
		void testProcessFindFormNoResults() throws Exception {
			// ARRANGE
			when(ownerRepository.findByLastNameStartingWith(anyString(), any(Pageable.class)))
				.thenReturn(new PageImpl<>(Collections.emptyList()));

			// ACT & ASSERT
			mockMvc.perform(get("/owners")
					.param("lastName", "Desconocido"))
				.andExpect(status().isOk())
				.andExpect(view().name("owners/findOwners"));
		}

		@Test
		@DisplayName("Should find all owners when lastName is empty")
		void testProcessFindFormEmptyLastName() throws Exception {
			// ARRANGE
			when(ownerRepository.findByLastNameStartingWith(anyString(), any(Pageable.class)))
				.thenReturn(new PageImpl<>(List.of(testOwner)));

			// ACT & ASSERT
			mockMvc.perform(get("/owners"))
				.andExpect(status().is3xxRedirection());
		}
	}

	@Nested
	@DisplayName("Update Owner Tests")
	class UpdateOwnerTests {

		@Test
		@DisplayName("Should display update form")
		void testInitUpdateOwnerForm() throws Exception {
			// ARRANGE
			when(ownerRepository.findById(1)).thenReturn(Optional.of(testOwner));

			// ACT & ASSERT
			mockMvc.perform(get("/owners/{ownerId}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("owners/createOrUpdateOwnerForm"))
				.andExpect(model().attributeExists("owner"));
		}

		@Test
		@DisplayName("Should update owner successfully")
		void testProcessUpdateOwnerFormSuccess() throws Exception {
			// ARRANGE
			when(ownerRepository.findById(1)).thenReturn(Optional.of(testOwner));
			when(ownerRepository.save(any(Owner.class))).thenReturn(testOwner);

			// ACT & ASSERT
			mockMvc.perform(post("/owners/{ownerId}/edit", 1).with(csrf())
					.param("firstName", "María")
					.param("lastName", "Pérez")
					.param("address", "Calle Venezuela 123 y Chile")
					.param("city", "Guayaquil")
					.param("telephone", "0412345678"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/owners/1"))
				.andExpect(flash().attributeExists("message"));
		}

		@Test
		@DisplayName("Should return to form when validation errors exist on update")
		void testProcessUpdateOwnerFormWithErrors() throws Exception {
			// ARRANGE
			when(ownerRepository.findById(1)).thenReturn(Optional.of(testOwner));

			// ACT & ASSERT
			mockMvc.perform(post("/owners/{ownerId}/edit", 1).with(csrf())
					.param("firstName", "")
					.param("lastName", "")
					.param("address", "")
					.param("city", "")
					.param("telephone", "invalid"))
				.andExpect(status().isOk())
				.andExpect(view().name("owners/createOrUpdateOwnerForm"));
		}
	}

	@Nested
	@DisplayName("Show Owner Tests")
	class ShowOwnerTests {

		@Test
		@DisplayName("Should display owner details")
		void testShowOwner() throws Exception {
			// ARRANGE
			when(ownerRepository.findById(1)).thenReturn(Optional.of(testOwner));

			// ACT & ASSERT
			mockMvc.perform(get("/owners/{ownerId}", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("owners/ownerDetails"))
				.andExpect(model().attributeExists("owner"));
		}

		@Test
		@DisplayName("Should throw exception when owner not found")
		void testShowOwnerNotFound() throws Exception {
			// ARRANGE
			when(ownerRepository.findById(999)).thenReturn(Optional.empty());

			// ACT & ASSERT
			try {
				mockMvc.perform(get("/owners/{ownerId}", 999));
			}
			catch (ServletException ex) {
				assertThat(ex.getCause()).isInstanceOf(IllegalArgumentException.class);
				assertThat(ex.getCause().getMessage()).contains("Owner not found");
			}
		}
	}

}
