/*
 * This file is part routeshare.
 * Copyright PROGS bvba, http://www.progs.be/
 * For licensing details, see LICENSE.txt in the project root.
 */

package be.progs.routeshare.client;

import com.google.gwt.inject.client.GinModules;
import org.geomajas.plugin.editing.puregwt.client.GeomajasEditorModule;
import org.geomajas.plugin.editing.puregwt.client.GeometryEditorFactory;
import org.geomajas.puregwt.client.GeomajasGinjector;

/**
 * RouteShare Ginjector, adding stuff from editing plug-in.
 *
 * @author Joachim Van der Auwera
 */
@GinModules( GeomajasEditorModule.class )
public interface RoutShareGinjector extends GeomajasGinjector {

    GeometryEditorFactory getGeometryEditorFactory();

}
