package pl.pwr.hiervis.dimensionReduction;

import java.util.HashMap;
import java.util.LinkedList;

import basic_hierarchy.common.Constants;
import basic_hierarchy.implementation.BasicHierarchy;
import basic_hierarchy.implementation.BasicInstance;
import basic_hierarchy.implementation.BasicNode;
import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.interfaces.Instance;
import basic_hierarchy.interfaces.Node;

public class TestCommon {
    public static final double DOUBLE_COMPARISION_DELTA = 1e-9;

    public static String getIDOfChildCluster(String parentId, int childNumber) {
	return parentId + Constants.HIERARCHY_BRANCH_SEPARATOR + childNumber;
    }

    public static Hierarchy getTwoTwoGroupsHierarchy() {
	String rootId = basic_hierarchy.common.Constants.ROOT_ID;
	String childId = TestCommon.getIDOfChildCluster(rootId, 0);
	LinkedList<Instance> firstClusterInstances = new LinkedList<>();
	firstClusterInstances.add(new BasicInstance("11", rootId, new double[] { 1.0, 2.0, 1.0, 2.0 }, rootId));
	firstClusterInstances.add(new BasicInstance("12", rootId, new double[] { 3.0, 4.0, 3.0, 4.0 }, rootId));
	BasicNode firstCluster = new BasicNode(rootId, null, new LinkedList<Node>(), firstClusterInstances, false);

	LinkedList<Instance> secondClusterInstances = new LinkedList<Instance>();
	secondClusterInstances.add(new BasicInstance("21", childId, new double[] { 1.5, 2.5, 1.5, 2.5 }, childId));
	secondClusterInstances.add(new BasicInstance("22", childId, new double[] { 3.5, 4.5, .5, 4.5 }, rootId));
	BasicNode secondCluster = new BasicNode(childId, firstCluster, new LinkedList<Node>(), secondClusterInstances,
		false);

	firstCluster.addChild(secondCluster);

	LinkedList<Node> groups = new LinkedList<>();
	groups.add(firstCluster);
	groups.add(secondCluster);
	HashMap<String, Integer> eachClassWithCount = new HashMap<>();
	eachClassWithCount.put(rootId, 3);
	eachClassWithCount.put(childId, 1);

	return new BasicHierarchy(firstCluster, groups, eachClassWithCount, 4);
    }
}
