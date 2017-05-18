import static org.junit.Assert.*;

import org.junit.Test;

public class InventoryTests {
	


	@Test
	public void restockAndPickToZeroTest() {
		
		Warehouse w = new Warehouse();
		w.restockProduct("1", 10);
		w.pickProduct("1", 10);
		w.restockProduct("2", 20);
		w.pickProduct("2", 18);
		w.pickProduct("2", 1);
		w.pickProduct("2", 1);
		
		w.joinThreads();
		
		assertTrue(w.getInventory().get("1") == 0);
		assertTrue(w.getInventory().get("2") == 0);
	}

	@Test
	public void manyItemsTest(){
		
		Warehouse w = new Warehouse();

		w.restockProduct("1", 10);
		w.restockProduct("2", 20);
		w.restockProduct("3", 40);
		w.restockProduct("4", 10);
		w.restockProduct("5", 20);
		w.restockProduct("6", 100);
		w.restockProduct("7", 10);
		w.restockProduct("8", 20);
		w.restockProduct("9", 40);
		w.restockProduct("10", 10);
		w.restockProduct("11", 20);
		w.restockProduct("12", 100);
		w.restockProduct("13", 10);
		w.restockProduct("14", 20);
		w.restockProduct("15", 40);
		w.restockProduct("16", 10);
		w.restockProduct("17", 10);
		w.restockProduct("18", 20);
		w.restockProduct("19", 40);
		w.restockProduct("20", 10);
		w.restockProduct("21", 20);
		w.restockProduct("22", 100);
		
		w.joinThreads();
		
		assertTrue(w.getInventory().get("1") == 10);
		assertTrue(w.getInventory().get("2") == 20);
		assertTrue(w.getInventory().get("3") == 40);
		assertTrue(w.getInventory().get("4") == 10);
		assertTrue(w.getInventory().get("5") == 20);
		assertTrue(w.getInventory().get("6") == 100);
		assertTrue(w.getInventory().get("7") == 10);
		assertTrue(w.getInventory().get("8") == 20);
		assertTrue(w.getInventory().get("9") == 40);
		assertTrue(w.getInventory().get("10") == 10);
		assertTrue(w.getInventory().get("11") == 20);
		assertTrue(w.getInventory().get("12") == 100);
		assertTrue(w.getInventory().get("13") == 10);
		assertTrue(w.getInventory().get("14") == 20);
		assertTrue(w.getInventory().get("15") == 40);
		assertTrue(w.getInventory().get("16") == 10);
		assertTrue(w.getInventory().get("17") == 10);
		assertTrue(w.getInventory().get("18") == 20);
		assertTrue(w.getInventory().get("19") == 40);
		assertTrue(w.getInventory().get("20") == 10);
		assertTrue(w.getInventory().get("21") == 20);
		assertTrue(w.getInventory().get("22") == 100);

	}
	
	@Test
	public void notInStockTest(){
		
		Warehouse w = new Warehouse();
		
		w.pickProduct("1", 5);
		w.pickProduct("2", 10);
		w.pickProduct("3", 15);
		w.restockProduct("1", 10);
		w.restockProduct("2", 20);
		w.restockProduct("3", 40);
		
		w.joinThreads();
		
		assertTrue(w.getInventory().get("1") == 5);
		assertTrue(w.getInventory().get("2") == 10);
		assertTrue(w.getInventory().get("3") == 25);
		

	}
	
	@Test
	public void graduallyRestockTest(){
		
		Warehouse w = new Warehouse();
		

		w.pickProduct("3", 15);
		
		for(int i = 0; i < 3; i++)
			w.restockProduct("3", 5);
		
		w.joinThreads();

		assertTrue(w.getInventory().get("3") == 0);

	}
	
	@Test
	public void multiplePickersWaitingTest(){
		
		Warehouse w = new Warehouse();

		w.pickProduct("3", 15);
		w.pickProduct("3", 5);
		w.pickProduct("3", 5);
		w.pickProduct("3", 5);
		
		for(int i = 0; i < 6; i++)
			w.restockProduct("3", 5);
		
		w.joinThreads();

		assertTrue(w.getInventory().get("3") == 0);

	}
	
	@Test
	public void pickZeroTest(){
		
		Warehouse w = new Warehouse();
		
		for(int i = 0; i < 6; i++)
			w.restockProduct("3", 5);
		
		w.pickProduct("3", 0);
		
		w.joinThreads();

		assertTrue(w.getInventory().get("3") == 30);

	}
	
	@Test
	public void stressTest(){
		
		Warehouse w = new Warehouse();
		
		for(int i = 0; i < 1000; i++)
			w.restockProduct(""+i, 1000);
		
		for(int i = 0; i < 1000; i++)
			w.pickProduct(""+i, 1000);
		
		w.joinThreads();
		
		for(int i = 0; i < 1000; i++)
			assertTrue(w.getInventory().get(""+i) == 0);

	}
}
