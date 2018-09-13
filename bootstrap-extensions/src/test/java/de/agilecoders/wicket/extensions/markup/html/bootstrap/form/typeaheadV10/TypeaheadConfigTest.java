package de.agilecoders.wicket.extensions.markup.html.bootstrap.form.typeaheadV10;

import java.util.Collections;

import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.WicketApplicationTest;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.typeaheadV10.bloodhound.Bloodhound;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.typeaheadV10.bloodhound.BloodhoundConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.typeaheadV10.bloodhound.Remote;

public class TypeaheadConfigTest extends WicketApplicationTest {

    @Test
    public void assertValidHeaderScript() {

        final WicketTester tester = tester();

        final Bloodhound bloodhound = new Bloodhound("engine", new BloodhoundConfig()) {

            @Override
            public Iterable getChoices(final String input) {
                return Collections.EMPTY_LIST;
            }

            @Override
            public String renderChoice(final Object choice) {
                return null;
            }
        };

        final DataSet dataSet = new DataSet(bloodhound);
        final TypeaheadConfig config = new TypeaheadConfig(dataSet);

        final Typeahead field = new Typeahead("typeahead", Model.of(""), config);
        tester.startComponentInPage(field,
                Markup.of("<html><head></head><body><input type='text' wicket:id='typeahead'/></body></html>"));

        final OnDomReadyHeaderItem item = field.getDomReadyScript(config);
        final String expected = "var engine = new Bloodhound({\"datumTokenizer\":function(d) { return Bloodhound.tokenizers.whitespace(d.value); },\"queryTokenizer\":Bloodhound.tokenizers.whitespace,\"remote\":\"./wicket/page?0-1.IBehaviorListener.0-typeahead&term=%QUERY\"});engine.initialize();$('#typeahead1').typeahead({},{\"name\":\"engine\",\"source\":engine.ttAdapter()});";
        assertEquals(expected, item.getJavaScript());
    }

    @Test
    public void testComplexRemote() {

        final Remote remote = new Remote();
        remote.withUrl("foo").withWildcard("%FOO");

        final BloodhoundConfig config = new BloodhoundConfig();
        config.withRemote(remote);

        final String expected = "{\"datumTokenizer\":function(d) { return Bloodhound.tokenizers.whitespace(d.value); },\"queryTokenizer\":Bloodhound.tokenizers.whitespace,\"remote\":{\"url\":\"foo\",\"wildcard\":\"%FOO\"}}";
        assertEquals(expected, config.toJsonString());

    }

    @Test
    public void testSimpleRemote() {

        final Remote remote = new Remote("foo");

        final BloodhoundConfig config = new BloodhoundConfig();
        config.withRemote(remote);

        final String expected = "{\"datumTokenizer\":function(d) { return Bloodhound.tokenizers.whitespace(d.value); },\"queryTokenizer\":Bloodhound.tokenizers.whitespace,\"remote\":\"foo\"}";
        assertEquals(expected, config.toJsonString());

    }
}
