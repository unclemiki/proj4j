/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

/*
 * This file was semi-automatically converted from the public-domain USGS PROJ source.
 */
package org.osgeo.proj4j.proj;

import java.awt.geom.*;

import org.osgeo.proj4j.ProjectionMath;
import org.osgeo.proj4j.Projection;
import org.osgeo.proj4j.ProjectionException;
import org.osgeo.proj4j.units.*;

public class CollignonProjection extends Projection {

	private final static double FXC = 1.12837916709551257390;
	private final static double FYC = 1.77245385090551602729;
	private final static double ONEEPS = 1.0000001;

	public Point2D.Double project(double lplam, double lpphi, Point2D.Double out) {
		if ((out.y = 1. - Math.sin(lpphi)) <= 0.)
			out.y = 0.;
		else
			out.y = Math.sqrt(out.y);
		out.x = FXC * lplam * out.y;
		out.y = FYC * (1. - out.y);
		return out;
	}

	public Point2D.Double projectInverse(double xyx, double xyy, Point2D.Double out) {
		double lpphi = xyy / FYC - 1.;
		if (Math.abs(out.y = 1. - lpphi * lpphi) < 1.)
			out.y = Math.asin(lpphi);
		else if (Math.abs(lpphi) > ONEEPS) throw new ProjectionException("I");
		else	out.y = lpphi < 0. ? -ProjectionMath.HALFPI : ProjectionMath.HALFPI;
		if ((out.x = 1. - Math.sin(lpphi)) <= 0.)
			out.x = 0.;
		else
			out.x = xyx / (FXC * Math.sqrt(out.x));
		out.y = lpphi;
		return out;
	}

	/**
	 * Returns true if this projection is equal area
	 */
	public boolean isEqualArea() {
		return true;
	}

	public boolean hasInverse() {
		return true;
	}

	public String toString() {
		return "Collignon";
	}

}
