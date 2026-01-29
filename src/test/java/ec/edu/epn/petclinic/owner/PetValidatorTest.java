package ec.edu.epn.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link PetValidator}.
 *
 * @author Ronny
 */
class PetValidatorTest {

	private PetValidator validator;

	private Pet pet;

	private Errors errors;

	@BeforeEach
	void setUp() {
		validator = new PetValidator();
		pet = new Pet();
		errors = new BeanPropertyBindingResult(pet, "pet");
	}

	@Nested
	@DisplayName("Supports Tests")
	class SupportsTests {

		@Test
		@DisplayName("Should support Pet class")
		void testSupportsPetClass() {
			// ARRANGE - nothing needed

			// ACT
			boolean supports = validator.supports(Pet.class);

			// ASSERT
			assertThat(supports).isTrue();
		}

		@Test
		@DisplayName("Should not support other classes")
		void testNotSupportsOtherClass() {
			// ARRANGE - nothing needed

			// ACT
			boolean supportsOwner = validator.supports(Owner.class);
			boolean supportsVisit = validator.supports(Visit.class);
			boolean supportsString = validator.supports(String.class);

			// ASSERT
			assertThat(supportsOwner).isFalse();
			assertThat(supportsVisit).isFalse();
			assertThat(supportsString).isFalse();
		}

		@Test
		@DisplayName("Should support subclasses of Pet")
		void testSupportsSubclassOfPet() {
			// ARRANGE
			class ExtendedPet extends Pet {
			}

			// ACT
			boolean supports = validator.supports(ExtendedPet.class);

			// ASSERT
			assertThat(supports).isTrue();
		}
	}

	@Nested
	@DisplayName("Name Validation Tests")
	class NameValidationTests {

		@Test
		@DisplayName("Should reject empty name")
		void testValidateEmptyName() {
			// ARRANGE
			pet.setName("");
			PetType type = new PetType();
			type.setName("Dog");
			pet.setType(type);
			pet.setBirthDate(LocalDate.now());

			// ACT
			validator.validate(pet, errors);

			// ASSERT
			assertThat(errors.hasFieldErrors("name")).isTrue();
			assertThat(errors.getFieldError("name").getCode()).isEqualTo("required");
		}

		@Test
		@DisplayName("Should reject null name")
		void testValidateNullName() {
			// ARRANGE
			pet.setName(null);
			PetType type = new PetType();
			type.setName("Dog");
			pet.setType(type);
			pet.setBirthDate(LocalDate.now());

			// ACT
			validator.validate(pet, errors);

			// ASSERT
			assertThat(errors.hasFieldErrors("name")).isTrue();
		}

		@Test
		@DisplayName("Should reject whitespace-only name")
		void testValidateWhitespaceName() {
			// ARRANGE
			pet.setName("   ");
			PetType type = new PetType();
			type.setName("Dog");
			pet.setType(type);
			pet.setBirthDate(LocalDate.now());

			// ACT
			validator.validate(pet, errors);

			// ASSERT
			assertThat(errors.hasFieldErrors("name")).isTrue();
		}

		@Test
		@DisplayName("Should accept valid name")
		void testValidateValidName() {
			// ARRANGE
			pet.setName("Buddy");
			PetType type = new PetType();
			type.setName("Dog");
			pet.setType(type);
			pet.setBirthDate(LocalDate.now());

			// ACT
			validator.validate(pet, errors);

			// ASSERT
			assertThat(errors.hasFieldErrors("name")).isFalse();
		}
	}

	@Nested
	@DisplayName("Type Validation Tests")
	class TypeValidationTests {

		@Test
		@DisplayName("Should reject null type for new pet")
		void testValidateNullTypeForNewPet() {
			// ARRANGE
			pet.setName("Buddy");
			pet.setType(null);
			pet.setBirthDate(LocalDate.now());
			// pet is new (no id set)

			// ACT
			validator.validate(pet, errors);

			// ASSERT
			assertThat(errors.hasFieldErrors("type")).isTrue();
			assertThat(errors.getFieldError("type").getCode()).isEqualTo("required");
		}

		@Test
		@DisplayName("Should not reject null type for existing pet")
		void testValidateNullTypeForExistingPet() {
			// ARRANGE
			pet.setId(1); // existing pet
			pet.setName("Buddy");
			pet.setType(null);
			pet.setBirthDate(LocalDate.now());

			// ACT
			validator.validate(pet, errors);

			// ASSERT
			assertThat(errors.hasFieldErrors("type")).isFalse();
		}

		@Test
		@DisplayName("Should accept valid type for new pet")
		void testValidateValidTypeForNewPet() {
			// ARRANGE
			pet.setName("Buddy");
			PetType type = new PetType();
			type.setName("Dog");
			pet.setType(type);
			pet.setBirthDate(LocalDate.now());

			// ACT
			validator.validate(pet, errors);

			// ASSERT
			assertThat(errors.hasFieldErrors("type")).isFalse();
		}
	}

	@Nested
	@DisplayName("Birth Date Validation Tests")
	class BirthDateValidationTests {

		@Test
		@DisplayName("Should reject null birth date")
		void testValidateNullBirthDate() {
			// ARRANGE
			pet.setName("Buddy");
			PetType type = new PetType();
			type.setName("Dog");
			pet.setType(type);
			pet.setBirthDate(null);

			// ACT
			validator.validate(pet, errors);

			// ASSERT
			assertThat(errors.hasFieldErrors("birthDate")).isTrue();
			assertThat(errors.getFieldError("birthDate").getCode()).isEqualTo("required");
		}

		@Test
		@DisplayName("Should accept valid birth date")
		void testValidateValidBirthDate() {
			// ARRANGE
			pet.setName("Buddy");
			PetType type = new PetType();
			type.setName("Dog");
			pet.setType(type);
			pet.setBirthDate(LocalDate.of(2020, 1, 15));

			// ACT
			validator.validate(pet, errors);

			// ASSERT
			assertThat(errors.hasFieldErrors("birthDate")).isFalse();
		}
	}

	@Nested
	@DisplayName("Complete Validation Tests")
	class CompleteValidationTests {

		@Test
		@DisplayName("Should pass validation with all valid fields")
		void testValidateSuccess() {
			// ARRANGE
			pet.setName("Buddy");
			PetType type = new PetType();
			type.setName("Dog");
			pet.setType(type);
			pet.setBirthDate(LocalDate.of(2020, 1, 15));

			// ACT
			validator.validate(pet, errors);

			// ASSERT
			assertThat(errors.hasErrors()).isFalse();
		}

		@Test
		@DisplayName("Should report multiple errors when multiple fields are invalid")
		void testValidateMultipleErrors() {
			// ARRANGE
			pet.setName("");
			pet.setType(null);
			pet.setBirthDate(null);

			// ACT
			validator.validate(pet, errors);

			// ASSERT
			assertThat(errors.getErrorCount()).isEqualTo(3);
			assertThat(errors.hasFieldErrors("name")).isTrue();
			assertThat(errors.hasFieldErrors("type")).isTrue();
			assertThat(errors.hasFieldErrors("birthDate")).isTrue();
		}
	}

}
