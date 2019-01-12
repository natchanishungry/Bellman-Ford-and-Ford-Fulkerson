import java.io.*;
import java.util.*;

  
public class FordFulkerson {

	
	public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph){
		ArrayList<Integer> Parents = new ArrayList<Integer>();
		ArrayList<Edge> Stack = new ArrayList<Edge>();
		ArrayList<Edge> e = graph.getEdges();
		
		Hashtable<Integer, Boolean> visited = new Hashtable<Integer, Boolean>();
	
		Parents.add(source);
		

		boolean found = false;
		boolean noAdjacent = true;
		
		while(!found)
		{
			if(Parents.isEmpty()) return Parents;
			noAdjacent = true;
		
			
			if(Parents.get(Parents.size()-1) == destination)
			{
				found = true;
			}
			
			
			else if(Stack.isEmpty() || Stack.get(Stack.size()-1).weight > 0)
			{
				visited.put(Parents.get(Parents.size()-1), new Boolean(true));
				for(int i = 0; i<e.size(); i++) 
				{
					if(e.get(i).nodes[0] == Parents.get(Parents.size()-1)  && (e.get(i).weight > 0) //|| e.get(i).nodes[1] == destination) 
							&& (visited.get(e.get(i).nodes[1]) == null || !visited.get(e.get(i).nodes[1]))) 
					{
						Stack.add(e.get(i));
						Parents.add(e.get(i).nodes[1]);
						noAdjacent = false; 
						break;
						
					}
					
				}
				if(noAdjacent)
				{
					if(Stack.isEmpty()) 
					{
						Parents.clear();
						break;
					}
					Stack.remove(Stack.size() -1);
					Parents.remove(Parents.size()-1);
				}
			}
			
			else if(Stack.get(Stack.size()-1).weight <= 0)
			{
				visited.put(Parents.get(Parents.size()-1), new Boolean(true));
				Stack.remove(Stack.size() -1);
				Parents.remove(Parents.size()-1);
			}
			
		}
		
		return Parents;
		
	
	}
	
	
	
	public static void fordfulkerson(Integer source, Integer destination, WGraph graph, String filePath){
		String answer="";
		String myMcGillID = "260682940"; //Please initialize this variable with your McGill ID
		int maxFlow = 0;
		
		//WGraph flowGraph = new WGraph(graph); //make a copy for a graph of flows
		WGraph duplicate = new WGraph(graph);
		WGraph residual = new WGraph(graph); //make a copy for a residual graph
		
		ArrayList<Edge> list1 = duplicate.getEdges();
		
		for(int i=0; i<graph.getEdges().size(); i++) //set duplicate to 0 weights in each edge
		{
			list1.get(i).weight = 0;
			
		}
		
	
		while(true)
		{

			ArrayList<Integer> Path = pathDFS(source, destination, residual);
			
		
			Integer bottleneck = new Integer(Integer.MAX_VALUE);
			if(Path.isEmpty()) break; // there are no more paths
			
			
			for(int i= 0; i<Path.size()-1; i++) //find the bottleneck inside residual graph
			{
				Edge e = residual.getEdge(Path.get(i), Path.get(i+1));
				if(e.weight < bottleneck) bottleneck = e.weight;
			}
			
			
			for(int i=0; i<Path.size()-1; i++) //augmenting the path on graph
			{
				int node1 = Path.get(i);
				int node2 = Path.get(i+1);
				Edge e = duplicate.getEdge(node1, node2);
				
				if(e!= null && graph.getEdge(node1, node2) != null) //this is a forward edge
				{
					e.weight+=bottleneck;
				}
				else 
				{
					if(e==null)
					{
						Edge a = duplicate.getEdge(node2, node1);
						a.weight-=bottleneck;
					} 
				
				}
				
			}
			
			
		
			for(int i= 0; i<Path.size()-1; i++) //update residual graph	
			{
				int node1 = Path.get(i);
				int node2 = Path.get(i+1);
				Edge e = duplicate.getEdge(node1, node2);
				Edge capacity = graph.getEdge(node1, node2);
				
				if(capacity == null) //if we are dealing with a backwards edge, e will also be null
				{
					e = duplicate.getEdge(node2, node1);
					capacity = graph.getEdge(node2, node1);
					if(e!= null)
					{
						residual.setEdge(node2, node1, (capacity.weight - e.weight)); //backwards weight
						residual.setEdge(node1, node2, e.weight);
					}
					continue;
				}
				
				else if(e.weight <= capacity.weight) //maybe delete this else later
				{
					
					residual.setEdge(node1, node2, (capacity.weight - e.weight)); //set forward edge
				}
				if(e.weight>0)
				{
					
					try{
						residual.addEdge(new Edge(node2, node1, e.weight)); //set backward edge
					}
					catch(RuntimeException error)
					{
						residual.setEdge(node2, node1, e.weight); //set backward edge
					}
					
					
				}
			}
			
			maxFlow += bottleneck;
			
	
			
			
		} 
		
		
		graph = duplicate;
		if(duplicate.getNbNodes() == 0) maxFlow = -1; 
		
		answer += maxFlow + "\n" + graph.toString();	
		writeAnswer(filePath+myMcGillID+".txt",answer);
		System.out.println(answer);
		
		
	}
	
	
	public static void writeAnswer(String path, String line){
		BufferedReader br = null;
		File file = new File(path);
		// if file doesnt exists, then create it
		
		try {
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(line+"\n");	
		bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	 public static void main(String[] args){
		 String file = args[0];
		 File f = new File(file);
		 WGraph g = new WGraph(file);
		 fordfulkerson(g.getSource(),g.getDestination(),g,f.getAbsolutePath().replace(".txt",""));
	 }
}
