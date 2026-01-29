package ec.edu.epn.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link Owner}.
 *
 * @author Dome
 */
class OwnerTest {

	private Owner owner;

	@BeforeEach
	void setUp() {
		owner = new Owner();
	}

	@Nested
	@DisplayName("Getters and Setters Tests")
	class GettersAndSettersTests {

		@Test
		@DisplayName("Should set and get address correctly")
		void testSetAndGetAddress() {
			// ARRANGE
			String address = "Av. 10 de Agosto N35-12 y Murgeon";

			// ACT
			owner.setAddress(address);

			// ASSERT
			assertThat(owner.getAddress()).isEqualTo(address);
		}

		@Test
		@DisplayName("Should set and get city correctly")
		void testSetAndGetCity() {
			// ARRANGE
			String city = "Quito";

			// ACT
			owner.setCity(city);

			// ASSERT
			assertThat(owner.getCity()).isEqualTo(city);
		}

		@Test
		@DisplayName("Should set and get telephone correctly")
		void testSetAndGetTelephone() {
			// ARRANGE
			String telephone = "0991234567";

			// ACT
			owner.setTelephone(telephone);

			// ASSERT
			assertThat(owner.getTelephone()).isEqualTo(telephone);
		}

		@Test
		@DisplayName("Should set and get firstName correctly (inherited from Person)")
		void testSetAndGetFirstName() {
			// ARRANGE
			String firstName = "María";

			// ACT
			owner.setFirstName(firstName);

			// ASSERT
			assertThat(owner.getFirstName()).isEqualTo(firstName);
		}

		@Test
		@DisplayName("Should set and get lastName correctly (inherited from Person)")
		void testSetAndGetLastName() {
			// ARRANGE
			String lastName = "Pérez";

			// ACT
			owner.setLastName(lastName);

			// ASSERT
			assertThat(owner.getLastName()).isEqualTo(lastName);
		}
	}

	@Nested
	@DisplayName("Pet Management Tests")
	class PetManagementTests {

		@Test
		@DisplayName("Should return empty list when no pets added")
		void testGetPetsReturnsEmptyList() {
			// ARRANGE - owner already created

			// ACT
			var pets = owner.getPets();

			// ASSERT
			assertThat(pets).isNotNull().isEmpty();
		}

		@Test
		@DisplayName("Should add new pet to owner")
		void testAddNewPet() {
			// ARRANGE
			Pet pet = new Pet();
			pet.setName("Manchitas");

			// ACT
			owner.addPet(pet);

			// ASSERT
			assertThat(owner.getPets()).hasSize(1);
			assertThat(owner.getPets()).contains(pet);
		}

		@Test
		@DisplayName("Should not add pet if pet is not new")
		void testAddPetNotNew() {
			// ARRANGE
			Pet pet = new Pet();
			pet.setId(1);
			pet.setName("Firulais");

			// ACT
			owner.addPet(pet);

			// ASSERT
			assertThat(owner.getPets()).isEmpty();
		}

		@Test
		@DisplayName("Should find pet by name")
		void testGetPetByName() {
			// ARRANGE
			Pet pet = new Pet();
			pet.setName("Diablo");
			owner.addPet(pet);

			// ACT
			Pet foundPet = owner.getPet("Diablo");

			// ASSERT
			assertThat(foundPet).isNotNull();
			assertThat(foundPet.getName()).isEqualTo("Diablo");
		}

		@Test
		@DisplayName("Should find pet by name case insensitive")
		void testGetPetByNameCaseInsensitive() {
			// ARRANGE
			Pet pet = new Pet();
			pet.setName("Pelusa");
			owner.addPet(pet);

			// ACT
			Pet foundPet = owner.getPet("PELUSA");

			// ASSERT
			assertThat(foundPet).isNotNull();
			assertThat(foundPet.getName()).isEqualTo("Pelusa");
		}

		@Test
		@DisplayName("Should return null when pet not found by name")
		void testGetPetByNameNotFound() {
			// ARRANGE
			Pet pet = new Pet();
			pet.setName("Barbu");
			owner.addPet(pet);

			// ACT
			Pet foundPet = owner.getPet("Negrito");

			// ASSERT
			assertThat(foundPet).isNull();
		}

		@Test
		@DisplayName("Should find pet by id")
		void testGetPetById() {
			// ARRANGE
			Pet pet = new Pet();
			pet.setId(1);
			pet.setName("Cuchito");
			owner.getPets().add(pet);

			// ACT
			Pet foundPet = owner.getPet(1);

			// ASSERT
			assertThat(foundPet).isNotNull();
			assertThat(foundPet.getId()).isEqualTo(1);
		}

		@Test
		@DisplayName("Should return null when pet not found by id")
		void testGetPetByIdNotFound() {
			// ARRANGE
			Pet pet = new Pet();
			pet.setId(1);
			pet.setName("Toby");
			owner.getPets().add(pet);

			// ACT
			Pet foundPet = owner.getPet(999);

			// ASSERT
			assertThat(foundPet).isNull();
		}

		@Test
		@DisplayName("Should skip new pets when finding by id")
		void testGetPetByIdSkipsNewPets() {
			// ARRANGE
			Pet newPet = new Pet();
			newPet.setName("Princesa");
			owner.addPet(newPet);

			// ACT
			Pet foundPet = owner.getPet(1);

			// ASSERT
			assertThat(foundPet).isNull();
		}

		@Test
		@DisplayName("Should find pet by name ignoring new pets when flag is true")
		void testGetPetByNameIgnoreNew() {
			// ARRANGE
			Pet newPet = new Pet();
			newPet.setName("Copito");
			owner.addPet(newPet);

			// ACT
			Pet foundPet = owner.getPet("Copito", true);

			// ASSERT
			assertThat(foundPet).isNull();
		}

		@Test
		@DisplayName("Should find pet by name including new pets when flag is false")
		void testGetPetByNameIncludeNew() {
			// ARRANGE
			Pet newPet = new Pet();
			newPet.setName("Negra");
			owner.addPet(newPet);

			// ACT
			Pet foundPet = owner.getPet("Negra", false);

			// ASSERT
			assertThat(foundPet).isNotNull();
			assertThat(foundPet.getName()).isEqualTo("Negra");
		}
	}

	@Nested
	@DisplayName("Visit Management Tests")
	class VisitManagementTests {

		@Test
		@DisplayName("Should add visit to pet")
		void testAddVisit() {
			// ARRANGE
			Pet pet = new Pet();
			pet.setId(1);
			pet.setName("Lobo");
			owner.getPets().add(pet);

			Visit visit = new Visit();
			visit.setDescription("Vacunacion antirabica");

			// ACT
			owner.addVisit(1, visit);

			// ASSERT
			assertThat(pet.getVisits()).hasSize(1);
			assertThat(pet.getVisits().iterator().next().getDescription()).isEqualTo("Vacunacion antirabica");
		}

		@Test
		@DisplayName("Should throw exception when petId is null")
		void testAddVisitNullPetId() {
			// ARRANGE
			Visit visit = new Visit();

			// ACT & ASSERT
			assertThatThrownBy(() -> owner.addVisit(null, visit))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Pet identifier must not be null");
		}

		@Test
		@DisplayName("Should throw exception when visit is null")
		void testAddVisitNullVisit() {
			// ARRANGE - nothing needed

			// ACT & ASSERT
			assertThatThrownBy(() -> owner.addVisit(1, null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Visit must not be null");
		}

		@Test
		@DisplayName("Should throw exception when pet not found")
		void testAddVisitPetNotFound() {
			// ARRANGE
			Visit visit = new Visit();

			// ACT & ASSERT
			assertThatThrownBy(() -> owner.addVisit(999, visit))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Invalid Pet identifier");
		}
	}

	@Nested
	@DisplayName("ToString Tests")
	class ToStringTests {

		@Test
		@DisplayName("Should return correct string representation")
		void testToString() {
			// ARRANGE
			owner.setId(1);
			owner.setFirstName("Carlos");
			owner.setLastName("Morocho");
			owner.setAddress("Calle Sucre 456 y Bolivar");
			owner.setCity("Guayaquil");
			owner.setTelephone("0984567890");

			// ACT
			String result = owner.toString();

			// ASSERT
			assertThat(result).contains("id = 1");
			assertThat(result).contains("lastName = 'Morocho'");
			assertThat(result).contains("firstName = 'Carlos'");
			assertThat(result).contains("address = 'Calle Sucre 456 y Bolivar'");
			assertThat(result).contains("city = 'Guayaquil'");
			assertThat(result).contains("telephone = '0984567890'");
		}

		@Test
		@DisplayName("Should handle new entity in toString")
		void testToStringNewEntity() {
			// ARRANGE - owner with no id set

			// ACT
			String result = owner.toString();

			// ASSERT
			assertThat(result).contains("new = true");
		}
	}

}
