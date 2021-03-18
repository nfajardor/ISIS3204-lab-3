package servidor;

import java.net.*;
import java.util.ArrayList;

import cliente.Client;

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
	
	public void envioMultiplesClientes(int cantidadClientes, String archivo) {
		for(int i = 0;i<cantidadClientes;i++) {
			Client cli = new Client(archivo);
			cli.start();
		}
	}
	
}

