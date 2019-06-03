package com.rib.modbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.WriteCoilRequest;
import com.serotonin.modbus4j.msg.WriteCoilResponse;
import com.serotonin.modbus4j.msg.WriteCoilsRequest;
import com.serotonin.modbus4j.msg.WriteCoilsResponse;
import com.serotonin.modbus4j.msg.WriteRegisterRequest;
import com.serotonin.modbus4j.msg.WriteRegisterResponse;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;

public class Modbus4jWriteUtils {
	static Log log = LogFactory.getLog(Modbus4jWriteUtils.class);
	/**
	 * 工厂。
	 */
	static ModbusFactory modbusFactory;
	static {
		if (modbusFactory == null) {
			modbusFactory = new ModbusFactory();
		}
	}

	 /**
     * 获取master
     * 
     * @return
     * @throws ModbusInitException
     */
	public static ModbusMaster getMaster() throws ModbusInitException {
		IpParameters params = new IpParameters();
		params.setHost("127.0.0.1");
		params.setPort(502);

		ModbusMaster master = new ModbusFactory().createTcpMaster(params, true);
		master.init();

		return master;
	}

	/**
	 * 写 [01 Coil Status(0x)]写一个 function ID = 5
	 * 
	 * @param slaveId
	 *            slave的ID
	 * @param writeOffset
	 *            位置
	 * @param writeValue
	 *            值
	 * @return 是否写入成功
	 * @throws ModbusTransportException
	 * @throws ModbusInitException
	 */
	public static boolean writeCoil(int slaveId, int writeOffset, boolean writeValue)
			throws ModbusTransportException, ModbusInitException {
		// 01 Coil Status
		ModbusMaster modbusMaster = getMaster();
		WriteCoilRequest request = new WriteCoilRequest(slaveId, writeOffset, writeValue);
		WriteCoilResponse response = (WriteCoilResponse) modbusMaster.send(request);
		if (response.isException()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 写[01 Coil Status(0x)] 写多个 function ID = 15
	 * 
	 * @param slaveId
	 *            slaveId
	 * @param startOffset
	 *            开始位置
	 * @param bdata
	 *            写入的数据
	 * @return 是否写入成功
	 * @throws ModbusTransportException
	 * @throws ModbusInitException
	 */
	public static boolean writeCoils(int slaveId, int startOffset, boolean[] bdata)
			throws ModbusTransportException, ModbusInitException {
		// 获取master
		ModbusMaster tcpMaster = getMaster();
		// 创建请求
		WriteCoilsRequest request = new WriteCoilsRequest(slaveId, startOffset, bdata);
		// 发送请求并获取响应对象
		WriteCoilsResponse response = (WriteCoilsResponse) tcpMaster.send(request);
		if (response.isException()) {
			return false;
		} else {
			return true;
		}

	}

	/***
	 * 写[03 Holding Register(4x)] 写一个 function ID = 6
	 * 
	 * @param slaveId
	 * @param writeOffset
	 * @param writeValue
	 * @return
	 * @throws ModbusTransportException
	 * @throws ModbusInitException
	 */
	public static boolean writeRegister(int slaveId, int writeOffset, short writeValue)
			throws ModbusTransportException, ModbusInitException {
		// 获取master
		ModbusMaster tcpMaster = getMaster();
		// 创建请求对象
		WriteRegisterRequest request = new WriteRegisterRequest(slaveId, writeOffset, writeValue);
		WriteRegisterResponse response = (WriteRegisterResponse) tcpMaster.send(request);
		if (response.isException()) {
			log.error(response.getExceptionMessage());
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 
	 * 写入[03 Holding Register(4x)]写多个 function ID=16
	 * 
	 * @param slaveId
	 *            modbus的slaveID
	 * @param startOffset
	 *            起始位置偏移量值
	 * @param sdata
	 *            写入的数据
	 * @return 返回是否写入成功
	 * @throws ModbusTransportException
	 * @throws ModbusInitException
	 */
	public static boolean writeRegisters(int slaveId, int startOffset, short[] sdata)
			throws ModbusTransportException, ModbusInitException {
		// 获取master
		ModbusMaster tcpMaster = getMaster();
		// 创建请求对象
		WriteRegistersRequest request = new WriteRegistersRequest(slaveId, startOffset, sdata);
		// 发送请求并获取响应对象
		ModbusResponse response = tcpMaster.send(request);
		if (response.isException()) {
			log.error(response.getExceptionMessage());
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 写入数字类型的模拟量（如:写入Float类型的模拟量、Double类型模拟量、整数类型Short、Integer、Long）
	 * 
	 * @param slaveId
	 * @param offset
	 * @param value
	 *            写入值,Number的子类,例如写入Float浮点类型,Double双精度类型,以及整型short,int,long
	 * @throws ModbusTransportException
	 * @throws ErrorResponseException
	 * @throws ModbusInitException
	 */
	public static void writeHoldingRegister(int slaveId, int offset, Number value, int dataType)
			throws ModbusTransportException, ErrorResponseException, ModbusInitException {
		// 获取master
		ModbusMaster tcpMaster = getMaster();
		// 类型
		BaseLocator<Number> locator = BaseLocator.holdingRegister(slaveId, offset, dataType);
		tcpMaster.setValue(locator, value);
	}
	    /**
     * 测试
     * 
     * @param args
     */
	public static void main(String[] args) {
		try {
            // 01测试 coil 单个
//			Boolean v011 = writeCoil(1, 0, false);
//			Boolean v012 = writeCoil(1, 1, true);
//			Boolean v013 = writeCoil(1, 2, false);
//			System.out.println("v011:" + v011);
//			System.out.println("v012:" + v012);
//			System.out.println("v013:" + v013);
            //02测试 coil 批量
//			boolean t02 = writeCoils(1, 0, new boolean[] { true, false, true });
//			System.out.println("T02:" + t02);
			// 测试03
//			short v = -3;
//			boolean t03 = writeRegister(1, 0, v);
//			System.out.println("T03:" + t03);
			// 测试04
//			boolean t04 = writeRegisters(1, 0, new short[] { -3, 3, 9 });
//			System.out.println("t04:" + t04);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
