package servidor;

import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Random;
import java.io.*;
import java.math.BigInteger;

public class ServerThread extends Thread{

	private Socket s;
	private int id;
	private Server servidor;
	public static final String HASHALG = "MD5";
	public static final String FOLDER_ROUTE = "C:\\Users\\Nicolás\\OneDrive - Universidad de los Andes\\UNIVERSIDAD\\2021-10\\ISIS3204 - INFRAESTRUCTURA DE COMUNICACIONES\\Laboratorios\\Lab3\\ISIS3204-lab-3\\src";
	public static final String MB100 = FOLDER_ROUTE+"\\ServerData\\100MB.test";
	public static final String MB250 = FOLDER_ROUTE+"\\ServerData\\250MB.test";
	public ServerThread(Socket sock, int i, Server serv){

		s = sock;
		id = i;
		servidor = serv;
	}

	public int darId() {
		return id;
	}

	public static String encypt(String ruta) throws Exception{
		String s = "";
		Path p;
		p = Paths.get(ruta);
		MessageDigest md = MessageDigest.getInstance(HASHALG);
		s = (new BigInteger(1,md.digest(Files.readAllBytes(p)))).toString();
		return s;
	}

	@Override
	public void run() {
		System.out.println("Nueva conexion, soy el thread " + id);
		try {
			InputStream in = s.getInputStream();
			OutputStream out = s.getOutputStream();
			FileInputStream fis;
			boolean fin = false;
			byte[] b = new byte[1024];
			System.out.println("Thread " + id+" va a mandar un mensaje");
			String s = "Bienvenido, soy el thread " + id + ". Por favor seleccione la opcion:\nA-Enviar Archivo de 100MB\nB-Enviar Archivo de 250MB\nC-Terminar la conexion ";

			//Three hand shaking

			/* Recepcion del SYN */
			System.out.println("Se va a recibir el SYN");
			in.read(b, 0, b.length);
			String syn = new String(b);
			syn = syn.trim();
			int synint = Integer.parseInt(syn);

			/* Envio del SYN con el ACK */
			synint++;
			Random rand = new Random();
			int ack = rand.nextInt(9000) + 1000;
			String synack = ack+"-"+synint;
			System.out.println("Se va a enviar la confirmacion: "+synack);
			out.write(synack.getBytes());

			/* Recepcion del último ACK */
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
					System.out.println("Se selecciono la opcion A: enviar archivo de 100 MB");
					str = "Selecciono la opcion 1";
					out.write(str.getBytes());

					in.read(b,0,b.length);
					str = new String(b);
					fis = new FileInputStream(MB100);
					if(str.contains("A")) {
						System.out.println("Se va a enviar el archivo de 100MB a un solo cliente");
						System.out.println("Se van a leer: " + fis.available());
						int hasheo;
						int c = 0;
						String llave = encypt(MB100);
						System.out.println("El hash es: " + llave);
						out.write("ENVIAR".getBytes());
						in.read(b);
						boolean confirmacion = (new String(b)).contains("S");
						while(fis.read(b) != -1 && confirmacion) {
							//System.out.println("se va a acrear el hash");
							hasheo = b.hashCode();
							//System.out.println("Se cero el hash");
							//mandar los bytes
							out.write(b);
							c++;

							//System.out.println("Se manda algo");
							//in.read(b,0,b.length);
							//System.out.println("se mando algo..."+hasheo);
							//mandar el hash
							str = hasheo+"";
							//System.out.println("se termino");
							//fis.skip(b.length);
							//System.out.println("pal siguiente");
						}
						System.out.println("Se leyo todo: "+c);
						str = "END";
						out.write(str.getBytes());

					}else if(str.contains("B")) {

					}else if(str.contains("C")) {

					}else {
						out.write("Seleccione una opcion adecuada\n".getBytes());
					}
				}else if(str.contains("B")) {
					System.out.println("Se selecciono la opcion B: enviar archivo de 250 MB");		
					out.write("Selecciono la opcion 2".getBytes());

					out.write(str.getBytes());

					in.read(b,0,b.length);
					str = new String(b);
					fis = new FileInputStream(MB250);
					if(str.contains("A")) {
						System.out.println("Se va a enviar el archivo de 250MB a un solo cliente");
						System.out.println("Se van a leer: " + fis.available());
						int hasheo;
						int c = 0;
						String llave = encypt(MB250);
						System.out.println("El hash es: " + llave);
						out.write("ENVIAR".getBytes());
						in.read(b);
						boolean confirmacion = (new String(b)).contains("S");
						while(fis.read(b) != -1 && confirmacion) {
							//System.out.println("se va a acrear el hash");
							hasheo = b.hashCode();
							//System.out.println("Se cero el hash");
							//mandar los bytes
							out.write(b);
							c++;

							//System.out.println("Se manda algo");
							in.read(b,0,b.length);
							//System.out.println("se mando algo..."+hasheo);
							//mandar el hash
							str = hasheo+"";
							//System.out.println("se termino");
							//fis.skip(b.length);
							//System.out.println("pal siguiente");
						}
						System.out.println("Se leyo todo: "+c);
						str = "END";
						out.write(str.getBytes());

					}else if(str.contains("B")) {

					}else if(str.contains("C")) {

					}else {
						out.write("Seleccione una opcion adecuada\n".getBytes());
					}

				}else if(str.contains("C")) {
					out.write("Gracias, vuelva pronto".getBytes());
					fin = true;
				}else {
					out.write("Seleccione una opcion adecuada\n".getBytes());
				}
			}
			System.out.println("La conexion del thread "+ id + " ha terminado");
			in.close();
			out.close();
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
		servidor.eliminarThreadPorId(id);
	}
}
