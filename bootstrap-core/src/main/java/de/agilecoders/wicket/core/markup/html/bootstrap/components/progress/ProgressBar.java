package de.agilecoders.wicket.core.markup.html.bootstrap.components.progress;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.lang.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.agilecoders.wicket.core.markup.html.bootstrap.utilities.BackgroundColorBehavior;
import de.agilecoders.wicket.core.util.Attributes;

/**
 * A component for <a href="http://getbootstrap.com/components/#progress">Progress bars</a>
 */
public class ProgressBar extends GenericPanel<Integer> {
    private static final Logger LOG = LoggerFactory.getLogger(ProgressBar.class);

    public static final int MIN = 0;
    public static final int MAX = 100;

    private final RepeatingView stacks;

    /**
     * Constructor.
     *
     * Creates an empty progress bar.
     * Use {@linkplain #addStacks(Stack...)}
     * to add stacks to it
     *
     * @param id The component id
     * @see #ProgressBar(String, org.apache.wicket.model.IModel,
     *      de.agilecoders.wicket.core.markup.html.bootstrap.utilities.BackgroundColorBehavior.Color, boolean, boolean,
     *      boolean)
     * @see #addStacks(Stack...)
     */
    public ProgressBar(final String id) {
        this(id, null);
    }

    /**
     * Constructor.
     *
     * Creates a progress bar with a stack that uses the given model, default type and is not labeled.
     *
     * @param id The component id
     * @param model The model that will be used for the default stack.
     * @see #ProgressBar(String)
     */
    public ProgressBar(final String id, final IModel<Integer> model) {
        this(id, model, BackgroundColorBehavior.Color.Secondary, false, false, false);
    }

    /**
     * Constructor.
     *
     * Creates a progress bar with a stack that uses the given model, type and is not labeled.
     *
     * @param id The component id
     * @param model The model that will be used for the default stack.
     * @param color The color of the stack
     * @see #ProgressBar(String)
     */
    public ProgressBar(final String id, final IModel<Integer> model, final BackgroundColorBehavior.Color color) {
        this(id, model, color, false, false, false);
    }

    /**
     * Constructor.
     *
     * Creates a progress bar with a stack that uses the given model, default type and it is labeled or not.
     *
     * @param id The component id
     * @param model The model that will be used for the default stack.
     * @param labeled A flag whether the stack should be labeled or not
     * @see #ProgressBar(String)
     */
    public ProgressBar(final String id, final IModel<Integer> model, final boolean labeled) {
        this(id, model, BackgroundColorBehavior.Color.Secondary, labeled, false, false);
    }

    /**
     * Constructor.
     *
     * Creates a progress bar with a stack that uses the given model, type and is labeled or not.
     *
     * @param id The component id
     * @param model The model that will be used for the default stack.
     * @param color The color of the stack
     * @param labeled A flag whether the stack should be labeled or not
     * @see #ProgressBar(String)
     */
    public ProgressBar(final String id, final IModel<Integer> model, final BackgroundColorBehavior.Color color,
            final boolean labeled) {
        this(id, model, color, labeled, false, false);
    }

    /**
     * Constructor.
     *
     * Creates a progress bar with a stack that uses the given model, type, is labeled or not, is animated or not,
     * is striped or not.
     *
     * @param id The component id
     * @param model The model that will be used for the default stack.
     * @param color The color of the stack
     * @param labeled A flag whether the stack should be labeled or not
     * @param animated A flag indicating whether the stack is active/active.
     * @param striped A flag indicating whether the stack is striped.
     * @see #ProgressBar(String)
     */
    public ProgressBar(final String id, final IModel<Integer> model, final BackgroundColorBehavior.Color color,
            final boolean labeled, final boolean animated, final boolean striped) {
        super(id, model);

        Args.notNull(color, "color");

        stacks = new RepeatingView("stacks");
        add(stacks);

        if (model != null) {
            final Stack defaultStack = new Stack(getStackId(), ProgressBar.this.getModel());
            defaultStack.color(color).labeled(labeled).active(animated).striped(striped);
            addStacks(defaultStack);
        }
    }

    /**
     * Generates a safe component id for a stack in this progress bar
     * 
     * @return a component id for a stack
     */
    public String getStackId() {
        return stacks.newChildId();
    }

    /**
     * Adds stacks to this progress bar.
     *
     * @param _stacks The stacks to add
     * @return this instance, for method chaining
     */
    public ProgressBar addStacks(final Stack... _stacks) {
        Args.notNull(_stacks, "_stacks");

        for (final Stack stack : _stacks) {
            stacks.add(stack);
        }

        return this;
    }

    public boolean striped() {
        final Boolean striped = stacks.visitChildren(Stack.class, (c, v) -> {
            final Stack stack = (Stack) c;
            if (stack.striped()) {
                v.stop(true);
            }
        });

        return Objects.booleanValue(striped);
    }

    public ProgressBar striped(final boolean value) {
        stacks.visitChildren(Stack.class, (c, v) -> {
            final Stack stack = (Stack) c;
            stack.striped(value);
        });
        return this;
    }

    public boolean active() {
        final Boolean active = stacks.visitChildren(Stack.class, (c, v) -> {
            final Stack stack = (Stack) c;
            if (stack.active()) {
                v.stop(true);
            }
        });

        return Objects.booleanValue(active);
    }

    public ProgressBar active(final boolean value) {
        stacks.visitChildren(Stack.class, (c, v) -> {
            final Stack stack = (Stack) c;
            stack.active(value);
            if (value) {
                stack.striped(true);
            }
        });

        return this;
    }

    /**
     * Returns whether the progress bar is complete or not.
     *
     * Useful only when used with the default stack, i.e. only when a constructor different than
     * {@linkplain #ProgressBar(String)}
     * is used!
     *
     * @return {@code true} if the progress bar is complete.
     */
    public final boolean complete() {
        return value() == MAX;
    }

    /**
     * Sets a new value for the progress.
     *
     * Useful only when used with the default stack, i.e. only when a constructor different than
     * {@linkplain #ProgressBar(String)}
     * is used!
     *
     * @return this instance, for method chaining.
     */
    public ProgressBar value(final IModel<Integer> value) {
        setDefaultModel(value);
        return this;
    }

    /**
     * Sets a new value for the progress.
     *
     * Useful only when used with the default stack, i.e. only when a constructor different than
     * {@linkplain #ProgressBar(String)}
     * is used!
     *
     * @return this instance, for method chaining.
     */
    public ProgressBar value(Integer value) {
        if (value < MIN) {
            LOG.warn("The provided value '{}' is smaller than the allowed minimum '{}'.", value, MIN);
            value = MIN;
        } else if (value > MAX) {
            LOG.warn("The provided value '{}' is bigger than the allowed maximum'{}'.", value, MAX);
            value = MAX;
        }
        setDefaultModelObject(value);
        return this;
    }

    /**
     * Returns the current value of the progress.
     *
     * Useful only when used with the default stack, i.e. only when a constructor different than
     * {@linkplain #ProgressBar(String)}
     * is used!
     *
     * @return the current value of the progress.
     */
    public Integer value() {
        Integer value = getModelObject();
        if (value < MIN) {
            LOG.warn("The model object '{}' is smaller than the allowed minimum '{}'.", value, MIN);
            value = MIN;
        } else if (value > MAX) {
            LOG.warn("The model object '{}' is bigger than the allowed maximum'{}'.", value, MAX);
            value = MAX;
        }
        return value;
    }

    @Override
    protected void onComponentTag(final ComponentTag tag) {
        super.onComponentTag(tag);

        internalOnComponentTag(tag);
    }

    /**
     * Checks the tag name and sets the Bootstrap CSS classes
     *
     * @param tag The component tag to check and manage
     */
    static void internalOnComponentTag(final ComponentTag tag) {
        if (!"div".equalsIgnoreCase(tag.getName())) {
            LOG.warn("You've added a progress bar component to a non 'div' tag: {}. Changing it to 'div'!",
                    tag.getName());

            tag.setName("div");
        }

        Attributes.addClass(tag, "progress");
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        response.render(JavaScriptHeaderItem.forReference(new UploadProgressBarJavaScriptReference()));
    }

}
