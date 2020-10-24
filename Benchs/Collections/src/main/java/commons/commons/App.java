package commons.commons;


import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;

import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.eclipse.collections.api.factory.set.*;
import org.eclipse.collections.api.factory.set.sorted.MutableSortedSetFactory;
//import org.eclipse.collections.api.factory.SortedSets;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.sorted.MutableSortedSet;
import org.eclipse.collections.api.set.sorted.ImmutableSortedSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.list.Interval;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.set.sorted.immutable.ImmutableSortedSetFactoryImpl;
import org.eclipse.collections.impl.set.sorted.mutable.MutableSortedSetFactoryImpl;







@State(Scope.Benchmark)
public class App {
	 
    private static final int SIZE = 100_000;
    private static final int BATCH_SIZE = 1_000;
    private ExecutorService executorService;

    private final MutableList<Integer> ecMutable = Lists.mutable.withAll(Interval.zeroTo(SIZE));
    private final ImmutableList<Integer> ecImmutable = Lists.immutable.withAll(Interval.zeroTo(SIZE));
    // uncomment for latter versions
    //private static final int SIZE2 = 2_000_000;
	
    //private final MutableSortedSet<Integer> ecMutableset = SortedSets.mutable.withAll(Interval.zeroToBy(SIZE, 2));
    //private final ImmutableSortedSet<Integer> ecImmutableset = (ImmutableSortedSet<Integer>) Sets.immutable.withAll(Interval.zeroToBy(SIZE, 2));
    

    private final MutableSortedSet<Integer> ecMutableset = new MutableSortedSetFactoryImpl().withAll(Interval.zeroToBy(SIZE, 2));
    private final ImmutableSortedSet<Integer> ecImmutableset = new ImmutableSortedSetFactoryImpl().withAll(Interval.zeroToBy(SIZE, 2));
    
    private static final int RANDOM_COUNT = 9;

    @Param({"250000", "500000", "750000", "1000000", "1250000", "1500000", "1750000", "2000000", "2250000", "2500000", "2750000", "3000000",
    	 "5000000",  "3250000", "3500000", "3750000", "4000000", "4250000", "4500000", "4750000", "5000000", "5250000", "5500000", "5750000", "6000000",
    	 "7500000",   "6250000", "6500000", "6750000", "7000000", "7250000", "7500000", "7750000", "8000000", "8250000", "8500000", "8750000", "9000000",
            "9250000", "9500000", "9750000", "10000000"})
    public int size;
    private String[] elements;
    private MutableMap<String, String> ecMap;
    
    
    @Benchmark
    @Warmup(iterations = 1, time = 2)
    @Measurement(iterations = 2, time = 10)
    @BenchmarkMode(Mode.AverageTime)
    //@BenchmarkMode({Mode.All})
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void httpBenchmark(Blackhole bc)
    {	Energy j = new Energy();
		try {
		j.init();
		///List
		 int count = this.ecMutable
	                .asLazy()
	                .select(each -> each % 10_000 != 0)
	                .collect(String::valueOf)
	                .collect(Integer::valueOf)
	                .count(each -> (each + 1) % 10_000 != 0);
		 count = this.ecImmutable
	                .asLazy()
	                .select(each -> each % 10_000 != 0)
	                .collect(String::valueOf)
	                .collect(Integer::valueOf)
	                .count(each -> (each + 1) % 10_000 != 0);
		 count = this.ecMutable
	                .asParallel(this.executorService, BATCH_SIZE)
	                .select(each -> each % 10_000 != 0)
	                .collect(String::valueOf)
	                .collect(Integer::valueOf)
	                .count(each -> (each + 1) % 10_000 != 0);
		 count = this.ecImmutable
	                .asParallel(this.executorService, BATCH_SIZE)
	                .select(each -> each % 10_000 != 0)
	                .collect(String::valueOf)
	                .collect(Integer::valueOf)
	                .count(each -> (each + 1) % 10_000 != 0);
		 ///sets
	       int size = SIZE;
	        MutableSortedSet<Integer> localEcMutable =	this.ecMutableset;

	        for (int i = 0; i < size; i += 2)
	        {
	            if (!localEcMutable.contains(i))
	            {
	                throw new AssertionError(i);
	            }
	        }

	        for (int i = 1; i < size; i += 2)
	        {
	            if (localEcMutable.contains(i))
	            {
	                throw new AssertionError(i);
	            }
	        }
	        ImmutableSortedSet<Integer> localEcImmutable =  this.ecImmutableset;

	        for (int i = 0; i < size; i += 2)
	        {
	            if (!localEcImmutable.contains(i))
	            {
	                throw new AssertionError(i);
	            }
	        }

	        for (int i = 1; i < size; i += 2)
	        {
	            if (localEcImmutable.contains(i))
	            {
	                throw new AssertionError(i);
	            }
	        }
	        count = this.ecMutableset
	                .asLazy()
	                .select(each -> each % 10_000 != 0)
	                .collect(String::valueOf)
	                .collect(Integer::valueOf)
	                .count(each -> (each + 1) % 10_000 != 0);
	        count = this.ecMutableset
	                .asParallel(this.executorService, BATCH_SIZE)
	                .select(each -> each % 10_000 != 0)
	                .collect(String::valueOf)
	                .collect(Integer::valueOf)
	                .count(each -> (each + 1) % 10_000 != 0);
	        ///MAP
	        int localSize = this.size;
	        String[] localElements = this.elements;
	        MutableMap<String, String> localEcMap = this.ecMap;

	        for (int i = 0; i < localSize; i++)
	        {
	            if (localEcMap.get(localElements[i]) == null)
	            {
	                throw new AssertionError(i);
	            }
	        }
	        
	        localSize = this.size;
	        /**
	         * @see UnifiedMap#DEFAULT_LOAD_FACTOR
	         */
	        float localLoadFactor = 0.75f;
	        localElements = this.elements;

	        MutableMap<String, String> map = UnifiedMap.newMap(localSize, localLoadFactor);

	        for (int i = 0; i < localSize; i++)
	        {
	            map.put(localElements[i], "dummy");
	        }
		
		j.stop();
		System.out.print(j.getEnergy()+"+ ");
		}
        
		catch (Exception e) {
			e.printStackTrace();
		}
    	//table.add(j.getEnergy());
        	
        
    }
    @Setup
    public void setUp()
    {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.elements = new String[this.size];
        this.ecMap = UnifiedMap.newMap(this.size);

        Random random = new Random(123456789012345L);
        for (int i = 0; i < this.size; i++)
        {
            String element = RandomStringUtils.random(RANDOM_COUNT, 0, 0, false, true, null, random);
            this.elements[i] = element;
            this.ecMap.put(element, "dummy");
        }
    }

    @TearDown
    public void tearDown() throws InterruptedException
    {
        this.executorService.shutdownNow();
        this.executorService.awaitTermination(1L, TimeUnit.SECONDS);
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
