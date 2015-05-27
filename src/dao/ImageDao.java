package dao;

import exception.DataBaseAccessException;
import exception.ImageNotSavedException;
import model.Image;

public class ImageDao extends Dao {
	
	private final String SQL_ADD_NEW_IMAGE = "insert into public.images (cameraID, date) values (?,?)";
	
	public void saveImage(Image image)
	{
		if (image == null)
			throw new IllegalArgumentException("image can not be null");
		
		try
		{
			Object[] values = new Object[2];
			values[0] = image.getCameraID();
			values[1] = image.getDate();
			
			executeUpdate(SQL_ADD_NEW_IMAGE, values);
		}
		catch (DataBaseAccessException e)
		{
			throw new ImageNotSavedException();
		}
		finally
		{
			closeConnectionToDB();
		}
	}	
}
