package basic;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class ByteOps {
	public static byte[] intToByteArray1(int i) {  
        byte[] result = new byte[4];  
        result[0] = (byte)((i >> 24) & 0xFF);  
        result[1] = (byte)((i >> 16) & 0xFF);  
        result[2] = (byte)((i >> 8) & 0xFF);  
        result[3] = (byte)(i & 0xFF);  
        return result;  
    }  
  
    public static byte[] intToByteArray2(int i) throws Exception {  
        ByteArrayOutputStream buf = new ByteArrayOutputStream();  
        DataOutputStream out = new DataOutputStream(buf);  
        out.writeInt(i);  
        byte[] b = buf.toByteArray();  
        out.close();  
        buf.close();  
        return b;  
    }  
  
    public static int byteArrayToInt(byte[] b) {  
        int intValue=0;  
        for(int i=0;i<b.length;i++){  
           intValue +=(b[i] & 0xFF)<<(8*(3-i));  
        }  
        return intValue;  
    }  
}
