package ec.edu.epn.petclinic.owner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OwnerRepository}.
 * Uses @DataJpaTest for JPA repository testing with H2 in-memory database.
 *
 * @author Dome
 */
@DataJpaTest
class OwnerRepositoryIntegrationTest {

	@Autowired
	private OwnerRepository ownerRepository;

	@Test
	@DisplayName("Should find owners by last name starting with prefix")
	void testFindByLastNameStartingWith() {
		// ARRANGE
		Owner owner = createOwner("María", "Quinapaluisa", "Av. Amazonas N35-17", "Quito", "0991234567");
		ownerRepository.save(owner);

		// ACT
		Page<Owner> result = ownerRepository.findByLastNameStartingWith("Quinapaluisa", PageRequest.of(0, 10));

		// ASSERT
		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().get(0).getLastName()).isEqualTo("Quinapaluisa");
	}

	@Test
	@DisplayName("Should return empty page when no owners match last name prefix")
	void testFindByLastNameStartingWithNoMatch() {
		// ARRANGE
		Owner owner = createOwner("Carlos", "Guamán", "Calle Sucre 456", "Cuenca", "0987654321");
		ownerRepository.save(owner);

		// ACT
		Page<Owner> result = ownerRepository.findByLastNameStartingWith("Morocho", PageRequest.of(0, 10));

		// ASSERT
		assertThat(result).isNotNull();
		assertThat(result.getContent()).isEmpty();
	}

	@Test
	@DisplayName("Should find all owners when searching with empty string")
	void testFindByLastNameStartingWithEmptyString() {
		// ARRANGE
		Owner owner1 = createOwner("Rosa", "Quispe", "Av. 10 de Agosto N35-12", "Quito", "0991234567");
		Owner owner2 = createOwner("José", "Pillajo", "Calle Venezuela 123", "Guayaquil", "0412345678");
		ownerRepository.save(owner1);
		ownerRepository.save(owner2);

		// ACT
		Page<Owner> result = ownerRepository.findByLastNameStartingWith("", PageRequest.of(0, 10));

		// ASSERT
		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSizeGreaterThanOrEqualTo(2);
	}

	@Test
	@DisplayName("Should find owner by id")
	void testFindById() {
		// ARRANGE
		Owner owner = createOwner("Luis", "Chuquimarca", "Av. El Inca E5-123", "Quito", "0998765432");
		Owner savedOwner = ownerRepository.save(owner);

		// ACT
		Optional<Owner> result = ownerRepository.findById(savedOwner.getId());

		// ASSERT
		assertThat(result).isPresent();
		assertThat(result.get().getFirstName()).isEqualTo("Luis");
		assertThat(result.get().getLastName()).isEqualTo("Chuquimarca");
	}

	@Test
	@DisplayName("Should return empty optional when owner not found by id")
	void testFindByIdNotFound() {
		// ARRANGE - no owner saved with id 9999

		// ACT
		Optional<Owner> result = ownerRepository.findById(9999);

		// ASSERT
		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("Should save owner correctly")
	void testSaveOwner() {
		// ARRANGE
		Owner owner = createOwner("Ana", "Toapanta", "Calle Bolivar 789", "Ambato", "0321234567");

		// ACT
		Owner savedOwner = ownerRepository.save(owner);

		// ASSERT
		assertThat(savedOwner.getId()).isNotNull();
		assertThat(savedOwner.getFirstName()).isEqualTo("Ana");
		assertThat(savedOwner.getLastName()).isEqualTo("Toapanta");
	}

	@Test
	@DisplayName("Should update existing owner")
	void testUpdateOwner() {
		// ARRANGE
		Owner owner = createOwner("Pedro", "Caiza", "Av. Mariscal Sucre", "Quito", "0991234567");
		Owner savedOwner = ownerRepository.save(owner);
		savedOwner.setCity("Latacunga");

		// ACT
		Owner updatedOwner = ownerRepository.save(savedOwner);

		// ASSERT
		assertThat(updatedOwner.getId()).isEqualTo(savedOwner.getId());
		assertThat(updatedOwner.getCity()).isEqualTo("Latacunga");
	}

	@Test
	@DisplayName("Should handle pagination correctly")
	void testPagination() {
		// ARRANGE
		String[] nombres = {"Juan", "María", "Carlos", "Rosa", "Luis", "Ana", "Pedro", "Lucía", "Jorge", "Carmen"};
		for (int i = 0; i < 10; i++) {
			Owner owner = createOwner(nombres[i], "Testpaginacion", "Calle " + (i+1), "Quito", "099123456" + i);
			ownerRepository.save(owner);
		}

		// ACT
		Page<Owner> firstPage = ownerRepository.findByLastNameStartingWith("Testpaginacion", PageRequest.of(0, 5));
		Page<Owner> secondPage = ownerRepository.findByLastNameStartingWith("Testpaginacion", PageRequest.of(1, 5));

		// ASSERT
		assertThat(firstPage.getContent()).hasSize(5);
		assertThat(secondPage.getContent()).hasSize(5);
		assertThat(firstPage.getTotalElements()).isEqualTo(10);
		assertThat(firstPage.getTotalPages()).isEqualTo(2);
	}

	@Test
	@DisplayName("Should save owner without pets initially")
	void testSaveOwnerWithoutPets() {
		// ARRANGE
		Owner owner = createOwner("Sofía", "Chango", "Av. Eloy Alfaro N34-56", "Quito", "0987654321");

		// ACT
		Owner savedOwner = ownerRepository.save(owner);

		// ASSERT
		assertThat(savedOwner.getId()).isNotNull();
		assertThat(savedOwner.getPets()).isEmpty();
	}

	private Owner createOwner(String firstName, String lastName, String address, String city, String telephone) {
		Owner owner = new Owner();
		owner.setFirstName(firstName);
		owner.setLastName(lastName);
		owner.setAddress(address);
		owner.setCity(city);
		owner.setTelephone(telephone);
		return owner;
	}

}
