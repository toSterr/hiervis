function() {
	// Load required classes
	var FlatEntropy2 = Java.type( 'external_measures.information_based.FlatEntropy2' );

	// Initialize the measure object
	var measure = new FlatEntropy2();

	// Create and return the result holder object
	var measureData = {};
	measureData.id = 'Flat Entropy 2';
	measureData.callback = function ( hierarchy ) {
		return measure.getMeasure( hierarchy );
	}

	return measureData;
}