/*
 * This file is part routeshare.
 * Copyright PROGS bvba, http://www.progs.be/
 * For licensing details, see LICENSE.txt in the project root.
 */

package be.progs.routeshare.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point and main class for PureGWT example application.
 *
 * @author Joachim Van der Auwera
 */
public class RouteShare implements EntryPoint {

    public void onModuleLoad() {
        RouteShareLayout layout = new RouteShareLayout();
        RootPanel.get().add( layout );
    }
}