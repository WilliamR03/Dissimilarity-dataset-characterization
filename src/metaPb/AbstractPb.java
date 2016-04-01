package metaPb;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.io.FileUtils;

import main.Controller;

public abstract class AbstractPb implements Serializable {
	
	public AbstractPb(int jobNum) {
		super();
		this.jobNum = jobNum;
	}

	private static final long serialVersionUID = 2252976819182407982L;
	protected int jobNum;

	/**
	 * Create dir job_jobNum and serialize the context inside
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void create() throws FileNotFoundException, IOException {
		File jobDir = new File(Controller.mainDir, "job_" + jobNum);
		FileUtils.deleteQuietly(jobDir);
		jobDir.mkdir();
		File jobFile = new File(jobDir, "job.ser");
		jobFile.delete();
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(jobFile));
		oos.writeObject(this);
		oos.close();
	}

	/**
	 * Execute the actual learning task, with outputs in dir
	 * @throws Exception 
	 */
	public abstract void work(File dir) throws Exception;

}
