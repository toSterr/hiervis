package pl.pwr.hiervis.visualisation;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.apache.commons.lang3.tuple.Pair;

import basic_hierarchy.interfaces.Instance;
import basic_hierarchy.interfaces.Node;
import pl.pwr.hiervis.core.ElementRole;
import pl.pwr.hiervis.core.HVConfig;
import pl.pwr.hiervis.core.HVConstants;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.util.Utils;
import prefuse.Constants;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.layout.AxisLabelLayout;
import prefuse.action.layout.AxisLayout;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.data.Table;
import prefuse.data.Tree;
import prefuse.data.query.NumberRangeModel;
import prefuse.render.AxisRenderer;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.Renderer;
import prefuse.render.RendererFactory;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.VisiblePredicate;


public class HierarchyProcessor
{
	public static Pair<Tree, TreeLayoutData> buildHierarchyTree( HVConfig config, Node sourceRoot )
	{
		Tree tree = new Tree();
		tree.addColumn( HVConstants.PREFUSE_NODE_ID_COLUMN_NAME, String.class );
		tree.addColumn( HVConstants.PREFUSE_NODE_ROLE_COLUMN_NAME, int.class );

		prefuse.data.Node treeRoot = tree.addRoot();
		treeRoot.setString( HVConstants.PREFUSE_NODE_ID_COLUMN_NAME, sourceRoot.getId() );
		treeRoot.setInt( HVConstants.PREFUSE_NODE_ROLE_COLUMN_NAME, ElementRole.OTHER.getNumber() );

		// path from node to root
		int maxTreeDepth = 0;
		int maxTreeWidth = 0;
		// TODO: In order to improve performance, it might be better to change this to a LinkedList, because
		// HashMap is only quick once it is already built, but the building process itself could be slow.
		HashMap<Integer, Integer> treeLevelToWidth = new HashMap<>();
		treeLevelToWidth.put( 0, 1 );

		Queue<Map.Entry<prefuse.data.Node, Node>> treeParentToSourceChild = new LinkedList<>();
		for ( Node sourceChild : sourceRoot.getChildren() ) {
			treeParentToSourceChild.add( new AbstractMap.SimpleEntry<prefuse.data.Node, Node>( treeRoot, sourceChild ) );
		}

		while ( !treeParentToSourceChild.isEmpty() ) {
			Entry<prefuse.data.Node, Node> treeParentAndSourceChild = treeParentToSourceChild.remove();
			Node sourceGroup = treeParentAndSourceChild.getValue();

			// Create a new tree node based on the source group
			prefuse.data.Node newNode = tree.addChild( treeParentAndSourceChild.getKey() );
			newNode.setString( HVConstants.PREFUSE_NODE_ID_COLUMN_NAME, sourceGroup.getId() );
			newNode.setInt( HVConstants.PREFUSE_NODE_ROLE_COLUMN_NAME, ElementRole.OTHER.getNumber() );

			// Compute new max tree depth
			int currentNodeDepth = newNode.getDepth();
			maxTreeDepth = Math.max( maxTreeDepth, currentNodeDepth );

			// Update the number of nodes on this tree level, for later processing
			Integer treeLevelWidth = treeLevelToWidth.get( currentNodeDepth );
			if ( treeLevelWidth == null ) {
				treeLevelToWidth.put( currentNodeDepth, 1 );
			}
			else {
				treeLevelToWidth.put( currentNodeDepth, treeLevelWidth + 1 );
			}

			// Enqueue this group's children for processing
			for ( Node child : sourceGroup.getChildren() ) {
				treeParentToSourceChild.add( new AbstractMap.SimpleEntry<prefuse.data.Node, Node>( newNode, child ) );
			}
		}

		// Tree is complete, now find the max tree width
		maxTreeWidth = Collections.max( treeLevelToWidth.values() );

		TreeLayoutData layoutData = new TreeLayoutData( config, tree, maxTreeDepth, maxTreeWidth );

		return Pair.of( tree, layoutData );
	}

	@SuppressWarnings("unchecked")
	public static void updateTreeNodeRoles( HVContext context, String currentGroupId )
	{
		Tree hierarchyTree = context.getTree();
		HVConfig config = context.getConfig();

		if ( context.isHierarchyDataLoaded() ) {
			boolean found = false;

			for ( int i = 0; i < hierarchyTree.getNodeCount(); ++i ) {
				prefuse.data.Node n = hierarchyTree.getNode( i );

				// Reset node role to 'other'
				n.setInt( HVConstants.PREFUSE_NODE_ROLE_COLUMN_NAME, ElementRole.OTHER.getNumber() );

				if ( !found && n.getString( HVConstants.PREFUSE_NODE_ID_COLUMN_NAME ).equals( currentGroupId ) ) {
					found = true;

					n.setInt( HVConstants.PREFUSE_NODE_ROLE_COLUMN_NAME, ElementRole.CURRENT.getNumber() );

					// Color child groups
					LinkedList<prefuse.data.Node> stack = new LinkedList<>();
					stack.add( n );

					while ( !stack.isEmpty() ) {
						prefuse.data.Node current = stack.removeFirst();
						current.setInt( HVConstants.PREFUSE_NODE_ROLE_COLUMN_NAME, ElementRole.CHILD.getNumber() );

						for ( Iterator<prefuse.data.Node> children = current.children(); children.hasNext(); ) {
							prefuse.data.Node child = children.next();
							stack.add( child );
						}
					}

					if ( config.isDisplayAllPoints() && n.getParent() != null ) {
						stack.clear();

						// IF the parent is empty, then we need to search up in the hierarchy because empty
						// parents are skipped, but displayed on output images
						prefuse.data.Node directParent = n.getParent();
						stack.add( directParent );

						while ( !stack.isEmpty() ) {
							prefuse.data.Node current = stack.removeFirst();
							current.setInt( HVConstants.PREFUSE_NODE_ROLE_COLUMN_NAME, ElementRole.INDIRECT_PARENT.getNumber() );

							if ( current.getParent() != null ) {
								stack.add( current.getParent() );
							}
						}

						directParent.setInt( HVConstants.PREFUSE_NODE_ROLE_COLUMN_NAME, ElementRole.DIRECT_PARENT.getNumber() );
					}
				}
			}
		}
	}

	public static Visualization createTreeVisualization( HVContext context )
	{
		return createTreeVisualization( context, null );
	}

	public static Visualization createTreeVisualization( HVContext context, String currentGroupId )
	{
		updateTreeNodeRoles( context, currentGroupId );

		Tree hierarchyTree = context.getTree();
		TreeLayoutData layoutData = context.getTreeLayoutData();
		HVConfig config = context.getConfig();

		int hierarchyImageWidth = config.getTreeWidth();
		int hierarchyImageHeight = config.getTreeHeight();

		Visualization vis = new Visualization();

		if ( context.isHierarchyDataLoaded() ) {
			vis.add( HVConstants.HIERARCHY_DATA_NAME, hierarchyTree );

			NodeRenderer r = new NodeRenderer( layoutData.getNodeSize(), config );
			DefaultRendererFactory drf = new DefaultRendererFactory( r );
			EdgeRenderer edgeRenderer = new EdgeRenderer( prefuse.Constants.EDGE_TYPE_LINE );
			drf.setDefaultEdgeRenderer( edgeRenderer );
			vis.setRendererFactory( drf );

			ColorAction edgesColor = new ColorAction(
				HVConstants.HIERARCHY_DATA_NAME + ".edges",
				VisualItem.STROKECOLOR,
				ColorLib.color( Color.lightGray )
			);

			NodeLinkTreeLayout treeLayout = new NodeLinkTreeLayout(
				HVConstants.HIERARCHY_DATA_NAME,
				layoutData.getTreeOrientation(),
				layoutData.getDepthSpace(),
				layoutData.getSiblingSpace(),
				layoutData.getSubtreeSpace()
			);
			treeLayout.setLayoutBounds( new Rectangle2D.Float( 0, 0, hierarchyImageWidth, hierarchyImageHeight ) );
			treeLayout.setRootNodeOffset( 0 );// 0.5*finalSizeOfNodes);//offset is set in order to show all nodes on images
			ActionList layout = new ActionList();
			layout.add( treeLayout );
			layout.add( new RepaintAction() );

			vis.putAction( HVConstants.HIERARCHY_DATA_NAME + ".edges", edgesColor );
			vis.putAction( HVConstants.HIERARCHY_DATA_NAME + ".layout", layout );
			// TODO we can here implement a heuristic that will check if after enlarging
			// the border lines (rows and columns) of pixels do not contain other values
			// than background colour. If so, then we are expanding one again, otherwise
			// we have appropriate size of image
		}

		return vis;
	}

	public static void layoutVisualization( Visualization vis )
	{
		// TODO: in run function a threads are used, so threads could be somehow used
		// to fill the images more efficiently
		vis.run( HVConstants.HIERARCHY_DATA_NAME + ".edges" );
		vis.run( HVConstants.HIERARCHY_DATA_NAME + ".layout" );

		Utils.waitUntilActivitiesAreFinished();
	}

	public static Visualization createInstanceVisualization( HVContext context, Node group, int dimX, int dimY )
	{
		HVConfig config = context.getConfig();
		int pointImageWidth = config.getInstanceWidth();
		int pointImageHeight = config.getInstanceHeight();

		// TODO: Make this a config property?
		int pointSize = 2;

		Visualization vis = new Visualization();

		String nameLabelsX = "labelsX";
		String nameLabelsY = "labelsY";

		vis.setRendererFactory(
			new RendererFactory() {
				Renderer rendererAxisX = new AxisRenderer( Constants.CENTER, Constants.FAR_BOTTOM );
				Renderer rendererAxisY = new AxisRenderer( Constants.FAR_LEFT, Constants.CENTER );
				Renderer rendererPoint = new PointRenderer( pointSize, config );


				public Renderer getRenderer( VisualItem item )
				{
					if ( item.isInGroup( nameLabelsX ) ) {
						return rendererAxisX;
					}
					if ( item.isInGroup( nameLabelsY ) ) {
						return rendererAxisY;
					}
					return rendererPoint;
				}
			}
		);

		Table table = new Table();
		table.addColumn( HVConstants.PREFUSE_NODE_PLOT_X_COLUMN_NAME, double.class );
		table.addColumn( HVConstants.PREFUSE_NODE_PLOT_Y_COLUMN_NAME, double.class );
		table.addColumn( HVConstants.PREFUSE_NODE_ROLE_COLUMN_NAME, int.class );
		table.addColumn( HVConstants.PREFUSE_NODE_LABEL_COLUMN_NAME, String.class );

		Node root = context.getHierarchy().getRoot();
		Rectangle2D bounds = Utils.calculateBoundingRectForCluster( root, dimX, dimY );

		for ( Instance i : group.getSubtreeInstances() ) {
			double sourceX = i.getData()[dimX];
			double sourceY = i.getData()[dimY];

			double normalizedX = Utils.normalize(
				sourceX,
				bounds.getMinX(), bounds.getMaxX(),
				0, pointImageWidth
			);
			double normalizedY = Utils.normalize(
				sourceY,
				bounds.getMinY(), bounds.getMaxY(),
				0, pointImageHeight
			);

			int row = table.addRow();
			// NOTE: Prefuse shows (0, 0) in bottom-left corner.
			// Might want to provide the option to invert Y for convenience?
			table.set( row, 0, normalizedX );
			table.set( row, 1, normalizedY );
			table.set( row, HVConstants.PREFUSE_NODE_ROLE_COLUMN_NAME, 0 );
			table.setString( row, HVConstants.PREFUSE_NODE_LABEL_COLUMN_NAME, i.getInstanceName() );
		}

		vis.addTable( HVConstants.INSTANCE_DATA_NAME, table );

		AxisLayout axisX = new AxisLayout(
			HVConstants.INSTANCE_DATA_NAME,
			HVConstants.PREFUSE_NODE_PLOT_X_COLUMN_NAME,
			Constants.X_AXIS, VisiblePredicate.TRUE
		);
		axisX.setRangeModel( new NumberRangeModel( 0, pointImageWidth, 0, pointImageWidth ) );

		AxisLayout axisY = new AxisLayout(
			HVConstants.INSTANCE_DATA_NAME,
			HVConstants.PREFUSE_NODE_PLOT_Y_COLUMN_NAME,
			Constants.Y_AXIS, VisiblePredicate.TRUE
		);
		axisY.setRangeModel( new NumberRangeModel( 0, pointImageHeight, 0, pointImageHeight ) );

		AxisLabelLayout labelX = new AxisLabelLayout( nameLabelsX, axisX );
		labelX.setNumberFormat( NumberFormat.getNumberInstance() );
		labelX.setScale( Constants.LINEAR_SCALE );

		AxisLabelLayout labelY = new AxisLabelLayout( nameLabelsY, axisY );
		labelX.setNumberFormat( NumberFormat.getNumberInstance() );
		labelX.setScale( Constants.LINEAR_SCALE );

		ActionList actions = new ActionList();
		actions.add( axisX );
		actions.add( axisY );
		actions.add( labelX );
		actions.add( labelY );
		actions.add(
			new ColorAction(
				HVConstants.INSTANCE_DATA_NAME,
				VisualItem.FILLCOLOR,
				ColorLib.color( Color.MAGENTA )
			)
		);
		actions.add( new RepaintAction() );

		vis.putAction( "draw", actions );

		return vis;
	}
}
