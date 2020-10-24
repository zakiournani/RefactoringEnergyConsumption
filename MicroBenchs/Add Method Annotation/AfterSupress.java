import java.util.ArrayList;

public class AfterSupress {
	
	public static void main(String[] args) throws Exception{
		double medium= (double) 0;
		ArrayList<Long> table = new ArrayList<Long>();
		for(int j=0;j<30;j++) {
			Energy e = new Energy();
			e.init();
			
			for(int iter=0; iter<900000000;iter++) {
			Interface1 c = new Class2();
			c.facto(iter%10+1);


			}
			e.stop();
			
			medium = j==0 ? e.getEnergy() : (medium*(j)+e.getEnergy())/(j+1);
			table.add(e.getEnergy());
			System.out.println("énergie par étape "+ e.getEnergy() + " uJ");
		}
		
		System.out.println("l'énergie totale consommée est : "+ medium + " uJ"+ " avec un STD avec : "+ std(table,medium));
		
	}

	public static boolean valid(int i) {
		if (i >0) 
			return true;
		return false;
	}
	public static int square(int i) {
		
		  int result = i * i;
		  return result;
	}

	
	public static long facto(long limit) {
		long f= (long) 1;
		for (long i= (long)1 ; i<limit;i++)
			f*=i;
		return f;
	}
	public interface Interface1{
		public long facto(long facto);
		public  int square(int i);

	}
	public static class Class1  {
		public long facto(long limit) {
			long f= (long) 1;
			for (long i= (long)1 ; i<limit;i++)
				f*=i;
			return f;
		}


	}
	public static class Class2 implements Interface1{
		Class1 c;
		public Class2() {
			c= new Class1();
		}
		@SuppressWarnings({"unused", "unchecked"})
		public long facto(long limit) {
			String temp="je suis un string qui ne sert à rien";
			return c.facto(limit);
		}
		@SuppressWarnings({"unused", "unchecked"})
		public int square(int i) {
			return i*i;
		}
		

	}
	
	public static double std (ArrayList<Long> table, double mean)
	{
	
	    double temp = 0;

	    for (int i = 0; i < table.size(); i++)
	    {
	      
	        double squrDiffToMean = Math.pow(table.get(i) - mean, 2);
	        temp += squrDiffToMean;
	    }
	    return Math.sqrt((double) temp / (double) (table.size()));
	}


}

