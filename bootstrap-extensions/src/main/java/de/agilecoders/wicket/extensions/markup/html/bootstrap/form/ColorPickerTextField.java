package de.agilecoders.wicket.extensions.markup.html.bootstrap.form;

import static de.agilecoders.wicket.jquery.JQuery.$;

import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestHandler;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxRequestTarget.IJavaScriptResponse;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.util.string.StringValue;

import de.agilecoders.wicket.core.util.Attributes;
import de.agilecoders.wicket.jquery.JQuery;
import de.agilecoders.wicket.jquery.function.JavaScriptInlineFunction;

/**
 * Bootstrap ColorPicker from https://farbelous.io/bootstrap-colorpicker/v2/
 */
public class ColorPickerTextField extends TextField<String> {

    private static final long serialVersionUID = 1L;

    /**
     * bootstrap-colorpicker won't enhance if the textfield was not enabled,
     * so we must wait until the first time the textfield is enabled.
     */
    private boolean wasEnhanced = false;

    private final ColorPickerConfig config;

    /**
     * @param id
     */
    public ColorPickerTextField(final String id) {
        this(id, null, new ColorPickerConfig());
    }

    /**
     * @param id
     * @param model
     */
    public ColorPickerTextField(final String id, final IModel<String> model) {
        this(id, model, new ColorPickerConfig());
    }

    public ColorPickerTextField(final String id, final IModel<String> model, final ColorPickerConfig config) {
        super(id, model, String.class);

        this.config = config;

        setOutputMarkupId(true);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        final List<ColorChangeAjaxBehavior> behaviors = getBehaviors(ColorChangeAjaxBehavior.class);
        if (config.isAjaxUpdate()) {
            if (behaviors.isEmpty()) {
                add(new ColorChangeAjaxBehavior() {

                    @Override
                    protected void updateAjaxAttributes(final AjaxRequestAttributes attributes) {
                        super.updateAjaxAttributes(attributes);
                        ColorPickerTextField.this.updateAjaxAttributes(attributes);
                    }

                    @Override
                    protected void onChange(final AjaxRequestTarget target, final String color) {
                        ColorPickerTextField.this.onChange(target, color);
                    }
                });
            }
        } else {
            for (final ColorChangeAjaxBehavior colorChangeAjaxBehavior : behaviors) {
                remove(colorChangeAjaxBehavior);
            }
        }
    }

    protected void updateAjaxAttributes(final AjaxRequestAttributes attributes) {
    }

    protected void onChange(final AjaxRequestTarget target, final String color) {
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(ColorPickerTextFieldCssReference.instance()));
        response.render(JavaScriptHeaderItem.forReference(ColorPickerTextFieldJavaScriptReference.instance()));
        if (isEnabledInHierarchy()) {
            response.render(OnDomReadyHeaderItem.forScript(createScript(config)));
            // wasEnhanced = true; // not working if initially disabled
        }
    }

    @Override
    protected void onComponentTag(final ComponentTag tag) {
        super.onComponentTag(tag);
        Attributes.addClass(tag, "colorpicker-component");
    }

    @Override
    public void onEvent(final IEvent<?> event) {
        super.onEvent(event);
        if (event.getPayload() instanceof AjaxRequestHandler) {
            final AjaxRequestHandler target = (AjaxRequestHandler) event.getPayload();
            target.addListener(new AjaxRequestTarget.AbstractListener() {
                @Override
                public void onAfterRespond(final Map<String, Component> map, final IJavaScriptResponse response) {
                    if (isEnabledInHierarchy() && !wasEnhanced) {
                        response.addJavaScript(createScript(config));
                        wasEnhanced = true;
                    } else if (!isEnabledInHierarchy()) {
                        // we need to enhance again when/if this component is enabled later
                        wasEnhanced = false;
                    }
                }
            });
        }
    }

    /**
     * creates the initializer script.
     *
     * @return initializer script
     */
    protected String createScript(final ColorPickerConfig config) {
        final JQuery script = $(this);

        if (config.isComponent()) {
            script.closest(".colorpicker-component");
        }
        script.chain("colorpicker", config);

        if (config.isAjaxUpdate()) {

            final List<ColorChangeAjaxBehavior> behaviors = getBehaviors(ColorChangeAjaxBehavior.class);
            final ColorChangeAjaxBehavior colorChangeAjaxBehavior = behaviors.get(0);
            final String toColor = config.getFormat().to();

            final CharSequence attrs = colorChangeAjaxBehavior.getAttrs();
            script.on("changeColor", new JavaScriptInlineFunction(
                    String.format("var color = evt.color.%s; new Wicket.Ajax.Call().ajax(%s)", toColor, attrs)));
        }

        return script.get();
    }

    private static abstract class ColorChangeAjaxBehavior extends AbstractDefaultAjaxBehavior {

        @Override
        protected void updateAjaxAttributes(final AjaxRequestAttributes attributes) {
            super.updateAjaxAttributes(attributes);

            attributes.getDynamicExtraParameters().add("return [{name: 'color', value: color}]");
        }

        private CharSequence getAttrs() {
            return renderAjaxAttributes(getComponent());
        }

        @Override
        protected void respond(final AjaxRequestTarget target) {
            final IRequestParameters requestParameters = getComponent().getRequest().getRequestParameters();
            final StringValue color = requestParameters.getParameterValue("color");

            onChange(target, color.toString());
        }

        protected abstract void onChange(AjaxRequestTarget target, String color);
    }
}
