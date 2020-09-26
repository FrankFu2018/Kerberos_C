//TGS的票据(由AS生成,由客户端发送给TGS)
import java.io.*;

public class Ticket_tgs implements Serializable{
	private static final long serialVersionUID = 1;
	byte[] Keyct,IDc, ADc,IDtgs,TS2,Lifetime2;
	public Ticket_tgs(byte[] Keyct,byte[] IDc,byte[] ADc,byte[] IDtgs,byte[] TS2,byte[] Lifetime2){
		this.Keyct=Keyct;
		this.IDc = IDc;
		this.ADc = ADc;
		this.IDtgs = IDtgs;
		this.TS2 = TS2;
		this.Lifetime2 = Lifetime2;
	}
	
	public byte[] getKeyct()
	{
		return this.Keyct;
	}
	
	public byte[] getIDc()
	{
		return this.IDc;
	}
	
	public byte[] getADc()
	{
		return this.ADc;
	}
	
	public byte[] getIDtgs()
	{
		return this.IDtgs;
	}
	
	public byte[] getTS2()
	{
		return this.TS2;
	}
	
	public byte[] getLifetime2()
	{
		return this.Lifetime2;
	}
	
}