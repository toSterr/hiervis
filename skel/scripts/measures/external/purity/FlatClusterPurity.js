function() {
	// Load required classes
	var FlatClusterPurity = Java.type( 'external_measures.purity.FlatClusterPurity' );

	// Initialize the measure object
	var measure = new FlatClusterPurity();

	// Create and return the result holder object
	var measureData = {};
	measureData.id = 'Flat Cluster Purity';
	measureData.isApplicable = function ( hierarchy ) {
		// Applicable only to hierarchies with ground truth attribute
		return hierarchy.getNumberOfClasses() > 0;
	}
	measureData.callback = function ( hierarchy ) {
		return measure.getMeasure( hierarchy );
	}

	return measureData;
}