package com.shipping.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.shipping.service.ShippingService;

@RunWith(Parameterized.class)
public class ShippingTest {
	ShippingService service = null;
	
	@Parameters
    public static Collection<Object[]> testData() {
    	List<Object[]> rawData = Arrays.asList(new Object[][] {
            {2.0, 100, 1000.0},
            {10.0, 100, 1500.0},
           {25.0, 100, 2000.0},
           {10.0, 1, 15.0},
           {100.0, 50, 1000.0},
            {0.0, 100,1500.0},
            {-1.0, 100,0.0},
            {10.0, -1,0.0},
            {"10", -1,0.0}
            
        });
        
        List<Object[]> validData = new ArrayList<>();
        for (Object[] row : rawData) {
            if (isValid(row)) {
                validData.add(row);
            } else {
                System.err.println("Skipping invalid dataset: " + Arrays.toString(row));
            }
        }
        return validData;
    }
	
    private static boolean isValid(Object[] row) {
        if (row.length != 3) return false;
        if (!(row[0] instanceof Double)) return false;
        if (!(row[1] instanceof Integer)) return false;
        if (!(row[2] instanceof Double)) return false;
        return true;
    }
    
    
    double weight;
    int distance;
    double expectedValue;
    
    public ShippingTest(double weight, int distance, double expectedValue) throws Exception{
    	try {
	        this.weight = weight;
	        this.distance = distance;
	        this.expectedValue = expectedValue;
    	}catch(ClassCastException ex) {
    		System.out.println(ex.getMessage() + "- COnstructer");
    		throw new AssumptionViolatedException("Invalid data type: " + ex.getMessage());
    	}
        
    }
	
	
	@BeforeClass
	public static void first() {
		
		System.out.println("Before All");
	}

	@AfterClass
	public static void last() {
		//bill = new EbillService();
		System.out.println("After All");
	}
	
	@Before
	public void firstEvery() {
		try {
		service = new ShippingService(this.weight, this.distance);
		System.out.println("Before11");
		}catch(Exception ex) {
			System.out.println(ex.getMessage() + "- firstEvery");
			assertEquals("argument type mismatch", ex.getMessage());
		}
	}

	@After
	public void lastEvery() {
		
		System.out.println("After");
	}

	@Test
	public void testCalculateShippingCost_LightPackage() {
		try {
			assertEquals(this.expectedValue, service.calculateShippingCost(), 0.001);
		}catch(Exception ex) {
			System.out.println(ex.getMessage() + "- test case");
			assertEquals("argument type mismatch", ex.getMessage());
		}
	}

	/*@Test
	public void testCalculateShippingCost_MediumPackage() {
		service = new ShippingService(10, 100);
		assertEquals(1500, service.calculateShippingCost(), 0.001);
	}

	@Test
	public void testCalculateShippingCost_HeavyPackage() {
		service = new ShippingService(25, 100);
		assertEquals(2000, service.calculateShippingCost(), 0.001);
	}

	@Test
	public void testCalculateShippingCost_MinimalDistance() {
		service = new ShippingService(10, 1); // Minimal distance with a medium weight package
		assertEquals(15, service.calculateShippingCost(), 0.001);
	}

	@Test
	public void testCalculateShippingCost_VeryHeavyPackage() {
		service = new ShippingService(100, 50); // Very heavy package with moderate distance
		assertEquals(1000, service.calculateShippingCost(), 0.001);
	}

	@Test
	public void testCalculateShippingCost_ZeroWeightPackage() {
		service = new ShippingService(0, 100); // Edge case: zero weight (should handle as the lightest package)
		assertEquals(1000, service.calculateShippingCost(), 0.001);
	}*/
}