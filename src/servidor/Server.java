package servidor;

import java.net.*;
import java.util.ArrayList;

public class Server {
	
	private ArrayList<ServerThread> sts;
	private ServerSocket ss;
	public static void main(String[] args) throws Exception {
		
		Socket s;
		int c =0;
		Server serv = new Server();
		while(true) {
			s = serv.aceptarConexion();
			ServerThread st = new ServerThread(s,c, serv);
			c++;
			serv.agregarThreadALista(st);;
			st.start();
		}
		
		
		
		
		/*
		
		byte[] b = new byte[(int) ((int)100*Math.pow(2, 20))];
		fis.readNBytes(b, 0, b.length);
		OutputStream os = s.getOutputStream();
		os.write(b, 0, b.length);
		ss.close();
		fis.close();*/
	}
	
	public Server() throws Exception{
		ss = new ServerSocket(6969);
		sts = new ArrayList<ServerThread>();
	}
	
	public void agregarThreadALista(ServerThread st) {
		sts.add(st);
	}
	
	public Socket aceptarConexion() throws Exception{
		return ss.accept();
	}
	
	public void eliminarThreadPorId(int id) {
		for(int i = 0;i<sts.size();i++) {
			if(sts.get(i).darId()==id) {
				sts.remove(i);
			}
		}
	}
	
}
