package com.mommoo.flat.list;

import java.awt.*;
import java.util.*;
import java.util.List;

class CompIndexList<T extends Component> {
    private List<T> compList = new ArrayList<>();
    private Map<T, Integer> indexMapper = new HashMap<>();

    private void pushOneByOne(int index){
        for (int i = index ,size = getSize(); i < size; i++){
            indexMapper.put(compList.get(i), i);
        }
    }

    void addComp(T comp){
        compList.add(comp);
        indexMapper.put(comp, getSize() - 1);
    }

    void addAllComp(List<T> compList){
        compList.forEach(this::addComp);
    }

    void removeComp(int index){
        compList.remove(index);
        pushOneByOne(index);
    }

    int getIndex(Component comp){
        return indexMapper.get(comp);
    }

    T peek(int index){
        return compList.get(index);
    }

    int getSize(){
        return compList.size();
    }

    List<T> getList(){
        return Collections.unmodifiableList(compList);
    }
}
