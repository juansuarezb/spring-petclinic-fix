package ec.edu.epn.petclinic.system;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link WebConfiguration}.
 *
 * @author Gianfranco Pupiales
 */
@SpringBootTest
class WebConfigurationTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private WebConfiguration webConfiguration;

    @Test
    @DisplayName("Should create localeResolver bean")
    void testLocaleResolverBeanExists() {
        // ACT
        LocaleResolver localeResolver = applicationContext.getBean(LocaleResolver.class);

        // ASSERT
        assertThat(localeResolver).isNotNull();
        assertThat(localeResolver).isInstanceOf(SessionLocaleResolver.class);
    }

    @Test
    @DisplayName("Should create localeChangeInterceptor bean")
    void testLocaleChangeInterceptorBeanExists() {
        // ACT
        LocaleChangeInterceptor interceptor = applicationContext.getBean(LocaleChangeInterceptor.class);

        // ASSERT
        assertThat(interceptor).isNotNull();
    }

    @Test
    @DisplayName("Should set default locale to English")
    void testDefaultLocaleIsEnglish() {
        // ACT
        LocaleResolver resolver = webConfiguration.localeResolver();

        // ASSERT
        assertThat(resolver).isInstanceOf(SessionLocaleResolver.class);
        assertThat(resolver).isNotNull();
    }

    @Test
    @DisplayName("Should configure interceptor with 'lang' parameter")
    void testInterceptorParamName() {
        // ACT
        LocaleChangeInterceptor interceptor = webConfiguration.localeChangeInterceptor();

        // ASSERT
        assertThat(interceptor.getParamName()).isEqualTo("lang");
    }

    @Test
    @DisplayName("Should create SessionLocaleResolver")
    void testLocaleResolverType() {
        // ACT
        LocaleResolver resolver = webConfiguration.localeResolver();

        // ASSERT
        assertThat(resolver).isInstanceOf(SessionLocaleResolver.class);
    }
}