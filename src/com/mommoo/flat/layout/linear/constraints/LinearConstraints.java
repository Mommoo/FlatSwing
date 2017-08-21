package com.mommoo.flat.layout.linear.constraints;

public class LinearConstraints implements Cloneable{
    private int weight = 0;
    private LinearSpace linearSpace = LinearSpace.WRAP_CONTENT;

    public LinearConstraints setWeight(int weight){
        this.weight = weight;
        return this;
    }

    public int getWeight(){
        return weight;
    }

    public LinearConstraints setLinearSpace(LinearSpace linearSpace){
        this.linearSpace = linearSpace;
        return this;
    }

    public LinearSpace getLinearSpace(){
        return linearSpace;
    }

    @Override
    public LinearConstraints clone() {
        try {
            return (LinearConstraints)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            System.out.println("clone failed");
            return this;
        }
    }

    @Override
    public String toString() {
        return  "[weight]      : " + weight+"\n" +
                "[LinearSpace] : " + linearSpace;
    }
}
