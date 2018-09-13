package de.agilecoders.wicket.core.markup.html.bootstrap.components.progress;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import de.agilecoders.wicket.core.markup.html.bootstrap.utilities.BackgroundColorBehavior;
import de.agilecoders.wicket.core.util.Attributes;

/**
 * Represents a stack of the progress bar.
 */
public class Stack extends GenericPanel<Integer> {

    /**
     * A label for the stack's progress
     */
    private Label srOnly;

    /**
     * The color type of the stack
     */
    private final BackgroundColorBehavior backgroundColorBehavior = BackgroundColorBehavior.secondary();

    /**
     * A flag that is used to decide whether to show the label or not.
     * By default the label is not shown.
     */
    private boolean labeled = false;

    /**
     * A flag indicating whether the stack is active/active.
     */
    private boolean animated = false;

    /**
     * A flag indicating whether the stack is striped.
     */
    private boolean striped = false;

    /**
     * Constructor.
     *
     * @param id The component id
     * @param model The progress of this stack
     */
    public Stack(final String id, final IModel<Integer> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        srOnly = new Label("srOnly", createLabelModel());
        add(srOnly);
    }

    public BackgroundColorBehavior.Color color() {
        return backgroundColorBehavior.getColor();
    }

    public Stack color(final BackgroundColorBehavior.Color color) {
        backgroundColorBehavior.color(color);

        return this;
    }

    public boolean labeled() {
        return labeled;
    }

    public Stack labeled(final boolean labeled) {
        this.labeled = labeled;
        return this;
    }

    public boolean striped() {
        return striped;
    }

    public Stack striped(final boolean value) {
        striped = value;
        return this;
    }

    public boolean active() {
        return animated;
    }

    public Stack active(final boolean value) {
        animated = value;
        if (value) {
            striped(true);
        }
        return this;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        if (labeled()) {
            srOnly.setRenderBodyOnly(true);
        }
    }

    @Override
    protected void onComponentTag(final ComponentTag tag) {
        super.onComponentTag(tag);

        final Integer value = getModelObject();
        Attributes.set(tag, "style", String.format("width: %s%%", value));
        Attributes.set(tag, "aria-valuenow", String.valueOf(value));
        Attributes.set(tag, "aria-valuemin", String.valueOf(ProgressBar.MIN));
        Attributes.set(tag, "aria-valuemax", String.valueOf(ProgressBar.MAX));

        if (animated) {
            Attributes.addClass(tag, "progress-bar-active");
        }

        if (striped) {
            Attributes.addClass(tag, "progress-bar-striped");
        }

        Attributes.addClass(tag, color().cssClassName());
    }

    /**
     * Creates a model that is used for the stack's label
     *
     * @return A model with the label
     */
    protected IModel<String> createLabelModel() {
        return Model.of(String.format("%s%%", Stack.this.getModelObject()));
    }
}
