package ec.edu.epn.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Pet}.
 *
 * @author Ronny
 */
class PetTest {

	private Pet pet;

	@BeforeEach
	void setUp() {
		pet = new Pet();
	}

	@Nested
	@DisplayName("Basic Property Tests")
	class BasicPropertyTests {

		@Test
		@DisplayName("Should set and get birth date correctly")
		void testSetAndGetBirthDate() {
			// ARRANGE
			LocalDate birthDate = LocalDate.of(2020, 1, 15);

			// ACT
			pet.setBirthDate(birthDate);

			// ASSERT
			assertThat(pet.getBirthDate()).isEqualTo(birthDate);
		}

		@Test
		@DisplayName("Should set and get type correctly")
		void testSetAndGetType() {
			// ARRANGE
			PetType type = new PetType();
			type.setName("Perro");

			// ACT
			pet.setType(type);

			// ASSERT
			assertThat(pet.getType()).isEqualTo(type);
			assertThat(pet.getType().getName()).isEqualTo("Perro");
		}

		@Test
		@DisplayName("Should return null when birth date is not set")
		void testGetBirthDateReturnsNullWhenNotSet() {
			// ARRANGE - pet already created without birth date

			// ACT
			LocalDate birthDate = pet.getBirthDate();

			// ASSERT
			assertThat(birthDate).isNull();
		}

		@Test
		@DisplayName("Should return null when type is not set")
		void testGetTypeReturnsNullWhenNotSet() {
			// ARRANGE - pet already created without type

			// ACT
			PetType type = pet.getType();

			// ASSERT
			assertThat(type).isNull();
		}
	}

	@Nested
	@DisplayName("Inherited Property Tests")
	class InheritedPropertyTests {

		@Test
		@DisplayName("Should set and get name correctly (inherited from NamedEntity)")
		void testSetAndGetName() {
			// ARRANGE
			String name = "Manchitas";

			// ACT
			pet.setName(name);

			// ASSERT
			assertThat(pet.getName()).isEqualTo(name);
		}

		@Test
		@DisplayName("Should set and get id correctly (inherited from BaseEntity)")
		void testSetAndGetId() {
			// ARRANGE
			Integer id = 1;

			// ACT
			pet.setId(id);

			// ASSERT
			assertThat(pet.getId()).isEqualTo(id);
		}

		@Test
		@DisplayName("Should return isNew true when id is null")
		void testIsNewTrue() {
			// ARRANGE - pet without id

			// ACT
			boolean isNew = pet.isNew();

			// ASSERT
			assertThat(isNew).isTrue();
		}

		@Test
		@DisplayName("Should return isNew false when id is set")
		void testIsNewFalse() {
			// ARRANGE
			pet.setId(1);

			// ACT
			boolean isNew = pet.isNew();

			// ASSERT
			assertThat(isNew).isFalse();
		}
	}

	@Nested
	@DisplayName("Visit Management Tests")
	class VisitManagementTests {

		@Test
		@DisplayName("Should return empty collection when no visits added")
		void testGetVisitsReturnsEmptyCollection() {
			// ARRANGE - pet already created

			// ACT
			var visits = pet.getVisits();

			// ASSERT
			assertThat(visits).isNotNull().isEmpty();
		}

		@Test
		@DisplayName("Should add visit correctly")
		void testAddVisit() {
			// ARRANGE
			Visit visit = new Visit();
			visit.setDescription("Control anual de salud");

			// ACT
			pet.addVisit(visit);

			// ASSERT
			assertThat(pet.getVisits()).hasSize(1);
			assertThat(pet.getVisits().iterator().next().getDescription()).isEqualTo("Control anual de salud");
		}

		@Test
		@DisplayName("Should add multiple visits")
		void testAddMultipleVisits() {
			// ARRANGE
			Visit visit1 = new Visit();
			visit1.setDescription("Vacunacion antirabica");
			Visit visit2 = new Visit();
			visit2.setDescription("Desparasitacion");

			// ACT
			pet.addVisit(visit1);
			pet.addVisit(visit2);

			// ASSERT
			assertThat(pet.getVisits()).hasSize(2);
		}

		@Test
		@DisplayName("Should maintain visit order by date")
		void testVisitsOrderedByDate() {
			// ARRANGE
			Visit olderVisit = new Visit();
			olderVisit.setDate(LocalDate.of(2020, 1, 1));
			olderVisit.setDescription("Primera consulta");

			Visit newerVisit = new Visit();
			newerVisit.setDate(LocalDate.of(2021, 1, 1));
			newerVisit.setDescription("Consulta de seguimiento");

			// ACT
			pet.addVisit(newerVisit);
			pet.addVisit(olderVisit);

			// ASSERT
			assertThat(pet.getVisits()).hasSize(2);
		}
	}

	@Nested
	@DisplayName("Complete Pet Tests")
	class CompletePetTests {

		@Test
		@DisplayName("Should create a complete pet with all properties")
		void testCompletePet() {
			// ARRANGE
			PetType perroType = new PetType();
			perroType.setName("Perro");

			// ACT
			pet.setId(1);
			pet.setName("Firulais");
			pet.setBirthDate(LocalDate.of(2020, 5, 15));
			pet.setType(perroType);

			Visit visit = new Visit();
			visit.setDescription("Vacunacion triple");
			pet.addVisit(visit);

			// ASSERT
			assertThat(pet.getId()).isEqualTo(1);
			assertThat(pet.getName()).isEqualTo("Firulais");
			assertThat(pet.getBirthDate()).isEqualTo(LocalDate.of(2020, 5, 15));
			assertThat(pet.getType().getName()).isEqualTo("Perro");
			assertThat(pet.getVisits()).hasSize(1);
			assertThat(pet.isNew()).isFalse();
		}
	}

}
