import java.io.*;
import java.nio.ByteBuffer;  

public class Type_Switch implements Serializable{
	//byte数组与 int的相互转换  
	public int byteArrayToInt(byte[] b) {
	    return   b[3] & 0xFF |
	            (b[2] & 0xFF) << 8 |
	            (b[1] & 0xFF) << 16 |
	            (b[0] & 0xFF) << 24;
	}
	
	public byte[] intToByteArray(int a) {
	    return new byte[] {
	        (byte) ((a >> 24) & 0xFF),
	        (byte) ((a >> 16) & 0xFF),
	        (byte) ((a >> 8) & 0xFF),
	        (byte) (a & 0xFF)
	    };
	}
	
	private static ByteBuffer buffer = ByteBuffer.allocate(8);
	
	//byte 数组与 long 的相互转换
	public byte[] longToBytes(long x) {
		buffer.clear();
	    buffer.putLong(0, x);	    
	    return buffer.array();
	}
	  
	public long bytesToLong(byte[] bytes) {
		buffer.clear();
	    	    
		buffer.put(bytes, 0, bytes.length);	   
		buffer.flip();
	    return buffer.getLong();
	    
	}
	
}