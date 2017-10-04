package com.mommoo.flat.list;

import java.awt.*;
import java.util.*;
import java.util.List;

class CompIndexList<T extends Component>{
    private ArrayList<T> compList = new ArrayList<>();
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

    void addComp(T comp, int index){
        compList.add(index, comp);
        indexMapper.put(comp,getSize()-1);
        pushOneByOne(index);
    }

    void addAllComp(List<T> compList){
        compList.forEach(this::addComp);
    }

    void removeComp(int index){
        indexMapper.remove(compList.get(index));
        compList.remove(index);
        pushOneByOne(index);
    }

    void removeAll(){
        for (T component : compList){
            indexMapper.remove(component);
        }

        compList.clear();
    }

    int getIndex(T comp){
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
