package org.knime.geo.resample;

public enum DirectoryFormat {
	
	MainDir {
	      public String toString() {
	          return "Use name of source file";
	      }
	  },
	SubDir {
	      public String toString() {
	          return "Create files in sub-directory";
	      }
	  }

}
