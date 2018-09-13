package de.agilecoders.wicket.sass;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.net.URI;
import java.net.URL;

import org.apache.wicket.Application;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.mock.MockServletContext;
import org.apache.wicket.request.resource.IResourceReferenceFactory;
import org.apache.wicket.request.resource.ResourceReferenceRegistry;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.tester.WicketTester;
import org.hamcrest.text.IsEqualIgnoringWhiteSpace;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.agilecoders.wicket.webjars.WicketWebjars;
import io.bit3.jsass.Options;
import io.bit3.jsass.type.SassString;
import io.bit3.jsass.type.SassValue;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public class BootstrapSassTest {

    private WicketTester tester;

    @Before
    public void before() {
        tester = new WicketTester(new TestApplication() {
            @Override
            public void init() {
                super.init();

                WicketWebjars.install(this);
                BootstrapSass.install(this);
            }
        });
    }

    @After
    public void after() {
        tester.destroy();
    }

    @Test
    public void importWebContext() throws Exception {
        final WebApplication application = tester.getApplication();
        final URI uri = getClass().getResource("/de/agilecoders/wicket/sass/test/webContextImported.scss").toURI();
        final File file = new File(uri);
        final File contextRoot = file.getParentFolder();
        // setup folder /.../bootstrap-sass/src/test/resources/de/agilecoders/wicket/sass/test as a root for the
        // ServletContext
        application.setServletContext(new MockServletContext(application, contextRoot.getAbsolutePath()));

        final SassCacheManager sass = SassCacheManager.get();
        final URL res = Thread.currentThread().getContextClassLoader().getResource("importWebContext.scss");

        final SassSource sassSource = sass.getSassContext(res, null);
        final String css = sass.getCss(sassSource);
        assertThat(css, IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(".rule {\n  background: #999; }\n"));
    }

    /**
     * Tests with {@link ContextRelativeSassResourceReference}
     *
     * https://github.com/l0rdn1kk0n/wicket-bootstrap/issues/524
     *
     * @throws Exception
     */
    @Test
    public void importServletContextRelative() throws Exception {
        final WebApplication application = tester.getApplication();
        final URI uri = getClass().getResource("/servlet/context/root/").toURI();
        final File contextRoot = new File(uri);
        // setup folder /.../bootstrap-sass/src/test/resources/servlet/context/root as a root for the ServletContext
        application.setServletContext(new MockServletContext(application, contextRoot.getAbsolutePath()));

        // tester.startPage(HomePage.class);
        tester.executeUrl("./wicket/resource/org.apache.wicket.Application/relative.scss?--"
                + ContextRelativeSassResourceReference.CONTEXT_RELATIVE_SASS_REFERENCE_VARIATION);
        tester.assertContains("sass-servlet-relative-cls");
        tester.assertContains("color: #333;");
    }

    @Test
    public void importWebJars() throws Exception {
        final SassCacheManager sass = SassCacheManager.get();
        final URL res = Thread.currentThread().getContextClassLoader().getResource("import.scss");

        final SassSource sassSource = sass.getSassContext(res, null);
        final String css = sass.getCss(sassSource);
        assertThat(css, IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(".rule {\n  background: #007bff; }\n"));
    }

    @Test
    public void importClasspath() {
        final SassCacheManager sass = SassCacheManager.get();
        final URL res = Thread.currentThread().getContextClassLoader().getResource("importClasspath.scss");

        final SassSource sassSource = sass.getSassContext(res, null);
        final String css = sass.getCss(sassSource);
        assertThat(css,
                IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(".classPathImported {\n  color: #333; }\n"));
    }

    @Test
    public void importPackage() {
        final SassCacheManager sass = SassCacheManager.get();
        final URL res = BootstrapSassTest.class.getResource("package-dependency-1.scss");

        final SassSource sassSource = sass.getSassContext(res, BootstrapSassTest.class.getName());
        final String css = sass.getCss(sassSource);
        assertThat(css, containsString("package1"));
        assertThat(css, containsString("package2"));
        assertThat(css, containsString("package3"));
    }

    @Test
    public void importPartials() {
        final SassCacheManager sass = SassCacheManager.get();
        final URL res = BootstrapSassTest.class.getResource("partial-root.scss");

        final SassSource sassSource = sass.getSassContext(res, BootstrapSassTest.class.getName());
        final String css = sass.getCss(sassSource);
        assertThat(css, containsString("root-cls"));
        assertThat(css, containsString("partial"));
    }

    @Test
    public void sassResourceReferenceFactoryIsInstalled() {
        final ResourceReferenceRegistry registry = tester.getApplication().getResourceReferenceRegistry();
        final IResourceReferenceFactory referenceFactory = registry.getResourceReferenceFactory();
        assertThat(referenceFactory, is(instanceOf(SassResourceReferenceFactory.class)));
    }

    @Test
    public void usesCustomSassCompilerConfigurationFactoryWhenProvided() {
        // tests the invocation of a custom sass function that will be registered within the configuration factory
        BootstrapSass.install(Application.get(), () -> {
            final Options config = new Options();
            config.getFunctionProviders().add(new TestFunctions());
            return config;
        });

        final SassCacheManager sass = SassCacheManager.get();
        final URL res = BootstrapSassTest.class.getResource("resources/custom-functions.scss");

        final SassSource sassSource = sass.getSassContext(res, null);
        final String css = sass.getCss(sassSource);
        assertThat(css, IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(".my-class {\n  color: blue; }\n"));
    }

    public static class TestFunctions {

        public SassValue myFancyFunction() {
            return new SassString("blue", false);
        }

    }
}
