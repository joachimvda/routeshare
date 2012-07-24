/*
 * This file is part routeshare.
 * Copyright PROGS bvba, http://www.progs.be/
 * For licensing details, see LICENSE.txt in the project root.
 */

package be.progs.routeshare.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.puregwt.client.map.MapGadget;
import org.geomajas.puregwt.client.map.MapPresenter;

/**
 * Display button to create a new route on the map.
 *
 * @author Joachim Van der Auwera
 */
public class MenuMapGadget implements MapGadget {

    private MapPresenter mapPresenter;

    private RouteShareMenu menu;

    private int closeDelay = 3000;

    private Timer timer;

    @Override
    public Widget asWidget() {
        if ( menu == null ) {
            menu = new RouteShareMenu( mapPresenter );

            // Stop propagation of mouse events to the map:
            menu.addDomHandler( new MouseDownHandler() {

                public void onMouseDown( MouseDownEvent event ) {
                    event.stopPropagation();
                }
            }, MouseDownEvent.getType() );
            menu.addDomHandler( new ClickHandler() {

                public void onClick( ClickEvent event ) {
                    event.stopPropagation();
                }
            }, ClickEvent.getType() );
            menu.addDomHandler( new MouseUpHandler() {

                public void onMouseUp( MouseUpEvent event ) {
                    event.stopPropagation();
                }
            }, MouseUpEvent.getType() );
            menu.addDomHandler( new DoubleClickHandler() {

                public void onDoubleClick( DoubleClickEvent event ) {
                    event.stopPropagation();
                }
            }, DoubleClickEvent.getType() );

            // Install a timer for automatic closing when the mouse leaves us:
            menu.addDomHandler( new MouseOutHandler() {

                public void onMouseOut( MouseOutEvent event ) {
                    if ( menu.isOpen() && closeDelay > 0 ) {
                        timer = new Timer() {

                            public void run() {
                                menu.setOpen( false );
                            }
                        };
                        timer.schedule( closeDelay );
                    }
                }
            }, MouseOutEvent.getType() );
            menu.addDomHandler( new MouseOverHandler() {

                public void onMouseOver( MouseOverEvent event ) {
                    if ( timer != null ) {
                        timer.cancel();
                    }
                }
            }, MouseOverEvent.getType() );
        }
        return menu;
    }

    @Override
    public void beforeDraw( MapPresenter mapPresenter ) {
        this.mapPresenter = mapPresenter;
    }

    @Override
    public Layout.Alignment getHorizontalAlignment() {
        return Layout.Alignment.END;
    }

    @Override
    public Layout.Alignment getVerticalAlignment() {
        return Layout.Alignment.BEGIN;
    }

    @Override
    public int getHorizontalMargin() {
        return 15;
    }

    @Override
    public int getVerticalMargin() {
        return 15;
    }

    /**
     * Get the number of milliseconds this drop down will wait before automatically closing when the mouse moves out of
     * view. If the mouse comes back within the delay time, the drop down will stay open.
     *
     * @return The number of milliseconds this drop down will wait before automatically closing.
     */
    public int getCloseDelay() {
        return closeDelay;
    }

    /**
     * Set the number of milliseconds this drop down will wait before automatically closing when the mouse moves out of
     * view. If the mouse comes back within the delay time, the drop down will stay open.
     *
     * @param closeDelay The automatic closing delay, expressed in milliseconds.
     */
    public void setCloseDelay( int closeDelay ) {
        this.closeDelay = closeDelay;
    }

    /**
     * Apply a new title on the drop down button.
     *
     * @param safeHtml The title in the form of a trusted HTML string.
     */
    public void setTitle( String safeHtml ) {
        if ( menu == null ) {
            throw new IllegalStateException( "Make sure this gadget has been added to the map before calling this"
                    + " method." );
        }
        menu.setTitle( safeHtml );
    }

    @Override
    public int getWidth() {
        return asWidget().getOffsetWidth();
    }

    @Override
    public int getHeight() {
        return asWidget().getOffsetHeight();
    }

    @Override
    public void setWidth( int width ) {
        asWidget().setWidth( width + "px" );
    }

    @Override
    public void setHeight( int height ) {
        asWidget().setHeight( height + "px" );
    }

    @Override
    public void setTop( int top ) {
        asWidget().getElement().getStyle().setTop( top, Style.Unit.PX );
    }

    @Override
    public void setLeft( int left ) {
        asWidget().getElement().getStyle().setLeft( left, Style.Unit.PX );
    }

    @Override
    public void addResizeHandler( ResizeHandler resizeHandler ) {
        asWidget().addHandler( resizeHandler, ResizeEvent.getType() );
    }
}
