package com.app.org.refresh.interfaceclass;

import java.util.List;

public interface IEditEvent<T extends Object>{
    List<T> getEditList();
    void sendEditList();
    void openEdit(boolean isOpenEdit);
}