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

import dao.Dao;
import dao.DaoFactory;
import model.Camera;
import model.Image;
import model.Privilege;
import model.User;

public class Controller {

	final Dao dao = DaoFactory.getInstance().getDao();
	
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
		String path = getPath(image.getCameraID(), image.getDate());
		String pathThumbnail = getPathThumbnail(image.getCameraID(), image.getDate());
		
		image.setPath(path);
		image.setPathThumbnail(pathThumbnail);
		
		String dir = this.getClass().getClassLoader().getResource("").getPath();
		String delete = "/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/wai_webcam/WEB-INF/classes/";
		
		dir = dir.replace(delete, "");

		File file = new File(dir + pathThumbnail.substring(0, pathThumbnail.length()-9));
		file.mkdirs();		
		
		URL image_url = new URL(camera.getUrl());
		
		input = new BufferedInputStream(image_url.openStream());
		output = new FileOutputStream(dir+path);
		
		BufferedImage bufferedImage = ImageIO.read(input);
		ImageIO.write(bufferedImage, "jpg", output);
		
		outputthumb = new FileOutputStream(dir+pathThumbnail);
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
	
	public String getPath(Long cameraID, Timestamp date) throws IOException {
		
		if(date == null || cameraID == null)
			throw new IllegalArgumentException("date or cameraID can not be null");
			
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		

		
		String month = Integer.toString(calendar.get(Calendar.MONTH)+1);
		String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
		String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
		String min = Integer.toString(calendar.get(Calendar.MINUTE));
		
		if((calendar.get(Calendar.MONTH)+1) < 10)
			month = "0" + (calendar.get(Calendar.MONTH)+1);
		if(calendar.get(Calendar.DAY_OF_MONTH) < 10)
			day = "0" + calendar.get(Calendar.DAY_OF_MONTH);		
		
		String path = "/wai_webcam/WebContent/img/cam_" + cameraID.toString() + "/" + calendar.get(Calendar.YEAR) + "_"
				+ month + "_" + day + "/" + hour + "_" + min + ".jpg";				
				
		return path;
	}
	
	public String getPathThumbnail(Long cameraID, Timestamp date) throws IOException {
		
		if(date == null || cameraID == null)
			throw new IllegalArgumentException("date or cameraID can not be null");
			
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		String month = Integer.toString(calendar.get(Calendar.MONTH)+1);
		String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
		String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
		String min = Integer.toString(calendar.get(Calendar.MINUTE));
		
		if((calendar.get(Calendar.MONTH)+1) < 10)
			month = "0" + (calendar.get(Calendar.MONTH)+1);
		if(calendar.get(Calendar.DAY_OF_MONTH) < 10)
			day = "0" + calendar.get(Calendar.DAY_OF_MONTH);	
		
		String path = "/wai_webcam/WebContent/img/cam_" + cameraID.toString() + "/" + calendar.get(Calendar.YEAR) + "_"
				+ month + "_" + day + "/thumbnail/" + hour + "_" + min + ".jpg";				
				
		return path;
	}
	
	public void deleteDirectory(Long cameraID) throws IOException {
		
		String path = this.getClass().getClassLoader().getResource("").getPath();
		String delete = "/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/wai_webcam/WEB-INF/classes/";
		
		path = path.replace(delete, "");
		
		path = path + "//images//cam_" + cameraID;
		File dir = new File(path);
		
		FileUtils.deleteDirectory(dir);
	}
	
	public List<Privilege> getPrivilegeList() {
				
		List<User> user = dao.getUserList();
		List<Privilege> privileges = new ArrayList<Privilege>();
		
		for(int i = 0; i< user.size(); i++) {
			List<Long> cameras = dao.getPrivilegesUser(user.get(i).getId());
			for(int j = 0; j < cameras.size(); j++) {
				Privilege privilege = new Privilege();
				privilege.setCameraId(cameras.get(j));
				privilege.setUserId(user.get(i).getId());
				privileges.add(privilege);
			}
		}
		
		return privileges;
	}
}