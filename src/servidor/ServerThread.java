package servidor;

import java.net.*;
import java.util.Random;
import java.io.*;

public class ServerThread extends Thread{
	
	private Socket s;
	private int id;
	private Server servidor;
	
	public ServerThread(Socket sock, int i, Server serv){
		s = sock;
		id = i;
		servidor = serv;
	}
	
	public int darId() {
		return id;
	}
	
	@Override
	public void run() {
		System.out.println("Nueva conexión, soy el thread " + id);
		try {
			InputStream in = s.getInputStream();
			OutputStream out = s.getOutputStream();
			boolean fin = false;
			byte[] b = new byte[270000];
			System.out.println("Thread " + id+" va a mandar un mensaje");
			String s = "Bienvenido, soy el thread " + id + ". Por favor seleccione la opcion:\nA-Enviar Archivo de 100MB\nB-Enviar Archivo de 250MB\nC-Terminar la conexion ";
			
			//Three hand shaking
			
			/* Recepción del SYN */
			System.out.println("Se va a recibir el SYN");
			in.read(b, 0, b.length);
			String syn = new String(b);
			syn = syn.trim();
			int synint = Integer.parseInt(syn);
			
			/* Envío del SYN con el ACK */
			synint++;
			Random rand = new Random();
			int ack = rand.nextInt(9000) + 1000;
			String synack = ack+"-"+synint;
			System.out.println("Se va a enviar la confirmacion: "+synack);
			out.write(synack.getBytes());
			
			/* Recepción del último ACK */
			in.read(b,0,b.length);
			String sack = new String(b);
			sack = sack.trim();
			System.out.println("Se recibio: "+sack);
			int ack2 = Integer.parseInt(sack);
			if(!(ack2 == ack+1)) {
				String e = "Connection error";
				out.write(e.getBytes());
				throw new Exception(e);
			};
			
			String giveId = id+"";
			out.write(giveId.getBytes());
			in.read(b,0,b.length);
			
			
			while(!fin) {
				out.write(s.getBytes());
				in.read(b, 0 , b.length);
				String str = new String(b);
				if(str.contains("A")) {
					System.out.println("Se seleccionó la opción A: enviar archivo de 100 MB");
					out.write("Selecciono la opcion 1".getBytes());
				}else if(str.contains("B")) {
					System.out.println("Se seleccionó la opción B: enviar archivo de 250 MB");		
					out.write("Selecciono la opcion 1".getBytes());
				}else if(str.contains("C")) {
					out.write("Gracias, vuelva pronto".getBytes());
					fin = true;
				}else {
					out.write("Seleccione una opcion adecuada\n".getBytes());
				}
			}
			System.out.println("La conexión del thread "+ id + " ha terminado");
			in.close();
			out.close();
		}catch(Exception e) {
			System.err.println("Ha ocurrido un error: " + e.getMessage());
		}
		servidor.eliminarThreadPorId(id);
	}
}
