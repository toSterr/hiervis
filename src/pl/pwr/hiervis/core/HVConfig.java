package pl.pwr.hiervis.core;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Class storing the configuration of the visualizer at runtime for easy access.
 * 
 * @author Tomasz Bachmiński
 *
 */
public class HVConfig {
	/**
	 * Default path to the config file
	 */
	public static final String FILE_PATH = "./config.json";

	private static final Logger log = LogManager.getLogger(HVConfig.class);

	@SerializableField
	private Color currentGroupColor;
	@SerializableField
	private Color childGroupColor;
	@SerializableField
	private Color parentGroupColor;
	@SerializableField
	private Color ancestorGroupColor;
	@SerializableField
	private Color otherGroupColor;
	@SerializableField
	private Color histogramColor;
	@SerializableField
	private Color backgroundColor;

	@SerializableField
	private int pointSize;
	@SerializableField
	private int numberOfHistogramBins;
	@SerializableField
	private int doubleFormatPrecision;
	@SerializableField
	private boolean measuresUseSubtree;

	@SerializableField
	private String preferredLookAndFeel;
	@SerializableField
	private boolean stopXfceLafChange;

	// ---------------------------------------
	// HK++ properties
	@SerializableField
	private int hkClusters;
	@SerializableField
	private int hkIterations;
	@SerializableField
	private int hkRepetitions;
	@SerializableField
	private int hkDendrogramHeight;
	@SerializableField
	private int hkMaxNodes;
	@SerializableField
	private int hkEpsilon;
	@SerializableField
	private int hkLittleValue;
	@SerializableField
	private boolean hkWithTrueClass;
	@SerializableField
	private boolean hkWithInstanceNames;
	@SerializableField
	private boolean hkWithDiagonalMatrix;
	@SerializableField
	private boolean hkNoStaticCenter;
	@SerializableField
	private boolean hkGenerateImages;
	@SerializableField
	private boolean hkVerbose;

	/**
	 * Create a new config with default values.
	 */
	public HVConfig() {
		// Setup default values.
		currentGroupColor = Color.red;
		childGroupColor = Color.green;
		parentGroupColor = Color.black;
		ancestorGroupColor = Color.blue.brighter();
		otherGroupColor = Color.lightGray;
		histogramColor = Color.magenta;
		backgroundColor = new Color(-1);

		numberOfHistogramBins = 100;
		pointSize = 3;
		doubleFormatPrecision = 3;
		measuresUseSubtree = false;

		preferredLookAndFeel = "";
		stopXfceLafChange = false;

		hkClusters = 2;
		hkIterations = 10;
		hkRepetitions = 10;
		hkDendrogramHeight = 2;
		hkMaxNodes = -1;
		hkEpsilon = 10;
		hkLittleValue = 5;
		hkWithTrueClass = true;
		hkWithInstanceNames = false;
		hkWithDiagonalMatrix = true;
		hkNoStaticCenter = false;
		hkGenerateImages = false;
		hkVerbose = false;
	}

	/**
	 * Create a shallow copy of the specified source config.
	 * 
	 * @param source
	 *            config to copy values from
	 * @return the new, copied config (shallow copy)
	 */
	public static HVConfig from(HVConfig source) {
		if (source == null) {
			throw new IllegalArgumentException("Source config must not be null!");
		}

		HVConfig clone = new HVConfig();

		try {
			for (Field field : HVConfig.class.getDeclaredFields()) {
				if (isValidField(field)) {
					// We're setting corresponding fields, so there's no need to use
					// HVConfig.setField().
					field.set(clone, field.get(source));
				}
			}
		}
		catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("Error while processing config fields: ", e);
		}

		return clone;
	}

	/**
	 * Create a new config from the specified file.
	 * 
	 * @param file
	 *            the file containing config values. Assumed to be in *.json format.
	 * @return the loaded confing
	 * @throws IOException
	 *             if an IO error occurred
	 * @throws JsonProcessingException
	 *             if an error occurred while processing the json text
	 */
	public static HVConfig from(File file) throws IOException {
		HVConfig config = new HVConfig();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

		JsonNode rootNode = mapper.readTree(file);

		try {
			for (Field field : HVConfig.class.getDeclaredFields()) {
				JsonNode node = rootNode.get(field.getName());
				if (node != null && node instanceof NullNode == false && isValidField(field)) {
					config.setField(field, node);
				}
			}
		}
		catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("Error while processing config fields: ", e);
		}

		return config;
	}

	/**
	 * Create a shallow copy of the receiver.
	 * 
	 * @see HVConfig#from(HVConfig)
	 */
	public HVConfig copy() {
		return HVConfig.from(this);
	}

	/**
	 * Serializes this config object, saving it to the specified file in JSON
	 * format.
	 * 
	 * @param file
	 *            the file to save the config to
	 */
	public void to(File file) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

		ObjectNode root = mapper.createObjectNode();

		try {
			JsonNodeFactory f = JsonNodeFactory.withExactBigDecimals(false);

			for (Field field : HVConfig.class.getDeclaredFields()) {
				if (isValidField(field)) {
					root.set(field.getName(), serializeField(f, field));
				}
			}
		}
		catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("Error while processing config fields: ", e);
		}

		try {
			ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
			writer.writeValue(file, root);
		}
		catch (Exception e) {
			log.error("Error while writing json data to file: ", e);
		}
	}

	/**
	 * Checks whether the field is 'valid' for the purpose of config serialization.
	 * 
	 * @param f
	 *            the field to check
	 * @return true if the field is marked with the SerializableField annnotation.
	 */
	private static boolean isValidField(Field f) {
		return f.isAnnotationPresent(SerializableField.class);
	}

	/**
	 * Set the specified field to the value represented by the specified JSON node,
	 * converted to appropriate type (depending on field type)
	 * 
	 * @param f
	 *            the field to set
	 * @param node
	 *            the node containing the value for the field
	 * @throws IllegalArgumentException
	 *             if the value represented by the node is not the appropriate type
	 *             for the field, or no case has been implemented to handle that
	 *             field's type.
	 * @throws IllegalAccessException
	 *             if the specified field cannot be accessed
	 */
	private void setField(Field f, JsonNode node) throws IllegalArgumentException, IllegalAccessException {
		if (f.getType().equals(boolean.class)) {
			f.set(this, node.asBoolean());
		}
		else if (f.getType().equals(int.class)) {
			f.set(this, node.asInt());
		}
		else if (f.getType().equals(long.class)) {
			f.set(this, node.asLong());
		}
		else if (f.getType().equals(double.class)) {
			f.set(this, node.asDouble());
		}
		else if (f.getType().equals(float.class)) {
			float value = (float) node.asDouble();
			f.set(this, value);
		}
		else if (f.getType().equals(String.class)) {
			f.set(this, node.asText());
		}
		else if (f.getType().equals(Path.class)) {
			File value = new File(node.asText());
			f.set(this, value.toPath());
		}
		else if (f.getType().equals(Color.class)) {
			String input = node.asText();
			try {
				Field cf = Color.class.getDeclaredField(input);
				int m = cf.getModifiers();
				// Get only publically available static fields, so that we only permit
				// accessing color constants by name, like 'red'
				if (Modifier.isPublic(m) && Modifier.isStatic(m)) {
					f.set(this, cf.get(null));
				}
				else {
					throw new IllegalArgumentException(String.format("'%s' is not a valid color constant!", input));
				}
			}
			catch (Exception e) {
				try {
					Color value = Color.decode(input);
					f.set(this, value);
				}
				catch (NumberFormatException ex) {
					log.error("Error while processing value for a color field: ", e);
				}
			}
		}
		else {
			throw new IllegalArgumentException(
					String.format("No case defined for field type %s", f.getType().getSimpleName()));
		}
	}

	/**
	 * Serializes the specified field into a JsonNode of the appropriate type
	 * created by the specified factory object.
	 * 
	 * @param factory
	 *            the factory object which creates JSON nodes
	 * @param f
	 *            the field to serialize
	 * @return a JsonNode instance representing the specified field
	 * @throws IllegalArgumentException
	 *             if the value represented by the node is not the appropriate type
	 *             for the field, or no case has been implemented to handle that
	 *             field's type.
	 * @throws IllegalAccessException
	 *             if the specified field cannot be accessed
	 */
	private JsonNode serializeField(JsonNodeFactory factory, Field f)
			throws IllegalArgumentException, IllegalAccessException {
		if (f.getType().equals(boolean.class)) {
			return factory.booleanNode(f.getBoolean(this));
		}
		else if (f.getType().equals(int.class)) {
			return factory.numberNode(f.getInt(this));
		}
		else if (f.getType().equals(long.class)) {
			return factory.numberNode(f.getLong(this));
		}
		else if (f.getType().equals(double.class)) {
			return factory.numberNode(f.getDouble(this));
		}
		else if (f.getType().equals(float.class)) {
			return factory.numberNode(f.getFloat(this));
		}
		else if (f.getType().equals(String.class)) {
			return factory.textNode((String) f.get(this));
		}
		else if (f.getType().equals(Path.class)) {
			Path value = (Path) f.get(this);
			return factory.textNode(value == null ? null : value.toString());
		}
		else if (f.getType().equals(Color.class)) {
			Color value = (Color) f.get(this);
			return factory.textNode(String.format("#%02X%02X%02X", // Format as uppercase hex string.
					value.getRed(), value.getGreen(), value.getBlue()));
		}
		else {
			throw new IllegalArgumentException(
					String.format("No case defined for field type %s", f.getType().getSimpleName()));
		}
	}

	/*
	 * ----------------------------------- Config values' getters and setters.
	 */

	public Color getCurrentGroupColor() {
		return currentGroupColor;
	}

	public void setCurrentLevelColor(Color currentGroupColor) {
		this.currentGroupColor = currentGroupColor;
	}

	public Color getChildGroupColor() {
		return childGroupColor;
	}

	public void setChildGroupColor(Color childGroupsColor) {
		this.childGroupColor = childGroupsColor;
	}

	public Color getParentGroupColor() {
		return parentGroupColor;
	}

	public void setParentGroupColor(Color parentGroupsColor) {
		this.parentGroupColor = parentGroupsColor;
	}

	public Color getOtherGroupColor() {
		return otherGroupColor;
	}

	public void setOtherGroupColor(Color otherGroupsColor) {
		this.otherGroupColor = otherGroupsColor;
	}

	public Color getAncestorGroupColor() {
		return ancestorGroupColor;
	}

	public void setAncestorGroupColor(Color ancestorColor) {
		this.ancestorGroupColor = ancestorColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroudColor) {
		this.backgroundColor = backgroudColor;
	}

	public Color getHistogramColor() {
		return histogramColor;
	}

	public void setHistogramColor(Color histogramColor) {
		this.histogramColor = histogramColor;
	}

	public int getPointSize() {
		return pointSize;
	}

	public void setPointSize(int pointSize) {
		this.pointSize = pointSize;
	}

	public void setNumberOfHistogramBins(int numberOfHistogramBins) {
		this.numberOfHistogramBins = numberOfHistogramBins;
	}

	public int getNumberOfHistogramBins() {
		return numberOfHistogramBins;
	}

	public int getDoubleFormatPrecision() {
		return doubleFormatPrecision;
	}

	public void setDoubleFormatPrecision(int doubleFormatPrecision) {
		this.doubleFormatPrecision = doubleFormatPrecision;
	}

	public boolean isMeasuresUseSubtree() {
		return measuresUseSubtree;
	}

	public void setMeasuresUseSubtree(boolean measuresUseSubtree) {
		this.measuresUseSubtree = measuresUseSubtree;
	}

	public void setPreferredLookAndFeel(String lookAndFeel) {
		preferredLookAndFeel = lookAndFeel;
	}

	public String getPreferredLookAndFeel() {
		return preferredLookAndFeel;
	}

	public void setStopXfceLafChange(boolean stopLafChange) {
		this.stopXfceLafChange = stopLafChange;
	}

	public boolean isStopXfceLafChange() {
		return this.stopXfceLafChange;
	}

	/*
	 * ----------------------------------- HK++ config's getters and setters.
	 */

	public int getHkClusters() {
		return hkClusters;
	}

	public void setHkClusters(int hkClusters) {
		this.hkClusters = hkClusters;
	}

	public int getHkIterations() {
		return hkIterations;
	}

	public void setHkIterations(int hkIterations) {
		this.hkIterations = hkIterations;
	}

	public int getHkRepetitions() {
		return hkRepetitions;
	}

	public void setHkRepetitions(int hkRepetitions) {
		this.hkRepetitions = hkRepetitions;
	}

	public int getHkDendrogramHeight() {
		return hkDendrogramHeight;
	}

	public void setHkDendrogramHeight(int hkDendrogramHeight) {
		this.hkDendrogramHeight = hkDendrogramHeight;
	}

	public int getHkMaxNodes() {
		return hkMaxNodes;
	}

	public void setHkMaxNodes(int hkMaxNodes) {
		this.hkMaxNodes = hkMaxNodes;
	}

	public int getHkEpsilon() {
		return hkEpsilon;
	}

	public void setHkEpsilon(int hkEpsilon) {
		this.hkEpsilon = hkEpsilon;
	}

	public int getHkLittleValue() {
		return hkLittleValue;
	}

	public void setHkLittleValue(int hkLittleValue) {
		this.hkLittleValue = hkLittleValue;
	}

	public boolean isHkWithTrueClass() {
		return hkWithTrueClass;
	}

	public void setHkWithTrueClass(boolean hkWithTrueClass) {
		this.hkWithTrueClass = hkWithTrueClass;
	}

	public boolean isHkWithInstanceNames() {
		return hkWithInstanceNames;
	}

	public void setHkWithInstanceNames(boolean hkWithInstanceNames) {
		this.hkWithInstanceNames = hkWithInstanceNames;
	}

	public boolean isHkWithDiagonalMatrix() {
		return hkWithDiagonalMatrix;
	}

	public void setHkWithDiagonalMatrix(boolean hkWithDiagonalMatrix) {
		this.hkWithDiagonalMatrix = hkWithDiagonalMatrix;
	}

	public boolean isHkNoStaticCenter() {
		return hkNoStaticCenter;
	}

	public void setHkNoStaticCenter(boolean hkNoStaticCenter) {
		this.hkNoStaticCenter = hkNoStaticCenter;
	}

	public boolean isHkGenerateImages() {
		return hkGenerateImages;
	}

	public void setHkGenerateImages(boolean hkGenerateImages) {
		this.hkGenerateImages = hkGenerateImages;
	}

	public boolean isHkVerbose() {
		return hkVerbose;
	}

	public void setHkVerbose(boolean hkVerbose) {
		this.hkVerbose = hkVerbose;
	}

	/*
	 * ---------------------- Miscellaneous methods.
	 */

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o instanceof HVConfig == false)
			return false;
		return equals((HVConfig) o);
	}

	public boolean equals(HVConfig o) {
		try {
			for (Field field : HVConfig.class.getDeclaredFields()) {
				if (isValidField(field)) {
					Object lv = field.get(this);
					Object rv = field.get(o);

					if (!Objects.equals(lv, rv))
						return false;
				}
			}
		}
		catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("Error while processing config fields: ", e);
		}

		return true;
	}

	/**
	 * Marker annotation used to distinguish fields that are meant to be serialized
	 * into the config file.
	 * 
	 * @author Tomasz Bachmiński
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	private @interface SerializableField {
	}
}
