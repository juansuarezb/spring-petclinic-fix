package ec.edu.epn.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Visit}.
 *
 * @author Gianfranco Pupiales
 */
class VisitTest {
    private Visit visit;

    @BeforeEach
    void setUp() {
        visit = new Visit();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should set current date in constructor")
        void testConstructorSetsCurrentDate() {
            // ARRANGE
            LocalDate today = LocalDate.now();

            // ACT
            Visit newVisit = new Visit();

            // ASSERT
            assertThat(newVisit.getDate()).isEqualTo(today);
        }
    }

    @Nested
    @DisplayName("Date Property Tests")
    class DatePropertyTests {

        @Test
        @DisplayName("Should get date correctly")
        void testGetDate() {
            // ARRANGE
            LocalDate today = LocalDate.now();

            // ACT
            LocalDate date = visit.getDate();

            // ASSERT
            assertThat(date).isEqualTo(today);
        }

        @Test
        @DisplayName("Should set date correctly")
        void testSetDate() {
            // ARRANGE
            LocalDate newDate = LocalDate.of(2023, 6, 15);

            // ACT
            visit.setDate(newDate);

            // ASSERT
            assertThat(visit.getDate()).isEqualTo(newDate);
        }

        @Test
        @DisplayName("Should allow setting date to null")
        void testSetDateToNull() {
            // ARRANGE - visit has date set in constructor

            // ACT
            visit.setDate(null);

            // ASSERT
            assertThat(visit.getDate()).isNull();
        }

        @Test
        @DisplayName("Should allow setting past date")
        void testSetPastDate() {
            // ARRANGE
            LocalDate pastDate = LocalDate.of(2020, 1, 1);

            // ACT
            visit.setDate(pastDate);

            // ASSERT
            assertThat(visit.getDate()).isEqualTo(pastDate);
        }

        @Test
        @DisplayName("Should allow setting future date")
        void testSetFutureDate() {
            // ARRANGE
            LocalDate futureDate = LocalDate.of(2099, 12, 31);

            // ACT
            visit.setDate(futureDate);

            // ASSERT
            assertThat(visit.getDate()).isEqualTo(futureDate);
        }
    }

    @Nested
    @DisplayName("Description Property Tests")
    class DescriptionPropertyTests {

        @Test
        @DisplayName("Should get description correctly")
        void testGetDescription() {
            // ARRANGE
            visit.setDescription("Control anual de salud");

            // ACT
            String description = visit.getDescription();

            // ASSERT
            assertThat(description).isEqualTo("Control anual de salud");
        }

        @Test
        @DisplayName("Should set description correctly")
        void testSetDescription() {
            // ARRANGE
            String description = "Vacunación antirrábica";

            // ACT
            visit.setDescription(description);

            // ASSERT
            assertThat(visit.getDescription()).isEqualTo(description);
        }

        @Test
        @DisplayName("Should return null when description is not set")
        void testGetDescriptionReturnsNullWhenNotSet() {
            // ARRANGE - visita creada sin descripción.

            // ACT
            String description = visit.getDescription();

            // ASSERT
            assertThat(description).isNull();
        }

        @Test
        @DisplayName("Should allow empty string as description")
        void testSetEmptyDescription() {
            // ARRANGE
            String emptyDescription = "";

            // ACT
            visit.setDescription(emptyDescription);

            // ASSERT
            assertThat(visit.getDescription()).isEmpty();
        }

        @Test
        @DisplayName("Should allow setting description to null")
        void testSetDescriptionToNull() {
            // ARRANGE
            visit.setDescription("Desparasitación interna");

            // ACT
            visit.setDescription(null);

            // ASSERT
            assertThat(visit.getDescription()).isNull();
        }
    }

    @Nested
    @DisplayName("Inherited Property Tests")
    class InheritedPropertyTests {

        @Test
        @DisplayName("Should set and get id correctly (inherited from BaseEntity)")
        void testSetAndGetId() {
            // ARRANGE
            Integer id = 1;

            // ACT
            visit.setId(id);

            // ASSERT
            assertThat(visit.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Should return isNew true when id is null")
        void testIsNewTrue() {
            // ARRANGE - visit without id

            // ACT
            boolean isNew = visit.isNew();

            // ASSERT
            assertThat(isNew).isTrue();
        }

        @Test
        @DisplayName("Should return isNew false when id is set")
        void testIsNewFalse() {
            // ARRANGE
            visit.setId(1);

            // ACT
            boolean isNew = visit.isNew();

            // ASSERT
            assertThat(isNew).isFalse();
        }
    }

    @Nested
    @DisplayName("Complete Visit Tests")
    class CompleteVisitTests {
        @Test
        @DisplayName("Should create a complete visit with all properties")
        void testCompleteVisit() {
            // ARRANGE & ACT
            visit.setId(1);
            visit.setDate(LocalDate.of(2023, 7, 20));
            visit.setDescription("Limpieza dental para Manchitas");

            // ASSERT
            assertThat(visit.getId()).isEqualTo(1);
            assertThat(visit.getDate()).isEqualTo(LocalDate.of(2023, 7, 20));
            assertThat(visit.getDescription()).isEqualTo("Limpieza dental para Manchitas");
            assertThat(visit.isNew()).isFalse();
        }
    }
}
