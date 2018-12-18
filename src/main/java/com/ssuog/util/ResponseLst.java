package com.ssuog.util;

import java.util.ArrayList;
import java.util.List;

import com.ssuog.po.EServiceResponseCode;

public class ResponseLst<E> extends Response {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7785297230238378328L;

	public ResponseLst() {
		this(EServiceResponseCode.C200);
    }

    public ResponseLst(EServiceResponseCode code) {
        super(code);
    }

    /**
     * object list
     */
    private List<E> data;

    public List<E> getData() {
        return data;
    }

    public void setData(List<E> data) {
        this.data = data;
    }

    public synchronized void addObjToData(E obj) {
        if (null == obj) {
            return;
        }
        if (null == data) {
            data = new ArrayList<E>();
        }
        data.add(obj);
        total = total + 1;
        count = data.size();
    }

    public synchronized <T extends E> void addListToData(List<T> data) {
        if (null == data || 0 >= data.size()) {
            return;
        }

        for (int i = 0; i < data.size(); i++) {
            addObjToData(data.get(i));
        }
    }
}
