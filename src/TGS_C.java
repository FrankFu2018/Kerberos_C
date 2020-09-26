//TGS发送给客户端的消息
import java.io.Serializable;
public class TGS_C implements Serializable{
	byte[] Keycv,IDv,TS4;
	Ticket_v tv;
	
	public TGS_C(byte[] Keycv,byte[] IDv, byte[] TS4,Ticket_v tv)
	{
		this.Keycv=Keycv;
		this.IDv = IDv;
		this.TS4 = TS4;
		this.tv = tv;
	}
	
	public byte[] getKeycv() {
		return this.Keycv;
	}
	
	public byte[] getIDv() {
		return this.IDv;
	}
	
	public byte[] getTS4() {
		return this.TS4;
	}
	
	public Ticket_v getTicket_v() {
		return this.tv;
	}
	
}