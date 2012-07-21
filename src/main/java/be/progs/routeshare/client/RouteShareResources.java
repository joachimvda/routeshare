/*
 * This file is part routeshare.
 * Copyright PROGS bvba, http://www.progs.be/
 * For licensing details, see LICENSE.txt in the project root.
 */

package be.progs.routeshare.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Resource bundle for everything that is used in the application.
 *
 * @author Joachim Van der Auwera
 */
public interface RouteShareResources extends ClientBundle {

    @Source( "../images/geomajas_logo.png" )
    ImageResource geomajasLogo();
}