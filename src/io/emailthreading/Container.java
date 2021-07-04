package io.emailthreading;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * @author tianzhenjiu
 */
public class Container {
    Message message;            // (may be null)
    Set<Container> children=new HashSet<>();    // first child
    Container parent;
    Container next;

    /**
     *  if is null message return true
     * @return
     */
    public boolean isDummy(){
        return this.message==null;
    }

    /**
     * add child to this children list
     * @param child
     */
    public void addChild(Container child){
        if(child.parent!=null){
            child.parent.removeChild(child);
        }
        this.children.add(child);
        child.parent=this;
    }

    /**
     * remove child from this children list
     * @param child
     */
    public void removeChild(Container child){
        this.children.remove(child);
        child.parent=null;
    }

    /**
     * avoid loop dependency
     * for example  :a->b->c / c->a
     * @param ctr
     * @return
     */
    public boolean hasDescendant(Container ctr){

        Stack<Container>  stack=new  Stack<>();
        stack.push(this);
        HashSet<Container> seen=new HashSet();

        while(!stack.isEmpty()){
            Container node=stack.pop();
            if(node==ctr){
                return true;
            }
            seen.add(node);
            node.children.forEach((child)->{
                if(!seen.contains(child)){
                    stack.push(child);
                }
            });
        }
        return false;
    }

    @Override
    public String toString() {
        return "Container{" +
                "message=" + message +
                ", next=" + next +
                '}';
    }
}
