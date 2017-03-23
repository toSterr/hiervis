function() {
	// Load required classes
	var JaccardIndex = Java.type( 'external_measures.statistical_hypothesis.JaccardIndex' );
	var FlatHypotheses = Java.type( 'external_measures.statistical_hypothesis.FlatHypotheses' );

	// Initialize the measure object
	var measure = new JaccardIndex( new FlatHypotheses() );

	// Create and return the result holder object
	var measureData = {};
	measureData.id = 'Jaccard Index (Flat)';
	measureData.isApplicable = function ( hierarchy ) {
		// Applicable only to hierarchies with ground truth attribute
		return hierarchy.getNumberOfClasses() > 0;
	}
	measureData.callback = function ( hierarchy ) {
		return measure.getMeasure( hierarchy );
	}

	return measureData;
}
