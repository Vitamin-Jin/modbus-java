/**
 * 
 */
package com.rib.modbus;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.util.queue.ByteQueue;

/**
 * @author Matt
 *
 */
public class TestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        TestCase.modbusTCP("192.168.0.205", 502, 0, 8);
		}

	public static ByteQueue modbusTCP(String ip, int port, int start,int readLenth) {  
        ModbusFactory modbusFactory = new ModbusFactory();  
        // 设备ModbusTCP的Ip与端口，如果不设定端口则默认为502
        IpParameters params = new IpParameters();  
        params.setHost(ip);  
		if (502 != port) {
            params.setPort(port);
        }// 设置端口，默认502
        ModbusMaster tcpMaster = null;  
            tcpMaster = modbusFactory.createTcpMaster(params, true);  
            try {  
                tcpMaster.init();  
                System.out.println("==============="+1111111);  
            } catch (ModbusInitException e) {  
                return null;  
            }  
            ModbusRequest modbusRequest=null;  
            try {  
            modbusRequest = new ReadHoldingRegistersRequest(1, start, readLenth);// 功能码03
            } catch (ModbusTransportException e) {  
                e.printStackTrace();  
            }  
            ModbusResponse modbusResponse=null;  
            try {  
                modbusResponse = tcpMaster.send(modbusRequest);  
            } catch (ModbusTransportException e) {  
                e.printStackTrace();  
            }  
            ByteQueue byteQueue= new ByteQueue(12);  
            modbusResponse.write(byteQueue);  
            System.out.println("功能码:" + modbusRequest.getFunctionCode());
            System.out.println("从站地址:" + modbusRequest.getSlaveId());
            System.out.println("收到的响应信息大小:" + byteQueue.size());
            System.out.println("收到的响应信息值:" + byteQueue);
            return byteQueue;  
    } 
}
