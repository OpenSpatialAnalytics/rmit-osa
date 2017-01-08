package org.knime.geo.proximity;

public enum DistanceUnit {
	
	PIXEL {
	      public String toString() {
	          return "PIXEL";
	      }
	  },
	GEO {
	      public String toString() {
	          return "GEO";
	      }
	  }


}
