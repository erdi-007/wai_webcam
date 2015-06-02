package control;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.imgscalr.Scalr;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import dao.PrivilegeDao;
import dao.UserDao;
import model.Camera;
import model.Image;
import model.Privilege;
import model.User;

public class Controller implements Job {

	final UserDao userDao = new UserDao();
	final PrivilegeDao privilegeDao = new PrivilegeDao();
	final String PATH = "C:/wai/images/cam_"; 
	
	public Image saveImage(Camera camera) throws IOException
	{
		BufferedInputStream input = null;
		FileOutputStream output = null;
		FileOutputStream outputthumb =null;
		
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		Image image = new Image();
		image.setCameraID(camera.getId());
		image.setDate(new Timestamp(new Date().getTime()));	
		
		String path = generatePath(camera);
		String pathThumbnail = generatePathThumbnail(camera);
		
		String name = generateName(image.getDate());

		File file = new File(pathThumbnail);
		file.mkdirs();		
		
		URL image_url = new URL(camera.getUrl());
		
		input = new BufferedInputStream(image_url.openStream());
		output = new FileOutputStream(path+name);
		
		BufferedImage bufferedImage = ImageIO.read(input);
		ImageIO.write(bufferedImage, "jpg", output);
		
		outputthumb = new FileOutputStream(pathThumbnail + name);
		BufferedImage thumbnail = Scalr.resize(bufferedImage, 100);
		ImageIO.write(thumbnail, "jpg", outputthumb);
		
		if (input != null)
    	input.close();
	    if (output != null)
	    	output.close();
	    if (outputthumb != null)
	    	outputthumb.close();
			return image;
		
	}
	
	public String generateName(Timestamp date) {
		
		String name;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
		String min = Integer.toString(calendar.get(Calendar.MINUTE));
		
		if((calendar.get(Calendar.HOUR_OF_DAY)) < 10)
			hour = "0" + hour;
		if(calendar.get(Calendar.MINUTE) < 10)
			min = "0" + min;	
		
		name = hour + "_" + min + ".jpg";
		
		return name;
	}
	
	public String generatePath(Camera camera) throws IOException {
		
		if(camera == null)
			throw new IllegalArgumentException("camera can not be null");
		return  PATH + camera.getId() + "/";
	}
	
	public String generatePathThumbnail(Camera camera) throws IOException {
		
		if(camera == null)
			throw new IllegalArgumentException("camera can not be null");
		return  PATH + camera.getId() + "/thumbnail/";
	}
	
	public void deleteDirectory(Camera camera) throws IOException {
		
		String path = generatePath(camera);
		File dir = new File(path);
		
		FileUtils.deleteDirectory(dir);
	}
	
	public List<Privilege> getPrivilegeList() {
				
		List<User> user = userDao.getListOfAllUsers();
		List<Privilege> privileges = new ArrayList<Privilege>();
		
		for(int i = 0; i< user.size(); i++) {
			List<Long> cameras = privilegeDao.getPrivilegesForUser(user.get(i).getId());
			for(int j = 0; j < cameras.size(); j++) {
				Privilege privilege = new Privilege();
				privilege.setCameraId(cameras.get(j));
				privilege.setUserId(user.get(i).getId());
				privileges.add(privilege);
			}
		}
		
		return privileges;
	}
	
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			System.out.println("Timer geht!!!!!!");
			/*Controller core = new Controller();
			List<Camera> cameralist =CameraDao.getListOfAllCameras();
			for(int i = 0; i< cameralist.size(); i++) {
				dao.save(core.saveImage(cameralist.get(i)));*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}