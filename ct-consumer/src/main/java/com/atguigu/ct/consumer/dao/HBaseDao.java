package com.atguigu.ct.consumer.dao;

import com.atguigu.ct.common.bean.BaseDao;
import com.atguigu.ct.common.constant.Names;
import com.atguigu.ct.common.constant.ValueConstant;
import com.atguigu.ct.consumer.bean.Calllog;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * HBase的数据访问对象
 */
public class HBaseDao extends BaseDao {
    /**
     * 初始化
     */
    public void init() throws IOException {
        //构建Hbase的连接
        start();

        //创建命名空间
        createNamespaceNX(Names.NAMESPACE.getValue());

        //创建表和列族（如果表存在，则删除原有表再创建表）
        createTableXX(Names.TABLE.getValue(), "com.atguigu.ct.consumer.coprocessor.InsertCalleeCoprocessor", ValueConstant.REGION_COUNT, Names.CF_CALLER.getValue(), Names.CF_CALLEE.getValue());

        //关闭Hbase的链接
        end();
    }

    /**
     * 插入对象
     * @param log
     * @throws Exception
     */
    public void insertData(Calllog log)throws Exception{
        log.setRowKey(getRegionNum(log.getCall1(), log.getCalltime()) + "_" + log.getCall1() + "_" + log.getCall2() + "_" + log.getCalltime() + "_" + log.getDuration());
        putData(log);
    }

    /**
     * 插入数据
     * @param value
     */
    public void insertData(String value) throws IOException {
        //将通话日志保存到Hbase表中
        //1. 获取通话日志数据
        String [] values = value.split("\t");
        String call1 = values[0];
        String call2 = values[1];
        String calltime = values[2];
        String duration = values[3];

        //2. 创建数据对象
        /**
         * rowkey设计
         * 1）长度原则：最大长度为64kb，推荐长度为10~100byte；最好8的倍数（64位机器），能短则短，rowkey如果太长会影响系统性能（存储和内存优化）
         * 2）唯一原则：rowkey具备唯一性
         * 3）散列原则（重要）：处理多个分区的场景
         *  3.1）盐值散列：不能使用时间戳直接作为rowkey，设计原则就是让数据“标识”失去规律
         *          在rowkey前增加随机数
         *  3.2）字符串反转：例如反转时间戳，反转电话号码
         *       电话号码：133（运营商编码）+4868（地区编码）+4567（用户编码）
         *  3.3）计算分区号：；例如hashMap
         **/

        //主叫用户CallerPut
        //rowkey = regionNum + call1 + time + call2 + duration
        String callerrowKey = getRegionNum(call1, calltime) + "_" + call1 + "_" + calltime + "_" + call2 + "_" + duration + "_1";
        Put callerPut = new Put(Bytes.toBytes(callerrowKey));
        byte[] callerfamily = Bytes.toBytes(Names.CF_CALLER.getValue());
        callerPut.addColumn(callerfamily, Bytes.toBytes("call1"), Bytes.toBytes(call1));
        callerPut.addColumn(callerfamily, Bytes.toBytes("call2"), Bytes.toBytes(call2));
        callerPut.addColumn(callerfamily, Bytes.toBytes("calltime"), Bytes.toBytes(calltime));
        callerPut.addColumn(callerfamily, Bytes.toBytes("duration"), Bytes.toBytes(duration));
        callerPut.addColumn(callerfamily, Bytes.toBytes("flg"), Bytes.toBytes("1"));

        //被叫用户CalleePut
//        String calleerowKey = getRegionNum(call2, calltime) + "_" + call2 + "_" + calltime + "_" + call1 + "_" + duration + "_0";
//        Put calleePut = new Put(Bytes.toBytes(calleerowKey));
//        byte[] calleefamily = Bytes.toBytes(Names.CF_CALLEE.getValue());
//        calleePut.addColumn(calleefamily, Bytes.toBytes("call1"), Bytes.toBytes(call2));
//        calleePut.addColumn(calleefamily, Bytes.toBytes("call2"), Bytes.toBytes(call1));
//        calleePut.addColumn(calleefamily, Bytes.toBytes("calltime"), Bytes.toBytes(calltime));
//        calleePut.addColumn(calleefamily, Bytes.toBytes("duration"), Bytes.toBytes(duration));
//        calleePut.addColumn(calleefamily, Bytes.toBytes("flg"), Bytes.toBytes("0"));

        //3. 保存数据
        List<Put> puts = new ArrayList<Put>();
        puts.add(callerPut);
//        puts.add(calleePut);
        
        putData(Names.TABLE.getValue(), puts);
    }

}
