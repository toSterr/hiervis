package pl.pwr.hiervis.hk.ui;

import java.awt.GridBagLayout;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.NumberFormatter;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;

import basic_hierarchy.interfaces.Node;
import pl.pwr.hiervis.HierarchyVisualizer;
import pl.pwr.hiervis.core.HVConfig;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.hk.HKPlusPlusWrapper;
import pl.pwr.hiervis.util.HierarchyUtils;
import pl.pwr.hiervis.util.ui.GridBagConstraintsBuilder;

@SuppressWarnings("serial")
public class HKOptionsPanel extends JPanel {
	private Logger logger = null;
	private HVContext context;

	private LoadedHierarchy hierarchy;
	private Node node;

	private JTextField txtClusters = null;
	private JTextField txtIterations = null;
	private JTextField txtRepeats = null;
	private JTextField txtDendrogram = null;
	private JTextField txtMaxNodes = null;
	private JTextField txtEpsilon = null;
	private JTextField txtLittleVal = null;
	private JCheckBox cboxTrueClass = null;
	private JCheckBox cboxInstanceNames = null;
	private JCheckBox cboxDiagonalMatrix = null;
	private JCheckBox cboxNoStaticCenter = null;
	private JCheckBox cboxGenerateImages = null;

	private JCheckBox cboxVerbose = null;

	public HKOptionsPanel(HVContext context, Node node, Logger logger) {
		this.context = context;
		this.hierarchy = context.getHierarchy();
		this.node = node;
		this.logger = logger;

		createContent();
	}

	private void createContent() {
		GridBagConstraintsBuilder builder = new GridBagConstraintsBuilder();

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 0, 0 };
		layout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		layout.columnWeights = new double[] { 1.0, 0.0 };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		setLayout(layout);

		JLabel lblClusters = new JLabel("[-k] Clusters:");
		txtClusters = buildNumberTextField();
		add(lblClusters, builder.insets(5).anchorWest().position(0, 0).build());
		add(txtClusters, builder.insets(5).anchorEast().position(1, 0).build());

		JLabel lblIterations = new JLabel("[-n] Iterations:");
		txtIterations = buildNumberTextField();
		add(lblIterations, builder.insets(5).anchorWest().position(0, 1).build());
		add(txtIterations, builder.insets(5).anchorEast().position(1, 1).build());

		JLabel lblRepeats = new JLabel("[-r] Repeats:");
		txtRepeats = buildNumberTextField();
		add(lblRepeats, builder.insets(5).anchorWest().position(0, 2).build());
		add(txtRepeats, builder.insets(5).anchorEast().position(1, 2).build());

		JLabel lblDendrogram = new JLabel("[-s] Max Dendrogram Height:");
		txtDendrogram = buildNumberTextField();
		add(lblDendrogram, builder.insets(5).anchorWest().position(0, 3).build());
		add(txtDendrogram, builder.insets(5).anchorEast().position(1, 3).build());

		JLabel lblMaxNodes = new JLabel("[-w] Max Generated Nodes:");
		txtMaxNodes = buildNumberTextField();
		add(lblMaxNodes, builder.insets(5).anchorWest().position(0, 4).build());
		add(txtMaxNodes, builder.insets(5).anchorEast().position(1, 4).build());

		JLabel lblEpsilon = new JLabel("[-e] Epsilon:");
		txtEpsilon = buildNumberTextField();
		add(lblEpsilon, builder.insets(5).anchorWest().position(0, 5).build());
		add(txtEpsilon, builder.insets(5).anchorEast().position(1, 5).build());

		JLabel lblLittleVal = new JLabel("[-l] Little Value:");
		txtLittleVal = buildNumberTextField();
		add(lblLittleVal, builder.insets(5).anchorWest().position(0, 6).build());
		add(txtLittleVal, builder.insets(5).anchorEast().position(1, 6).build());

		JLabel lblTrueClass = new JLabel("[-c] Include True Class:");
		cboxTrueClass = new JCheckBox();
		cboxTrueClass.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblTrueClass, builder.insets(5).anchorWest().position(0, 7).build());
		add(cboxTrueClass, builder.insets(5).anchorCenter().fill().position(1, 7).build());

		JLabel lblInstanceNames = new JLabel("[-in] Include Instance Names:");
		cboxInstanceNames = new JCheckBox();
		cboxInstanceNames.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblInstanceNames, builder.insets(5).anchorWest().position(0, 8).build());
		add(cboxInstanceNames, builder.insets(5).anchorCenter().fill().position(1, 8).build());

		JLabel lblDiagonalMatrix = new JLabel("[-dm] Use Diagonal Matrix:");
		cboxDiagonalMatrix = new JCheckBox();
		cboxDiagonalMatrix.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblDiagonalMatrix, builder.insets(5).anchorWest().position(0, 9).build());
		add(cboxDiagonalMatrix, builder.insets(5).anchorCenter().fill().position(1, 9).build());

		JLabel lblNoStaticCenter = new JLabel("[-ds] Disable Static Center:");
		cboxNoStaticCenter = new JCheckBox();
		cboxNoStaticCenter.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblNoStaticCenter, builder.insets(5).anchorWest().position(0, 10).build());
		add(cboxNoStaticCenter, builder.insets(5).anchorCenter().fill().position(1, 10).build());

		JLabel lblGenerateImages = new JLabel("[-gi] Generate Images:");
		cboxGenerateImages = new JCheckBox();
		cboxGenerateImages.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblGenerateImages, builder.insets(5).anchorWest().position(0, 11).build());
		add(cboxGenerateImages, builder.insets(5).anchorCenter().fill().position(1, 11).build());

		JLabel lblVerbose = new JLabel("<html>[-v] Verbose Mode<br> (Output will be visible from the command line):");
		cboxVerbose = new JCheckBox();
		cboxVerbose.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblVerbose, builder.insets(5).anchorWest().position(0, 12).build());
		add(cboxVerbose, builder.insets(5).anchorCenter().fill().position(1, 12).build());
	}

	public void setupDefaultValues(HVConfig cfg) {
		cboxTrueClass.setEnabled(hierarchy.options.hasTrueClassAttribute);
		cboxInstanceNames.setEnabled(hierarchy.options.hasInstanceNameAttribute);

		txtClusters.setText("" + cfg.getHkClusters());
		txtIterations.setText("" + cfg.getHkIterations());
		txtRepeats.setText("" + cfg.getHkRepetitions());
		txtDendrogram.setText("" + cfg.getHkDendrogramHeight());
		txtMaxNodes.setText("" + cfg.getHkMaxNodes());
		txtEpsilon.setText("" + cfg.getHkEpsilon());
		txtLittleVal.setText("" + cfg.getHkLittleValue());

		cboxTrueClass.setSelected(cfg.isHkWithTrueClass() && cboxTrueClass.isEnabled());
		cboxInstanceNames.setSelected(cfg.isHkWithInstanceNames() && cboxInstanceNames.isEnabled());
		cboxDiagonalMatrix.setSelected(cfg.isHkWithDiagonalMatrix());
		cboxNoStaticCenter.setSelected(cfg.isHkNoStaticCenter());
		cboxGenerateImages.setSelected(cfg.isHkGenerateImages());
		cboxVerbose.setSelected(cfg.isHkVerbose());
	}

	/**
	 * Launches the HK generation method.
	 * 
	 * @param window
	 *            the window to attach the modal dialog to. Can null to attach the
	 *            dialog to the window the HK options panel is placed in.
	 * @return the {@link HKPlusPlusWrapper} instance associated with this
	 *         subprocess
	 */
	public HKPlusPlusWrapper generate(Window window) {
		int clusters = Integer.parseInt(getText(txtClusters)); // -k
		int iterations = Integer.parseInt(getText(txtIterations)); // -n
		int repeats = Integer.parseInt(getText(txtRepeats)); // -r
		int dendrogramHeight = Integer.parseInt(getText(txtDendrogram)); // -s
		int maxNodes = Integer.parseInt(getText(txtMaxNodes)); // -w
		int epsilon = Integer.parseInt(getText(txtEpsilon)); // -e
		int littleVal = Integer.parseInt(getText(txtLittleVal)); // -l

		// Update HK++ config
		HVConfig cfg = context.getConfig();
		cfg.setHkClusters(clusters);
		cfg.setHkIterations(iterations);
		cfg.setHkRepetitions(repeats);
		cfg.setHkDendrogramHeight(dendrogramHeight);
		cfg.setHkMaxNodes(maxNodes);
		cfg.setHkEpsilon(epsilon);
		cfg.setHkLittleValue(littleVal);
		cfg.setHkWithTrueClass(cboxTrueClass.isSelected());
		cfg.setHkWithInstanceNames(cboxInstanceNames.isSelected());
		cfg.setHkWithDiagonalMatrix(cboxDiagonalMatrix.isSelected());
		cfg.setHkNoStaticCenter(cboxNoStaticCenter.isSelected());
		cfg.setHkGenerateImages(cboxGenerateImages.isSelected());
		cfg.setHkVerbose(cboxVerbose.isSelected());

		if (maxNodes < 0) {
			maxNodes = Integer.MAX_VALUE;
		}

		try {
			HKPlusPlusWrapper wrapper = new HKPlusPlusWrapper(cfg);
			wrapper.subprocessFinished.addListener(this::onSubprocessFinished);
			wrapper.subprocessAborted.addListener(this::onSubprocessAborted);

			logger.trace("Preparing input file...");
			wrapper.prepareInputFile(hierarchy.getMainHierarchy(), node, cboxTrueClass.isSelected(),
					cboxInstanceNames.isSelected());

			logger.trace("Starting...");
			wrapper.start(window == null ? SwingUtilities.getWindowAncestor(this) : window, cboxTrueClass.isSelected(),
					cboxInstanceNames.isSelected(), cboxDiagonalMatrix.isSelected(), cboxNoStaticCenter.isSelected(),
					cboxGenerateImages.isSelected(), epsilon, littleVal, clusters, iterations, repeats,
					dendrogramHeight, maxNodes, cboxVerbose.isSelected());

			return wrapper;
		}
		catch (IOException ex) {
			logger.error(ex);
		}

		return null;
	}

	// ---------------------------------------------------------------------------------------------
	// Helper methods

	private static JTextField buildNumberTextField() {
		// TODO: Verifying user input
		NumberFormatter formatter = new NumberFormatter(new DecimalFormat("0"));
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(Integer.MIN_VALUE);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setAllowsInvalid(false);

		JFormattedTextField result = new JFormattedTextField();
		result.setHorizontalAlignment(SwingConstants.RIGHT);
		result.setColumns(10);

		return result;
	}

	/**
	 * Gets the text of the specified text field, or "0", if the text field is
	 * empty.
	 */
	private static String getText(JTextField text) {
		String t = text.getText();
		return t == null || t.equals("") ? "0" : t;
	}

	private String getParameterString(HVConfig cfg) {
		int maxNodes = cfg.getHkMaxNodes();
		String maxNodesStr = maxNodes < 0 ? "MAX_INT" : ("" + maxNodes);

		return String.format("%s / -k %s / -n %s / -r %s / -s %s / -e %s / -l %s / -w %s%s", node.getId(),
				cfg.getHkClusters(), cfg.getHkIterations(), cfg.getHkRepetitions(), cfg.getHkDendrogramHeight(),
				cfg.getHkEpsilon(), cfg.getHkLittleValue(), maxNodesStr, cfg.isHkWithDiagonalMatrix() ? " / DM" : "");
	}

	// ---------------------------------------------------------------------------------------------
	// Listeners

	private void onSubprocessAborted(HKPlusPlusWrapper wrapper) {
		logger.trace("Aborted.");
	}

	private void onSubprocessFinished(Pair<HKPlusPlusWrapper, Integer> args) {
		HKPlusPlusWrapper wrapper = args.getKey();
		int exitCode = args.getValue();

		if (exitCode == 0) {
			logger.trace("Finished successfully.");

			try {
				HVConfig cfg = wrapper.getConfig();

				LoadedHierarchy outputHierarchy = wrapper.getOutputHierarchy(cfg.isHkWithTrueClass(),
						cfg.isHkWithInstanceNames(), false);

				LoadedHierarchy finalHierarchy = HierarchyUtils.merge(outputHierarchy, hierarchy, node.getId());
				context.loadHierarchy(getParameterString(cfg), finalHierarchy);
			}
			catch (Throwable ex) {
				logger.error("Subprocess finished successfully, but failed during processing: ", ex);
				ex.printStackTrace();
			}
		}
		else {
			logger.error("Failed! Error code: " + exitCode);
		}

		wrapper = null;
	}

	private void openInNewInstance(HVConfig cfg, LoadedHierarchy hierarchy) throws IOException {
		File tmp = File.createTempFile("hv-h-", ".tmp.csv");
		logger.trace("Saving merged hierarchy to: " + tmp.getAbsolutePath());
		HierarchyUtils.save(tmp.getAbsolutePath(), hierarchy.getMainHierarchy(), true, cfg.isHkWithTrueClass(),
				cfg.isHkWithInstanceNames(), true);

		// TODO: Check if the selection was an internal node or leaf node, and decide
		// where to load the new hierarchy based on that
		HierarchyVisualizer.spawnNewInstance(getParameterString(cfg), tmp, cboxTrueClass.isSelected(),
				cboxInstanceNames.isSelected());
	}
}
