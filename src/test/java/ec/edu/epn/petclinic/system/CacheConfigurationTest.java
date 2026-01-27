package ec.edu.epn.petclinic.system;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.cache.autoconfigure.JCacheManagerCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link CacheConfiguration}.
 *
 * @author Gianfranco Pupiales
 */
@SpringBootTest
class CacheConfigurationTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private CacheManager cacheManager;

    @Test
    @DisplayName("Should create JCacheManagerCustomizer bean")
    void testCacheManagerCustomizerBeanExists() {
        // ACT
        JCacheManagerCustomizer customizer = applicationContext.getBean(JCacheManagerCustomizer.class);

        // ASSERT
        assertThat(customizer).isNotNull();
    }

    @Test
    @DisplayName("Should have cache manager configured")
    void testCacheManagerConfigured() {
        // ACT & ASSERT
        assertThat(cacheManager).isNotNull();
    }

    @Test
    @DisplayName("Should have cache manager with expected behavior")
    void testCacheManagerBehavior() {
        // ACT
        var cacheNames = cacheManager.getCacheNames();

        // ASSERT
        // El gestor de caché debe inicializarse
        assertThat(cacheManager).isNotNull();
        // La colección de nombres de caché debe ser accesible
        assertThat(cacheNames).isNotNull();
    }
}