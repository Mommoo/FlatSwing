package flat.layout.linear;

public enum Orientation {
    VERTICAL,
    HORIZONTAL;

    private int gap;

    void setGap(int gap){
        this.gap = gap;
    }

    int getGap(){
        return this.gap;
    }
}
