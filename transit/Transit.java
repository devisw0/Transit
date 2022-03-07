package transit;

import java.util.ArrayList;
import java.util.Arrays;


public class Transit {
	private TNode trainZero; // a reference to the zero node in the train layer

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */ 
	public Transit() { trainZero = null; }

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */
	public Transit(TNode tz) { trainZero = tz; }
	
	/*
	 * Getter method for trainZero
	 *
	 * DO NOT remove from this file.
	 */
	public TNode getTrainZero () {
		return trainZero;
	}

	/**
	 * Makes a layered linked list representing the given arrays of train stations, bus
	 * stops, and walking locations. Each layer begins with a location of 0, even though
	 * the arrays don't contain the value 0. Store the zero node in the train layer in
	 * the instance variable trainZero.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops Int array listing all the bus stops
	 * @param locations Int array listing all the walking locations (always increments by 1)
	 */
	public void makeList(int[] trainStations, int[] busStops, int[] locations) {

	    // UPDATE THIS METHOD

            trainZero = new TNode(0, null, new TNode(0, null, new TNode(0, null, null)));
            
            TNode trainNode = trainZero;
            TNode busNode = trainZero.getDown();
            TNode walkingNode = trainZero.getDown().getDown();
            
            for (int i = 0; i < trainStations.length; i++) {
                trainNode.setNext(new TNode(trainStations[i], null, null));
                trainNode = trainNode.getNext();
            }
            
            for (int i = 0; i < busStops.length; i++) {
                busNode.setNext(new TNode(busStops[i], null, null));
                busNode = busNode.getNext();
            }
            
            for (int i = 0; i < locations.length; i++) {
                walkingNode.setNext(new TNode(locations[i], null, null));
                walkingNode = walkingNode.getNext();
            }
            
            trainNode = trainZero;
            busNode = trainZero.getDown();
            while (trainNode != null) {
                while (busNode != null && busNode.getLocation() < trainNode.getLocation()) {
                    busNode = busNode.getNext();
                }
                if (busNode != null && busNode.getLocation() == trainNode.getLocation()) {
                    trainNode.setDown(busNode);
                }
                trainNode = trainNode.getNext();
            }
            
            busNode = trainZero.getDown();
            walkingNode = trainZero.getDown().getDown();
            while (busNode != null) {
                while (walkingNode != null && walkingNode.getLocation() < busNode.getLocation()) {
                    walkingNode = walkingNode.getNext();
                }
                if (walkingNode != null && walkingNode.getLocation() == busNode.getLocation()) {
                    busNode.setDown(walkingNode);
                }
                busNode = busNode.getNext();
            }
            
	}
	
	/**
	 * Modifies the layered list to remove the given train station but NOT its associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param station The location of the train station to remove
	 */
	public void removeTrainStation(int station) {
	    // UPDATE THIS METHOD
            TNode pre = null;
            TNode trainNode = trainZero;
            while (trainNode != null && trainNode.getLocation() < station) {
                pre = trainNode;
                trainNode = trainNode.getNext();
            }
            if (pre != null && trainNode != null && trainNode.getLocation() == station) {
                pre.setNext(pre.getNext().getNext());
            }
	}

	/**
	 * Modifies the layered list to add a new bus stop at the specified location. Do nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param busStop The location of the bus stop to add
	 */
	public void addBusStop(int busStop) {
	    // UPDATE THIS METHOD
            TNode pre = null;
            TNode busNode = trainZero.getDown();
            while(busNode != null && busNode.getLocation() < busStop) {
                pre = busNode;
                busNode = busNode.getNext();
            }
            if (busNode == null || busNode.getLocation() != busStop) {
               pre.setNext(new TNode(busStop, pre.getNext(), null));
            } else {
                return;
            }
            busNode = pre.getNext();
            
            TNode trainNode = trainZero;
            while (trainNode != null && trainNode.getLocation() < busNode.getLocation()) {
                trainNode = trainNode.getNext();
            }
            if (trainNode != null && trainNode.getLocation() == busNode.getLocation()) {
                trainNode.setDown(busNode);
            }
            
            TNode walkingNode = pre.getDown();
            while (walkingNode != null && walkingNode.getLocation() < busNode.getLocation()) {
                pre = walkingNode;
                walkingNode = walkingNode.getNext();
            }
            if (walkingNode != null && walkingNode.getLocation() == busNode.getLocation()) {
                busNode.setDown(walkingNode);
            } else {
                pre.setNext(new TNode(busStop, pre.getNext(), null));
                busNode.setDown(pre.getNext());
            }
	}
	
	/**
	 * Determines the optimal path to get to a given destination in the walking layer, and 
	 * collects all the nodes which are visited in this path into an arraylist. 
	 * 
	 * @param destination An int representing the destination
	 * @return
	 */
	public ArrayList<TNode> bestPath(int destination) {

	    // UPDATE THIS METHOD
            ArrayList<TNode> list = new ArrayList<TNode>();
            
	    
            TNode node = trainZero;
            TNode pre = null;
            
            while (node != null) {
                while (node != null && node.getLocation() <= destination) {
                    list.add(node);
                    pre = node;
                    node = node.getNext();
                }
                node = pre.getDown();
                pre = node;
            }
            return list;
	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @return A reference to the train zero node of a deep copy
	 */
	public TNode duplicate() {

	    // UPDATE THIS METHOD
            if (trainZero == null) {
                return null;
            }
            
            TNode head1 = trainZero;
            
            TNode head = new TNode(head1.getLocation(), null, null);
            TNode head2 = head;
            
            while (head1 != null) {
                TNode node1 = head1;
                TNode node2 = head2;
                while (node1.getNext() != null) {
                    node1 = node1.getNext();
                    node2.setNext(new TNode(node1.getLocation(), null, null));
                    node2 = node2.getNext();
                }
                
                head1 = head1.getDown();
                if (head1 != null) {
                    head2.setDown(new TNode(head1.getLocation(), null, null));
                    head2 = head2.getDown();
                }
            }
            
            head1 = trainZero;
            head2 = head;
            while (head1 != null) {
                TNode node1 = head1;
                TNode node2 = head2;
                while (node1 != null) {
                    while (node1 != null && node1.getDown() == null){
                        node1 = node1.getNext();
                        node2 = node2.getNext();
                    }
                    if (node1 != null && node1.getDown() != null) {
                        TNode node = head2.getDown();
                        while (node != null && node.getLocation() != node1.getDown().getLocation()) {
                            node = node.getNext();
                        }
                        if (node != null && node.getLocation() == node1.getDown().getLocation()) {
                            node2.setDown(node);
                        }
                        node1 = node1.getNext();
                        node2 = node2.getNext();
                    }
                }
                head1 = head1.getDown();
                head2 = head2.getDown();
            }
            
	    return head;
	}
        

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param scooterStops An int array representing where the scooter stops are located
	 */
	public void addScooter(int[] scooterStops) {

	    // UPDATE THIS METHOD
            
            trainZero.getDown().setDown(new TNode(0, null, trainZero.getDown().getDown()));
            
            TNode scooterNode = trainZero.getDown().getDown();
            
            for (int i = 0; i < scooterStops.length; i++) {
                scooterNode.setNext(new TNode(scooterStops[i], null, null));
                scooterNode = scooterNode.getNext();
            }
            
            TNode busNode = trainZero.getDown();
            scooterNode = trainZero.getDown().getDown();
            while (busNode != null) {
                while (scooterNode != null && scooterNode.getLocation() < busNode.getLocation()) {
                    scooterNode = scooterNode.getNext();
                }
                if (scooterNode != null && scooterNode.getLocation() == busNode.getLocation()) {
                    busNode.setDown(scooterNode);
                }
                busNode = busNode.getNext();
            }
            
            scooterNode = trainZero.getDown().getDown();
            TNode walkingNode = trainZero.getDown().getDown().getDown();
            while (scooterNode != null) {
                while (walkingNode != null && walkingNode.getLocation() < scooterNode.getLocation()) {
                    walkingNode = walkingNode.getNext();
                }
                if (walkingNode != null && walkingNode.getLocation() == scooterNode.getLocation()) {
                    scooterNode.setDown(walkingNode);
                }
                scooterNode = scooterNode.getNext();
            }
            
	}

	/**
	 * Used by the driver to display the layered linked list. 
	 * DO NOT edit.
	 */
	public void printList() {
		// Traverse the starts of the layers, then the layers within
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// Output the location, then prepare for the arrow to the next
				StdOut.print(horizPtr.getLocation());
				if (horizPtr.getNext() == null) break;
				
				// Spacing is determined by the numbers in the walking layer
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print("--");
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print("-");
				}
				StdOut.print("->");
			}

			// Prepare for vertical lines
			if (vertPtr.getDown() == null) break;
			StdOut.println();
			
			TNode downPtr = vertPtr.getDown();
			// Reset horizPtr, and output a | under each number
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				while (downPtr.getLocation() < horizPtr.getLocation()) downPtr = downPtr.getNext();
				if (downPtr.getLocation() == horizPtr.getLocation() && horizPtr.getDown() == downPtr) StdOut.print("|");
				else StdOut.print(" ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
	
	/**
	 * Used by the driver to display best path. 
	 * DO NOT edit.
	 */
	public void printBestPath(int destination) {
		ArrayList<TNode> path = bestPath(destination);
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the number if this node is in the path, otherwise spaces
				if (path.contains(horizPtr)) StdOut.print(horizPtr.getLocation());
				else {
					int numLen = String.valueOf(horizPtr.getLocation()).length();
					for (int i = 0; i < numLen; i++) StdOut.print(" ");
				}
				if (horizPtr.getNext() == null) break;
				
				// ONLY print the edge if both ends are in the path, otherwise spaces
				String separator = (path.contains(horizPtr) && path.contains(horizPtr.getNext())) ? ">" : " ";
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print(separator + separator);
					
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print(separator);
				}

				StdOut.print(separator + separator);
			}
			
			if (vertPtr.getDown() == null) break;
			StdOut.println();

			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the vertical edge if both ends are in the path, otherwise space
				StdOut.print((path.contains(horizPtr) && path.contains(horizPtr.getDown())) ? "V" : " ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
}
