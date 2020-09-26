//解密后的TGS发给客户端的数据
import java.io.Serializable;

public class Decrypt_TGS_C implements Serializable{
	byte[] Keycv;
	String IDv;
	long TS4;
	Ticket_v tv;
	
	public Decrypt_TGS_C(byte[] Keycv,String IDv, long TS4,Ticket_v tv)
	{
		this.Keycv=Keycv;
		this.IDv = IDv;
		this.TS4 = TS4;
		this.tv = tv;
	}
	
	public byte[] getKeycv() {
		return this.Keycv;
	}
	
	public String getIDv() {
		return this.IDv;
	}
	
	public long getTS4() {
		return this.TS4;
	}
	
	public Ticket_v getTicket_v() {
		return this.tv;
	}
	
}
