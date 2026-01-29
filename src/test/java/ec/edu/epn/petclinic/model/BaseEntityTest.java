package ec.edu.epn.petclinic.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class BaseEntityTest {

	@Test
	@DisplayName("Should return null when id is not set")
	void testGetIdReturnsNullWhenNotSet() {
		// ARRANGE
		BaseEntity entity = new ConcreteBaseEntity();

		// ACT
		Integer id = entity.getId();

		// ASSERT
		assertThat(id).isNull();
	}

	@Test
	@DisplayName("Should return the id when set")
	void testGetIdReturnsValueWhenSet() {
		// ARRANGE
		BaseEntity entity = new ConcreteBaseEntity();
		entity.setId(1);

		// ACT
		Integer id = entity.getId();

		// ASSERT
		assertThat(id).isEqualTo(1);
	}

	@Test
	@DisplayName("Should set id correctly")
	void testSetId() {
		// ARRANGE
		BaseEntity entity = new ConcreteBaseEntity();

		// ACT
		entity.setId(42);

		// ASSERT
		assertThat(entity.getId()).isEqualTo(42);
	}

	@Test
	@DisplayName("Should return true when entity is new (id is null)")
	void testIsNewReturnsTrueWhenIdIsNull() {
		// ARRANGE
		BaseEntity entity = new ConcreteBaseEntity();

		// ACT
		boolean isNew = entity.isNew();

		// ASSERT
		assertThat(isNew).isTrue();
	}

	@Test
	@DisplayName("Should return false when entity is not new (id is set)")
	void testIsNewReturnsFalseWhenIdIsSet() {
		// ARRANGE
		BaseEntity entity = new ConcreteBaseEntity();
		entity.setId(1);

		// ACT
		boolean isNew = entity.isNew();

		// ASSERT
		assertThat(isNew).isFalse();
	}

	@Test
	@DisplayName("Should allow setting id to null")
	void testSetIdToNull() {
		// ARRANGE
		BaseEntity entity = new ConcreteBaseEntity();
		entity.setId(1);

		// ACT
		entity.setId(null);

		// ASSERT
		assertThat(entity.getId()).isNull();
		assertThat(entity.isNew()).isTrue();
	}

	/**
	 * Concrete implementation of BaseEntity for testing purposes.
	 */
	private static class ConcreteBaseEntity extends BaseEntity {
	}

}
