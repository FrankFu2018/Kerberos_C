//客户端发送给AS的数据（未加密）
import java.io.Serializable;

public class C_AS implements Serializable{
	String IDc,IDtgs;
	long TS1;
	
	public C_AS(String IDc,String IDtgs,long TS1)
	{
		this.IDc=IDc;
		this.IDtgs=IDtgs;
		this.TS1=TS1;
	}
	
	public String getIDc() {
		return this.IDc;
	}
	
	public String getIDtgs() {
		return this.IDtgs;
	}
	
	public long getTS1() {
		return this.TS1;
	}
	
}