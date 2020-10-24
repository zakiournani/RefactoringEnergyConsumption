package commons.commons;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;

import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;



//import org.jgrapht.alg.connectivity.*;
import org.jgrapht.alg.flow.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.generate.*;
import org.jgrapht.graph.*;
import org.jgrapht.util.*;



//@State(Scope.Benchmark)
public class App {
	   public static final int PERF_BENCHMARK_VERTICES_COUNT = 1000;
	    public static final int PERF_BENCHMARK_EDGES_COUNT = 100000;
	    public static final long SEED = 1446523573696201013l;
	    public static final int NR_GRAPHS = 5;
	    



        /**
         * Benchmark 2: Simulate graph usage: Create a graph, perform various algorithms, partially
         * destroy graph
         */
    @Benchmark
    @Warmup(iterations = 3, time = 2)
    @Measurement(iterations = 10, time = 2)
    //@BenchmarkMode(Mode.AverageTime)
    @BenchmarkMode({Mode.All})
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void graphPerformanceBenchmark(Blackhole bc)
    {	Energy j = new Energy();
		try {
		j.init();
		
        for (int i = 0; i < NR_GRAPHS; i++) {
        	//generate graph bench
        	GnmRandomGraphGenerator<Integer, DefaultWeightedEdge> rgg = new GnmRandomGraphGenerator<Integer, DefaultWeightedEdge>(
        			PERF_BENCHMARK_VERTICES_COUNT, PERF_BENCHMARK_EDGES_COUNT,SEED+i);
        	
            // Create a graph
        	
        	SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph =  new SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge>(
                    SupplierUtil.createIntegerSupplier(1),
                    SupplierUtil.DEFAULT_WEIGHTED_EDGE_SUPPLIER);
        	rgg.generateGraph(graph);
        
            Integer[] vertices = graph.vertexSet().toArray(new Integer[graph.vertexSet().size()]);
            Integer source = vertices[0];
            Integer sink = vertices[vertices.length - 1];
            

            // Run various algorithms on the graph
           double length = this.calculateShorestPath(graph, source, sink);
            bc.consume(length);

            double maxFlow = this.calculateMaxFlow(graph, source, sink);
            bc.consume(maxFlow);
            
           boolean isStronglyConnected = this.isStronglyConnected(graph);
           bc.consume(isStronglyConnected);

            // Destroy some random edges in the graph
            destroyRandomEdges(graph);
            //destriy the whole graph
            graph.removeAllEdges( graph.edgeSet() );
            graph.removeAllVertices( graph.vertexSet() );
    		LinkedList<DefaultWeightedEdge> copy = new LinkedList<DefaultWeightedEdge>();
    		for (DefaultWeightedEdge e : graph.edgeSet()) {
    			copy.add(e);
    		}
    		graph.removeAllEdges(copy);
    		LinkedList<Integer> copy2 = new LinkedList<Integer>();
    		for (Integer v : graph.vertexSet()) {
    			copy2.add(v);
    		}
    		graph.removeAllVertices(copy2);
            bc.consume(copy);
            bc.consume(copy2);
            bc.consume(rgg);
            bc.consume(graph);
           
			j.stop();
			System.out.print(j.getEnergy()+"+ ");
			}
        }
			catch (IOException e) {
				e.printStackTrace();
			}
    		//table.add(j.getEnergy());
        	
        
    }
    
    private double calculateShorestPath(
            SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph, Integer source,
            Integer sink)
        {
           // DijkstraShortestPath<Integer, DefaultWeightedEdge> shortestPathAlg = new DijkstraShortestPath<>(graph);
    		BellmanFordShortestPath<Integer, DefaultWeightedEdge> shortestPathAlg = new BellmanFordShortestPath<>(graph);
    	    return shortestPathAlg.getPath(source, sink).getWeight();
        }

        private double calculateMaxFlow(
            SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph, Integer source,
            Integer sink)
        {
            EdmondsKarpMFImpl<Integer, DefaultWeightedEdge> maximumFlowAlg =
                new EdmondsKarpMFImpl<Integer, DefaultWeightedEdge>(graph);
            return maximumFlowAlg.getMaximumFlow(source, sink).getValue();
        }
  
        private boolean isStronglyConnected(
            SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph)
        {
            StrongConnectivityAlgorithm<Integer, DefaultWeightedEdge> strongConnectivityAlg =
                new GabowStrongConnectivityInspector<Integer, DefaultWeightedEdge>(graph);
            return strongConnectivityAlg.isStronglyConnected();
        }

        private void destroyRandomEdges(
            SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph)
        {
            int nrVertices = graph.vertexSet().size();
            Random rand = new Random(SEED);
            for (int i = 0; i < PERF_BENCHMARK_EDGES_COUNT / 2; i++) {
                int u = rand.nextInt(nrVertices);
                int v = rand.nextInt(nrVertices);
                graph.removeEdge(u, v);
            }
        }
    
    public static void main(String... args) throws Exception {

        Options opts = new OptionsBuilder()
            .include(App.class.getSimpleName())
            //.warmupIterations(10)
            //.measurementIterations(1)
//          .shouldDoGC(true)
            .resultFormat(ResultFormatType.CSV)
            .result("csv/res.csv")
            .forks(1)
//          .outputFormat(OutputFormatType.TextReport)
            .build();
        
        new Runner(opts).run();

      }  


}
