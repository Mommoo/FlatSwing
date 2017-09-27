package com.mommoo.flat.list;

import java.awt.*;
import java.util.*;
import java.util.List;

class CompIndexList{
    private ArrayList<Component> compList = new ArrayList<>();
    private Map<Component, Integer> indexMapper = new HashMap<>();

    private void pushOneByOne(int index){
        for (int i = index ,size = getSize(); i < size; i++){
            indexMapper.put(compList.get(i), i);
        }
    }

    void addComp(Component comp){
        compList.add(comp);
        indexMapper.put(comp, getSize() - 1);
    }

    void addComp(Component comp, int index){
        compList.add(index, comp);
        indexMapper.put(comp,getSize()-1);
        pushOneByOne(index);
    }

    void addAllComp(List<Component> compList){
        compList.forEach(this::addComp);
    }

    void removeComp(int index){
        indexMapper.remove(compList.get(index));
        compList.remove(index);
        pushOneByOne(index);
    }

    void removeAll(){
        for (Component component : compList){
            indexMapper.remove(component);
        }

        compList.clear();
    }

    int getIndex(Component comp){
        return indexMapper.get(comp);
    }

    Component peek(int index){
        return compList.get(index);
    }

    int getSize(){
        return compList.size();
    }

    List<Component> getList(){
        return Collections.unmodifiableList(compList);
    }
}
