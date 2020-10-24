package jackson.jackson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
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
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;


public class App {
	//ArrayList<Long> table = new ArrayList<Long>();
	
    @Benchmark
    @Warmup(iterations = 3, time = 2)
    @Measurement(iterations = 10, time = 2)
    //@BenchmarkMode(Mode.AverageTime)
    @BenchmarkMode({Mode.All})
    @OutputTimeUnit(TimeUnit.SECONDS)
    public Void benchmarkSerializationWithGSON(Blackhole bh) {
    	
    	//double medium= (double) 0;
		
    	
		
		try {
	    	Energy j = new Energy();
	    	j.init();
			//reader = Files.newBufferedReader(Paths.get("/home/jffk0586/Downloads/generated5000-10.json"));
			
			XStream xstream = new XStream(new StaxDriver());
			xstream.addPermission(AnyTypePermission.ANY);
			xstream.alias("row", AUser.class);
			xstream.alias("freind", Friend.class);
			xstream.alias("root", AUser[].class);
			xstream.addImplicitCollection(AUser.class,"tags",String.class);
			xstream.addImplicitCollection(AUser.class,"friends",Friend.class);
		//	xstream.addImplicitCollection(AUser[].class, "root",AUser.class);
			File xmlFile = new File("/home/jffk0586/Downloads/users5000-10.xml");
			AUser[] users=xstream.fromXML(new FileInputStream(xmlFile));
			
			Writer writer = new FileWriter(new File("/home/jffk0586/eclipse-workspace/xstream/target/marshalled"));        
			writer.write(xstream.toXML(users));
			writer.close();
		//AUser[] users= new ObjectMapper().readValue(new File("/home/jffk0586/Downloads/generated5000-10.json"), AUser[].class);
    	//JsonReader reader = new JsonReader(new FileReader("/home/jffk0586/Downloads/generated100-50.json"));
    	//AUser[] users = new Gson().fromJson(reader, AUser[].class);
    	//reader.close();

    	// Java object to JSON file
    	//new ObjectMapper().writeValue(new File("serialization.json"), users);
    	//bh.consume(users);
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