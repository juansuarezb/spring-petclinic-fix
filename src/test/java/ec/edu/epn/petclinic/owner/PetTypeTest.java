package ec.edu.epn.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link PetType}.
 *
 * @author Ronny
 */
class PetTypeTest {

	private PetType petType;

	@BeforeEach
	void setUp() {
		petType = new PetType();
	}

	@Test
	@DisplayName("Should inherit from NamedEntity")
	void testInheritsFromNamedEntity() {
		// ARRANGE - petType already created

		// ACT
		petType.setName("Dog");
		petType.setId(1);

		// ASSERT
		assertThat(petType.getName()).isEqualTo("Dog");
		assertThat(petType.getId()).isEqualTo(1);
	}

	@Test
	@DisplayName("Should set and get name correctly")
	void testSetAndGetName() {
		// ARRANGE
		String name = "Cat";

		// ACT
		petType.setName(name);

		// ASSERT
		assertThat(petType.getName()).isEqualTo(name);
	}

	@Test
	@DisplayName("Should return correct toString representation")
	void testToString() {
		// ARRANGE
		petType.setName("Bird");

		// ACT
		String result = petType.toString();

		// ASSERT
		assertThat(result).isEqualTo("Bird");
	}

	@Test
	@DisplayName("Should return <null> in toString when name is null")
	void testToStringWithNullName() {
		// ARRANGE - petType without name set

		// ACT
		String result = petType.toString();

		// ASSERT
		assertThat(result).isEqualTo("<null>");
	}

	@Test
	@DisplayName("Should be new when id is not set")
	void testIsNewTrue() {
		// ARRANGE - petType without id

		// ACT
		boolean isNew = petType.isNew();

		// ASSERT
		assertThat(isNew).isTrue();
	}

	@Test
	@DisplayName("Should not be new when id is set")
	void testIsNewFalse() {
		// ARRANGE
		petType.setId(1);

		// ACT
		boolean isNew = petType.isNew();

		// ASSERT
		assertThat(isNew).isFalse();
	}

}
