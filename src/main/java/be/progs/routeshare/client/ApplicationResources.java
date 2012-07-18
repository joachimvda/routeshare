/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package be.progs.routeshare.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Resource bundle for everything that is used in the showcase examples.
 * 
 * @author Pieter De Graef
 */
public interface ApplicationResources extends ClientBundle {

	@Source("../images/geomajas_logo.png")
	ImageResource geomajasLogo();
}