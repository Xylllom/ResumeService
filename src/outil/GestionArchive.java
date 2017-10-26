package outil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public abstract class GestionArchive {

    public static void extraire(String fichier, String dossier) throws IOException{
        File file = new File(fichier);
		File folder = new File(dossier);
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(file.getCanonicalFile())));
        ZipEntry ze = null;
        if (zis.getNextEntry() == null){
        	zis.close();
        	throw new IOException("Archive vide ou endommagée");
        }
        try {
        	while((ze = zis.getNextEntry()) != null){
        		File f = new File(folder.getCanonicalPath(), ze.getName());
            			if (ze.isDirectory()) {
            				f.mkdirs();
            				continue;
            			}
            			f.getParentFile().mkdirs();
            			OutputStream fos = new BufferedOutputStream(new FileOutputStream(f));
            			try {
            				try {
            					final byte[] buf = new byte[Reserve.BUFFER];
            					int bytesRead;
            					while (-1 != (bytesRead = zis.read(buf)))
            						fos.write(buf, 0, bytesRead);
            					}
            					finally {
            					fos.close();
            					}
            				}
            			catch (IOException e) {
            				f.delete();
            				throw new IOException("Gestion archive a rencontré un problème");
            			}
           
            		}
            	
        }
        finally {
            // fermeture de la ZipInputStream et on supprime l'archive
            zis.close();
            file.delete();
        }
    }
}