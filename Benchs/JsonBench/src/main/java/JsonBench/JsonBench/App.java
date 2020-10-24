package JsonBench.JsonBench;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;

import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.google.gson.Gson;


public class App {
	//ArrayList<Long> table = new ArrayList<Long>();
	
    @Benchmark
    @Warmup(iterations = 3, time = 2)
    @Measurement(iterations = 10, time = 2)
    @BenchmarkMode(Mode.AverageTime)
    //@BenchmarkMode({Mode.All})
    @OutputTimeUnit(TimeUnit.SECONDS)
    public Void benchmarkSerializationWithGSON(Blackhole bh) {
    	Reader reader;
    	FileWriter writer;
    	//double medium= (double) 0;
		
    	
		
		try {
	    	Energy j = new Energy();
	    	j.init();
			reader = Files.newBufferedReader(Paths.get("/home/jffk0586/Downloads/generated5000-10.json"));
			

    	//JsonReader reader = new JsonReader(new FileReader("/home/jffk0586/Downloads/generated100-50.json"));
    	AUser[] users = new Gson().fromJson(reader, AUser[].class);
    	reader.close();
   	 	writer= new FileWriter("serialization.json");
   	 	new Gson().toJson(users, writer);
   	 	writer.close();
    	bh.consume(users);
    	j.stop();
		//table.add(j.getEnergy());
    	System.out.print(j.getEnergy()+"+ ");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;


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