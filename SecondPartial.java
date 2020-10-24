import java.awt.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import javax.annotation.Generated;

public class SecondPartial {

	/* Datos: 
	 * Name: Francisco Javier Ramos Velasco
	 * Date: 20/10/2020
	 */
	
    private double[] P; //Costo de pozo en la casa
    private double[][] T; //Costo T[i][j] para tuberia entre dos casas
    private final int N;//Numero de casas

    /**A house will have water supply if either there is well in that house 
     * or there is some path of the pipes that connects that house that has a well
     * in its location*/
    
    /**Minimum amount of money needed to supply water to all houses*/
    
    
    /**
     * Well Cost: 
     * 	  No. Homes: 6
     * 	  20.73 
		  11.98 
		  19.9
		  22.83
		  18.49
		  10.23
     */
    
    /**
     * Pipes Cost:
     *  House 1 
     * 	0.0 //No se puede construir una tuberia en de salida y destino misma casa 
		19.65 0->1
		18.75 0->2
		18.5  0->3
		17.62 0->4
		11.31 0->5
		
		19.65 1->0
		0.0   1->1
		13.72 1->2
		17.04 1->3
		15.55 1->4
		15.21 1->5
		
		18.75 2->0
		13.72 2->1  
		0.0   2->2
		17.96 2->3
		18.67 2->4
		12.17 2->5
		
		18.5  3->0
 		17.04 3->1
		17.96 3->2
		0.0   3->3
		13.1  3->4
		16.02 3->5
		
		17.62 4->0
 		15.55 4->1
		18.67 4->2
		13.1  4->3
		0.0   4->4
		14.53 4->5
		
		11.31 5->0
		15.21 5->1
		12.17 5->2
		16.02 5->3
		14.53 5->4
		0.0   5->5
	**/
    
    /* Importante
     * Método que use para el grafo en clase Edge Weighted Graph
     * Para insertar los ejes.
     * 
     * public void weightEdges(int v, int w, double weight) {
            Edge e = new Edge(v, w, weight);
            addEdge(e);
     }
     * */
    
    public SecondPartial(In p, In t) {
        if (p == null || t == null) throw new IllegalArgumentException("argument is null");

        try {
            N = p.readInt();
            P = new double[N];
            T = new double[N][N];
            EdgeWeightedGraph grafo = new EdgeWeightedGraph(N);
            int start = 0;
            //Inserción de valores
            for (int i = 0; i < N; i++) {
                P[i] = p.readDouble();
                if(P[start]>P[i])
                	start = i;
                for (int j = 0; j < N; j++) {
                    T[i][j] = t.readDouble();
                    grafo.weightEdges(i, j, T[i][j]);
                }
            }minimoPrimAlgorithm(grafo, start);
        }
        catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in SecondPartial constructor", e);
        }
    }
    
    
    //Build the graph requires to solve the problem
    private void minimoPrimAlgorithm(EdgeWeightedGraph grafo, int start) {
    	HashSet<Integer> wells = new HashSet<>(); //Guardar index de los wells
    	HashSet<Integer> vertex = new HashSet<>(); 
    	HashMap<Integer, Integer> pipes = new HashMap<>();
    	double sum = P[start]; //Minimo pozo inicial 
    	int nextIndex = -1;  
    	double valorguardado= 0; //Por si se encuentra well>tuberia anterior
    	double minweight = 100000;
    	boolean[] visited = new boolean[grafo.V()]; //Casas visitadas
    	visited[start] = true; 
    	wells.add(start); 
    	while(vertex.size()<5) {//Cuando ya se tienen todas las casas visitadas
    		int i=0;
    		vertex.add(start); 
	    	for(boolean valor : visited) { //Solo checa casas visitadas 
	    		if(valor) {
		    		for(Edge e: grafo.adj(i)) {
		    			if(minweight>e.weight() && e.weight()!=0.0 
		    				&& !vertex.contains(e.other(i))) {
		    				//Saca el más pequeño de los edges
		    				//En el cual el destino no esta visitado
		    				minweight = e.weight();
		    				nextIndex = e.other(i);
		    			}
		    		}
	    		}
	    		i++;
	    	}
	    	
	    	pipes.put(start, nextIndex);
	    	start = nextIndex;
    		sum+=minweight;
     		visited[start] = true;
     		
     		if(minweight == P[start]) {
     			wells.add(start); 
    			visited[start] = true; 
    			minweight = 1000000;
    			sum-=valorguardado;
    			valorguardado = 0; 
	    	}else {
	    		valorguardado = minweight;
	    		minweight = P[start];
	    		
	    	}
    		
    	}getDatos(sum, wells, pipes);
    }
    
    public void getDatos(double minimo, HashSet<Integer> wells, HashMap<Integer, Integer> pipes) {
    	System.out.println("Mínimo Costo: "+minimo);
    	System.out.print("Well: ");
    	for (Integer i : wells)  
            System.out.print(" "+i+",");         
    
    	System.out.print("\nPipes: ");
    	
        for (Entry<Integer, Integer> me : pipes.entrySet()) {
          System.out.print(me.getKey() + "-" + me.getValue()+",  ");
        }
    	
    }

    public int getN() {
        return N;
        
    }

    public double[] getP() {
        return P;
    }

    public double[][] getT() {
        return T;
    }

    public static void main(String args[]) {
        In p = new In("./WellCost6.txt");
        In t = new In("./PipeCost6.txt");
        SecondPartial exam = new SecondPartial(p, t);
		
    }
}
