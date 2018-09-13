/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.agilecoders.wicket.extensions.wizard;

import org.apache.wicket.extensions.wizard.CancelButton;
import org.apache.wicket.extensions.wizard.FinishButton;
import org.apache.wicket.extensions.wizard.IWizard;
import org.apache.wicket.extensions.wizard.LastButton;
import org.apache.wicket.extensions.wizard.NextButton;
import org.apache.wicket.extensions.wizard.PreviousButton;
import org.apache.wicket.extensions.wizard.WizardButtonBar;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;

/**
 * @author vit
 */
public class BootstrapWizardButtonBar extends WizardButtonBar {

    /**
     * Construct.
     *
     * @param id
     * @param wizard
     */
    public BootstrapWizardButtonBar(final String id, final IWizard wizard) {
        super(id, wizard);
    }

    @Override
    protected CancelButton newCancelButton(final String id, final IWizard wizard) {
        final CancelButton button = super.newCancelButton(id, wizard);
        button.add(new ButtonBehavior(Type.Warning, Buttons.Size.Medium));
        return button;
    }

    @Override
    protected FinishButton newFinishButton(final String id, final IWizard wizard) {
        final FinishButton button = super.newFinishButton(id, wizard);
        button.add(new ButtonBehavior(Type.Success, Buttons.Size.Medium));
        return button;
    }

    @Override
    protected LastButton newLastButton(final String id, final IWizard wizard) {
        final LastButton button = super.newLastButton(id, wizard);
        button.add(new ButtonBehavior(Type.Secondary, Buttons.Size.Medium));
        return button;
    }

    @Override
    protected NextButton newNextButton(final String id, final IWizard wizard) {
        final NextButton button = super.newNextButton(id, wizard);
        button.add(new ButtonBehavior(Type.Secondary, Buttons.Size.Medium));
        return button;
    }

    @Override
    protected PreviousButton newPreviousButton(final String id, final IWizard wizard) {
        final PreviousButton button = super.newPreviousButton(id, wizard);
        button.add(new ButtonBehavior(Type.Secondary, Buttons.Size.Medium));
        return button;
    }
}
