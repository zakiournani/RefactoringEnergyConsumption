package commons.commons;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
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

import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.http.javanet.NetHttpTransport;






//@State(Scope.Benchmark)
public class App {
	 



    @Benchmark
    @Warmup(iterations = 1, time = 2)
    @Measurement(iterations = 2, time = 7)
    //@BenchmarkMode(Mode.AverageTime)
    @BenchmarkMode({Mode.All})
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void httpBenchmark(Blackhole bc)
    {	Energy j = new Energy();
		try {
		j.init();
		
		for(int loop=0;loop<20;loop++) {
		HttpTransport transport= new NetHttpTransport();
		HttpRequestFactory factory= transport.createRequestFactory();
		//////// GET
		 GenericUrl url = new GenericUrl("http://localhost:8080/code");
		    HttpRequest req = factory.buildGetRequest(url);
		    req.setHeaders( new HttpHeaders().set("Cache-Control","no-cache"));
		    HttpResponse resp = req.execute();
		    if(resp.isSuccessStatusCode()) {
		    	   InputStream is = resp.getContent();
	               BufferedInputStream input = new BufferedInputStream(is);
	               byte[] data = new byte[64 * 1024];

	               long count;

	               while ((count = input.read(data)) != -1) {
	                   bc.consume(count);
	               }
	               
	               input.close();
		    }
		    resp.disconnect();

	        //// POST
	        File file= new File("bench");
	        LinkedHashMap<String, String> data = new LinkedHashMap<>();
	        data.put("name", "bench");
	        GenericUrl url2 = new GenericUrl("http://localhost:8080");
	        FileContent fileContent = new FileContent("text/json",file);
	        MultipartContent content = new MultipartContent();
	        MultipartContent.Part part = new MultipartContent.Part(fileContent);
	        content.addPart(part);
	        HttpRequest req2 = factory.buildPostRequest(url2,content);
		    req.setHeaders( new HttpHeaders().set("Cache-Control","no-cache"));
		    HttpResponse resp2 = req2.execute();
	        bc.consume(resp2);
	        //// Delete the posted file
		// Go to /home/jff

		}//end for  
		j.stop();
		System.out.print(j.getEnergy()+"+ ");
		}
        
		catch (IOException e) {
			e.printStackTrace();
		}
    	//table.add(j.getEnergy());
        	
        
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
