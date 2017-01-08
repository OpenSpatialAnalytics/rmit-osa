package org.knime.geo.mosaic;

public enum OutputType {
	
	Byte {
	      public String toString() {
	          return "Byte";
	      }
	  },
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
	  },
	CInt16 {
	      public String toString() {
	          return "CInt16";
	      }
	  },
	CInt32 {
	      public String toString() {
	          return "CInt32";
	      }
	  },
	CFloat32 {
	      public String toString() {
	          return "CFloat32";
	      }
	  },
	CFloat64 {
	      public String toString() {
	          return "CFloat64";
	      }
	  }
}
