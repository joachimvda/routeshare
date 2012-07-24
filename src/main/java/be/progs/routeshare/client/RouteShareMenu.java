/*
 * This file is part routeshare.
 * Copyright PROGS bvba, http://www.progs.be/
 * For licensing details, see LICENSE.txt in the project root.
 */

package be.progs.routeshare.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.HasResizeHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.plugin.editing.puregwt.client.GeometryEditor;
import org.geomajas.puregwt.client.map.MapPresenter;

/**
 * Menu for the RouteShare application.
 *
 * @author Joachim Van der Auwera
 */
public class RouteShareMenu extends Composite implements HasOpenHandlers<RouteShareMenu>,
        HasCloseHandlers<RouteShareMenu>, HasResizeHandlers {

    private static final RouteShareGinjector INJECTOR = GWT.create( RouteShareGinjector.class );

    /**
     * UI binder definition for the {@link RouteShareMenu} widget.
     *
     * @author Pieter De Graef
     */
    interface RouteShareMenuUiBinder extends UiBinder<Widget, RouteShareMenu> {
        // built by GWT.create
    }

    private static final RouteShareMenuUiBinder UI_BINDER = GWT.create( RouteShareMenuUiBinder.class );

    private boolean open;

    @UiField
    protected HTML title;

    @UiField
    protected SpanElement titleElement;

    @UiField
    protected VerticalPanel contentPanel;

    public RouteShareMenu( MapPresenter mapPresenter ) {
        initWidget( UI_BINDER.createAndBindUi( this ) );
        setWidth( "200px" );

        title.addClickHandler( new ClickHandler() {

            public void onClick( ClickEvent event ) {
                setOpen( !open );
            }
        } );

        GeometryEditor editor = INJECTOR.getGeometryEditorFactory().create( mapPresenter );
        GeometryEditService editService = editor.getEditService();
        GeometryToShapeConverter geometryToShapeConverter = new GeometryToShapeConverter( editService );

        Button createButton = new Button( "Create route" );
        createButton.addClickHandler( new CreateRouteClickHandler( editService, geometryToShapeConverter ) );
        contentPanel.add( createButton );
    }

    @Override
    public HandlerRegistration addCloseHandler( CloseHandler<RouteShareMenu> handler ) {
        return addHandler( handler, CloseEvent.getType() );
    }

    @Override
    public HandlerRegistration addOpenHandler( OpenHandler<RouteShareMenu> handler ) {
        return addHandler( handler, OpenEvent.getType() );
    }

    @Override
    public HandlerRegistration addResizeHandler( ResizeHandler handler ) {
        return addHandler( handler, ResizeEvent.getType() );
    }

    /**
     * Is the drop down panel currently visible or not?
     *
     * @return Is the drop down panel currently visible or not?
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Open or close the legend drop down panel.
     *
     * @param open The new value.
     */
    public void setOpen( boolean open ) {
        this.open = open;
        buildGui();
    }

    /**
     * Apply a new title on the drop down button.
     *
     * @param safeHtml The title in the form of a trusted HTML string.
     */
    public void setTitle( String safeHtml ) {
        titleElement.setInnerHTML( safeHtml );
    }

    private void buildGui() {
        contentPanel.setVisible( open );
        if ( open ) {
            int width = contentPanel.getOffsetWidth();
            setWidth( width + "px" );
        } else {
            setWidth( "100px" );
        }
        ResizeEvent.fire( this, getOffsetWidth(), getOffsetHeight() );
    }

    /**
     * Click handler to start creating a new route.
     */
    private final class CreateRouteClickHandler implements ClickHandler {

        private final GeometryEditService editService;
        private final GeometryToShapeConverter geometryToShapeConverter;

        private CreateRouteClickHandler( GeometryEditService editService,
                GeometryToShapeConverter geometryToShapeConverter ) {
            this.editService = editService;
            this.geometryToShapeConverter = geometryToShapeConverter;
        }

        public void onClick( ClickEvent event ) {
            geometryToShapeConverter.processCurrentGeometry();

            Geometry line = new Geometry( Geometry.LINE_STRING, 0, -1 );
            editService.start( line );

            GeometryIndex index = editService.getIndexService()
                    .create( GeometryIndexType.TYPE_VERTEX, 0 );
            editService.setEditingState( GeometryEditState.INSERTING );
            editService.setInsertIndex( index );
        }

    }
}
