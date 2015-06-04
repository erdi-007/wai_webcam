package servlets;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imgscalr.Scalr;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import model.Camera;
import model.Image;
import model.User;
import dao.CameraDao;
import dao.ImageDao;
import dao.PrivilegeDao;
import dao.UserDao;
/**
 * Servlet implementation class ImageServlet
 */
@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet implements Job {
	private static final long serialVersionUID = 1L;	
	
	final CameraDao cameraDao = new CameraDao();
	final UserDao userDao = new UserDao();
	final ImageDao imageDao = new ImageDao();
	final PrivilegeDao privilegeDao = new PrivilegeDao();
	
	final String PATH = "C:/wai/images"; 
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		if(request.getParameter("date")!=null){
			
			String dateStart = request.getParameter("dateStart");
			String dateEnd = request.getParameter("dateEnd");
			String cam = request.getParameter("cameras");
			//Name wieder wegfiltern!
			cam = cam.split(":")[0];
			Camera camera = cameraDao.find(Long.valueOf(cam));
			
			List<Image> images = imageDao.find(camera, Timestamp.valueOf(dateStart),Timestamp.valueOf(dateEnd));
			
			for(int i = 0; i < images.size(); i++) {
				String path = generatePath(camera, images.get(i).getDate()) + generateName(images.get(i).getDate());
				String pathThumbnail = generatePathThumbnail(camera, images.get(i).getDate()) + generateName(images.get(i).getDate());
				
				images.get(i).setPath(path);
				images.get(i).setPathThumbnail(pathThumbnail);
			}
			
			
			String loggeduser = request.getParameter("loggeduser");
			User user = userDao.find(Long.valueOf(loggeduser));
			List<Long> cameraIds = privilegeDao.listPrivileges(user);
			List<Camera> cameras = new ArrayList<Camera>();
			for (int i = 0; i < cameraIds.size(); i++)
			{
				cameras.add(cameraDao.find(cameraIds.get(i)));
			}
			request.setAttribute("cameraList", cameras);			
			request.setAttribute("imageList", images);
			request.setAttribute("dateStart", dateStart);
			request.setAttribute("dateEnd", dateEnd);
			request.setAttribute("cam", cam);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/ImagePage.jsp");
			dispatcher.forward(request, response);
		} else {
			String loggeduser = request.getParameter("loggeduser");
			User user = userDao.find(Long.valueOf(loggeduser));
			List<Long> cameraIds = privilegeDao.listPrivileges(user);
			List<Camera> cameras = new ArrayList<Camera>();
			for (int i = 0; i < cameraIds.size(); i++)
			{
				cameras.add(cameraDao.find(cameraIds.get(i)));
			}
			request.setAttribute("cameraList", cameras);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/ImagePage.jsp");
			dispatcher.forward(request, response);
		}
		
	}
	
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
		
		return  "/cam_" + camera.getId() + "/" + year + "_" + month + "_" + day + "/";
	}
	
	public String generatePathThumbnail(Camera camera, Timestamp date) throws IOException {
		
		if(camera == null)
			throw new IllegalArgumentException("camera can not be null");
		return  generatePath(camera, date) + "/thumbnail/";
	}
	
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			System.out.println("Timer geht!!!!!!");
			
			CameraDao camdao = new CameraDao();
			ImageDao imadao = new ImageDao();
			
			List<Camera>cameralist = camdao.list();
			for(int i = 0; i< cameralist.size(); i++) 
			{
				imadao.save(saveImage(cameralist.get(i)));
			}
			} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
