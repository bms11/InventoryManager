/**
* This class picks an item(s) from the inventory. A thread will wait until
* an item is available to be removed by a picker thread.
*/
public class PickingResult implements Runnable {
	
	private String productId;
	private int amountToPick;
	private Warehouse warehouse;
	
	public PickingResult(String productId, int amountToPick, Warehouse warehouse){
		this.productId = productId;
		this.amountToPick = amountToPick;
		this.warehouse = warehouse;
	}

	@Override
	public void run() {
		
		synchronized(Warehouse.lock){
			// Wait for the item to be restocked
			while(!warehouse.getInventory().containsKey(productId) || warehouse.getInventory().get(productId) - amountToPick < 0){
				try {
					System.out.println("Waiting for Item " + productId  + "'s stock to replenish");
					Warehouse.lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} 
			// Remove the specified amount from the inventory
			warehouse.decreaseInventory(productId, amountToPick);
			System.out.println("Item " + productId + " decreased by " + amountToPick + ". Item " + productId + " is now at " + warehouse.getInventory().get(productId) + " stock.");
		}
		
	}


}
