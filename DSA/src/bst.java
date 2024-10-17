
public class bst<E extends Comparable<E>> implements Tree<E> {
    protected TreeNode<E> root;
    protected int size = 0;

    public bst(){}

    public bst(E[] objects){
        for (int i = 0; i < objects.length; i++){
            add(objects[i]);
        }
    }

    @Override
    public boolean search(E e){
        TreeNode<E> current = root;
        while(current != null){
            if (e.compareTo(current.val) < 0){
                current = current.left;
            }
            else if (e.compareTo(current.val) > 0){
                current = current.right;
            }
            else{
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean insert(E e){
        TreeNode<E> parent = null;
        TreeNode<E> current = root;

        if (current == null){
            root = new TreeNode<>(e);
            return true;
        }

        while(current != null){
            parent = current;
            if (e.compareTo(current.val) < 0){
                current = current.left;
            }
            else if (e.compareTo(current.val) > 0){
                current = current.right;
            }
            else{
                return false;
            }
        }

        if (parent.left != null){
            parent.right = new TreeNode<>(e);
        }
        else{
            parent.left = new TreeNode<>(e);
        }
        size++;
        return true;
    }

    @Override
    public void inorder(){
        inorder(root);
    }

    public void inorder(TreeNode<E> root){
        if (root == null){
            return;
        }
        inorder(root.left);
        System.out.print(root.val + "\t");
        inorder(root.right);
    }

    @Override
    public void postorder(){
        postorder(root);
    }

    public void postorder(TreeNode<E> root){
        if (root == null){
            return;
        }
        postorder(root.left);
        postorder(root.right);
        System.out.print(root.val + "\t");
    }

    @Override
    public void preorder(){
        postorder(root);
    }

    public void preorder(TreeNode<E> root){
        if (root == null){
            return;
        }
        System.out.print(root.val + "\t");
        preorder(root.left);
        preorder(root.right);
    }
}

class TreeNode<E>{
    public E val;
    public TreeNode<E> left;
    public TreeNode<E> right;

    public TreeNode (E element){
        val = element;
    }
}