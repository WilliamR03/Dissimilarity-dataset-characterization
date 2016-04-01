package main;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import org.apache.commons.io.FileUtils;

import metaPb.AbstractPb;

public class JobManager {
	
	public JobManager(){
	}

	public void run(int jobNum, int jobTimer) throws IOException {
		// lancer le job sur le contexte stocké
		File jobDir = new File(Controller.mainDir, "job_" + jobNum);
		File jobTxt = new File(jobDir, "job.txt");
		
		PrintWriter writer = new PrintWriter(jobTxt, "UTF-8");
		writer.println("#!/bin/bash");
		writer.println("#SBATCH --job-name=job_" + jobNum);
		writer.println("#SBATCH --output="+jobDir+"/out.txt");
		writer.println("#SBATCH --error="+jobDir+"/err.txt");
		writer.println("#SBATCH --mem=10000");
		writer.println("#SBATCH --nodes=1");
		writer.println("#SBATCH --ntasks=1");
		writer.println("#SBATCH --exclude=co2-nc08,co2-nc09");
		writer.println("#SBATCH --time=" + jobTimer);
		writer.println("");
		writer.println("ulimit -u 10240");
		writer.println("srun /logiciels/jdk1.8.0_25/bin/java -Xmx9G -XX:+UseSerialGC -cp main.jar main.JobManager " + jobNum);
		writer.close();
		//-XX:+UseSerialGC -XX:ParallelGCThreads=2
		
		String[] args = { "/bin/sh", "-c", "sbatch " + jobTxt };
		Runtime.getRuntime().exec(args);
	}

	public static void main(String[] args) throws Exception {
		/* Chargement du driver JDBC pour MySQL */
		Class.forName("com.mysql.jdbc.Driver");
		
		// execution du job
		String jobNum = args[0];
		System.out.println("manager launches job " + jobNum);
		
		File jobDir = new File(Controller.mainDir, "job_" + jobNum);
		File jobFile = new File(jobDir, "job.ser");
		ObjectInputStream ois =  new ObjectInputStream(new FileInputStream(jobFile)) ;
		AbstractPb job = (AbstractPb) ois.readObject() ;
		ois.close();
		job.work(jobDir);
		FileUtils.deleteDirectory(jobDir);
	}
	
}
