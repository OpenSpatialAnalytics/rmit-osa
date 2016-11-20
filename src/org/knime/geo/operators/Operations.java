package org.knime.geo.operators;

public enum Operations {
	
	UNION {
	      public String toString() {
	          return "Union";
	      }
	  },
	INTERSECTION {
	      public String toString() {
	          return "Intersection";
	      }
	  },
	DIFFERENCE {
	      public String toString() {
	          return "Difference";
	      }
	  }

}
