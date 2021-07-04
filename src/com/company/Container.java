package com.company;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Container {
    Message message;            // (may be null)
    Set<Container> children=new HashSet<>();    // first child
    Container parent;
    Container next;

    public boolean isDummy(){
        return this.message==null;
    }

    public void addChild(Container child){
        if(child.parent!=null){
            child.parent.removeChild(child);
        }
        this.children.add(child);
        child.parent=this;
    }

    public void removeChild(Container child){
        this.children.remove(child);
        child.parent=null;
    }

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
}
