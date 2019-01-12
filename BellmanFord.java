//import java.io.*;
import java.util.*;
public class BellmanFord{

	
	/**
	 * Utility class. Don't use.
	 */
	public class BellmanFordException extends Exception{
		private static final long serialVersionUID = -4302041380938489291L;
		public BellmanFordException() {super();}
		public BellmanFordException(String message) {
			super(message);
		}
	}
	
	/**
	 * Custom exception class for BellmanFord algorithm
	 * 
	 * Use this to specify a negative cycle has been found 
	 */
	public class NegativeWeightException extends BellmanFordException{
		private static final long serialVersionUID = -7144618211100573822L;
		public NegativeWeightException() {super();}
		public NegativeWeightException(String message) {
			super(message);
		}
	}
	
	/**
	 * Custom exception class for BellmanFord algorithm
	 *
	 * Use this to specify that a path does not exist
	 */
	public class PathDoesNotExistException extends BellmanFordException{
		private static final long serialVersionUID = 547323414762935276L;
		public PathDoesNotExistException() { super();} 
		public PathDoesNotExistException(String message) {
			super(message);
		}
	}
	
    private int[] distances = null;
    private int[] predecessors = null;
    private int source;

    BellmanFord(WGraph g, int source) throws BellmanFordException{
    	/* Constructor, input a graph and a source
         * Computes the Bellman Ford algorithm to populate the
         * attributes 
         *  distances - at position "n" the distance of node "n" to the source is kept
         *  predecessors - at position "n" the predecessor of node "n" on the path
         *                 to the source is kept
         *  source - the source node
         *
         *  If the node is not reachable from the source, the
         *  distance value must be Integer.MAX_VALUE
         *  
         *  When throwing an exception, choose an appropriate one from the ones given above
         */    
    	//Do following for each edge u-v
   // 	……If dist[v] > dist[u] + weight of edge uv, then “Graph contains negative weight cycle”
 
    	ArrayList<Edge> e = g.getEdges();
    	distances = new int [g.getNbNodes()];
    	predecessors = new int [g.getNbNodes()];
    	predecessors[source] = -1; //i think you do this?
    	this.source = source;
    	int current = source;
    	
    	for(int i=0; i<distances.length; i++)
    	{
    		distances[i] = Integer.MAX_VALUE;
    		predecessors[i] = -2; //-2 indicates the node does not have any parental lineage
    	}
    	
    distances[source] = 0;
	predecessors[source] = -1; //i think you do this?
    	
    	for(int i=0; i<g.getNbNodes()-1; i++) //for loop v-1 times
    	{
    		current = source;
    		int index = 0;
    		int [] nodes = new int[g.getNbNodes()];
    		nodes[0] = source;
    		
    	
    		for(int j=0; j<g.getNbNodes(); j++) //going to each node
    		{
    			
    			for(int k=0; k<e.size(); k++) 
    			{
    				if(e.get(k).nodes[0] == current 
    						&& distances[e.get(k).nodes[1]] > (distances[e.get(k).nodes[0]] + e.get(k).weight)
    						&& distances[e.get(k).nodes[0]] != Integer.MAX_VALUE) 
    				{
    					distances[e.get(k).nodes[1]] = (distances[e.get(k).nodes[0]] + e.get(k).weight);
    					predecessors[e.get(k).nodes[1]] = e.get(k).nodes[0];
    					
    				}
    				
    			}
    			
    			//current++;
    			
    			for(int l=0; l< e.size(); l++) //choosing next node
    			{
    				int n1 = e.get(l).nodes[0];
    				int n2 = e.get(l).nodes[1];
    				boolean b1 = false;
    				boolean b2 = false;
    				
    				for(int m = 0; m< nodes.length; m++)
    				{
    					if(nodes[m]== n1)
    					{
    						b1 = true;
    						
    					}
    					if(nodes[m]== n2)
    					{
    						b2 = true;
    						
    					}
    				}
    				if(b1 && b2) continue;
    				
    				else if(b1 == false && b2)
    				{
    					//b2 = false;
    					nodes[index] = n1;
    					current = n1;
    					index++;
    					break;
    				}
    				else
    				{
    					//b1 = false;
    					nodes[index] = n2;
    					current = n2;
    					index++;
    					break;
    				}
     				
    			}
    			
    		}
    		
    		
    	}
    	
   
    	//checking if theres a negative cycle at the end
    
    	for(int i=0; i<distances.length; i++)
    	{
    		try
    		{
    			int sumthing = distances[predecessors[i]];
    		}
    		catch(ArrayIndexOutOfBoundsException n) { continue; } //we are checking if our for loop is trying to find the edge between source and its parent. preborn
    		
    		if(distances[i] != Integer.MAX_VALUE &&
    				distances[i] > (distances[predecessors[i]] + g.getEdge(predecessors[i], i).weight)) 
    		{
    			throw new NegativeWeightException();
    		}
    		
    	}
    	
        
        
     
    }

    public int[] shortestPath(int destination) throws BellmanFordException{
        /*Returns the list of nodes along the shortest path from 
         * the object source to the input destination
         * If not path exists an Exception is thrown
         * Choose appropriate Exception from the ones given 
         */	
    	
    		if(distances[destination] == Integer.MAX_VALUE) throw new PathDoesNotExistException();
    		
    		int [] temp = new int[predecessors.length];
    		int jump = destination;
    		
    		for(int i=0; i<predecessors.length; i++)
    		{
    			if(jump == -1) {
    				break;
    			}
    			temp[i] = predecessors[jump];
    			jump = predecessors[jump];
    		}
    		
    		int length = -1;
    		boolean start = false;
    		int count = 0;
    		for(int i=temp.length-1; i>=0; i--)
    		{
    			if(temp[i] == -1 || start) 
    				{
    					length++;
    					start = true;
    				}
    		}
    		
    		int [] shortPath = new int[length+1];
    		
    	
    		
    		for(int i=0; i<length; i++)
    		{
    			
    				
    			shortPath[i] = temp[length-1-i];
    			
    
    		}
    		shortPath[length] = destination;
    		
    		
        return shortPath;
    }

    public void printPath(int destination){
        /*Print the path in the format s->n1->n2->destination
         *if the path exists, else catch the Error and 
         *prints it
         */
        try {
            int[] path = this.shortestPath(destination);
            for (int i = 0; i < path.length; i++){
                int next = path[i];
                if (next == destination){
                    System.out.println(destination);
                }
                else {
                    System.out.print(next + "-->");
                }
            }
        }
        catch (BellmanFordException e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){

        String file = args[0];
        WGraph g = new WGraph(file);
        try{
            BellmanFord bf = new BellmanFord(g, g.getSource());
            bf.printPath(g.getDestination());
        }
        catch (BellmanFordException e){
            System.out.println(e);
        }

   } 
}
