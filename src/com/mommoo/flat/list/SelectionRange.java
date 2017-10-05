package com.mommoo.flat.list;

import com.mommoo.flat.list.model.SelectionModel;

class SelectionRange {
    private static final int INVALID_INDEX = -1;
    private final SelectionModel SELECTION_MODEL;
    private int beginIndex = INVALID_INDEX;
    private int endIndex   = INVALID_INDEX;

    SelectionRange(SelectionModel model){
        SELECTION_MODEL = model;
    }

    void saveRange(int index1, int index2){
        this.beginIndex = Math.max(INVALID_INDEX, Math.min(index1, index2));
        this.endIndex   = Math.max(INVALID_INDEX, Math.max(index1, index2));
    }

    int getBeginIndex(){
        return this.beginIndex;
    }

    int getEndIndex(){
        return this.endIndex;
    }

    int[] getRange(){
        return new int[]{this.beginIndex, this.endIndex};
    }

    void setInValidRange(){
        this.beginIndex = this.endIndex = INVALID_INDEX;
    }

    boolean isValid(){
        return this.beginIndex != INVALID_INDEX && this.endIndex != INVALID_INDEX;
    }

    void inspectValidSelection(int removedIndex){
        switch(whatIndexDirection(removedIndex)){
            case DOWN_WARD :
                inspectValidIndex();
                return;

            case BETWEEN :
                if (SELECTION_MODEL.isSingleSelectionMode()){
                    setInValidRange();
                } else {
                    this.endIndex--;
                    inspectValidIndex();
                }

                return;

            case UP_WARD :
                this.beginIndex--;
                this.endIndex--;
                inspectValidIndex();

                return;
        }

    }

    private void inspectValidIndex(){
        if (!isValidIndex()) {
            setInValidRange();
        }
    }

    private boolean isValidIndex(){
        int itemSize = SELECTION_MODEL.getItemSize();
        boolean isInItemSize = 0 <= beginIndex && beginIndex < itemSize && endIndex < itemSize && 0 <= endIndex;
        boolean isValid = beginIndex != INVALID_INDEX && endIndex != INVALID_INDEX && endIndex >= beginIndex;
        return isInItemSize && isValid;
    }

    private IndexDirection whatIndexDirection(int index){
        if (beginIndex > index && index < endIndex){
            return IndexDirection.UP_WARD;
        }

        else if (beginIndex <= index && index <= endIndex){
            return IndexDirection.BETWEEN;
        }

        else {
            return IndexDirection.DOWN_WARD;
        }
    }

    @Override
    public String toString() {
        return "beginIndex : " + beginIndex + " , endIndex : " + endIndex;
    }

    private enum IndexDirection{
        UP_WARD,
        BETWEEN,
        DOWN_WARD
    }
}
