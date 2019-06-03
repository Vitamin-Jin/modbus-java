package com.rib.modbus;

import java.util.Arrays;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.NumericLocator;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;

public class Test2 {
     public static void main(String[] args) throws Exception {
     ModbusFactory factory = new ModbusFactory();
     IpParameters params = new IpParameters();

     params.setHost("192.168.0.250");
     params.setPort(502);
     params.setEncapsulated(false);

     ModbusMaster master = factory.createTcpMaster(params, true);
     // master.setRetries(4);
     master.setTimeout(2000);
     master.setRetries(0);

     long start = System.currentTimeMillis();
     try {
     master.init();
     for (int i = 0; i < 10; i++) {
     System.out.println(master.getValue(new NumericLocator(1,
     RegisterRange.HOLDING_REGISTER, 2,
     DataType.TWO_BYTE_INT_SIGNED)));

     Thread.sleep(800);
     }

     try {
     ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(
     1, 0, 100);
     ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse)
     master
     .send(request);

     if (response.isException()) {
     System.out.println("Exception response: message="
     + response.getExceptionMessage());
     } else {
     System.out
     .println(Arrays.toString(response.getShortData()));
     }

     } catch (ModbusTransportException e) {
     e.printStackTrace();
     }

     } finally {
     master.destroy();
     }

     System.out.println("Took: " + (System.currentTimeMillis() - start)
     + "ms");
     }

}
