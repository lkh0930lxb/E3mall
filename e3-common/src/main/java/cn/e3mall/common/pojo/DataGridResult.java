package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: Likh
 * @CreateDate: 2018/3/14 10:43
 */
public class DataGridResult implements Serializable{
    private long total;
    private List rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
