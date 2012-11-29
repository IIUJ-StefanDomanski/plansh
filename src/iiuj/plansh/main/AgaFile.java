package com.example.apli;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;



public class AgaFile {
	
	private String nazwa;
	private Bitmap zawartosc;
	public Bitmap getZawartosc(){
		return zawartosc;
		
	}
	public AgaFile(final String sciezka) throws IOException{
		File jakisPlik = new File(sciezka);
		zawartosc = BitmapFactory.decodeFile(jakisPlik.getAbsolutePath());
		nazwa = jakisPlik.getName();
	}
	public void zapisz() throws IOException{
		OutputStream stream = new FileOutputStream(nazwa);
	    /* Write bitmap to file using JPEG and 80% quality hint for JPEG. */
	    zawartosc.compress(CompressFormat.JPEG, 100, stream);
	}
}
