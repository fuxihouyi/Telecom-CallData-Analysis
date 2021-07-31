package com.atguigu.ct.consumer.coprocessor;

import com.atguigu.ct.common.bean.BaseDao;
import com.atguigu.ct.common.constant.Names;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;

import java.io.IOException;

/**
 * 使用协处理器保存被叫用户的数据
 *
 * 协处理器的使用
 * 1.创建类
 * 2.让表直到协处理器类（和表有关联）
 * 3.将项目打成jar包并发布到hbase中（且与其关联的jar包也要发布），并且需要分发到其他节点中
 */
public class InsertCalleeCoprocessor extends BaseRegionObserver {
    //方法的命名规则
    //login
    //logout
    //prePut
    //doPut : 模板方法设计模式
    //     存在父子类
    //      put父类搭建算法的框架，例如：1.tel取用户代码；   2.年月日；   3.异或运算；     4.hash散列
    //      do子类重写算法的细节，例如：1.取tel的前四位；   3.取年月；   3.哈希异或；     4.异或结果%分区数
    //postPut

    /**
     * 保存主叫用户数据之后，由Hbase自动保存被叫用户数据
     * @param e
     * @param put
     * @param edit
     * @param durability
     * @throws IOException
     */
    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
        //获取表
        Table table = e.getEnvironment().getTable(TableName.valueOf(Names.TABLE.getValue()));

        //通过主叫用户的rowkey定位提交后的数据内容
        String rowkey = Bytes.toString(put.getRow());

        //提取主教用户rowkey中的内容
        String[] values = rowkey.split("_");

        //构建表元素
        CoprocessorDao dao = new CoprocessorDao();
        String call1 = values[1];
        String call2 = values[3];
        String calltime = values[2];
        String duration = values[4];
        String flg = values[5];

        //api-触发器构建：判断当前插入的数据是否是主叫数据，即只有主叫用户的数据才需要保存一份被叫数据
        if ("1".equals(flg)){
            //构建行键
            String calleerowKey = dao.getRegionNumRef(call2, calltime) + "_" + call2 + "_" + calltime + "_" + call1 + "_" + duration + "_0";

            //加载数据
            Put calleePut = new Put(Bytes.toBytes(calleerowKey));
            byte[] calleefamily = Bytes.toBytes(Names.CF_CALLEE.getValue());
            calleePut.addColumn(calleefamily, Bytes.toBytes("call1"), Bytes.toBytes(call2));
            calleePut.addColumn(calleefamily, Bytes.toBytes("call2"), Bytes.toBytes(call1));
            calleePut.addColumn(calleefamily, Bytes.toBytes("calltime"), Bytes.toBytes(calltime));
            calleePut.addColumn(calleefamily, Bytes.toBytes("duration"), Bytes.toBytes(duration));
            calleePut.addColumn(calleefamily, Bytes.toBytes("flg"), Bytes.toBytes("0"));

            //提交保存数据
            table.put(calleePut);

            //关闭表
            table.close();
        }



    }

    private class CoprocessorDao extends BaseDao{
        public int getRegionNumRef(String tel, String time){
            return getRegionNum(tel, time);
        }
    }

}
