package control;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.tomcat.util.http.fileupload.FileUtils;

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
		
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		Image image = new Image();
		image.setCameraID(camera.getId());
		image.setDate(new Timestamp(new Date().getTime()));				
		String path = getPath(image.getCameraID(), image.getDate());		
		image.setPath(path);
		
		try	{
			URL image_url = new URL(camera.getUrl());
			
			input = new BufferedInputStream(image_url.openStream());
			File file = new File(path);
			file.mkdirs();
			output = new FileOutputStream(path);
			
			byte data[] = new byte[1024];
			int count;
			while ((count = input.read(data, 0, 1024)) != -1) {
				output.write(data, 0, count);
            }
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            if (input != null)
            	input.close();
            if (output != null)
            	output.close();
		}
		return image;
	}
	
	public String getPath(Long cameraID, Timestamp date) throws IOException {
		
		if(date == null || cameraID == null)
			throw new IllegalArgumentException("date or cameraID can not be null");
			
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		String dir = this.getClass().getClassLoader().getResource("").getPath();
		String delete = "/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/wai_webcam/WEB-INF/classes/";
		
		dir = dir.replace(delete, "");
		
		String month = Integer.toString(calendar.get(Calendar.MONTH)+1);
		String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
		String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
		String min = Integer.toString(calendar.get(Calendar.MINUTE));
		
		if((calendar.get(Calendar.MONTH)+1) < 10)
			month = "0" + (calendar.get(Calendar.MONTH)+1);
		if(calendar.get(Calendar.DAY_OF_MONTH) < 10)
			day = "0" + calendar.get(Calendar.DAY_OF_MONTH);		
		
		String path = dir + "//images//cam_" + cameraID.toString() + "//" + calendar.get(Calendar.YEAR) + "//"
				+ month + "//" + day + "//" + hour + "_" + min + ".jpg";				
				
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
