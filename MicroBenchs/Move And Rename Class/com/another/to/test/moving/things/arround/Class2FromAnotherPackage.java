package com.another.to.test.moving.things.arround;

public  class Class2FromAnotherPackage implements Interface1FromComanotherToTestMovingThingsArroundAnotherClass{
		//Class1 c;
		public Class2FromAnotherPackage() {
			//c= new Class1();
		}
		
		@Override
		@SuppressWarnings({"unused", "unchecked"})
		public long facto(long limit) {
			long f= (long) 1;
		for (long i= (long)1 ; i<limit;i++)
			f*=i;
		return f;
		}
		@Override
		public int square(int i) {
			return i*i;
		}
		

	}
