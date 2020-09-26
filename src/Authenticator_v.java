//客户端发送给应用服务器的认证信息
import java.io.*;
public class Authenticator_v implements Serializable{

	byte[] IDc,ADc,TS5;
	public Authenticator_v(byte[] IDc,byte[] ADc,byte[] TS5)
	{
		this.IDc = IDc;
		this.ADc = ADc;
		this.TS5 = TS5;
	}
	
	public byte[] getTS5()
	{
		return this.TS5;
	}
	public byte[] getIDc()
	{
		return this.IDc;
	}
	public byte[] getADc()
	{
		return this.ADc;
	}
}