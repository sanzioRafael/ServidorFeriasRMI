package servidor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.lang.model.SourceVersion;

public class Server extends UnicastRemoteObject implements Metodos {

	Registry registry;
	FileInputStream fis;
	FileOutputStream fos;
	private String path;

	protected Server() throws RemoteException {
		super();
	}

	public boolean startServer(String name, int port, String path) {
		this.path = path;
		try {
			Server stub = this;
			registry = LocateRegistry.createRegistry(port);
			registry.rebind(name, stub);

			return true;
		} catch (RemoteException e) {
			return false;
		}

	}

	@Override
	public String digaOla() throws RemoteException {

		return "ola tudo funfa belezinha";
	}

	@Override
	public boolean UploadFiles(byte[] bFile, String string) {
		File file = new File(path + "\\" + string);
		try {
			System.out.println(file.getPath());
			FileOutputStream fileOuputStream = new FileOutputStream(file);
			fileOuputStream.write(bFile);
			fileOuputStream.close();
			return true;
		} catch (IOException e) {
			return false;
		}

	}

	

	@Override
	public void UploadForCompact(byte[] fileToByte, String name) throws RemoteException {
		System.out.println("upCOmpact");
		File file = new File(path + "\\forZip\\" + name);
		try {
			file.createNewFile();
			System.out.println(file.getPath());
			FileOutputStream fileOuputStream = new FileOutputStream(file);
			fileOuputStream.write(fileToByte);
			fileOuputStream.close();
		} catch (IOException e) {
		}
	}

	List<String> fileList = new ArrayList<String>();
	final String SOURCE_FOLDER = path + "\\forZip";
	@Override
	public byte[] compressArchive() throws RemoteException,	IOException {
		
		byte[] buffer = new byte[1024];
		FileOutputStream fos = new FileOutputStream(SOURCE_FOLDER);
		ZipOutputStream zos = new ZipOutputStream(fos);
		
		generateFileList(new File(SOURCE_FOLDER));
		
		for (String	file : 	fileList) {
			System.out.println("Adicionado ao zip: "+file);
			ZipEntry ze = new ZipEntry(file);
			zos.putNextEntry(ze);
			
			FileInputStream in = new FileInputStream(SOURCE_FOLDER + File.separator + file);

	     	int len;
	     	while ((len = in.read(buffer)) > 0) {
	     		zos.write(buffer, 0, len);
	     	}
	     	zos.closeEntry();
	     	in.close();
		}
		return null;
	}

	private void generateFileList(File file) {
		if(file.isFile()) fileList.add(getZipEntry(file.getAbsoluteFile().toString()));
		
		if(file.isDirectory()){
			String[] subNote = file.list();
			for (String string : subNote) {
				generateFileList(new File (file, string));
			}
		}
	}

	private String getZipEntry(String string) {
		return string.substring(SOURCE_FOLDER.length()+1, string.length());
	}
		
	
	
}
