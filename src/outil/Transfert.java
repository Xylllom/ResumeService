package outil;

import java.lang.management.RuntimeMXBean;

public class Transfert {

	// RuntimeMXBean run = ManagementFactory.getRuntimeMXBean()
	/*
	 * Retourne le PID d'une instance main
	 * 
	 */
	public Long getPID(RuntimeMXBean run){
		String name = run.getName();
		return Long.valueOf(name.split("@")[0]);
	}

}
