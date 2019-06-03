package com.rib.modbus;

import java.util.Arrays;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;

public class WriteToModbus {
    // 模擬器地址
    private final static int SLAVE_ADDRESS = 1;

    public static void main(String[] args) throws Exception {
        ModbusFactory factory = new ModbusFactory();
        IpParameters params = new IpParameters();
        params.setHost("192.168.0.250");
        params.setPort(502);
        params.setEncapsulated(false);

        ModbusMaster master = factory.createTcpMaster(params, true);
        //
        short[] d = new short[10];
        d[0] = 0;
        d[1] = 0;
        d[2] = 1;
        d[3] = 1;
        d[4] = 1;
        d[5] = 1;
        d[6] = 1;
        d[7] = 1;
        d[8] = 1;
        d[9] = 0;
        // master.setRetries(4);
        master.setTimeout(20000);
        master.setRetries(0);
        try {
            // 设备初始化
            master.init();
            boolean se = master.isConnected();
            if (se) {
                System.out.println("获取连接状态" + se);
            }
            // readDiscreteInputTest(master, SLAVE_ADDRESS, 0, 8);
            // writeRegistersTest(master, SLAVE_ADDRESS, 0, new short[] { 0x31,
            // 0xb, 0xc, 0xd, 0xe,
            // 0x9, 0x8, 0x7, 0x6 });
            writeRegistersTest(master, SLAVE_ADDRESS, 0, d);
        } catch (Exception e) {
            e.getMessage();
        } finally {
            master.destroy();
        }
    }

    /**
     * 读开关量型的输入信号
     * 
     * @param master
     *            主站
     * @param slaveId
     *            从站地址
     * @param start
     *            起始偏移量
     * @param len
     *            待读的开关量的个数
     */
    private static void readDiscreteInputTest(ModbusMaster master, int slaveId, int start, int len) {
        try {
            ReadDiscreteInputsRequest request = new ReadDiscreteInputsRequest(slaveId, start, len);
            ReadDiscreteInputsResponse response = (ReadDiscreteInputsResponse) master.send(request);
            if (response.isException())
                System.out.println("Exception response: message=" + response.getExceptionMessage());
            else
                System.out.println(Arrays.toString(response.getBooleanData()));
        } catch (ModbusTransportException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读保持寄存器上的内容
     * 
     * @param master
     *            主站
     * @param slaveId
     *            从站地址
     * @param start
     *            起始地址的偏移量
     * @param len
     *            待读寄存器的个数
     */
    private static void readHoldingRegistersTest(ModbusMaster master, int slaveId, int start,
            int len) {
        try {
            ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, start,
                    len);
            ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master
                    .send(request);
            if (response.isException())
                System.out.println("Exception response: message=" + response.getExceptionMessage());
            else
                System.out.println(Arrays.toString(response.getShortData()));
        } catch (ModbusTransportException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量写数据到保持寄存器
     * 
     * @param master
     *            主站
     * @param slaveId
     *            从站地址
     * @param start
     *            起始地址的偏移量
     * @param values
     *            待写数据
     */
    public static void writeRegistersTest(ModbusMaster master, int slaveId, int start,
            short[] values) {
        try {
            WriteRegistersRequest request = new WriteRegistersRequest(slaveId, start, values);
            WriteRegistersResponse response = (WriteRegistersResponse) master.send(request);
            if (response.isException())
                System.out.println("Exception response: message=" + response.getExceptionMessage());
            else {
                System.out.println("Success");
            }
        } catch (ModbusTransportException e) {
            e.printStackTrace();
        }
    }
}