import com.avaje.ebean.Ebean;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Yaml;

import helper.MpdMonitor;

import java.util.List;

import org.bff.javampd.exception.MPDConnectionException;

/**
 * Global is instantiated by the framework when an application starts, to let
 * you perform specific tasks at start-up or shut-down.
 * @author Martin Steiger
 */
public class Global extends GlobalSettings
{
	@Override
	public void onStart(Application app)
	{
		// Check if the database is empty
		if (app.isDev() && User.count() == 0)
		{
			Logger.debug("Bootstrapping with default data");
			Ebean.save((List<?>) Yaml.load("initial-data.yml"));
		}
	}
	
	@Override
	public void onStop(Application app)
	{
		Logger.info("Disconnecting .. ");
		
		try
		{
			MpdMonitor.getInstance().stop();
		}
		catch (MPDConnectionException e)
		{
			// ignore
		}
		
		super.onStop(app);
	}
}
