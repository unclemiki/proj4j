package org.osgeo.proj4j;

import java.awt.geom.Point2D;

import org.osgeo.proj4j.util.ProjectionUtil;

public class CoordinateTransformTester {

  boolean verbose = true;
  
  CoordinateSystemFactory csFactory = new CoordinateSystemFactory();

  static final String WGS84_PARAM = "+title=long/lat:WGS84 +proj=longlat +ellps=WGS84 +datum=WGS84 +units=degrees";
  CoordinateSystem WGS84 = csFactory.createFromParameters("WGS84",WGS84_PARAM);

  public CoordinateTransformTester(boolean verbose) {
    this.verbose = verbose;
  }

  Point2D.Double p = new Point2D.Double();
  Point2D.Double p2 = new Point2D.Double();

  public boolean checkTransformFromGeo(String name, double lon, double lat, double x, double y)
  {
    return checkTransformFromGeo(name, lon, lat, x, y, 0.0001);
  }
  
  public boolean checkTransformFromGeo(String name, double lon, double lat, double x, double y, double tolerance)
  {
    CoordinateSystem cs = null;
    // test if name is a PROJ4 spec
    if (name.indexOf("+") >= 0) {
      cs = csFactory.createFromParameters("Anon", name);
    } 
    else {
      cs = csFactory.createFromName(name);
    }
    return checkTransformFromGeo(cs, lon, lat, x, y, tolerance);
  }
  
  public boolean checkTransformFromGeo(CoordinateSystem cs, double lon, double lat, double x, double y, double tolerance)
  {
    if (cs != null) {
      System.out.println(cs + " ( " + cs.getProjection() + " ) - " + cs.getParameterString());
    }
    else {
      System.out.println(cs + " - NOT DEFINED");
    }

    if (cs == null) return false;
    
    boolean ok = checkTransformFromWGS84(cs, lon, lat, x, y, tolerance);
    return ok;
  }
  

  private boolean checkTransformFromWGS84(CoordinateSystem cs, double lon, double lat, double x, double y, double tolerance)
  {
    p.x = lon;
    p.y = lat;
    CoordinateTransformation trans = new CoordinateTransformation(
        WGS84, // CoordinateSystem.CS_GEO
        cs);
    trans.transform(p, p2);
    
    if (verbose) {
      System.out.println(ProjectionUtil.toString(p) 
          + " -> " + ProjectionUtil.toString(p2)
          + " ( expected: " + x + ", " + y + " )"
          );
      System.out.println();
    }
    
    double dx = Math.abs(p2.x - x);
    double dy = Math.abs(p2.y - y);
    
    boolean isInTol =  dx <= tolerance && dy <= tolerance;
   
    if (verbose && ! isInTol) {
      System.out.println(cs.getParameterString());
      System.out.println("FAIL: "
          + ProjectionUtil.toString(p) 
          + " -> " + ProjectionUtil.toString(p2) 
          );
    }

    return isInTol;
  }
  
  private CoordinateSystem createCS(String csSpec)
  {
    CoordinateSystem cs = null;
    // test if name is a PROJ4 spec
    if (csSpec.indexOf("+") >= 0) {
      cs = csFactory.createFromParameters("Anon", csSpec);
    } 
    else {
      cs = csFactory.createFromName(csSpec);
    }
    return cs;
  }
  
  public boolean checkTransform(String cs1, double x1, double y1, 
  		String cs2, double x2, double y2, double tolerance)
  {
    return checkTransform(createCS(cs1), x1, y1, 
    		createCS(cs2), x2, y2, tolerance);
  }
  

  private boolean checkTransform(
  		CoordinateSystem cs1, double x1, double y1, 
  		CoordinateSystem cs2, double x2, double y2, 
  		double tolerance)
  {
    p.x = x1;
    p.y = y1;
    CoordinateTransformation trans = new CoordinateTransformation(
        cs1, cs2);
    trans.transform(p, p2);
    
    if (verbose) {
      System.out.println(ProjectionUtil.toString(p) 
          + " -> " + ProjectionUtil.toString(p2)
          + " ( expected: " + x2 + ", " + y2 + " )"
          );
      System.out.println();
    }
    
    double dx = Math.abs(p2.x - x2);
    double dy = Math.abs(p2.y - y2);
    
    boolean isInTol =  dx <= tolerance && dy <= tolerance;
   
    if (verbose && ! isInTol) {
      System.out.println(cs1.getParameterString());
      System.out.println(cs2.getParameterString());
      System.out.println("FAIL: "
          + ProjectionUtil.toString(p) 
          + " -> " + ProjectionUtil.toString(p2) 
          );
    }

    return isInTol;
  }

}
