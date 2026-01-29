package ec.edu.epn.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link PetTypeFormatter}.
 *
 * @author Ronny
 */
@ExtendWith(MockitoExtension.class)
class PetTypeFormatterTest {

	@Mock
	private PetTypeRepository petTypeRepository;

	private PetTypeFormatter formatter;

	@BeforeEach
	void setUp() {
		formatter = new PetTypeFormatter(petTypeRepository);
	}

	@Nested
	@DisplayName("Print Tests")
	class PrintTests {

		@Test
		@DisplayName("Should print pet type name")
		void testPrintPetTypeName() {
			// ARRANGE
			PetType petType = new PetType();
			petType.setName("Perro");

			// ACT
			String result = formatter.print(petType, Locale.forLanguageTag("es-EC"));

			// ASSERT
			assertThat(result).isEqualTo("Perro");
		}

		@Test
		@DisplayName("Should print <null> when pet type name is null")
		void testPrintNullName() {
			// ARRANGE
			PetType petType = new PetType();
			petType.setName(null);

			// ACT
			String result = formatter.print(petType, Locale.forLanguageTag("es-EC"));

			// ASSERT
			assertThat(result).isEqualTo("<null>");
		}

		@Test
		@DisplayName("Should work with different locales")
		void testPrintWithDifferentLocales() {
			// ARRANGE
			PetType petType = new PetType();
			petType.setName("Gato");

			// ACT
			String resultSpanish = formatter.print(petType, Locale.forLanguageTag("es-EC"));
			String resultEnglish = formatter.print(petType, Locale.ENGLISH);
			String resultGerman = formatter.print(petType, Locale.GERMAN);

			// ASSERT
			assertThat(resultSpanish).isEqualTo("Gato");
			assertThat(resultEnglish).isEqualTo("Gato");
			assertThat(resultGerman).isEqualTo("Gato");
		}
	}

	@Nested
	@DisplayName("Parse Tests")
	class ParseTests {

		@Test
		@DisplayName("Should parse existing pet type")
		void testParseExistingPetType() throws ParseException {
			// ARRANGE
			PetType perro = new PetType();
			perro.setId(1);
			perro.setName("Perro");

			PetType gato = new PetType();
			gato.setId(2);
			gato.setName("Gato");

			when(petTypeRepository.findPetTypes()).thenReturn(Arrays.asList(perro, gato));

			// ACT
			PetType result = formatter.parse("Perro", Locale.forLanguageTag("es-EC"));

			// ASSERT
			assertThat(result).isNotNull();
			assertThat(result.getName()).isEqualTo("Perro");
			assertThat(result.getId()).isEqualTo(1);
		}

		@Test
		@DisplayName("Should throw ParseException when pet type not found")
		void testParseNotFound() {
			// ARRANGE
			PetType perro = new PetType();
			perro.setName("Perro");

			when(petTypeRepository.findPetTypes()).thenReturn(Collections.singletonList(perro));

			// ACT & ASSERT
			assertThatThrownBy(() -> formatter.parse("Loro", Locale.forLanguageTag("es-EC")))
				.isInstanceOf(ParseException.class)
				.hasMessageContaining("type not found: Loro");
		}

		@Test
		@DisplayName("Should throw ParseException when repository returns empty list")
		void testParseEmptyRepository() {
			// ARRANGE
			when(petTypeRepository.findPetTypes()).thenReturn(Collections.emptyList());

			// ACT & ASSERT
			assertThatThrownBy(() -> formatter.parse("Perro", Locale.forLanguageTag("es-EC")))
				.isInstanceOf(ParseException.class)
				.hasMessageContaining("type not found: Perro");
		}

		@Test
		@DisplayName("Should parse case-sensitive")
		void testParseCaseSensitive() {
			// ARRANGE
			PetType perro = new PetType();
			perro.setName("Perro");

			when(petTypeRepository.findPetTypes()).thenReturn(Collections.singletonList(perro));

			// ACT & ASSERT
			assertThatThrownBy(() -> formatter.parse("perro", Locale.forLanguageTag("es-EC")))
				.isInstanceOf(ParseException.class)
				.hasMessageContaining("type not found: perro");
		}

		@Test
		@DisplayName("Should parse with different locales")
		void testParseWithDifferentLocales() throws ParseException {
			// ARRANGE
			PetType gato = new PetType();
			gato.setId(1);
			gato.setName("Gato");

			when(petTypeRepository.findPetTypes()).thenReturn(Collections.singletonList(gato));

			// ACT
			PetType resultSpanish = formatter.parse("Gato", Locale.forLanguageTag("es-EC"));
			PetType resultEnglish = formatter.parse("Gato", Locale.ENGLISH);

			// ASSERT
			assertThat(resultSpanish.getName()).isEqualTo("Gato");
			assertThat(resultEnglish.getName()).isEqualTo("Gato");
		}

		@Test
		@DisplayName("Should find first matching pet type when duplicates exist")
		void testParseFirstMatch() throws ParseException {
			// ARRANGE
			PetType perro1 = new PetType();
			perro1.setId(1);
			perro1.setName("Perro");

			PetType perro2 = new PetType();
			perro2.setId(2);
			perro2.setName("Perro");

			when(petTypeRepository.findPetTypes()).thenReturn(Arrays.asList(perro1, perro2));

			// ACT
			PetType result = formatter.parse("Perro", Locale.forLanguageTag("es-EC"));

			// ASSERT
			assertThat(result.getId()).isEqualTo(1);
		}
	}

}
