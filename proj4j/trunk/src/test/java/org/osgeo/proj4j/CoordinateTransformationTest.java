package org.osgeo.proj4j;

import junit.framework.TestCase;
import junit.textui.TestRunner;

/**
 * Tests correctness and accuracy of Coordinate System transformations.
 * 
 * @author Martin Davis
 *
 */
public class CoordinateTransformationTest extends TestCase
{
	static boolean debug = true;
	
	CRSFactory csFactory = new CRSFactory();
  
  CoordinateTransformTester tester = new CoordinateTransformTester(true);
	
  public static void main(String args[]) {
    TestRunner.run(CoordinateTransformationTest.class);
  }

  public CoordinateTransformationTest(String name) { super(name); }

  public void xtestFirst()
  {
    checkTransformFromGeo("EPSG:3031",    0, -75, 0, 1638783.238407   );
  }
  public void testLambertConformalConic()
  {
    // Landon's test pt 
    checkTransformFromGeo("EPSG:2227", -121.3128278, 37.95657778, 6327319.23 , 2171792.15, 0.01 );
  }
  
  public void testStereographic()
  {
    checkTransformFromGeo("EPSG:3031",    0, -75, 0, 1638783.238407   );
    checkTransformFromGeo("EPSG:3031",    -57.65625, -79.21875, -992481.633786, 628482.06328   );
  }
  
  public void testUTM()
  {
    checkTransformFromGeo("EPSG:23030",    -3, 49.95,        				500000, 5533182.925903  );
    checkTransformFromGeo("EPSG:32615",    -93, 42,        					500000, 4649776.22482 );
    checkTransformFromGeo("EPSG:32612",    -113.109375, 60.28125, 	383357.429537, 6684599.06392 );
  }
  
  public void testMercator()
  {
    //    google
    checkTransformFromGeo("EPSG:3785",     -76.640625, 49.921875,  -8531595.34908, 6432756.94421   );  
  }
  public void testAlbersEqualArea()
  {
    checkTransformFromGeo("EPSG:3005",     -126.54, 54.15,   964813.103719, 1016486.305862  );
    // # NAD83(CSRS) / BC Albers
    checkTransformFromGeo("EPSG:3153",     -127.0, 52.11,  931625.9111828626, 789252.646454557 );
  }
  
  public void testEPSG_4326()
  {
  	checkTransformAndInverse(
  			"EPSG:4326", -126.54, 54.15,  
  			"EPSG:3005", 964813.103719, 1016486.305862, 
  			0.0001);
  	
  	checkTransformAndInverse(
    		"EPSG:32633",  249032.839239894, 7183612.30572229, 
    		"EPSG:4326", 9.735465995810884, 64.68347938257097, 
    		0.000001 );
  }
  
  public void testSouth()
  {
    // <2736> +proj=utm +zone=36 +south +ellps=clrk66 +units=m +no_defs  <>
    //from spatialreference.org
    checkTransformFromGeo("EPSG:2736",     33.115, -19.14, 512093.765437, 7883804.406911    );
    // from proj4.js - suspect this expected result is bogus
    //checkTransformFromGeo("EPSG:2736",     34.0, -21.0, 603933.40, 7677505.64    );
  }

  public void testParams()
  {
    checkTransformFromGeo("+proj=aea +lat_1=50 +lat_2=58.5 +lat_0=45 +lon_0=-126 +x_0=1000000 +y_0=0 +ellps=GRS80 +units=m ", 
        -127.0, 52.11,  931625.9111828626, 789252.646454557, 0.0001);
  }
  
  public void XtestUndefined()
  {
 	 

    //runInverseTransform("EPSG:27492",    25260.493584, -9579.245052,    -7.84, 39.58);
    //runInverseTransform("EPSG:27563",    653704.865208, 176887.660037,    3.005, 43.89);
    //runInverseTransform("EPSG:54003",    1223145.57,6491218.13,-6468.21,    11.0, 53.0);
    
    
//    runTransform("EPSG:31467",   9, 51.165,       3500072.082451, 5670004.744777   );

    checkTransformFromGeo("EPSG:54008",    11.0, 53.0,     738509.49,5874620.38 );
    
    
    checkTransformFromGeo("EPSG:102026",   40.0, 40.0,     3000242.40, 5092492.64);
    checkTransformFromGeo("EPSG:54032",    -127.0, 52.11,  -4024426.19, 6432026.98 );
    
    checkTransformFromGeo("EPSG:42304",    -99.84375, 48.515625,   -358185.267976, -40271.099023   );
    checkTransformFromGeo("EPSG:42304",    -99.84375, 48.515625,   -358185.267976, -40271.099023  );
//    runInverseTransform("EPSG:28992",    148312.15, 457804.79, 698.48,    5.29, 52.11);
  }
  
  void checkTransformFromGeo(String code, double lon, double lat, double x, double y)
  {
    assertTrue(tester.checkTransformFromGeo(code, lon, lat, x, y, 0.0001));
  }
  void checkTransformFromGeo(String code, double lon, double lat, double x, double y, double tolerance)
  {
    assertTrue(tester.checkTransformFromGeo(code, lon, lat, x, y, tolerance));
  }
  void checkTransform(
  		String cs1, double x1, double y1, 
  		String cs2, double x2, double y2, 
  		double tolerance)
  {
    assertTrue(tester.checkTransform(cs1, x1, y1, cs2, x2, y2, tolerance));
  }
  void checkTransformAndInverse(
  		String cs1, double x1, double y1, 
  		String cs2, double x2, double y2, 
  		double tolerance)
  {
    assertTrue(tester.checkTransform(cs1, x1, y1, cs2, x2, y2, tolerance, true));
  }
 
}
