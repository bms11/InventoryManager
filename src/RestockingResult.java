/** 
*	This class restocks items to the inventory when prompted by a restocker.
*/
public class RestockingResult implements Runnable {
	private String productId;
	private int amountToRestock;
	private Warehouse warehouse;
	
	public RestockingResult(String productId, int amountToRestock, Warehouse warehouse){
		this.productId = productId;
		this.amountToRestock = amountToRestock;
		this.warehouse = warehouse;
	}

	@Override
	public void run() {
		synchronized(Warehouse.lock){
			warehouse.increaseInventory(productId, amountToRestock);
			System.out.println("Item " + productId + " restocked by " + amountToRestock + ". Item " + productId + " is now at " + warehouse.getInventory().get(productId) + " stock.");
			Warehouse.lock.notifyAll();
		}
	}

}
