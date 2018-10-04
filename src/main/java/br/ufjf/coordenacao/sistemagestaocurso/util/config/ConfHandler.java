package br.ufjf.coordenacao.sistemagestaocurso.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;

import org.apache.commons.io.IOUtils;

public class ConfHandler {
	private static ConfHandler instance = null;
	private HashMap<String, String> confs;

	private ConfHandler() {
		try {
			final String configPath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("configPath");
			File f = new File(configPath);
			InputStream inputStream = new FileInputStream(f);
			
	        String arquivo = IOUtils.toString(inputStream);
			confs = new HashMap<String, String>();
			Pattern patternConf = Pattern.compile("(.*) = (.*)", Pattern.MULTILINE);
			Matcher conf = patternConf.matcher(arquivo);
			while (conf.find()) {
				confs.put(conf.group(1), conf.group(2));
			}
	        inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getConf(String key) {
		if (instance == null)
			instance = new ConfHandler();
		return instance.confs.get(key);
	}
}
