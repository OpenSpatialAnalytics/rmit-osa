package org.knime.geo.mosaic;

public enum OutputType {
	
	Int16 {
	      public String toString() {
	          return "Int16";
	      }
	  },
	Int32 {
	      public String toString() {
	          return "Int32";
	      }
	  },
	UInt16 {
	      public String toString() {
	          return "UInt16";
	      }
	  },
	UInt32 {
	      public String toString() {
	          return "UInt32";
	      }
	  },
	Float32 {
	      public String toString() {
	          return "Float32";
	      }
	  },
	Float64 {
	      public String toString() {
	          return "Float64";
	      }
	  }
}
