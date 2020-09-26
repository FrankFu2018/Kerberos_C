//AS发送给客户端的信息（AS通过客户端的密钥进行加密）
import java.io.Serializable;

public class AS_C implements Serializable{

	byte[] Keyct,IDtgs,TS2,Lifetime2;
	Ticket_tgs Tickettgs;
	
	public AS_C(byte[] Keyct,byte[] IDtgs,byte[] TS2,byte[] Lifetime2,Ticket_tgs Tickettgs)
	{
		this.Keyct = Keyct;
		this.IDtgs = IDtgs;
		this.TS2 = TS2;
		this.Lifetime2 = Lifetime2;
		this.Tickettgs = Tickettgs;
	}
	
	public byte[] getIDtgs() {
		return this.IDtgs;
	}
	
	public byte[] getTS2() {
		return this.TS2;
	}
	
	public byte[] getLifetime2() {
		return this.Lifetime2;
	}
	
	public byte[] getKeyct() {
		return this.Keyct;
	}
	
	public Ticket_tgs getTickettgs() {
		return this.Tickettgs;
	}
	
}