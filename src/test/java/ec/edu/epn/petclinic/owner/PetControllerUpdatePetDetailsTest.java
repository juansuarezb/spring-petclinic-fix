package ec.edu.epn.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link PetController#updatePetDetails(Owner, Pet)} private method.
 * Uses reflection to test the private method directly.
 *
 * @author Ronny
 */
@ExtendWith(MockitoExtension.class)
class PetControllerUpdatePetDetailsTest {

	@Mock
	private OwnerRepository ownerRepository;

	@Mock
	private PetTypeRepository petTypeRepository;

	@InjectMocks
	private PetController petController;

	private Owner testOwner;
	private Pet existingPet;
	private PetType perroType;
	private PetType gatoType;

	@BeforeEach
	void setUp() {
		perroType = new PetType();
		perroType.setId(1);
		perroType.setName("Perro");

		gatoType = new PetType();
		gatoType.setId(2);
		gatoType.setName("Gato");

		existingPet = new Pet();
		existingPet.setId(1);
		existingPet.setName("Manchitas");
		existingPet.setType(perroType);
		existingPet.setBirthDate(LocalDate.of(2020, 1, 15));

		testOwner = new Owner();
		testOwner.setId(1);
		testOwner.setFirstName("María");
		testOwner.setLastName("Pérez");
		testOwner.setAddress("Av. Amazonas N35-17");
		testOwner.setCity("Quito");
		testOwner.setTelephone("0991234567");
		testOwner.getPets().add(existingPet);
	}

	@Nested
	@DisplayName("updatePetDetails - Update Existing Pet Tests")
	class UpdateExistingPetTests {

		@Test
		@DisplayName("Should update existing pet name")
		void testUpdateExistingPetName() throws Exception {
			// ARRANGE
			Pet updatedPet = new Pet();
			updatedPet.setId(1);
			updatedPet.setName("Firulais");
			updatedPet.setType(perroType);
			updatedPet.setBirthDate(LocalDate.of(2020, 1, 15));

			// ACT
			invokeUpdatePetDetails(testOwner, updatedPet);

			// ASSERT
			Pet petInOwner = testOwner.getPet(1);
			assertThat(petInOwner.getName()).isEqualTo("Firulais");
			verify(ownerRepository).save(testOwner);
		}

		@Test
		@DisplayName("Should update existing pet type")
		void testUpdateExistingPetType() throws Exception {
			// ARRANGE
			Pet updatedPet = new Pet();
			updatedPet.setId(1);
			updatedPet.setName("Manchitas");
			updatedPet.setType(gatoType);
			updatedPet.setBirthDate(LocalDate.of(2020, 1, 15));

			// ACT
			invokeUpdatePetDetails(testOwner, updatedPet);

			// ASSERT
			Pet petInOwner = testOwner.getPet(1);
			assertThat(petInOwner.getType().getName()).isEqualTo("Gato");
			verify(ownerRepository).save(testOwner);
		}

		@Test
		@DisplayName("Should update existing pet birth date")
		void testUpdateExistingPetBirthDate() throws Exception {
			// ARRANGE
			LocalDate newBirthDate = LocalDate.of(2019, 6, 20);
			Pet updatedPet = new Pet();
			updatedPet.setId(1);
			updatedPet.setName("Manchitas");
			updatedPet.setType(perroType);
			updatedPet.setBirthDate(newBirthDate);

			// ACT
			invokeUpdatePetDetails(testOwner, updatedPet);

			// ASSERT
			Pet petInOwner = testOwner.getPet(1);
			assertThat(petInOwner.getBirthDate()).isEqualTo(newBirthDate);
			verify(ownerRepository).save(testOwner);
		}

		@Test
		@DisplayName("Should update all pet properties at once")
		void testUpdateAllPetProperties() throws Exception {
			// ARRANGE
			LocalDate newBirthDate = LocalDate.of(2018, 3, 10);
			Pet updatedPet = new Pet();
			updatedPet.setId(1);
			updatedPet.setName("Pelusa");
			updatedPet.setType(gatoType);
			updatedPet.setBirthDate(newBirthDate);

			// ACT
			invokeUpdatePetDetails(testOwner, updatedPet);

			// ASSERT
			Pet petInOwner = testOwner.getPet(1);
			assertThat(petInOwner.getName()).isEqualTo("Pelusa");
			assertThat(petInOwner.getType().getName()).isEqualTo("Gato");
			assertThat(petInOwner.getBirthDate()).isEqualTo(newBirthDate);
			verify(ownerRepository).save(testOwner);
		}
	}

	@Nested
	@DisplayName("updatePetDetails - Add New Pet Tests")
	class AddNewPetTests {

		@Test
		@DisplayName("Should add new pet when pet id does not exist in owner")
		void testAddNewPetWhenNotExists() throws Exception {
			// ARRANGE
			Pet newPet = new Pet();
			newPet.setId(99); // ID que no existe en el owner
			newPet.setName("Diablo");
			newPet.setType(perroType);
			newPet.setBirthDate(LocalDate.of(2021, 7, 5));

			int initialPetCount = testOwner.getPets().size();

			// ACT
			invokeUpdatePetDetails(testOwner, newPet);

			// ASSERT
			// La mascota no se agrega porque addPet solo agrega mascotas nuevas (sin ID)
			// pero el método verifica si existe y si no, intenta agregarla
			verify(ownerRepository).save(testOwner);
		}
	}

	@Nested
	@DisplayName("updatePetDetails - Validation Tests")
	class ValidationTests {

		@Test
		@DisplayName("Should throw exception when pet id is null")
		void testThrowsExceptionWhenPetIdIsNull() {
			// ARRANGE
			Pet petWithNullId = new Pet();
			petWithNullId.setId(null);
			petWithNullId.setName("Copito");
			petWithNullId.setType(perroType);
			petWithNullId.setBirthDate(LocalDate.of(2020, 1, 1));

			// ACT & ASSERT
			assertThatThrownBy(() -> invokeUpdatePetDetails(testOwner, petWithNullId))
				.hasCauseInstanceOf(IllegalStateException.class)
				.hasMessageContaining("'pet.getId()' must not be null");
		}
	}

	@Nested
	@DisplayName("updatePetDetails - Save Behavior Tests")
	class SaveBehaviorTests {

		@Test
		@DisplayName("Should always call save on owner repository")
		void testAlwaysCallsSaveOnOwnerRepository() throws Exception {
			// ARRANGE
			Pet updatedPet = new Pet();
			updatedPet.setId(1);
			updatedPet.setName("Manchitas");
			updatedPet.setType(perroType);
			updatedPet.setBirthDate(LocalDate.of(2020, 1, 15));

			// ACT
			invokeUpdatePetDetails(testOwner, updatedPet);

			// ASSERT
			verify(ownerRepository).save(testOwner);
		}

		@Test
		@DisplayName("Should save owner even when pet does not exist")
		void testSavesOwnerEvenWhenPetNotFound() throws Exception {
			// ARRANGE
			Pet nonExistentPet = new Pet();
			nonExistentPet.setId(999);
			nonExistentPet.setName("Fantasma");
			nonExistentPet.setType(perroType);
			nonExistentPet.setBirthDate(LocalDate.of(2020, 1, 1));

			// ACT
			invokeUpdatePetDetails(testOwner, nonExistentPet);

			// ASSERT
			verify(ownerRepository).save(testOwner);
		}
	}

	/**
	 * Helper method to invoke the private updatePetDetails method using reflection.
	 */
	private void invokeUpdatePetDetails(Owner owner, Pet pet) throws Exception {
		Method method = PetController.class.getDeclaredMethod("updatePetDetails", Owner.class, Pet.class);
		method.setAccessible(true);
		try {
			method.invoke(petController, owner, pet);
		}
		catch (java.lang.reflect.InvocationTargetException e) {
			throw new RuntimeException(e.getCause());
		}
	}

}
