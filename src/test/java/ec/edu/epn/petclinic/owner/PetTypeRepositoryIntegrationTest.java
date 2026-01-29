package ec.edu.epn.petclinic.owner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link PetTypeRepository}.
 * Uses @DataJpaTest for JPA repository testing with H2 in-memory database.
 *
 * @author Ronny
 */
@DataJpaTest
class PetTypeRepositoryIntegrationTest {

	@Autowired
	private PetTypeRepository petTypeRepository;

	@Test
	@DisplayName("Should find all pet types")
	void testFindPetTypes() {
		// ARRANGE
		PetType dog = new PetType();
		dog.setName("Dog");
		petTypeRepository.save(dog);

		PetType cat = new PetType();
		cat.setName("Cat");
		petTypeRepository.save(cat);

		// ACT
		List<PetType> result = petTypeRepository.findPetTypes();

		// ASSERT
		assertThat(result).isNotNull().hasSizeGreaterThanOrEqualTo(2);
	}

	@Test
	@DisplayName("Should return pet types ordered by name")
	void testFindPetTypesOrderedByName() {
		// ARRANGE
		PetType zebra = new PetType();
		zebra.setName("Zebra");
		petTypeRepository.save(zebra);

		PetType ant = new PetType();
		ant.setName("Ant");
		petTypeRepository.save(ant);

		PetType monkey = new PetType();
		monkey.setName("Monkey");
		petTypeRepository.save(monkey);

		// ACT
		List<PetType> result = petTypeRepository.findPetTypes();

		// ASSERT
		assertThat(result).isNotNull().hasSizeGreaterThanOrEqualTo(3);

		// Find our test types and verify ordering
		List<String> names = result.stream().map(PetType::getName).toList();
		int antIndex = names.indexOf("Ant");
		int monkeyIndex = names.indexOf("Monkey");
		int zebraIndex = names.indexOf("Zebra");

		assertThat(antIndex).isLessThan(monkeyIndex);
		assertThat(monkeyIndex).isLessThan(zebraIndex);
	}

	@Test
	@DisplayName("Should save new pet type")
	void testSavePetType() {
		// ARRANGE
		PetType bird = new PetType();
		bird.setName("Bird");

		// ACT
		PetType savedPetType = petTypeRepository.save(bird);

		// ASSERT
		assertThat(savedPetType.getId()).isNotNull();
		assertThat(savedPetType.getName()).isEqualTo("Bird");
	}

	@Test
	@DisplayName("Should find pet type by id")
	void testFindById() {
		// ARRANGE
		PetType reptile = new PetType();
		reptile.setName("Reptile");
		PetType savedPetType = petTypeRepository.save(reptile);

		// ACT
		var result = petTypeRepository.findById(savedPetType.getId());

		// ASSERT
		assertThat(result).isPresent();
		assertThat(result.get().getName()).isEqualTo("Reptile");
	}

	@Test
	@DisplayName("Should return empty when pet type not found by id")
	void testFindByIdNotFound() {
		// ACT
		var result = petTypeRepository.findById(9999);

		// ASSERT
		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("Should update existing pet type")
	void testUpdatePetType() {
		// ARRANGE
		PetType fish = new PetType();
		fish.setName("Fish");
		PetType savedPetType = petTypeRepository.save(fish);
		savedPetType.setName("Tropical Fish");

		// ACT
		PetType updatedPetType = petTypeRepository.save(savedPetType);

		// ASSERT
		assertThat(updatedPetType.getId()).isEqualTo(savedPetType.getId());
		assertThat(updatedPetType.getName()).isEqualTo("Tropical Fish");
	}

	@Test
	@DisplayName("Should delete pet type")
	void testDeletePetType() {
		// ARRANGE
		PetType hamster = new PetType();
		hamster.setName("Hamster");
		PetType savedPetType = petTypeRepository.save(hamster);
		Integer id = savedPetType.getId();

		// ACT
		petTypeRepository.delete(savedPetType);

		// ASSERT
		assertThat(petTypeRepository.findById(id)).isEmpty();
	}

}
