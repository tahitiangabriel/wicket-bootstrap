package de.agilecoders.wicket.extensions.markup.html.bootstrap.form;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupException;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.tester.TagTester;
import org.junit.Test;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.WicketApplicationTest;

/**
 * Abstract base class for the DateTextField tests.
 *
 * @param <T>
 *            the type of date ({@code java.util.Date} or {@code java.time.LocalDate})
 * @param <P>
 *            the type of the parent wicket {@code TextField} working with date of type {@code T}
 * @param <I>
 *            the input date type for the configuration options withBeginDate or withEndDate
 * @param <C>
 *            the configuration - the concrete implementation of the AbstractDateTextFieldConfig
 * @param <F>
 *            the concrete implementation type of this abstract class
 */
public abstract class AbstractDateTextFieldTest<T, P extends TextField<T> & AbstractTextComponent.ITextFormatProvider, I, C extends AbstractDateTextFieldConfig<C, I>, F extends AbstractDateTextField<T, P, I, C, F>>
        extends WicketApplicationTest {

    private final AbstractDateTextField.IParentAjaxEventHandler handler = (target, date, event) -> {
    };

    /**
     * @return a new default configuration with no specific properties set.
     */
    abstract C newDefaultConfig();

    /**
     * @return a date of type {@code D} representing {@literal now}
     */
    abstract T getNow();

    @Test
    public void assertDefaultLocalDateTextFieldConfig_hasLanguageEnglish() {
        assertThat(newDefaultConfig().getLanguage(), is(equalTo("en")));
    }

    @Test
    public void assertDefaultLocalDateTextFieldConfig_hasAmericanDateFormat() {
        assertThat(newDefaultConfig().getFormat(), is(equalTo("MM/dd/yyyy")));
    }

    @Test
    public void constructing_withIdOnly_knowsIdAndDefaultConfig() {
        final F f = newTextField();

        assertThat(f.getId(), is(equalTo("tf")));
        assertThat(f.getModelObject(), is(equalTo(null)));

        hasDefaultModel(f);
    }

    abstract F newTextField();

    void hasDefaultModel(final F f) {
        final C config = f.getConfig();
        assertThat(config.getFormat(), is(equalTo(newDefaultConfig().getFormat())));
        assertThat(config.getLanguage(), is(equalTo(newDefaultConfig().getLanguage())));
    }

    @Test
    public void canReplaceConfig() {
        final F f = newTextField();
        assertThat(f.getConfig().getFormat(), is(equalTo("MM/dd/yyyy")));

        f.with(newDefaultConfig().withFormat("dd.MM.yyyy"));

        assertThat(f.getConfig().getFormat(), is(equalTo("dd.MM.yyyy")));
    }

    @Test
    public void cannotReplaceConfig_withNullConfig() {
        final F f = newTextField();
        f.with(newDefaultConfig().withFormat("dd.MM.yyyy"));
        f.with(null);
        assertThat(f.getConfig().getFormat(), is(equalTo("dd.MM.yyyy")));
    }

    @Test
    public void onInitialize_setsOutputMarkupId() {
        final F f = newTextField();
        assertThat(f.getOutputMarkupId(), is(false));
        f.onInitialize();
        assertThat(f.getOutputMarkupId(), is(true));
    }

    @Test
    public void gettingDestroyScript() {
        final F f = newTextField();
        assertThat(f.getDestroyScript(), is(equalTo("$('#tf1').datepicker('destroy');")));
    }

    @Test
    public void startingComponent_withoutInputTagAttached_throwsMarkupException() {
        final F f = newTextField();
        try {
            tester().startComponentInPage(f);
            fail("should have thrown exception");
        } catch (final Exception ex) {
            assertThat(ex.getClass(), is(equalTo(MarkupException.class)));
            assertThat(ex.getMessage(), is(equalTo(
                    "Component [tf] (path = [0:tf]) must be applied to a tag of type [input], not:  '<span wicket:id=\"tf\">' (line 0, column 0)")));
        }
    }

    @Test
    public void startingComponent_withFieldAssignedToInput_succeedsAndHasTypeText() {
        final DateTextField f = new DateTextField("myId");
        tester().startComponentInPage(f, Markup.of("<input wicket:id='myId'></input>"));

        final String responseTxt = tester().getLastResponse().getDocument();
        assertThat(TagTester.createTagByAttribute(responseTxt, "type", "text"), is(notNullValue()));
        tester().assertNoErrorMessage();
    }

    @Test
    public void creatingScript_withDefaultConfig() {
        final CharSequence script = newTextField().createScript(newDefaultConfig());
        assertThat(script, is(equalTo("$('#tf1').datepicker();")));
    }

    @Test
    public void creatingScript_withExplicitConfig_withNonDefaultValues() {
        // @formatter:off
        final String expectedScript = "$('#tf1').datepicker({" + "\"format\":\"dd.mm.yyyy\"," + "\"language\":\"de\","
                + "\"startDate\":\"30.06.2018\"," + "\"minViewMode\":4," + "\"endDate\":\"13.07.2018\","
                + "\"startView\":3," + "\"weekStart\":1," + "\"keyboardNavigation\":false," + "\"todayHighlight\":true,"
                + "\"todayBtn\":\"linked\"," + "\"forceParse\":false," + "\"clearBtn\":true,"
                + "\"calendarWeeks\":true," + "\"autoclose\":true," + "\"multidate\":true" + "});";
        // @formatter:on
        final C config = newDefaultConfig().withFormat("dd.MM.yyyy").withLanguage("de").withStartDate(getStartDate())
                .withMinViewMode(DateTextFieldConfig.View.Day).withEndDate(getEndDate())
                .withView(DateTextFieldConfig.View.Day).withWeekStart(DateTextFieldConfig.Day.Monday)
                .allowKeyboardNavigation(false).highlightToday(true)
                .showTodayButton(DateTextFieldConfig.TodayButton.LINKED).forceParse(false).clearButton(true)
                .calendarWeeks(true).autoClose(true).withMulti(true);
        final CharSequence script = newTextField().createScript(config);
        assertThat(script, is(equalTo(expectedScript)));
    }

    abstract I getStartDate();

    abstract I getEndDate();

    @Test
    public void creatingScript_withExplicitConfig_skipsDefaultValuesExcplicitlySet() {
        // @formatter:off
        final String expectedScript = "$('#tf1').datepicker({" + "\"format\":\"yyyy-mm-dd\"," + "\"language\":\"fr\","
                + "\"startDate\":\"2018-06-30\"," + "\"minViewMode\":1," + "\"endDate\":\"2018-07-13\","
                + "\"startView\":2," + "\"weekStart\":2" + "});";
        // @formatter:on
        final C config = newDefaultConfig().withFormat("yyyy-MM-dd").withLanguage("fr").withStartDate(getStartDate())
                .withMinViewMode(DateTextFieldConfig.View.Month).withEndDate(getEndDate())
                .withView(DateTextFieldConfig.View.Decade).withWeekStart(DateTextFieldConfig.Day.Tuesday)
                .allowKeyboardNavigation(true).highlightToday(false)
                .showTodayButton(DateTextFieldConfig.TodayButton.FALSE).forceParse(true).clearButton(false)
                .calendarWeeks(false).autoClose(false).withMulti(false);
        final CharSequence script = newTextField().createScript(config);
        assertThat(script, is(equalTo(expectedScript)));
    }

    @Test
    public void creatingScript_withExplicitConfigWithoutExplicitFormat_usesUsFormat() {
        // @formatter:off
        final String expectedScript = "$('#tf1').datepicker({" + "\"language\":\"es\","
                + "\"startDate\":\"06/30/2018\"," + "\"todayBtn\":true," + "\"endDate\":\"07/13/2018\"" + "});";
        // @formatter:on
        final C config = newDefaultConfig().withLanguage("es").withStartDate(getStartDate())
                .showTodayButton(DateTextFieldConfig.TodayButton.TRUE).withEndDate(getEndDate());
        final CharSequence script = newTextField().createScript(config);
        assertThat(script, is(equalTo(expectedScript)));
    }

    @Test
    public void addingEvent_addsToScript() {
        final DateTextField.AbstractEventHandler handler = new DateTextField.AbstractEventHandler() {

            @Override
            protected CharSequence getBody() {
                return "eventBody";
            }
        };
        final F f = newTextField().addEvent(DateTextField.Event.clearDate, handler);
        final String script = f.createScript(newDefaultConfig()).toString();
        assertThat(script.replaceAll("\\n", ""),
                is(equalTo("$('#tf1').datepicker().on('clearDate',function(e) {eventBody});")));
    }

    @Test
    public void canAddAndRemoveEvent() {
        final DateTextField.AbstractEventHandler handler1 = new DateTextField.AbstractEventHandler() {

            @Override
            protected CharSequence getBody() {
                return "eventBody1";
            }
        };
        final DateTextField.AbstractEventHandler handler2 = new DateTextField.AbstractEventHandler() {

            @Override
            protected CharSequence getBody() {
                return "eventBody2";
            }
        };
        final F f = newTextField().addEvent(DateTextField.Event.clearDate, handler1)
                .addEvent(DateTextField.Event.changeYear, handler2);
        String script = f.createScript(newDefaultConfig()).toString();
        assertThat(script.replaceAll("\\n", ""), startsWith("$('#tf1').datepicker()"));
        assertThat(script.replaceAll("\\n", ""), containsString(".on('clearDate',function(e) {eventBody1})"));
        assertThat(script.replaceAll("\\n", ""), containsString(".on('changeYear',function(e) {eventBody2})"));
        f.removeEvent(DateTextField.Event.clearDate);
        script = f.createScript(newDefaultConfig()).toString();
        assertThat(script.replaceAll("\\n", ""),
                is(equalTo("$('#tf1').datepicker().on('changeYear',function(e) {eventBody2});")));

    }

    @Test
    public void addingAjaxEvent1() {
        final F f = newTextField().addAjaxEvent(DateTextField.Event.clearDate, handler);
        tester().startComponentInPage(f, Markup.of("<input wicket:id='tf'></input>"));
        tester().executeAjaxEvent("tf", "clearDate");
        // TODO need to find something to assert the ajax event
    }
}
