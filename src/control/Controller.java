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

import dao.CameraDao;
import dao.ImageDao;
import dao.PrivilegeDao;
import dao.UserDao;
import model.Camera;
import model.Image;
import model.Privilege;
import model.User;

public class Controller implements Job {

	final UserDao userDao = new UserDao();
	final PrivilegeDao privilegeDao = new PrivilegeDao();
	final String PATH = "C:/wai/images/"; 
	
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
		
		String path = generatePath(camera, image.getDate());
		String pathThumbnail = generatePathThumbnail(camera, image.getDate());
		
		String name = generateName(image.getDate());

		File file = new File(PATH + pathThumbnail);
		file.mkdirs();		
		
		URL image_url = new URL(camera.getUrl());
		
		input = new BufferedInputStream(image_url.openStream());
		output = new FileOutputStream(PATH+path+name);
		
		BufferedImage bufferedImage = ImageIO.read(input);
		ImageIO.write(bufferedImage, "jpg", output);
		
		outputthumb = new FileOutputStream(PATH + pathThumbnail + name);
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
	
	public String generatePath(Camera camera, Timestamp date) throws IOException {
		
		if(camera == null)
			throw new IllegalArgumentException("camera can not be null");
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		String year = Integer.toString(calendar.get(Calendar.YEAR));
		String month = Integer.toString(calendar.get(Calendar.MONTH)+1);
		String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
		
		if((calendar.get(Calendar.MONTH)) < 10)
			month = "0" + month;
		if(calendar.get(Calendar.DAY_OF_MONTH) < 10)
			day = "0" + day;	
		
		return  "cam_" + camera.getId() + "/" + year + "_" + month + "_" + day + "/";
	}
	
	public String generatePathThumbnail(Camera camera, Timestamp date) throws IOException {
		
		if(camera == null)
			throw new IllegalArgumentException("camera can not be null");
		return  generatePath(camera, date) + "/thumbnail/";
	}
	
	public void deleteDirectory(Camera camera) throws IOException {
		
		String path = PATH + camera.getId();
		File dir = new File(path);
		
		FileUtils.deleteDirectory(dir);
	}
	
	public List<Privilege> getPrivilegeList() {
				
		List<User> user = userDao.list();
		List<Privilege> privileges = new ArrayList<Privilege>();
		
		for(int i = 0; i< user.size(); i++) {
			List<Long> cameras = privilegeDao.listPrivileges(user.get(i));
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
			
			Controller core = new Controller();
			CameraDao camdao = new CameraDao();
			ImageDao imadao = new ImageDao();
			
			List<Camera>cameralist = camdao.list();
			for(int i = 0; i< cameralist.size(); i++) 
			{
				imadao.save(core.saveImage(cameralist.get(i)));
			}
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
