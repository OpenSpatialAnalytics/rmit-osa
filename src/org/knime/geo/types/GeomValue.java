package org.knime.geo.types;

import javax.swing.Icon;

import org.knime.core.data.DataValue;
import org.knime.core.data.DataValueComparator;
import org.knime.core.data.ExtensibleUtilityFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

public interface GeomValue extends DataValue {
	
	/**
	 * Meta information to this value type.
	 *
	 * @see DataValue#UTILITY
	 */
	public static final UtilityFactory UTILITY = new GeomUtilityFactory();
	
	Geometry getGeometry();
	
	String getGeomValue();
	
	
	public static class GeomUtilityFactory extends ExtensibleUtilityFactory  {

		/** Singleton icon to be used to display this cell type. */
		private static final Icon ICON = loadIcon(GeomValue.class, "/geom.png");

		private static final DataValueComparator COMPARATOR = new DataValueComparator() {

			@Override
			protected int compareDataValues(final DataValue v1, final DataValue v2) {

				final int BEFORE = -1;
				final int EQUAL = 0;
				final int AFTER = 1;

				return v1.hashCode() < v2.hashCode() ? BEFORE : (v1.hashCode() == v2.hashCode() ? EQUAL : AFTER);
			}
		};

		/** Only subclasses are allowed to instantiate this class. */
		protected GeomUtilityFactory() {
			super(GeomValue.class);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Icon getIcon() {
			return ICON;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected DataValueComparator getComparator() {
			return COMPARATOR;
		}

		@Override
		public String getName() {
			return "Geometry";
		}
	}
	
	

	

}
