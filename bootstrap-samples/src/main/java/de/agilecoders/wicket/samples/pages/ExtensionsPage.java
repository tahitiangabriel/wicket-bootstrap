package de.agilecoders.wicket.samples.pages;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.annotation.mount.MountPath;

import com.google.common.collect.Lists;

import de.agilecoders.wicket.core.markup.html.bootstrap.block.Code;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipConfig;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.TextContentModal;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.behavior.AnimatedBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.behavior.AnimatedBehavior.Animation;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.behavior.Draggable;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.behavior.DraggableConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.behavior.Resizable;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.password.strength.PasswordStrengthBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.password.strength.PasswordStrengthConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.rating.RatingConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.rating.RatingField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.spinner.Spinner;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.spinner.SpinnerConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.html5player.Html5Player;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.html5player.Html5VideoConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.html5player.Video;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.OpenWebIconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.OpenWebIconsCssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.inputmask.InputMaskBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.jqueryui.JQueryUICssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxLink;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.tour.TourBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.tour.TourStep;

/**
 * The {@code ExtensionsPage}
 *
 * @author miha
 */
@MountPath(value = "/extensions")
public class ExtensionsPage extends BasePage {

    private static final Logger LOG = LoggerFactory.getLogger(ExtensionsPage.class);

    /**
     * Construct.
     *
     * @param parameters
     *            the current page parameters.
     */
    public ExtensionsPage(final PageParameters parameters) {
        super(parameters);

        final List<Html5Player.IVideo> videos = Lists.<Html5Player.IVideo> newArrayList(
                new Video("https://archive.org/download/CopyingIsNotTheft/CINT_Nik_H264_720.ogv", "video/ogg"),
                new Video("https://archive.org/download/CopyingIsNotTheft/CINT_Nik_H264_720_512kb.mp4", "video/mp4"));
        add(new Html5Player("video", Model.ofList(videos)));

        add(new Code("video-code",
                Model.of("List<Html5Player.IVideo> videos = Lists.<Html5Player.IVideo>newArrayList(\n"
                        + "\t\tnew Video(\"video.ogv\", \"video/ogg\"),\n"
                        + "\t\tnew Video(\"video.mp4\", \"video/mp4\")\n" + ");\n"
                        + "add(new Html5Player(\"video\", Model.ofList(videos)));")));

        add(new Html5Player("video-custom", Model.ofList(videos),
                new Html5VideoConfig().showProgressBar(false).autoHideControlBar(false)).setWidth(680).setHeight(360));
        add(new Code("video-custom-code",
                Model.of("List<Html5Player.IVideo> videos = Lists.<Html5Player.IVideo>newArrayList(\n"
                        + "\t\tnew Video(\"video.ogv\", \"video/ogg\"),\n"
                        + "\t\tnew Video(\"video.mp4\", \"video/mp4\")\n" + ");\n"
                        + "add(new Html5Player(\"video\", Model.ofList(videos),\n"
                        + "\tnew Html5VideoConfig().showProgressBar(false).autoHideControlBar(false))\n"
                        + "\t\t.setWidth(680).setHeight(360));")));

        final Modal<String> draggableModal = new TextContentModal("draggable-modal", Model.of(
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.")) {
            @Override
            protected WebMarkupContainer createDialog(final String id) {
                final WebMarkupContainer dialog = super.createDialog(id);
                dialog.add(new Draggable(new DraggableConfig().withHandle(".modal-header").withCursor("move")));
                return dialog;
            }

            @Override
            public void renderHead(final IHeaderResponse response) {
                super.renderHead(response);
                response.render(JQueryUICssReference.asHeaderItem());
                response.render(CssHeaderItem
                        .forReference(new CssResourceReference(JQueryUICssReference.class, "css/resizable.css")));
            }
        };
        draggableModal.add(new Resizable().withChildSelector(".modal-content"));
        draggableModal.setUseKeyboard(true).addCloseButton();
        draggableModal.setFadeIn(false);
        final Label draggableButton = new Label("open-draggable", "Open Modal Dialog");
        draggableModal.addOpenerAttributesTo(draggableButton);
        add(draggableModal, draggableButton, new Code("draggable-code", Model.of("")));

        addTour();
        add(new Icon("html5-colored", OpenWebIconType.html5_colored), new Icon("apml", OpenWebIconType.apml),
                new Icon("feed", OpenWebIconType.feed_colored));
        add(new Icon("html5", OpenWebIconType.html5), new Code("openwebicon-code", Model.of(
                "response.render(JavaScriptHeaderItem.forReference(OpenWebIconsCssReference.instance()));\n\nadd(new Icon(\"html5\", OpenWebIconType.html5));")));

        addInputMaskDemo();

        laddaButton();
        confirmationButton();

        spinnerSample();
        ratingSample();
        animationSample();

        pwstrengthSample();
    }

    private void pwstrengthSample() {
        final StatelessForm<Void> pwstrengthForm = new StatelessForm<>("pwstrengthForm");

        final RequiredTextField<String> username = new RequiredTextField<>("username", Model.of(""));
        final PasswordTextField password = new PasswordTextField("password", Model.of(""));
        final PasswordStrengthConfig config = new PasswordStrengthConfig();
        config.withDebug(true).withMinChar(3).withUsernameField(username)
                // .withShowPopover(true)
                .withShowVerdicts(true).withUseVerdictCssClass(true).withShowErrors(true);

        password.add(new PasswordStrengthBehavior(config));

        pwstrengthForm.add(username, password);

        add(pwstrengthForm);
    }

    private void spinnerSample() {
        final NotificationPanel feedback = new NotificationPanel("spinnerFeedback");
        feedback.setOutputMarkupId(true);
        final Number minValue = 20d;
        final SpinnerConfig config = new SpinnerConfig();
        config.withPrefix("pre").withDecimals(2).withPostfix("post").withMin(minValue).withMax(30).withStep(.2)
                .withVerticalbuttons(true).withBootstap(2).withInitVal(24);
        final Spinner<Double> spinner = new Spinner<Double>("spinner", config) {
            @Override
            protected boolean wantMinNotification() {
                return true;
            }

            @Override
            protected void onMin(final AjaxRequestTarget target) {
                super.onMin(target);
                info("Reached the configured min value of " + minValue);
                target.add(feedback);
            }
        };
        add(spinner, feedback);
    }

    private void ratingSample() {
        final NotificationPanel feedback = new NotificationPanel("ratingFeedback");
        feedback.setOutputMarkupId(true);
        final RatingConfig config = new RatingConfig();
        config.withStart(0).withStop(10).withStep(2).withFilled("fa fa-star fa-3x").withEmpty("fa fa-star-o fa-3x");
        final RatingField<String> rating = new RatingField<String>("rating", Model.of(""), config) {

            private static final long serialVersionUID = 1L;

            @Override
            protected boolean wantAjaxNotification() {
                return true;
            }

            @Override
            protected void onChange(final AjaxRequestTarget target) {
                super.onChange(target);
                info("Changed rating to " + getModelObject());
                target.add(feedback);
            }
        };
        add(rating, feedback);
    }

    private void animationSample() {
        final NotificationPanel feedback = new NotificationPanel("animationFeedback");
        feedback.setOutputMarkupId(true).add(new AnimatedBehavior(Animation.bounceInLeft));

        final AjaxLink<Void> animate = new AjaxLink<Void>("startAnimation") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(final AjaxRequestTarget target) {
                info("Nice feedback transition!");
                target.add(feedback);
            }
        };
        add(animate, feedback);
    }

    private void laddaButton() {
        final Form form = new Form("laddaForm");
        add(form);

        final LaddaAjaxButton laddaButton = new LaddaAjaxButton("laddaButton", Model.of("Button, 3secs"), form,
                Buttons.Type.Info) {
            @Override
            protected void onSubmit(final AjaxRequestTarget target, final Form<?> paramForm) {
                super.onSubmit(target, paramForm);

                Duration.seconds(3).sleep();
            }

        };
        laddaButton.setSize(Buttons.Size.Small);

        final LaddaAjaxLink<String> laddaLink = new LaddaAjaxLink<String>("laddaLink", null, Buttons.Type.Success,
                Model.of("Link, 2secs")) {
            @Override
            public void onClick(final AjaxRequestTarget target) {
                Duration.seconds(2).sleep();
            }
        };
        laddaLink.setEffect(LaddaBehavior.Effect.EXPAND_LEFT).setSize(Buttons.Size.Medium);

        form.add(new Code("linkCode", Model.of(
                "laddaLink = new LaddaAjaxLink<String>(\"laddaLink\", Model.of(\"Link, 2secs\"), Buttons.Type.Success) {\n"
                        + "    @Override public void onClick(AjaxRequestTarget target) {\n"
                        + "        Duration.seconds(2).sleep();\n" + "    }\n" + "};\n"
                        + "laddaLink.setEffect(LaddaBehavior.Effect.EXPAND_LEFT).setSize(Buttons.Size.Medium);")));

        form.add(laddaButton, laddaLink);
    }

    private void confirmationButton() {
        final Form form = new Form("confirmationForm");
        add(form);

        final NotificationPanel feedback = new NotificationPanel("confirmationFeedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        final AjaxButton confirmationButton = new AjaxButton("confirmationButton", Model.of("Button")) {
            @Override
            protected void onSubmit(final AjaxRequestTarget target, final Form<?> paramForm) {
                super.onSubmit(target, paramForm);

                info("Invoked button's #onSubmit()!");
                target.add(feedback);
            }
        };
        confirmationButton.add(new ConfirmationBehavior(new ConfirmationConfig().withTitle("My title?")
                .withSingleton(true).withPopout(true).withBtnOkLabel("Confirm")));

        final AjaxLink<String> confirmationLink = new AjaxLink<String>("confirmationLink", Model.of("Link")) {
            @Override
            public void onClick(final AjaxRequestTarget target) {
                info("Invoked link's #onClick()!");
                target.add(feedback);
            }
        };
        confirmationLink.add(new ConfirmationBehavior(new ConfirmationConfig().withBtnCancelLabel("Reject")));

        form.add(new Code("linkCode",
                Model.of("confirmationLink = new AjaxLink<String>(\"confirmationLink\", Model.of(\"Link\")) {\n"
                        + "    @Override public void onClick(AjaxRequestTarget target) {\n"
                        + "        info(\"Invoked link's #onClick()!\");\n" + "        target.add(feedback);\n"
                        + "    }\n" + "};\n"
                        + "confirmationLink.add(new ConfirmationBehavior(new ConfirmationConfig().withBtnCancelLabel(\"Reject\")));\n")));

        form.add(confirmationButton, confirmationLink);
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(OpenWebIconsCssReference.instance()));
        response.render(CssHeaderItem.forReference(FontAwesomeCssReference.instance()));

    }

    private void addInputMaskDemo() {
        final TextField<String> textField = new TextField<>("inputMask", Model.of("l0rdn1kk0n"));
        final InputMaskBehavior inputMask = new InputMaskBehavior("a9aaa9aa9a");
        textField.add(inputMask);
        add(textField);
    }

    /**
     * Demo for TourBehavior. Issue #116
     */
    private void addTour() {
        final RepeatingView view = new RepeatingView("tourDemo");
        add(view);

        final Label stepOne = new Label(view.newChildId(), "Step One");
        view.add(stepOne);

        final Label stepTwo = new Label(view.newChildId(), "Step Two");
        view.add(stepTwo);

        final Label stepThree = new Label(view.newChildId(), "Step Three");
        view.add(stepThree);

        final TourBehavior tourBehavior = new TourBehavior() {
            @Override
            protected CharSequence createExtraConfig() {
                return "    $('<div class=\"alert alert-info\">\\\n"
                        + "      <button type=\"button\" class=\"close\" data-dismiss=\"alert\">&times;</button>\\\n"
                        + "      <a href=\"\" class=\"restart\" style=\"color:white\">Start the demo tour.</a>\\\n"
                        + "      </div>').prependTo(\".tourContent\").alert();\n" + "  \n" + "\n"
                        + "  $(\".restart\").click(function (e) {\n" + "    e.preventDefault();\n"
                        + "    tour.restart();\n" + "    $(this).parents(\".alert\").alert(\"close\");\n" + "  });";
            }
        };
        tourBehavior.addStep(new TourStep().title(Model.of("Step One Title")).element(stepOne)
                .content(Model.of("Some longer help content <strong> for step <span style='color: red'>1</span>.")));
        tourBehavior.addStep(new TourStep().title(new ResourceModel("tour.step.two")).element(stepTwo)
                .placement(TooltipConfig.Placement.left)
                .content(Model.of("Some longer help content <strong> for step <span style='color: red'>2</span>."))
                .backdrop(true));
        tourBehavior.addStep(new TourStep().title(Model.of("Step Three Title")).element(stepThree)
                .placement(TooltipConfig.Placement.left)
                .content(Model.of("Some longer help content <strong> for step <span style='color: red'>3</span>.")));
        view.add(tourBehavior);
    }

    @Override
    protected boolean hasNavigation() {
        return true;
    }
}
