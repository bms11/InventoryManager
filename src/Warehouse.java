import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
/**
 * This class picks and restocks items from the hashmap inventory. A call to either pickProduct or restockProduct creates a new 
 * thread and executes modifications concurrently to the inventory hashmap. Each thread represents either a picker or stocker working 
 * concurrently from many terminals.
 * 
 * This program utilizes the producer/consumer model for multithreading.
 */
public class Warehouse {

	/** 
	 * Hashmap representing the inventory {product id => Amount in inventory}
	 */
	private HashMap<String, Integer> inventory;
	private ArrayList<Thread> threads;
	
	/**
	 *  Lock for consumer/producer threading
	 */
	protected final static Object lock = new Object();
	
	/**
	 * Constructor
	 */
	public Warehouse() {
		inventory = new HashMap<String, Integer>();
		setThreads(new ArrayList<Thread>());
	}

	/** 
	 * Produces a thread to reduce amount of products in stock of the given product ID
	 * @param productId - productId to pick
	 * @param amountToPick - amount to remove from the inventory
	 */
	public PickingResult pickProduct(String productId, int amountToPick) {
		
		PickingResult pick = new PickingResult(productId, amountToPick, this);
		
		Thread pickThread = new Thread(pick);
		pickThread.start();
		getThreads().add(pickThread);
		
		return pick;
	}
	
	/** 
	 * Restocks amount of products of the given product ID. 
	 * @param productId - productId to restock
	 * @param amountToRestock - amount to restock
	 */
	public RestockingResult restockProduct(String productId, int amountToRestock) {
		
		RestockingResult restock = new RestockingResult(productId, amountToRestock, this);
		Thread restockThread = new Thread(restock);
		
		restockThread.start();
		getThreads().add(restockThread);
		
		return restock;
	}
	
	/**
	 * Joins all created threads
	 */
	public void joinThreads(){
		
		for(Thread thread : getThreads()){
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		setThreads(new ArrayList<Thread>());
		
	}
	
	/*****************GETTERS AND SETTERS*****************/

	public ArrayList<Thread> getThreads() {
		return threads;
	}

	public void setThreads(ArrayList<Thread> threads) {
		this.threads = threads;
	}

	public HashMap<String, Integer> getInventory() {
		return inventory;
	}
	
	/*****************INVENTORY MUTATORS*****************/

	/** 
	 * Increase the item stock in the inventory of the given ID and create a new productID entry if not present.
	 * @param productId - product to increase
	 * @param amount - amount to increase by
	 */
	public void increaseInventory(String productId, int amount) {
		inventory.putIfAbsent(productId, 0);
		inventory.put(productId, inventory.get(productId) + amount);
	}
	
	/** 
	 * Reduce the item stock in the inventory.
	 * @param productId - product to decrease
	 * @param - amount to decrease by
	 */
	public void decreaseInventory(String productId, int amount) {
		if(inventory.get(productId) != null) {
			inventory.put(productId, inventory.get(productId) - amount);
		} 
	}
	
	
	/****** MAIN ******/
	public static void main(String[] args){
		Warehouse warehouse = new Warehouse();
		
		// System runs until prompted to quit
		while(true){
			
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			
			System.out.println("Press 1 to restock, 2 to pick, or 3 to quit.");
			int choice = scan.nextInt();
			
			if(choice == 1){ // Restock
				
				System.out.println("Enter the product ID you'd like to restock.");
				String productId = scan.next();
				System.out.println("Enter the amount you'd like to restock.");
				int amount = scan.nextInt();
				
				warehouse.restockProduct(productId, amount);
				
			} else if(choice == 2) { // Pick
				
				System.out.println("Enter the product ID you'd like to pick.");
				String productId = scan.next();
				System.out.println("Enter the amount you'd like to pick.");
				int amount = scan.nextInt();
				
				warehouse.pickProduct(productId, amount);
				
			} else if(choice == 3){ // Quit
				
				break;
				
			} else { // Invalid Choice
			
				System.out.println("Not a valid choice.");
			}
			
		}
		
		warehouse.joinThreads();
	}

}
