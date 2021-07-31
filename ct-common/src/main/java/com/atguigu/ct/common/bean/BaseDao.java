package com.atguigu.ct.common.bean;

import com.atguigu.ct.common.api.Column;
import com.atguigu.ct.common.api.Rowkey;
import com.atguigu.ct.common.api.TableRef;
import com.atguigu.ct.common.constant.Names;
import com.atguigu.ct.common.constant.ValueConstant;
import com.atguigu.ct.common.util.DateUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


/**
 * 基础数据访问对象
 */
public abstract class BaseDao {
    //通过建立一个线程Connection连接对象，保证在多线程任务中，Connection可以被重用，以减少资源消耗
    private ThreadLocal<Connection> connHolder = new ThreadLocal<Connection>();

    //通过建立一个线程Admin的HBase表对象，保证在多线程任务中，Admin可以被重用，以减少资源消耗
    private ThreadLocal<Admin> adminHolder = new ThreadLocal<Admin>();

    //构建HBase连接
    protected void start() throws IOException {
        getConnection();
        getAdmin();
    }

    //关闭HBase连接
    protected void end()throws IOException {
        Admin admin = getAdmin();
        if (admin != null){
            admin.close();
            adminHolder.remove();
        }

        Connection conn = getConnection();
        if (conn != null){
            conn.close();
            connHolder.remove();
        }
    }

    /**
     * 获取连接对象
     * 添加protected，是为了让其子类使用，其他的不允许是有这个类
     * 添加synchronized，是为了保证这个类的在实行时不会被重复执行
     */
    protected synchronized Admin getAdmin() throws IOException {
        Admin admin = adminHolder.get();
        if (admin == null){
            admin = getConnection().getAdmin();
            //添加到线程中
            adminHolder.set(admin);
        }

        return admin;
    }

    /**
     * 获取连接对象
     * 添加protected，是为了让其子类使用，其他的不允许是有这个类
     * 添加synchronized，是为了保证这个类的在实行时不会被重复执行
     */
    protected synchronized Connection getConnection() throws IOException {
        Connection conn = connHolder.get();
        if (conn == null){
            Configuration conf = HBaseConfiguration.create();
            conn = ConnectionFactory.createConnection(conf);
            //添加到线程
            connHolder.set(conn);
        }

        return conn;
    }

    /**
     * 创建命名空间，如果命名空间已经存在，不需要创建，否则，创建新的命名空间
     * @param namespace
     */
    protected void createNamespaceNX(String namespace) throws IOException {
        Admin admin = getAdmin();
        try{
            admin.getNamespaceDescriptor(namespace);
        } catch (NamespaceNotFoundException e){
            NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(namespace).build();
            admin.createNamespace(namespaceDescriptor);
            return;
        }
        System.out.println("命名空间已存在！");
    }

    /**
     * 创建表，如果表已经存在，则删除表，再创建一个新的表，否则直接创建一个新的表
     * @param table
     * @param columnFamilys
     * @throws IOException
     */
    protected void createTableXX(String table, String... columnFamilys) throws IOException {
        createTable(table, null, null, columnFamilys);
    }

    protected void createTableXX(String table, String coprocessorClass, Integer regionCount, String... columnFamilys) throws IOException {
        //格式转换，获取表
        Admin admin = getAdmin();
        TableName tableName = TableName.valueOf(table);
        if(admin.tableExists(tableName)){
            //表存在，删除表
            deleteTable(table);
        };
        //创建表
        createTable(table, coprocessorClass, regionCount, columnFamilys);
    }

    /**
     * 创建表-参数：1. 表名；2. 列族名（支持多个传入）
     * @param table
     * @param regionCount
     * @param columnFamilys
     * @throws IOException
     */
    protected void createTable(String table, String coprocessorClass, Integer regionCount, String... columnFamilys) throws IOException{
        Admin admin = getAdmin();
        TableName tableName = TableName.valueOf(table);
        HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
        if (columnFamilys == null || columnFamilys.length == 0){
            columnFamilys = new String [1];
            columnFamilys[0] = Names.CF_INFO.getValue();
        }
        for (String columnFamily : columnFamilys){
            HColumnDescriptor columnDescriptor = new HColumnDescriptor(columnFamily);
            hTableDescriptor.addFamily(columnDescriptor);
        }

        if (coprocessorClass != null && !"".equals(coprocessorClass)){
            hTableDescriptor.addCoprocessor(coprocessorClass);
        }

        //增加预分区：用于优化数据倾斜问题
        if(regionCount == null || regionCount <= 0){
            admin.createTable(hTableDescriptor);
        }else{
            //分区键
            byte[][] splitKeys = getSplitKeys(regionCount);
            admin.createTable(hTableDescriptor, splitKeys);
        }
    }

    /**
     * 按月-获取查询时的StartRow，StopRow集合
     * @return
     */
    protected List<String[]> getStartStopRowkey(String tel, String start, String end) throws ParseException {
        List<String[]> rowkeyss = new ArrayList<String[]>();

        //获取查询的基本要求及必要信息
        tel = tel.substring(tel.length()-4);
        String startTime = start.substring(0, 6);
        String endTime = end.substring(0, 6);

        //构建一个Calender日历类，用于迭代不同月的数据请求
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(DateUtil.parse(startTime, "yyyyMM"));
        Calendar endCal = Calendar.getInstance();
        startCal.setTime(DateUtil.parse(endTime, "yyyyMM"));
        while(startCal.getTimeInMillis() <= endCal.getTimeInMillis()){
            //迭代统计的当前时间
            String nowTime = DateUtil.format(startCal.getTime(), "yyyyMM");

            //获取当前行分区号
            int regionNum = getRegionNum(tel, nowTime);

            //构建要获取的集合的始末行键，指定获取范围内的集合1_134_20180103 ~1_134_20180103|
            String startRow = regionNum + "_" + tel + "_" + startTime;
            String stopRow = startRow+"|";
            String[] rowkeys = {startRow, stopRow};
            rowkeyss.add(rowkeys);

            //月份+1，用于做迭代
            startCal.add(Calendar.MONTH, 1);
        }
        return rowkeyss;
    }

    /**
     * 自定义分区号计算
     * @param tel
     * @param date
     * @return
     */
    protected int getRegionNum(String tel, String date){
        //133+0123+4567
        String usercode = tel.substring(tel.length()-4);
        //20180101000000
        String yearMonth = date.substring(0, 6);

        int userCodeHash = usercode.hashCode();

        int yearMothHash = yearMonth.hashCode();

        //crc校验采用异或算法，hash
        int crc = Math.abs(userCodeHash ^ yearMothHash);

//        System.out.println("crc:"+crc+"\tREGION_COUNT:"+ValueConstant.REGION_COUNT);

        //取模
        int regionNum = crc % ValueConstant.REGION_COUNT;

        //返回分区号
        return regionNum;

    }

    /**
     * 生成分区键
     * 分区的目的是为了解决数据倾斜的问题，通过分区，使各个工作节点的工作尽可能达到一个“负载均衡”
     * @param regionCount
     * @return
     */
    private byte[][] getSplitKeys(Integer regionCount){
        int splitKeyCount = regionCount - 1;
        byte[][] bs = new byte[splitKeyCount][];
        //0|, 1|, 2|, 3|, 4|
        //(-∞, 0|), [0|, 1|), [1| +∞)               //为什么用竖线：因为区间的划分如果纯粹是数字那局限性一定很大，所以为了可以支持各种复杂的分区键的同时又要保证分区键是以1-n纯整数划分的，则可以选择一个ASCLL大一点的字符，竖线|是ASCLL码中第二大的值比它小的值它都可以接收
        List<byte[]> bsList = new ArrayList<byte[]>();
        for (int i=0; i<splitKeyCount; i++){
            String splitkey=i+"|";
            bsList.add(Bytes.toBytes(splitkey ));
        }
        //对分区键进行排序
        Collections.sort(bsList, new Bytes.ByteArrayComparator());
        bsList.toArray(bs);
        return bs;
    }

    /**
     * 增加对象，自动封装数据，将对象数据保存到HBase中
     * @param obj
     * @throws Exception
     */
    protected void putData(Object obj) throws Exception{
        //反射-获取表名称
        Class clazz = obj.getClass();
        TableRef tableRef = (TableRef)clazz.getAnnotation(TableRef.class);
        String tableName = tableRef.value();

        //获取对象的各个属性值
        Field[] fs = clazz.getDeclaredFields();
        String stringRowkey="";
        for(Field f : fs){
            Rowkey rowkey = f.getAnnotation(Rowkey.class);
            if(rowkey != null){
                f.setAccessible(true);
                stringRowkey = (String)f.get(obj);
                break;
            }
        }

        //获取表对象
        Connection conn = getConnection();
        Table table = conn.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(stringRowkey));

        for(Field f : fs){
            //获取f属性类的的Column列注解（自定义的注解，里面包含了我们需要的列族和列限定符）
            Column column = f.getAnnotation(Column.class);
            if(column != null){
                //获取列族
                String family = column.family();
                //获取列限定符
                String colName = column.column();
                if (colName == null || "".equals(colName)){
                    colName = f.getName();
                }
                //关闭安全检查-我们需要对私有字段进行操作
                f.setAccessible(true);
                //获取当前field字段值
                String value = (String)f.get(obj);
                //给put压入要插入的值
                put.addColumn(Bytes.toBytes(family), Bytes.toBytes(colName), Bytes.toBytes(value));
            }
        }

        //增加数据
        table.put(put);

        //关闭表
        table.close();
    }

    /**
     * 增加数据
     * @param name
     * @param put
     * @throws IOException
     */
    protected void putData(String name, Put put) throws IOException {
        //获取表对象
        Connection conn = getConnection();
        Table table = conn.getTable(TableName.valueOf(name));

        //增加数据
        table.put(put);

        //关闭表
        table.close();
    }

    /**
     * 增加多条数据
     * @param name
     * @param puts
     * @throws IOException
     */
    protected void putData(String name, List<Put> puts) throws IOException {
        //获取表对象
        Connection conn = getConnection();
        Table table = conn.getTable(TableName.valueOf(name));

        //增加数据
        table.put(puts);

        //关闭表
        table.close();
    }

    /**
     * 删除表-参数：1.表名
     * @param table
     * @throws IOException
     */
    protected void deleteTable(String table) throws IOException {
        //格式转换，获取表
        TableName tableName = TableName.valueOf(table);
        Admin admin = getAdmin();

        //禁用表
        admin.disableTable(tableName);

        //删除表
        admin.deleteTable(tableName);

        System.out.println("删除成功！");

    }

//    public static void main(String[] args) throws ParseException {
//        System.out.println(getSplitKeys(6));
//        System.out.println(genRegionNum("13533333327", "20180607124523"));
//        System.out.println("-----------------\n"+Math.abs(4^1));
//        for (String[] strings : getStartStopRowkey("13321564628", "201803", "201808")) {
//            /**
//             * 133_201803~133_201803|
//             * 133_201804~133_201804|
//             * 133_201805~133_201805|
//             * 133_201806~133_201806|
//             * 133_201807~133_201807|
//             * 133_201808~133_201808|
//             */
//            System.out.println(strings[0] + "~" + strings[1]);
//        }
//    }

}
