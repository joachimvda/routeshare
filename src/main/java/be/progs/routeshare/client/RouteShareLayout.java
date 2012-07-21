/*
 * This file is part routeshare.
 * Copyright PROGS bvba, http://www.progs.be/
 * For licensing details, see LICENSE.txt in the project root.
 */

package be.progs.routeshare.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.puregwt.client.GeomajasGinjector;
import org.geomajas.puregwt.client.map.MapPresenter;

/**
 * RouteShare layout.
 *
 * @author Joachim Van der Auwera
 */
public class RouteShareLayout extends Composite {

    /**
     * UI binder interface for the application layout.
     *
     * @author Joachim Van der Auwera
     */
    interface ApplicationLayoutUiBinder extends UiBinder<Widget, RouteShareLayout> {

    }

    private static final ApplicationLayoutUiBinder UI_BINDER = GWT.create( ApplicationLayoutUiBinder.class );

    private static final GeomajasGinjector INJECTOR = GWT.create( GeomajasGinjector.class );

    @UiField
    protected SimplePanel contentPanel;

    public RouteShareLayout() {
        initWidget( UI_BINDER.createAndBindUi( this ) );
        MapPresenter mapPresenter = INJECTOR.getMapPresenter();
        mapPresenter.initialize( "puregwt-app", "mapOsm" );
        mapPresenter.getMapRenderer().setAnimationMillis( 300 );
        ResizableMapLayout mapLayout = new ResizableMapLayout( mapPresenter );
        contentPanel.setWidget( mapLayout );
    }
}