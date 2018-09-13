package de.agilecoders.wicket.samples.panels.validation;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;

/**
 * @author Alexey Volkov
 * @since 18.09.2014
 */
public class SimpleFormPanel extends Panel {

    private static final long serialVersionUID = 1L;

    private class SimpleForm extends BootstrapForm<String> {

        private static final long serialVersionUID = 1L;

        /**
         * @param componentId component id
         */
        public SimpleForm(final String componentId) {
            super(componentId);
            add(new RequiredTextField<>("required", Model.of("")).setLabel(Model.of("Username")));
            add(new PasswordTextField("pass", Model.of("")).setLabel(Model.of("Password")));
            add(new DateTextField("date", Model.of()).setRequired(true).setLabel(Model.of("Date")));
            add(new AjaxButton("submitBtn") {
                @Override
                protected void onSubmit(final AjaxRequestTarget target, final Form<?> paramForm) {
                    super.onSubmit(target, paramForm);
                    SimpleFormPanel.this.onSubmit(target);
                }

                @Override
                protected void onError(final AjaxRequestTarget target, final Form<?> paramForm) {
                    super.onError(target, paramForm);
                    target.add(form);
                }
            });
        }
    }

    private final SimpleForm form;

    /**
     * @param componentId component id
     */
    public SimpleFormPanel(final String componentId) {
        super(componentId);
        form = new SimpleForm("form");
        add(form);
    }

    /**
     * with ajax
     *
     * @return current instance
     */
    public SimpleFormPanel withAjax() {
        form.add(new AjaxFormSubmitBehavior("submit") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onError(final AjaxRequestTarget target) {
                target.add(form);
            }

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                target.add(form);
                SimpleFormPanel.this.onSubmit(target);
            }
        });
        return this;
    }

    protected void onSubmit(final AjaxRequestTarget target) {
    }
}
