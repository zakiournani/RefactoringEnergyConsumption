package commons.commons;



import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.concurrent.TimeUnit;

import jflex.benchmark.NoAction;
import jflex.benchmark.pregen.NoAction17;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

// @BenchmarkMode({Mode.AverageTime, Mode.SampleTime, Mode.SingleShotTime})
@BenchmarkMode({Mode.AverageTime, Mode.SingleShotTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1)
public class App {

  @State(Scope.Benchmark)
  public static class LexerState {
    /**
     * Factor by which to scale the input size. We should see a benchmark time roughly linear in the
     * factor, i.e. the first time times 10 and 100.
     */
    @Param({"100", "1000", "10000"})
    public int factor;

    @Param({"1", "2"})
    public int input;

    /** The length of the input for the benchmark. We give this to the baseline, but not JFlex. */
    public int length;

    /** The reader the input will be read from. Must support {@code reset()}. */
    public Reader reader;

    /** Create input and populate state fields. Runs once per entire benchmark. */
    @Setup
    public void setup() {
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < 10 * factor; i++) {
        switch (input) {
          case 1:
            builder.append("aaa");
            builder.append("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
            builder.append("  ");
            break;
          case 2:
            builder.append("😎a");
            builder.append("このマニュアルについてbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
            builder.append("  ");
            break;
          default:
            assert false : "reached unreachable default case";
        }
      }
      length = builder.length();
      reader = new StringReader(builder.toString());
    }
  }

/*  @Benchmark
  public int noActionLexer(LexerState state) throws IOException {
    state.reader.reset();
    return new NoAction(state.reader).yylex();
  }*/

/*  @Benchmark
  public int noAction17Lexer(LexerState state) throws IOException {

    state.reader.reset();
    return new NoAction17(state.reader).yylex();
    
  }*/
 /* @Benchmark
  public int noAction17Lexer(LexerState state) throws IOException {
		int kk=0;
	  Energy j = new Energy();
		try {
		j.init();
		}	catch (IOException e) {
			e.printStackTrace();
		}
    state.reader.reset();
    kk= new NoAction17(state.reader).yylex();
	j.stop();
	System.out.print(j.getEnergy()+"- ");
	return kk;
  }*/
  /**
   * The base line: a single continuous pass accessing each character once, through a buffer filled
   * by a reader in one single reader invocation.
   */
  @Benchmark
  @Warmup(iterations = 1, time = 2)
  @Measurement(iterations = 3, time = 5)
  @BenchmarkMode(Mode.AverageTime)
  //@BenchmarkMode({Mode.All})
  @OutputTimeUnit(TimeUnit.SECONDS)
  public void baselineReader(LexerState state, Blackhole bh) throws IOException {
	Energy j = new Energy();
	try {
	j.init();
	for(int l=0;l<1000;l++) {
    state.reader.reset();
    new NoAction17(state.reader).yylex();
    state.reader.reset();
    new NoAction(state.reader).yylex();
    char[] buffer = new char[state.length];
    state.reader.reset();
    state.reader.read(buffer, 0, buffer.length);
    for (int i = 0; i < buffer.length; i++) {
      bh.consume(buffer[i]);
    } 
	}//end for l
	j.stop();
	System.out.print(j.getEnergy()+"+ ");
	}
    
	catch (IOException e) {
		e.printStackTrace();
	}
  }

  public static void main(String... args) throws Exception {
      Options opts = new OptionsBuilder()
    	 .include(App.class.getSimpleName())
          //.warmupIterations(10)
          //.measurementIterations(1)
//        .shouldDoGC(true)
          .result("csv/res.csv")
          .forks(1)
//        .outputFormat(OutputFormatType.TextReport)
          .build();
      
      new Runner(opts).run();

    }
  
}

    
  

