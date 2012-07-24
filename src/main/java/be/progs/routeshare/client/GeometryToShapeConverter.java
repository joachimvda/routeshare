/*
 * This file is part routeshare.
 * Copyright PROGS bvba, http://www.progs.be/
 * For licensing details, see LICENSE.txt in the project root.
 */

package be.progs.routeshare.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.puregwt.client.gfx.GfxUtil;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.shape.Circle;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for the conversion of {@link Geometry} to a {@link Shape} on the map.
 *
 * @author Joachim Van der Auwera
 */
public class GeometryToShapeConverter {

    private static final RouteShareGinjector INJECTOR = GWT.create( RouteShareGinjector.class );

    private GfxUtil gfxUtil;

    private final VectorContainer shapeContainer;

    private final List<Geometry> geometries = new ArrayList<Geometry>();

    private final GeometryEditService editService;

    /**
     * Constructor.
     *
     * @param editService editing service
     */
    public GeometryToShapeConverter( GeometryEditService editService ) {
        this.editService = editService;
        gfxUtil = INJECTOR.getGfxUtil();
        shapeContainer = INJECTOR.getMapPresenter().addWorldContainer();
    }

    /** Process the currently edited geometry into a {@link Shape} and place it into a {@link VectorContainer}. */
    public void processCurrentGeometry() {
        Geometry geometry = null;
        if ( editService.isStarted() ) {
            geometry = editService.stop();
        }
        processGeometry( geometry );
    }

    /**
     * Process the given geometry into a {@link Shape} and place it into a {@link VectorContainer}.
     *
     * @param geometry geometry
     */
    public void processGeometry( Geometry geometry ) {
        if ( null != geometry ) {
            getGeometries().add( geometry );
            Shape shape;
            if ( Geometry.POINT.equals( geometry.getGeometryType() ) ) {
                Coordinate[] coordinates = geometry.getCoordinates();
                shape = new Circle( coordinates[0].getX(), coordinates[0].getY(), 5 );
            } else {
                shape = gfxUtil.toPath( geometry );
            }
            if ( null != shape ) {
                shape.setStrokeWidth( 3 );
                shape.setFillOpacity( 0 );
                shape.addClickHandler( new SelectShapeHandler( geometry, shape ) );
                shape.addMouseOverHandler( new SelectShapeHandler( geometry, shape ) );
                shape.addMouseOutHandler( new SelectShapeHandler( geometry, shape ) );
                if ( shape instanceof Circle ) {
                    shape.setFixedSize( true );
                }
                getShapeContainer().add( shape );
            }
        }
    }

    /**
     * Get geometries.
     *
     * @return list of geometries
     */
    public List<Geometry> getGeometries() {
        return geometries;
    }

    /**
     * Get shape container.
     *
     * @return container which contains the geometries when drawn
     */
    public VectorContainer getShapeContainer() {
        return shapeContainer;
    }

    /**
     * Handler for converting a {@link Shape} back to a editable {@link Geometry}.
     * Also provides the {@link MouseOverHandler} and {@link MouseOutHandler} styles.
     *
     * @author Joachim Van der Auwera
     */
    class SelectShapeHandler implements ClickHandler, MouseOverHandler, MouseOutHandler {

        private final Geometry geometry;
        private final Shape shape;

        /**
         * Constructor.
         *
         * @param geometry geometry
         * @param shape shape
         */
        public SelectShapeHandler( Geometry geometry, Shape shape ) {
            this.geometry = geometry;
            this.shape = shape;
        }

        @Override
        public void onClick( ClickEvent event ) {
            if ( editService.getEditingState() == GeometryEditState.IDLE ) {
                processCurrentGeometry();
                getShapeContainer().remove( shape );
                getGeometries().remove( geometry );
                editService.start( geometry );
            }
        }

        @Override
        public void onMouseOver( MouseOverEvent event ) {
            if ( editService.getEditingState() == GeometryEditState.IDLE ) {
                shape.setStrokeWidth( 4 );
                shape.setStrokeColor( "#F00" );
                if ( Geometry.POLYGON.equals( geometry.getGeometryType() ) ||
                        Geometry.MULTI_POLYGON.equals( geometry.getGeometryType() ) ) {
                    shape.setFillColor( "#F00" );
                    shape.setFillOpacity( 0.3 );
                }
            }
        }

        @Override
        public void onMouseOut( MouseOutEvent event ) {
            if ( editService.getEditingState() == GeometryEditState.IDLE ) {
                shape.setStrokeWidth( 3 );
                shape.setStrokeColor( "#000" );
                shape.setFillOpacity( 0 );
            }
        }
    }

    /** Clear current geometry. */
    public void clear() {
        shapeContainer.clear();
        geometries.clear();
    }

    public GeometryEditService getEditService() {
        return editService;
    }

}
