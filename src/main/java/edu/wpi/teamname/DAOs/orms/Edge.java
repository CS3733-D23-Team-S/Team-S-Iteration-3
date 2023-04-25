package edu.wpi.teamname.DAOs.orms;

import edu.wpi.teamname.DAOs.IDataPack;
import lombok.Getter;
import lombok.Setter;

public class Edge implements IDataPack {
	@Getter
	@Setter
	private Node startNode;
	@Getter
	@Setter
	private Node endNode;

	public Edge(Node sN, Node eN) {
		startNode = sN;
		endNode = eN;
	}

	public int getStartNodeID() {
		return startNode.getNodeID();
	}

	public int getEndNodeID() {
		return endNode.getNodeID();
	}

	public void updateEdge(Node sN, Node eN) {
		startNode = sN;
		endNode = eN;
	}

	@Override
	public String toString() {
		return "Edge{"
			   + "startNode = "
			   + startNode.getNodeID()
			   + ", endNode = "
			   + endNode.getNodeID()
			   + '}';
	}

	@Override
	public String toCSVString() {
		return startNode.getNodeID() + "," + endNode.getNodeID();
	}
}
