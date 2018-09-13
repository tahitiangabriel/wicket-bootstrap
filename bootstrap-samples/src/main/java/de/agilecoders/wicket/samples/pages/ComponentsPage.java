package de.agilecoders.wicket.samples.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.time.Duration;
import org.wicketstuff.annotation.mount.MountPath;

import com.google.common.collect.Lists;

import de.agilecoders.wicket.core.markup.html.bootstrap.badge.BadgeBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.badge.BootstrapBadge;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.Code;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.SplitButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.ProgressBar;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.Stack;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.UpdatableProgressBar;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.AjaxBooleanRadioGroup;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.AjaxBootstrapRadioGroup;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.BooleanRadioGroup;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.EnumRadioChoiceRenderer;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.AjaxBootstrapTabbedPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.ClientSideBootstrapTabbedPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.utilities.BackgroundColorBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.utilities.BorderBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.utilities.ColorBehavior;
import de.agilecoders.wicket.samples.components.basecss.ButtonGroups;
import de.agilecoders.wicket.samples.panels.SimpleCard;

/**
 * The {@code ComponentsPage}
 *
 * @author miha
 */
@MountPath(value = "/components")
public class ComponentsPage extends BasePage {

    private static enum Status {
        submitted, onReview, discarded, accepted
    }

    private Label booleanAjaxSelected;
    private Label enumAjaxSelected;

    /**
     * Construct.
     *
     * @param parameters the current page parameters.
     */
    public ComponentsPage(final PageParameters parameters) {
        super(parameters);

        add(newSplitButton("splitbutton"));

        add(new ButtonGroups("buttonGroups"));

        // create radio buttons
        addRadioGroups();

        // create bootstrap labels
        addLabels();

        addBadges();

        // add example for dropdown button with sub-menu
        // add(newDropDownSubMenuExample());

        add(newTabs("tabs"));

        add(newClientSideTabs("tabsClient"));

        addProgressBars();

        add(newCard("card-demo"));
    }

    private void addProgressBars() {
        final ProgressBar basic = new ProgressBar("basic", Model.of(60));
        add(basic);

        final ProgressBar striped = new ProgressBar("striped", Model.of(20)).striped(true);
        add(striped);

        final ProgressBar animated = new ProgressBar("animated", Model.of(45)).active(true);
        add(animated);

        final ProgressBar labeledProgressBar = new ProgressBar("labeled");
        final Stack labeledStack = new Stack(labeledProgressBar.getStackId(), Model.of(45)) {
            @Override
            protected IModel<String> createLabelModel() {
                return new AbstractReadOnlyModel<String>() {
                    @Override
                    public String getObject() {
                        return String.format("The progress is: %s%%", getModelObject());
                    }
                };
            }
        };
        labeledStack.labeled(true).color(BackgroundColorBehavior.Color.Success);
        labeledProgressBar.addStacks(labeledStack);
        add(labeledProgressBar);

        final ProgressBar stacked = new ProgressBar("stacked");
        add(stacked);
        final Stack stackedStack1 = new Stack(stacked.getStackId(), Model.of(35))
                .color(BackgroundColorBehavior.Color.Success);
        final Stack stackedStack2 = new Stack(stacked.getStackId(), Model.of(20))
                .color(BackgroundColorBehavior.Color.Warning);
        final Stack stackedStack3 = new Stack(stacked.getStackId(), Model.of(10))
                .color(BackgroundColorBehavior.Color.Danger);
        stacked.addStacks(stackedStack1, stackedStack2, stackedStack3);

        final ProgressBar coloredInfo = new ProgressBar("coloredInfo", Model.of(20),
                BackgroundColorBehavior.Color.Info);
        add(coloredInfo);

        final ProgressBar coloredSuccess = new ProgressBar("coloredSuccess", Model.of(40),
                BackgroundColorBehavior.Color.Success);
        add(coloredSuccess);

        final ProgressBar coloredWarning = new ProgressBar("coloredWarning", Model.of(60),
                BackgroundColorBehavior.Color.Warning);
        add(coloredWarning);

        final ProgressBar coloredDanger = new ProgressBar("coloredDanger", Model.of(80),
                BackgroundColorBehavior.Color.Danger);
        add(coloredDanger);

        final UpdatableProgressBar updatableBar = new UpdatableProgressBar("updatable", Model.of(0)) {
            @Override
            protected IModel<Integer> newValue() {
                final int newValue = (value() + 1) % ProgressBar.MAX;
                return Model.of(newValue);
            }
        };
        updatableBar.updateInterval(Duration.seconds(80));
        add(updatableBar);

    }

    private void addRadioGroups() {
        add(new BooleanRadioGroup("boolean", new Model<>(Boolean.FALSE)));

        final IModel<Boolean> booleanAjaxSelectedModel = Model.of(true);
        booleanAjaxSelected = new Label("booleanAjaxSelected", booleanAjaxSelectedModel);
        booleanAjaxSelected.setOutputMarkupId(true);
        add(booleanAjaxSelected);

        add(new AjaxBooleanRadioGroup("booleanAjax", booleanAjaxSelectedModel) {
            @Override
            protected void onSelectionChanged(final AjaxRequestTarget target, final Boolean value) {
                target.add(booleanAjaxSelected);
            }
        });

        final IModel<Status> enumAjaxSelectedModel = Model.of(Status.submitted);
        enumAjaxSelected = new Label("enumAjaxSelected", enumAjaxSelectedModel);
        enumAjaxSelected.setOutputMarkupId(true);
        add(enumAjaxSelected);

        final AjaxBootstrapRadioGroup<Status> enumAjax = new AjaxBootstrapRadioGroup<Status>("enumAjax",
                Arrays.asList(Status.values())) {
            @Override
            protected void onSelectionChanged(final AjaxRequestTarget target, final Status value) {
                target.add(enumAjaxSelected);
            }
        };
        enumAjax.setDefaultModel(enumAjaxSelectedModel);
        enumAjax.setChoiceRenderer(new EnumRadioChoiceRenderer<Status>(Buttons.Type.Success, enumAjax));
        add(enumAjax);
    }

    private void addLabels() {
        final List<BadgeBehavior.Type> types = Lists.newArrayList(BadgeBehavior.Type.values());
        add(new ListView<BadgeBehavior.Type>("badges", types) {
            @Override
            protected void populateItem(final ListItem<BadgeBehavior.Type> item) {
                final BadgeBehavior.Type type = item.getModelObject();

                item.add(new BootstrapBadge("badge", type.cssClassName(), type));

                final Code code = new Code("code",
                        Model.of(String.format("<span class='badge %1$s'>%1$s</span>", type.cssClassName())));
                item.add(code);
            }
        });
    }

    private void addBadges() {
        final List<BadgeBehavior.Type> types = Lists.newArrayList(BadgeBehavior.Type.values());

        add(new ListView<BadgeBehavior.Type>("badge-pills", types) {
            @Override
            protected void populateItem(final ListItem<BadgeBehavior.Type> item) {
                final BadgeBehavior.Type type = item.getModelObject();

                item.add(new Label("name", type.cssClassName()));

                item.add(new BootstrapBadge("badge", 1, type).setPill(true));

                item.add(new Code("code", Model
                        .of(String.format("<span class='badge badge-pills %1$s'>%1$s</span>", type.cssClassName()))));
            }
        });

        final Link<Void> badgeButton = new Link<Void>("button-with-badge") {
            @Override
            public void onClick() {
                // ok
            }
        };
        badgeButton.add(new ButtonBehavior(Buttons.Type.Primary));
        badgeButton.add(new BootstrapBadge("badge", Model.of(1), BadgeBehavior.Type.Light));
        add(badgeButton);
    }

    private Component newCard(final String markupId) {
        return new SimpleCard(markupId).add(new BorderBehavior().type(BorderBehavior.Type.All)
                .color(BorderBehavior.Color.Dark).radius(BorderBehavior.Radius.All)).add(ColorBehavior.success());
    }

    private Component newTabs(final String markupId) {
        return new AjaxBootstrapTabbedPanel<>(markupId,
                Lists.newArrayList(createTab("Section 1"), createTab("Section 2"), createTab("Section 3")));
    }

    private Component newClientSideTabs(final String markupId) {
        return new ClientSideBootstrapTabbedPanel<>(markupId,
                Lists.newArrayList(createTab("Section 1"), createTab("Section 2"), createTab("Section 3")));
    }

    private AbstractTab createTab(final String title) {
        return new AbstractTab(Model.of(title)) {
            @Override
            public WebMarkupContainer getPanel(final String panelId) {
                return new WebMarkupContainer(panelId) {
                    @Override
                    public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
                        replaceComponentTagBody(markupStream, openTag, "<br/>I'm in " + title);
                    }
                };
            }
        };
    }

    /**
     * creates a new split button with some submenu links.
     *
     * @param markupId the markup id of that the split button has to use
     * @return new {@link SplitButton} instance
     */
    private Component newSplitButton(final String markupId) {
        return new SplitButton(markupId, Model.of("Action")) {
            @Override
            protected AbstractLink newBaseButton(final String markupId, final IModel<String> labelModel,
                    final IModel<IconType> iconTypeModel) {
                return new BootstrapAjaxLink<String>(markupId, labelModel, Buttons.Type.Secondary, labelModel) {
                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        target.appendJavaScript("alert('clicked');");
                    }
                }.setIconType(iconTypeModel.getObject());
            }

            @Override
            protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
                final List<AbstractLink> subMenu = new ArrayList<>();
                subMenu.add(new MenuBookmarkablePageLink<Void>(ComponentsPage.class, Model.of("Link 1")));
                subMenu.add(new MenuBookmarkablePageLink<Void>(ComponentsPage.class, Model.of("Link 2")));
                subMenu.add(new MenuBookmarkablePageLink<Void>(ComponentsPage.class, Model.of("Link 3")));

                return subMenu;
            }
        };
    }

    @Override
    protected boolean hasNavigation() {
        return true;
    }
}
