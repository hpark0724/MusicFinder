import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.lang.NullPointerException;

/**
 * This class implements RedBlackTreeInterfaceAE and contains the Red Black Tree
 * algorithm methods and the methods that travese the range of RBT
 */
public class RedBlackTree<T extends Comparable<T>> implements RedBlackTreeInterface<T> {

    /**
     * This class represents a node holding a single value within a binary tree.
     */
    protected static class Node<T> {
        public T data;
        // The context array stores the context of the node in the tree:
        // - context[0] is the parent reference of the node,
        // - context[1] is the left child reference of the node,
        // - context[2] is the right child reference of the node.
        // The @SupressWarning("unchecked") annotation is used to supress an unchecked
        // cast warning. Java only allows us to instantiate arrays without generic
        // type parameters, so we use this cast here to avoid future casts of the
        // node type's data field.
        @SuppressWarnings("unchecked")
        public Node<T>[] context = (Node<T>[]) new Node[3];
        public int blackHeight = 0;

        public Node(T data) {
            this.data = data;
        }

        /**
         * @return true when this node has a parent and is the right child of that
         *         parent, otherwise
         *         return false
         */
        public boolean isRightChild() {
            return context[0] != null && context[0].context[2] == this;
        }
    }

    protected Node<T> root; // reference to root node of tree, null when empty
    protected int size = 0; // the number of values in the tree

    /**
     * Performs a naive insertion into a binary search tree: adding the input data
     * value to a new
     * node in a leaf position within the tree. After this insertion, no attempt is
     * made to
     * restructure or balance the tree. This tree will not hold null references, nor
     * duplicate data
     * values.
     *
     * @param data to be added into this binary search tree
     * @return true if the value was inserted, false if not
     * @throws NullPointerException     when the provided data argument is null
     * @throws IllegalArgumentException when data is already contained in the tree
     */

    public boolean insert(T data) throws NullPointerException, IllegalArgumentException {
        // null references cannot be stored within this tree
        if (data == null)
            throw new NullPointerException("This RedBlackTree cannot store null references.");

        Node<T> newNode = new Node<T>(data);
        if (this.root == null) {
            // add first node to an empty tree
            root = newNode;
            root.blackHeight = 1;
            size++;
            return true;
        } else {
            // insert into subtree
            Node<T> current = this.root;
            while (true) {
                int compare = newNode.data.compareTo(current.data);
                if (compare == 0) {
                    throw new IllegalArgumentException("This RedBlackTree already contains value "
                            + data.toString());
                } else if (compare < 0) {
                    // insert in left subtree
                    if (current.context[1] == null) {
                        // empty space to insert into
                        current.context[1] = newNode;
                        newNode.context[0] = current;
                        // Resolves any red-black tree property violations when each new node is
                        // inserted
                        enforceRBTreePropertiesAfterInsert(newNode);
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.context[1];
                    }
                } else {
                    // insert in right subtree
                    if (current.context[2] == null) {
                        // empty space to insert into
                        current.context[2] = newNode;
                        newNode.context[0] = current;
                        // Resolves any red-black tree property violations when each new node is
                        // inserted
                        enforceRBTreePropertiesAfterInsert(newNode);
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.context[2];
                    }
                }
            }
        }
    }


    /**
     * Resolves any red-black tree property violations when each
     * new node is inserted into the red-black tree
     *
     * @param newChild The new node to insert into the tree
     */
    protected void enforceRBTreePropertiesAfterInsert(Node<T> newChild) {
        // base case
        if (newChild.blackHeight == 1 || newChild.context[0] == null
                || newChild.context[0].context[0] == null) {
            return;
        }
        // if child's parent is red node
        else if (newChild.context[0].blackHeight == 0) {
            // if child node is right child
            if (newChild.isRightChild()) {
                // case 1
                if (newChild.context[0].isRightChild()
                        && (newChild.context[0].context[0].context[1] == null
                                || newChild.context[0].context[0].context[1].blackHeight == 1)) {
                    rotate(newChild.context[0], newChild.context[0].context[0]); // rotate left
                    newChild.context[0].blackHeight = 1; // swap color
                    newChild.context[0].context[1].blackHeight = 0; // swap color
                }
                // case 2
                else if (!newChild.context[0].isRightChild() &&
                        (newChild.context[0].context[0].context[2] == null
                                || newChild.context[0].context[0].context[2].blackHeight == 1)) {
                    rotate(newChild, newChild.context[0]);
                    enforceRBTreePropertiesAfterInsert(newChild.context[1]);
                }
                // case 3 : if parent node is right child
                else if (newChild.context[0].isRightChild() &&
                        newChild.context[0].context[0].context[1].blackHeight == 0) {
                    newChild.context[0].blackHeight = 1;
                    newChild.context[0].context[0].blackHeight = 0;
                    newChild.context[0].context[0].context[1].blackHeight = 1;
                    this.root.blackHeight = 1;
                    enforceRBTreePropertiesAfterInsert(newChild.context[0].context[0]);
                }
                // case 3 : if parent node is left child
                else if (!newChild.context[0].isRightChild() &&
                        newChild.context[0].context[0].context[2].blackHeight == 0) {
                    newChild.context[0].blackHeight = 1;
                    newChild.context[0].context[0].blackHeight = 0;
                    newChild.context[0].context[0].context[2].blackHeight = 1;
                    this.root.blackHeight = 1;
                    enforceRBTreePropertiesAfterInsert(newChild.context[0].context[0]);
                }

            }

            // if child node is left child
            else if (!newChild.isRightChild()) {
                // case 1
                if (!newChild.context[0].isRightChild() &&
                        (newChild.context[0].context[0].context[2] == null ||
                                newChild.context[0].context[0].context[2].blackHeight == 1)) {
                    rotate(newChild.context[0], newChild.context[0].context[0]); // rotate right
                    newChild.context[0].blackHeight = 1; // swap color
                    newChild.context[0].context[2].blackHeight = 0; // swap color
                }
                // case 2
                else if (newChild.context[0].isRightChild() &&
                        (newChild.context[0].context[0].context[1] == null ||
                                newChild.context[0].context[0].context[1].blackHeight == 1)) {
                    rotate(newChild, newChild.context[0]); // rotate right
                    enforceRBTreePropertiesAfterInsert(newChild.context[2]);
                }
                // case 3 : if parent node is left child
                else if (!newChild.context[0].isRightChild() &&
                        newChild.context[0].context[0].context[1].blackHeight == 0) {
                    newChild.context[0].blackHeight = 1;
                    newChild.context[0].context[0].blackHeight = 0;
                    newChild.context[0].context[0].context[2].blackHeight = 1;
                    this.root.blackHeight = 1;
                    enforceRBTreePropertiesAfterInsert(newChild.context[0].context[0]);
                }
                // case 3 : if parent node is right child
                else if (newChild.context[0].isRightChild() &&
                        newChild.context[0].context[0].context[1].blackHeight == 0) {
                    newChild.context[0].blackHeight = 1;
                    newChild.context[0].context[0].blackHeight = 0;
                    newChild.context[0].context[0].context[1].blackHeight = 1;
                    this.root.blackHeight = 1;
                    enforceRBTreePropertiesAfterInsert(newChild.context[0].context[0]);
                }
            }

        }

    }

    /**
     * Performs the rotation operation on the provided nodes within this tree. When
     * the provided
     * child is a left child of the provided parent, this method will perform a
     * right rotation. When
     * the provided child is a right child of the provided parent, this method will
     * perform a left
     * rotation. When the provided nodes are not related in one of these ways, this
     * method will
     * throw an IllegalArgumentException.
     *
     * @param child  is the node being rotated from child to parent position
     *               (between these two node
     *               arguments)
     * @param parent is the node being rotated from parent to child position
     *               (between these two node
     *               arguments)
     * @throws IllegalArgumentException when the provided child and parent node
     *                                  references are not
     *                                  initially (pre-rotation) related that way
     */
    private void rotate(Node<T> child, Node<T> parent) throws IllegalArgumentException {

        if(child == null || parent == null){
            throw new IllegalArgumentException(" child is null");
        }

        // if the root is parent node
        if (parent == root) {
            // when the child node is right child of the parent, rotate left
            if (child.isRightChild()) {
                parent.context[2] = child.context[1];               
                if(child.context[1] != null){
                    child.context[1].context[0] = parent;
                }
                parent.context[0] = child;
                child.context[1] = parent;
                root = child;

            }
            // when child node is left child of the parent, rotate right
            else if (!child.isRightChild()) {
                parent.context[1] = child.context[2];
                if(child.context[2] != null){
                child.context[2].context[0] = parent;
            }  
                parent.context[0] = child;
                child.context[2] = parent;    
                root = child;
                
               

            } else {
                throw new IllegalArgumentException("provided child and parent node references " +
                        "are" + " not initially related");
            }
        }

        // if the root is not parent node
        else {
            // if the child node is right child of the parent, rotate left
            if (child.isRightChild()) {
                // when the parent node is right child of the root
                if (parent.isRightChild()) {
                    parent.context[0].context[2] = child;
                    parent.context[2] = child.context[1];
                    if(child.context[1] != null){
                        child.context[1].context[0] = parent;
                    }
                     child.context[0] = parent.context[0];
                     child.context[1] = parent;
                     parent.context[0] = child;

                }
                // when the parent node is left child of the root
                else if (!parent.isRightChild()) {
                    parent.context[0].context[1] = child;
                    parent.context[2] = child.context[1];
                    if(child.context[1] != null){
                        child.context[1].context[0] = parent;
                    }
                     child.context[0] = parent.context[0];
                     child.context[1] = parent;
                     parent.context[0] = child;
                }
            }

            // if child node is left child of the parent, rotate right
            else if (!child.isRightChild()) {
                // when the parent node is right child of the root
                if (parent.isRightChild()) {
                    parent.context[0].context[2] = child;
                    parent.context[1] = child.context[2];
                    if(child.context[2] != null){
                        child.context[2].context[0] = parent;
                    }
                    child.context[0] = parent.context[0];
                    child.context[2] = parent;
                    parent.context[0] = child;
                }
                // when the parent node is left child of the root
                else if (!parent.isRightChild()) {
                    parent.context[0].context[1] = child;
                    parent.context[1] = child.context[2];
                    if(child.context[2] != null){
                        child.context[2].context[0] = parent;
                    }
                    child.context[0] = parent.context[0];
                    child.context[2] = parent;
                    parent.context[0] = child;
                }
            } else {
                throw new IllegalArgumentException("provided child and parent node references " +
                        "are" + " not initially related");
            }
        }

    }


    /**
     * Removes the value data from the tree if the tree contains the value. This
     * method rebalance the tree after the removal
     *
     * @return true if the value was remove, false if it didn't exist
     * @throws NullPointerException     when the provided data argument is null
     * @throws IllegalArgumentException when data is not stored in the tree
     */
    public boolean remove(T data) throws NullPointerException, IllegalArgumentException {
        // null references will not be stored within this tree
        if (data == null) {
            throw new NullPointerException("This RedBlackTree cannot store null references.");
        } else {
            Node<T> nodeWithData = this.findNodeWithData(data);
            // throw exception if node with data does not exist
            if (nodeWithData == null) {
                throw new IllegalArgumentException("The following value is not in the tree and " +
                        "cannot be deleted: " + data.toString());
            }
            boolean hasRightChild = (nodeWithData.context[2] != null);
            boolean hasLeftChild = (nodeWithData.context[1] != null);
            if (hasRightChild && hasLeftChild) {
                // has 2 children
                Node<T> successorNode = this.findMinOfRightSubtree(nodeWithData);
                // replace value of node with value of successor node
                nodeWithData.data = successorNode.data;
                // remove successor node
                if (successorNode.context[2] == null) {
                    // successor has no children, replace with null
                    this.replaceNode(successorNode, null);
                } else {
                    // successor has a right child, replace successor with its child
                    this.replaceNode(successorNode, successorNode.context[2]);
                }
            } else if (hasRightChild) {
                // only right child, replace with right child
                this.replaceNode(nodeWithData, nodeWithData.context[2]);
            } else if (hasLeftChild) {
                // only left child, replace with left child
                this.replaceNode(nodeWithData, nodeWithData.context[1]);
            } else {
                // no children, replace node with a null node
                this.replaceNode(nodeWithData, null);
            }
            this.size--;
            //rebalance the tree after removal
            enforceRBTreePropertiesAfterRemove();
            return true;
        }
    }

    /**
     * The helper method that keeps RBTTreProperties after
     * remove
     *
     * @param newChild The new node to insert into the tree
     */
        protected void enforceRBTreePropertiesAfterRemove(){
        List<T> list = new ArrayList<T>();
        // store all the nodes in the tree to the the list
        inOrderListHelper(root, null); 
        clear();
        for(int i = 0; i< list.size(); i++){
            // insert the node into the RBT tree
            insert(list.get(i));
        }

    }


    /**
     * Helper method that will return the inorder successor of a node with two
     * children.
     *
     * @param node the node to find the successor for
     * @return the node that is the inorder successor of node
     */
    protected Node<T> findMinOfRightSubtree(Node<T> node) {
        if (node.context[1] == null && node.context[2] == null) {
            throw new IllegalArgumentException("Node must have two children");
        }
        // take a steop to the right
        Node<T> current = node.context[2];
        while (true) {
            // then go left as often as possible to find the successor
            if (current.context[1] == null) {
                // we found the successor
                return current;
            } else {
                current = current.context[1];
            }
        }
    }

    /**
     * Helper method that will replace a node with a replacement node. The
     * replacement node may be
     * null to remove the node from the tree.
     *
     * @param nodeToReplace   the node to replace
     * @param replacementNode the replacement for the node (may be null)
     */
    protected void replaceNode(Node<T> nodeToReplace, Node<T> replacementNode) {
        if (nodeToReplace == null) {
            throw new NullPointerException("Cannot replace null node.");
        }
        if (nodeToReplace.context[0] == null) {
            // we are replacing the root
            if (replacementNode != null)
                replacementNode.context[0] = null;
            this.root = replacementNode;
        } else {
            // set the parent of the replacement node
            if (replacementNode != null)
                replacementNode.context[0] = nodeToReplace.context[0];
            // do we have to attach a new left or right child to our parent?
            if (nodeToReplace.isRightChild()) {
                nodeToReplace.context[0].context[2] = replacementNode;
            } else {
                nodeToReplace.context[0].context[1] = replacementNode;
            }
        }
    }

    /**
     * Helper method that will return the node in the tree that contains a specific
     * value. Returns
     * null if there is no node that contains the value.
     *
     * @return the node that contains the data, or null of no such node exists
     */
    protected Node<T> findNodeWithData(T data) {
        Node<T> current = this.root;
        while (current != null) {
            int compare = data.compareTo(current.data);
            if (compare == 0) {
                // we found our value
                return current;
            } else if (compare < 0) {
                // keep looking in the left subtree
                current = current.context[1];
            } else {
                // keep looking in the right subtree
                current = current.context[2];
            }
        }
        // we're at a null node and did not find data, so it's not in the tree
        return null;
    }

    /**
     * This method performs an inorder traversal of the tree. The string
     * representations of each
     * data value within this tree are assembled into a comma separated string
     * within brackets
     * (similar to many implementations of java.util.Collection, like
     * java.util.ArrayList,
     * LinkedList, etc).
     *
     * @return string containing the ordered values of this tree (in-order
     *         traversal)
     */
    public String toInOrderString() {
        // generate a string of all values of the tree in (ordered) in-order
        // traversal sequence
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        if (this.root != null) {
            Stack<Node<T>> nodeStack = new Stack<>();
            Node<T> current = this.root;
            while (!nodeStack.isEmpty() || current != null) {
                if (current == null) {
                    Node<T> popped = nodeStack.pop();
                    sb.append(popped.data.toString());
                    if (!nodeStack.isEmpty() || popped.context[2] != null)
                        sb.append(", ");
                    current = popped.context[2];
                } else {
                    nodeStack.add(current);
                    current = current.context[1];
                }
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    /**
     * Checks whether the tree contains the value *data*.
     *
     * @param data the data value to test for
     * @return true if *data* is in the tree, false if it is not in the tree
     */
    public boolean contains(T data) {
        // null references will not be stored within this tree
        if (data == null) {
            throw new NullPointerException("This RedBlackTree cannot store null references.");
        } else {
            Node<T> nodeWithData = this.findNodeWithData(data);
            // return false if the node is null, true otherwise
            return (nodeWithData != null);
        }
    }

    /**
     * Get the size of the tree (its number of nodes).
     *
     * @return the number of nodes in the tree
     */
    public int size() {
        return this.size;
    }

    /**
     * Method to check if the tree is empty (does not contain any node).
     *
     * @return true of this.size() return 0, false if this.size() > 0
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * return a object in the RBT that matches the data that user searched for
     * by using compareTo method
     *
     * @param data the data value to search for
     * @return returns a object that contains the data
     * @throws NullPointerException     when the provided data argument is null
     * @throws IllegalArgumentException when data is not stored in the tree
     * @throws IllegalStateException    when the tree is empty
     */
    public T get(T data) throws IllegalArgumentException, IllegalStateException, NullPointerException {

        // throw exception when data is null
        if (data == null) {
            throw new NullPointerException("Data is null");
        }
        // throw exception when tree is empty
        if (isEmpty()) {
            throw new IllegalStateException("The tree is empty");
        }

        Node<T> current = root;
        int compareVal = 0;

        while (current != null) {
            compareVal = data.compareTo(current.data);

            // when input matches the data stored in the current node, return the data
            if (compareVal == 0) {
                return current.data;
            }
            // when the current node data is bigger, move to left child
            else if (compareVal < 0) {
                current = current.context[1];
            }
            // when the current node data is smaller, move to right child
            else {
                current = current.context[2];
            }
        }

        // throw exception when the data is not found in the tree
        throw new IllegalArgumentException("The data is not found in the tree");

    }

    /**
     * clear the RBT
     */
    public void clear() {
        root = null;
    }

    /**
     * return a list of songs that start with what the user inputted
     *
     * @param key the string that user inputted
     * @return returns a song object that starts with what user inputted
     * @throws NullPointerException when the provided data argument is null
     */
    public List<T> findByInput(String key) throws NullPointerException {

        // when the user input is null, throw exception
        if (key == null) {
            throw new NullPointerException("key is null");
        }

        List<T> list = new ArrayList<T>();
        List<T> result = new ArrayList<T>();
        // stores all the node data in the RBT into the list
        inOrderListHelper(root, list);

        for (int i = 0; i < list.size(); i++) {
            char ch1 = list.get(i).toString().charAt(0);
            char ch2 = key.toString().charAt(0);
            // when the data in the list starts with the user input, add the
            // data into the list
            if (Character.toUpperCase(ch1) == (Character.toUpperCase(ch2))) {
                result.add(list.get(i));
            }
        }
        // return the list that stores the data that starts with what user inputted
        return result;

    }

    /**
     * The helper method that store all the nodes data in the list
     *
     * @param node the node which is stored in the list first
     */
    private void inOrderListHelper(Node<T> node, List<T> list) {

        // return when it reaches to the base case
        if (node == null) {
            return;
        }

        // traverse each node's the left child in the RBT
        inOrderListHelper(node.context[1], list);

        // add all traversed node data to the list
        list.add(node.data);

        // traverse each node's right child in the RBT
        inOrderListHelper(node.context[2], list);

    }

    /**
     * returns the number of black nodes in the RBT
     *
     */
    public int getNumBlackNodes() {
        return countBlackNodes(root);
    }

    /**
     * The helper method that counts the number of black node
     *
     * @param node the node to start with to traverse the tree in order
     */
    private int countBlackNodes(Node<T> node) {
        // return when it reaches to the base case
        if (node == null) {
            return 0;
        }
        int blackCount = 0;
        // increment when the node is black
        if (node.blackHeight == 1) {
            blackCount++;
        }
        // traverse left child of RBT
        blackCount += countBlackNodes(node.context[1]);
        // traverse right child of RBT
        blackCount += countBlackNodes(node.context[2]);

        // return the number of black node of RBT
        return blackCount;
    }

    /**
     * returns the number of red nodes in the RBT
     *
     */
    public int getNumRedNodes() {
        return countRedNodes(root);

    }

    /**
     * The helper method that counts the number of number node
     *
     * @param node the node to start with to traverse the tree in order
     */
    private int countRedNodes(Node<T> node) {
        // return when it reaches to the base case
        if (node == null) {
            return 0;
        }
        int redCount = 0;
        // increment when the node is red
        if (node.blackHeight == 0) {
            redCount++;
        }
        // traverse left child of RBT
        redCount += countBlackNodes(node.context[1]);
        // traverse right child of RBT
        redCount += countBlackNodes(node.context[2]);

        // return the number of red node of RBT
        return redCount;
    }

}
