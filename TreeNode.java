package Tree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import Array_list.Array_list;

public class TreeNode<T> implements Iterable<TreeNode<T>> {

	public T data;
	public TreeNode<T> parent;
	public TreeNode<T> next;
	public List<TreeNode<T>> children;

	public boolean isRoot() {
		return parent == null;
	}

	public boolean isLeaf() {
		return children.size() == 0;
	}

	public List<TreeNode<T>> elementsIndex;

	public TreeNode(T data) {
		this.data = data;
		this.children = new LinkedList<TreeNode<T>>();
		this.elementsIndex = new LinkedList<TreeNode<T>>();
		this.elementsIndex.add(this);
	}

	public TreeNode<T> addChild(T child) {
		TreeNode<T> childNode = new TreeNode<T>(child);
		childNode.parent = this;
		this.children.add(childNode);
		this.registerChildForSearch(childNode);
		return childNode;
	}

	public int getLevel() {
		if (this.isRoot())
			return 0;
		else
			return parent.getLevel() + 1;
	}

	private void registerChildForSearch(TreeNode<T> node) {
		elementsIndex.add(node);
		if (parent != null)
			parent.registerChildForSearch(node);
	}

	public TreeNode<T> findTreeNode(Comparable<Array_list> searchCriteria) {
		for (TreeNode<T> element : this.elementsIndex) {
			Array_list elData = (Array_list) element.data;
			if (searchCriteria.compareTo(elData) == 0)
				return element;
		}

		return null;
	}

	@Override
	public String toString() {
		return data != null ? data.toString() : "[data null]";
	}

	@Override
	public Iterator<TreeNode<T>> iterator() {
		TreeNodeIter<T> iter = new TreeNodeIter<T>(this);
		return iter;
	}

	public TreeNode<T> next(int i) {
		// TODO Auto-generated method stub
		if (i < this.elementsIndex.size()) {
			this.next = this.elementsIndex.get(i);
			// System.out.println(this.elementsIndex.get(1));
			return this.next;
		} else
			return null;
	}

	public TreeNode<T> search(int i) {
		// TODO Auto-generated method stub
		if (this.children.size() != 0) {
			if (this.children.size() > i) {
				this.next = this.children.get(i);
				return this.next;
			}
		}
		return null;
	}

	public void print_linked_list() {
		for (int i = 0; i < this.elementsIndex.size(); i++) {
			System.out.println(this.elementsIndex.get(i));
		}
	}
}
