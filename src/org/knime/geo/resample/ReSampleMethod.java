package org.knime.geo.resample;

public enum ReSampleMethod {
	
	AVG {
	      public String toString() {
	          return "average";
	      }
	  },
	NEAR {
	      public String toString() {
	          return "near";
	      }
	  },
	BILINER {
	      public String toString() {
	          return "bilinear";
	      }
	  },
	CUBIC {
	      public String toString() {
	          return "cubic";
	      }
	  },
	CUBICSPLINE {
	      public String toString() {
	          return "cubicspline";
	      }
	  },
	LANCZOS {
	      public String toString() {
	          return "lanczos";
	      }
	  },
	MODE {
	      public String toString() {
	          return "mode";
	      }
	  },
	MAX {
	      public String toString() {
	          return "max";
	      }
	  },
	MIN {
	      public String toString() {
	          return "min";
	      }
	  },
	MED {
	      public String toString() {
	          return "med";
	      }
	  },
	Q1 {
	      public String toString() {
	          return "q1";
	      }
	  },
	Q2 {
	      public String toString() {
	          return "q2";
	      }
	  }
}
