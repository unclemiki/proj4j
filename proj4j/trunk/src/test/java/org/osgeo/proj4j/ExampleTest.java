package org.osgeo.proj4j;

import java.awt.geom.Point2D;

import junit.framework.TestCase;
import junit.textui.TestRunner;

/**
 * Test which serves as an example of using Proj4J.
 * 
 * @author mbdavis
 *
 */
public class ExampleTest extends TestCase
{
  public static void main(String args[]) {
    TestRunner.run(ExampleTest.class);
  }

  public ExampleTest(String name) { super(name); }

  public void test()
  {
    assertTrue(checkTransform("EPSG:2227", -121.3128278, 37.95657778, 6327319.23 , 2171792.15, 0.01 ));
  }
  
  private boolean checkTransform(String csName, double lon, double lat, double x, double y, double tolerance)
  {
  	/*
  	 * Create {@link CoordinateReferenceSystem} & CoordinateTransformation.
  	 * Normally this would be carried out once and reused for all transformations
  	 */ 
  	CRSFactory csFactory = new CRSFactory();
    CoordinateReferenceSystem cs = csFactory.createFromName(csName);
    
    final String WGS84_PARAM = "+title=long/lat:WGS84 +proj=longlat +ellps=WGS84 +datum=WGS84 +units=degrees";
    CoordinateReferenceSystem WGS84 = csFactory.createFromParameters("WGS84",WGS84_PARAM);

    CoordinateTransform trans = new CoordinateTransform(WGS84, cs);
    
    /*
     * Create input and output points.
     * These can be constructed once per thread and reused.
     */ 
    ProjCoordinate p = new ProjCoordinate();
    ProjCoordinate p2 = new ProjCoordinate();
    p.x = lon;
    p.y = lat;
    
    /*
     * Transform point
     */
    trans.transform(p, p2);
    
    /*
     * Compare result to expected, for test purposes
     */ 
    double dx = Math.abs(p2.x - x);
    double dy = Math.abs(p2.y - y);
    boolean isInTol =  dx <= tolerance && dy <= tolerance;
   
    return isInTol;
  }


}
