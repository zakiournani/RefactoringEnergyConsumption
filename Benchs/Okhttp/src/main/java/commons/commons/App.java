package commons.commons;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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



import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;





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
		
		OkHttpClient client = new OkHttpClient();
		  Request getRequest = new Request.Builder()
				  	.url("http://localhost:8080/code")
				  	.header("Cache-Control", "no-cache")
				  	.cacheControl(CacheControl.FORCE_NETWORK)
	                .build();

		  Response response = client.newCall(getRequest).execute();
	       if(response.isSuccessful()){
	                
	    	   InputStream is = response.body().byteStream();
               BufferedInputStream input = new BufferedInputStream(is);
               byte[] data = new byte[64 * 1024];

               long count;

               while ((count = input.read(data)) != -1) {
                   bc.consume(count);
               }
               
               input.close();
              
	        }
	       response.body().close();

	        //// POST
	        File file= new File("bench");
	        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
	        		.addFormDataPart("bench", file.getName(),
	                        RequestBody.create(MediaType.parse("text/json"), file))
	                .addFormDataPart("name", "bench")
	                .build();

	        Request postRequest = new Request.Builder()
	                .url("http://localhost:8080")
	                .post(requestBody)
	                .build();

			  Response responsePost = client.newCall(postRequest).execute();

			  responsePost.body().close();
	        //// Delete the posted file
	        Request delRequest = new Request.Builder().
	        		url("http://localhost:8080/bench").
	        		delete()
	        		.build();

			  Response responseDel = client.newCall(delRequest).execute();

			  responseDel.body().close();
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
