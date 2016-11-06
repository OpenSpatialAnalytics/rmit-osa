package org.knime.geo.resample;

public enum OutputFormat {
	
	GTiff {
	      public String toString() {
	          return "GTiff";
	      }
	  },
	WCS {
	      public String toString() {
	          return "WCS";
	      }
	  },
	WMS {
	      public String toString() {
	          return "WMS";
	      }
	  }

}
